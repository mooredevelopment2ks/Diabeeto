package com.twokingssolutions.diabeeto.di

import android.content.Context
import androidx.room.Room
import com.twokingssolutions.diabeeto.util.Constants
import com.twokingssolutions.diabeeto.db.FoodDatabase

fun provideDatabase(context: Context) =
    Room.databaseBuilder(context, FoodDatabase::class.java, Constants.FOOD_DATABASE)
        .allowMainThreadQueries()
        .fallbackToDestructiveMigration(false)
        .build()

fun provideDao(db: FoodDatabase) = db.foodDoa()