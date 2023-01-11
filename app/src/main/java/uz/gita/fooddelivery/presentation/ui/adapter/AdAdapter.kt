package uz.gita.fooddelivery.presentation.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import uz.gita.fooddelivery.R
import uz.gita.fooddelivery.data.model.AdData
import uz.gita.fooddelivery.databinding.ItemAdBinding

class AdAdapter : ListAdapter<AdData, AdAdapter.Holder>(AdDiffutils) {

    inner class Holder(private val viewBinding: ItemAdBinding) :
        RecyclerView.ViewHolder(viewBinding.root) {

        fun bind(): AdData = with(viewBinding) {
            getItem(absoluteAdapterPosition).apply {
                Glide
                    .with(image)
                    .load(imageUrl)
                    .placeholder(R.drawable.ic_placeholder)
                    .error(R.drawable.ic_error_image)
                    .centerCrop()
                    .into(image)
            }
        }
    }

    private object AdDiffutils : DiffUtil.ItemCallback<AdData>() {
        override fun areItemsTheSame(oldItem: AdData, newItem: AdData): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: AdData, newItem: AdData): Boolean =
            oldItem == newItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = ItemAdBinding.bind(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_ad, parent, false
            )
        )
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind()
    }

}