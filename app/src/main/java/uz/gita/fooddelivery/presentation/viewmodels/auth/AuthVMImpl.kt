package uz.gita.fooddelivery.presentation.viewmodels.auth

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AuthVMImpl
@Inject constructor(

) : ViewModel(), AuthVM {

    override val navigateNextScreenLD: MutableLiveData<Int> by lazy { MutableLiveData<Int>() }

    override fun onClickTabButton(position: Int) {
        navigateNextScreenLD.value = position
    }

}