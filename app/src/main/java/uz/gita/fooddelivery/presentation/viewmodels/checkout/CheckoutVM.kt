package uz.gita.fooddelivery.presentation.viewmodels.checkout

import androidx.lifecycle.LiveData
import uz.gita.fooddelivery.data.model.CartData

interface CheckoutVM {

    val userFullNameLD: LiveData<String>
    val userAddressLD: LiveData<String>
    val userTotalPaymentLD: LiveData<Int>
    val checkoutFoodsList: LiveData<List<CartData>>
    val errorMessageLD: LiveData<Int>
    val networkErrorMessageLD: LiveData<String>
    val popBackStackLD: LiveData<Unit>

    fun onClickClose()
    fun onClickPlaceOrder()

}