package com.muhasib.aliascreation.activity


import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.ShortcutManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.muhasib.aliascreation.R
import com.muhasib.aliascreation.databinding.ActivityCenterBinding

class CenterActivity : AppCompatActivity() {

    private lateinit var  binding : ActivityCenterBinding
    @SuppressLint("WrongThread")
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
            val manager = getSystemService(ShortcutManager::class.java)
            Log.d("SHORTCUTS", "Shortcuts: " + manager.dynamicShortcuts.size)
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