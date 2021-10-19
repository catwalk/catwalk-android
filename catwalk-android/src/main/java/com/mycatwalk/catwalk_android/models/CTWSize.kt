package com.mycatwalk.catwalk_android.models

import java.io.Serializable

data class CTWSize (val identifier: String?,
                    val available: Boolean?,
                    val sku: String?,
                    val price: CTWPrice?) : Serializable