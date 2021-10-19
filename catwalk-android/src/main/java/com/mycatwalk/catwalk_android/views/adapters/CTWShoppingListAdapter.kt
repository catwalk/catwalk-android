package com.mycatwalk.catwalk_android.views.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.mycatwalk.catwalk_android.CTWApplication
import com.mycatwalk.catwalk_android.R
import com.mycatwalk.catwalk_android.config.CTWConfig
import com.mycatwalk.catwalk_android.helpers.CTWUtils
import com.mycatwalk.catwalk_android.models.CTWProduct
import com.mycatwalk.catwalk_android.views.fragments.CTWGenieShoppingListFragment




class CTWShoppingListAdapter(
    private val fragment: CTWGenieShoppingListFragment,
    private val dataSet: MutableList<CTWProduct>) :
    RecyclerView.Adapter<CTWShoppingListAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ivItemImage: ImageView = view.findViewById(R.id.iv_item_image)
        val tvItemHeadline: TextView = view.findViewById(R.id.tv_item_headline)
        val tvItemPrice: TextView = view.findViewById(R.id.tv_item_price)
        val sizesSpinner: Spinner = view.findViewById(R.id.size_spinner)
        val btnRemoveItem: ImageView = view.findViewById(R.id.btn_remove)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.ctw_shopping_item, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val item: CTWProduct = dataSet[position]
        viewHolder.tvItemHeadline.text = item.headline ?: ""
        viewHolder.tvItemPrice.text = CTWUtils.formatCurrencyFromCents((item.price?.currentPriceInCents ?: 0F))
        Glide.with(CTWApplication.appContext)
            .load(item.image)
            .transforms(CenterCrop(), RoundedCorners(16))
            .into(viewHolder.ivItemImage)
        setupSizesForItem(position, viewHolder)
        viewHolder.btnRemoveItem.setOnClickListener {
            fragment.items.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, dataSet.size)
            fragment.updateLookTotalPrice()
            if(fragment.items.size == 0) {
                fragment.dismiss()
            }
        }
    }

    private fun setupSizesForItem(position: Int, viewHolder: CTWShoppingListAdapter.ViewHolder) {
        val sizesList = dataSet[position].sizes?.mapNotNull { "Tamanho ${it.identifier}" }
        if(sizesList != null) {
//            val adapter = ArrayAdapter(CTWApplication.appContext, R.layout.item_size_spinner, sizesList)
            val adapter = object : ArrayAdapter<String?>(
                CTWApplication.appContext,
                R.layout.item_size_spinner, sizesList
            ) {
                override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                    val v = super.getView(position, convertView, parent)
                    (v as TextView).typeface = CTWConfig.getRegularTypeface()
                    return v
                }

                override fun getDropDownView(
                    position: Int,
                    convertView: View?,
                    parent: ViewGroup
                ): View {
                    val v = super.getDropDownView(position, convertView, parent)
                    (v as TextView).typeface = CTWConfig.getRegularTypeface()
                    return v
                }
            }
            viewHolder.sizesSpinner.adapter = adapter
            viewHolder.sizesSpinner.setSelection(fragment.items[position].sizes?.indexOfFirst { it.sku == fragment.items[position].chosenSKU } ?: 0)
        }

        viewHolder.tvItemHeadline.typeface = CTWConfig.getBoldTypeface()
        viewHolder.tvItemPrice.typeface = CTWConfig.getRegularTypeface()

        viewHolder.sizesSpinner.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>?, view: View, i: Int, l: Long) {
                fragment.items[position].chosenSKU = fragment.items[position].sizes?.get(i)?.sku
            }

            override fun onNothingSelected(adapterView: AdapterView<*>?) {
                return
            }
        }
    }

    override fun getItemCount() = dataSet.size

}