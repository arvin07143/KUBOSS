package com.example.kuboss.database

import androidx.lifecycle.LiveData
import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Relation

@Entity(primaryKeys = ["rackId", "materialId"])
class RackDetails (
    @ColumnInfo
    var rackId: String,

    @ColumnInfo
    var materialId: String)

//data class RackWithMaterials(
//    @Embedded val rack: Rack,
//    @Relation(
//        parentColumn = "rackId",
//        entityColumn = "materialId"
//        associateBy = Junction()
//    )
//    val materials: LiveData<List<Material>>
//)