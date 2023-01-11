package uz.gita.fooddelivery.presentation.viewmodels.sign_in

import androidx.lifecycle.LiveData

interface SignInVM {

    val progressStatusLD: LiveData<Boolean>
    val loginButtonStatusLD: LiveData<Boolean>
    val hideKeyBoardLD: LiveData<Unit>
    val errorMessageLD: LiveData<Int>
    val errorLD: LiveData<String>
    val successLoginLD: LiveData<Unit>
    val forgetPasswordLD: LiveData<Unit>

    fun onClickForgotPassword()
    fun onClickLogin(phone: String, password: String, rememberStatus: Boolean)

}