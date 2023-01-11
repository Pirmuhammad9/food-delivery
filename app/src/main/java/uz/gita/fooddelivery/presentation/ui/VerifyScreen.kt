package uz.gita.fooddelivery.presentation.ui

import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import uz.gita.fooddelivery.R
import uz.gita.fooddelivery.databinding.ScreenVerifyBinding
import uz.gita.fooddelivery.presentation.viewmodels.verify.VerifyVM
import uz.gita.fooddelivery.presentation.viewmodels.verify.VerifyVMImpl
import uz.gita.fooddelivery.utils.*

@AndroidEntryPoint
class VerifyScreen : Fragment(R.layout.screen_verify) {

    private val viewBinding by viewBinding(ScreenVerifyBinding::bind)
    private val viewModel: VerifyVM by viewModels<VerifyVMImpl>()
    private val navController: NavController by lazy(LazyThreadSafetyMode.NONE) { findNavController() }
    private val args: VerifyScreenArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) = with(viewModel) {
        super.onCreate(savedInstanceState)
        hideKeyboardLD.observe(this@VerifyScreen) { hideKeyboard() }
        successRestoreLD.observe(this@VerifyScreen) { snackMessage(it) }
        networkErrorMessageLD.observe(this@VerifyScreen) { snackErrorMessage(it) }
        errorMessageLD.observe(this@VerifyScreen) { snackErrorMessage(resources.getString(it)) }
        navigateAuthScreenLD.observe(this@VerifyScreen) { navController.navigate(R.id.action_verifyScreen_to_authScreen) }
        navigateMainScreenLD.observe(this@VerifyScreen) { navController.navigate(R.id.action_verifyScreen_to_mainScreen) }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = with(viewBinding) {
        subscribeViewBinding(viewBinding)
        subscribeViewModel(viewModel)
    }

    private fun subscribeViewBinding(viewBinding: ScreenVerifyBinding) = with(viewBinding) {
        buttonVerify.onClick {
            viewModel.onClickVerifyButton(
                args.verificationId,
                verifyEditText.textToString(),
                args.firstname,
                args.lastname,
                args.phone,
                args.password,
                args.isRemember,
                args.isNewUser
            )
        }
        buttonResend.onClick {
            viewModel.onClickResendVerifyButton()
        }
        buttonBack.onClick {
            viewModel.onClickBack()
        }
    }

    private fun subscribeViewModel(viewModel: VerifyVM) = with(viewModel) {
        timerLD.observe(viewLifecycleOwner) {
            viewBinding.apply {
                timer.text = resources.getString(R.string.text_verify_count_down_timer, it)
                progressCircular.progress = it.toInt()
            }
        }
        verifyButtonStatus.observe(viewLifecycleOwner) { status ->
            viewBinding.buttonVerify.isEnabled = status
        }
        resendVerifyButtonStatus.observe(viewLifecycleOwner) { status ->
            viewBinding.buttonResend.apply {
                visibility = when (status) {
                    true -> VISIBLE
                    else -> GONE
                }
            }
        }
        progressLD.observe(viewLifecycleOwner) { status ->
            viewBinding.progress.apply {
                visibility = when (status) {
                    true -> VISIBLE
                    else -> GONE
                }
            }
        }
    }
}