package com.mycatwalk.catwalk_android

import android.app.Application
import android.content.Context
import com.mycatwalk.catwalk_android.networking.VolleyManager

class CTWApplication: Application() {

    companion object {
        lateinit  var appContext: Context

        fun setContext(context: Context) {
            appContext = context
            VolleyManager.initConfig(context)
        }

    }
}