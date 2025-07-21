package com.twokingssolutions.diabeeto.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.twokingssolutions.diabeeto.db.entity.ProductAllergyCrossRef

@Dao
interface ProductAllergyCrossRefDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertProductAllergyStatement(productAllergyCrossRef: ProductAllergyCrossRef)

    @Query("DELETE FROM ProductAllergyCrossRef WHERE product_id = :productId")
    suspend fun deleteByProductId(productId: String)

    @Transaction
    suspend fun updateAllergiesForProduct(productId: String, allergyIds: List<String>) {
        deleteByProductId(productId)
        allergyIds.forEach { allergyId ->
            insertProductAllergyStatement(ProductAllergyCrossRef(productId, allergyId))
        }
    }
}