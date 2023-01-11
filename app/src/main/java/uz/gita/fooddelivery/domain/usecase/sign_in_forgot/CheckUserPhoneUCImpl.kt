package uz.gita.fooddelivery.domain.usecase.sign_in_forgot

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOn
import uz.gita.fooddelivery.data.repository.auth.AuthRepository
import javax.inject.Inject

class CheckUserPhoneUCImpl
@Inject constructor(
    private val repository: AuthRepository
) : CheckUserPhoneUC {

    override fun checkUserPhone(phone: String) = callbackFlow<Result<Unit>> {
        repository
            .checkUserPhone(phone,
                { trySendBlocking(Result.success(Unit)) },
                { trySendBlocking(Result.failure(Exception("User not found"))) },
                { trySendBlocking(Result.failure(it)) }
            )
        awaitClose { }
    }.flowOn(Dispatchers.IO)

}