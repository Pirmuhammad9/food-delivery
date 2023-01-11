package uz.gita.fooddelivery.presentation.viewmodels.search

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import uz.gita.fooddelivery.data.model.FoodData
import uz.gita.fooddelivery.domain.usecase.search.SearchUC
import javax.inject.Inject

@HiltViewModel
class SearchVMImpl
@Inject constructor(
    private val useCase: SearchUC
) : ViewModel(), SearchVM {

    override val placeholderStatusLD: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>() }
    override val searchResultLD: MutableLiveData<List<FoodData>> by lazy { MutableLiveData<List<FoodData>>() }
    override val networkErrorMessageLD: MutableLiveData<String> by lazy { MutableLiveData<String>() }
    override val searchListStatusLD: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>() }
    override val noResultStatusLD: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>() }
    override val navigateFoodDetailsLD: MutableLiveData<FoodData> by lazy { MutableLiveData<FoodData>() }

    private var job: Job? = null

    override fun onSearch(query: String?) {
        if (query.isNullOrEmpty()) {
            placeholderStatusLD.value = true
            noResultStatusLD.value = false
            searchListStatusLD.value = false
            return
        }
        job?.cancel()
        job = useCase
            .foodsResultByQuery(query)
            .onEach { result ->
                result.onSuccess { data ->
                    placeholderStatusLD.value = false
                    if (data.isEmpty()) {
                        searchListStatusLD.value = false
                        noResultStatusLD.value = true
                        return@onEach
                    }
                    noResultStatusLD.value = false
                    searchListStatusLD.value = true
                    searchResultLD.value = data
                }
                result.onFailure { exception ->
                    networkErrorMessageLD.value = exception.message
                }
            }
            .launchIn(viewModelScope)
        /*job = viewModelScope.launch(Dispatchers.Default) {
            delay(1000)
            repository.foodsDataByQuery(query)
                .collectLatest { result ->
                    result.onSuccess { data ->
                        placeholderStatusLD.postValue(false)
                        if (data.isEmpty()) {
                            searchListStatusLD.postValue(true)
                            return@collectLatest
                        }
                        searchListStatusLD.postValue(false)
                        searchResultLD.postValue(data)
                    }
                    result.onFailure { exception ->
                        networkErrorMessageLD.postValue(exception.message)
                    }
                }*/
    }

    override fun onClickFoodItem(data: FoodData) {
        navigateFoodDetailsLD.value = data
    }

    override fun onClickClose() {
        placeholderStatusLD.value = true
        searchListStatusLD.value = false
        noResultStatusLD.value = false
    }

}