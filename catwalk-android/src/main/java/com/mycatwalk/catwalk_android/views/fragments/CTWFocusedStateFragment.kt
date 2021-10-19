package com.mycatwalk.catwalk_android.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.mycatwalk.catwalk_android.config.CTWAssistantContext
import com.mycatwalk.catwalk_android.config.CTWConfig
import com.mycatwalk.catwalk_android.databinding.FragmentCtwFocusedStateBinding
import com.mycatwalk.catwalk_android.views.activities.CTWGenieActivity

class CTWFocusedStateFragment : Fragment() {

    lateinit var binding: FragmentCtwFocusedStateBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentCtwFocusedStateBinding.inflate(layoutInflater, container, false)

        enableCustomConfig()

        binding.btnFocusedFindSimilars.setOnClickListener {
            val sku = CTWAssistantContext.focusedSKU
            if(sku != null) {
                (activity as CTWGenieActivity).findSimilarItems(sku)
            }
        }

        binding.btnFocusedCombineItem.setOnClickListener {
            val sku = CTWAssistantContext.focusedSKU
            if(sku != null) {
                (activity as CTWGenieActivity).combine(sku)
            }
        }

        binding.btnFocusedMoreSizes.setOnClickListener {
            val sku = CTWAssistantContext.focusedSKU
            if(sku != null) {
                (activity as CTWGenieActivity).availableSizes(sku)
            }
        }

        binding.btnFocusedMoreColors.setOnClickListener {
            val sku = CTWAssistantContext.focusedSKU
            if(sku != null) {
                (activity as CTWGenieActivity).availableColors(sku)
            }
        }

        return binding.root
    }

    private fun enableCustomConfig() {
        binding.tvAssistantTitle.setTextColor(CTWConfig.getMenuScreenTitleColor())
        binding.tvAssistantTitle.typeface = CTWConfig.getRegularTypeface()
        (activity as CTWGenieActivity).getAllButtons(binding.llFocusedStateOptions).forEach {
            it.setTextColor(CTWConfig.getMenuButtonFontColor())
            it.background.setTint(CTWConfig.getMenuButtonBackgroundColor())
            it.typeface = CTWConfig.getRegularTypeface()
        }
    }

    companion object {
        fun newInstance(): CTWFocusedStateFragment {
            return CTWFocusedStateFragment()
        }
    }
}