package uz.gita.fooddelivery.presentation.viewmodels.profile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import timber.log.Timber
import uz.gita.fooddelivery.R
import uz.gita.fooddelivery.data.model.ProfileData
import uz.gita.fooddelivery.domain.usecase.profile.ProfileEditUC
import javax.inject.Inject

@HiltViewModel
class ProfileEditVMImpl
@Inject constructor(
    private val useCase: ProfileEditUC
) : ViewModel(), ProfileEditVM {

    override val showDatePickerDialogLD: MutableLiveData<String> by lazy { MutableLiveData<String>() }
    override val profileDataLD: MutableLiveData<ProfileData> by lazy { MutableLiveData<ProfileData>() }
    override val errorMessageLD: MutableLiveData<Int> by lazy { MutableLiveData<Int>() }
    override val errorLD: MutableLiveData<String> by lazy { MutableLiveData<String>() }
    override val popBackStackLD: MutableLiveData<Unit> by lazy { MutableLiveData<Unit>() }

    private var userBirthDay = ""

    init {
        useCase.profileData()
            .onEach { result ->
                result.onSuccess { data ->
                    profileDataLD.value = data
                    userBirthDay = data.dateOfBirth
                }
                result.onFailure { throwable -> errorLD.value = throwable.message }
            }
            .launchIn(viewModelScope)

    }

    override fun onClickSubmit(
        firstname: String,
        lastname: String,
        location: String,
        gender: String,
        dateOfBirth: String
    ) {
        if (firstname.isEmpty()) {
            errorMessageLD.value = R.string.text_error_firstname
            return
        }
        if (lastname.isEmpty()) {
            errorMessageLD.value = R.string.text_error_lastname
            return
        }
        if (location.isEmpty()) {
            errorMessageLD.value = R.string.text_error_location
            return
        }
        Timber.d("useCase.editProfile: $firstname, $lastname, $location, $gender, $dateOfBirth")
        useCase.editProfile(firstname, lastname, location, gender, dateOfBirth)
            .onEach { result ->
                result.onSuccess { popBackStackLD.value = it }
                result.onFailure { throwable -> errorLD.value = throwable.message }
            }
            .launchIn(viewModelScope)
    }

    override fun onClickDatePicker() {
        showDatePickerDialogLD.value = userBirthDay
    }

    override fun onClickBack() {
        popBackStackLD.value = Unit
    }
}