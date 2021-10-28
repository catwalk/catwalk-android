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
import com.mycatwalk.catwalk_android.databinding.FragmentCtwGenieShoppingListBinding
import com.mycatwalk.catwalk_android.helpers.CTWUtils
import com.mycatwalk.catwalk_android.models.CTWProduct
import com.mycatwalk.catwalk_android.views.activities.CTWGenieActivity
import com.mycatwalk.catwalk_android.views.adapters.CTWShoppingListAdapter

class CTWGenieShoppingListFragment : Fragment() {

    private lateinit var binding: FragmentCtwGenieShoppingListBinding
    var items: MutableList<CTWProduct> = arrayListOf()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCtwGenieShoppingListBinding.inflate(layoutInflater, container, false)
        configureUI()
        return binding.root
    }

    private fun configureUI() {
        setupRecyclerView()
        setupListeners()
        updateLookTotalPrice()
        enableCustomConfig()
    }

    private fun setupListeners() {
        binding.btnBuyLook.setOnClickListener {
            CTWAssistantContext.delegate?.didReturnShoppingItems(items.mapNotNull { it.chosenSKU }.toTypedArray())
            (activity as? CTWGenieActivity)?.finishAttendance()
        }
    }

    private fun setupRecyclerView() {
        val items = arguments?.getSerializable("ITEMS_LIST") as? Array<CTWProduct>
        if (items != null) {
            this.items = items.toMutableList()
            this.items.forEach { it.chosenSKU = it.sizes?.get(0)?.sku }
            binding.rvItemList.adapter = CTWShoppingListAdapter(this, this.items)
            binding.rvItemList.layoutManager = LinearLayoutManager(CTWApplication.appContext, LinearLayoutManager.VERTICAL ,false)
        }
    }

    fun updateLookTotalPrice() {
        val itemsTotalPrice = CTWUtils.formatCurrencyFromCents(items.map { it.price?.currentPriceInCents}.fold(0F) { sum, element -> (sum + (element ?: 0F)) })
        binding.tvTotalPrice.text = itemsTotalPrice
    }

    private fun enableCustomConfig() {
        binding.btnBuyLook.background.setTint(CTWConfig.getGeneralButtonBackgroundColor())
        binding.btnBuyLook.setTextColor(CTWConfig.getGeneralButtonTextColor())
        binding.btnBuyLook.typeface = CTWConfig.getRegularTypeface()
    }

    fun dismiss() {
        activity?.supportFragmentManager?.popBackStack()
    }

    companion object {
        fun newInstance(): CTWGenieShoppingListFragment {
            return CTWGenieShoppingListFragment()
        }
    }
}