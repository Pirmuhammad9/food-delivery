package uz.gita.fooddelivery.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import uz.gita.fooddelivery.data.repository.app.AppRepository
import uz.gita.fooddelivery.data.repository.app.AppRepositoryImpl
import uz.gita.fooddelivery.data.repository.auth.AuthRepository
import uz.gita.fooddelivery.data.repository.auth.AuthRepositoryImpl
import uz.gita.fooddelivery.data.repository.food.FoodRepository
import uz.gita.fooddelivery.data.repository.food.FoodRepositoryImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {

    @[Binds Singleton]
    fun bindAppRepository(impl: AppRepositoryImpl): AppRepository

    @[Binds Singleton]
    fun bindFoodRepository(impl: FoodRepositoryImpl): FoodRepository

    @[Binds Singleton]
    fun bindAuthRepository(impl: AuthRepositoryImpl): AuthRepository

}