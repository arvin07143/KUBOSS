package com.example.kuboss.database

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ListConverter {
    //Convert list of pairs to a JSON
    @TypeConverter
    fun fromListJson(stat: List<Pair<String,Int>>):String{
        return Gson().toJson(stat)
    }

    //Convert json to list of pairs
    @TypeConverter
    fun toPairList(jsonList: String): MutableList<Pair<String,Int>>{
        val notesType = object  : TypeToken<List<Pair<String,Int>>>() {}.type
        return Gson().fromJson(jsonList,notesType)
    }
}
