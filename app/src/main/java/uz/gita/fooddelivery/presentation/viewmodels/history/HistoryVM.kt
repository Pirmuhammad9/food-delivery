package uz.gita.fooddelivery.presentation.viewmodels.history

import androidx.lifecycle.LiveData
import uz.gita.fooddelivery.data.model.CartData

interface HistoryVM {

    val networkErrorMessageLD: LiveData<String>
    val historyListLD: LiveData<List<CartData>>

}