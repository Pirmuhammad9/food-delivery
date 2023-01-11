package uz.gita.fooddelivery.domain.usecase.intro

import uz.gita.fooddelivery.data.repository.app.AppRepository
import javax.inject.Inject

class IntroUCImpl
@Inject constructor(
    private val repository: AppRepository
) : IntroUC {

    override fun introData() = repository.introData()

    override fun dismissFirstLaunch() = kotlin.run { repository.dismissFirstLaunch() }

}