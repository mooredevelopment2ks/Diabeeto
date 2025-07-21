package com.twokingssolutions.diabeeto.model

import com.twokingssolutions.diabeeto.db.relation.FullProductDetails

data class ProductsByDepartment(
    val departmentName: String,
    val products: List<FullProductDetails>
)