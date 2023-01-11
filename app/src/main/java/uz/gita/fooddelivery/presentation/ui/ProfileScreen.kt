package uz.gita.fooddelivery.presentation.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import uz.gita.fooddelivery.R
import uz.gita.fooddelivery.data.model.ProfileData
import uz.gita.fooddelivery.databinding.ScreenProfileBinding
import uz.gita.fooddelivery.presentation.ui.adapter.ProfileAdapter
import uz.gita.fooddelivery.presentation.viewmodels.profile.ProfileVM
import uz.gita.fooddelivery.presentation.viewmodels.profile.ProfileVMImpl
import uz.gita.fooddelivery.utils.onClick
import uz.gita.fooddelivery.utils.snackErrorMessage

@AndroidEntryPoint
class ProfileScreen : Fragment(R.layout.screen_profile) {

    private val viewBinding by viewBinding(ScreenProfileBinding::bind)
    private val viewModel: ProfileVM by viewModels<ProfileVMImpl>()
    private lateinit var adapter: ProfileAdapter

    override fun onCreate(savedInstanceState: Bundle?) = with(viewModel) {
        super.onCreate(savedInstanceState)
        Timber.d("onPause()")
        errorMessageLD.observe(this@ProfileScreen) { snackErrorMessage(it) }
        navigateEditProfileScreenLD.observe(this@ProfileScreen) {
            findNavController().navigate(R.id.action_mainScreen_to_profileEditScreen)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Timber.d("onPause()")
        subscribeViewBinding(viewBinding)
        subscribeViewModel(viewModel)
    }

    private fun subscribeViewBinding(viewBinding: ScreenProfileBinding) = with(viewBinding) {
        adapter = ProfileAdapter(childFragmentManager, lifecycle)
        viewpagerProfile.adapter = adapter
        TabLayoutMediator(tabLayout, viewpagerProfile) { tab, position -> }.attach()
        editProfile.onClick { viewModel.onClickEditProfile() }
        buttonAbout.onClick { viewModel.onClickTabButton(0) }
        buttonHistory.onClick { viewModel.onClickTabButton(1) }
    }

    private fun subscribeViewModel(viewModel: ProfileVM) = with(viewModel) {
        profileDataLD.observe(viewLifecycleOwner, profileDataLDObserver)
        navigateNextScreenLD.observe(viewLifecycleOwner, navigateNextScreenLDObserver)
    }

    private val profileDataLDObserver = Observer<ProfileData> { profileData ->
        viewBinding.apply {
            userImage.text = resources.getString(
                R.string.text_profile_image_text,
                profileData.firstname[0],
                profileData.lastname[0]
            )
            userFullName.text = resources.getString(
                R.string.text_profile_full_name,
                profileData.firstname,
                profileData.lastname
            )
            userPhone.text = resources.getString(
                R.string.text_profile_phone, profileData.phone
            )
        }
    }

    private val navigateNextScreenLDObserver = Observer<Int> {
        viewBinding.viewpagerProfile.setCurrentItem(it, true)
    }

    override fun onResume() {
        super.onResume()
        Timber.d("onResume()")
    }

    override fun onPause() {
        super.onPause()
        Timber.d("onPause()")
    }

}