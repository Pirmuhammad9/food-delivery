package uz.gita.fooddelivery.presentation.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import uz.gita.fooddelivery.R
import uz.gita.fooddelivery.data.model.IntroData
import uz.gita.fooddelivery.databinding.ItemIntroBinding

class IntroAdapter : ListAdapter<IntroData, IntroAdapter.Holder>(MyItemCallback) {

    private object MyItemCallback : DiffUtil.ItemCallback<IntroData>() {
        override fun areItemsTheSame(oldItem: IntroData, newItem: IntroData): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: IntroData, newItem: IntroData): Boolean =
            oldItem == newItem
    }

    inner class Holder(private val viewBinding: ItemIntroBinding) :
        RecyclerView.ViewHolder(viewBinding.root) {

        fun bind(): IntroData = with(viewBinding) {
            getItem(absoluteAdapterPosition).apply {
                textTitle.text = itemView.context.getString(title)
                textSubtitle.text = itemView.context.getString(subtitle)
                textDescription.text = itemView.context.getString(description)
                imageIntro.setImageResource(image)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_intro, parent, false)
        return Holder(ItemIntroBinding.bind(view))
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind()
    }

}
