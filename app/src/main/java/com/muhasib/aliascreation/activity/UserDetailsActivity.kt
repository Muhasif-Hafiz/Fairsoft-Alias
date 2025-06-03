package com.muhasib.aliascreation.activity

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.muhasib.aliascreation.R
import com.muhasib.aliascreation.databinding.ActivityUserDetailsBinding
import com.muhasib.aliascreation.model.UserDetails

class UserDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUserDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityUserDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadData()
        updateProfile()
        editProfilePicture()
        handleBackPress()
    }

    @SuppressLint("SetTextI18n")
    private fun loadData() {

        val data = getDataFromApi()
        binding.etName.setText(" ${data.userName}")
        binding.etNumber.setText(" ${data.mobile}")
        binding.etMailId.setText(" ${data.mailId}")
        binding.etRecoveryPhone.setText("${data.recoveryMobile}")
        binding.etRecoveryEmail.setText(" ${data.recoveryMail}")
        loadImage(data.imageLink.toString())
        managePhoneRecovery(data)
        manageMailRecovery(data)

    }

    @SuppressLint("SetTextI18n")
    private fun managePhoneRecovery(data: UserDetails) {
        if (data.isMobileVerified == true) {
            binding.chipTextPhone.apply {
                text = "✓ Verified"
                setTextColor(ContextCompat.getColor(context, R.color.verifiedText))
                background = ContextCompat.getDrawable(context, R.drawable.chip_verified_background)
            }
        } else {
            binding.chipTextPhone.apply {
                text = "✘ Not Verified"
                setTextColor(ContextCompat.getColor(context, R.color.notVerifiedText))
                background =
                    ContextCompat.getDrawable(context, R.drawable.chip_not_verified_background)
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun manageMailRecovery(data: UserDetails) {
        if (data.isMailVerified == true) {
            binding.chipMailRecovery.apply {
                text = "✓ Verified"
                setTextColor(ContextCompat.getColor(context, R.color.verifiedText))
                background = ContextCompat.getDrawable(context, R.drawable.chip_verified_background)
            }
        } else {
            binding.chipMailRecovery.apply {
                text = "✘ Not Verified"
                setTextColor(ContextCompat.getColor(context, R.color.notVerifiedText))
                background =
                    ContextCompat.getDrawable(context, R.drawable.chip_not_verified_background)
            }
        }
    }

    private fun getDataFromApi(): UserDetails {
        // Call Api here to get the required data.
        val userDetails = UserDetails(
            1,
            "Muhasif Hafiz",
            true,
            mobile = "9419358727",
            mailId = "muhasibhafeez91@gmail.com",
            imageLink = "https://avatars.githubusercontent.com/u/146239715?v=4?s=400",
            fcmToken = "123456",
            deviceId = "123456",
            iDeviceId = "123456",
            recoveryMobile = "6006418251",
            recoveryMail = "muhasibhafeez10@ieee.com",
            isMobileVerified = true,
            isMailVerified = false,
        )
        return userDetails
    }

    private fun loadImage(url: String) {

        Glide.with(this)
            .load(url)
            .placeholder(R.drawable.user_profile)
            .error(R.drawable.user_profile)
            .centerCrop()
            .into(binding.profileImage)
    }

    private fun updateProfile() {

        binding.btnUpdate.setOnClickListener {
            if (validateData()) {

                Snackbar.make(
                    binding.btnUpdate,
                    "Profile Updated Successfully",
                    Snackbar.LENGTH_LONG
                )
                    .setAnimationMode(Snackbar.ANIMATION_MODE_SLIDE)
                    .setBackgroundTint(ContextCompat.getColor(this, R.color.verifiedText))
                    .setTextColor(Color.WHITE)
                    .show()
            }
        }
    }

    private fun validateData(): Boolean {
        val name = binding.etName.text.toString()
        val email = binding.etMailId.text.toString()
        val recoveryPhone = binding.etRecoveryPhone.text.toString()
        val recoveryEmail = binding.etRecoveryEmail.text.toString()

        binding.emailError.visibility = View.GONE
        binding.recoveryEmailError.visibility = View.GONE

        var isValid = true

        if (name.isEmpty()) {
            binding.etName.error = "Name is required"
            isValid = false
        } else if (name.length > 250) {
            binding.etName.error = "Name must be less than 250 characters"
            isValid = false
        }

        if (email.isNotEmpty() && !Patterns.EMAIL_ADDRESS.matcher(email.trim()).matches()) {
            binding.emailError.visibility = View.VISIBLE
            isValid = false
        }

        if (recoveryEmail.isNotEmpty() && !Patterns.EMAIL_ADDRESS.matcher(recoveryEmail.trim())
                .matches()
        ) {
            binding.recoveryEmailError.visibility = View.VISIBLE
            isValid = false
        }

        if (recoveryPhone.isNotEmpty()) {
            if (recoveryPhone.length != 10 || !recoveryPhone.matches(Regex("^[0-9]*$"))) {
                binding.etRecoveryPhone.error = "Enter a valid Phone Number!"
                isValid = false
            }
        }
        return isValid
    }

    private fun editProfilePicture() {
        binding.cameraIcon.setOnClickListener {
            Toast.makeText(this, "Requires an Api call", Toast.LENGTH_SHORT).show()
        }
    }

    private fun handleBackPress() {
        binding.backButton.setOnClickListener { onBackPressedDispatcher.onBackPressed() }
    }
}