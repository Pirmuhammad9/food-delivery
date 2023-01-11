package uz.gita.fooddelivery.presentation.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import uz.gita.fooddelivery.R
import uz.gita.fooddelivery.databinding.ScreenAboutBinding
import uz.gita.fooddelivery.data.model.ProfileData
import uz.gita.fooddelivery.presentation.viewmodels.about.AboutVM
import uz.gita.fooddelivery.presentation.viewmodels.about.AboutVMImpl
import uz.gita.fooddelivery.utils.snackErrorMessage

@AndroidEntryPoint
class AboutScreen : Fragment(R.layout.screen_about) {

    private val viewBinding by viewBinding(ScreenAboutBinding::bind)
    private val viewModel: AboutVM by viewModels<AboutVMImpl>()

    override fun onCreate(savedInstanceState: Bundle?) = with(viewModel) {
        super.onCreate(savedInstanceState)
        errorLD.observe(this@AboutScreen) { snackErrorMessage(it) }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = with(viewModel) {
        setProfileDataLD.observe(viewLifecycleOwner, setProfileDataLDObserver)
    }

    private val setProfileDataLDObserver = Observer<ProfileData> { data ->
        viewBinding.apply {
            textFullName.text =
                resources.getString(
                    R.string.text_profile_full_name,
                    data.firstname,
                    data.lastname
                )
            textPhone.text = data.phone
            if (data.location.isNotEmpty()) textLocation.text = data.location
            else textLocation.text = resources.getString(R.string.text_profile_no_data)
            if (data.dateOfBirth.isNotEmpty()) textDateOfBirth.text = data.dateOfBirth
            else textDateOfBirth.text = resources.getString(R.string.text_profile_no_data)
            if (data.gender.isNotEmpty()) textGender.text = data.gender
            else textGender.text = resources.getString(R.string.text_profile_no_data)
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.updateProfileData()
    }
}