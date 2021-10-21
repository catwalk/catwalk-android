package com.mycatwalk.catwalk_android.config

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import com.mycatwalk.catwalk_android.networking.CTWNetworkManager
import com.mycatwalk.catwalk_android.protocols.CTWAssistantDelegate
import com.mycatwalk.catwalk_android.views.activities.CTWGenieActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CTWAssistantContext {
    companion object {
        var hostActivity: AppCompatActivity? = null
        var delegate: CTWAssistantDelegate? = null
        var focusedSKU: String? = null
        var offlineState: Boolean = false

        suspend fun shouldShowItem(sku: String): Boolean {
            val itemAvailability = CTWNetworkManager.checkItemAvailability(sku, true)
            return itemAvailability.available ?: false
        }

        fun presentAssistant(hostActivity: AppCompatActivity, delegate: CTWAssistantDelegate, sku: String? = null) {
            this.hostActivity = hostActivity
            this.delegate = delegate
            this.focusedSKU = sku
            this.offlineState = false

            if(sku != null) {
                GlobalScope.launch {
                    val skuAvailability = CTWNetworkManager.checkItemAvailability(sku, true)
                    withContext(Dispatchers.Main) {
                        if(skuAvailability.available != true) {
                            focusedSKU = null
                        }

                        openAssistantFromHost()
                    }
                }
            } else {
                openAssistantFromHost()
            }

        }

        private fun openAssistantFromHost() {
            val intent = Intent(hostActivity, CTWGenieActivity::class.java)
            hostActivity?.startActivity(intent)
        }

        fun presentOfflineState(hostActivity: AppCompatActivity) {
            this.offlineState = true
            this.hostActivity = hostActivity
            openAssistantFromHost()
        }
    }



}