package com.mycatwalk.catwalk_android.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.mycatwalk.catwalk_android.config.CTWConfig
import com.mycatwalk.catwalk_android.databinding.FragmentCtwOfflineStateBinding
import com.mycatwalk.catwalk_android.views.activities.CTWGenieActivity

class CTWOfflineStateFragment : Fragment() {

    lateinit var binding: FragmentCtwOfflineStateBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCtwOfflineStateBinding.inflate(layoutInflater, container, false)
        binding.btnOfflineStateConfirmation.setOnClickListener {
            (activity as CTWGenieActivity).finish()
        }
        enableCustomConfig()
        return binding.root
    }

    private fun enableCustomConfig() {
        binding.tvAssistantTitle.setTextColor(CTWConfig.getMenuScreenTitleColor())
        binding.tvAssistantTitle.typeface = CTWConfig.getRegularTypeface()
        (activity as CTWGenieActivity).getAllButtons(binding.llOfflineStateOptions).forEach {
            it.setTextColor(CTWConfig.getMenuButtonFontColor())
            it.background.setTint(CTWConfig.getMenuButtonBackgroundColor())
            it.typeface = CTWConfig.getRegularTypeface()
        }
    }

    companion object {
        fun newInstance(): CTWOfflineStateFragment {
            return CTWOfflineStateFragment()
        }
    }
}