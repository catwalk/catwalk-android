package com.mycatwalk.catwalk

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.mycatwalk.catwalk.databinding.ActivityMainBinding
import com.mycatwalk.catwalk_android.config.CTWAssistantContext
import com.mycatwalk.catwalk_android.protocols.CTWAssistantDelegate

class MainActivity : AppCompatActivity(), CTWAssistantDelegate {
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        binding.btnFocusedState.setOnClickListener {
            val focusedSKU = "YOUR_SKU_HERE"
            CTWAssistantContext.presentAssistant(this, this, focusedSKU)
        }

        binding.btnGlobalState.setOnClickListener {
            CTWAssistantContext.presentAssistant(this, this)
        }

        binding.btnOfflineState.setOnClickListener {
            CTWAssistantContext.presentOfflineState(this)
        }


        setContentView(binding.root)
    }

    override fun didReturnShoppingItems(skus: Array<String>) {
        Log.d("DEBUG:", "FROM ACTIVITY didReturnShoppingItems: ${skus.joinToString(",")}")
    }

    override fun didReturnSingleItem(sku: String) {
        Log.d("DEBUG:", "FROM ACTIVITY didReturnSingleItem: $sku")
    }
}