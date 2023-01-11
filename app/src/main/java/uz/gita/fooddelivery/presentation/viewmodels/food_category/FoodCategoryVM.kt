package uz.gita.fooddelivery.presentation.viewmodels.food_category

import androidx.lifecycle.LiveData
import uz.gita.fooddelivery.data.model.FoodData

interface FoodCategoryVM {

    val foodCategoryTitleLD: LiveData<String>
    val foodsList: LiveData<List<FoodData>>
    val popBackStackLD: LiveData<Unit>

    fun onClickBack()

}