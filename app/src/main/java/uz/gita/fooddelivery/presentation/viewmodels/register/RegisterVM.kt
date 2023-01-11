package uz.gita.fooddelivery.presentation.viewmodels.register

import androidx.lifecycle.LiveData

interface RegisterVM {

    val registerUserLD: LiveData<String>
    val navigateVerifyScreenLD: LiveData<Unit>
    val errorMessageLD: LiveData<Int>
    val networkErrorMessageLD: LiveData<String>
    val progressLD: LiveData<Boolean>
    val registerButtonStatusLD: LiveData<Boolean>
    val hideKeyboardLD: LiveData<Unit>

    fun onClickRegister(
        firstname: String,
        lastname: String,
        phone: String,
        password: String,
        confirmPassword:String,
        isRemember: Boolean
    )

    fun onVerificationFailed(errorMessage: String)
    fun onSuccessfullyCodeSent()

}
