package uz.gita.fooddelivery.presentation.ui

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import uz.gita.fooddelivery.R
import uz.gita.fooddelivery.databinding.ScreenEditProfileBinding
import uz.gita.fooddelivery.data.model.ProfileData
import uz.gita.fooddelivery.presentation.viewmodels.profile.ProfileEditVM
import uz.gita.fooddelivery.presentation.viewmodels.profile.ProfileEditVMImpl
import uz.gita.fooddelivery.utils.onClick
import uz.gita.fooddelivery.utils.snackErrorMessage
import uz.gita.fooddelivery.utils.textToString
import java.util.*

@AndroidEntryPoint
class ProfileEditScreen : Fragment(R.layout.screen_edit_profile) {

    private val viewBinding by viewBinding(ScreenEditProfileBinding::bind)
    private val viewModel: ProfileEditVM by viewModels<ProfileEditVMImpl>()

    override fun onCreate(savedInstanceState: Bundle?) = with(viewModel) {
        super.onCreate(savedInstanceState)
        errorLD.observe(this@ProfileEditScreen) {
            Timber.d("errorLD: $it")
            snackErrorMessage(it)
        }
        errorMessageLD.observe(this@ProfileEditScreen) { snackErrorMessage(resources.getString(it)) }
        popBackStackLD.observe(this@ProfileEditScreen) { findNavController().popBackStack() }
        showDatePickerDialogLD.observe(this@ProfileEditScreen, showDatePickerDialogLDObserver)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        subscribeViewBinding(viewBinding)
        subscribeViewModel(viewModel)

    }

    private fun subscribeViewBinding(viewBinding: ScreenEditProfileBinding) = with(viewBinding) {
        buttonBack.onClick { viewModel.onClickBack() }
        userDateOfBirth.onClick { viewModel.onClickDatePicker() }
        buttonSubmit.onClick {
            viewModel.onClickSubmit(
                userFirstname.textToString(),
                userLastname.textToString(),
                userLocation.textToString(),
                buttonGender.text.toString(),
                userDateOfBirth.textToString(),
            )
        }
    }

    private fun subscribeViewModel(viewModel: ProfileEditVM) = with(viewModel) {
        profileDataLD.observe(viewLifecycleOwner, profileDataLDObserver)
    }

    private val profileDataLDObserver = Observer<ProfileData> { data ->
        viewBinding.apply {
            userFirstname.setText(data.firstname)
            userLastname.setText(data.lastname)
            userLocation.setText(data.location)
            userDateOfBirth.setText(data.dateOfBirth)
            buttonGender.isChecked = data.gender == resources.getString(R.string.text_hint_female)
        }
    }

    private val showDatePickerDialogLDObserver = Observer<String> { dateOfBirth ->
        val currentYear: Number
        val currentMonth: Number
        val currentDayOfMonth: Int
        if (dateOfBirth.isNotEmpty()) {
            val date = dateOfBirth.split("-")
            currentYear = date[0].toInt()
            currentMonth = date[1].toInt()
            currentDayOfMonth = date[2].toInt()
        } else {
            val calendar = Calendar.getInstance()
            currentYear = calendar.get(Calendar.YEAR)
            currentMonth = calendar.get(Calendar.MONTH)
            currentDayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)
        }
        val datePickerDialogListener =
            DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                Timber.d("datePickerDialogListener: $year, $month+1, $dayOfMonth")
                val text = "$year-${month+1}-$dayOfMonth"
                viewBinding.userDateOfBirth.setText(text)
            }
        val datePickerDialog = DatePickerDialog(
            requireContext(),
            R.style.DatePickerDialog,
            datePickerDialogListener,
            currentYear,
            currentMonth-1,
            currentDayOfMonth,
        )
        datePickerDialog.window?.setBackgroundDrawable(
            AppCompatResources.getDrawable(
                requireContext(),
                R.color.color_theme_status_bar
            )
        )
        datePickerDialog.show()
    }

}