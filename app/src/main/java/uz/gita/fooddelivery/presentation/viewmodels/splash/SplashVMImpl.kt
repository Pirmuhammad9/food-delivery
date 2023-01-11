package uz.gita.fooddelivery.presentation.viewmodels.splash

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import uz.gita.fooddelivery.domain.usecase.splash.SplashUC
import javax.inject.Inject

@HiltViewModel
class SplashVMImpl
@Inject constructor(
    useCase: SplashUC
) : ViewModel(), SplashVM {

    override val introLD: MutableLiveData<Unit> by lazy { MutableLiveData<Unit>() }
    override val authLD: MutableLiveData<Unit> by lazy { MutableLiveData<Unit>() }
    override val mainLD: MutableLiveData<Unit> by lazy { MutableLiveData<Unit>() }

    init {
        useCase.navigateNextScreen()
            .onEach { delay(2000) }
            .onEach { result ->
                result.onSuccess {
                    when (it) {
                        0 -> introLD.value = Unit
                        1 -> authLD.value = Unit
                        else -> mainLD.value = Unit
                    }
                }
            }
            .launchIn(viewModelScope)
    }
}