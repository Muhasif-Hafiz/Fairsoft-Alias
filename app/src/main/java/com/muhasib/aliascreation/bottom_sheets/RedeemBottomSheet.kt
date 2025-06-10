package com.muhasib.aliascreation.bottom_sheets

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import android.widget.Button
import com.airbnb.lottie.LottieAnimationView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.snackbar.Snackbar
import com.muhasib.aliascreation.R


class RedeemBottomSheet : BottomSheetDialogFragment() {


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        // Set transparent background for the dialog window
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        return dialog
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.bottom_sheet_redeem, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val redeemButton: Button = view.findViewById(R.id.btnRedeemNow)

        redeemButton.setOnClickListener {
            Snackbar.make(view, "Redeem Successful", Snackbar.LENGTH_SHORT).show()
            dismiss()
        }
    }
}
