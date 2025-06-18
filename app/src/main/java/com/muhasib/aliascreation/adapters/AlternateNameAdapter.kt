package com.muhasib.aliascreation.adapters



import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.muhasib.aliascreation.databinding.ItemAlternateNameBinding
import com.muhasib.aliascreation.model.AlternateName
import com.muhasib.aliascreation.model.AlternateNameData

class AlternateNameAdapter(
    private val onClickListener: (AlternateNameData) -> Unit
)
    : ListAdapter<AlternateNameData, AlternateNameAdapter.ViewHolder>(DiffCallback()) {

    inner class ViewHolder(private val binding: ItemAlternateNameBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: AlternateNameData) {
            binding.tvAccountName
                .text = item.actName
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

    class DiffCallback : DiffUtil.ItemCallback<AlternateNameData>() {
        override fun areItemsTheSame(oldItem: AlternateNameData, newItem: AlternateNameData): Boolean {
            return oldItem.actName == newItem.actName
        }

        override fun areContentsTheSame(oldItem: AlternateNameData, newItem: AlternateNameData): Boolean {
            return oldItem == newItem
        }
    }
}
