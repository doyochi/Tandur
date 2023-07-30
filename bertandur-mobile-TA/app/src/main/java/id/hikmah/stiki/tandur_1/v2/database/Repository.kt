package com.finna.sales.database

import androidx.lifecycle.LiveData
import id.hikmah.stiki.tandur_1.v2.database.entity.FavoriteEntity
import id.hikmah.stiki.tandur_1.v2.database.entity.ProductEntity

class Repository(private val appDAO: AppDAO) {

    fun getProduct(): LiveData<List<ProductEntity>>  {
        return appDAO.getProduct()
    }

    fun insertProduct(productEntity: ProductEntity) {
        appDAO.insertProduct(productEntity)
    }

    fun deleteProduct(param: String) {
        appDAO.deleteProduct(param)
    }

    fun getAllFavorite(): LiveData<List<FavoriteEntity>>  {
        return appDAO.getAllFavorite()
    }

    fun getFavorite(idProduct: String): LiveData<FavoriteEntity>  {
        return appDAO.getFavorite(idProduct)
    }

    fun insertFavorite(favoriteEntity: FavoriteEntity) {
        appDAO.insertFavorite(favoriteEntity)
    }

    fun deleteFavorite(param: String) {
        appDAO.deleteFavorite(param)
    }
}