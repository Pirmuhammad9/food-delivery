package uz.gita.fooddelivery.domain.usecase.search

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import uz.gita.fooddelivery.data.repository.food.FoodRepository
import uz.gita.fooddelivery.data.model.FoodData
import javax.inject.Inject

class SearchUCImpl
@Inject constructor(
    private val repository: FoodRepository
) : SearchUC {

    override fun foodsResultByQuery(query: String) = callbackFlow<Result<List<FoodData>>> {
        withContext(Dispatchers.Default) {
            delay(1000)
            repository.foodsDataByQuery(
                query,
                { response ->
                    val result = ArrayList<FoodData>()
                    response.map { currentData ->
                        if (currentData.name?.contains(query, true) == true)
                            result.add(currentData.toFoodData())
                    }
                    trySendBlocking(Result.success(result))
                },
                { trySendBlocking(Result.failure(it)) })
        }
        awaitClose {}
    }.flowOn(Dispatchers.IO)
}