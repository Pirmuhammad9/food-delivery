package uz.gita.fooddelivery.domain.usecase.cart

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import uz.gita.fooddelivery.data.repository.food.FoodRepository
import uz.gita.fooddelivery.data.model.CartData
import javax.inject.Inject

class CartUCImpl
@Inject constructor(
    private val repository: FoodRepository
) : CartUC {

    override fun cartData() = callbackFlow<Result<List<CartData>>> {
        withContext(Dispatchers.Default) {
            repository
                .cartData(
                    { response ->
                        val result = ArrayList<CartData>()
                        response.map {
                            if (it.checkout == true) return@map
                            result.add(it.toCartData())
                        }
                        trySendBlocking(Result.success(result))
                    },
                    { trySendBlocking(Result.failure(it)) }
                )
        }
        awaitClose { }
    }.flowOn(Dispatchers.IO)

    override fun deleteFood(deletedItemId: Long) = callbackFlow<Result<Unit>> {
        withContext(Dispatchers.Default) {
            repository
                .deleteFoodFromCart(
                    deletedItemId,
                    { trySendBlocking(Result.success(Unit)) },
                    { trySendBlocking(Result.failure(it)) }
                )
        }
        awaitClose { }
    }.flowOn(Dispatchers.IO)

    override fun updateFoodCount(id: Long, pieces: Int) = callbackFlow<Result<Unit>> {
        repository
            .updateFoodPiecesCart(
                id,
                pieces,
                { trySendBlocking(Result.success(Unit)) },
                { trySendBlocking(Result.failure(it)) }
            )
        awaitClose { }
    }.flowOn(Dispatchers.IO)

}