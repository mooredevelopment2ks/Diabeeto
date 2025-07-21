package com.twokingssolutions.diabeeto.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable
import com.twokingssolutions.diabeeto.util.Constants.DEPARTMENTS_TABLE

@Entity(
    tableName = DEPARTMENTS_TABLE,
    indices = [
        Index(value = ["department_name"], unique = true)
    ]
)
@Serializable
data class Departments(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "department_id")
    val departmentId: String,
    @ColumnInfo(name = "department_name")
    val departmentName: String
)