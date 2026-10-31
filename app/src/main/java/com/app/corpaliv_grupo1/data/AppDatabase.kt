package com.app.corpaliv_grupo1.data

import android.content.Context
import androidx.room3.Database
import androidx.room3.Room
import androidx.room3.RoomDatabase
import androidx.sqlite.driver.AndroidSQLiteDriver

@Database(entities = [RegistroEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun registroDao(): RegistroDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder<AppDatabase>(
                    context.applicationContext,
                    "corpaliv.db"
                )
                    .setDriver(AndroidSQLiteDriver())
                    .build()
                    .also { INSTANCE = it }
            }
    }
}