package com.twokingssolutions.diabeeto.di

import com.twokingssolutions.diabeeto.repository.SettingsRepository
import com.twokingssolutions.diabeeto.viewModel.SettingsViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val preferencesModule = module {
    single { SettingsRepository(androidContext()) }
    viewModel { SettingsViewModel(get()) }
}