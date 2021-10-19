package com.mycatwalk.catwalk_android.models

import java.io.Serializable

data class CTWProduct (val headline: String?,
                       val productId: String?,
                       val image: String?,
                       val sku: String?,
                       val price: CTWPrice?,
                       val sizes: List<CTWSize>?,
                       var chosenSKU: String?) : Serializable