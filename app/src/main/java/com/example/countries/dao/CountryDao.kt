package com.example.countries.dao


import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.countries.model.Country

@Dao
interface CountryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(contact: List<Country>)

    @Insert
    fun insert(country: Country)

    @Query("SELECT * from country")
    fun getAll(): LiveData<List<Country>>

    @Query("DELETE from country")
    fun deleteAll()
}