package uz.gita.fooddelivery.data.remote.request

data class ProfileEditDataRequest(
    val uid: String,
    val firstname: String,
    val lastname: String,
    val phone: String,
    val location: String,
    val gender: String,
    val dateOfBirth: String
)