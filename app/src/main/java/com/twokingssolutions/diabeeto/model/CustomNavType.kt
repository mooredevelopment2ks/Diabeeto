package com.twokingssolutions.diabeeto.model

import android.os.Bundle
import androidx.navigation.NavType
import kotlinx.serialization.json.Json
import android.net.Uri

object CustomNavType {

    val FoodType = object : NavType<Food>(
        isNullableAllowed = false
    ) {
        override fun get(bundle: Bundle, key: String): Food? {
            return Json.decodeFromString(bundle.getString(key) ?: return null)
        }

        override fun parseValue(value: String): Food {
            return Json.decodeFromString(Uri.decode(value))
        }

        override fun serializeAsValue(value: Food): String {
            return Uri.encode(Json.encodeToString(value))
        }

        override fun put(bundle: Bundle, key: String, value: Food) {
            bundle.putString(key, Json.encodeToString(value))
        }
    }

    val FoodListType = object : NavType<List<Food>>(
        isNullableAllowed = false
    ) {
        override fun get(bundle: Bundle, key: String): List<Food>? {
            return Json.decodeFromString(bundle.getString(key) ?: return null)
        }

        override fun parseValue(value: String): List<Food> {
            return Json.decodeFromString(Uri.decode(value))
        }

        override fun serializeAsValue(value: List<Food>): String {
            return Uri.encode(Json.encodeToString(value))
        }

        override fun put(bundle: Bundle, key: String, value: List<Food>) {
            bundle.putString(key, Json.encodeToString(value))
        }
    }
}