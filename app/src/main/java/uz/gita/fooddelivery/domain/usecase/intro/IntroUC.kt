package uz.gita.fooddelivery.domain.usecase.intro

import uz.gita.fooddelivery.data.model.IntroData

interface IntroUC {

    fun introData(): List<IntroData>
    fun dismissFirstLaunch()

}