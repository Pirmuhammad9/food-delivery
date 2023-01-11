package uz.gita.fooddelivery.presentation.viewmodels.history

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import uz.gita.fooddelivery.data.model.CartData
import uz.gita.fooddelivery.domain.usecase.profile.ProfileUC
import javax.inject.Inject

@HiltViewModel
class HistoryVMImpl
@Inject constructor(
    useCase: ProfileUC
) : ViewModel(), HistoryVM {
    override val networkErrorMessageLD: MutableLiveData<String> by lazy { MutableLiveData<String>() }
    override val historyListLD: MutableLiveData<List<CartData>> by lazy { MutableLiveData<List<CartData>>() }

    init {
        useCase
            .historyData()
            .onEach { result ->
                result.onSuccess { historyListLD.value = it }
                result.onFailure { networkErrorMessageLD.value = it.message }
            }
            .launchIn(viewModelScope)
    }

}