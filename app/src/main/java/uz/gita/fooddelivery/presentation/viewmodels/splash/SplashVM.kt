package uz.gita.fooddelivery.presentation.viewmodels.splash

import androidx.lifecycle.LiveData

interface SplashVM {

    val introLD: LiveData<Unit>
    val authLD: LiveData<Unit>
    val mainLD: LiveData<Unit>

}