package com.example.kuboss.warehouse

import android.database.sqlite.SQLiteConstraintException
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kuboss.database.Rack
import com.example.kuboss.database.WarehouseDatabaseDao
import kotlinx.coroutines.launch

class AddRackViewModel(
    val database: WarehouseDatabaseDao
): ViewModel() {
    private var _isSqlError = MutableLiveData<Boolean>(false)
    val isSqlError: LiveData<Boolean> get() = _isSqlError
    private var _isAddRackSuccess = MutableLiveData<Boolean>(false)
    val isAddRackSuccess: LiveData<Boolean> get() = _isAddRackSuccess
    fun onAddRack(rackId: String){
        viewModelScope.launch {
            val newRack = Rack(rackId)
            insert(newRack)

        }
    }
    private suspend fun insert(rack: Rack){
        try {
            database.insert(rack)
            _isAddRackSuccess.value = true
        }catch(e: SQLiteConstraintException){
            _isSqlError.value = true
        }
    }
    fun finishShowingDialog(){
        _isSqlError.value = false
        _isAddRackSuccess.value = false
    }

}