package uz.gita.fooddelivery.presentation.viewmodels.about

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import uz.gita.fooddelivery.data.model.ProfileData
import uz.gita.fooddelivery.domain.usecase.profile.ProfileUC
import javax.inject.Inject

@HiltViewModel
class AboutVMImpl
@Inject constructor(
    private val useCase: ProfileUC
) : ViewModel(), AboutVM {

    override val setProfileDataLD: MutableLiveData<ProfileData> by lazy { MutableLiveData<ProfileData>() }
    override val errorLD: MutableLiveData<String> by lazy { MutableLiveData<String>() }
    /*override val errorMessageLD: MutableLiveData<Int> by lazy { MutableLiveData<Int>() }
    override val popBackStackLD: MutableLiveData<Unit> by lazy { MutableLiveData<Unit>() }*/

    init {
        setData()
    }

    override fun updateProfileData() {
        setData()
    }

    private fun setData() {
        useCase
            .profileData()
            .onEach { result ->
                result.onSuccess { data ->
                    setProfileDataLD.value = data
                }
                result.onFailure {
                    errorLD.value = it.message
                }
            }
            .launchIn(viewModelScope)
    }
}