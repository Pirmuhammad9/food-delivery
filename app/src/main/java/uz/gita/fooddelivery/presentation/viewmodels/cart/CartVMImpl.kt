package uz.gita.fooddelivery.presentation.viewmodels.cart

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import uz.gita.fooddelivery.R
import uz.gita.fooddelivery.data.model.CartData
import uz.gita.fooddelivery.domain.usecase.cart.CartUC
import javax.inject.Inject

@HiltViewModel
class CartVMImpl
@Inject constructor(
    private val useCase: CartUC
) : ViewModel(), CartVM {
    override val navigateCheckoutScreenLD: MutableLiveData<Unit> by lazy { MutableLiveData<Unit>() }
    override val placeholderStatusLD: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>() }
    override val cartListStatusLD: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>() }
    override val errorMessageLD: MutableLiveData<Int> by lazy { MutableLiveData<Int>() }
    override val networkErrorMessage: MutableLiveData<String> by lazy { MutableLiveData<String>() }
    override val deleteFoodMessage: MutableLiveData<Pair<Int, Int>> by lazy { MutableLiveData<Pair<Int, Int>>() }
    override val cartDataLD: MutableLiveData<List<CartData>> by lazy { MutableLiveData<List<CartData>>() }
    override val checkOutButtonStatusLD: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>() }

    private var deletedItemId = 0L

    override fun loadData() {
        setData()
    }

    override fun deleteFoodStatus(status: Boolean) {
        if (status) {
            useCase
                .deleteFood(deletedItemId)
                .onEach { result ->
                    result.onSuccess {
                        setData()
                        deletedItemId = 0
                    }
                    result.onFailure { networkErrorMessage.value = it.message }
                }
                .launchIn(viewModelScope)
        } else checkOutButtonStatusLD.value = true
    }

    override fun onClickDeleteFood(id: Long) {
        checkOutButtonStatusLD.value = false
        deletedItemId = id
        deleteFoodMessage.value =
            Pair(R.string.text_cart_food_delete_text, R.string.text_cart_food_delete)
    }

    override fun onClickUpdateFood(id: Long, pieces: Int) {
        useCase
            .updateFoodCount(id, pieces)
            .onEach { result ->
                result.onSuccess { }
                result.onFailure { networkErrorMessage.value = it.message }
            }
            .launchIn(viewModelScope)
    }

    override fun onClickCheckout() {
        navigateCheckoutScreenLD.value = Unit
    }


    private fun setData() {
        useCase
            .cartData()
            .onEach { result ->
                result.onSuccess { data ->
                    if (data.isNotEmpty()) {
                        placeholderStatusLD.value = false
                        cartListStatusLD.value = true
                        checkOutButtonStatusLD.value = true
                        cartDataLD.value = data
                    } else {
                        placeholderStatusLD.value = true
                        cartListStatusLD.value = false
                        checkOutButtonStatusLD.value = false
                    }
                }
                result.onFailure {
                    placeholderStatusLD.value = true
                    cartListStatusLD.value = false
                    checkOutButtonStatusLD.value = false
                    networkErrorMessage.value = it.message
                }
            }
            .launchIn(viewModelScope)
    }
}