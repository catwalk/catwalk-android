package com.mycatwalk.catwalk

import android.app.Application
import com.mycatwalk.catwalk_android.CTWApplication
import com.mycatwalk.catwalk_android.config.CTWConfig

class MyApp: Application() {
    override fun onCreate() {
        super.onCreate()
        CTWApplication.setContext(applicationContext)
        CTWConfig.apiToken = "YOUR_API_TOKEN"
    }
}