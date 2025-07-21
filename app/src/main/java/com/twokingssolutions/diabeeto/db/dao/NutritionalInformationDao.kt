package com.twokingssolutions.diabeeto.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.twokingssolutions.diabeeto.db.entity.NutritionalInformation

@Dao
interface NutritionalInformationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNutritionalInformation(nutritionalInformation: NutritionalInformation)

    @Query("SELECT * FROM NutritionalInformation WHERE nutritional_info_id = :id")
    suspend fun getNutritionalInformationById(id: String): NutritionalInformation?

    @Query("SELECT * FROM NutritionalInformation WHERE product_id = :productId")
    suspend fun getNutritionalInformationForProduct(productId: String): NutritionalInformation?
}