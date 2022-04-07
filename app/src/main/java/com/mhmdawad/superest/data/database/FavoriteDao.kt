package com.mhmdawad.superest.data.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.mhmdawad.superest.model.ProductModel
@Dao
interface FavoriteDao {
    
    @Insert(onConflict = REPLACE)
    fun saveProduct(productModel: ProductModel)
    
    @Query("SELECT * FROM ProductModel")
    fun getAllFavoriteProducts(): LiveData<List<ProductModel>>

    @Query("SELECT * FROM ProductModel WHERE id =:id")
    suspend fun getSpecificFavoriteProduct(id: String): ProductModel?

    @Query("SELECT * FROM ProductModel WHERE id =:id")
    fun getSpecificFavoriteProductLiveData(id: String): LiveData<ProductModel?>

    @Delete
    suspend fun removeProductFromFavorites(productModel: ProductModel)

    @Query("DELETE FROM ProductModel")
    suspend fun deleteAllProducts()
}