package com.mycatwalk.catwalk_android.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.mycatwalk.catwalk_android.databinding.FragmentCtwCreateLookColorOptionsBinding
import com.mycatwalk.catwalk_android.views.activities.CTWGenieActivity

class CTWCreateLookColorOptionsFragment : Fragment() {

    lateinit var binding: FragmentCtwCreateLookColorOptionsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCtwCreateLookColorOptionsBinding.inflate(layoutInflater, container, false)

        binding.btnBlackLooks.setOnClickListener {
            (activity as CTWGenieActivity).looksByColor(1)
        }

        binding.btnWhiteLooks.setOnClickListener {
            (activity as CTWGenieActivity).looksByColor(2)
        }

        binding.btnBlueLooks.setOnClickListener {
            (activity as CTWGenieActivity).looksByColor(3)
        }

        binding.btnGreenLooks.setOnClickListener {
            (activity as CTWGenieActivity).looksByColor(4)
        }

        binding.btnTerrainLooks.setOnClickListener {
            (activity as CTWGenieActivity).looksByColor(5)
        }

        binding.btnGrayLooks.setOnClickListener {
            (activity as CTWGenieActivity).looksByColor(6)
        }

        return binding.root
    }

    companion object {
        fun newInstance(): CTWCreateLookColorOptionsFragment {
            return CTWCreateLookColorOptionsFragment()
        }
    }
}