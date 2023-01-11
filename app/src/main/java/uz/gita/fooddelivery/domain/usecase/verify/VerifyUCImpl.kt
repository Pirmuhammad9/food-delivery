package uz.gita.fooddelivery.domain.usecase.verify

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOn
import uz.gita.fooddelivery.data.repository.auth.AuthRepository
import javax.inject.Inject

class VerifyUCImpl
@Inject constructor(
    private val repository: AuthRepository
) : VerifyUC {
    override fun registerUser(
        verificationId: String,
        smsCode: String,
        firstname: String,
        lastname: String,
        phone: String,
        password: String,
        isRemember: Boolean
    ) = callbackFlow<Result<Unit>> {
        repository.registerUser(
            verificationId,
            smsCode,
            firstname,
            lastname,
            phone,
            password,
            isRemember,
            { trySendBlocking(Result.success(Unit)) },
            { trySendBlocking(Result.failure(it)) }
        )
        awaitClose { }
    }.flowOn(Dispatchers.IO)

    override fun restoreUserPassword(
        verificationId: String,
        smsCode: String,
        phone: String,
        password: String
    ) = callbackFlow<Result<String>> {
        repository
            .restorePassword(verificationId, smsCode, phone, password,
                { trySendBlocking(Result.success("Password restored successfully")) },
                { trySendBlocking(Result.failure(it)) }
            )
        awaitClose { }
    }.flowOn(Dispatchers.IO)
}