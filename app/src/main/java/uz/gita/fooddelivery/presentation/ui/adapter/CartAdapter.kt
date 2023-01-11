package uz.gita.fooddelivery.presentation.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import uz.gita.fooddelivery.R
import uz.gita.fooddelivery.databinding.ItemCartBinding
import uz.gita.fooddelivery.data.model.CartData
import uz.gita.fooddelivery.utils.onClick

class CartAdapter : ListAdapter<CartData, CartAdapter.Holder>(CartDiffutils) {

    private var onItemClickUpdate: ((id: Long, pieces: Int) -> Unit)? = null
    private var onItemClickDelete: ((id: Long) -> Unit)? = null

    inner class Holder(private val viewBinding: ItemCartBinding) :
        RecyclerView.ViewHolder(viewBinding.root) {

        init {
            viewBinding.buttonMinus.onClick {
                getItem(absoluteAdapterPosition).apply {
                    if (pieces == 1) return@onClick
                    pieces -= 1
                    onItemClickUpdate?.invoke(id, pieces)
                    viewBinding.textPrice.text =
                        itemView.context.getString(R.string.text_cart_food_price, pieces * price)
                    viewBinding.textPieces.text =
                        itemView.context.getString(R.string.text_cart_food_pieces, pieces)
                }
            }

            viewBinding.buttonAdd.onClick {
                getItem(absoluteAdapterPosition).apply {
                    pieces += 1
                    onItemClickUpdate?.invoke(id, pieces)
                    viewBinding.textPrice.text =
                        itemView.context.getString(R.string.text_cart_food_price, pieces * price)
                    viewBinding.textPieces.text =
                        itemView.context.getString(R.string.text_cart_food_pieces, pieces)
                }
            }

            viewBinding.buttonDelete.onClick {
                onItemClickDelete?.invoke(getItem(absoluteAdapterPosition).id)
            }
        }

        fun bind(): CartData = with(viewBinding) {
            getItem(absoluteAdapterPosition).apply {
                Glide
                    .with(foodImage)
                    .load(image)
                    .into(foodImage)
                foodName.text = name
                textPrice.text =
                    itemView.context.getString(R.string.text_cart_food_price, price * pieces)
                textPieces.text = itemView.context.getString(R.string.text_cart_food_pieces, pieces)
            }
        }
    }

    private object CartDiffutils : DiffUtil.ItemCallback<CartData>() {
        override fun areItemsTheSame(oldItem: CartData, newItem: CartData) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: CartData, newItem: CartData) = oldItem == newItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder = Holder(
        ItemCartBinding.bind(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_cart, parent, false
            )
        )
    )

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind()
    }

    fun setOnUpdateFoodListener(block: (id: Long, pieces: Int) -> Unit) {
        onItemClickUpdate = block
    }

    fun setOnDeleteFoodListener(block: (id: Long) -> Unit) {
        onItemClickDelete = block
    }

}