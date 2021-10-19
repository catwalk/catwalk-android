package com.mycatwalk.catwalk_android.networking

import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.google.gson.Gson
import com.mycatwalk.catwalk_android.config.CTWConfig.Companion.apiToken
import com.mycatwalk.catwalk_android.config.CTWConfig.Companion.bundle
import com.mycatwalk.catwalk_android.config.CTWConfig.Companion.sessionId
import com.mycatwalk.catwalk_android.models.*
import org.json.JSONObject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class CTWNetworkManager {

    companion object {
        private const val CTWLK_API_ROOT = "https://app.mycatwalk.com/mobile"

        suspend fun fetchSessionInfo() = suspendCoroutine<String> { cont ->
            val request = object: JsonObjectRequest(
                 "$CTWLK_API_ROOT/session",
                { response ->
                    cont.resume("Response is: ${response.toString()}")
                },
                { cont.resume("Something went wrong!") })
            {
                override fun getHeaders(): MutableMap<String, String> {
                    return generateHeadersMap()
                }
            }
            VolleyManager.requestQueue.add(request)
        }

        suspend fun endSession() = suspendCoroutine<String> { cont ->
            val request = object: JsonObjectRequest(
                Method.POST,
                "$CTWLK_API_ROOT/session",
                JSONObject(),
                { response ->
                    cont.resume("Response is: ${response.toString()}")
                },
                { cont.resume("Something went wrong!") })
            {
                override fun getHeaders(): MutableMap<String, String> {
                    return generateHeadersMap()
                }
            }
            VolleyManager.requestQueue.add(request)
        }

        suspend fun sendAttendanceReview(positive: Boolean) = suspendCoroutine<String> { cont ->
            val postObj = JSONObject()
            postObj.put("positive", positive)
            val request = object: JsonObjectRequest(
                Method.POST,
                "$CTWLK_API_ROOT/session",
                postObj,
                { response ->
                    cont.resume("Response is: ${response.toString()}")
                },
                { cont.resume("Something went wrong!") })
            {
                override fun getHeaders(): MutableMap<String, String> {
                    return generateHeadersMap()
                }
            }
            VolleyManager.requestQueue.add(request)
        }

        suspend fun checkItemAvailability(sku: String, showCheck: Boolean? = false) = suspendCoroutine<CTWAvailability> { cont ->
            var urlString = "$CTWLK_API_ROOT/sku/availability"
            if(showCheck == true) urlString += "?showCheck=true"
            val request = object: JsonObjectRequest(
                urlString,
                { response ->
                    cont.resume(CTWAvailability(
                        response.optInt("code"),
                        response.optString("message"),
                        response.optBoolean("available")))
                },
                { cont.resume(CTWAvailability(
                    200,
                    null,
                    false)) })
            {
                override fun getHeaders(): MutableMap<String, String> {
                    return generateHeadersMap(sku)
                }
            }
            VolleyManager.requestQueue.add(request)
        }

        suspend fun fetchLooks(sku: String) = suspendCoroutine<Array<CTWLook>> { cont ->
            val request = object: StringRequest(
                "$CTWLK_API_ROOT/combinations?structured=true",
                { looks ->
                    cont.resume(Gson().fromJson(looks, Array<CTWLook>::class.java))
                },
                { cont.resume(emptyArray()) })
            {
                override fun getHeaders(): MutableMap<String, String> {
                    return generateHeadersMap(sku)
                }
            }
            VolleyManager.requestQueue.add(request)
        }

        suspend fun fetchSimilarItems(sku: String) = suspendCoroutine<Array<String>> { cont ->
            val request = object: StringRequest(
                "$CTWLK_API_ROOT/similars",
                { similarItems ->
                    cont.resume(Gson().fromJson(similarItems, Array<String>::class.java))
                },
                { cont.resume(emptyArray()) })
            {
                override fun getHeaders(): MutableMap<String, String> {
                    return generateHeadersMap(sku)
                }
            }
            VolleyManager.requestQueue.add(request)
        }

        suspend fun availableColors(sku: String) = suspendCoroutine<Array<String>> { cont ->
            val request = object: StringRequest(
                "$CTWLK_API_ROOT/availableColors",
                { availableColors ->
                    cont.resume(Gson().fromJson(availableColors, Array<String>::class.java))
                },
                { cont.resume(emptyArray()) })
            {
                override fun getHeaders(): MutableMap<String, String> {
                    return generateHeadersMap(sku)
                }
            }
            VolleyManager.requestQueue.add(request)
        }

        suspend fun fetchProductsInfoFromProductIds(productIds: Array<String>) = suspendCoroutine<Array<CTWProduct>> { cont ->
            val productIdsAsString = productIds.joinToString(",")
            val request = object: StringRequest(
                "$CTWLK_API_ROOT/productsInfo?productIds=$productIdsAsString",
                { productsInfo ->
                    cont.resume(Gson().fromJson(productsInfo, Array<CTWProduct>::class.java))
                },
                { cont.resume(emptyArray()) })
            {
                override fun getHeaders(): MutableMap<String, String> {
                    return generateHeadersMap()
                }
            }
            VolleyManager.requestQueue.add(request)
        }

        suspend fun fetchProductsInfoFromSKUS(skus: Array<String>) = suspendCoroutine<Array<CTWProduct>> { cont ->
            val productIdsAsString = skus.joinToString(",")
            val request = object: StringRequest(
                "$CTWLK_API_ROOT/productsInfo?skus=$productIdsAsString",
                { productsInfo ->
                    cont.resume(Gson().fromJson(productsInfo, Array<CTWProduct>::class.java))
                },
                { cont.resume(emptyArray()) })
            {
                override fun getHeaders(): MutableMap<String, String> {
                    return generateHeadersMap()
                }
            }
            VolleyManager.requestQueue.add(request)
        }

        suspend fun fetchProductsInfoBySKU(sku: String) = suspendCoroutine<CTWProduct?> { cont ->
            val request = object: StringRequest(
                "$CTWLK_API_ROOT/product?sku=$sku",
                { product ->
                    cont.resume(Gson().fromJson(product, CTWProduct::class.java))
                },
                { cont.resume(null) })
            {
                override fun getHeaders(): MutableMap<String, String> {
                    return generateHeadersMap()
                }
            }
            VolleyManager.requestQueue.add(request)
        }

        suspend fun fetchProductsInfoByProductId(productId: String) = suspendCoroutine<CTWProduct?> { cont ->
            val request = object: StringRequest(
                "$CTWLK_API_ROOT/product?productId=$productId",
                { product ->
                    cont.resume(Gson().fromJson(product, CTWProduct::class.java))
                },
                { cont.resume(null) })
            {
                override fun getHeaders(): MutableMap<String, String> {
                    return generateHeadersMap()
                }
            }
            VolleyManager.requestQueue.add(request)
        }

        suspend fun fetchTrendingSKUs() = suspendCoroutine<Array<String>> { cont ->
            val request = object: StringRequest(
                "$CTWLK_API_ROOT/trending/clothing",
                { availableColors ->
                    cont.resume(Gson().fromJson(availableColors, Array<String>::class.java))
                },
                { cont.resume(emptyArray()) })
            {
                override fun getHeaders(): MutableMap<String, String> {
                    return generateHeadersMap()
                }
            }
            VolleyManager.requestQueue.add(request)
        }

        suspend fun fetchTrendingClothingAsLooks() = suspendCoroutine<Array<CTWLook>> { cont ->
            val request = object: StringRequest(
                "$CTWLK_API_ROOT/trending/clothingInLooks",
                { looks ->
                    cont.resume(Gson().fromJson(looks, Array<CTWLook>::class.java))
                },
                { cont.resume(emptyArray()) })
            {
                override fun getHeaders(): MutableMap<String, String> {
                    return generateHeadersMap()
                }
            }
            VolleyManager.requestQueue.add(request)
        }

        suspend fun fetchCombinationsByHue(hue: Int) = suspendCoroutine<Array<CTWLook>> { cont ->
            val request = object: StringRequest(
                "$CTWLK_API_ROOT/combinations?hue=$hue",
                { looks ->
                    cont.resume(Gson().fromJson(looks, Array<CTWLook>::class.java))
                },
                { cont.resume(emptyArray()) })
            {
                override fun getHeaders(): MutableMap<String, String> {
                    return generateHeadersMap()
                }
            }
            VolleyManager.requestQueue.add(request)
        }

        suspend fun fetchMessageResponse(message: String, sku: String?) = suspendCoroutine<CTWChatMessage?> { cont ->
            val postObj = JSONObject()
            postObj.put("message", message)
            val request = object: JsonObjectRequest(
                Method.POST,
                "$CTWLK_API_ROOT/chat/message",
                postObj,
                { message ->
                    cont.resume(Gson().fromJson(message.toString(), CTWChatMessage::class.java))
                },
                { cont.resume(null) })
            {
                override fun getHeaders(): MutableMap<String, String> {
                    return generateHeadersMap(sku)
                }
            }
            VolleyManager.requestQueue.add(request)
        }

        suspend fun likeLook(productIds: Array<String>) = suspendCoroutine<CTWDefaultResponse?> { cont ->
            val postObj = JSONObject()
            postObj.put("productIds", productIds)
            val request = object: JsonObjectRequest(
                Method.POST,
                "$CTWLK_API_ROOT/combinations/like",
                postObj,
                { message ->
                    cont.resume(Gson().fromJson(message.toString(), CTWDefaultResponse::class.java))
                },
                { cont.resume(null) })
            {
                override fun getHeaders(): MutableMap<String, String> {
                    return generateHeadersMap()
                }
            }
            VolleyManager.requestQueue.add(request)
        }

        fun generateHeadersMap(sku: String? = null): HashMap<String, String> {
            val headers = HashMap<String, String>()
            headers["apiToken"] = apiToken ?: ""
            headers["bundle"] = bundle
            if(sessionId != null) {
                headers["sessionId"] = sessionId!!
            }

            if(sku != null) {
                headers["sku"] = sku
            }

            return headers
        }
    }



//
//    fun fetchResponse() {
//
//        val stringRequest = StringRequest(
//            Request.Method.GET, url,
//            { response ->
//                // Display the first 500 characters of the response string.
//                return response
//            },
//            { err -> textView.text = "That didn't work!" })
//    }
//
//    //sampleStart
//    fun main() = Promise  { // this: CoroutineScope
//        launch { // launch a new coroutine and continue
//            delay(1000L) // non-blocking delay for 1 second (default time unit is ms)
//            println("World!") // print after delay
//        }
//        println("Hello") // main coroutine continues while a previous one is delayed
//    }

}