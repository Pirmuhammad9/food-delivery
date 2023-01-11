package uz.gita.fooddelivery.presentation.viewmodels.profile

import androidx.lifecycle.LiveData
import uz.gita.fooddelivery.data.model.ProfileData

interface ProfileVM {

    val errorMessageLD: LiveData<String>
    val navigateNextScreenLD: LiveData<Int>
    val navigateEditProfileScreenLD: LiveData<Unit>
    val profileDataLD: LiveData<ProfileData>

    fun onClickEditProfile()
    fun onClickTabButton(position: Int)

}