package com.muhasib.aliascreation.activity

import android.graphics.Color
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.snackbar.Snackbar
import com.muhasib.aliascreation.databinding.ActivityReferAndEarnBinding

@Suppress("DEPRECATION")
class ReferAndEarnActivity : AppCompatActivity() {

    private lateinit var binding: ActivityReferAndEarnBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityReferAndEarnBinding.inflate(layoutInflater)
        setContentView(binding.rootLayout)
        window.statusBarColor = Color.WHITE
        WindowCompat.getInsetsController(window, window.decorView).isAppearanceLightStatusBars =
            true
        ViewCompat.setOnApplyWindowInsetsListener(binding.rootLayout) { view, insets ->
            val imeInsets = insets.getInsets(WindowInsetsCompat.Type.ime())
            view.setPadding(0, 0, 0, imeInsets.bottom)
            insets
        }

        binding.btnRedeemNow.setOnClickListener { redeemRewards() }
    }

    private fun redeemRewards() {
        Snackbar.make(binding.root, "Rewards redeemed successfully!", Snackbar.LENGTH_SHORT).show()
    }

}