package uz.gita.fooddelivery.domain.usecase.profile

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOn
import uz.gita.fooddelivery.data.model.ProfileData
import uz.gita.fooddelivery.data.model.ProfileEditData
import uz.gita.fooddelivery.data.repository.auth.AuthRepository
import javax.inject.Inject

class ProfileEditUCImpl
@Inject constructor(
    private val repository: AuthRepository
) : ProfileEditUC {

    override fun profileData() = callbackFlow<Result<ProfileData>> {
        repository.profileData(
            { profileDataResponse -> trySendBlocking(Result.success(profileDataResponse.toProfileData())) },
            { trySendBlocking(Result.failure(it)) })
        awaitClose { }
    }.flowOn(Dispatchers.Default)

    override fun editProfile(
        firstname: String,
        lastname: String,
        location: String,
        gender: String,
        dateOfBirth: String
    ) = callbackFlow<Result<Unit>> {
        repository.editProfileData(
            profileEditData = ProfileEditData(
                firstname,
                lastname,
                location,
                gender,
                dateOfBirth
            ),
            { trySendBlocking(Result.success(Unit)) },
            { trySendBlocking(Result.failure(it)) }
        )
        awaitClose { }
    }.flowOn(Dispatchers.IO)
}