package uz.gita.fooddelivery.presentation.viewmodels.food_category

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import uz.gita.fooddelivery.data.model.FoodData
import javax.inject.Inject

@HiltViewModel
class FoodCategoryVMImpl
@Inject constructor(

) : ViewModel(), FoodCategoryVM {

    override val foodCategoryTitleLD: MutableLiveData<String> by lazy { MutableLiveData<String>() }
    override val foodsList: MutableLiveData<List<FoodData>> by lazy { MutableLiveData<List<FoodData>>() }
    override val popBackStackLD: MutableLiveData<Unit> by lazy { MutableLiveData<Unit>() }

    override fun onClickBack() {
        popBackStackLD.value = Unit
    }
}