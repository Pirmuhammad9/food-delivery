package uz.gita.fooddelivery.domain.usecase.main

import uz.gita.fooddelivery.data.repository.auth.AuthRepository
import javax.inject.Inject

class MainUCImpl
@Inject constructor(
    private val repository: AuthRepository
) : MainUC {

    override fun signOut() = kotlin.run { repository.signOut() }

}