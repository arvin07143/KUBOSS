package com.example.kuboss.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "rack_table")
data class Rack(
    @PrimaryKey(autoGenerate = false)
    var rackID: Int,
    @ColumnInfo(name = "rack_items")
    //@TypeConverters(ListConverter::class)
    var rackItems: String
)