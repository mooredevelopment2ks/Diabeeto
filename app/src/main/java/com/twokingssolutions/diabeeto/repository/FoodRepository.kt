package com.twokingssolutions.diabeeto.repository

import com.twokingssolutions.diabeeto.db.FoodDao
import com.twokingssolutions.diabeeto.db.Food
import kotlinx.coroutines.flow.Flow

class FoodRepository(private val foodDao: FoodDao) {
    suspend fun insertFood(food: Food) = foodDao.insertFood(food)
    suspend fun updateFood(food: Food) = foodDao.updateFood(food)
    suspend fun deleteFood(food: Food) = foodDao.deleteFood(food)
    fun getAllFoodsOrderByFoodItem(): Flow<List<Food>> = foodDao.getAllFoodsOrderByFoodItem()
    fun searchFoods(query: String): Flow<List<Food>> = foodDao.searchFoods(query)
}