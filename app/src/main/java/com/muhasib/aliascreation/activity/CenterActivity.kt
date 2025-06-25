package com.muhasib.aliascreation.activity


import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.muhasib.aliascreation.R
import com.muhasib.aliascreation.databinding.ActivityCenterBinding

class CenterActivity : AppCompatActivity() {

    private lateinit var  binding : ActivityCenterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityCenterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding.MainActivity.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
        binding.ReferAndEarn.setOnClickListener {
            startActivity(Intent(this, ReferAndEarnActivity::class.java))
        }
        binding.AlternateNames.setOnClickListener {
            startActivity(Intent(this, AlternateNamesActivity::class.java))
        }
        binding.FeaturedAccounts.setOnClickListener {
            startActivity(Intent(this, FeaturedAccountActivity::class.java))
        }

    }
}