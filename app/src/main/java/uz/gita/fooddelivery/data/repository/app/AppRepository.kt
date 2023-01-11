package uz.gita.fooddelivery.data.repository.app

import uz.gita.fooddelivery.data.model.IntroData

interface AppRepository {

    // splash screen
    fun checkUserForAvailable(): Boolean
    fun checkForFirstLaunch(): Boolean

    // intro screen
    fun introData(): List<IntroData>
    fun dismissFirstLaunch()

}