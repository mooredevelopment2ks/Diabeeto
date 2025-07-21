package com.twokingssolutions.diabeeto.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.twokingssolutions.diabeeto.db.entity.NutrientValues

@Dao
interface NutrientValuesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNutrientValue(nutrientValues: NutrientValues)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNutrientValues(nutrientValues: List<NutrientValues>)

    @Update
    suspend fun updateNutrientValues(nutrientValues: List<NutrientValues>)

    @Query("SELECT * FROM NutrientValues WHERE nutrient_value_id = :id")
    suspend fun getNutrientValueById(id: String): NutrientValues?

    @Query("SELECT * FROM NutrientValues WHERE nutritional_info_id = :nutritionalInfoId")
    suspend fun getNutrientValuesForNutritionalInfo(nutritionalInfoId: String): List<NutrientValues>

    @Query("DELETE FROM NutrientValues WHERE nutritional_info_id = :nutritionalInfoId")
    suspend fun deleteNutrientValuesForNutritionalInfo(nutritionalInfoId: String)

    @Transaction
    suspend fun updateNutrientValuesForInfo(nutritionalInfoId: String, newNutrientValues: List<NutrientValues>) {
        deleteNutrientValuesForNutritionalInfo(nutritionalInfoId)
        insertNutrientValues(newNutrientValues)
    }
}