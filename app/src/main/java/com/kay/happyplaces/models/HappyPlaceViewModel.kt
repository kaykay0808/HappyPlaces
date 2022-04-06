package com.kay.happyplaces.models

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.kay.happyplaces.database.HappyPlaceDatabase
import com.kay.happyplaces.repository.HappyPlaceRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HappyPlaceViewModel(application: Application) : AndroidViewModel(application){
    private val happyPlaceDaoVM = HappyPlaceDatabase.getInstance(application).happyPlaceDao()
    private val repositoryVM : HappyPlaceRepository = HappyPlaceRepository(happyPlaceDaoVM)

    val getAllDataVM: LiveData<List<HappyPlaceModelEntity>> = repositoryVM.getAllData

    fun insertDataVM(happyPlaceModelEntity: HappyPlaceModelEntity){
        viewModelScope.launch(Dispatchers.IO){
            repositoryVM.insert(happyPlaceModelEntity)
        }
    }

    fun updateDataVM(happyPlaceModelEntity: HappyPlaceModelEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repositoryVM.updateData(happyPlaceModelEntity)
        }
    }

    fun deleteItemVM(happyPlaceModelEntity: HappyPlaceModelEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repositoryVM.deleteItem(happyPlaceModelEntity)
        }
    }
}