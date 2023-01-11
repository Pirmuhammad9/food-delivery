package uz.gita.fooddelivery.data.repository.auth

import uz.gita.fooddelivery.data.remote.response.ProfileDataResponse
import uz.gita.fooddelivery.data.model.ProfileEditData

interface   AuthRepository {

    suspend fun registerUser(
        verificationId: String,
        smsCode: String,
        firstname: String,
        lastname: String,
        phone: String,
        password: String,
        isRemember: Boolean,
        success: () -> Unit,
        failure: (Throwable) -> Unit
    )

    suspend fun signInUser(
        phone: String,
        password: String,
        isRemember: Boolean,
        success: () -> Unit,
        failure: (Throwable) -> Unit,
        wrong: () -> Unit,
        userNotFound: () -> Unit
    )

    suspend fun checkUserPhone(
        phone: String,
        success: () -> Unit,
        userNotFound: () -> Unit,
        failure: (Throwable) -> Unit
    )

    suspend fun restorePassword(
        verificationId: String,
        smsCode: String,
        phone: String,
        password: String,
        success: () -> Unit,
        failure: (Throwable) -> Unit
    )

    suspend fun profileData(
        success: (ProfileDataResponse) -> Unit,
        failure: (Throwable) -> Unit
    )

    suspend fun editProfileData(
        profileEditData: ProfileEditData,
        success: () -> Unit,
        failure: (Throwable) -> Unit
    )

    fun signOut()

}