package com.twokingssolutions.diabeeto.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.twokingssolutions.diabeeto.util.Constants.FOOD_TABLE
import kotlinx.serialization.Serializable

@Entity(tableName = FOOD_TABLE)
@Serializable
data class Food(
    @ColumnInfo(name = "food_item")
    val foodItem: String = "",
    @ColumnInfo(name = "carb_amount")
    val carbAmount: String = "",
    @ColumnInfo(name = "notes")
    val notes: String,
    @ColumnInfo(name = "image_uri")
    val imageUri: String,
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0
)
