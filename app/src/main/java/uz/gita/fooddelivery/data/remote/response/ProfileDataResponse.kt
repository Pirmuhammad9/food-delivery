package uz.gita.fooddelivery.data.remote.response

import uz.gita.fooddelivery.data.model.ProfileData

data class ProfileDataResponse(
    val uid: String? = null,
    val firstname: String? = null,
    val lastname: String? = null,
    val phone: String? = null,
    val location: String? = null,
    val gender: String? = null,
    val dateOfBirth: String? = null
) {
    fun toProfileData(): ProfileData =
        ProfileData(
            firstname = firstname ?: "",
            lastname = lastname ?: "",
            phone = phone ?: "",
            location = location ?: "",
            gender = gender ?: "",
            dateOfBirth = dateOfBirth ?: ""
        )
}