package uz.gita.fooddelivery.data.local

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import uz.gita.fooddelivery.utils.SharedPreference
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MySharedPreferences
@Inject constructor(@ApplicationContext context: Context) : SharedPreference(context) {

    var isFirstLaunch: Boolean by BooleanPreference(true)
    var isRememberUser: Boolean by BooleanPreference(false)

    var currentUserPhone: String by StringPreference("")
    var currentUID: String by StringPreference("")
}