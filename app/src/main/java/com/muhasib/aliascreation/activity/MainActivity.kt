package com.muhasib.aliascreation.activity

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.widget.SearchView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.muhasib.aliascreation.R
import com.muhasib.aliascreation.adapters.AccountItemAdapter
import com.muhasib.aliascreation.databinding.ActivityMainBinding
import com.muhasib.aliascreation.demo.DummyData
import com.muhasib.aliascreation.model.AccountItem

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val adapter by lazy { AccountItemAdapter { item -> showEditDialog(item) } }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        setupChips()
        setupSearchView()
        loadData()
        setUpFab()
    }
    private fun setUpFab(){
        binding.userDetailsFab.setOnClickListener{
            startActivity(Intent(this, UserDetailsActivity::class.java ))
        }
    }

    private fun setupRecyclerView() {
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter
        binding.recyclerView.setHasFixedSize(true)
    }

    private fun setupChips() {

        binding.allChip.isChecked = true
        adapter.filterByType("All")
        binding.chipGroup.isSingleSelection = true

        binding.chipGroup.setOnCheckedStateChangeListener { _, checkedIds ->
            when (checkedIds.firstOrNull()) {
                R.id.allChip -> {
                    adapter.filterByType("All")
                    // Ensure other chips are properly unchecked
                    binding.accountsChip.isChecked = false
                    binding.itemsChip.isChecked = false
                }

                R.id.accountsChip -> {
                    adapter.filterByType("Accounts")
                    binding.allChip.isChecked = false
                }

                R.id.itemsChip -> {
                    adapter.filterByType("Items")
                    binding.allChip.isChecked = false
                }

                null -> {
                    binding.allChip.isChecked = true
                    adapter.filterByType("All")
                }
            }
        }

        binding.allChip.setOnClickListener {
            if (!binding.allChip.isChecked) {
                binding.chipGroup.check(R.id.allChip)
            }
        }
        binding.accountsChip.setOnClickListener {
            if (!binding.accountsChip.isChecked) {
                binding.chipGroup.check(R.id.accountsChip)
            }
        }
        binding.itemsChip.setOnClickListener {
            if (!binding.itemsChip.isChecked) {
                binding.chipGroup.check(R.id.itemsChip)
            }
        }
    }

    private fun setupSearchView() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean = false
            override fun onQueryTextChange(newText: String?): Boolean {
                adapter.filter.filter(newText)
                return true
            }
        })
    }

    private fun loadData() {

        val dummyDataList = DummyData.dummyList
        adapter.submitList(dummyDataList)
    }

    @SuppressLint("MissingInflatedId", "SetTextI18n")
    private fun showEditDialog(item: AccountItem) {
        val dialog = Dialog(this).apply {
            setContentView(R.layout.dialog_edit_aliases)
            window?.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            window?.setBackgroundDrawableResource(android.R.color.transparent)


            if (item.type == "Account") findViewById<TextView>(R.id.tv_item_type).text =
                "Account Name : "
            else findViewById<TextView>(R.id.tv_item_type).text = "Item Name : "

            findViewById<TextView>(R.id.itemNameTextView).text = item.name
            findViewById<EditText>(R.id.alias1EditText).setText(item.aliases[0])
            findViewById<EditText>(R.id.alias2EditText).setText(item.aliases[1])
            findViewById<EditText>(R.id.alias3EditText).setText(item.aliases[2])
            findViewById<EditText>(R.id.alias4EditText).setText(item.aliases[3])

            findViewById<Button>(R.id.saveButton).setOnClickListener {
                val newAliases = listOf(
                    findViewById<EditText>(R.id.alias1EditText).text.toString(),
                    findViewById<EditText>(R.id.alias2EditText).text.toString(),
                    findViewById<EditText>(R.id.alias3EditText).text.toString(),
                    findViewById<EditText>(R.id.alias4EditText).text.toString()
                )

                val index = adapter.originalList.indexOfFirst { it.id == item.id }
                if (index != -1) {
                    val updatedItem = item.copy(aliases = newAliases)
                    adapter.originalList = adapter.originalList.toMutableList().apply {
                        set(index, updatedItem)
                    }
                    adapter.submitList(adapter.originalList)
                }
                dismiss()
            }
            findViewById<Button>(R.id.cancelButton).setOnClickListener{dismiss()}
        }

        val close = dialog.findViewById<ImageView>(R.id.closeButton)
        close.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }

}