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
import androidx.activity.viewModels
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
import com.muhasib.aliascreation.model.AlternateName
import com.muhasib.aliascreation.mvvm.AccountRepository
import com.muhasib.aliascreation.mvvm.AccountViewModel
import com.muhasib.aliascreation.mvvm.AccountViewModelFactory
import com.muhasib.aliascreation.mvvm.RetrofitInstance
import kotlinx.coroutines.launch

class AlternateNamesActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAlternateNamesBinding
    private lateinit var adapter: AlternateNameAdapter
    private lateinit var viewModel: AccountViewModel

    private val categories = listOf("All", "Charges", "Items", "Accounts")
    private val subcategoriesMap = mutableMapOf(
        "Charges" to listOf("Tax", "Service Fee", "Discount"),
        "Items" to listOf("Stationery", "Groceries", "Utilities"),
        "Accounts" to emptyList() // Will be populated from API
    )

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAlternateNamesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Manual dependency injection
        val api = RetrofitInstance.api
        val repository = AccountRepository(api)
        viewModel = ViewModelProvider(this, AccountViewModelFactory(repository))
            .get(AccountViewModel::class.java)

        setupWindowInsets()
        setupRecyclerView()
        setupObservers()
        setupCategoryDropdown()

        viewModel.fetchAccountIds()
        binding.textInputSublayout.visibility = View.GONE
    }

    private fun setupWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun setupCategoryDropdown() {
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
                filterItems(selectedCategory)
                handleSubcategoryVisibility(selectedCategory)
            }
            if (categories.isNotEmpty()) setText(categories[0], false)
        }
    }

    private fun handleSubcategoryVisibility(category: String) {
        binding.textInputSublayout.visibility = if (category != "All" &&
            subcategoriesMap.containsKey(category) &&
            subcategoriesMap[category]?.isNotEmpty() == true) {
            setupSubcategoryDropdown(category)
            View.VISIBLE
        } else {
            View.GONE
        }
    }

    private fun setupSubcategoryDropdown(category: String) {
        val subcategories = subcategoriesMap[category] ?: emptyList()
        val subAdapter = ArrayAdapter(
            this,
            R.layout.simple_spinner_dropdown_item,
            subcategories
        ).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }

        binding.spinnerItemsSubCategory.apply {
            setAdapter(subAdapter)
            setOnItemClickListener { _, _, position, _ ->
                val selectedSubcategory = subcategories[position]
                filterBySubcategory(category, selectedSubcategory)
                showFilterSnackbar(category, selectedSubcategory)
            }
            setText("", false)
        }
    }

    private fun showFilterSnackbar(category: String, subcategory: String) {
        Snackbar.make(binding.root, "Filtering by: $category > $subcategory", Snackbar.LENGTH_SHORT).show()
    }

    private fun setupRecyclerView() {
        adapter = AlternateNameAdapter { item -> openDeleteDialog(item) }
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@AlternateNamesActivity)
            adapter = this@AlternateNamesActivity.adapter
        }
        adapter.submitList(allItems)
    }

    private fun setupObservers() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.accountNames.collect { accountNames ->
                        updateSubcategories(accountNames)
                        binding.tvItemCount.text = accountNames.size.toString()
                    }
                }

                launch {
                    viewModel.loading.collect { isLoading ->
                        binding.progressbar.isVisible = isLoading
                    }
                }


                launch {
                    viewModel.error.collect { error ->
                        error?.let { Toast.makeText(this@AlternateNamesActivity, it, Toast.LENGTH_LONG).show() }
                    }
                }
            }
        }
    }

    private fun updateSubcategories(accountNames: List<String>) {
        subcategoriesMap["Accounts"] = accountNames
        if (binding.spinnerItems.text.toString() == "Accounts") {
            setupSubcategoryDropdown("Accounts")
        }
    }

    private fun filterItems(category: String) {
        val filteredList = if (category == "All") allItems else allItems.filter { it.category == category }
        adapter.submitList(filteredList)
        binding.tvItemCount.text = filteredList.size.toString()
    }

    private fun filterBySubcategory(category: String, subcategory: String) {
        val filteredList = allItems.filter {
            it.category == category && it.name.contains(subcategory, ignoreCase = true)
        }
        adapter.submitList(if (filteredList.isEmpty()) allItems else filteredList)
    }

    private fun openDeleteDialog(item: AlternateName) {
        Dialog(this).apply {
            setContentView(R.layout.dialog_delete_account)
            window?.apply {
                setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                setBackgroundDrawableResource(android.R.color.transparent)
            }

            findViewById<TextView>(R.id.AlternateAccountName).text = item.name
            findViewById<Button>(R.id.deleteButton).setOnClickListener {
                Snackbar.make(binding.root, "Deleted!", Snackbar.LENGTH_SHORT).show()
                dismiss()
            }
            findViewById<Button>(R.id.cancelButton).setOnClickListener { dismiss() }
            show()
        }
    }

    companion object {
        private val allItems = listOf(
            AlternateName("Nusrat Fateh Ali Khan", "Charges"),
            AlternateName("Ismat Chughtai", "Accounts"),
            AlternateName("Begum Rookery", "Items"),
            AlternateName("Abdul Sattar Edhi", "Accounts"),
            AlternateName("Malala Yousafzai", "Items"),
            AlternateName("Mohammed Rafi", "Accounts"),
            AlternateName("Khushwant Singh", "Charges"),
            AlternateName("Bulleh Shah", "Items"),
            AlternateName("Rumi", "Accounts"),
            AlternateName("Kishore Kumar", "Items"),
            AlternateName("Amjad Sabri", "Accounts")
        )
    }
}