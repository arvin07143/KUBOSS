package com.example.kuboss.warehouse

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.kuboss.database.WarehouseDatabaseDao

class SearchUserViewModel(val database: WarehouseDatabaseDao, application: Application) : ViewModel() {

    private val _searchStringLiveData = MutableLiveData<String>()

    val searchResults = Transformations.switchMap(_searchStringLiveData){ string ->
        database.searchByUserName(string)

    }

    init {
        _searchStringLiveData.value = ""

    }

    fun searchStringChange(name:String){
        _searchStringLiveData.value = name

    }

}



