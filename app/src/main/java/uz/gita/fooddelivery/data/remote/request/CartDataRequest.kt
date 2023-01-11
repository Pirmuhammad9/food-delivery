package uz.gita.fooddelivery.data.remote.request

data class CartDataRequest(
    val id: Long,
    val uid: String,
    val image: String,
    val name: String,
    val pieces: Int = 1,
    val price: Int,
    val checkout: Boolean = false
)