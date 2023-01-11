package uz.gita.fooddelivery.presentation.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import uz.gita.fooddelivery.R
import uz.gita.fooddelivery.databinding.ScreenHistoryBinding
import uz.gita.fooddelivery.presentation.ui.adapter.CheckoutAdapter
import uz.gita.fooddelivery.presentation.viewmodels.history.HistoryVM
import uz.gita.fooddelivery.presentation.viewmodels.history.HistoryVMImpl
import uz.gita.fooddelivery.utils.snackErrorMessage

@AndroidEntryPoint
class HistoryScreen : Fragment(R.layout.screen_history) {

    private val viewBinding by viewBinding(ScreenHistoryBinding::bind)
    private val viewModel: HistoryVM by viewModels<HistoryVMImpl>()
    private val adapter: CheckoutAdapter by lazy { CheckoutAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) = with(viewModel) {
        super.onCreate(savedInstanceState)
        networkErrorMessageLD.observe(this@HistoryScreen) { snackErrorMessage(it) }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = with(viewBinding) {
        listHistory.adapter = adapter
        listHistory.layoutManager = LinearLayoutManager(requireContext())
        viewModel.historyListLD.observe(viewLifecycleOwner) { adapter.submitList(it) }
    }

}