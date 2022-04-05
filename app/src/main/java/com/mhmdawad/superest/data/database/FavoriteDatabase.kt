package com.mhmdawad.superest.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.mhmdawad.superest.model.ProductModel

@Database(entities = [ProductModel::class], version = 1, exportSchema = false)
abstract class FavoriteDatabase: RoomDatabase() {

    abstract fun getFavoriteDao(): FavoriteDao
}