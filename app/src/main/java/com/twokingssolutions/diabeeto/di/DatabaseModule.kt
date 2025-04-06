package com.twokingssolutions.diabeeto.di

import com.twokingssolutions.diabeeto.repository.FoodRepository
import com.twokingssolutions.diabeeto.viewModel.FoodDatabaseViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import org.koin.core.module.dsl.viewModelOf

val databaseModule = module {
    single { provideDatabase(androidContext()) }
    single { provideDao(get()) }
    factory { FoodRepository(get()) }
    viewModelOf(::FoodDatabaseViewModel)
}