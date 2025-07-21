package com.twokingssolutions.diabeeto.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable
import com.twokingssolutions.diabeeto.util.Constants.ALLERGY_STATEMENTS_TABLE

@Entity(
    tableName = ALLERGY_STATEMENTS_TABLE,
    indices = [
        Index(value = ["statement_name"], unique = true)
    ]
)
@Serializable
data class AllergyStatements(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "allergy_statement_id")
    val allergyStatementId: String,
    @ColumnInfo(name = "statement_name")
    val statementName: String
)