package uz.gita.fooddelivery.domain.usecase.profile

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import uz.gita.fooddelivery.data.repository.auth.AuthRepository
import uz.gita.fooddelivery.data.repository.food.FoodRepository
import uz.gita.fooddelivery.data.model.CartData
import uz.gita.fooddelivery.data.model.ProfileData
import javax.inject.Inject

class ProfileUCImpl
@Inject constructor(
    private val authRepository: AuthRepository,
    private val foodRepository: FoodRepository
) : ProfileUC {

    override fun profileData() = callbackFlow<Result<ProfileData>> {
        withContext(Dispatchers.Default) {
            authRepository
                .profileData(
                    { profileDataResponse -> trySendBlocking(Result.success(profileDataResponse.toProfileData())) },
                    { trySendBlocking(Result.failure(it)) })
        }
        awaitClose { }
    }.flowOn(Dispatchers.IO)

    override fun historyData() = callbackFlow<Result<List<CartData>>> {
        withContext(Dispatchers.Default) {
            foodRepository
                .historyData(
                    { response ->
                        val result = ArrayList<CartData>()
                        response.forEach {
                            if (it.checkout == false) return@forEach
                            result.add(it.toCartData())
                        }
                        trySendBlocking(Result.success(result))
                    },
                    { trySendBlocking(Result.failure(it)) }
                )
        }
        awaitClose {}
    }.flowOn(Dispatchers.IO)

}