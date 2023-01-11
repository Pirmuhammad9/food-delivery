package uz.gita.fooddelivery.data.repository.food

import uz.gita.fooddelivery.data.remote.response.AdDataResponse
import uz.gita.fooddelivery.data.remote.response.CartDataResponse
import uz.gita.fooddelivery.data.remote.response.FoodDataResponse

interface FoodRepository {

    // home screen
    suspend fun adsData(
        success: (List<AdDataResponse>) -> Unit,
        failure: (Throwable) -> Unit
    )

    suspend fun foodsData(
        success: (List<FoodDataResponse>) -> Unit,
        failure: (Throwable) -> Unit
    )

    /*fun selectedFoodsData(
        categoriesId: List<Int>,
        success: (List<FoodDataResponse>) -> Unit,
        failure: (Throwable) -> Unit)*/
    //----------------------------------------------------------------------------------------------

    // search screen
    suspend fun foodsDataByQuery(
        query: String,
        success: (List<FoodDataResponse>) -> Unit,
        failure: (Throwable) -> Unit
    )
    //----------------------------------------------------------------------------------------------

    // cart screen
    suspend fun cartData(
        success: (List<CartDataResponse>) -> Unit,
        failure: (Throwable) -> Unit
    ) // do it immediately

    fun addFoodToCart(
        name: String,
        price: Int,
        image: String,
        success: () -> Unit,
        failure: (Throwable) -> Unit
    )

    suspend fun deleteFoodFromCart(
        deletedItemId: Long,
        success: () -> Unit,
        failure: (Throwable) -> Unit
    )

    suspend fun updateFoodPiecesCart(
        id: Long,
        pieces: Int,
        success: () -> Unit,
        failure: (Throwable) -> Unit
    )

    suspend fun placeOrder(
        success: () -> Unit,
        failure: (Throwable) -> Unit
    )

    suspend fun historyData(
        success: (List<CartDataResponse>) -> Unit,
        failure: (Throwable) -> Unit
    )

    //----------------------------------------------------------------------------------------------

}
