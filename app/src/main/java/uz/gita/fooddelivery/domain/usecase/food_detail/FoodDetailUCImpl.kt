package uz.gita.fooddelivery.domain.usecase.food_detail

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOn
import uz.gita.fooddelivery.data.repository.food.FoodRepository
import javax.inject.Inject

class FoodDetailUCImpl
@Inject constructor(
    private val repository: FoodRepository
) : FoodDetailUC {

    override fun addFoodToCart(
        name: String,
        price: Int,
        image: String
    ) = callbackFlow<Result<Unit>> {
        repository
            .addFoodToCart(
                name,
                price,
                image,
                { trySendBlocking(Result.success(Unit)) },
                { trySendBlocking(Result.failure(it)) })
        awaitClose { }
    }
        .flowOn(Dispatchers.IO)
}