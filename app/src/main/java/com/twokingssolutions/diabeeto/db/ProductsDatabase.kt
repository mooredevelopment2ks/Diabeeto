package com.twokingssolutions.diabeeto.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.twokingssolutions.diabeeto.db.dao.AllergyStatementsDao
import com.twokingssolutions.diabeeto.db.dao.DepartmentsDao
import com.twokingssolutions.diabeeto.db.dao.DietaryStatementsDao
import com.twokingssolutions.diabeeto.db.dao.NutrientValuesDao
import com.twokingssolutions.diabeeto.db.dao.NutritionalInformationDao
import com.twokingssolutions.diabeeto.db.dao.ProductAllergyCrossRefDao
import com.twokingssolutions.diabeeto.db.dao.ProductDao
import com.twokingssolutions.diabeeto.db.dao.ProductDietaryCrossRefDao
import com.twokingssolutions.diabeeto.db.entity.AllergyStatements
import com.twokingssolutions.diabeeto.db.entity.Departments
import com.twokingssolutions.diabeeto.db.entity.DietaryStatements
import com.twokingssolutions.diabeeto.db.entity.NutrientValues
import com.twokingssolutions.diabeeto.db.entity.NutritionalInformation
import com.twokingssolutions.diabeeto.db.entity.Product
import com.twokingssolutions.diabeeto.db.entity.ProductAllergyCrossRef
import com.twokingssolutions.diabeeto.db.entity.ProductDietaryCrossRef

@Database(
    entities = [
        Product::class,
        DietaryStatements::class,
        Departments::class,
        AllergyStatements::class,
        NutrientValues::class,
        NutritionalInformation::class,
        ProductDietaryCrossRef::class,
        ProductAllergyCrossRef::class
    ],
    version = 1,
    exportSchema = false // Set to true if you want to export the schema for migrations
)
abstract class ProductsDatabase: RoomDatabase(){
    abstract fun productDao(): ProductDao
    abstract fun dietaryStatementsDao(): DietaryStatementsDao
    abstract fun departmentsDao(): DepartmentsDao
    abstract fun allergyStatementsDao(): AllergyStatementsDao
    abstract fun nutrientValuesDao(): NutrientValuesDao
    abstract fun nutritionalInformationDao(): NutritionalInformationDao
    abstract fun productDietaryCrossRefDao(): ProductDietaryCrossRefDao
    abstract fun productAllergyCrossRefDao(): ProductAllergyCrossRefDao
}