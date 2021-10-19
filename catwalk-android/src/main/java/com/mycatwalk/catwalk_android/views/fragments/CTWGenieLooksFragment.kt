package com.mycatwalk.catwalk_android.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.mycatwalk.catwalk_android.CTWApplication
import com.mycatwalk.catwalk_android.R
import com.mycatwalk.catwalk_android.config.CTWConfig
import com.mycatwalk.catwalk_android.databinding.FragmentCtwGenieLooksBinding
import com.mycatwalk.catwalk_android.helpers.CTWUtils
import com.mycatwalk.catwalk_android.models.CTWLook
import com.mycatwalk.catwalk_android.views.activities.CTWGenieActivity
import com.mycatwalk.catwalk_android.views.adapters.CTWGenieLooksAdapter


class CTWGenieLooksFragment : Fragment() {

    lateinit var binding: FragmentCtwGenieLooksBinding
    var lookTotalCount: Int = 0
    var looks: Array<CTWLook> = emptyArray()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCtwGenieLooksBinding.inflate(layoutInflater, container, false)
        setupLooksViewPager()
        setupListeners()
        enableCustomConfig()
        return binding.root
    }

    private fun enableCustomConfig() {
        binding.tvLookPrice.typeface = CTWConfig.getBoldTypeface()
        binding.tvAssistantMessage.typeface = CTWConfig.getRegularTypeface()
        binding.btnBuyLook.typeface = CTWConfig.getRegularTypeface()
        binding.btnBuyLook.background.setTint(CTWConfig.getGeneralButtonBackgroundColor())
        binding.btnBuyLook.setTextColor(CTWConfig.getGeneralButtonTextColor())
    }

    private fun setupLooksViewPager() {
        val looks = arguments?.getSerializable("LOOKS_LIST") as? Array<CTWLook>
        if (looks != null) {
            this.looks = looks

            val pager = binding.containerLookViewpager
            pager.adapter = CTWGenieLooksAdapter(this, looks)
            pager.currentItem = 0
            lookTotalCount = looks.size
            setTitle("Look 1 de $lookTotalCount")
            updateLookTotalPriceByPosition(0)

            pager.addOnPageChangeListener(object : OnPageChangeListener {
                override fun onPageScrolled(
                    position: Int,
                    positionOffset: Float,
                    positionOffsetPixels: Int
                ) {

                }

                override fun onPageSelected(position: Int) {
                    setTitle("Look ${position + 1} de $lookTotalCount")
                    updateLookTotalPriceByPosition(position)
                    setupLikeLookButton()
                }

                override fun onPageScrollStateChanged(state: Int) {
                    print("DEBUG: State $state")
                }
            })
        }

    }

    private fun setTitle(title: String) {
        (activity as CTWGenieActivity).setHeaderTitle(title)
    }

    private fun setupListeners() {
        binding.btnBuyLook.setOnClickListener {
            val lookItems = looks[binding.containerLookViewpager.currentItem].items
            if (lookItems != null) {
                (activity as CTWGenieActivity).buyLook(lookItems.toTypedArray())
            }

        }

        binding.btnLikeLook.setOnClickListener {
            if(looks[binding.containerLookViewpager.currentItem].likedLook == true) {
                unlikeLook()
            } else {
                likeLook()
            }
        }
    }

    private fun updateLookTotalPriceByPosition(position: Int) {
        val lookTotalPrice = CTWUtils.formatCurrencyFromCents(looks[position].items?.map { it.product?.price?.currentPriceInCents}?.fold(0) {sum, element -> (sum + (element?.toInt() ?: 0)) }?.toFloat() ?: 0.toFloat())
        binding.tvLookPrice.text = lookTotalPrice
    }

    private fun setupLikeLookButton() {
        if(looks[binding.containerLookViewpager.currentItem].likedLook == true) {
            binding.btnLikeLook.setColorFilter(CTWConfig.getMenuScreenBackgroundColor())
        } else {
            binding.btnLikeLook.setColorFilter(ContextCompat.getColor(CTWApplication.appContext, R.color.black))
        }
    }


    private fun likeLook() {
        binding.btnLikeLook.setColorFilter(CTWConfig.getMenuScreenBackgroundColor())
        looks[binding.containerLookViewpager.currentItem].likedLook = true
    }

    private fun unlikeLook() {
        binding.btnLikeLook.setColorFilter(ContextCompat.getColor(CTWApplication.appContext, R.color.black))
        looks[binding.containerLookViewpager.currentItem].likedLook = false
    }

    companion object {
        fun newInstance(): CTWGenieLooksFragment {
            return CTWGenieLooksFragment()
        }
    }
}
