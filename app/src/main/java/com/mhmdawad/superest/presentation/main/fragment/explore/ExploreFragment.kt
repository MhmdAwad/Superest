package com.mhmdawad.superest.presentation.main.fragment.explore

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.mhmdawad.superest.R
import com.mhmdawad.superest.databinding.FragmentExploreBinding
import com.mhmdawad.superest.model.CategoryItem
import com.mhmdawad.superest.model.MainShopItem
import com.mhmdawad.superest.model.ProductModel
import com.mhmdawad.superest.presentation.main.adapter.ExploreCategoryAdapter
import com.mhmdawad.superest.util.LOADING_ANNOTATION
import com.mhmdawad.superest.util.Resource
import com.mhmdawad.superest.util.extention.searchListener
import com.mhmdawad.superest.util.extention.showToast
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import javax.inject.Named

@AndroidEntryPoint
class ExploreFragment : Fragment(), ExploreCategoryAdapter.CategoryListener {

    private lateinit var binding: FragmentExploreBinding
    private val exploreViewModel by viewModels<ExploreViewModel>()
    private var searchedText: String = ""

    @Inject
    lateinit var exploreCategoryAdapter: ExploreCategoryAdapter

    @Inject
    @Named(LOADING_ANNOTATION)
    lateinit var loadingDialog: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        exploreViewModel.getCategoryList()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_explore, container, false)
        binding.exploreAdapter = exploreCategoryAdapter
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeListener()
        initViews()
    }

    private fun initViews() {
        binding.apply {
            searchProductEditText.searchListener {
                val search = searchProductEditText.text.toString().trim()
                if (search.isNotEmpty()) {
                    searchedText = search
                    exploreViewModel.getProductsHasContainName(searchedText)
                    searchProductEditText.text.clear()
                }
            }
        }
    }

    private fun observeListener() {
        exploreViewModel.categoryLiveData.observe(viewLifecycleOwner, { categories ->
            when (categories) {
                is Resource.Success -> {
                    exploreCategoryAdapter.addCategoryItems(categories.data!!, this)
                    loadingDialog.hide()
                }
                is Resource.Error -> {
                    showToast(categories.msg!!)
                    loadingDialog.hide()
                }
                is Resource.Loading -> loadingDialog.show()
            }
        })

        exploreViewModel.searchedProductsLiveData.observe(viewLifecycleOwner, { products ->
            when (products) {
                is Resource.Success -> {
                    val shopItem = MainShopItem(
                        "", searchedText, 0, false,
                        products.data as MutableList<ProductModel>
                    )
                    navigateToAllProducts(shopItem)
                    loadingDialog.hide()
                }
                is Resource.Loading -> loadingDialog.show()
                is Resource.Error -> {
                    showToast(products.msg!!)
                    loadingDialog.hide()
                }
            }
        })

        exploreViewModel.categoryProductsLiveData.observe(viewLifecycleOwner, { mainShopItem ->
            when (mainShopItem) {
                is Resource.Success -> {
                    navigateToAllProducts(mainShopItem.data!!)
                    loadingDialog.hide()
                }
                is Resource.Loading -> loadingDialog.show()
                is Resource.Error -> {
                    showToast(mainShopItem.msg!!)
                    loadingDialog.hide()
                }
            }
        })
    }

    private fun navigateToAllProducts(item: MainShopItem) {
        val action = ExploreFragmentDirections.actionExploreFragmentToAllProductsFragment(item)
        findNavController().navigate(action)

    }

    override fun onCategoryClick(categoryItem: CategoryItem) {
        exploreViewModel.getProductsByCategoryId(categoryItem.id)
    }
}