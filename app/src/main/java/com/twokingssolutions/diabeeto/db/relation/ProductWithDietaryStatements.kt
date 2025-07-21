package com.twokingssolutions.diabeeto.db.relation

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.twokingssolutions.diabeeto.db.entity.DietaryStatements
import com.twokingssolutions.diabeeto.db.entity.Product
import com.twokingssolutions.diabeeto.db.entity.ProductDietaryCrossRef
import kotlinx.serialization.Serializable

@Serializable
data class ProductWithDietaryStatements(
    @Embedded val product: Product,
    @Relation(
        parentColumn = "product_id",
        entityColumn = "dietary_statement_id",
        associateBy = Junction(ProductDietaryCrossRef::class)
    )
    val dietaryStatements: List<DietaryStatements>
)
