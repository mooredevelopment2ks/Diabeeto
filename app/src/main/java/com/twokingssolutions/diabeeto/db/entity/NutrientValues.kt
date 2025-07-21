package com.twokingssolutions.diabeeto.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable
import com.twokingssolutions.diabeeto.util.Constants.NUTRIENT_VALUES_TABLE
import java.util.UUID

@Entity(
    tableName = NUTRIENT_VALUES_TABLE,
    foreignKeys = [
        ForeignKey(
            entity = NutritionalInformation::class,
            parentColumns = ["nutritional_info_id"],
            childColumns = ["nutritional_info_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["nutritional_info_id"]),
        Index(value = ["nutrient_name"])
    ]
)
@Serializable
data class NutrientValues(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "nutrient_value_id")
    val nutrientValueId: String = UUID.randomUUID().toString(),
    @ColumnInfo(name = "nutritional_info_id")
    val nutritionalInfoId: String,
    @ColumnInfo(name = "nutrient_name")
    val nutrientName: String?,
    @ColumnInfo(name = "quantity_per_serving")
    val quantityPerServing: String?,
    @ColumnInfo(name = "quantity_per_100g_100ml")
    val quantityPer100g100ml: String?
)