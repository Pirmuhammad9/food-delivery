package uz.gita.fooddelivery.domain.usecase.checkout

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import uz.gita.fooddelivery.data.repository.auth.AuthRepository
import uz.gita.fooddelivery.data.repository.food.FoodRepository
import uz.gita.fooddelivery.data.model.CartData
import javax.inject.Inject

class CheckoutUCImpl
@Inject constructor(
    private val foodRepository: FoodRepository,
    private val authRepository: AuthRepository
) : CheckoutUC {

    override fun userData() = callbackFlow<Result<Pair<String, String>>> {
        withContext(Dispatchers.Default) {
            authRepository.profileData(
                { response ->
                    val data = response.toProfileData()
                    val fullName = data.firstname + ", " + data.lastname
                    val address = data.location
                    trySendBlocking(Result.success(Pair(fullName, address)))
                },
                { trySendBlocking(Result.failure(it)) }
            )
        }
        awaitClose {}
    }.flowOn(Dispatchers.IO)

    override fun cartData() = callbackFlow<Result<Pair<List<CartData>, Int>>> {
        withContext(Dispatchers.Default) {
            foodRepository
                .cartData(
                    { response ->
                        val result = ArrayList<CartData>()
                        response.forEach {
                            if (it.checkout == true) return@forEach
                            result.add(it.toCartData())
                        }
                        var totalPrice = 0
                        result.forEach { totalPrice += it.price * it.pieces }
                        trySendBlocking(Result.success(Pair(result, totalPrice)))
                    },
                    { trySendBlocking(Result.failure(it)) }
                )
        }
        awaitClose { }
    }.flowOn(Dispatchers.IO)

    override fun placeOrder() = callbackFlow<Result<Unit>> {
        withContext(Dispatchers.Default) {
            foodRepository.placeOrder(
                { trySendBlocking(Result.success(Unit)) },
                { trySendBlocking(Result.failure(it)) }
            )
        }
        awaitClose {}
    }.flowOn(Dispatchers.IO)
}