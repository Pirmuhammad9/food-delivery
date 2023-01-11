package uz.gita.fooddelivery.data.remote.response

import uz.gita.fooddelivery.data.model.AdData

data class AdDataResponse(
    val id: Int? = null,
    val imageUrl: String? = null
) {
    fun toAdData(): AdData = AdData(id ?: 0, imageUrl ?: "")
}