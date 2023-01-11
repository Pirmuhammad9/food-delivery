package uz.gita.fooddelivery.data.remote.response

import uz.gita.fooddelivery.data.model.CartData

data class CartDataResponse(
    val id: Long? = null,
    val uid: String? = null,
    val image: String? = null,
    val name: String? = null,
    val pieces: Int? = null,
    val price: Int? = null,
    var checkout: Boolean? = null
) {
    fun toCartData(): CartData = CartData(
        id = id ?: 0,
        image = image ?: "",
        name = name ?: "",
        pieces = pieces ?: 0,
        price = price ?: 0,
    )

}