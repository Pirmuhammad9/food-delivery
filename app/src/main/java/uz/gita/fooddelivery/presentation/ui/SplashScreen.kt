package uz.gita.fooddelivery.presentation.ui

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import uz.gita.fooddelivery.R
import uz.gita.fooddelivery.presentation.viewmodels.splash.SplashVM
import uz.gita.fooddelivery.presentation.viewmodels.splash.SplashVMImpl

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashScreen : Fragment(R.layout.screen_splash) {

    private val viewModel: SplashVM by viewModels<SplashVMImpl>()
    private val navController: NavController by lazy(LazyThreadSafetyMode.NONE) { findNavController() }

    override fun onCreate(savedInstanceState: Bundle?) = with(viewModel) {
        super.onCreate(savedInstanceState)
        mainLD.observe(this@SplashScreen) { navController.navigate(R.id.action_splashScreen_to_mainScreen) }
        authLD.observe(this@SplashScreen) { navController.navigate(R.id.action_splashScreen_to_authScreen) }
        introLD.observe(this@SplashScreen) { navController.navigate(R.id.action_splashScreen_to_introScreen) }
    }

}