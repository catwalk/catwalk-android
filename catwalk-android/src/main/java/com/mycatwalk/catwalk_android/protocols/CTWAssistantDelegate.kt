package com.mycatwalk.catwalk_android.protocols

interface CTWAssistantDelegate {
    fun didReturnShoppingItems(skus: Array<String>)
    fun didReturnSingleItem(sku: String)
}