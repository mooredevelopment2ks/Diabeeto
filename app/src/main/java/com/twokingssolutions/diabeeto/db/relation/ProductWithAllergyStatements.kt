package com.twokingssolutions.diabeeto.db.relation

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.twokingssolutions.diabeeto.db.entity.AllergyStatements
import com.twokingssolutions.diabeeto.db.entity.Product
import com.twokingssolutions.diabeeto.db.entity.ProductAllergyCrossRef
import kotlinx.serialization.Serializable

@Serializable
data class ProductWithAllergyStatements(
    @Embedded val product: Product,
    @Relation(
        parentColumn = "product_id",
        entityColumn = "allergy_statement_id",
        associateBy = Junction(ProductAllergyCrossRef::class)
    )
    val allergyStatements: List<AllergyStatements>
)
