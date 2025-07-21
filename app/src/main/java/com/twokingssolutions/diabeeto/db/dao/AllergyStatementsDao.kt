package com.twokingssolutions.diabeeto.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.twokingssolutions.diabeeto.db.entity.AllergyStatements
import com.twokingssolutions.diabeeto.db.entity.Product

@Dao
interface AllergyStatementsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllergyStatement(allergyStatements: AllergyStatements)

    @Query("SELECT * FROM AllergyStatements WHERE allergy_statement_id = :id")
    suspend fun getAllergyStatementById(id: String): AllergyStatements?

    @Query("SELECT * FROM AllergyStatements WHERE statement_name = :name")
    fun getAllergyStatementByName(name: String): AllergyStatements?

    @Query("SELECT * FROM AllergyStatements")
    suspend fun getAllAllergyStatementsList(): List<AllergyStatements>

    @Transaction
    @Query("""
    SELECT P.* FROM Products P
    JOIN ProductAllergyCrossRef PA ON P.product_id = PA.product_id
    JOIN AllergyStatements ALS ON PA.allergy_statement_id = ALS.allergy_statement_id
    WHERE ALS.statement_name = 'Contains Gluten'
""")
    fun getProductsContainingGluten(): List<Product>
}