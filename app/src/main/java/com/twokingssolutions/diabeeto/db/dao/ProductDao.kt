package com.twokingssolutions.diabeeto.db.dao

import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Embedded
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.twokingssolutions.diabeeto.db.entity.NutrientValues
import com.twokingssolutions.diabeeto.db.entity.NutritionalInformation
import com.twokingssolutions.diabeeto.db.entity.Product
import com.twokingssolutions.diabeeto.db.relation.FullProductDetails
import com.twokingssolutions.diabeeto.db.relation.ProductWithAllergyStatements
import com.twokingssolutions.diabeeto.db.relation.ProductWithDietaryStatements
import com.twokingssolutions.diabeeto.util.Constants.PRODUCTS_TABLE
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProduct(product: Product)

    @Transaction
    suspend fun insertFullProductTransaction(
        product: Product,
        nutritionalInformation: NutritionalInformation,
        nutrientValues: List<NutrientValues>,
        allergyIds: List<String>,
        dietaryIds: List<String>,
        nutritionalInformationDao: NutritionalInformationDao,
        nutrientValuesDao: NutrientValuesDao,
        productAllergyCrossRefDao: ProductAllergyCrossRefDao,
        productDietaryCrossRefDao: ProductDietaryCrossRefDao
    ) {
        insertProduct(product)
        nutritionalInformationDao.insertNutritionalInformation(nutritionalInformation)
        nutrientValuesDao.insertNutrientValues(nutrientValues)
        productAllergyCrossRefDao.updateAllergiesForProduct(product.productId, allergyIds)
        productDietaryCrossRefDao.updateDietariesForProduct(product.productId, dietaryIds)
    }

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateProduct(product: Product)

    @Delete
    suspend fun deleteProduct(product: Product)

    @Transaction
    @Query("SELECT * FROM Products WHERE product_id = :productId")
    suspend fun getFullProductDetailsById(productId: String): FullProductDetails?

    @Query("SELECT * FROM Products WHERE barcode = :barcode")
    suspend fun getProductByBarcode(barcode: String): Product?

    @Transaction
    @Query("SELECT * FROM $PRODUCTS_TABLE ORDER BY name ASC")
    fun getAllProducts(): Flow<List<FullProductDetails>>

    @Transaction
    @Query("""
        SELECT * FROM $PRODUCTS_TABLE p 
        LEFT JOIN Departments d ON p.department_id = d.department_id 
        WHERE p.name LIKE '%' || :query || '%' 
        ORDER BY COALESCE(d.department_name, 'Unspecified') ASC, p.name ASC 
        LIMIT :limit OFFSET :offset
    """)
    suspend fun getFullProductDetailsByQueryPaginated(
        query: String,
        limit: Int,
        offset: Int
    ): List<FullProductDetails>

    @Query("""
        SELECT COUNT(*) FROM $PRODUCTS_TABLE p 
        WHERE p.name LIKE '%' || :query || '%'
    """)
    suspend fun getProductCountByQuery(query: String): Int

    @Query("SELECT * FROM Products WHERE department_id = :departmentId")
    suspend fun getProductsByDepartment(departmentId: String): List<Product>

    data class ProductWithDepartmentName(
        @Embedded val product: Product,
        @ColumnInfo(name = "department_name") val departmentName: String?
    )

    @Query(
        """
        SELECT P.*, D.department_name
        FROM Products AS P
        LEFT JOIN Departments AS D ON P.department_id = D.department_id
        WHERE P.product_id = :productId
    """
    )
    suspend fun getProductWithDepartmentName(productId: String): ProductWithDepartmentName?

    @Transaction
    @Query("SELECT * FROM $PRODUCTS_TABLE WHERE is_favourite = 1 ORDER BY name ASC")
    fun getAllFavourites(): Flow<List<FullProductDetails>>

    @Transaction
    @Query("SELECT * FROM Products WHERE product_id = :productId")
    fun getProductWithDietaryStatements(productId: String): ProductWithDietaryStatements?

    @Transaction
    @Query("SELECT * FROM Products WHERE product_id = :productId")
    suspend fun getProductWithAllergyStatements(productId: String): ProductWithAllergyStatements?

    @Transaction
    @Query("""
        SELECT * FROM $PRODUCTS_TABLE p 
        LEFT JOIN Departments d ON p.department_id = d.department_id 
        WHERE COALESCE(d.department_name, 'Unspecified') = :departmentName
        ORDER BY p.name ASC 
        LIMIT :limit OFFSET :offset
    """)
    suspend fun getFullProductDetailsByDepartmentPaginated(
        departmentName: String,
        limit: Int,
        offset: Int
    ): List<FullProductDetails>

    @Query("""
        SELECT COUNT(*) FROM $PRODUCTS_TABLE p 
        LEFT JOIN Departments d ON p.department_id = d.department_id 
        WHERE COALESCE(d.department_name, 'Unspecified') = :departmentName
    """)
    suspend fun getProductCountByDepartment(departmentName: String): Int

    @Transaction
    @Query("SELECT * FROM Products WHERE name LIKE '%' || :query || '%' LIMIT :limit")
    suspend fun searchProducts(query: String, limit: Int = 5): List<FullProductDetails>

    @Transaction
    @Query("""
        SELECT * FROM Products
        ORDER BY name ASC
        LIMIT :limit OFFSET :offset
    """)
    suspend fun getAllProductsWithDepartmentsPaginated(
        limit: Int,
        offset: Int
    ): List<FullProductDetails>
}