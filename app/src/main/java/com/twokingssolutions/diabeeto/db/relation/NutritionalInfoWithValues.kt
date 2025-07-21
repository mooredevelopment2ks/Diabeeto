package com.twokingssolutions.diabeeto.db.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.twokingssolutions.diabeeto.db.entity.NutrientValues
import com.twokingssolutions.diabeeto.db.entity.NutritionalInformation
import kotlinx.serialization.Serializable

@Serializable
data class NutritionalInfoWithValues(
    @Embedded val nutritionalInformation: NutritionalInformation?,
    @Relation(
        parentColumn = "nutritional_info_id",
        entityColumn = "nutritional_info_id"
    )
    val nutrientValues: List<NutrientValues>
)
