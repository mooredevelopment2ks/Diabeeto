package com.twokingssolutions.diabeeto.di

import com.twokingssolutions.diabeeto.viewModel.InsulinCalculatorViewModel
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val insulinCalculatorModule = module {
    singleOf(::InsulinCalculatorViewModel)
}