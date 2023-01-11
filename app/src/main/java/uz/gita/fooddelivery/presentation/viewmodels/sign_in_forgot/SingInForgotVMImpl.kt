package uz.gita.fooddelivery.presentation.viewmodels.sign_in_forgot

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
class SingInForgotVMImpl
@Inject constructor(
    private val useCase: CheckUserPhoneUC
) : ViewModel(), SignInForgotVM {

    override val hideKeyboardLD: MutableLiveData<Unit> by lazy { MutableLiveData<Unit>() }
    override val popBackStackLD: MutableLiveData<Unit> by lazy { MutableLiveData<Unit>() }
    override val userIsAvailableLD: MutableLiveData<String> by lazy { MutableLiveData<String>() }
    override val networkErrorLD: MutableLiveData<String> by lazy { MutableLiveData<String>() }
    override val errorMessageLD: MutableLiveData<Int> by lazy { MutableLiveData<Int>() }
    override val progressLD: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>() }
    override val restoreButtonStatusLD: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>() }

    override fun onClickRestore(phone: String, password: String, confirmPassword: String) {
        if (!phone.startsWith("+998") && phone.length != 13) {
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
        progressLD.value = true
        restoreButtonStatusLD.value = false
        hideKeyboardLD.value = Unit
        useCase
            .checkUserPhone(phone)
            .onEach { result ->
                progressLD.value = false
                restoreButtonStatusLD.value = true
                result.onSuccess { userIsAvailableLD.value = phone }
                result.onFailure { networkErrorLD.value = it.message }
            }
            .launchIn(viewModelScope)
    }

    override fun onClickBackButton() = kotlin.run { popBackStackLD.value = Unit }
}