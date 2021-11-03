package com.mycatwalk.catwalk_android.views.activities

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.mycatwalk.catwalk_android.R
import com.mycatwalk.catwalk_android.config.CTWAssistantContext
import com.mycatwalk.catwalk_android.config.CTWConfig
import com.mycatwalk.catwalk_android.databinding.ActivityCtwgenieBinding
import com.mycatwalk.catwalk_android.helpers.CTWUtils
import com.mycatwalk.catwalk_android.helpers.CTWUtils.Companion.addFragment
import com.mycatwalk.catwalk_android.helpers.CTWUtils.Companion.replaceFragment
import com.mycatwalk.catwalk_android.models.CTWLookItem
import com.mycatwalk.catwalk_android.models.CTWProduct
import com.mycatwalk.catwalk_android.networking.CTWNetworkManager
import com.mycatwalk.catwalk_android.views.dialogs.CTWInfoDialog
import com.mycatwalk.catwalk_android.views.dialogs.CTWLoadingDialog
import com.mycatwalk.catwalk_android.views.fragments.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class CTWGenieActivity : AppCompatActivity() {

    lateinit var binding: ActivityCtwgenieBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCtwgenieBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupSession()
        configureUI()
        setupListeners()
    }

    private fun configureUI() {
        when {
            CTWAssistantContext.offlineState -> {
                replaceFragment(CTWOfflineStateFragment.newInstance(), R.id.fragments_container)
            }
            CTWAssistantContext.focusedSKU != null -> {
                replaceFragment(CTWFocusedStateFragment.newInstance(), R.id.fragments_container)
            }
            else -> {
                replaceFragment(CTWGlobalStateFragment.newInstance(), R.id.fragments_container)
            }
        }

        enableCustomConfig()

        binding.rootLayout.setBackgroundColor(CTWUtils.getColor(R.color.menuScreenBackgroundColor))
    }

    private fun setupSession() {
        CoroutineScope(Dispatchers.IO).launch {
            val sessionInfo = CTWNetworkManager.fetchSessionInfo()
            CTWConfig.sessionId = sessionInfo.id
        }
    }

    private fun enableCustomConfig() {
        binding.header.tvHeaderTitle.setTextColor(CTWConfig.getMenuButtonBackgroundColor())
        binding.header.tvHeaderTitle.typeface = CTWConfig.getBoldTypeface()
    }

    private fun setupListeners() {
        supportFragmentManager.addOnBackStackChangedListener {
            binding.header.btnBack.visibility = View.VISIBLE
            binding.header.btnClose.visibility = View.VISIBLE

            val fragment = supportFragmentManager.findFragmentById(R.id.fragments_container)
            if (fragment is CTWGenieLooksFragment) {
                setHeaderTitle("Look ${fragment.binding.containerLookViewpager.currentItem + 1} de ${fragment.lookTotalCount}")
                setLightMode()
            } else if (fragment is CTWGenieItemListingFragment) {
                setHeaderTitle("Suas sugestões")
                setLightMode()
            } else if (fragment is CTWGenieSizeListingFragment) {
                setHeaderTitle("Escolha seu tamanho")
                setLightMode()
            } else if (fragment is CTWGenieShoppingListFragment) {
                setHeaderTitle("Escolha seus tamanhos")
                setLightMode()
            } else if (fragment is CTWChatFragment) {
                setHeaderTitle("Converse comigo")
                setDarkMode()
            } else if (fragment is CTWCreateLookGlobalOptionsFragment || fragment is CTWCreateLookColorOptionsFragment) {
                setHeaderTitle("Montar um look")
                setDarkMode()
            } else if (fragment is CTWFocusedStateFragment || fragment is CTWGlobalStateFragment || fragment is CTWOfflineStateFragment) {
                setHeaderTitle("")
                setDarkMode()
                binding.header.btnBack.visibility = View.GONE
            }
        }

        binding.header.btnBack.setOnClickListener {
            supportFragmentManager.popBackStack()
        }

        binding.header.btnClose.setOnClickListener {
            finishAttendance()
        }
    }

    fun getAllButtons(layout: ViewGroup): List<Button> {
        val btn: MutableList<Button> = ArrayList()
        for (i in 0 until layout.childCount) {
            val v = layout.getChildAt(i)
            if (v is Button) {
                btn.add(v as Button)
            }
        }
        return btn
    }

    private fun setDarkMode() {
        binding.rootLayout.setBackgroundColor(CTWConfig.getMenuScreenBackgroundColor())
        val headerTitle = binding.header.tvHeaderTitle
        headerTitle.setTextColor(CTWUtils.getColor(R.color.menuScreenTitleColor))
        val backButton = binding.header.btnBack
        backButton.setColorFilter(CTWConfig.getMenuButtonBackgroundColor())
        val closeButton = binding.header.btnClose
        closeButton.setColorFilter(CTWConfig.getMenuButtonBackgroundColor())
    }

    private fun setLightMode() {
        binding.rootLayout.setBackgroundColor(CTWUtils.getColor(R.color.white))
        val headerTitle = binding.header.tvHeaderTitle
        headerTitle.setTextColor(CTWUtils.getColor(R.color.black))
        val backButton = binding.header.btnBack
        backButton.setColorFilter(CTWUtils.getColor(R.color.black))
        val closeButton = binding.header.btnClose
        closeButton.setColorFilter(CTWUtils.getColor(R.color.black))
    }

    fun setHeaderTitle(title: String) {
        val headerTitle = binding.header.tvHeaderTitle
        headerTitle.text = title
    }

    fun findSimilarItems(sku: String) {
        if (!CTWLoadingDialog.isLoading()) {
            openLoader()
            CoroutineScope(Dispatchers.IO).launch {
                val similarSKUS = CTWNetworkManager.fetchSimilarItems(sku)
                withContext(Dispatchers.Default) {
                    val items = CTWNetworkManager.fetchProductsInfoFromSKUS(similarSKUS)
                    withContext(Dispatchers.Main) {
                        if(items.isNotEmpty()) {
                            val bundle = Bundle()
                            bundle.putSerializable("ITEMS_LIST", items)
                            val fragment = CTWGenieItemListingFragment.newInstance()
                            fragment.arguments = bundle
                            closeLoader()
                            addFragment(fragment, R.id.fragments_container)

                        } else {
                            showErrorDialog("Oops... não encontrei itens similares para esta peça!")
                        }
                    }
                }
            }
        }
    }

    fun availableColors(sku: String) {
        if (!CTWLoadingDialog.isLoading()) {
            openLoader()
            CoroutineScope(Dispatchers.IO).launch {
                val availableColorsSKUS = CTWNetworkManager.availableColors(sku)
                withContext(Dispatchers.Default) {
                    val items = CTWNetworkManager.fetchProductsInfoFromSKUS(availableColorsSKUS)
                    withContext(Dispatchers.Main) {
                        if(items.isNotEmpty()) {
                            val bundle = Bundle()
                            bundle.putSerializable("ITEMS_LIST", items)
                            val fragment = CTWGenieItemListingFragment.newInstance()
                            fragment.arguments = bundle
                            closeLoader()
                            addFragment(fragment, R.id.fragments_container)
                        } else {
                            showErrorDialog("Oops... não encontrei outras cores para esta peça!")
                        }
                    }
                }
            }
        }
    }

    fun availableSizes(sku: String) {
        if (!CTWLoadingDialog.isLoading()) {
            openLoader()
            CoroutineScope(Dispatchers.IO).launch {
                val item = CTWNetworkManager.fetchProductsInfoBySKU(sku)
                withContext(Dispatchers.Main) {
                    val sizesAsItems = item?.sizes?.filter { it.available == true}?.map {
                        CTWProduct(item.headline, item.productId, item.image, it.sku, item.price, listOf(it), it.sku)
                    }

                    if(sizesAsItems != null && sizesAsItems.isNotEmpty()) {
                        val bundle = Bundle()
                        bundle.putSerializable("ITEMS_LIST", sizesAsItems.toTypedArray())
                        val fragment = CTWGenieSizeListingFragment.newInstance()
                        fragment.arguments = bundle
                        closeLoader()
                        addFragment(fragment, R.id.fragments_container)
                    } else {
                        showErrorDialog("Oops... não encontrei mais tamanhos para esta peça!")
                    }
                }
            }
        }
    }

    fun combine(sku: String) {
        if (!CTWLoadingDialog.isLoading()) {
            openLoader()
            CoroutineScope(Dispatchers.IO).launch {
                val looks = CTWNetworkManager.fetchLooks(sku)
                withContext(Dispatchers.Main) {
                    if (looks.isNotEmpty()) {
                        val bundle = Bundle()
                        bundle.putSerializable("LOOKS_LIST", looks)
                        val fragment = CTWGenieLooksFragment.newInstance()
                        fragment.arguments = bundle
                        closeLoader()
                        addFragment(fragment, R.id.fragments_container)
                    } else {
                        showErrorDialog("Oops... não encontrei combinações!")
                    }

                }
            }
        }
    }

    fun buyLook(lookItems: Array<CTWLookItem>) {
        if (!CTWLoadingDialog.isLoading()) {
            openLoader()
            val itemsProductIds = lookItems.mapNotNull { it.product?.productId }
            CoroutineScope(Dispatchers.IO).launch {
                val items = CTWNetworkManager.fetchProductsInfoFromProductIds(itemsProductIds.toTypedArray())
                withContext(Dispatchers.Main) {
                    val bundle = Bundle()
                    bundle.putSerializable("ITEMS_LIST", items)
                    val fragment = CTWGenieShoppingListFragment.newInstance()
                    fragment.arguments = bundle
                    closeLoader()
                    addFragment(fragment, R.id.fragments_container)
                }
            }
        }
    }

    fun trendingItems() {
        if (!CTWLoadingDialog.isLoading()) {
            openLoader()
            CoroutineScope(Dispatchers.IO).launch {
                val trendingSKUS = CTWNetworkManager.fetchTrendingSKUs()
                withContext(Dispatchers.Default) {
                    val items = CTWNetworkManager.fetchProductsInfoFromSKUS(trendingSKUS)
                    withContext(Dispatchers.Main) {
                        if(items.isNotEmpty()) {
                            val bundle = Bundle()
                            bundle.putSerializable("ITEMS_LIST", items)
                            val fragment = CTWGenieItemListingFragment.newInstance()
                            fragment.arguments = bundle
                            closeLoader()
                            addFragment(fragment, R.id.fragments_container)
                        } else {
                            showErrorDialog("Oops... não encontrei peças em alta!")
                        }
                    }
                }
            }
        }
    }

    fun trendingLooks() {
        if (!CTWLoadingDialog.isLoading()) {
            openLoader()
            CoroutineScope(Dispatchers.IO).launch {
                val looks = CTWNetworkManager.fetchTrendingClothingAsLooks()
                withContext(Dispatchers.Main) {
                    if (looks.isNotEmpty()) {
                        val bundle = Bundle()
                        bundle.putSerializable("LOOKS_LIST", looks)
                        val fragment = CTWGenieLooksFragment.newInstance()
                        fragment.arguments = bundle
                        closeLoader()
                        addFragment(fragment, R.id.fragments_container)
                    } else {
                        showErrorDialog("Oops... não encontrei combinações!")
                    }

                }
            }
        }
    }

    fun looksByColor(hueId: Int) {
        if (!CTWLoadingDialog.isLoading()) {
            openLoader()
            CoroutineScope(Dispatchers.IO).launch {
                val looks = CTWNetworkManager.fetchCombinationsByHue(hueId)
                withContext(Dispatchers.Main) {
                    if (looks.isNotEmpty()) {
                        val bundle = Bundle()
                        bundle.putSerializable("LOOKS_LIST", looks)
                        val fragment = CTWGenieLooksFragment.newInstance()
                        fragment.arguments = bundle
                        closeLoader()
                        addFragment(fragment, R.id.fragments_container)
                    } else {
                        showErrorDialog("Oops... não encontrei combinações!")
                    }

                }
            }
        }
    }

    fun sendAttendanceReview(positive: Boolean) {
        if (!CTWLoadingDialog.isLoading()) {
            openLoader()
            CoroutineScope(Dispatchers.IO).launch {
                CTWNetworkManager.sendAttendanceReview(positive)
                closeLoader()
                finishAttendance()
            }
        }
    }

    fun openInfoDialog(text: String?) {
        CTWInfoDialog.displayLoadingWithText(this, true, CTWConfig.defaultErrorMessage)
    }


    fun openLoader(text: String? = null) {
        CTWLoadingDialog.displayLoadingWithText(this, false)
    }

    fun closeLoader() {
        CTWLoadingDialog.hideLoading()
    }

    fun showErrorDialog(text: String? = null) {
        closeLoader()
        openInfoDialog(text)
    }



    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 1) {
            supportFragmentManager.popBackStack()
        } else {
            finishAttendance()
        }
    }

    fun finishAttendance() {
        CoroutineScope(Dispatchers.IO).launch {
            withContext(Dispatchers.Main) {
                finish()
            }
            CTWNetworkManager.endSession()
        }

    }
}

fun View.hideKeyboard() {
    val inputManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputManager.hideSoftInputFromWindow(windowToken, 0)
}