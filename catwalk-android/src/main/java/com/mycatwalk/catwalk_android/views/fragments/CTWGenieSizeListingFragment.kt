package com.mycatwalk.catwalk_android.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.mycatwalk.catwalk_android.CTWApplication
import com.mycatwalk.catwalk_android.databinding.FragmentCtwGenieSizeListingBinding
import com.mycatwalk.catwalk_android.models.CTWProduct
import com.mycatwalk.catwalk_android.views.adapters.CTWSizesListingAdapter

class CTWGenieSizeListingFragment : Fragment() {
    lateinit var binding: FragmentCtwGenieSizeListingBinding
    var items: Array<CTWProduct> = emptyArray()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCtwGenieSizeListingBinding.inflate(layoutInflater, container, false)
        setupRecyclerView()
        return binding.root
    }

    private fun setupRecyclerView() {
        val items = arguments?.getSerializable("ITEMS_LIST") as? Array<CTWProduct>
        if (items != null) {
            this.items = items
            binding.rvItemList.adapter = CTWSizesListingAdapter(this, items)
            binding.rvItemList.layoutManager = LinearLayoutManager(CTWApplication.appContext, LinearLayoutManager.VERTICAL ,false)
        }
    }

    companion object {
        fun newInstance(): CTWGenieSizeListingFragment {
            return CTWGenieSizeListingFragment()
        }
    }
}