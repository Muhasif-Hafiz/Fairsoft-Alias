package com.muhasib.aliascreation.adapters

import android.annotation.SuppressLint

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.muhasib.aliascreation.R
import com.muhasib.aliascreation.model.AccountItem


class AccountItemAdapter(
    private val onItemClick: (AccountItem) -> Unit
) : RecyclerView.Adapter<AccountItemAdapter.ViewHolder>(), Filterable {

    var originalList = listOf<AccountItem>()
    private var filteredList = listOf<AccountItem>()
    private var currentFilter = ""

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.nameTextView)
        val alias1TextView: TextView = itemView.findViewById(R.id.alias1TextView)
        val alias2TextView: TextView = itemView.findViewById(R.id.alias2TextView)
        val alias3TextView: TextView = itemView.findViewById(R.id.alias3TextView)
        val alias4TextView: TextView = itemView.findViewById(R.id.alias4TextView)

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onItemClick(filteredList[position])
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_account_or_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = filteredList[position]
        holder.nameTextView.text = item.name
        holder.alias1TextView.text = item.aliases[0]
        holder.alias2TextView.text = item.aliases[1]
        holder.alias3TextView.text = item.aliases[2]
        holder.alias4TextView.text = item.aliases[3]
    }

    override fun getItemCount() = filteredList.size

    fun submitList(newList: List<AccountItem>) {
        originalList = newList
        getFilter().filter(currentFilter)

    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val filterPattern = constraint?.toString()?.lowercase() ?: ""
                currentFilter = filterPattern

                val filtered = originalList.filter {
                    item ->
                    item.name.lowercase().contains(filterPattern) ||
                            item.aliases.any { it.lowercase().contains(filterPattern)
                            }
                }

                return FilterResults().apply { values = filtered }
            }

            @SuppressLint("NotifyDataSetChanged")
            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                filteredList = results?.values as? List<AccountItem> ?: emptyList()
                notifyDataSetChanged()
            }
        }
    }
    fun filterByType(type: String) {

        val chipFiltered = when (type) {
            "Accounts" -> originalList.filter { it.type == "Account" }
            "Items" -> originalList.filter { it.type == "Item" }
            else -> originalList
        }

        filteredList = chipFiltered.filter {
            it.name.contains(currentFilter, ignoreCase = true) ||
                    it.aliases.any { alias -> alias.contains(currentFilter, ignoreCase = true) }
        }
        notifyDataSetChanged()
    }

}
