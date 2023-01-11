package uz.gita.fooddelivery.presentation.viewmodels.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import timber.log.Timber
import uz.gita.fooddelivery.R
import uz.gita.fooddelivery.domain.usecase.main.MainUC
import javax.inject.Inject

@HiltViewModel
class MainVMImpl
@Inject constructor(
    private val useCase: MainUC
) : ViewModel(), MainVM {

    override val navigateNextScreenLD: MutableLiveData<Int> by lazy { MutableLiveData<Int>() }
    override val toolbarTitleLD: MutableLiveData<String> by lazy { MutableLiveData<String>() }
    override val messageLD: MutableLiveData<Int> by lazy { MutableLiveData<Int>() }
    override val signOutIconStatusLD: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>() }
    override val signOutLD: MutableLiveData<Pair<Int, Int>> by lazy { MutableLiveData<Pair<Int, Int>>() }
    override val signOutActionLD: MutableLiveData<Unit> by lazy { MutableLiveData<Unit>() }

    override fun onMenuSelected(position: Int, title: String) {
        Timber.d("onMenuSelected: $position")
        signOutIconStatusLD.value = position == 3
        navigateNextScreenLD.value = position
        toolbarTitleLD.value = title
    }

    override fun onBackPressed() {
        messageLD.value = R.string.text_main_exit_text
    }

    override fun onClickSignOut() {
        signOutLD.value =
            Pair(R.string.text_main_sign_out_message, R.string.text_main_sign_out_action)
    }

    override fun onClickSignOutAction() {
        Timber.d("onClickSignOutAction")
        useCase.signOut()
        signOutActionLD.value = Unit
    }

}
