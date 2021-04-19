package com.example.kuboss.warehouse

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.kuboss.database.WarehouseDatabaseDao

class SearchItemViewModel(
    val database: WarehouseDatabaseDao,
    application: Application
) : ViewModel() {

    private val _searchStringLiveData = MutableLiveData<String>()
    val searchResults = Transformations.switchMap(_searchStringLiveData){
        string -> database.searchBySKU(string)
    }

    init {
        _searchStringLiveData.value = ""
    }

    fun searchStringChange(SKU:String){
        _searchStringLiveData.value = SKU
    }


}