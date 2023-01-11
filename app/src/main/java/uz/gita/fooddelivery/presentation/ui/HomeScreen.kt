package uz.gita.fooddelivery.presentation.ui

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import uz.gita.fooddelivery.R
import uz.gita.fooddelivery.data.model.AdData
import uz.gita.fooddelivery.data.model.CategoryData
import uz.gita.fooddelivery.data.model.FoodsGroupData
import uz.gita.fooddelivery.databinding.ScreenHomeBinding
import uz.gita.fooddelivery.presentation.ui.adapter.AdAdapter
import uz.gita.fooddelivery.presentation.ui.adapter.CategoryAdapter
import uz.gita.fooddelivery.presentation.ui.adapter.FoodAdapterOuter
import uz.gita.fooddelivery.presentation.viewmodels.home.HomeVM
import uz.gita.fooddelivery.presentation.viewmodels.home.HomeVMImpl
import uz.gita.fooddelivery.utils.snackErrorMessage


@AndroidEntryPoint
class HomeScreen : Fragment(R.layout.screen_home) {

    private val viewBinding by viewBinding(ScreenHomeBinding::bind)
    private val viewModel: HomeVM by viewModels<HomeVMImpl>()
    private val navController: NavController by lazy(LazyThreadSafetyMode.NONE) { findNavController() }
    private val adAdapter: AdAdapter by lazy { AdAdapter() }
    private val categoryAdapter: CategoryAdapter by lazy { CategoryAdapter() }
    private val foodAdapterOuter: FoodAdapterOuter by lazy { FoodAdapterOuter() }


    override fun onCreate(savedInstanceState: Bundle?) = with(viewModel) {
        Timber.d("onCreate")
        super.onCreate(savedInstanceState)
        networkErrorMessage.observe(this@HomeScreen) { snackErrorMessage(it) }
//        foodsByCategoryLD.observe(this@HomeScreen) { navController.navigate(R.id.action_mainScreen_to_foodsScreen) }
        navigateFoodDetailsLD.observe(this@HomeScreen) { data ->
            navController.navigate(
                MainScreenDirections.actionMainScreenToFoodDetailScreen(
                    data.id,
                    data.price,
                    data.image,
                    data.name,
                    data.description
                )
            )
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Timber.d("onViewCreated")
        subscribeViewBinding(viewBinding)
        subscribeViewModel(viewModel)
    }

    private fun subscribeViewBinding(viewBinding: ScreenHomeBinding) = with(viewBinding) {
        viewpagerAds.adapter = adAdapter

        categoryAdapter.setOnItemClickListener { clickedCategoryId, isSelected ->
            viewModel.onClickCategory(clickedCategoryId, isSelected)
        }
        listCategories.adapter = categoryAdapter
        listCategories.layoutManager =
            GridLayoutManager(requireContext(), 1, GridLayoutManager.HORIZONTAL, false)


        /*foodAdapterOuter.setOnClickViewAllListener {
            Timber.d("setOnClickViewAllListener: $it")
            viewModel.onClickViewAll(it)
        }*/

        foodAdapterOuter.setOnClickFoodItemListener {
            viewModel.onClickFood(it)
        }
        listFoodsOuter.adapter = foodAdapterOuter
        listFoodsOuter.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun subscribeViewModel(viewModel: HomeVM) = with(viewModel) {
        adsDataLD.observe(viewLifecycleOwner, adsDataLDObserver)
        viewPagerNextPageLD.observe(viewLifecycleOwner, nextPageLDObserver)
        categoriesDataLD.observe(viewLifecycleOwner, categoriesDataLDObserver)
        foodsDataLD.observe(viewLifecycleOwner, foodsDataLDObserver)
    }

    private val adsDataLDObserver = Observer<List<AdData>> {
        Timber.d("adsDataLDObserver: ${it.size}")
        adAdapter.submitList(it)
        onInfinitePageChangeCallback(it.size)
        viewBinding.viewpagerAds.setCurrentItem(1, false)
    }

    private val nextPageLDObserver = Observer<Int> {
        viewBinding.viewpagerAds.setCurrentItem(it, true)
    }

    private val categoriesDataLDObserver = Observer<List<CategoryData>> {
        categoryAdapter.submitList(it)
    }

    private val foodsDataLDObserver = Observer<List<FoodsGroupData>> {
        Timber.d("foodsDataLDObserver: ${it.size}")
        foodAdapterOuter.submitList(it)
    }

    private fun onInfinitePageChangeCallback(listSize: Int) = with(viewBinding) {
        viewpagerAds.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                if (position != 0 && position != listSize - 1) {
                    viewModel.onPageSelected(position)
                }
            }

            override fun onPageScrollStateChanged(state: Int) {
                super.onPageScrollStateChanged(state)
                if (state == ViewPager2.SCROLL_STATE_IDLE || state == ViewPager2.SCROLL_STATE_DRAGGING) {
                    when (viewpagerAds.currentItem) {
                        listSize - 1 -> viewpagerAds.setCurrentItem(1, false)
                        0 -> viewpagerAds.setCurrentItem(listSize - 2, false)
                    }
                }
            }
        })
    }

    override fun onPause() {
        super.onPause()
        Timber.d("onPause")
    }

    override fun onResume() {
        super.onResume()
        Timber.d("onResume")
    }
}
