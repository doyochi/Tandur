package com.finna.sales.database

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import id.hikmah.stiki.tandur_1.v2.database.entity.FavoriteEntity
import id.hikmah.stiki.tandur_1.v2.database.entity.ProductEntity

@androidx.room.Database(
    entities = [
        ProductEntity::class,
        FavoriteEntity::class,
    ],
    version = 2,
    exportSchema = false
)
abstract class Database: RoomDatabase() {
    abstract fun appDAO(): AppDAO

    companion object {
        @Volatile
        private var INSTANCE: Database? = null

        fun getDatabase(context: Context): Database {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    Database::class.java,
                    "tandur_db"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}