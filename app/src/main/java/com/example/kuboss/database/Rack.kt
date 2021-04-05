package com.example.kuboss.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName="rack_table")
class Rack (
    @PrimaryKey
    var rackId: String


)