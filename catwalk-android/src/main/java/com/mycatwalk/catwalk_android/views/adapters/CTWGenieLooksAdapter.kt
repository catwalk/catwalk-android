package com.mycatwalk.catwalk_android.views.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.mycatwalk.catwalk_android.CTWApplication
import com.mycatwalk.catwalk_android.config.CTWAssistantContext
import com.mycatwalk.catwalk_android.databinding.CtwLookLayoutBinding
import com.mycatwalk.catwalk_android.models.CTWLook
import com.mycatwalk.catwalk_android.models.CTWLookItem
import com.mycatwalk.catwalk_android.views.fragments.CTWGenieLooksFragment

class CTWGenieLooksAdapter(private val fragment: CTWGenieLooksFragment, private val lookSet: Array<CTWLook>) :
    PagerAdapter() {
    lateinit var binding: CtwLookLayoutBinding
    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        binding = CtwLookLayoutBinding.inflate(LayoutInflater.from(CTWApplication.appContext), container, false)
        binding.root.tag = position
        val look = lookSet[position]
        look.items?.forEach {
            createLookItemView(it)
        }
        container.addView(binding.root)
        return binding.root
    }

    private fun createLookItemView(item: CTWLookItem) {
        val imageView = ImageView(CTWApplication.appContext)
        imageView.setOnClickListener {
            val sku = item.product?.sku
            if(sku != null) {
                CTWAssistantContext.delegate?.didReturnSingleItem(sku)
                fragment.activity?.finish()
            }
        }
        val layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0)
        layoutParams.weight = item.weight ?: 0F
        layoutParams.setMargins(16, 16, 16, 16)
        imageView.layoutParams = layoutParams
        Glide.with(CTWApplication.appContext)
            .load(item.product?.image)
            .transforms(CenterCrop(), RoundedCorners(16))
            .into(imageView)

        if (item.column == 1) {
            binding.firstColumn.addView(imageView)
        } else if (item.column == 2) {
            binding.secondColumn.addView(imageView)
        }
    }

    override fun destroyItem(collection: ViewGroup, position: Int, view: Any) {
        collection.removeView(view as View)
    }

    override fun getCount(): Int {
        return lookSet.size
    }

}

