package uz.gita.fooddelivery.data.remote.response

data class RegisterDataResponse(
    val documentId: String? = null,
    val uid: String? = null,
    val firstname: String? = null,
    val lastname: String? = null,
    val phone: String? = null,
    val password: String? = null,
    val location: String? = null,
    val gender: String? = null,
    val dateOfBirth: String? = null,
    val isRemember: Boolean = false
)