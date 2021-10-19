package com.mycatwalk.catwalk_android.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.mycatwalk.catwalk_android.config.CTWConfig
import com.mycatwalk.catwalk_android.R
import com.mycatwalk.catwalk_android.databinding.FragmentCtwGlobalStateBinding
import com.mycatwalk.catwalk_android.helpers.CTWUtils.Companion.addFragment
import com.mycatwalk.catwalk_android.views.activities.CTWGenieActivity

class CTWGlobalStateFragment : Fragment() {

    lateinit var binding: FragmentCtwGlobalStateBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCtwGlobalStateBinding.inflate(layoutInflater, container, false)
        enableCustomConfig()

        binding.btnGlobalSeeTrendingItems.setOnClickListener {
            (activity as CTWGenieActivity).trendingItems()
        }

        binding.btnGlobalCreateLook.setOnClickListener {
            (activity as CTWGenieActivity).addFragment(CTWCreateLookGlobalOptionsFragment.newInstance(), R.id.fragments_container)
        }

        return binding.root
    }

    private fun enableCustomConfig() {
        binding.tvAssistantTitle.setTextColor(CTWConfig.getMenuButtonBackgroundColor())
        binding.tvAssistantTitle.typeface = CTWConfig.getRegularTypeface()
        (activity as CTWGenieActivity).getAllButtons(binding.llGlobalStateOptions).forEach {
            it.setTextColor(CTWConfig.getMenuButtonFontColor())
            it.background.setTint(CTWConfig.getMenuButtonBackgroundColor())
            it.typeface = CTWConfig.getRegularTypeface()
        }
    }

    companion object {
        fun newInstance(): CTWGlobalStateFragment {
            return CTWGlobalStateFragment()
        }
    }
}