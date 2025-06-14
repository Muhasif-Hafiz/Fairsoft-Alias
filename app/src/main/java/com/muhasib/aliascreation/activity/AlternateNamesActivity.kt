package com.muhasib.aliascreation.activity

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.muhasib.aliascreation.R
import com.muhasib.aliascreation.adapters.AlternateNameAdapter
import com.muhasib.aliascreation.databinding.ActivityAlternateNamesBinding
import com.muhasib.aliascreation.model.AlternateName


class AlternateNamesActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAlternateNamesBinding
    private lateinit var adapter: AlternateNameAdapter

    private val allItems = listOf(
        AlternateName("Ali Baba", "Historical Figures"),
        AlternateName("Sheikh Noor", "Spiritual Leaders"),
        AlternateName("Raja Farooq", "Political Leaders"),
        AlternateName("Mehboob Khan", "Film Directors"),
        AlternateName("Umar Alvi", "Poets"),
        AlternateName("Sir Syed Ahmed", "Reformers"),
        AlternateName("Allama Iqbal", "Poets"),
        AlternateName("Zia Mohyeddin", "Artists"),
        AlternateName("Faiz Ahmad Faiz", "Poets"),
        AlternateName("Nusrat Fateh Ali Khan", "Musicians"),
        AlternateName("Ismat Chughtai", "Writers"),
        AlternateName("Begum Rookery", "Reformers"),
        AlternateName("Abdul Sattar Edhi", "Philanthropists"),
        AlternateName("Malala Yousafzai", "Activists"),
        AlternateName("Mohammed Rafi", "Musicians"),
        AlternateName("Khushwant Singh", "Writers"),
        AlternateName("Bulleh Shah", "Spiritual Leaders"),
        AlternateName("Rumi", "Spiritual Leaders"),
        AlternateName("Kishore Kumar", "Artists"),
        AlternateName("Amjad Sabri", "Musicians")
    )


    private val categories = listOf(
        "All",
        "Historical Figures",
        "Spiritual Leaders",
        "Political Leaders",
        "Poets",
        "Film Directors",
        "Writers",
        "Artists",
        "Musicians",
        "Reformers",
        "Activists",
        "Philanthropists"
    )


    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAlternateNamesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupCategoryDropdown()
        setupRecyclerView()
        binding.tvItemCount.text = allItems.size.toString()
    }

    private fun setupCategoryDropdown() {
        val dropdownAdapter = ArrayAdapter(
            this,
            R.layout.simple_spinner_dropdown_item,
            categories
        ).also { adapter ->

            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }

        binding.spinnerItems.apply {
            setAdapter(dropdownAdapter)

            setOnItemClickListener { _, _, position, _ ->
                filterItems(categories[position])
            }

            if (categories.isNotEmpty()) {
                setText(categories[0], false)
            }
        }
    }

    private fun setupRecyclerView() {
        adapter = AlternateNameAdapter(
            onClickListener = { item ->
                openDeleteDialog(item)
            }
        )
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter
        adapter.submitList(allItems)
    }

    @SuppressLint("SetTextI18n")
    private fun filterItems(category: String) {
        val filteredList = if (category == "All") {
            allItems
        } else {
            allItems.filter { it.category == category }
        }
        adapter.submitList(filteredList)
        binding.tvItemCount.text = filteredList.size.toString()
    }

    private fun openDeleteDialog(item: AlternateName) {

        val dialog = Dialog(this).apply {
            setContentView(R.layout.dialog_delete_account)
            window?.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            window?.setBackgroundDrawableResource(android.R.color.transparent)

            findViewById<TextView>(R.id.AlternateAccountName).text = item.name


            findViewById<Button>(R.id.deleteButton).setOnClickListener {

                Snackbar.make(binding.root, "Deleted!", Snackbar.LENGTH_SHORT).show()
                dismiss()
            }
            findViewById<Button>(R.id.cancelButton).setOnClickListener {
                dismiss()
            }
        }
        dialog.show()
    }

}