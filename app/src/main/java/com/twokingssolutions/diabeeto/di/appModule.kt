package com.twokingssolutions.diabeeto.di

import com.twokingssolutions.diabeeto.repository.SettingsRepository
import com.twokingssolutions.diabeeto.viewModel.InsulinCalculatorViewModel
import com.twokingssolutions.diabeeto.viewModel.ProductDatabaseViewModel
import com.twokingssolutions.diabeeto.viewModel.SettingsViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    // Repositories
    single { SettingsRepository(androidContext()) }

    // ViewModels
    viewModel { ProductDatabaseViewModel(get()) }
    single { InsulinCalculatorViewModel(get()) }
    viewModel { SettingsViewModel(get()) }
}