package com.example.countries.db

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.countries.ui.CountryApplication
import com.example.countries.utils.DbConstants
import com.example.countries.dao.CountryDao
import com.example.countries.model.Country

@Database(
    entities = [Country::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(CountriesTypeConverter::class)
abstract class CountryDatabase : RoomDatabase() {
    abstract fun countryDao(): CountryDao?

    companion object {
        private var INSTANCE: CountryDatabase? = null
        fun getDatabase(): CountryDatabase? {
            if (INSTANCE == null) {
                synchronized(CountryDatabase::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = Room.databaseBuilder(
                            CountryApplication.INSTANCE,
                            CountryDatabase::class.java, DbConstants.DB_NAME
                        )
                            .fallbackToDestructiveMigration()
                            .build()
                    }
                }
            }
            return INSTANCE
        }
    }
}