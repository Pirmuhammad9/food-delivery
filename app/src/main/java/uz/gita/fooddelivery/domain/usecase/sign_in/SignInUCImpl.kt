package uz.gita.fooddelivery.domain.usecase.sign_in

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOn
import timber.log.Timber
import uz.gita.fooddelivery.data.repository.auth.AuthRepository
import javax.inject.Inject

class SignInUCImpl
@Inject constructor(
    private val repository: AuthRepository
) : SignInUC {

    override fun signInUser(
        phone: String,
        password: String,
        rememberStatus: Boolean
    ) = callbackFlow<Result<Unit>> {
        Timber.tag("isRemember").d("isRemember: $rememberStatus")
        repository
            .signInUser(phone, password, rememberStatus,
                { trySendBlocking(Result.success(Unit)) },
                { trySendBlocking(Result.failure(it)) },
                { trySendBlocking(Result.failure(Exception("Phone or Password is wrong"))) },
                { trySendBlocking(Result.failure(Exception("User not found"))) })
        awaitClose { }
    }.flowOn(Dispatchers.IO)

    override fun forgotPassword() {

    }
}