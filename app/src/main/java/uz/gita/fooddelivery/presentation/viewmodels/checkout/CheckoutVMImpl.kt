package uz.gita.fooddelivery.presentation.viewmodels.checkout

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import uz.gita.fooddelivery.data.model.CartData
import uz.gita.fooddelivery.domain.usecase.checkout.CheckoutUC
import javax.inject.Inject

@HiltViewModel
class CheckoutVMImpl
@Inject constructor(
    private val useCase: CheckoutUC
) : ViewModel(), CheckoutVM {

    override val userFullNameLD: MutableLiveData<String> by lazy { MutableLiveData<String>() }
    override val userAddressLD: MutableLiveData<String> by lazy { MutableLiveData<String>() }
    override val userTotalPaymentLD: MutableLiveData<Int> by lazy { MutableLiveData<Int>() }
    override val checkoutFoodsList: MutableLiveData<List<CartData>> by lazy { MutableLiveData<List<CartData>>() }
    override val errorMessageLD: MutableLiveData<Int> by lazy { MutableLiveData<Int>() }
    override val networkErrorMessageLD: MutableLiveData<String> by lazy { MutableLiveData<String>() }
    override val popBackStackLD: MutableLiveData<Unit> by lazy { MutableLiveData<Unit>() }

    init {
        setUserData()
        setCartData()
    }

    private fun setUserData() {
        useCase
            .userData()
            .onEach { result ->
                result.onSuccess {
                    userFullNameLD.value = it.first
                    userAddressLD.value = it.second
                }
                result.onFailure { networkErrorMessageLD.value = it.message }
            }
            .launchIn(viewModelScope)
    }

    private fun setCartData() {
        useCase
            .cartData()
            .onEach { result ->
                result.onSuccess {
                    checkoutFoodsList.value = it.first
                    userTotalPaymentLD.value = it.second
                }
                result.onFailure { networkErrorMessageLD.value = it.message }
            }
            .launchIn(viewModelScope)
    }

    override fun onClickClose() {
        popBackStackLD.value = Unit
    }

    override fun onClickPlaceOrder() {
        useCase
            .placeOrder()
            .onEach { result ->
                result.onSuccess { popBackStackLD.value = Unit }
                result.onFailure { networkErrorMessageLD.value = it.message }
            }
            .launchIn(viewModelScope)
    }

}