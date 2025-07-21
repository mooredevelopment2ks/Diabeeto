package com.twokingssolutions.diabeeto.db.entity

import androidx.room.ColumnInfo
import com.twokingssolutions.diabeeto.util.Constants.PRODUCT_DIETARY_STATEMENT_TABLE
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import kotlinx.serialization.Serializable

@Entity(
    tableName = PRODUCT_DIETARY_STATEMENT_TABLE,
    primaryKeys = ["product_id", "dietary_statement_id"],
    foreignKeys = [
        ForeignKey(
            entity = Product::class,
            parentColumns = ["product_id"],
            childColumns = ["product_id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = DietaryStatements::class,
            parentColumns = ["dietary_statement_id"],
            childColumns = ["dietary_statement_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["product_id", "dietary_statement_id"], unique = true),
        Index(value = ["product_id"]),
        Index(value = ["dietary_statement_id"])
    ]
)
@Serializable
data class ProductDietaryCrossRef(
    @ColumnInfo(name = "product_id")
    val productId: String,
    @ColumnInfo(name = "dietary_statement_id")
    val dietaryStatementId: String
)