package uz.gita.fooddelivery.domain.usecase.cart

import kotlinx.coroutines.flow.Flow
import uz.gita.fooddelivery.data.model.CartData

interface CartUC {

    fun cartData(): Flow<Result<List<CartData>>>

    fun deleteFood(deletedItemId: Long): Flow<Result<Unit>>

    fun updateFoodCount(id: Long, pieces: Int): Flow<Result<Unit>>

}