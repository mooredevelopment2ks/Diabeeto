package com.twokingssolutions.diabeeto.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [Food::class],
    version = 1,
    exportSchema = false
)
abstract class FoodDatabase: RoomDatabase(){
    abstract fun foodDoa(): FoodDao
}