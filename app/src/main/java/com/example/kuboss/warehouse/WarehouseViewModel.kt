package com.example.kuboss.warehouse

import android.app.Application
import android.database.sqlite.SQLiteConstraintException
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.kuboss.database.Material
import com.example.kuboss.database.Rack
import com.example.kuboss.database.WarehouseDatabaseDao
import kotlinx.coroutines.launch


class WarehouseViewModel(
    val database: WarehouseDatabaseDao,
    application: Application
) : AndroidViewModel(application) {

    val racks = database.getAllRacks()
    private var materialList = listOf<Material>()

    private var _isSqlError = MutableLiveData(false)
    val isSqlError: LiveData<Boolean>
        get() = _isSqlError

    private var _isAddRackSuccess = MutableLiveData(false)
    val isAddRackSuccess: LiveData<Boolean>
        get() = _isAddRackSuccess

    fun onAddRack(rackId: String) {
        viewModelScope.launch {
            val newRack = Rack(rackId)
            insert(newRack)

        }
    }

    private suspend fun insert(rack: Rack) {
        try {
            database.insert(rack)
            _isAddRackSuccess.value = true
        } catch (e: SQLiteConstraintException) {
            _isSqlError.value = true
        }
    }

    fun finishShowingDialog() {
        _isSqlError.value = false
        _isAddRackSuccess.value = false
    }

    suspend fun getReportString(): String {
        val scope = viewModelScope.launch {
            materialList = database.getAllMaterials()
        }
        scope.join()
        var reportStr = "Rack ID, Material, Name, SKU, Quantity \n"
        for (mat: Material in materialList) {
            val rackId = (mat.mRackId ?: "Not Assigned")
            val matId = mat.materialId
            val matName = mat.materialName
            val sku = mat.SKU
            val qty = mat.quantity.toString()
            reportStr += "$rackId,$matId,$matName,$sku,$qty\n"
        }

        return reportStr
    }

}