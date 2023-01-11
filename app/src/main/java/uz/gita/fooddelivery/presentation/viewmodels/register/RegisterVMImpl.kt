package uz.gita.fooddelivery.presentation.viewmodels.register

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import uz.gita.fooddelivery.R
import uz.gita.fooddelivery.domain.usecase.sign_in_forgot.CheckUserPhoneUC
import javax.inject.Inject

@HiltViewModel
class RegisterVMImpl
@Inject constructor(
    private val useCase: CheckUserPhoneUC
) : ViewModel(), RegisterVM {

    override val registerUserLD: MutableLiveData<String> by lazy { MutableLiveData<String>() }
    override val navigateVerifyScreenLD: MutableLiveData<Unit> by lazy { MutableLiveData<Unit>() }
    override val errorMessageLD: MutableLiveData<Int> by lazy { MutableLiveData<Int>() }
    override val networkErrorMessageLD: MutableLiveData<String> by lazy { MutableLiveData<String>() }
    override val progressLD: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>() }
    override val registerButtonStatusLD: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>() }
    override val hideKeyboardLD: MutableLiveData<Unit> by lazy { MutableLiveData<Unit>() }

    override fun onClickRegister(
        firstname: String,
        lastname: String,
        phone: String,
        password: String,
        confirmPassword: String,
        isRemember: Boolean
    ) {
        if (firstname.isEmpty()) {
            errorMessageLD.value = R.string.text_error_firstname
            return
        }
        if (lastname.isEmpty()) {
            errorMessageLD.value = R.string.text_error_lastname
        }
        if (!(phone.startsWith("+998") && phone.length == 13)) {
            errorMessageLD.value = R.string.text_error_phone
            return
        }
        if (password.isEmpty()) {
            errorMessageLD.value = R.string.text_error_password
            return
        }
        if (confirmPassword.isEmpty()) {
            errorMessageLD.value = R.string.text_error_confirm_password
            return
        }
        if (password != confirmPassword) {
            errorMessageLD.value = R.string.text_error_confirm_password_not_matches
            return
        }
        hideKeyboardLD.value = Unit
        progressLD.value = true
        registerButtonStatusLD.value = false
        useCase
            .checkUserPhone(phone)
            .onEach { result ->
                result.onSuccess {
                    errorMessageLD.value = R.string.text_error_user_available
                    progressLD.value = false
                    registerButtonStatusLD.value = true
                }
                result.onFailure { registerUserLD.value = phone }
            }.launchIn(viewModelScope)

    }

    override fun onVerificationFailed(errorMessage: String) {
        progressLD.value = false
        registerButtonStatusLD.value = true
        networkErrorMessageLD.value = errorMessage
    }

    override fun onSuccessfullyCodeSent() {
        progressLD.value = false
        registerButtonStatusLD.value = true
    }

}