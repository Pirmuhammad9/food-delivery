package uz.gita.fooddelivery.presentation.ui

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import uz.gita.fooddelivery.R
import uz.gita.fooddelivery.databinding.ScreenFoodDetailBinding
import uz.gita.fooddelivery.presentation.viewmodels.food_detail.FoodDetailVM
import uz.gita.fooddelivery.presentation.viewmodels.food_detail.FoodDetailVMImpl
import uz.gita.fooddelivery.utils.onClick

@AndroidEntryPoint
class FoodDetailScreen : Fragment(R.layout.screen_food_detail) {

    private val viewBinding by viewBinding(ScreenFoodDetailBinding::bind)
    private val viewModel: FoodDetailVM by viewModels<FoodDetailVMImpl>()
    private val args: FoodDetailScreenArgs by navArgs()
    private val navController: NavController by lazy(LazyThreadSafetyMode.NONE) { findNavController() }

    override fun onCreate(savedInstanceState: Bundle?) = with(viewModel) {
        super.onCreate(savedInstanceState)
        popBackStack.observe(this@FoodDetailScreen) {
            navController.popBackStack()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = with(viewBinding) {
        onBackPressed()
        buttonBack.onClick {
            viewModel.onClickBack()
        }
        buttonAddCart.onClick {
            viewModel.onClickAddCart(
                name = args.foodName, price = args.foodPrice, image = args.foodImage!!,
            )
        }
        Glide.with(foodImage).load(args.foodImage).into(foodImage)
        foodName.text = args.foodName
        foodPrice.text = resources.getString(R.string.text_food_detail_price, args.foodPrice)
        foodDescription.text = args.foodDescription
    }

    private fun onBackPressed() {
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    navController.popBackStack()
                }
            })
    }
}