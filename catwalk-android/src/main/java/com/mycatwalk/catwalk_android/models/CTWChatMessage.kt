package com.mycatwalk.catwalk_android.models

import java.io.Serializable

data class CTWChatMessage (val text: String?,
                           val likedLook: CTWChatMessageType?,
                           val sender: ChatMessageSender?): Serializable

enum class CTWChatMessageType {
    PlainText,
    Look,
    Similar,
    AvailableColors,
    AvailableSizes,
    TrendingClothing,
    Buy,
    Review
}

enum class ChatMessageSender {
    User,
    Assistant
}
