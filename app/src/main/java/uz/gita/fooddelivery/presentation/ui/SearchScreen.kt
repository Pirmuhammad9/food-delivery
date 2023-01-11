package uz.gita.fooddelivery.presentation.ui

import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import uz.gita.fooddelivery.R
import uz.gita.fooddelivery.databinding.ScreenSearchBinding
import uz.gita.fooddelivery.data.model.FoodData
import uz.gita.fooddelivery.presentation.ui.adapter.SearchAdapter
import uz.gita.fooddelivery.presentation.viewmodels.search.SearchVM
import uz.gita.fooddelivery.presentation.viewmodels.search.SearchVMImpl
import uz.gita.fooddelivery.utils.snackErrorMessage

@AndroidEntryPoint
class SearchScreen : Fragment(R.layout.screen_search), SearchView.OnQueryTextListener {

    private val viewBinding by viewBinding(ScreenSearchBinding::bind)
    private val viewModel: SearchVM by viewModels<SearchVMImpl>()
    private val adapter: SearchAdapter by lazy { SearchAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) = with(viewModel) {
        super.onCreate(savedInstanceState)
        networkErrorMessageLD.observe(this@SearchScreen) { snackErrorMessage(it) }
        navigateFoodDetailsLD.observe(this@SearchScreen) {
            findNavController().navigate(
                MainScreenDirections.actionMainScreenToFoodDetailScreen(
                    it.id,
                    it.price,
                    it.image,
                    it.name,
                    it.description
                )
            )
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        subscribeViewBinding(viewBinding)
        subscribeViewModel(viewModel)
    }

    private fun subscribeViewBinding(viewBinding: ScreenSearchBinding) = with(viewBinding) {
        listSearch.adapter = adapter
        listSearch.layoutManager = LinearLayoutManager(requireContext())
        adapter.setOnClickFoodItemListener { viewModel.onClickFoodItem(it) }
        searchView.setOnQueryTextListener(this@SearchScreen)
        searchView.setOnCloseListener {
            searchView.setQuery(null, true)
            viewModel.onClickClose()
            return@setOnCloseListener true
        }
    }

    private fun subscribeViewModel(vieModel: SearchVM) = with(vieModel) {
        searchResultLD.observe(viewLifecycleOwner, searchResultLDObserver)
        placeholderStatusLD.observe(viewLifecycleOwner, placeholderStatusLDDObserver)
        searchListStatusLD.observe(viewLifecycleOwner, searchListStatusLDObserver)
        noResultStatusLD.observe(viewLifecycleOwner, noResultStatusLDObserver)
    }

    private val searchResultLDObserver = Observer<List<FoodData>> {
        adapter.submitList(it)
    }

    private val placeholderStatusLDDObserver = Observer<Boolean> { status ->
        Timber.tag("SearchScreen").d("placeholderStatusLDDObserver: $status")
        viewBinding.searchPlaceholder.apply {
            visibility = when (status) {
                true -> VISIBLE
                else -> GONE
            }
        }
    }

    private val searchListStatusLDObserver = Observer<Boolean> { status ->
        Timber.tag("SearchScreen").d("searchListStatusLDObserver: $status")
        viewBinding.listSearch.apply {
            visibility = when (status) {
                true -> VISIBLE
                else -> GONE
            }
        }
    }

    private val noResultStatusLDObserver = Observer<Boolean> { status ->
        Timber.tag("SearchScreen").d("noResultStatusLDObserver: $status")
        viewBinding.searchNoResult.apply {
            visibility = when (status) {
                true -> VISIBLE
                else -> GONE
            }
        }
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        viewModel.onSearch(query)
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        viewModel.onSearch(newText)
        return true
    }

}