package uz.gita.fooddelivery.presentation.viewmodels.sign_in

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import uz.gita.fooddelivery.R
import uz.gita.fooddelivery.domain.usecase.sign_in.SignInUC
import javax.inject.Inject

@HiltViewModel
class SignInVMImpl
@Inject constructor(
    private val useCase: SignInUC
) : ViewModel(), SignInVM {

    override val hideKeyBoardLD: MutableLiveData<Unit> by lazy { MutableLiveData<Unit>() }
    override val progressStatusLD: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>() }
    override val loginButtonStatusLD: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>() }
    override val errorMessageLD: MutableLiveData<Int> by lazy { MutableLiveData<Int>() }
    override val errorLD: MutableLiveData<String> by lazy { MutableLiveData<String>() }
    override val successLoginLD: MutableLiveData<Unit> by lazy { MutableLiveData<Unit>() }
    override val forgetPasswordLD: MutableLiveData<Unit> by lazy { MutableLiveData<Unit>() }

    override fun onClickForgotPassword() {
        forgetPasswordLD.value = Unit
    }

    override fun onClickLogin(phone: String, password: String, rememberStatus: Boolean) {
        if (!(phone.length == 13 && phone.startsWith("+998"))) {
            errorMessageLD.value = R.string.text_error_phone
            return
        }
        if (password.isEmpty()) {
            errorMessageLD.value = R.string.text_error_password
            return
        }
        hideKeyBoardLD.value = Unit
        progressStatusLD.value = true
        loginButtonStatusLD.value = false
        useCase.signInUser(phone, password, rememberStatus)
            .onEach { result ->
                progressStatusLD.value = false
                loginButtonStatusLD.value = true
                result.onSuccess { successLoginLD.value = it }
                result.onFailure { throwable -> errorLD.value = throwable.message }
            }.launchIn(viewModelScope)
    }
}