package uz.gita.fooddelivery.presentation.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import timber.log.Timber
import uz.gita.fooddelivery.R
import uz.gita.fooddelivery.databinding.ItemFoodInnerBinding
import uz.gita.fooddelivery.data.model.FoodData
import uz.gita.fooddelivery.utils.onClick

class FoodAdapterInner : ListAdapter<FoodData, FoodAdapterInner.Holder>(FoodDataDiffutils) {

    private var onClickFoodItemListener: ((FoodData) -> Unit)? = null
    private var k = 0

    private object FoodDataDiffutils : DiffUtil.ItemCallback<FoodData>() {
        override fun areItemsTheSame(oldItem: FoodData, newItem: FoodData): Boolean =
            newItem.id == oldItem.id

        override fun areContentsTheSame(oldItem: FoodData, newItem: FoodData): Boolean =
            newItem == oldItem

    }

    inner class Holder(private val viewBinding: ItemFoodInnerBinding) :
        RecyclerView.ViewHolder(viewBinding.root) {
        init {
            Timber.d("test: ${++k}")
            itemView.onClick {
                onClickFoodItemListener?.invoke(getItem(absoluteAdapterPosition))
            }
        }

        fun bind(): FoodData = with(viewBinding) {
            getItem(absoluteAdapterPosition).apply {
                foodName.text = name
                Glide
                    .with(foodImage)
                    .load(image)
                    .into(foodImage)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder = Holder(
        ItemFoodInnerBinding.bind(
            LayoutInflater.from(parent.context).inflate(R.layout.item_food_inner, parent, false)
        )
    )

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind()
    }

    fun setOnClickFoodItemListener(block: (FoodData) -> Unit) {
        onClickFoodItemListener = block
    }
}