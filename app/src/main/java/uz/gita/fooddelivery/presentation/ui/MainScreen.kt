package uz.gita.fooddelivery.presentation.ui

import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import uz.gita.fooddelivery.R
import uz.gita.fooddelivery.databinding.ScreenMainBinding
import uz.gita.fooddelivery.presentation.ui.adapter.MainAdapter
import uz.gita.fooddelivery.presentation.viewmodels.main.MainVM
import uz.gita.fooddelivery.presentation.viewmodels.main.MainVMImpl
import uz.gita.fooddelivery.utils.onClick
import uz.gita.fooddelivery.utils.snackMessage


@AndroidEntryPoint
class MainScreen : Fragment(R.layout.screen_main) {

    private val viewBinding by viewBinding(ScreenMainBinding::bind)
    private val viewModel: MainVM by viewModels<MainVMImpl>()
    private lateinit var adapter: MainAdapter
    private var onBackPressedTime: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) = with(viewModel) {
        Timber.d("onCreate")
        super.onCreate(savedInstanceState)
        messageLD.observe(this@MainScreen) { snackMessage(resources.getString(it)) }
        signOutLD.observe(this@MainScreen) {
            snackAction(resources.getString(it.first), resources.getString(it.second))
        }
        signOutActionLD.observe(this@MainScreen) {
            findNavController().navigate(R.id.action_mainScreen_to_authScreen)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Timber.d("onViewCreated")
        registerOnBackPressed()
        adapter = MainAdapter(childFragmentManager, lifecycle)
        subscribeViewBinding(viewBinding)
        subscribeViewModel(viewModel)
    }

    private fun subscribeViewBinding(viewBinding: ScreenMainBinding) = with(viewBinding) {

        viewpagerMain.isUserInputEnabled = false
        viewpagerMain.adapter = adapter
        buttonLogout.onClick {
            viewModel.onClickSignOut()
        }

        bottomNavigationView.setOnItemReselectedListener {
            Timber.d("bottomNavigationView: reselected")
        }

        bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.home -> {
                    viewModel.onMenuSelected(0, it.title.toString())
                    true
                }
                R.id.search -> {
                    viewModel.onMenuSelected(1, it.title.toString())
                    true
                }
                R.id.cart -> {
                    viewModel.onMenuSelected(2, it.title.toString())
                    true
                }
                else -> {
                    viewModel.onMenuSelected(3, it.title.toString())
                    true
                }
            }
        }
    }

    private fun subscribeViewModel(viewModel: MainVM) = with(viewModel) {
        navigateNextScreenLD.observe(viewLifecycleOwner, navigateNextScreenLDObserver)
        toolbarTitleLD.observe(viewLifecycleOwner, toolbarTitleLDObserver)
        signOutIconStatusLD.observe(viewLifecycleOwner, signOutIconStatusLDObserver)
    }

    private val navigateNextScreenLDObserver = Observer<Int> {
        viewBinding.viewpagerMain.setCurrentItem(it, true)
        Timber.d("viewpagerMain position: ${viewBinding.viewpagerMain.currentItem}")
    }
    private val toolbarTitleLDObserver = Observer<String> {
        viewBinding.toolbarTitle.text = it
    }

    private val signOutIconStatusLDObserver = Observer<Boolean> {
        viewBinding.buttonLogout.apply {
            visibility = when (it) {
                false -> GONE
                else -> VISIBLE
            }
        }
    }

    override fun onPause() {
        super.onPause()
        Timber.d("onPause")
    }

    override fun onResume() {
        super.onResume()
        Timber.d("onResume")
    }

    private fun registerOnBackPressed() {
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (onBackPressedTime + 2000 > System.currentTimeMillis()) {
                        Timber.d("AAA: $onBackPressedTime")
                        requireActivity().finish()
                        return
                    } else viewModel.onBackPressed()
                    onBackPressedTime = System.currentTimeMillis()
                }
            })
    }

    private fun snackAction(message: String, actionMessage: String) {
        Snackbar.make(requireView(), message, Snackbar.LENGTH_LONG).apply {
            setBackgroundTint(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.color_snack_error
                )
            )
                .setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
            setAction(actionMessage) {
                viewModel.onClickSignOutAction()
            }.setActionTextColor(
                AppCompatResources.getColorStateList(
                    requireContext(),
                    R.color.color_snack_action
                )
            )
        }.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Timber.tag("onDestroyView()")
    }
}