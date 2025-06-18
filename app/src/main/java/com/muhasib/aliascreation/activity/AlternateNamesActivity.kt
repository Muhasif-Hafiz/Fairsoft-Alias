package com.muhasib.aliascreation.activity

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.muhasib.aliascreation.R
import com.muhasib.aliascreation.adapters.AlternateNameAdapter
import com.muhasib.aliascreation.databinding.ActivityAlternateNamesBinding
import com.muhasib.aliascreation.model.AlternateNameData
import com.muhasib.aliascreation.model.Item
import com.muhasib.aliascreation.mvvm.AccountRepository
import com.muhasib.aliascreation.mvvm.AccountViewModel
import com.muhasib.aliascreation.mvvm.AccountViewModelFactory
import com.muhasib.aliascreation.mvvm.RetrofitInstance
import kotlinx.coroutines.launch

class AlternateNamesActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAlternateNamesBinding
    private lateinit var adapter: AlternateNameAdapter
    private lateinit var viewModel: AccountViewModel

    private val categories = listOf("Accounts", "Charges", "Items")
    private val subcategoriesMap = mutableMapOf(
        "Accounts" to emptyList<Item>()
    )

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAlternateNamesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val api = RetrofitInstance.api
        val repository = AccountRepository(api)
        viewModel = ViewModelProvider(
            this,
            AccountViewModelFactory(repository)
        )[AccountViewModel::class.java]

        setupWindowInsets()
        setupRecyclerView()
        setupObservers()
        setupCategoryDropdown()

      //  binding.textInputSublayout.visibility = View.GONE
    }

    private fun setupWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }    private fun setupCategoryDropdown() {
        val dropdownAdapter = ArrayAdapter(
            this,
            R.layout.simple_spinner_dropdown_item,
            categories
        ).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }

        binding.spinnerItems.apply {
            setAdapter(dropdownAdapter)
            setOnItemClickListener { _, _, position, _ ->
                val selectedCategory = categories[position]
                handleCategorySelection(selectedCategory)
                handleSubcategoryVisibility(selectedCategory)
            }
//            if (categories.isNotEmpty()) setText(categories[0], false)
        }
    }

    private fun handleCategorySelection(category: String) {
        when (category) {
            "Accounts" -> {
                viewModel.fetchAccountIds()
            }
            "Charges", "Items" -> {

                Toast.makeText(this@AlternateNamesActivity, "Coming soon...", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun handleSubcategoryVisibility(category: String) {
        binding.textInputSublayout.visibility = if (subcategoriesMap.containsKey(category)) {
            setupSubcategoryDropdown(category)
            View.VISIBLE
        } else {
            View.GONE
        }
    }

    private fun setupSubcategoryDropdown(category: String) {
        val subcategories = (subcategoriesMap[category] ?: emptyList())

        val subAdapter = ArrayAdapter(
            this,
            R.layout.dropdown_item,
            subcategories
        )

        binding.spinnerItemsSubCategory.apply {
            setAdapter(subAdapter)
            setOnItemClickListener { _, _, position, _ ->
                val selected = subcategories[position]
                onSubcategorySelected(category, selected.name, selected.id)
            }
            setText("", false)
        }
    }

    private fun onSubcategorySelected(category: String, subcategory: String, actId: Int) {
        if (category == "Accounts") {
            viewModel.fetchAlternateNameById("accounts", actId)
        } else {
         //   filterBySubcategory(category, subcategory)
            filterBySubcategory()
        }
    }
    private fun setupRecyclerView() {
        adapter = AlternateNameAdapter { item -> openDeleteDialog(item) }
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@AlternateNamesActivity)
            adapter = this@AlternateNamesActivity.adapter
        }
        adapter.submitList(emptyList())
    }

    @SuppressLint("SetTextI18n")
    private fun setupObservers() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.accountNames.collect { accountNames ->
                        val list = accountNames.map { Item(it.actName, it.actId) }
                        subcategoriesMap["Accounts"] = list
                        if (binding.spinnerItems.text.toString() == "Accounts") {
                            setupSubcategoryDropdown("Accounts")
                        }
                    }
                }

                launch {
                    viewModel.alternateNameByIds.observe(this@AlternateNamesActivity) { alternateNamesData ->
                        adapter.submitList(alternateNamesData)
                        binding.tvItemCount.text = alternateNamesData.size.toString()
                    }
                }

                launch {
                    viewModel.deleteStatus.observe(this@AlternateNamesActivity){
                        Snackbar.make(binding.root, it, Snackbar.LENGTH_SHORT).show()
                    }
                }
                launch {
                    viewModel.errorState.observe(this@AlternateNamesActivity){
                        Toast.makeText(this@AlternateNamesActivity, it, Toast.LENGTH_SHORT).show()
                    }
                }

                launch {
                    viewModel.loading.collect { isLoading ->
                        binding.progressBar.isVisible = isLoading
                    }
                }

                launch {
                    viewModel.error.collect { error ->
                        error?.let {
                            Toast.makeText(
                                this@AlternateNamesActivity,
                                it,
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                }
            }
        }
    }

    private fun filterBySubcategory() {
        val filteredList = allItems
        adapter.submitList(if (filteredList.isEmpty()) allItems else filteredList)
    }

    private fun openDeleteDialog(item: AlternateNameData) {
        Dialog(this).apply {
            setContentView(R.layout.dialog_delete_account)
            window?.apply {
                setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                setBackgroundDrawableResource(android.R.color.transparent)
            }

            findViewById<TextView>(R.id.AlternateAccountName).text = item.actName
            findViewById<Button>(R.id.deleteButton).setOnClickListener {
                deleteItem(item)
                dismiss()

            }

            findViewById<Button>(R.id.cancelButton).setOnClickListener { dismiss() }
            show()
        }
    }

    private fun deleteItem(item : AlternateNameData){
        if(binding.spinnerItems.text.toString() == "Accounts"){
            viewModel.deleteAlternateName("accounts", item.altId)
        }
    }

    companion object {
        private val allItems = listOf(
            AlternateNameData(altId = 13, actName = "Charges", actId = 1, gstNumber = "123", createdDate = "2023"))
    }
}