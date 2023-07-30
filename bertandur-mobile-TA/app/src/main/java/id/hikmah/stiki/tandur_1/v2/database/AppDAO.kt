package com.finna.sales.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import id.hikmah.stiki.tandur_1.v2.database.entity.FavoriteEntity
import id.hikmah.stiki.tandur_1.v2.database.entity.ProductEntity

@Dao
interface AppDAO {
    @Query("SELECT * FROM ProductEntity")
    fun getProduct(): LiveData<List<ProductEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertProduct(productEntity: ProductEntity)

    @Query("DELETE FROM ProductEntity WHERE idProduct = :idProduct")
    fun deleteProduct(idProduct: String)

    @Query("SELECT * FROM FavoriteEntity")
    fun getAllFavorite(): LiveData<List<FavoriteEntity>>

    @Query("SELECT * FROM FavoriteEntity WHERE idProduct = :idProduct")
    fun getFavorite(idProduct: String): LiveData<FavoriteEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertFavorite(favoriteEntity: FavoriteEntity)

    @Query("DELETE FROM FavoriteEntity WHERE idProduct = :idProduct")
    fun deleteFavorite(idProduct: String)
}