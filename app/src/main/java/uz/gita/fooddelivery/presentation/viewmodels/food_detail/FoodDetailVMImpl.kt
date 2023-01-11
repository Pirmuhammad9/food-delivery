package uz.gita.fooddelivery.presentation.viewmodels.food_detail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import uz.gita.fooddelivery.domain.usecase.food_detail.FoodDetailUC
import javax.inject.Inject

@HiltViewModel
class FoodDetailVMImpl
@Inject constructor(
    private val useCase: FoodDetailUC
) : ViewModel(), FoodDetailVM {

    override val errorMessageLD: MutableLiveData<String> by lazy { MutableLiveData<String>() }
    override val popBackStack: MutableLiveData<Unit> by lazy { MutableLiveData<Unit>() }

    override fun onClickAddCart(name: String, price: Int, image: String) {
        useCase
            .addFoodToCart(name, price, image)
            .onEach { result ->
                result.onSuccess { popBackStack.value = it }
                result.onFailure { errorMessageLD.value = it.message }
            }
            .launchIn(viewModelScope)
    }

    override fun onClickBack() {
        popBackStack.value = Unit
    }
}