package uz.gita.fooddelivery.presentation.viewmodels.intro

import androidx.lifecycle.LiveData
import uz.gita.fooddelivery.data.model.IntroData

interface IntroVM {

    val introDataLD: LiveData<List<IntroData>>
    val navigateNextScreenLD: LiveData<Unit>
    val openNextPageLD: LiveData<Unit>
    val isLastPageLD: LiveData<Boolean>

    fun onClickNext(selectedTabPosition: Int)
    fun setSelectedPagePosition(currentPosition: Int)

}