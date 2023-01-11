package uz.gita.fooddelivery.data.model

data class CartData(
    var id: Long = 0,
    val image: String,
    val name: String,
    var pieces: Int = 1,
    val price: Int
)