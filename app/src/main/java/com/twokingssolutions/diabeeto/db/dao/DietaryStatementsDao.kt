package com.twokingssolutions.diabeeto.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.twokingssolutions.diabeeto.db.entity.DietaryStatements
import com.twokingssolutions.diabeeto.db.entity.Product

@Dao
interface DietaryStatementsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDietaryStatement(dietaryStatements: DietaryStatements)

    @Query("SELECT * FROM DietaryStatements WHERE dietary_statement_id = :id")
    suspend fun getDietaryStatementById(id: String): DietaryStatements?

    @Query("SELECT * FROM DietaryStatements WHERE statement_name = :name")
    fun getDietaryStatementByName(name: String): DietaryStatements?

    @Query("SELECT * FROM DietaryStatements")
    suspend fun getAllDietaryStatementsList(): List<DietaryStatements>

    @Transaction
    @Query("""
        SELECT P.* FROM Products P
        JOIN ProductDietaryCrossRef PD ON P.product_id = PD.product_id
        JOIN DietaryStatements DS ON PD.dietary_statement_id = DS.dietary_statement_id
        WHERE DS.statement_name = 'Freezable'
    """)
    fun getFreezableProducts(): List<Product>

    @Transaction
    @Query("""
        SELECT P.* FROM Products P
        WHERE P.product_id NOT IN (
            SELECT PD.product_id FROM ProductDietaryCrossRef PD
            JOIN DietaryStatements DS ON PD.dietary_statement_id = DS.dietary_statement_id
            WHERE DS.statement_name = 'Freezable'
        )
    """)
    fun getNonFreezableProducts(): List<Product>

    @Transaction
    @Query("""
        SELECT P.* FROM Products P
        JOIN ProductDietaryCrossRef PD ON P.product_id = PD.product_id
        JOIN DietaryStatements DS ON PD.dietary_statement_id = DS.dietary_statement_id
        WHERE DS.statement_name = 'Gluten Free'
    """)
    fun getGlutenFreeProducts(): List<Product>

    @Query("SELECT dietary_statement_id FROM DietaryStatements WHERE dietary_statement_id IN (:ids)")
    suspend fun getExistingDietaryStatementIds(ids: List<String>): List<String>
}