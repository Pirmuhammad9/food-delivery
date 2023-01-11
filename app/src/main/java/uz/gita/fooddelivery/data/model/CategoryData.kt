package uz.gita.fooddelivery.data.model

data class CategoryData(
    val id: Int,
    val name: String,
    val icon: String,
    var foodsCount: Int = 0,
    var isSelected: Boolean = false
)
