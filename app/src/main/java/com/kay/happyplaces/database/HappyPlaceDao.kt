package com.kay.happyplaces.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.kay.happyplaces.models.HappyPlaceModelEntity

@Dao
interface HappyPlaceDao {
    @Insert
    suspend fun insert(happyPlaceModelEntity: HappyPlaceModelEntity)

    @Update
    suspend fun update(happyPlaceModelEntity: HappyPlaceModelEntity)

    @Delete
    suspend fun delete(happyPlaceModelEntity: HappyPlaceModelEntity)

    // reading data / retrieving data?
    @Query("SELECT * FROM 'happyPlaceTable'")
    fun fetchAllPlaces(): LiveData<List<HappyPlaceModelEntity>>

    @Query("SELECT * FROM 'happyPlaceTable' where id=:id")
    fun fetchPlacesById(id: Int): LiveData<HappyPlaceModelEntity>
}