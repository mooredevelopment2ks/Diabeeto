package com.twokingssolutions.diabeeto.repository

import com.twokingssolutions.diabeeto.db.dao.ProductDao
import com.twokingssolutions.diabeeto.db.dao.DietaryStatementsDao
import com.twokingssolutions.diabeeto.db.dao.DepartmentsDao
import com.twokingssolutions.diabeeto.db.dao.AllergyStatementsDao
import com.twokingssolutions.diabeeto.db.dao.NutrientValuesDao
import com.twokingssolutions.diabeeto.db.dao.NutritionalInformationDao
import com.twokingssolutions.diabeeto.db.dao.ProductDietaryCrossRefDao
import com.twokingssolutions.diabeeto.db.dao.ProductAllergyCrossRefDao
import com.twokingssolutions.diabeeto.db.entity.AllergyStatements
import com.twokingssolutions.diabeeto.db.entity.Departments
import com.twokingssolutions.diabeeto.db.entity.DietaryStatements
import com.twokingssolutions.diabeeto.db.entity.NutrientValues
import com.twokingssolutions.diabeeto.db.entity.NutritionalInformation
import com.twokingssolutions.diabeeto.db.entity.Product
import com.twokingssolutions.diabeeto.db.relation.FullProductDetails
import kotlinx.coroutines.flow.Flow
import java.util.UUID

class ProductRepository(
    private val productDao: ProductDao,
    private val dietaryStatementsDao: DietaryStatementsDao,
    private val departmentsDao: DepartmentsDao,
    private val allergyStatementsDao: AllergyStatementsDao,
    private val nutrientValuesDao: NutrientValuesDao,
    private val nutritionalInformationDao: NutritionalInformationDao,
    private val productDietaryCrossRefDao: ProductDietaryCrossRefDao,
    private val productAllergyCrossRefDao: ProductAllergyCrossRefDao
) {
    val products: Flow<List<FullProductDetails>> = productDao.getAllProducts()
    val favouriteProducts: Flow<List<FullProductDetails>> = productDao.getAllFavourites()

    suspend fun getProductById(productId: String): FullProductDetails? {
        return productDao.getFullProductDetailsById(productId)
    }

    suspend fun getProductByBarcode(barcode: String): FullProductDetails? {
        return productDao.getFullProductDetailsByBarcode(barcode)
    }

    suspend fun insertProduct(
        product: Product,
        nutritionalInformation: NutritionalInformation,
        nutrientValues: List<NutrientValues>,
        allergyIds: List<String>,
        dietaryIds: List<String>
    ) {
        productDao.insertFullProductTransaction(
            product = product,
            nutritionalInformation = nutritionalInformation,
            nutrientValues = nutrientValues,
            allergyIds = allergyIds,
            dietaryIds = dietaryIds,
            nutritionalInformationDao = nutritionalInformationDao,
            nutrientValuesDao = nutrientValuesDao,
            productAllergyCrossRefDao = productAllergyCrossRefDao,
            productDietaryCrossRefDao = productDietaryCrossRefDao
        )
    }

    suspend fun updateAllProductDetails(
        product: Product,
        nutritionalInformation: NutritionalInformation,
        nutrientValues: List<NutrientValues>,
        allergyIds: List<String>,
        dietaryIds: List<String>
    ) {
        productDao.insertFullProductTransaction(
            product = product,
            nutritionalInformation = nutritionalInformation,
            nutrientValues = nutrientValues,
            allergyIds = allergyIds,
            dietaryIds = dietaryIds,
            nutritionalInformationDao = nutritionalInformationDao,
            nutrientValuesDao = nutrientValuesDao,
            productAllergyCrossRefDao = productAllergyCrossRefDao,
            productDietaryCrossRefDao = productDietaryCrossRefDao
        )
    }

    suspend fun updateProduct(product: Product) {
        productDao.updateProduct(product)
    }

    suspend fun deleteProduct(product: Product) {
        productDao.deleteProduct(product)
    }

    suspend fun getAllAllergyStatements(): List<AllergyStatements> {
        return allergyStatementsDao.getAllAllergyStatementsList()
    }

    suspend fun getAllDietaryStatements(): List<DietaryStatements> {
        return dietaryStatementsDao.getAllDietaryStatementsList()
    }

    suspend fun getOrCreateDepartment(departmentName: String): String {
        var department = departmentsDao.getDepartmentByName(departmentName)
        if (department == null) {
            department = Departments(
                departmentId = UUID.randomUUID().toString(),
                departmentName = departmentName
            )
            departmentsDao.insertDepartment(department)
        }
        return department.departmentId
    }

    suspend fun getAllDepartments(): List<Departments> {
        return departmentsDao.getAllDepartments()
    }

    suspend fun getProductsByQueryPaginated(
        query: String,
        limit: Int,
        offset: Int
    ): List<FullProductDetails> {
        return productDao.getFullProductDetailsByQueryPaginated(query, limit, offset)
    }

    suspend fun getProductsByDepartmentPaginated(
        departmentName: String,
        limit: Int,
        offset: Int
    ): List<FullProductDetails> {
        return productDao.getFullProductDetailsByDepartmentPaginated(departmentName, limit, offset)
    }

    suspend fun getAllProductsWithDepartmentsPaginated(
        limit: Int,
        offset: Int
    ): List<FullProductDetails> {
        return productDao.getAllProductsWithDepartmentsPaginated(limit, offset)
    }

    suspend fun searchDepartments(query: String, limit: Int = 5) = departmentsDao.searchDepartments(query, limit)
    suspend fun searchProducts(query: String, limit: Int = 5) = productDao.searchProducts(query, limit)
}