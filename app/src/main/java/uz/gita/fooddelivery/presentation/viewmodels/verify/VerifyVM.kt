package uz.gita.fooddelivery.presentation.viewmodels.verify

import androidx.lifecycle.LiveData

interface VerifyVM {

    val progressLD: LiveData<Boolean>
    val hideKeyboardLD:LiveData<Unit>
    val successRestoreLD: LiveData<String>
    val networkErrorMessageLD: LiveData<String>
    val errorMessageLD: LiveData<Int>
    val timerLD: LiveData<Long>
    val verifyButtonStatus: LiveData<Boolean>
    val resendVerifyButtonStatus: LiveData<Boolean>
    val navigateAuthScreenLD: LiveData<Unit>
    val navigateMainScreenLD: LiveData<Unit>

    fun onClickVerifyButton(
        verificationId: String,
        smsCode: String, firstname: String,
        lastname: String,
        phone: String,
        password: String,
        isRemember: Boolean,
        isNewUser: Boolean
    )

    fun onClickResendVerifyButton()
    fun onClickBack()
}