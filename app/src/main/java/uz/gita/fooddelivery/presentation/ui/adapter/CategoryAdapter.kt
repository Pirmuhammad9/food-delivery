package uz.gita.fooddelivery.presentation.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import timber.log.Timber
import uz.gita.fooddelivery.R
import uz.gita.fooddelivery.databinding.ItemCategoryBinding
import uz.gita.fooddelivery.data.model.CategoryData
import uz.gita.fooddelivery.utils.onClick

class CategoryAdapter : ListAdapter<CategoryData, CategoryAdapter.Holder>(CategoryDiffutils) {

    private var onItemClickListener: ((Int, Boolean) -> Unit)? = null
    private val clickedCategories: MutableList<Int> by lazy { ArrayList() }

    inner class Holder(private val viewBinding: ItemCategoryBinding) :
        RecyclerView.ViewHolder(viewBinding.root) {

        init {
            itemView.onClick {
                getItem(absoluteAdapterPosition).apply {
                    isSelected = !isSelected
                    Timber.tag("KKK").d("categoryID: $id, isSelected: $isSelected")
                    if (isSelected) clickedCategories.add(id)
                    else clickedCategories.remove(id)
                    onItemClickListener?.invoke(id, isSelected)
                    when {
                        isSelected -> viewBinding.itemCategory.setBackgroundResource(R.drawable.shape_item_category_checked)
                        else -> viewBinding.itemCategory.setBackgroundResource(R.drawable.shape_item_category_default)
                    }
                }
            }
        }

        fun bind(): CategoryData = with(viewBinding) {
            getItem(absoluteAdapterPosition).apply {
                when {
                    isSelected -> viewBinding.itemCategory.setBackgroundResource(R.drawable.shape_item_category_checked)
                    else -> viewBinding.itemCategory.setBackgroundResource(R.drawable.shape_item_category_default)
                }
                itemName.text = name
                Glide
                    .with(itemIcon)
                    .load(icon)
                    .into(itemIcon)
                itemCount.text = itemView.context.getString(
                    R.string.text_home_category_items_count,
                    foodsCount
                )
            }
        }
    }

    private object CategoryDiffutils : DiffUtil.ItemCallback<CategoryData>() {
        override fun areItemsTheSame(oldItem: CategoryData, newItem: CategoryData): Boolean =
            newItem.id == oldItem.id

        override fun areContentsTheSame(oldItem: CategoryData, newItem: CategoryData): Boolean =
            newItem == oldItem

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder = Holder(
        ItemCategoryBinding.bind(
            LayoutInflater.from(parent.context).inflate(R.layout.item_category, parent, false)
        )
    )

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind()
    }

    fun setOnItemClickListener(block: (Int, Boolean) -> Unit) {
        onItemClickListener = block
    }
}