package uz.gita.fooddelivery.domain.usecase.profile

import kotlinx.coroutines.flow.Flow
import uz.gita.fooddelivery.data.model.CartData
import uz.gita.fooddelivery.data.model.ProfileData

interface ProfileUC {

    fun profileData(): Flow<Result<ProfileData>>

    fun historyData(): Flow<Result<List<CartData>>>

}