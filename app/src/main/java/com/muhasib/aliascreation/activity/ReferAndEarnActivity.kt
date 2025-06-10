package com.muhasib.aliascreation.activity

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import com.muhasib.aliascreation.R
import com.muhasib.aliascreation.bottom_sheets.RedeemBottomSheet
import com.muhasib.aliascreation.databinding.ActivityReferAndEarnBinding

class ReferAndEarnActivity : AppCompatActivity() {

    private lateinit var binding : ActivityReferAndEarnBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityReferAndEarnBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // ✅ Set white status bar and dark icons
        window.statusBarColor = Color.WHITE
        WindowCompat.getInsetsController(window, window.decorView)
            ?.isAppearanceLightStatusBars = true
        ViewCompat.setOnApplyWindowInsetsListener(binding.rootLayout) { view, insets ->
            val imeInsets = insets.getInsets(WindowInsetsCompat.Type.ime())
            view.setPadding(0, 0, 0, imeInsets.bottom)
            insets
        }

        binding.arrowRight.setOnClickListener {
            openBottomSheet()
        }
    }

    private fun openBottomSheet(){


        val bottomSheet = RedeemBottomSheet()
        bottomSheet.show(supportFragmentManager, "RedeemBottomSheet")

    }

}