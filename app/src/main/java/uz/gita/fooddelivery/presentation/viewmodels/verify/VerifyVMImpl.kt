package uz.gita.fooddelivery.presentation.viewmodels.verify

import android.os.CountDownTimer
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import uz.gita.fooddelivery.R
import uz.gita.fooddelivery.domain.usecase.verify.VerifyUC
import javax.inject.Inject

@HiltViewModel
class VerifyVMImpl
@Inject constructor(
    private val useCase: VerifyUC
) : ViewModel(), VerifyVM {

    override val progressLD: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>() }
    override val hideKeyboardLD: MutableLiveData<Unit> by lazy { MutableLiveData<Unit>() }
    override val successRestoreLD: MutableLiveData<String> by lazy { MutableLiveData<String>() }
    override val networkErrorMessageLD: MutableLiveData<String> by lazy { MutableLiveData<String>() }
    override val errorMessageLD: MutableLiveData<Int> by lazy { MutableLiveData<Int>() }
    override val timerLD: MutableLiveData<Long> by lazy { MutableLiveData<Long>() }
    override val verifyButtonStatus: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>() }
    override val resendVerifyButtonStatus: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>() }
    override val navigateAuthScreenLD: MutableLiveData<Unit> by lazy { MutableLiveData<Unit>() }
    override val navigateMainScreenLD: MutableLiveData<Unit> by lazy { MutableLiveData<Unit>() }

    private val countDownTimer: CountDownTimer
    private val timerMillisInFuture = 61000L
    private val timerCountDownInterval = 1000L

    init {
        countDownTimer = object : CountDownTimer(timerMillisInFuture, timerCountDownInterval) {
            override fun onTick(millisUntilFinished: Long) {
                timerLD.value = millisUntilFinished / timerCountDownInterval
            }

            override fun onFinish() {
                verifyButtonStatus.value = false
                progressLD.value = false
                resendVerifyButtonStatus.value = true
            }
        }
        countDownTimer.start()
    }

    override fun onClickVerifyButton(
        verificationId: String,
        smsCode: String, firstname: String,
        lastname: String,
        phone: String,
        password: String,
        isRemember: Boolean,
        isNewUser: Boolean
    ) {
        if (smsCode.isEmpty() || smsCode.length != 6) {
            errorMessageLD.value = R.string.text_verify_invalid_code
            return
        }
        hideKeyboardLD.value = Unit
        progressLD.value = true
        if (isNewUser) {
            useCase.registerUser(
                verificationId,
                smsCode,
                firstname,
                lastname,
                phone,
                password,
                isRemember
            ).onEach { result ->
                progressLD.value = false
                result.onSuccess { navigateMainScreenLD.value = it }
                result.onFailure { throwable ->
                    progressLD.value = false
                    networkErrorMessageLD.value = throwable.message
                }
            }.launchIn(viewModelScope)
        } else {
            useCase
                .restoreUserPassword(
                    verificationId,
                    smsCode,
                    phone,
                    password
                )
                .onEach { result ->
                    progressLD.value = false
                    result.onSuccess { message ->
                        successRestoreLD.value = message
                        navigateAuthScreenLD.value = Unit
                    }
                    result.onFailure { throwable ->
                        progressLD.value = false
                        networkErrorMessageLD.value = throwable.message
                    }
                }.launchIn(viewModelScope)
        }
    }

    override fun onClickResendVerifyButton() {
        countDownTimer.cancel()
        countDownTimer.start()
        verifyButtonStatus.value = true
        resendVerifyButtonStatus.value = false
    }

    override fun onClickBack() {
        navigateAuthScreenLD.value = Unit
    }

}