package com.mhmdawad.superest.presentation.main.fragment.shop

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.mhmdawad.superest.R
import com.mhmdawad.superest.databinding.FragmentShopBinding
import com.mhmdawad.superest.model.MainShopItem
import com.mhmdawad.superest.model.OffersModel
import com.mhmdawad.superest.model.ProductModel
import com.mhmdawad.superest.model.UserInfoModel
import com.mhmdawad.superest.presentation.MainActivity
import com.mhmdawad.superest.presentation.main.adapter.ImageSliderAdapter
import com.mhmdawad.superest.presentation.main.adapter.ShopProductAdapter
import com.mhmdawad.superest.presentation.main.adapter.ProductItemsAdapter
import com.mhmdawad.superest.util.LOADING_ANNOTATION
import com.mhmdawad.superest.util.Resource
import com.mhmdawad.superest.util.extention.*
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import javax.inject.Named


@AndroidEntryPoint
class ShopFragment : Fragment(), ShopProductAdapter.MainProductListener,
    ProductItemsAdapter.ProductListener,
    ImageSliderAdapter.OfferListener {

    private val shopViewModel by activityViewModels<ShopViewModel>()

    private val shopAdapter by lazy { ShopProductAdapter(this, this, this) }

    private val bottomNavigationView by lazy {
        (activity as MainActivity).findViewById<BottomNavigationView>(
            R.id.bottomNavigationView
        )
    }

    @Inject
    @Named(LOADING_ANNOTATION)
    lateinit var loadingDialog: Dialog

    private lateinit var binding: FragmentShopBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_shop, container, false
        )
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        shopViewModel.getUserInfo()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).showBottomNav()
        observeListener()
    }


    private fun observeListener() {
        shopViewModel.userInfoLiveData.observe(viewLifecycleOwner, { userInfo ->
            when (userInfo) {
                is Resource.Success -> {
                    initViews(userInfo.data)
                    shopViewModel.getShopList()
                }
                is Resource.Error -> {
                    loadingDialog.hide()
                    navigateToCreateUserInfoFragment()
                }
                is Resource.Loading -> {
                    loadingDialog.show()
                }
            }
        })

        shopViewModel.shopListLiveData.observe(viewLifecycleOwner, { shopList ->
            when (shopList) {
                is Resource.Success -> {
                    shopAdapter.addMainShopListItems(shopList.data)
                    loadingDialog.hide()
                }
                is Resource.Error -> {
                    showToast(shopList.msg!!)
                    loadingDialog.hide()
                }
            }
        })

        shopViewModel.offersListLiveData.observe(viewLifecycleOwner, { offersList ->
            if (offersList is Resource.Success)
                shopAdapter.addOffersListItems(offersList.data)
        })

        shopViewModel.categoryLiveData.observe(viewLifecycleOwner, {category->
            when(category){
                is Resource.Success->{
                    loadingDialog.hide()
                    shopViewModel.changeCategoryLiveData()
                    navigateToAllProductsFragment(category.data!!)
                }
                is Resource.Loading-> loadingDialog.show()

                is Resource.Error->{
                    loadingDialog.hide()
                }
            }
        })
    }

    private fun initViews(data: UserInfoModel?) {
        if (data == null) return
        binding.apply {
            userNameTextView.text = data.userName
            userImageImageView.loadImage(data.userImage)
            shopRV.adapter = shopAdapter
            shopSearchEditText.searchListener {
                val searchText = shopSearchEditText.text.toString()
                if (isTextNotEmpty(searchText)) {
                    showToast(searchText)
                }
            }
            shopContainer.show()
            shopContainer.transitionBottomNavigationBar({
                bottomNavigationView.animate().scaleY(it)
            }, {
                if (bottomNavigationView.isShown)
                    bottomNavigationView.hide()
                else {
                    showBottomNavigationView()
                }
            })
        }
    }

    override fun onResume() {
        super.onResume()
        showBottomNavigationView()
    }

    private fun showBottomNavigationView() {
        bottomNavigationView.animate().scaleY(1f)
        bottomNavigationView.show()
    }

    private fun navigateToCreateUserInfoFragment() {
        val action = ShopFragmentDirections.actionMainFragmentToCreateUserInfoFragment()
        findNavController().navigate(action)
    }

    override fun onSeeAllClicked(mainShopItem: MainShopItem) {
        navigateToAllProductsFragment(mainShopItem)
    }

    private fun navigateToAllProductsFragment(mainShopItem: MainShopItem) {
        val action = ShopFragmentDirections.actionShopFragmentToAllProductsFragment(mainShopItem)
        findNavController().navigate(action)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (loadingDialog.isShowing)
            loadingDialog.hide()
    }

    override fun onProductClick(productModel: ProductModel, transitionImageView: ImageView) {
        val extras = FragmentNavigatorExtras(
            transitionImageView to productModel.image
        )
        val action =
            ShopFragmentDirections.actionShopFragmentToSpecificProductFragment(productModel)
        findNavController().navigate(action, extras)

    }

    override fun onOfferClicked(offersModel: OffersModel) {
        shopViewModel.getSpecificCategoryProducts(offersModel.offerId)
    }
}