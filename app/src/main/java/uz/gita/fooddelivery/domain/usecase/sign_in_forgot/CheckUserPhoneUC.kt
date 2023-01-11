package uz.gita.fooddelivery.domain.usecase.sign_in_forgot

import kotlinx.coroutines.flow.Flow

interface CheckUserPhoneUC {

    fun checkUserPhone(phone: String): Flow<Result<Unit>>

}