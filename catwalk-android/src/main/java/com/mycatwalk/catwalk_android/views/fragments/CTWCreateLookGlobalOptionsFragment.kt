package com.mycatwalk.catwalk_android.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.mycatwalk.catwalk_android.R
import com.mycatwalk.catwalk_android.config.CTWConfig
import com.mycatwalk.catwalk_android.databinding.FragmentCtwCreateLookGlobalOptionsBinding
import com.mycatwalk.catwalk_android.helpers.CTWUtils.Companion.addFragment
import com.mycatwalk.catwalk_android.views.activities.CTWGenieActivity

class CTWCreateLookGlobalOptionsFragment : Fragment() {

    lateinit var binding: FragmentCtwCreateLookGlobalOptionsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCtwCreateLookGlobalOptionsBinding.inflate(layoutInflater, container, false)

        enableCustomConfig()

        binding.btnLookWithTrendingItems.setOnClickListener {
            (activity as CTWGenieActivity).trendingLooks()
        }

        binding.btnLookByColor.setOnClickListener {
            (activity as CTWGenieActivity).addFragment(CTWCreateLookColorOptionsFragment.newInstance(), R.id.fragments_container)
        }

        return binding.root
    }

    private fun enableCustomConfig() {
        (activity as CTWGenieActivity).getAllButtons(binding.llCreateLookOptions).forEach {
            it.setTextColor(CTWConfig.getMenuButtonFontColor())
            it.background.setTint(CTWConfig.getMenuButtonBackgroundColor())
            it.typeface = CTWConfig.getRegularTypeface()
        }
    }

    companion object {
        fun newInstance(): CTWCreateLookGlobalOptionsFragment {
            return CTWCreateLookGlobalOptionsFragment()
        }
    }
}