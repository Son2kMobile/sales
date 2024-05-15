package com.ngoctuan.sales.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation

@Entity("history")
data class History(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val personId: Person,

    @Relation(
        parentColumn = "id",
        entityColumn = "personId"
    )
    val dateTime: String,
    val status: Boolean
)
