package uz.gita.fooddelivery.presentation.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import uz.gita.fooddelivery.R
import uz.gita.fooddelivery.databinding.ScreenCheckoutBinding
import uz.gita.fooddelivery.presentation.ui.adapter.CheckoutAdapter
import uz.gita.fooddelivery.presentation.viewmodels.checkout.CheckoutVM
import uz.gita.fooddelivery.presentation.viewmodels.checkout.CheckoutVMImpl
import uz.gita.fooddelivery.utils.onClick
import uz.gita.fooddelivery.utils.snackErrorMessage

@AndroidEntryPoint
class CheckoutScreen : Fragment(R.layout.screen_checkout) {

    private val viewBinding by viewBinding(ScreenCheckoutBinding::bind)
    private val viewModel: CheckoutVM by viewModels<CheckoutVMImpl>()
    private val adapter: CheckoutAdapter by lazy { CheckoutAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) = with(viewModel) {
        super.onCreate(savedInstanceState)
        errorMessageLD.observe(this@CheckoutScreen) { snackErrorMessage(resources.getString(it)) }
        networkErrorMessageLD.observe(this@CheckoutScreen) { snackErrorMessage(it) }
        popBackStackLD.observe(this@CheckoutScreen) { findNavController().popBackStack() }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        subscribeViewBinding(viewBinding)
        subscribeViewModel(viewModel)
    }

    private fun subscribeViewBinding(viewBinding: ScreenCheckoutBinding) = with(viewBinding) {
        listCart.adapter = adapter
        listCart.layoutManager = LinearLayoutManager(requireContext())
        buttonClose.onClick { viewModel.onClickClose() }
        buttonPlaceOrder.onClick { viewModel.onClickPlaceOrder() }
    }

    private fun subscribeViewModel(viewModel: CheckoutVM) = with(viewModel) {
        checkoutFoodsList.observe(viewLifecycleOwner) { adapter.submitList(it) }
        userAddressLD.observe(viewLifecycleOwner) { viewBinding.textUserAddress.text = it }
        userFullNameLD.observe(viewLifecycleOwner) { viewBinding.textUserFullName.text = it }
        userTotalPaymentLD.observe(viewLifecycleOwner) {
            viewBinding.textTotalPrice.text =
                resources.getString(R.string.text_checkout_food_price, it)
        }
    }
}