package uz.gita.fooddelivery.data.model

data class ProfileData(
    val firstname: String,
    val lastname: String,
    val phone: String,
    val location: String,
    val gender: String = "",
    val dateOfBirth: String = ""
)