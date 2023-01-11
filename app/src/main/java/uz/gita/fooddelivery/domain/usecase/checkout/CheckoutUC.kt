package uz.gita.fooddelivery.domain.usecase.checkout

import kotlinx.coroutines.flow.Flow
import uz.gita.fooddelivery.data.model.CartData

interface CheckoutUC {

    fun userData(): Flow<Result<Pair<String, String>>>

    fun cartData(): Flow<Result<Pair<List<CartData>, Int>>>

    fun placeOrder(): Flow<Result<Unit>>

}