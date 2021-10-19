package com.mycatwalk.catwalk_android.config

import android.graphics.Color
import android.graphics.Typeface
import com.mycatwalk.catwalk_android.CTWApplication

class CTWConfig {
    companion object {
        var apiToken: String? = null
        val bundle: String = CTWApplication.appContext.packageName
        var sessionId: String? = null

        private var menuScreenBackgroundColor: Int = Color.parseColor("#0000FF")
        private var menuButtonBackgroundColor: Int = Color.parseColor("#FFFFFF")
        private var menuButtonFontColor: Int = Color.parseColor("#CC2C8D")
        private var generalButtonBackgroundColor: Int = Color.parseColor("#0000FF")
        private var generalButtonFontColor: Int = Color.parseColor("#FFFFFF")

        private var regularTypeface: Typeface = Typeface.createFromAsset(CTWApplication.appContext.assets, "fonts/greycliff_cf_regular.ttf")
        private var lightTypeface: Typeface = Typeface.createFromAsset(CTWApplication.appContext.assets, "fonts/greycliff_cf_light.ttf")
        private var boldTypeface: Typeface = Typeface.createFromAsset(CTWApplication.appContext.assets, "fonts/greycliff_cf_bold.ttf")
        private var italicTypeface: Typeface = Typeface.createFromAsset(CTWApplication.appContext.assets, "fonts/greycliff_cf_italic.ttf")

        var defaultErrorMessage: String = "Parece haver um erro na sua requisição. Por favor, tente novamente mais tarde."

        fun getMenuScreenBackgroundColor(): Int {
            return menuScreenBackgroundColor
        }

        fun setMenuScreenBackgroundColor(hex: String) {
            menuScreenBackgroundColor = Color.parseColor(hex)
        }

        fun getMenuButtonBackgroundColor(): Int {
            return menuButtonBackgroundColor
        }

        fun setMenuButtonBackgroundColor(hex: String) {
            menuButtonBackgroundColor = Color.parseColor(hex)
        }

        fun getMenuButtonFontColor(): Int {
            return menuButtonFontColor
        }

        fun setMenuButtonFontColor(hex: String) {
            menuButtonFontColor = Color.parseColor(hex)
        }

        fun getGeneralButtonBackgroundColor(): Int {
            return generalButtonBackgroundColor
        }

        fun setGeneralButtonBackgroundColor(hex: String) {
            generalButtonBackgroundColor = Color.parseColor(hex)
        }

        fun getGeneralButtonTextColor(): Int {
            return generalButtonFontColor
        }

        fun setGeneralButtonTextColor(hex: String) {
            generalButtonFontColor = Color.parseColor(hex)
        }

        fun getRegularTypeface(): Typeface {
            return regularTypeface
        }

        fun setRegularTypeface(typeface: Typeface) {
            regularTypeface = typeface
        }

        fun getLightTypeface(): Typeface {
            return lightTypeface
        }

        fun setLightTypeface(typeface: Typeface) {
            lightTypeface = typeface
        }

        fun getBoldTypeface(): Typeface {
            return boldTypeface
        }

        fun setBoldTypeface(typeface: Typeface) {
            boldTypeface = typeface
        }

        fun getItalicTypeface(): Typeface {
            return boldTypeface
        }

        fun setItalicTypeface(typeface: Typeface) {
            italicTypeface = typeface
        }
    }
}