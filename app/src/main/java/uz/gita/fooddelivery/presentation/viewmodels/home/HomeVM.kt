package uz.gita.fooddelivery.presentation.viewmodels.home

import androidx.lifecycle.LiveData
import uz.gita.fooddelivery.data.model.AdData
import uz.gita.fooddelivery.data.model.CategoryData
import uz.gita.fooddelivery.data.model.FoodData
import uz.gita.fooddelivery.data.model.FoodsGroupData

interface HomeVM {

    val adsDataLD: LiveData<List<AdData>>
    val categoriesDataLD: LiveData<List<CategoryData>>
    val foodsDataLD: LiveData<List<FoodsGroupData>>
    val networkErrorMessage: LiveData<String>
    val viewPagerNextPageLD: LiveData<Int>

    val foodsByCategoryLD: LiveData<FoodsGroupData>
    val navigateFoodDetailsLD: LiveData<FoodData>

    fun onPageSelected(position: Int)
    fun onClickCategory(categoryId: Int, isSelected: Boolean)
    fun onClickViewAll(foodsGroupData: FoodsGroupData)
    fun onClickFood(foodData: FoodData)
}