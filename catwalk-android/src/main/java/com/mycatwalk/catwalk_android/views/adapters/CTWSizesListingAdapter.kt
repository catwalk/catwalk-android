package com.mycatwalk.catwalk_android.views.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.mycatwalk.catwalk_android.CTWApplication
import com.mycatwalk.catwalk_android.R
import com.mycatwalk.catwalk_android.config.CTWAssistantContext
import com.mycatwalk.catwalk_android.config.CTWConfig
import com.mycatwalk.catwalk_android.helpers.CTWUtils
import com.mycatwalk.catwalk_android.models.CTWProduct

class CTWSizesListingAdapter(private val fragment: Fragment, private val dataSet: Array<CTWProduct>) :
    RecyclerView.Adapter<CTWSizesListingAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ivItemImage: ImageView = view.findViewById(R.id.iv_item_image)
        val tvItemHeadline: TextView = view.findViewById(R.id.tv_item_headline)
        val tvItemPrice: TextView = view.findViewById(R.id.tv_item_price)
        val tvItemSize: TextView = view.findViewById(R.id.tv_item_size)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.ctw_listing_item, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val item: CTWProduct = dataSet[position]
        viewHolder.tvItemSize.visibility = View.VISIBLE
        viewHolder.tvItemHeadline.text = item.headline ?: ""
        viewHolder.tvItemPrice.text = CTWUtils.formatCurrencyFromCents((item.price?.currentPriceInCents ?: 0F))
        viewHolder.tvItemSize
        Glide.with(CTWApplication.appContext)
            .load(item.image)
            .transforms(CenterCrop(), RoundedCorners(16))
            .into(viewHolder.ivItemImage)
        viewHolder.itemView.setOnClickListener {
            sendItemToShoppingCart(position)
        }

        viewHolder.tvItemHeadline.typeface = CTWConfig.getBoldTypeface()
        viewHolder.tvItemPrice.typeface = CTWConfig.getRegularTypeface()
        viewHolder.tvItemSize.typeface = CTWConfig.getRegularTypeface()

        viewHolder.tvItemSize.text = dataSet[position].sizes?.mapNotNull { "Tamanho ${it.identifier}" }?.get(0) ?: ""
    }

    private fun sendItemToShoppingCart(position: Int) {
        val sku = dataSet[position].sizes?.get(0)?.sku
        if (sku != null) {
            CTWAssistantContext.delegate?.didReturnShoppingItems(arrayOf(sku))
            fragment.activity?.finish()
        }
    }

    override fun getItemCount() = dataSet.size

}