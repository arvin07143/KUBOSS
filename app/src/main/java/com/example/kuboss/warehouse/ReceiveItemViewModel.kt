package com.example.kuboss.warehouse

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.kuboss.database.Material
import com.example.kuboss.database.WarehouseDatabaseDao
import kotlinx.coroutines.launch
import kotlin.random.Random

class ReceiveItemViewModel(
    val database: WarehouseDatabaseDao,
    application: Application,
): AndroidViewModel(application) {
    private val allMaterialID = database.getAllMaterialID()
    val receivedItems = database.getReceivedItems()
    fun getNewMaterialID() : String{
        val currentExistMaterialID = allMaterialID.value?.toSet()
        var tempID: Int
        var idString : String
        if (currentExistMaterialID == null){
            tempID = Random.nextInt(0,9999)
            idString = tempID.toString().padStart(4,'0')
            return idString
        }
        do {
            tempID = Random.nextInt(0,9999)
            idString = tempID.toString().padStart(4,'0')
        }while (!currentExistMaterialID.contains(idString))

        return idString
    }

    fun storeMaterial(mat: Material): LiveData<Boolean> {
        val error = MutableLiveData<Boolean>()
        viewModelScope.launch {
            try {
                database.insert(mat)
                error.value = false
            } catch (e:Exception){
                Log.e("DB",e.toString())
                error.value = true
            }
        }
        return error
    }
}
