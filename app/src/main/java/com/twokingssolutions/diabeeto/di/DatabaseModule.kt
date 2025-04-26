package com.twokingssolutions.diabeeto.di

import com.twokingssolutions.diabeeto.repository.FoodRepository
import com.twokingssolutions.diabeeto.viewModel.FoodDatabaseViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val databaseModule = module {
    single { provideDatabase(androidContext()) }
    single { provideDao(get()) }
    factory { FoodRepository(get()) }
    viewModel { FoodDatabaseViewModel(get()) }
}