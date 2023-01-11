package uz.gita.fooddelivery.presentation.viewmodels.profile

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
class ProfileVMImpl
@Inject constructor(
    private val useCase: ProfileUC
) : ViewModel(), ProfileVM {
    override val errorMessageLD: MutableLiveData<String> by lazy { MutableLiveData<String>() }
    override val navigateNextScreenLD: MutableLiveData<Int> by lazy { MutableLiveData<Int>() }
    override val navigateEditProfileScreenLD: MutableLiveData<Unit> by lazy { MutableLiveData<Unit>() }
    override val profileDataLD: MutableLiveData<ProfileData> by lazy { MutableLiveData<ProfileData>() }

    init {
        setProfileData()
    }

    private fun setProfileData() {
        useCase.profileData()
            .onEach { result ->
                result.onSuccess {
                    profileDataLD.value = it
                }
                result.onFailure { throwable ->
                    errorMessageLD.value = throwable.message
                }
            }
            .launchIn(viewModelScope)
    }

    override fun onClickEditProfile() {
        navigateEditProfileScreenLD.value = Unit
    }

    override fun onClickTabButton(position: Int) {
        navigateNextScreenLD.value = position
    }
}