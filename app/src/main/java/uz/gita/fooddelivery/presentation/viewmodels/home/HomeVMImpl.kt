package uz.gita.fooddelivery.presentation.viewmodels.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import uz.gita.fooddelivery.data.model.AdData
import uz.gita.fooddelivery.data.model.CategoryData
import uz.gita.fooddelivery.data.model.FoodData
import uz.gita.fooddelivery.data.model.FoodsGroupData
import uz.gita.fooddelivery.domain.usecase.home.HomeUC
import javax.inject.Inject

@HiltViewModel
class HomeVMImpl
@Inject constructor(
    private val useCase: HomeUC
) : ViewModel(), HomeVM {

    override val adsDataLD: MutableLiveData<List<AdData>> by lazy { MutableLiveData<List<AdData>>() }
    override val categoriesDataLD: MutableLiveData<List<CategoryData>> by lazy { MutableLiveData<List<CategoryData>>() }
    override val foodsDataLD: MutableLiveData<List<FoodsGroupData>> by lazy { MutableLiveData<List<FoodsGroupData>>() }
    override val networkErrorMessage: MutableLiveData<String> by lazy { MutableLiveData<String>() }
    override val viewPagerNextPageLD: MutableLiveData<Int> by lazy { MutableLiveData<Int>() }

    override val foodsByCategoryLD: MutableLiveData<FoodsGroupData> by lazy { MutableLiveData<FoodsGroupData>() }
    override val navigateFoodDetailsLD: MutableLiveData<FoodData> by lazy { MutableLiveData<FoodData>() }

    private val clickedCategories: MutableList<Int> by lazy { ArrayList() }
    private var job: Job? = null

    init {
        adsData()
        foodsData()
    }

    private fun adsData() {
        useCase
            .adsData()
            .onEach { result ->
                result.onSuccess { data -> adsDataLD.value = data }
                result.onFailure { exception -> networkErrorMessage.value = exception.message }
            }
            .launchIn(viewModelScope)
    }

    private fun foodsData() {
        useCase
            .categoryListWithFoodsList()
            .onEach { result ->
                result.onSuccess { data ->
                    categoriesDataLD.value = data.first
                    foodsDataLD.value = data.second
                }
                result.onFailure { exception -> networkErrorMessage.value = exception.message }
            }
            .launchIn(viewModelScope)
    }

    private fun selectedFoodsData() {
        useCase
            .selectedCategoriesListWithFoodsList(clickedCategories)
            .onEach { result ->
                result.onSuccess { data -> foodsDataLD.value = data }
                result.onFailure { exception -> networkErrorMessage.value = exception.message }
            }
            .launchIn(viewModelScope)
    }

    override fun onPageSelected(position: Int) {
        job?.cancel()
        job = viewModelScope.launch(Dispatchers.Default) {
            delay(2000)
            viewPagerNextPageLD.postValue(position + 1)
        }
    }

    override fun onClickCategory(categoryId: Int, isSelected: Boolean) {
        when {
            isSelected -> clickedCategories.add(categoryId)
            else -> clickedCategories.remove(categoryId)
        }
        if (clickedCategories.isEmpty()) foodsData()
        else selectedFoodsData()
    }

    override fun onClickViewAll(foodsGroupData: FoodsGroupData) {
        foodsByCategoryLD.value = foodsGroupData
    }

    override fun onClickFood(foodData: FoodData) {
        navigateFoodDetailsLD.value = foodData
    }
}