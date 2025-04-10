package com.twokingssolutions.diabeeto.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [Food::class],
    version = 3,
    exportSchema = false
)
abstract class FoodDatabase: RoomDatabase(){
    abstract fun foodDoa(): FoodDao
}