package uz.gita.fooddelivery.data.repository.auth

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import timber.log.Timber
import uz.gita.fooddelivery.BuildConfig.*
import uz.gita.fooddelivery.data.local.MySharedPreferences
import uz.gita.fooddelivery.data.remote.request.ProfileEditDataRequest
import uz.gita.fooddelivery.data.remote.request.RegisterDataRequest
import uz.gita.fooddelivery.data.remote.response.ProfileDataResponse
import uz.gita.fooddelivery.data.remote.response.SignInDataResponse
import uz.gita.fooddelivery.data.model.ProfileEditData
import javax.inject.Inject

class AuthRepositoryImpl
@Inject constructor(
    private val preferences: MySharedPreferences,
    private val auth: FirebaseAuth,
    private val store: FirebaseFirestore
) : AuthRepository {

    init {
        Timber.tag("AuthRepositoryImpl").d("isRemember: ${preferences.isRememberUser}")
        Timber.tag("AuthRepositoryImpl").d("currentUID: ${preferences.currentUID}")
        Timber.tag("AuthRepositoryImpl").d("currentUserPhone: ${preferences.currentUserPhone}")
    }

    // register new user
    override suspend fun registerUser(
        verificationId: String,
        smsCode: String,
        firstname: String,
        lastname: String,
        phone: String,
        password: String,
        isRemember: Boolean,
        success: () -> Unit,
        failure: (Throwable) -> Unit
    ) {
        val credential = PhoneAuthProvider.getCredential(verificationId, smsCode)
        auth
            .signInWithCredential(credential)
            .addOnSuccessListener { authResult ->
                Timber.tag("AUTH").d("signInWithCredential: isSuccessful")
                store
                    .collection(USERS)
                    .document("$firstname: $phone")
                    .set(
                        RegisterDataRequest(
                            uid = authResult.user!!.uid,
                            firstname = firstname,
                            lastname = lastname,
                            phone = phone,
                            password = password,
                        )
                    )
                    .addOnSuccessListener {
                        success.invoke()
                        rememberCurrentUser(isRemember)
                        preferences.currentUserPhone = phone
                        preferences.isRememberUser = isRemember
                        preferences.currentUID = auth.uid.toString()
                    }
                    .addOnFailureListener { exception -> failure.invoke(exception) }
            }
            .addOnFailureListener { exception -> failure.invoke(exception) }
    }

    override suspend fun signInUser(
        phone: String,
        password: String,
        isRemember: Boolean,
        success: () -> Unit,
        failure: (Throwable) -> Unit,
        wrong: () -> Unit,
        userNotFound: () -> Unit
    ) {
        store
            .collection(USERS)
            .whereEqualTo(PHONE, phone)
            .get()
            .addOnSuccessListener { querySnapshot ->
                Timber.tag("isRemember").d("isRemember: ${preferences.isRememberUser}")
                val data = querySnapshot.map { queryDocumentSnapshot ->
                    queryDocumentSnapshot.toObject(SignInDataResponse::class.java)
                }
                Timber.tag("AUTH").d("data: $data")
                if (data.isNotEmpty()) {
                    if (data.first().password == password && data.first().phone == phone) {
                        preferences.currentUserPhone = phone
                        preferences.isRememberUser = isRemember
                        preferences.currentUID = data.first().uid.toString()
                        Timber.tag("isRemember").d("isRemember: ${preferences.isRememberUser}")
                        success.invoke()
                    } else wrong.invoke()
                } else {
                    userNotFound.invoke()
                }
            }
            .addOnFailureListener {
                failure.invoke(it)
            }
    }

    override suspend fun checkUserPhone(
        phone: String,
        success: () -> Unit,
        userNotFound: () -> Unit,
        failure: (Throwable) -> Unit
    ) {
        store
            .collection(USERS)
            .whereEqualTo(PHONE, phone)
            .get()
            .addOnSuccessListener { querySnapshot ->
                val response = querySnapshot.map { it.toObject(ProfileDataResponse::class.java) }
                if (response.isNotEmpty()) {
                    if (response.first().phone == phone) success.invoke()
                } else userNotFound.invoke()
            }
            .addOnFailureListener { failure.invoke(it) }

    }

    override suspend fun restorePassword(
        verificationId: String,
        smsCode: String,
        phone: String,
        password: String,
        success: () -> Unit,
        failure: (Throwable) -> Unit
    ) {
        val credential = PhoneAuthProvider.getCredential(verificationId, smsCode)
        auth
            .signInWithCredential(credential)
            .addOnSuccessListener { authResult ->
                store
                    .collection(USERS)
                    .whereEqualTo(UID, authResult.user?.uid)
                    .get()
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val documentId = task.result.documents.map { it.id }
                            store
                                .collection(USERS)
                                .document(documentId.first())
                                .update("password", password)
                                .addOnSuccessListener { success.invoke() }
                                .addOnFailureListener { failure.invoke(it) }
                        }
                    }
                    .addOnFailureListener { failure.invoke(it) }
            }
            .addOnFailureListener { failure.invoke(it) }

    }

    override fun signOut() {
        preferences.currentUID = ""
        preferences.currentUserPhone = ""
        preferences.isRememberUser = false
    }

    private fun rememberCurrentUser(status: Boolean) =
        run { preferences.isRememberUser = status }

    override suspend fun profileData(
        success: (ProfileDataResponse) -> Unit,
        failure: (Throwable) -> Unit
    ) {
        store.collection(USERS)
            .whereEqualTo(UID, preferences.currentUID)
            .get()
            .addOnSuccessListener { querySnapshot ->
                val response = querySnapshot.first().toObject(ProfileDataResponse::class.java)
                Timber.tag("PROFILE").d("response: $response")
                success.invoke(response)
            }
            .addOnFailureListener { failure.invoke(it) }
    }

    override suspend fun editProfileData(
        profileEditData: ProfileEditData,
        success: () -> Unit,
        failure: (Throwable) -> Unit
    ) {
        store.collection(USERS)
            .whereEqualTo(UID, preferences.currentUID)
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val documentId =
                        task.result.documents.map { documentSnapshot -> documentSnapshot.id }
                    Timber.tag("AUTH").d("documentId: ${documentId.first()}")
                    val request = ProfileEditDataRequest(
                        uid = preferences.currentUID,
                        firstname = profileEditData.firstname,
                        lastname = profileEditData.lastname,
                        phone = preferences.currentUserPhone,
                        location = profileEditData.location,
                        gender = profileEditData.gender,
                        dateOfBirth = profileEditData.dateOfBirth
                    )
                    store
                        .collection(USERS)
                        .document(documentId.first())
                        .set(request, SetOptions.merge())
                        .addOnSuccessListener {
                            Timber.d("AUTH: isSuccessful")
                            success.invoke()
                        }
                        .addOnFailureListener {
                            Timber.d("AUTH: !isSuccessful: $it")
                            failure.invoke(it)
                        }
                }
            }
            .addOnFailureListener { failure.invoke(it) }
    }

/*private fun addNewUserToCloud(
    uid: String,
    firstname: String,
    lastname: String,
    phone: String,
    password: String
) {
    users.add(
        AuthDataRequest(
            uid = uid,
            firstname = firstname,
            lastname = lastname,
            phone = phone,
            password = password,
        )
    )
}

private fun updateCurrentUserFromCloud(
    uid: String,
    firstname: String,
    lastname: String,
    phone: String,
    password: String
) {
    users
        .whereEqualTo(UID, uid)
        .get()
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val documentId = task.result.documents.map { documentSnapshot ->
                    documentSnapshot.id
                }
                Timber.tag("AUTH").d("documentId: ${documentId.first()}")
                users.document(documentId.first()).set(
                    AuthDataRequest(
                        uid = uid,
                        firstname = firstname,
                        lastname = lastname,
                        phone = phone,
                        password = password
                    ), SetOptions.merge()
                )
            }
        }
}*/
}