package com.twokingssolutions.diabeeto.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.twokingssolutions.diabeeto.db.entity.ProductDietaryCrossRef

@Dao
interface ProductDietaryCrossRefDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertProductDietaryStatement(productDietaryCrossRef: ProductDietaryCrossRef)

    @Query("DELETE FROM ProductDietaryCrossRef WHERE product_id = :productId")
    suspend fun deleteByProductId(productId: String)

    @Transaction
    suspend fun updateDietariesForProduct(productId: String, dietaryIds: List<String>) {
        deleteByProductId(productId)
        dietaryIds.forEach { dietaryId ->
            insertProductDietaryStatement(ProductDietaryCrossRef(productId, dietaryId))
        }
    }
}