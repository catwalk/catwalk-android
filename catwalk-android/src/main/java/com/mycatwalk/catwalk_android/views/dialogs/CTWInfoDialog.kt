package com.mycatwalk.catwalk_android.views.dialogs

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Window
import android.widget.TextView
import com.mycatwalk.catwalk_android.R
import com.mycatwalk.catwalk_android.config.CTWConfig

object CTWInfoDialog {
    var dialog: Dialog? = null //obj
    fun displayLoadingWithText(context: Context?, cancelable: Boolean, text: String? = CTWConfig.defaultErrorMessage ) { // function -- context(parent (reference))
        dialog = Dialog(context!!)
        dialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog!!.setContentView(R.layout.ctw_info_dialog)
        dialog!!.setCancelable(cancelable)
        dialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val tvTitle = dialog!!.findViewById<TextView>(R.id.tv_dialog_title)
        tvTitle.text = "Assistente diz"
        tvTitle.typeface = CTWConfig.getBoldTypeface()

        val tvDescription = dialog!!.findViewById<TextView>(R.id.tv_common_dialog)
        tvDescription.text = text
        tvDescription.typeface = CTWConfig.getRegularTypeface()

        val btnClose = dialog!!.findViewById<TextView>(R.id.btn_action)
        btnClose.typeface = CTWConfig.getBoldTypeface()
        btnClose.setOnClickListener {
            hideLoading()
        }
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