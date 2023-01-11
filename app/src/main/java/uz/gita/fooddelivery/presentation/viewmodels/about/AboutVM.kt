package uz.gita.fooddelivery.presentation.viewmodels.about

import androidx.lifecycle.LiveData
import uz.gita.fooddelivery.data.model.ProfileData

interface AboutVM {

    val setProfileDataLD: LiveData<ProfileData>
    val errorLD: LiveData<String>

    /*val errorMessageLD: LiveData<Int>
    val popBackStackLD: LiveData<Unit>*/
    fun updateProfileData()

}