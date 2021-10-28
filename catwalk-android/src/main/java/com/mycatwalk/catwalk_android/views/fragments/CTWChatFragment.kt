package com.mycatwalk.catwalk_android.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.mycatwalk.catwalk_android.CTWApplication
import com.mycatwalk.catwalk_android.config.CTWAssistantContext
import com.mycatwalk.catwalk_android.config.CTWConfig
import com.mycatwalk.catwalk_android.databinding.FragmentCtwChatBinding
import com.mycatwalk.catwalk_android.models.CTWChatMessage
import com.mycatwalk.catwalk_android.models.CTWChatMessageSender
import com.mycatwalk.catwalk_android.models.CTWChatMessageType
import com.mycatwalk.catwalk_android.networking.CTWNetworkManager
import com.mycatwalk.catwalk_android.views.activities.CTWGenieActivity
import com.mycatwalk.catwalk_android.views.activities.hideKeyboard
import com.mycatwalk.catwalk_android.views.adapters.CTWChatMessagesAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CTWChatFragment : Fragment() {

    private val TAG: String = "CTWChatFragment"

    lateinit var binding: FragmentCtwChatBinding

    var messages: MutableList<CTWChatMessage> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCtwChatBinding.inflate(layoutInflater, container, false)
        enableCustomConfig()
        setupListeners()
        setupMessagesList()
        return binding.root
    }

    private fun enableCustomConfig() {
        binding.root.setBackgroundColor(CTWConfig.getChatScreenBackgroundColor())
        binding.etMessage.typeface = CTWConfig.getRegularTypeface()
        binding.btnSendMessage.setTextColor(CTWConfig.getMenuButtonFontColor())
        binding.btnSendMessage.background.setTint(CTWConfig.getMenuButtonBackgroundColor())
        binding.btnSendMessage.typeface = CTWConfig.getRegularTypeface()
    }

    private fun setupListeners() {
        binding.btnSendMessage.setOnClickListener {
            val text = binding.etMessage.text.toString()
            if (text.isNotEmpty()) {
                messages.add(
                    CTWChatMessage(
                        text,
                        CTWChatMessageType.PlainText,
                        CTWChatMessageSender.User
                    )
                )

                fetchMessageResponse(binding.etMessage.text.toString())
                clearMessageInput()
            }
        }
    }

    private fun fetchMessageResponse(message: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val messageResponse = CTWNetworkManager.fetchMessageResponse(message, CTWAssistantContext.focusedSKU)
            if (messageResponse != null) {
                withContext(Dispatchers.Main) {
                    addMessage(messageResponse)
                    when (messageResponse.type) {
                        CTWChatMessageType.Look -> showLooks()
                        CTWChatMessageType.Similar -> showSimilar()
                        CTWChatMessageType.AvailableColors -> showAvailableColors()
                        CTWChatMessageType.AvailableSizes -> showAvailableSizes()
                        CTWChatMessageType.TrendingClothing -> showTrendingItems()
                        CTWChatMessageType.Buy -> buyItem()
                        else -> {}
                    }
                }
            }
        }
    }

    private fun showLooks() {
        val focusedSKU = CTWAssistantContext.focusedSKU
        if (focusedSKU != null) {
            (activity as? CTWGenieActivity)?.combine(focusedSKU)
        } else {
            (activity as? CTWGenieActivity)?.trendingLooks()
        }
    }

    private fun showSimilar() {
        val focusedSKU = CTWAssistantContext.focusedSKU
        if (focusedSKU != null) {
            (activity as? CTWGenieActivity)?.findSimilarItems(focusedSKU)
        } else {
            showTrendingItems()
        }
    }

    private fun showTrendingItems() {
        (activity as? CTWGenieActivity)?.trendingItems()
    }

    private fun showAvailableSizes() {
        val focusedSKU = CTWAssistantContext.focusedSKU
        if (focusedSKU != null) {
            (activity as? CTWGenieActivity)?.availableSizes(focusedSKU)
        } else {
            showTrendingItems()
        }
    }

    private fun showAvailableColors() {
        val focusedSKU = CTWAssistantContext.focusedSKU
        if (focusedSKU != null) {
            (activity as? CTWGenieActivity)?.availableColors(focusedSKU)
        } else {
            showTrendingItems()
        }
    }

    private fun buyItem() {
        val focusedSKU = CTWAssistantContext.focusedSKU
        if (focusedSKU != null) {
            CTWAssistantContext.delegate?.didReturnShoppingItems(arrayOf(focusedSKU))
            (activity as? CTWGenieActivity)?.finishAttendance()
        } else {
            showTrendingItems()
        }
    }

    private fun addMessage(message: CTWChatMessage) {
        messages.add(message)
        binding.rvChatMessages.adapter?.notifyItemInserted(messages.size - 1)
        binding.rvChatMessages.post {
            if(messages.size > 1) {
                binding.rvChatMessages.smoothScrollToPosition(
                    (binding.rvChatMessages.adapter?.itemCount ?: 1) - 1
                )
            }
        }
    }

    private fun clearMessageInput() {
        binding.etMessage.text.clear()
        binding.etMessage.clearFocus()
        view?.hideKeyboard()
    }

    private fun setupMessagesList() {
        val startingMessage = arguments?.getString("STARTING_MESSAGE")
        if (startingMessage != null) {
            fetchMessageResponse(startingMessage)
        } else {
            messages.add(CTWChatMessage("Olá, em que posso te ajudar?", CTWChatMessageType.PlainText, CTWChatMessageSender.Assistant))
        }
        binding.rvChatMessages.adapter = CTWChatMessagesAdapter(this, messages)
        binding.rvChatMessages.layoutManager = LinearLayoutManager(CTWApplication.appContext, LinearLayoutManager.VERTICAL ,false)
    }

    companion object {
        fun newInstance(): CTWChatFragment {
            return CTWChatFragment()
        }
    }


}