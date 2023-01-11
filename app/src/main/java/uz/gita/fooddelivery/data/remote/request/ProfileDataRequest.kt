package uz.gita.fooddelivery.data.remote.request

data class ProfileDataRequest(
    val firstname: String,
    val lastname: String,
    val location: String = "",
    val gender: String = "",
    val dateOfBirth: String = ""
)