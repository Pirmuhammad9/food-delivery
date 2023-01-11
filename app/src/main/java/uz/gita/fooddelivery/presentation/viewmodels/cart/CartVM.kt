package uz.gita.fooddelivery.presentation.viewmodels.cart

import androidx.lifecycle.LiveData
import uz.gita.fooddelivery.data.model.CartData

interface CartVM {

    val navigateCheckoutScreenLD:LiveData<Unit>
    val placeholderStatusLD: LiveData<Boolean>
    val cartListStatusLD: LiveData<Boolean>
    val errorMessageLD: LiveData<Int>
    val networkErrorMessage: LiveData<String>
    val deleteFoodMessage: LiveData<Pair<Int, Int>>
    val cartDataLD: LiveData<List<CartData>>
    val checkOutButtonStatusLD: LiveData<Boolean>

    fun loadData()
    fun deleteFoodStatus(status: Boolean)
    fun onClickDeleteFood(id: Long)
    fun onClickUpdateFood(id: Long, pieces:Int)
    fun onClickCheckout()

}