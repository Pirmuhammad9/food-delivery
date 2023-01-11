package uz.gita.fooddelivery.presentation.viewmodels.food_detail

import androidx.lifecycle.LiveData

interface FoodDetailVM {

    val errorMessageLD: LiveData<String>
    val popBackStack: LiveData<Unit>

    fun onClickAddCart(name: String, price: Int, image: String)
    fun onClickBack()

}