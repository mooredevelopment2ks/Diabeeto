package com.twokingssolutions.diabeeto.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable
import com.twokingssolutions.diabeeto.util.Constants.NUTRITIONAL_INFORMATION_TABLE

@Entity(
    tableName = NUTRITIONAL_INFORMATION_TABLE,
    foreignKeys = [
        ForeignKey(
            entity = Product::class,
            parentColumns = ["product_id"],
            childColumns = ["product_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["product_id"], unique = true)
    ]
)
@Serializable
data class NutritionalInformation(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "nutritional_info_id")
    val nutritionalInfoId: String,
    @ColumnInfo(name = "product_id")
    val productId: String,
    @ColumnInfo(name = "serving_size")
    val servingSize: String?,
    @ColumnInfo(name = "servings_per_pack")
    val servingsPerPack: Double?
)