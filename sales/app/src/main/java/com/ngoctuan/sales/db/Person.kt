package com.ngoctuan.sales.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Person")
data class Person(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val name: String?,
    val number: String?,

    var called: Boolean = false,
    val sheetIndex: Int,
    val sheetName: String?,
    val aspiration: String?,
    val note: String?,
    val phoneParent: String?,
)