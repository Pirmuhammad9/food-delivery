package uz.gita.fooddelivery.presentation.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import uz.gita.fooddelivery.R
import uz.gita.fooddelivery.databinding.ScreenIntroBinding
import uz.gita.fooddelivery.data.model.IntroData
import uz.gita.fooddelivery.presentation.ui.adapter.IntroAdapter
import uz.gita.fooddelivery.presentation.viewmodels.intro.IntroVM
import uz.gita.fooddelivery.presentation.viewmodels.intro.IntroVMImpl
import uz.gita.fooddelivery.utils.onClick

@AndroidEntryPoint
class IntroScreen : Fragment(R.layout.screen_intro) {

    private val viewBinding by viewBinding(ScreenIntroBinding::bind)
    private val viewModel: IntroVM by viewModels<IntroVMImpl>()
    private val adapter: IntroAdapter by lazy { IntroAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) = with(viewModel) {
        super.onCreate(savedInstanceState)
        introDataLD.observe(this@IntroScreen, introDataLDObserver)
        openNextPageLD.observe(this@IntroScreen, openNextPageLDObserver)
        isLastPageLD.observe(this@IntroScreen, isLastPageLDObserver)
        navigateNextScreenLD.observe(this@IntroScreen) {
            Timber.d("navigateNextScreenLD: $it")
            findNavController().navigate(R.id.action_introScreen_to_authScreen)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        subscribeViewBinding(viewBinding)
    }

    private fun subscribeViewBinding(viewBinding: ScreenIntroBinding) = with(viewBinding) {
        viewpager.adapter = adapter
        TabLayoutMediator(tabLayout, viewpager) { _, _ -> }.attach()
        viewpager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                viewModel.setSelectedPagePosition(position)
            }
        })
        buttonNext.onClick {
            viewModel.onClickNext(viewpager.currentItem)
        }
    }

    private val introDataLDObserver = Observer<List<IntroData>> {
        adapter.submitList(it)
    }
    private val openNextPageLDObserver = Observer<Unit> {
        viewBinding.apply {
            viewpager.currentItem = tabLayout.selectedTabPosition + 1
        }
    }

    private val isLastPageLDObserver = Observer<Boolean> {
        when (it) {
            false -> viewBinding.buttonNext.text = getString(R.string.text_intro_next)
            else -> viewBinding.buttonNext.text = getString(R.string.text_intro_start)
        }
    }
}