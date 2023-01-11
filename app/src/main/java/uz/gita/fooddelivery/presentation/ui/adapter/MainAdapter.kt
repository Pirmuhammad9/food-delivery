package uz.gita.fooddelivery.presentation.ui.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import uz.gita.fooddelivery.presentation.ui.*

class MainAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int = 4

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> HomeScreen()
            1 -> SearchScreen()
            2 -> CartScreen()
            else -> ProfileScreen()
        }
    }
}