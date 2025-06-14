package com.muhasib.aliascreation.adapters



import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.muhasib.aliascreation.databinding.ItemAlternateNameBinding
import com.muhasib.aliascreation.model.AlternateName

class AlternateNameAdapter(
    private val onClickListener: (AlternateName) -> Unit
)
    : ListAdapter<AlternateName, AlternateNameAdapter.ViewHolder>(DiffCallback()) {

    inner class ViewHolder(private val binding: ItemAlternateNameBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: AlternateName) {
            binding.tvAccountName
                .text = item.name
            binding.ivDeleteIcon.setOnClickListener {
                onClickListener(item)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemAlternateNameBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class DiffCallback : DiffUtil.ItemCallback<AlternateName>() {
        override fun areItemsTheSame(oldItem: AlternateName, newItem: AlternateName): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: AlternateName, newItem: AlternateName): Boolean {
            return oldItem == newItem
        }
    }
}
