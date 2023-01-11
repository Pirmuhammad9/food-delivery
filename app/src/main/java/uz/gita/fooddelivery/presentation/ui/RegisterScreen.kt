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
import uz.gita.fooddelivery.databinding.ScreenRegisterBinding
import uz.gita.fooddelivery.presentation.viewmodels.register.RegisterVM
import uz.gita.fooddelivery.presentation.viewmodels.register.RegisterVMImpl
import uz.gita.fooddelivery.utils.hideKeyboard
import uz.gita.fooddelivery.utils.onClick
import uz.gita.fooddelivery.utils.snackErrorMessage
import uz.gita.fooddelivery.utils.textToString
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class RegisterScreen : Fragment(R.layout.screen_register) {

    private val viewBinding by viewBinding(ScreenRegisterBinding::bind)
    private val viewModel: RegisterVM by viewModels<RegisterVMImpl>()

    override fun onCreate(savedInstanceState: Bundle?) = with(viewModel) {
        super.onCreate(savedInstanceState)
        errorMessageLD.observe(this@RegisterScreen) { snackErrorMessage(resources.getString(it)) }
        hideKeyboardLD.observe(this@RegisterScreen) { hideKeyboard() }
        registerUserLD.observe(this@RegisterScreen, registerUserLDObserver)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        subScribeViewBinding(viewBinding)
        subscribeViewModel(viewModel)
    }

    private fun subScribeViewBinding(viewBinding: ScreenRegisterBinding) = with(viewBinding) {
        buttonRegister.onClick {
            viewModel.onClickRegister(
                userFirstname.textToString(),
                userLastname.textToString(),
                phoneNumber.textToString(),
                password.textToString(),
                confirmPassword.textToString(),
                buttonRememberMe.isChecked
            )
        }
    }

    private fun subscribeViewModel(viewModel: RegisterVM) = with(viewModel) {
        progressLD.observe(viewLifecycleOwner, progressLDObserver)
        registerButtonStatusLD.observe(viewLifecycleOwner) { status ->
            viewBinding.buttonRegister.isEnabled = status
        }
    }

    private val progressLDObserver = Observer<Boolean> { status ->
        viewBinding.progress.apply {
            visibility = when (status) {
                false -> GONE
                else -> VISIBLE
            }
        }
    }

    private val registerUserLDObserver = Observer<String> { phone ->
        val auth = Firebase.auth
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phone)
            .setTimeout(61L, TimeUnit.SECONDS)
            .setActivity(requireActivity())
            .setCallbacks(callbacks)
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    private val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        override fun onVerificationCompleted(phoneAuthCredential: PhoneAuthCredential) {
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
        }

        override fun onVerificationFailed(p0: FirebaseException) {
            Timber.tag("AUTH").d("onVerificationFailed: $p0")
            viewModel.onVerificationFailed(p0.message.toString())
            snackErrorMessage(p0.message.toString(), Snackbar.LENGTH_LONG)
        }

        override fun onCodeSent(verificationId: String, p1: PhoneAuthProvider.ForceResendingToken) =
            with(viewBinding) {
                Timber.tag("AUTH").d("onCodeSent: $verificationId")
                viewModel.onSuccessfullyCodeSent()
                findNavController().navigate(
                    AuthScreenDirections.actionAuthScreenToVerifyScreen(
                        userFirstname.textToString(),
                        userLastname.textToString(),
                        phoneNumber.textToString(),
                        password.textToString(),
                        buttonRememberMe.isChecked,
                        verificationId,
                        true
                    )
                )
            }
    }
}