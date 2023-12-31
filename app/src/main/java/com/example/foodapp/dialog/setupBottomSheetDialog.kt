package com.example.foodapp.dialog

import android.widget.Button
import android.widget.EditText
import android.annotation.SuppressLint
import androidx.fragment.app.Fragment
import com.example.foodapp.R
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog

@SuppressLint("MissingInflatedId")
fun Fragment.setupBottomSheetDialog(
    onSendClick: (String) -> Unit
) {
    val dialog = BottomSheetDialog(requireContext(), R.style.DialogStyle)
    val view = layoutInflater.inflate(R.layout.reset_password_dialog, null)
    dialog.setContentView(view)
    dialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED
    dialog.show()

    val etEmail = view.findViewById<EditText>(R.id.textResetPassword)
    val btnSend = view.findViewById<Button>(R.id.btnSend)
    val btnCancle = view.findViewById<Button>(R.id.btnCancel)

    btnSend?.setOnClickListener {
        val email = etEmail.text.toString().trim()
        onSendClick(email)
        dialog.dismiss()
    }

    btnCancle?.setOnClickListener {
        dialog.dismiss()
    }
}