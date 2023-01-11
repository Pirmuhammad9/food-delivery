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
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.FirebaseException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import uz.gita.fooddelivery.R
import uz.gita.fooddelivery.databinding.ScreenSignInForgotBinding
import uz.gita.fooddelivery.presentation.viewmodels.sign_in_forgot.SignInForgotVM
import uz.gita.fooddelivery.presentation.viewmodels.sign_in_forgot.SingInForgotVMImpl
import uz.gita.fooddelivery.utils.hideKeyboard
import uz.gita.fooddelivery.utils.onClick
import uz.gita.fooddelivery.utils.snackErrorMessage
import uz.gita.fooddelivery.utils.textToString
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class SignInForgotScreen : Fragment(R.layout.screen_sign_in_forgot) {

    private val viewBinding by viewBinding(ScreenSignInForgotBinding::bind)
    private val viewModel: SignInForgotVM by viewModels<SingInForgotVMImpl>()
    private val navController by lazy(LazyThreadSafetyMode.NONE) { findNavController() }

    override fun onCreate(savedInstanceState: Bundle?) = with(viewModel) {
        super.onCreate(savedInstanceState)
        errorMessageLD.observe(this@SignInForgotScreen) { snackErrorMessage(resources.getString(it)) }
        networkErrorLD.observe(this@SignInForgotScreen) { snackErrorMessage(it) }
        popBackStackLD.observe(this@SignInForgotScreen) { navController.popBackStack() }
        userIsAvailableLD.observe(this@SignInForgotScreen, userIsAvailableLDObserver)
        hideKeyboardLD.observe(this@SignInForgotScreen) { hideKeyboard() }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        subscribeViewBinding(viewBinding)
        subscribeViewModel(viewModel)
    }

    private fun subscribeViewBinding(viewBinding: ScreenSignInForgotBinding) = with(viewBinding) {
        buttonRestore.onClick {
            viewModel.onClickRestore(
                phoneNumber.textToString(),
                password.textToString(),
                confirmPassword.textToString()
            )
        }
        buttonBack.onClick {
            viewModel.onClickBackButton()
        }
    }

    private fun subscribeViewModel(viewModel: SignInForgotVM) = with(viewModel) {
        restoreButtonStatusLD.observe(viewLifecycleOwner, restoreButtonStatusLDObserver)
        progressLD.observe(viewLifecycleOwner, progressLDObserver)
    }

    private val userIsAvailableLDObserver = Observer<String> { phone ->
        val auth = Firebase.auth
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phone)
            .setTimeout(61L, TimeUnit.SECONDS)
            .setActivity(requireActivity())
            .setCallbacks(callbacks)
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    private val restoreButtonStatusLDObserver = Observer<Boolean> {
        viewBinding.buttonRestore.isEnabled = it
    }

    private val progressLDObserver = Observer<Boolean> { status ->
        viewBinding.progress.apply {
            visibility = when (status) {
                false -> GONE
                else -> VISIBLE
            }
        }
    }

    private val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        override fun onVerificationCompleted(phoneAuthCredential: PhoneAuthCredential) =

                Timber.tag("AUTH").d("onVerificationCompleted: ${phoneAuthCredential.smsCode}")
                /*if (!phoneAuthCredential.smsCode.isNullOrEmpty()) {
                    findNavController().navigate(
                        AuthScreenDirections.actionAuthScreenToVerifyScreen(
                            userFirstname.textToString(),
                            userLastname.textToString(),
                            phoneNumber.textToString(),
                            password.textToString(),
                            buttonRememberMe.isChecked,
                            phoneAuthCredential.smsCode!!
                        )
                    )
                    return
                }*/

        override fun onVerificationFailed(p0: FirebaseException) {
            Timber.tag("AUTH").d("onVerificationFailed: $p0")
            snackErrorMessage(p0.message.toString(), Snackbar.LENGTH_LONG)
        }

        override fun onCodeSent(verificationId: String, p1: PhoneAuthProvider.ForceResendingToken) =
            with(viewBinding) {
                Timber.tag("AUTH").d("onCodeSent: $verificationId")
                navController.navigate(
                    SignInForgotScreenDirections.actionSignInForgotScreenToVerifyScreen(
                        "",
                        "",
                        phoneNumber.textToString(),
                        password.textToString(),
                        false,
                        verificationId,
                        false
                    )
                )
            }
    }

}