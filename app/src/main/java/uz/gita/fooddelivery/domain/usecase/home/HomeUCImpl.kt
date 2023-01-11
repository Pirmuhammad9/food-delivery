package uz.gita.fooddelivery.domain.usecase.home

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import timber.log.Timber
import uz.gita.fooddelivery.data.repository.food.FoodRepository
import uz.gita.fooddelivery.data.model.AdData
import uz.gita.fooddelivery.data.model.CategoryData
import uz.gita.fooddelivery.data.model.FoodData
import uz.gita.fooddelivery.data.model.FoodsGroupData
import javax.inject.Inject

class HomeUCImpl
@Inject constructor(
    private val repository: FoodRepository
) : HomeUC {

    // when mapping use withContext(Dispatchers.Default) rather than just flowOn(Dispatchers.IO)
    override fun adsData() = callbackFlow<Result<List<AdData>>> {
        withContext(Dispatchers.Default) {
            repository
                .adsData(
                    { response ->
                        val result = ArrayList<AdData>()
                        result.add(response.last().toAdData())
                        result.addAll(response.map { it.toAdData() })
                        result.add(response.first().toAdData())
                        trySendBlocking(Result.success(result))
                    },
                    { trySendBlocking(Result.failure(it)) }
                )
        }
        awaitClose { }
    }.flowOn(Dispatchers.IO)

    override fun categoryListWithFoodsList() =
        callbackFlow<Result<Pair<List<CategoryData>, List<FoodsGroupData>>>> {
            Timber.d("categoryListWithFoodsList started: ${System.currentTimeMillis()}")
            withContext(Dispatchers.Default) {
                repository
                    .foodsData(
                        { response ->
                            val map = HashMap<CategoryData, MutableList<FoodData>>()
                            response.forEach { currentData ->
                                val categoryData = currentData.toCategoryData()
                                val foodData = currentData.toFoodData()
                                if (!map.containsKey(categoryData)) map[categoryData] =
                                    ArrayList()
                                map[categoryData]?.add(foodData)
                            }
                            val categoriesPair = ArrayList<CategoryData>()
                            val foodsPair = ArrayList<FoodsGroupData>()
                            map.forEach { currentData ->
                                currentData.key.foodsCount = currentData.value.size
                                categoriesPair.add(currentData.key)
                                foodsPair.add(FoodsGroupData(currentData.key, currentData.value))
                            }
                            val result = Pair(categoriesPair, foodsPair)
                            trySendBlocking(Result.success(result))
                            Timber.d("categoryListWithFoodsList finished: ${System.currentTimeMillis()}")
                        },
                        { trySendBlocking(Result.failure(it)) }
                    )
            }
            awaitClose { }
        }.flowOn(Dispatchers.IO)

    override fun selectedCategoriesListWithFoodsList(categoriesId: List<Int>) =
        callbackFlow<Result<List<FoodsGroupData>>> {
            Timber.d("selectedCategoriesListWithFoodsList started: ${System.currentTimeMillis()}")
            withContext(Dispatchers.Default) {
                repository
                    .foodsData(
                        { response ->
                            val map = HashMap<CategoryData, MutableList<FoodData>>()
                            response.forEach { currentData ->
                                val categoryData = currentData.toCategoryData()
                                val foodData = currentData.toFoodData()
                                if (!map.containsKey(categoryData)) map[categoryData] = ArrayList()
                                map[categoryData]?.add(foodData)
                            }
                            val result = ArrayList<FoodsGroupData>()
                            map.forEach { currentData ->
                                currentData.key.foodsCount = currentData.value.size
                                if (categoriesId.contains(currentData.key.id))
                                    result.add(FoodsGroupData(currentData.key, currentData.value))
                            }
                            trySendBlocking(Result.success(result))
                            Timber.d("selectedCategoriesListWithFoodsList finished: ${System.currentTimeMillis()}")
                        },
                        { trySendBlocking(Result.failure(it)) })
            }
            awaitClose {}
        }.flowOn(Dispatchers.IO)
}