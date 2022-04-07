package com.mhmdawad.superest.data.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.mhmdawad.superest.R
import com.mhmdawad.superest.data.database.FavoriteDao
import com.mhmdawad.superest.model.*
import com.mhmdawad.superest.util.*
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@ViewModelScoped
class ShopRepository
@Inject
constructor(
    private val fireStore: FirebaseFirestore,
    private val firebaseAuth: FirebaseAuth,
    private val favoriteDao: FavoriteDao,
    @ApplicationContext private val context: Context
) {

    private val errorMessage by lazy { context.getString(R.string.errorMessage) }
    private val userUid by lazy { firebaseAuth.uid!! }
    private val cartCollection by lazy {
        fireStore.collection(USERS_COLLECTION).document(userUid).collection(CART_COLLECTION)
    }

    // check if user has data into firebase firestore or not.
    suspend fun getUserInformation(userInfoLiveData: MutableLiveData<Resource<UserInfoModel>>) {
        try {
            val task = fireStore.collection(USERS_COLLECTION).document(userUid).get().await()
            val userInfoModel = convertMapToUserInfoModel(task.data!!)
            userInfoLiveData.postValue(Resource.Success(userInfoModel))
        } catch (e: Exception) {
            userInfoLiveData.postValue(Resource.Error(errorMessage))
        }
    }

    // get all products main shop from firebase.
    suspend fun getMainShopList(): Resource<List<MainShopItem>> {
        return try {
            val resultList = fireStore.collection(SHOP_LIST).get().await()
            val shopList = convertDocumentListToMainShopList(resultList.documents)
            for (shop in shopList) {
                shop.list.addAll(getProductsBySavedShopList(shop))
            }
            val categoryResult = getCategoryProductsList()
            if (categoryResult.isNotEmpty())
                shopList.addAll(categoryResult)
            Resource.Success(shopList)
        } catch (e: Exception) {
            Resource.Error(msg = errorMessage)
        }
    }

    // get all products by categories from firebase.
    private suspend fun getCategoryProductsList(): List<MainShopItem> {
        return try {
            val result = fireStore.collection(CATEGORY).get().await()
            val categoryList = convertDocumentsToCategoryList(result.documents)
            getProductsByCategory(categoryList)
        } catch (e: Exception) {
            emptyList()
        }
    }

    // get all categories from firebase.
    suspend fun getCategoryList(): Resource<List<CategoryItem>> {
        return try {
            val result = fireStore.collection(CATEGORY).get().await()
            Resource.Success(convertDocumentsToCategoryList(result.documents))
        } catch (e: Exception) {
            Resource.Error(errorMessage)
        }
    }

    // get all products that contain to category item id.
    private suspend fun getProductsByCategory(categoryList: List<CategoryItem>): List<MainShopItem> {
        val list = mutableListOf<MainShopItem>()
        categoryList.forEach {
            val products =
                fireStore.collection(PRODUCTS).whereEqualTo(CATEGORY.lowercase(), it.id).get()
                    .await()
            list.add(it.convertToMainShopItem(convertDocumentToProductList(products.documents)))
        }
        return list
    }

    // get all products that contain to main shop item id.
    private suspend fun getProductsBySavedShopList(shop: MainShopItem): List<ProductModel> {
        val productsList = mutableListOf<ProductModel>()
        val result =
            fireStore.collection(SHOP_LIST).document(shop.id).collection(ITEMS).get().await()
        val productIdList = mutableListOf<String>()
        result.documents.forEach { doc ->
            productIdList.add(doc.id)
        }
        productIdList.forEach { id ->
            val product = fireStore.collection(PRODUCTS).document(id).get().await()
            productsList.add(convertMapToProductModel(product.data!!))
        }
        return productsList
    }

    // get all offer data to show it into header of recyclerview.
    suspend fun getOffersData(): Resource<List<OffersModel>> {
        return try {
            val result = fireStore.collection(OFFERS).get().await()
            Resource.Success(convertDocumentsToOfferList(result.documents))
        } catch (e: Exception) {
            Resource.Error(errorMessage)
        }
    }

    // change product in database (save or remove) by check if it saved before or not.
    suspend fun saveOrRemoveProductFromFavorite(productModel: ProductModel) {
        val isSavedBefore = getProductFromFavorite(productModel.id)
        return if (isSavedBefore) {
            favoriteDao.removeProductFromFavorites(productModel)
        } else {
            favoriteDao.saveProduct(productModel)
        }
    }

    // check if product is saved into favorite database or not .
    private suspend fun getProductFromFavorite(id: String): Boolean {
        val productModel = favoriteDao.getSpecificFavoriteProduct(id)
        return productModel != null
    }

    // observe to specific product when save or not to change favorite icon .
    fun getProductFromFavoriteLiveData(id: String): LiveData<ProductModel?> =
        favoriteDao.getSpecificFavoriteProductLiveData(id)

    fun getFavoriteProductsLiveData(): LiveData<List<ProductModel>> =
        favoriteDao.getAllFavoriteProducts()

    /*
          get products from specific category by get category value and get products from
           getProductsByCategory function and get all products where it contain to category id.
     */
    suspend fun getSpecificCategoryProducts(categoryId: String): Resource<MainShopItem> {
        return try {
            val result = fireStore.collection(CATEGORY).document(categoryId).get().await()
            val categoryItem = convertMapToCategoryItem(result.data!!)
            Resource.Success(getProductsByCategory(listOf(categoryItem))[0])
        } catch (e: Exception) {
            Resource.Error(errorMessage)
        }
    }

    suspend fun getProductsContainName(searchName: String): Resource<List<ProductModel>> {
        return try {
            val result =
                fireStore.collection(PRODUCTS).get().await()
            val products = convertDocumentToProductList(result.documents)
            val selectedProducts = mutableListOf<ProductModel>()
            selectedProducts.addAll(products.filter {
                it.name.lowercase().contains(searchName.lowercase())
            })
            Resource.Success(selectedProducts)
        } catch (e: Exception) {
            Resource.Error(errorMessage)
        }

    }

    suspend fun addProductsToCart(list: List<ProductModel>, deleteFavoriteProducts: Boolean): Resource<Any> {
        return try {
            list.forEach { product ->
                cartCollection.document(product.id).set(product).await()
            }
            if(deleteFavoriteProducts)
                favoriteDao.deleteAllProducts()
            Resource.Success(Any())
        } catch (e: Exception) {
            Resource.Error(errorMessage)
        }

    }

    suspend fun deleteProductFromUserCart(productId: String) {
        cartCollection.document(productId).delete().await()
    }

    suspend fun getAllUserProducts(): Resource<List<ProductModel>> {
        return try {
            val result = cartCollection.get().await()
            val products = convertDocumentToProductList(result.documents)
            Resource.Success(products)
        } catch (e: Exception) {
            Resource.Error(errorMessage)
        }
    }
}