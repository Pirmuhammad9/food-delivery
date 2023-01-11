package uz.gita.fooddelivery.presentation.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import uz.gita.fooddelivery.R
import uz.gita.fooddelivery.data.model.FoodData
import uz.gita.fooddelivery.databinding.ItemSearchBinding
import uz.gita.fooddelivery.utils.onClick

class SearchAdapter : ListAdapter<FoodData, SearchAdapter.Holder>(FoodDataDiffutils) {

    private var onClickFoodItemListener: ((FoodData) -> Unit)? = null

    private object FoodDataDiffutils : DiffUtil.ItemCallback<FoodData>() {
        override fun areItemsTheSame(oldItem: FoodData, newItem: FoodData): Boolean =
            newItem.id == oldItem.id

        override fun areContentsTheSame(oldItem: FoodData, newItem: FoodData): Boolean =
            newItem == oldItem

    }

    inner class Holder(private val viewBinding: ItemSearchBinding) :
        RecyclerView.ViewHolder(viewBinding.root) {

        init {
            itemView.onClick {
                onClickFoodItemListener?.invoke(getItem(absoluteAdapterPosition))
            }
        }

        fun bind(): FoodData = with(viewBinding) {
            getItem(absoluteAdapterPosition).apply {
                Glide
                    .with(searchFoodImage)
                    .load(image)
                    .into(searchFoodImage)
                searchFoodName.text = name
                searchFoodPrice.text =
                    itemView.context.getString(R.string.text_search_food_price, price)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchAdapter.Holder =
        Holder(
            ItemSearchBinding.bind(
                LayoutInflater.from(parent.context).inflate(R.layout.item_search, parent, false)
            )
        )

    override fun onBindViewHolder(holder: SearchAdapter.Holder, position: Int) {
        holder.bind()
    }

    fun setOnClickFoodItemListener(block: (FoodData) -> Unit) {
        onClickFoodItemListener = block
    }

}