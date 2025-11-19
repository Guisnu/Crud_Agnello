package com.fiap.agnello.dataset.dao

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.fiap.agnello.model.Vinho


@Database(entities = [Vinho::class], version = 1)
abstract class VinhoDb : RoomDatabase() {
    abstract fun VinhoDao(): VinhoDao
    companion object{

        private lateinit var instance: VinhoDb

        fun getDataBase(context: Context): VinhoDb {
            if (!::instance.isInitialized) {
                instance = Room.databaseBuilder(
                    context,
                    VinhoDb::class.java,
                    "vinho_db"
                )
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration(false)
                    .build()
            }
        return instance
        }
    }
}