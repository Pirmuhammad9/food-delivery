package uz.gita.fooddelivery.data.remote.request

data class RegisterDataRequest(
    val uid: String,
    val firstname: String,
    val lastname: String,
    val phone: String,
    val password: String,
    val location: String = "",
    val gender: String = "",
    val dateOfBirth: String = ""
)