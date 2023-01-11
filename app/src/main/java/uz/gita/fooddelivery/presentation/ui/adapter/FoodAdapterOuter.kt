package uz.gita.fooddelivery.presentation.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import timber.log.Timber
import uz.gita.fooddelivery.R
import uz.gita.fooddelivery.databinding.ItemFoodOuterBinding
import uz.gita.fooddelivery.data.model.FoodData
import uz.gita.fooddelivery.data.model.FoodsGroupData

class FoodAdapterOuter : ListAdapter<FoodsGroupData, FoodAdapterOuter.Holder>(FoodsGroupDiffutils) {

    private var onClickFoodItemListener: ((FoodData) -> Unit)? = null
    private var onClickViewAllListener: ((FoodsGroupData) -> Unit)? = null
    /*private val selectedCategoriesId: MutableList<Int> = ArrayList()*/

    inner class Holder(private val viewBinding: ItemFoodOuterBinding) :
        RecyclerView.ViewHolder(viewBinding.root) {

        private val innerAdapter = FoodAdapterInner()
//        private val snapHelper: SnapHelper = LinearSnapHelper()

        init {
            innerAdapter.setOnClickFoodItemListener { data ->
                Timber.tag("AAA").d("data: $data")
                onClickFoodItemListener?.invoke(data)
            }
            /*viewBinding.foodViewAll.onClick {
                onClickViewAllListener?.invoke(getItem(absoluteAdapterPosition))
            }*/
        }

        fun bind(): FoodsGroupData = with(viewBinding) {
            /*Timber.tag("KKK").d("selectedCategories: $selectedCategoriesId")*/
            getItem(absoluteAdapterPosition).apply {
                innerAdapter.submitList(foodList)
                innerList.adapter = innerAdapter
//                snapHelper.attachToRecyclerView(innerList)
                innerList.layoutManager =
                    GridLayoutManager(
                        itemView.context,
                        2,
                        GridLayoutManager.VERTICAL,
                        false
                    )
                foodCategory.text = getItem(absoluteAdapterPosition).category.name
            }
        }
    }

    private object FoodsGroupDiffutils : DiffUtil.ItemCallback<FoodsGroupData>() {
        override fun areItemsTheSame(oldItem: FoodsGroupData, newItem: FoodsGroupData): Boolean =
            newItem.category.id == oldItem.category.id

        override fun areContentsTheSame(oldItem: FoodsGroupData, newItem: FoodsGroupData): Boolean =
            newItem == oldItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder = Holder(
        ItemFoodOuterBinding.bind(
            LayoutInflater.from(parent.context).inflate(R.layout.item_food_outer, parent, false)
        )
    )

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind()
    }

    fun setOnClickFoodItemListener(block: (FoodData) -> Unit) {
        onClickFoodItemListener = block
    }

    fun setOnClickViewAllListener(block: (FoodsGroupData) -> Unit) {
        onClickViewAllListener = block
    }

    /*fun setOnCategoriesIdSelectedListener(categoryId: Int, isSelected: Boolean) {
        when {
            isSelected -> {
                selectedCategoriesId.add(categoryId)
                notifyItemRemoved(categoryId)
            }
            else -> {
                selectedCategoriesId.remove(categoryId)
                notifyItemInserted(categoryId)
            }
        }
    }*/
}


