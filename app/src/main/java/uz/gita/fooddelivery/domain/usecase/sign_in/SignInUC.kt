package uz.gita.fooddelivery.domain.usecase.sign_in

import kotlinx.coroutines.flow.Flow


interface SignInUC {

    fun signInUser(
        phone: String,
        password: String,
        rememberStatus: Boolean
    ): Flow<Result<Unit>>

    fun forgotPassword(

    )
}