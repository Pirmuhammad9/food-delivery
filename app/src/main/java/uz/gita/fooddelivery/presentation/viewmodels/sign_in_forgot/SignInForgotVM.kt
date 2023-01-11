package uz.gita.fooddelivery.presentation.viewmodels.sign_in_forgot

import androidx.lifecycle.LiveData

interface SignInForgotVM {

    val hideKeyboardLD:LiveData<Unit>
    val popBackStackLD:LiveData<Unit>
    val userIsAvailableLD: LiveData<String>
    val networkErrorLD: LiveData<String>
    val errorMessageLD: LiveData<Int>
    val progressLD: LiveData<Boolean>
    val restoreButtonStatusLD: LiveData<Boolean>

    fun onClickRestore(phone: String, password: String, confirmPassword:String)
    fun onClickBackButton()

}