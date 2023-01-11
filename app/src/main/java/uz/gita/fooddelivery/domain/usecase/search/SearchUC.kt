package uz.gita.fooddelivery.domain.usecase.search

import kotlinx.coroutines.flow.Flow
import uz.gita.fooddelivery.data.model.FoodData

interface SearchUC {

    fun foodsResultByQuery(query: String): Flow<Result<List<FoodData>>>

}