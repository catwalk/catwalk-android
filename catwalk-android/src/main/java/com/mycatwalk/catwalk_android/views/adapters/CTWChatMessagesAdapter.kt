package com.mycatwalk.catwalk_android.views.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mycatwalk.catwalk_android.R
import com.mycatwalk.catwalk_android.config.CTWConfig
import com.mycatwalk.catwalk_android.models.CTWChatMessage
import com.mycatwalk.catwalk_android.views.activities.CTWGenieActivity
import com.mycatwalk.catwalk_android.views.fragments.CTWChatFragment

class CTWChatMessagesAdapter(private val fragment: CTWChatFragment, private val messages: MutableList<CTWChatMessage>) :
    RecyclerView.Adapter<CTWChatMessagesAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvMessage: TextView = view.findViewById(R.id.tv_message)
        val reviewButtonsContainer: LinearLayout? = view.findViewById(R.id.review_buttons_container)
        val btnPositiveReview: Button? = view.findViewById(R.id.btn_positive_review)
        val btnNegativeReview: Button? = view.findViewById(R.id.btn_negative_review)
    }


    override fun getItemViewType(position: Int): Int {
        return messages[position].sender?.ordinal ?: -1
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        when (viewType) {
            0 -> return ViewHolder(LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.ctw_user_message, viewGroup, false))
            1 -> return ViewHolder(LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.ctw_assistant_message, viewGroup, false))
            3 -> return ViewHolder(LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.ctw_assistant_message, viewGroup, false))

        }
        return ViewHolder(LayoutInflater.from(viewGroup.context).inflate(R.layout.ctw_assistant_message, viewGroup, false))
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val message: CTWChatMessage = messages[position]
        val senderType: Int = messages[position].sender?.ordinal ?: 0
        val messageType: Int = messages[position].type?.ordinal ?: 0
        viewHolder.tvMessage.text = message.text

        enableCustomConfig(viewHolder, senderType)

        if (senderType == 0) {
            viewHolder.tvMessage.setTextColor(CTWConfig.getChatScreenUserMessageColor())
        } else {
            viewHolder.tvMessage.setTextColor(CTWConfig.getChatScreenAssistantMessageColor())
        }

        if (messageType == 7) {
            viewHolder.reviewButtonsContainer?.visibility = View.VISIBLE
            viewHolder.btnPositiveReview?.setOnClickListener {
                (fragment.activity as? CTWGenieActivity)?.sendAttendanceReview(true)
            }
            viewHolder.btnNegativeReview?.setOnClickListener {
                (fragment.activity as? CTWGenieActivity)?.sendAttendanceReview(false)
            }
        } else {
            viewHolder.reviewButtonsContainer?.visibility = View.GONE
        }
    }

    private fun enableCustomConfig(viewHolder: ViewHolder, senderType: Int) {
        if (senderType == 0) {
            viewHolder.tvMessage.typeface = CTWConfig.getBoldTypeface()
            viewHolder.tvMessage.setTextColor(CTWConfig.getChatScreenUserMessageColor())
        } else {
            viewHolder.tvMessage.typeface = CTWConfig.getItalicTypeface()
            viewHolder.tvMessage.setTextColor(CTWConfig.getChatScreenAssistantMessageColor())
        }

        if (viewHolder.btnNegativeReview != null && viewHolder.btnPositiveReview != null) {
            viewHolder.btnPositiveReview.typeface = CTWConfig.getRegularTypeface()
            viewHolder.btnNegativeReview.typeface = CTWConfig.getRegularTypeface()
        }

    }


    override fun getItemCount() = messages.size

}