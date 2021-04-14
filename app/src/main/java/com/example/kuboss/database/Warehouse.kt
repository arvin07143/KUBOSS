package com.example.kuboss.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Entity(tableName="rack_table")
data class Rack (
    @PrimaryKey
    val rackId: String
)

@Entity(tableName = "material_table", foreignKeys = [ForeignKey(entity = Rack::class, parentColumns = ["rackId"], childColumns = ["mRackId"], onUpdate = ForeignKey.CASCADE)])
data class Material(
    @PrimaryKey
    val materialId: String,
    val SKU: String,
    val materialName: String,
    val quantity: Int,
    val mRackId: String?
)

data class RackWithMaterials(
    @Embedded val rack: Rack,
    @Relation(
        parentColumn = "rackId",
        entityColumn = "mRackId"
    )
    val materials: List<Material>
)

@Entity(tableName="user_table")
data class User (
    @PrimaryKey
    val email: String,
    val password: String,
    val name: String,
    val type: String
)