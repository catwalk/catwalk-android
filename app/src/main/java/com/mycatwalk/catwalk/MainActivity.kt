package com.mycatwalk.catwalk

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.mycatwalk.catwalk.databinding.ActivityMainBinding
import com.mycatwalk.catwalk_android.config.CTWAssistantContext
import com.mycatwalk.catwalk_android.config.CTWAssistantContext.Companion.shouldShowItem
import com.mycatwalk.catwalk_android.protocols.CTWAssistantDelegate
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity(), CTWAssistantDelegate {
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)


        binding.btnFocusedState.setOnClickListener {
            checkIfItemShouldBeOpened(this, this, "YOUR_SKU")
        }

        binding.btnGlobalState.setOnClickListener {
            CTWAssistantContext.presentAssistant(this, this)
        }

        binding.btnOfflineState.setOnClickListener {
            CTWAssistantContext.presentOfflineState(this)
        }


        setContentView(binding.root)
    }

    private fun checkIfItemShouldBeOpened(hostActivity: AppCompatActivity, delegate: CTWAssistantDelegate, focusedSKU: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val shouldShowItem = shouldShowItem(focusedSKU)
            withContext(Dispatchers.Main) {
                if(shouldShowItem)
                    CTWAssistantContext.presentAssistant(hostActivity, delegate, focusedSKU)
                else
                    CTWAssistantContext.presentAssistant(hostActivity, delegate)
            }
        }
    }

    override fun didReturnShoppingItems(skus: Array<String>) {
        Log.d("DEBUG:", "FROM ACTIVITY didReturnShoppingItems: ${skus.joinToString(",")}")
    }

    override fun didReturnSingleItem(sku: String) {
        Log.d("DEBUG:", "FROM ACTIVITY didReturnSingleItem: $sku")
    }
}