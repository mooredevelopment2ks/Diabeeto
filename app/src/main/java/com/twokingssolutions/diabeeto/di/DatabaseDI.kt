package com.twokingssolutions.diabeeto.di

import android.content.Context
import androidx.room.Room
import com.twokingssolutions.diabeeto.db.FoodDao
import com.twokingssolutions.diabeeto.db.FoodDatabase

fun provideDatabase(context: Context): FoodDatabase {
    return Room.databaseBuilder(
        context,
        FoodDatabase::class.java,
        "food_database"
    ).build()
}

fun provideDao(database: FoodDatabase): FoodDao {
    return database.foodDao()
}