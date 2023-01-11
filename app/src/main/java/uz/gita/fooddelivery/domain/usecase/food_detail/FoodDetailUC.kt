package uz.gita.fooddelivery.domain.usecase.food_detail

import kotlinx.coroutines.flow.Flow

interface FoodDetailUC {

    fun addFoodToCart(name: String, price: Int, image: String): Flow<Result<Unit>>

}