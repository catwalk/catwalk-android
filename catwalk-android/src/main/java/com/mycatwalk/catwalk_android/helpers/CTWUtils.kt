package com.mycatwalk.catwalk_android.helpers

import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.mycatwalk.catwalk_android.CTWApplication
import java.text.NumberFormat
import java.util.*


class CTWUtils {
    companion object {

        fun getColor(id: Int): Int {
            return ContextCompat.getColor(CTWApplication.appContext, id)
        }

        fun formatCurrencyFromCents(cents: Float): String {
            val numberFormat = NumberFormat.getCurrencyInstance(Locale("pt", "BR"))
            numberFormat.maximumFractionDigits = 2;
            return numberFormat.format(cents/100)
        }

        fun AppCompatActivity.addFragment(fragment: Fragment, frameId: Int){
            supportFragmentManager.inTransaction { add(frameId, fragment) }
        }

        fun AppCompatActivity.replaceFragment(fragment: Fragment, frameId: Int) {
            supportFragmentManager.inTransaction{replace(frameId, fragment)}
        }
    }

}

inline fun FragmentManager.inTransaction(func: FragmentTransaction.() -> FragmentTransaction) {
    beginTransaction().addToBackStack(null).func().commit()
}