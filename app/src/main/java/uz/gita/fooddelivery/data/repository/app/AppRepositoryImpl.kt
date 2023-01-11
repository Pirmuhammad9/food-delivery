package uz.gita.fooddelivery.data.repository.app

import timber.log.Timber
import uz.gita.fooddelivery.R
import uz.gita.fooddelivery.data.local.MySharedPreferences
import uz.gita.fooddelivery.data.model.IntroData
import javax.inject.Inject

class AppRepositoryImpl
@Inject constructor(
    private val preferences: MySharedPreferences
) : AppRepository {

    init {
        Timber.tag("AppRepositoryImpl").d("isRemember: ${preferences.isRememberUser}")
        Timber.tag("AppRepositoryImpl").d("currentUID: ${preferences.currentUID}")
        Timber.tag("AppRepositoryImpl").d("currentUserPhone: ${preferences.currentUserPhone}")
    }

    override fun checkUserForAvailable(): Boolean =
        preferences.currentUID.isNotEmpty() && preferences.currentUserPhone.isNotEmpty() && preferences.isRememberUser

    override fun checkForFirstLaunch(): Boolean = preferences.isFirstLaunch

    override fun introData(): List<IntroData> {
        return listOf(
            IntroData(
                id = 1,
                title = R.string.text_intro_title1,
                subtitle = R.string.text_intro_subtitle1,
                image = R.drawable.ic_intro1,
                description = R.string.text_intro_description1
            ),
            IntroData(
                id = 2,
                title = R.string.text_intro_title2,
                subtitle = R.string.text_intro_subtitle2,
                image = R.drawable.ic_intro2,
                description = R.string.text_intro_description2
            ),
            IntroData(
                id = 3,
                title = R.string.text_intro_title3,
                subtitle = R.string.text_intro_subtitle3,
                image = R.drawable.ic_intro3,
                description = R.string.text_intro_description3
            )
        )
    }

    override fun dismissFirstLaunch() = kotlin.run { preferences.isFirstLaunch = false }
}