package com.twokingssolutions.diabeeto.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import com.twokingssolutions.diabeeto.util.Constants.PRODUCT_ALLERGY_STATEMENT_TABLE
import kotlinx.serialization.Serializable

@Entity(
    tableName = PRODUCT_ALLERGY_STATEMENT_TABLE,
    primaryKeys = ["product_id", "allergy_statement_id"],
    foreignKeys = [
        ForeignKey(
            entity = Product::class,
            parentColumns = ["product_id"],
            childColumns = ["product_id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = AllergyStatements::class,
            parentColumns = ["allergy_statement_id"],
            childColumns = ["allergy_statement_id"],
            onDelete = ForeignKey.CASCADE
        )
                  ],
    indices = [
        Index(value = ["product_id", "allergy_statement_id"], unique = true),
        Index(value = ["product_id"]),
        Index(value = ["allergy_statement_id"])
    ]
)
@Serializable
data class ProductAllergyCrossRef(
    @ColumnInfo(name = "product_id")
    val productId: String,
    @ColumnInfo(name = "allergy_statement_id")
    val allergyStatementId: String
)