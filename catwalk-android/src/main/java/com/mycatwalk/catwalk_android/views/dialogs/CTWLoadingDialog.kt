package com.mycatwalk.catwalk_android.views.dialogs

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Window
import android.widget.TextView
import com.mycatwalk.catwalk_android.R

object CTWLoadingDialog {
    var dialog: Dialog? = null //obj
    fun displayLoadingWithText(context: Context?, cancelable: Boolean, text: String? = "Carregando", ) { // function -- context(parent (reference))
        dialog = Dialog(context!!)
        dialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog!!.setContentView(R.layout.ctw_loading_dialog)
        dialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog!!.setCancelable(cancelable)
        val textView = dialog!!.findViewById<TextView>(R.id.text)
        textView.text = text
        try {
            dialog!!.show()
        } catch (e: Exception) {
        }
    }

    fun hideLoading() {
        try {
            if (dialog != null) {
                dialog!!.dismiss()
            }
        } catch (e: Exception) {
        }
    }
}