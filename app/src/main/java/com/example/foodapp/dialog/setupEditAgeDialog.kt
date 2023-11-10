package com.example.foodapp.dialog

import android.annotation.SuppressLint
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.example.foodapp.R
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog

@SuppressLint("MissingInflatedId")
fun Fragment.setupEditAgeDialog(
    onEditClick: (String) -> Unit
) {
    val dialog = BottomSheetDialog(requireContext(), R.style.DialogStyle)
    val view = layoutInflater.inflate(R.layout.change_weight_dialog, null)
    dialog.setContentView(view)
    dialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED
    dialog.show()

    val etWeight = view.findViewById<EditText>(R.id.textEditAge)
    val btnEdit = view.findViewById<Button>(R.id.btnEditAge)
    val btnCancel = view.findViewById<Button>(R.id.btnCancelAge)

    btnEdit?.setOnClickListener {
        val weight = etWeight.text.toString().trim()
        onEditClick(weight)
        dialog.dismiss()
    }

    btnCancel?.setOnClickListener {
        dialog.dismiss()
    }
}