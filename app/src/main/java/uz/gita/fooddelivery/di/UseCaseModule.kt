package uz.gita.fooddelivery.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import uz.gita.fooddelivery.domain.usecase.cart.CartUC
import uz.gita.fooddelivery.domain.usecase.cart.CartUCImpl
import uz.gita.fooddelivery.domain.usecase.checkout.CheckoutUC
import uz.gita.fooddelivery.domain.usecase.checkout.CheckoutUCImpl
import uz.gita.fooddelivery.domain.usecase.food_detail.FoodDetailUC
import uz.gita.fooddelivery.domain.usecase.food_detail.FoodDetailUCImpl
import uz.gita.fooddelivery.domain.usecase.home.HomeUC
import uz.gita.fooddelivery.domain.usecase.home.HomeUCImpl
import uz.gita.fooddelivery.domain.usecase.intro.IntroUC
import uz.gita.fooddelivery.domain.usecase.intro.IntroUCImpl
import uz.gita.fooddelivery.domain.usecase.main.MainUC
import uz.gita.fooddelivery.domain.usecase.main.MainUCImpl
import uz.gita.fooddelivery.domain.usecase.profile.ProfileEditUC
import uz.gita.fooddelivery.domain.usecase.profile.ProfileEditUCImpl
import uz.gita.fooddelivery.domain.usecase.profile.ProfileUC
import uz.gita.fooddelivery.domain.usecase.profile.ProfileUCImpl
import uz.gita.fooddelivery.domain.usecase.search.SearchUC
import uz.gita.fooddelivery.domain.usecase.search.SearchUCImpl
import uz.gita.fooddelivery.domain.usecase.sign_in.SignInUC
import uz.gita.fooddelivery.domain.usecase.sign_in.SignInUCImpl
import uz.gita.fooddelivery.domain.usecase.sign_in_forgot.CheckUserPhoneUC
import uz.gita.fooddelivery.domain.usecase.sign_in_forgot.CheckUserPhoneUCImpl
import uz.gita.fooddelivery.domain.usecase.splash.SplashUC
import uz.gita.fooddelivery.domain.usecase.splash.SplashUCImpl
import uz.gita.fooddelivery.domain.usecase.verify.VerifyUC
import uz.gita.fooddelivery.domain.usecase.verify.VerifyUCImpl

@Module
@InstallIn(ViewModelComponent::class)
interface UseCaseModule {

    @Binds
    fun bindIntroUseCase(impl: IntroUCImpl): IntroUC

    @Binds
    fun bindSplashUseCase(impl: SplashUCImpl): SplashUC

    @Binds
    fun bindSignInUseCase(impl: SignInUCImpl): SignInUC

    @Binds
    fun bindCheckUserPhoneUseCase(impl: CheckUserPhoneUCImpl): CheckUserPhoneUC

    @Binds
    fun bindVerifyUseCase(impl: VerifyUCImpl): VerifyUC

    @Binds
    fun bindMainUseCase(impl: MainUCImpl): MainUC

    @Binds
    fun bindHomeUseCase(impl: HomeUCImpl): HomeUC

    @Binds
    fun bindSearchUseCase(impl: SearchUCImpl): SearchUC

    @Binds
    fun bindCartUseCase(impl: CartUCImpl): CartUC

    @Binds
    fun bindCheckoutUseCase(impl:CheckoutUCImpl): CheckoutUC

    @Binds
    fun bindFoodDetailUseCase(impl: FoodDetailUCImpl): FoodDetailUC

    @Binds
    fun bindProfileUseCase(impl: ProfileUCImpl): ProfileUC

    @Binds
    fun bindProfileEditUseCase(impl: ProfileEditUCImpl): ProfileEditUC

}