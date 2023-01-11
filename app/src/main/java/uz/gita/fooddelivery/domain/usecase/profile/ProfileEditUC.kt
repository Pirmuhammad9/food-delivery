package uz.gita.fooddelivery.domain.usecase.profile

import kotlinx.coroutines.flow.Flow
import uz.gita.fooddelivery.data.model.ProfileData

interface ProfileEditUC {

    fun profileData(): Flow<Result<ProfileData>>

    fun editProfile(
        firstname: String,
        lastname: String,
        location: String,
        gender: String,
        dateOfBirth: String
    ): Flow<Result<Unit>>

}