package com.example.kuboss.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Entity(tableName="rack_table")
data class Rack (
    @PrimaryKey
    val rackId: String
)

@Entity(tableName = "material_table")
data class Material(
    @PrimaryKey
    val materialId: String,
    val SKU: String,
    val quantity: Int,
    val mRackId: String,
    val materialName: String
)

data class RackWithMaterials(
    @Embedded val rack: Rack,
    @Relation(
        parentColumn = "rackId",
        entityColumn = "mRackId"
    )
    val materials: List<Material>
)