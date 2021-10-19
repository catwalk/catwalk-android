package com.mycatwalk.catwalk_android.networking

import android.content.Context
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley

object VolleyManager{
    private lateinit var context: Context
    val requestQueue: RequestQueue by lazy {
        Volley.newRequestQueue(context)
    }

    fun initConfig(context:Context){
        VolleyManager.context = context.applicationContext
    }
}
