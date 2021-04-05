package com.example.kuboss.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "rack_content_table", primaryKeys = ["rackId", "materialId"])
data class RackContent(
    @ColumnInfo
    var rackId: String,

    @ColumnInfo
    var materialId: String
)
