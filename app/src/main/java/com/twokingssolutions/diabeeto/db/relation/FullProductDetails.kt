package com.twokingssolutions.diabeeto.db.relation

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.twokingssolutions.diabeeto.db.entity.AllergyStatements
import com.twokingssolutions.diabeeto.db.entity.Departments
import com.twokingssolutions.diabeeto.db.entity.DietaryStatements
import com.twokingssolutions.diabeeto.db.entity.Product
import com.twokingssolutions.diabeeto.db.entity.ProductAllergyCrossRef
import com.twokingssolutions.diabeeto.db.entity.ProductDietaryCrossRef
import com.twokingssolutions.diabeeto.db.entity.NutritionalInformation
import kotlinx.serialization.Serializable

@Serializable
data class FullProductDetails(
    @Embedded val product: Product,
    @Relation(
        parentColumn = "department_id",
        entityColumn = "department_id"
    )
    val department: Departments?,
    @Relation(
        parentColumn = "product_id",
        entityColumn = "dietary_statement_id",
        associateBy = Junction(ProductDietaryCrossRef::class)
    )
    val dietaryStatements: List<DietaryStatements>,
    @Relation(
        parentColumn = "product_id",
        entityColumn = "allergy_statement_id",
        associateBy = Junction(ProductAllergyCrossRef::class)
    )
    val allergyStatements: List<AllergyStatements>,
    @Relation(
        parentColumn = "product_id",
        entityColumn = "product_id"
    )
    val nutritionalInformation: NutritionalInformation?,
    @Relation(
        entity = NutritionalInformation::class,
        parentColumn = "product_id",
        entityColumn = "product_id"
    )
    val nutritionalInfoWithValues: NutritionalInfoWithValues?
)
