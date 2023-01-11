package uz.gita.fooddelivery.presentation.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import uz.gita.fooddelivery.R
import uz.gita.fooddelivery.databinding.ScreenAuthBinding
import uz.gita.fooddelivery.presentation.viewmodels.auth.AuthVMImpl
import uz.gita.fooddelivery.presentation.ui.adapter.AuthAdapter
import uz.gita.fooddelivery.presentation.viewmodels.auth.AuthVM
import uz.gita.fooddelivery.utils.onClick

@AndroidEntryPoint
class AuthScreen : Fragment(R.layout.screen_auth) {

    private val viewBinding by viewBinding(ScreenAuthBinding::bind)
    private val viewModel: AuthVM by viewModels<AuthVMImpl>()
    private lateinit var adapter: AuthAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        subscribeViewBinding(viewBinding)
        subscribeViewModel(viewModel)
    }

    private fun subscribeViewBinding(viewBinding: ScreenAuthBinding) = with(viewBinding) {
        adapter = AuthAdapter(childFragmentManager, lifecycle)
        viewpagerAuth.adapter = adapter
        TabLayoutMediator(tabLayout, viewpagerAuth) { tab, position -> }.attach()
        buttonSignIn.onClick { viewModel.onClickTabButton(0) }
        buttonRegister.onClick { viewModel.onClickTabButton(1) }
    }

    private fun subscribeViewModel(viewModel: AuthVM) = with(viewModel) {
        navigateNextScreenLD.observe(viewLifecycleOwner, navigateNextScreenLDObserver)
    }

    private val navigateNextScreenLDObserver = Observer<Int> {
        viewBinding.viewpagerAuth.setCurrentItem(it, true)
    }
}