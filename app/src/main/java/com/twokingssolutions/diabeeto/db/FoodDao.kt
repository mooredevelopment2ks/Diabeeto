package com.twokingssolutions.diabeeto.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.twokingssolutions.diabeeto.util.Constants.FOOD_TABLE
import kotlinx.coroutines.flow.Flow

@Dao
interface FoodDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFood(food: Food)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateFood(food: Food)

    @Delete
    suspend fun deleteFood(food: Food)

    @Query("SELECT * FROM $FOOD_TABLE ORDER BY food_item ASC")
    fun getAllFoodsOrderByFoodItem(): Flow<List<Food>>
    /*
     * The '%' wildcard matches zero or more characters,
     * so placing it before and after the query string
     * allows for a partial match anywhere within the food_item.
     */
    @Query("SELECT * FROM $FOOD_TABLE WHERE food_item LIKE '%' || :query || '%' ORDER BY food_item ASC")
    fun searchFoods(query: String): Flow<List<Food>>
}