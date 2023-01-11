package uz.gita.fooddelivery.presentation.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import uz.gita.fooddelivery.R
import uz.gita.fooddelivery.databinding.ScreenFoodCategoryBinding

@AndroidEntryPoint
class FoodCategoryScreen : Fragment(R.layout.screen_food_category) {

    private val viewBinding by viewBinding(ScreenFoodCategoryBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    }
}