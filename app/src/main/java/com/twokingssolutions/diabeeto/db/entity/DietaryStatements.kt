package com.twokingssolutions.diabeeto.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable
import com.twokingssolutions.diabeeto.util.Constants.DIETARY_STATEMENTS_TABLE

@Entity(
    tableName = DIETARY_STATEMENTS_TABLE,
    indices = [
        Index(value = ["statement_name"], unique = true)
    ]
)
@Serializable
data class DietaryStatements(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "dietary_statement_id")
    val dietaryStatementId: String,
    @ColumnInfo(name = "statement_name")
    val statementName: String
)