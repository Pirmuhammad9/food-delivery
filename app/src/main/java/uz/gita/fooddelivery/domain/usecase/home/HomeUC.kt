package uz.gita.fooddelivery.domain.usecase.home

import kotlinx.coroutines.flow.Flow
import uz.gita.fooddelivery.data.model.AdData
import uz.gita.fooddelivery.data.model.CategoryData
import uz.gita.fooddelivery.data.model.FoodsGroupData

interface HomeUC {

    fun adsData(): Flow<Result<List<AdData>>>

    fun categoryListWithFoodsList(): Flow<Result<Pair<List<CategoryData>, List<FoodsGroupData>>>>

    fun selectedCategoriesListWithFoodsList(categoriesId: List<Int>): Flow<Result<List<FoodsGroupData>>>
}