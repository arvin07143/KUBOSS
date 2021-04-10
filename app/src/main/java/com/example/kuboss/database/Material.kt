package com.example.kuboss.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "material_table")
class Material(
    @PrimaryKey
    var materialId: String,

    @ColumnInfo
    var SKU: String,

    @ColumnInfo
    var quantity: Int
) {
}