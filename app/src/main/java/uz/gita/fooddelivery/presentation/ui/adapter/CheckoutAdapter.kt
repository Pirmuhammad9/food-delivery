package uz.gita.fooddelivery.presentation.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import uz.gita.fooddelivery.R
import uz.gita.fooddelivery.databinding.ItemCartCheckoutBinding
import uz.gita.fooddelivery.data.model.CartData

class CheckoutAdapter : ListAdapter<CartData, CheckoutAdapter.Holder>(CartDiffutils) {

    inner class Holder(private val viewBinding: ItemCartCheckoutBinding) :
        RecyclerView.ViewHolder(viewBinding.root) {

        fun bind(): CartData = with(viewBinding) {
            getItem(absoluteAdapterPosition).apply {
                Glide
                    .with(foodImage)
                    .load(image)
                    .into(foodImage)
                foodName.text = name
                textPrice.text =
                    itemView.context.getString(R.string.text_checkout_food_price, price * pieces)
                textPieces.text = itemView.context.getString(R.string.text_checkout_food_pieces, pieces)
            }
        }
    }

    private object CartDiffutils : DiffUtil.ItemCallback<CartData>() {
        override fun areItemsTheSame(oldItem: CartData, newItem: CartData) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: CartData, newItem: CartData) = oldItem == newItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder = Holder(
        ItemCartCheckoutBinding.bind(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_cart_checkout, parent, false
            )
        )
    )

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind()
    }

}