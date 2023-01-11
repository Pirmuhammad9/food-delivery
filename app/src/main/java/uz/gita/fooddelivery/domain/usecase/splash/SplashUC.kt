package uz.gita.fooddelivery.domain.usecase.splash

import kotlinx.coroutines.flow.Flow

interface SplashUC {

    fun navigateNextScreen(): Flow<Result<Int>>

}