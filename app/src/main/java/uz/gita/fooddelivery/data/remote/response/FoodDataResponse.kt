package uz.gita.fooddelivery.data.remote.response

import uz.gita.fooddelivery.data.model.CategoryData
import uz.gita.fooddelivery.data.model.FoodData

data class FoodDataResponse(
    val categoryId: Int? = null,
    val categoryIcon: String? = null,
    val categoryName: String? = null,
    val description: String? = null,
    val id: Int? = null,
    val image: String? = null,
    val name: String? = null,
    val price: Int? = null
) {
    fun toCategoryData(): CategoryData =
        CategoryData(id = categoryId ?: 0, name = categoryName ?: "", icon = categoryIcon ?: "")

    fun toFoodData(): FoodData = FoodData(
        id = id ?: 0,
        price = price ?: 0,
        image = image ?: "",
        name = name ?: "",
        description = description ?: ""
    )
}