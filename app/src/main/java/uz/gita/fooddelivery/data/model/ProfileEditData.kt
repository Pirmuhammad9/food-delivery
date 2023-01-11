package uz.gita.fooddelivery.data.model

data class ProfileEditData(
    val firstname: String,
    val lastname: String,
    val location: String,
    val gender: String = "",
    val dateOfBirth: String = ""
)