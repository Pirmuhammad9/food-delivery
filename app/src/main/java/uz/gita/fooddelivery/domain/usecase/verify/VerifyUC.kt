package uz.gita.fooddelivery.domain.usecase.verify

import kotlinx.coroutines.flow.Flow

interface VerifyUC {

    fun registerUser(
        verificationId: String,
        smsCode: String,
        firstname: String,
        lastname: String,
        phone: String,
        password: String,
        isRemember: Boolean
    ): Flow<Result<Unit>>

    fun restoreUserPassword(
        verificationId: String,
        smsCode: String,
        phone: String,
        password: String
    ): Flow<Result<String>>

}