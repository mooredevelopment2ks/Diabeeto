package com.twokingssolutions.diabeeto.di

import androidx.room.Room
import com.twokingssolutions.diabeeto.db.ProductsDatabase
import com.twokingssolutions.diabeeto.db.dao.DepartmentsDao
import com.twokingssolutions.diabeeto.db.dao.DietaryStatementsDao
import com.twokingssolutions.diabeeto.db.dao.ProductDao
import com.twokingssolutions.diabeeto.db.dao.AllergyStatementsDao
import com.twokingssolutions.diabeeto.db.dao.NutrientValuesDao
import com.twokingssolutions.diabeeto.db.dao.NutritionalInformationDao
import com.twokingssolutions.diabeeto.db.dao.ProductDietaryCrossRefDao
import com.twokingssolutions.diabeeto.db.dao.ProductAllergyCrossRefDao
import com.twokingssolutions.diabeeto.repository.ProductRepository
import com.twokingssolutions.diabeeto.util.Constants.PRODUCTS_DATABASE
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val databaseModule = module {
    single {
        Room.databaseBuilder(
            androidContext(),
            ProductsDatabase::class.java,
            PRODUCTS_DATABASE
        )
            .createFromAsset("productsIntUniqueRemovedDB.db")
            .build()
    }

    // DAOs
    single<ProductDao> { get<ProductsDatabase>().productDao() }
    single<DietaryStatementsDao> { get<ProductsDatabase>().dietaryStatementsDao() }
    single<DepartmentsDao> { get<ProductsDatabase>().departmentsDao() }
    single<AllergyStatementsDao> { get<ProductsDatabase>().allergyStatementsDao() }
    single<NutrientValuesDao> { get<ProductsDatabase>().nutrientValuesDao() }
    single<NutritionalInformationDao> { get<ProductsDatabase>().nutritionalInformationDao() }
    single<ProductDietaryCrossRefDao> { get<ProductsDatabase>().productDietaryCrossRefDao() }
    single<ProductAllergyCrossRefDao> { get<ProductsDatabase>().productAllergyCrossRefDao() }

    // Repository
    single {
        ProductRepository(
            productDao = get(),
            dietaryStatementsDao = get(),
            departmentsDao = get(),
            allergyStatementsDao = get(),
            nutrientValuesDao = get(),
            nutritionalInformationDao = get(),
            productDietaryCrossRefDao = get(),
            productAllergyCrossRefDao = get()
        )
    }
}