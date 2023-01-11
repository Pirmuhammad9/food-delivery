package uz.gita.fooddelivery.presentation.viewmodels.profile

import androidx.lifecycle.LiveData
import uz.gita.fooddelivery.data.model.ProfileData

interface ProfileEditVM {

    val showDatePickerDialogLD:LiveData<String>
    val profileDataLD: LiveData<ProfileData>
    val errorMessageLD: LiveData<Int>
    val errorLD: LiveData<String>
    val popBackStackLD: LiveData<Unit>

    fun onClickSubmit(
        firstname: String,
        lastname: String,
        location: String,
        gender: String,
        dateOfBirth: String
    )

    fun onClickDatePicker()

    fun onClickBack()

}