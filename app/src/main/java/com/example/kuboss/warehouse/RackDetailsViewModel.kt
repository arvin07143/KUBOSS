package com.example.kuboss.warehouse

import android.app.Application
import android.database.sqlite.SQLiteConstraintException
import android.util.Log
import androidx.lifecycle.*
import com.example.kuboss.database.Material
import com.example.kuboss.database.Rack
import com.example.kuboss.database.RackWithMaterials
import com.example.kuboss.database.WarehouseDatabaseDao
import kotlinx.coroutines.launch

class RackDetailsViewModel(
    val database: WarehouseDatabaseDao,
    application: Application,
    var rackId: String
    ): AndroidViewModel(application) {

    val materialList = database.getMaterials(rackId)
    private var _isSqlError = MutableLiveData<Boolean>(false)
    val isSqlError: LiveData<Boolean> get() = _isSqlError

    fun storeMaterial(mat: Material){
        viewModelScope.launch {
            try {
                database.insert(mat)
            }catch(e: SQLiteConstraintException){
                _isSqlError.value = true
            }
            Log.e("DB","ADD SUCCESSFUL")
        }
    }

    fun finishShowingDialog(){
        _isSqlError.value = false
    }


}