package uz.gita.fooddelivery.presentation.ui

import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import uz.gita.fooddelivery.R
import uz.gita.fooddelivery.databinding.ScreenCartBinding
import uz.gita.fooddelivery.data.model.CartData
import uz.gita.fooddelivery.presentation.ui.adapter.CartAdapter
import uz.gita.fooddelivery.presentation.viewmodels.cart.CartVM
import uz.gita.fooddelivery.presentation.viewmodels.cart.CartVMImpl
import uz.gita.fooddelivery.utils.onClick
import uz.gita.fooddelivery.utils.snackErrorMessage

@AndroidEntryPoint
class CartScreen : Fragment(R.layout.screen_cart) {

    private val viewBinding by viewBinding(ScreenCartBinding::bind)
    private val viewModel: CartVM by viewModels<CartVMImpl>()
    private val adapter: CartAdapter by lazy { CartAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) = with(viewModel) {
        super.onCreate(savedInstanceState)
        navigateCheckoutScreenLD.observe(this@CartScreen) { findNavController().navigate(R.id.action_mainScreen_to_checkoutScreen) }
        networkErrorMessage.observe(this@CartScreen) { snackErrorMessage(it) }
        errorMessageLD.observe(this@CartScreen) { snackErrorMessage(resources.getString(it)) }
        deleteFoodMessage.observe(this@CartScreen) {
            snackAction(resources.getString(it.first), resources.getString(it.second))
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        subscribeViewBinding(viewBinding)
        subscribeViewModel(viewModel)
    }

    private fun subscribeViewBinding(viewBinding: ScreenCartBinding) = with(viewBinding) {
        listCart.adapter = adapter
        listCart.layoutManager = LinearLayoutManager(requireContext())
        adapter.setOnUpdateFoodListener { id, pieces -> viewModel.onClickUpdateFood(id, pieces) }
        adapter.setOnDeleteFoodListener { viewModel.onClickDeleteFood(it) }
        buttonCheckout.onClick { viewModel.onClickCheckout() }
    }

    private fun subscribeViewModel(viewModel: CartVM) = with(viewModel) {
        cartDataLD.observe(viewLifecycleOwner, cartDataLDObserver)
        placeholderStatusLD.observe(viewLifecycleOwner, placeholderStatusLDObserver)
        cartListStatusLD.observe(viewLifecycleOwner, cartListStatusLDObserver)
        checkOutButtonStatusLD.observe(viewLifecycleOwner, checkOutButtonStatusLDObserver)
    }

    private val cartDataLDObserver = Observer<List<CartData>> {
        Timber.d("cartDataLDObserver: ${it.size}")
        adapter.submitList(it)
    }

    private val placeholderStatusLDObserver = Observer<Boolean> {
        viewBinding.cartPlaceholder.apply {
            visibility = if (it) VISIBLE
            else GONE
        }
    }

    private val cartListStatusLDObserver = Observer<Boolean> {
        viewBinding.listCart.apply {
            visibility = if (it) VISIBLE
            else GONE
        }
    }

    private val checkOutButtonStatusLDObserver = Observer<Boolean> {
        viewBinding.buttonCheckout.apply {
            visibility = when (it) {
                true -> VISIBLE
                else -> GONE
            }
        }
    }

    private fun snackAction(message: String, actionMessage: String) {
        val snack = Snackbar.make(requireView(), message, Snackbar.LENGTH_LONG).apply {
            setBackgroundTint(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.color_snack_error
                )
            )
                .setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
            setAction(actionMessage) {
                viewModel.deleteFoodStatus(true)
            }
                .setActionTextColor(
                    AppCompatResources.getColorStateList(
                        requireContext(),
                        R.color.color_snack_action
                    )
                )
        }
        snack.show()
        val snackCallback = object : Snackbar.Callback() {
            override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                if (event == DISMISS_EVENT_TIMEOUT) viewModel.deleteFoodStatus(false)
            }
        }
        snack.addCallback(snackCallback)
    }

    override fun onResume() {
        super.onResume()
        Timber.d("onResume")
        viewModel.loadData()
    }

}