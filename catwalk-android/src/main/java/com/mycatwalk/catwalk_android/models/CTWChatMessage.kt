package com.mycatwalk.catwalk_android.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class CTWChatMessage (val text: String?,
                           val type: CTWChatMessageType?,
                           val sender: CTWChatMessageSender?): Serializable

enum class CTWChatMessageType {
    @SerializedName("1")
    PlainText,
    @SerializedName("2")
    Look,
    @SerializedName("3")
    Similar,
    @SerializedName("4")
    AvailableColors,
    @SerializedName("5")
    AvailableSizes,
    @SerializedName("6")
    TrendingClothing,
    @SerializedName("7")
    Buy,
    @SerializedName("8")
    Review
}

enum class CTWChatMessageSender {
    @SerializedName("1")
    User,
    @SerializedName("2")
    Assistant
}
