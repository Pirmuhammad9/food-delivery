package uz.gita.fooddelivery.presentation.ui

import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import uz.gita.fooddelivery.R
import uz.gita.fooddelivery.databinding.ScreenSignInBinding
import uz.gita.fooddelivery.presentation.viewmodels.sign_in.SignInVM
import uz.gita.fooddelivery.presentation.viewmodels.sign_in.SignInVMImpl
import uz.gita.fooddelivery.utils.hideKeyboard
import uz.gita.fooddelivery.utils.onClick
import uz.gita.fooddelivery.utils.snackErrorMessage
import uz.gita.fooddelivery.utils.textToString

@AndroidEntryPoint
class SignInScreen : Fragment(R.layout.screen_sign_in) {

    private val viewBinding by viewBinding(ScreenSignInBinding::bind)
    private val viewModel: SignInVM by viewModels<SignInVMImpl>()
    private val navController by lazy(LazyThreadSafetyMode.NONE) { findNavController() }

    override fun onCreate(savedInstanceState: Bundle?) = with(viewModel) {
        super.onCreate(savedInstanceState)
        errorLD.observe(this@SignInScreen) { snackErrorMessage(it) }
        errorMessageLD.observe(this@SignInScreen) { snackErrorMessage(resources.getString(it)) }
        successLoginLD.observe(this@SignInScreen) { navController.navigate(R.id.action_authScreen_to_mainScreen) }
        hideKeyBoardLD.observe(this@SignInScreen) { hideKeyboard() }
        forgetPasswordLD.observe(this@SignInScreen) { navController.navigate(R.id.action_authScreen_to_signInForgotScreen) }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = with(viewBinding) {
        buttonLogin.onClick {
            viewModel.onClickLogin(
                phoneNumber.textToString(),
                password.textToString(),
                buttonRememberMe.isChecked
            )
        }
        buttonForgotPassword.onClick {
            viewModel.onClickForgotPassword()
        }

        buttonForgotPassword.onClick {
            viewModel.onClickForgotPassword()
        }
        subscribeViewModel(viewModel)
    }

    private fun subscribeViewModel(viewModel: SignInVM) = with(viewModel) {
        progressStatusLD.observe(viewLifecycleOwner, progressStatusLDObserver)
        loginButtonStatusLD.observe(viewLifecycleOwner, loginButtonStatusLDObserver)
    }

    private val progressStatusLDObserver = Observer<Boolean> {
        viewBinding.progress.apply {
            visibility = when (it) {
                true -> VISIBLE
                else -> GONE
            }
        }
    }

    private val loginButtonStatusLDObserver = Observer<Boolean> {
        viewBinding.buttonLogin.isEnabled = it
    }
}