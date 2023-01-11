package uz.gita.fooddelivery.presentation.viewmodels.search

import androidx.lifecycle.LiveData
import uz.gita.fooddelivery.data.model.FoodData

interface SearchVM {

    val placeholderStatusLD: LiveData<Boolean>
    val searchListStatusLD: LiveData<Boolean>
    val noResultStatusLD: LiveData<Boolean>
    val searchResultLD: LiveData<List<FoodData>>
    val networkErrorMessageLD: LiveData<String>
    val navigateFoodDetailsLD: LiveData<FoodData>

    fun onClickClose()
    fun onSearch(query: String?)
    fun onClickFoodItem(data: FoodData)

}