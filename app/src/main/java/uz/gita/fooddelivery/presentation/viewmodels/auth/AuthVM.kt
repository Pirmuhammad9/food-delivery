package uz.gita.fooddelivery.presentation.viewmodels.auth

import androidx.lifecycle.LiveData

interface AuthVM {

    val navigateNextScreenLD: LiveData<Int>

    fun onClickTabButton(position:Int)

}