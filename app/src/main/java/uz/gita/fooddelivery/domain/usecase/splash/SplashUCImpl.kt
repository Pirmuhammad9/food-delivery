package uz.gita.fooddelivery.domain.usecase.splash

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import uz.gita.fooddelivery.data.repository.app.AppRepository
import javax.inject.Inject

class SplashUCImpl
@Inject constructor(
    private val repository: AppRepository
) : SplashUC {

    override fun navigateNextScreen() = flow<Result<Int>> {
        if (repository.checkForFirstLaunch()) emit(Result.success(0))
        if (repository.checkUserForAvailable()) emit(Result.success(2))
        else emit(Result.success(1))
    }.flowOn(Dispatchers.Default)

}