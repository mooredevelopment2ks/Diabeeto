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
    val carbAmount: Int = 0,
    @ColumnInfo(name = "notes")
    val notes: String,
    @ColumnInfo(name = "is_favourite")
    val isFavourite: Boolean = false,
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0
)
