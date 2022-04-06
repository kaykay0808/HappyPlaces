package com.kay.happyplaces.repository

import androidx.lifecycle.LiveData
import com.kay.happyplaces.database.HappyPlaceDao
import com.kay.happyplaces.models.HappyPlaceModelEntity

class HappyPlaceRepository (private val happyPlaceDao: HappyPlaceDao){
    val getAllData: LiveData<List<HappyPlaceModelEntity>> = happyPlaceDao.fetchAllPlaces()

    suspend fun insert(happyPlaceModelEntity: HappyPlaceModelEntity) {
        happyPlaceDao.insert(happyPlaceModelEntity)
    }

    suspend fun updateData(happyPlaceModelEntity: HappyPlaceModelEntity) {
        happyPlaceDao.update(happyPlaceModelEntity)
    }

    suspend fun deleteItem(happyPlaceModelEntity: HappyPlaceModelEntity) {
        happyPlaceDao.delete(happyPlaceModelEntity)
    }
}