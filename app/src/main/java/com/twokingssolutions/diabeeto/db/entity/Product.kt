package com.twokingssolutions.diabeeto.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.twokingssolutions.diabeeto.util.Constants.PRODUCTS_TABLE
import kotlinx.serialization.Serializable

@Entity(
    tableName = PRODUCTS_TABLE,
    foreignKeys = [
        ForeignKey(
            entity = Departments::class,
            parentColumns = ["department_id"],
            childColumns = ["department_id"],
            onDelete = ForeignKey.SET_NULL
        )
    ],
    indices = [
        Index(value = ["barcode"], unique = true),
        Index(value = ["department_id"]),
        Index(value = ["name"])
    ]
)
@Serializable
data class Product(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "product_id")
    val productId: String, // UUID.randomUUID().toString() to generate a unique ID
    @ColumnInfo(name = "barcode")
    var barcode: String?,
    @ColumnInfo(name = "name")
    var name: String,
    @ColumnInfo(name = "display_name")
    var displayName: String?,
    @ColumnInfo(name = "package_size")
    var packageSize: String?,
    @ColumnInfo(name = "cup_measure")
    var cupMeasure: String?,
    @ColumnInfo(name = "full_description")
    var fullDescription: String?,
    @ColumnInfo(name = "brand")
    var brand: String?,
    @ColumnInfo(name = "ingredients")
    var ingredients: String?,
    @ColumnInfo(name = "storage_instructions")
    var storageInstructions: String?,
    @ColumnInfo(name = "department_id")
    val departmentId: String?,
    @ColumnInfo(name = "is_favourite", defaultValue = "0")
    var isFavourite: Boolean,
    @ColumnInfo(name = "is_editable", defaultValue = "0")
    val isEditable: Boolean
)