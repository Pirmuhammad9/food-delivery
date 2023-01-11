package uz.gita.fooddelivery.presentation.viewmodels.main

import androidx.lifecycle.LiveData

interface MainVM {

    val navigateNextScreenLD: LiveData<Int>
    val toolbarTitleLD: LiveData<String>
    val messageLD: LiveData<Int>
    val signOutIconStatusLD: LiveData<Boolean>
    val signOutLD: LiveData<Pair<Int, Int>>
    val signOutActionLD: LiveData<Unit>

    fun onMenuSelected(position: Int, title: String)
    fun onBackPressed()
    fun onClickSignOut()
    fun onClickSignOutAction()

}