package com.snofed.publicapp.membership.model

import com.snofed.publicapp.models.membership.Benefit

data class BuyMembership(
    val success: Boolean,
    val message: Any?,
    val data: List<BuyMembershipResponse>,
    val statusCode: Long,
    val totalItems: Long,
)

data class BuyMembershipResponse(
    val name: String,
    val nameTranslates: NameTranslates,
    val isActive: Boolean,
    val activeTo: String,
    val validity: String,
    val customSaleFrom: String,
    val customSaleTo: String,
    val price: Double,
    val priceWithCurrency: String,
    val description: String,
    val descriptionTranslates: DescriptionTranslates,
    val benefitsNames: List<String>,
    val benefits: List<Benefit>,
    val clientRef: String,
    val id: String,
    val createdDate: String,
    val syncAction: Long,
    val isAdminMembership: Boolean,
    val isProMembership: Boolean,
)

data class NameTranslates(
    val en: String,
    val sv: String,
    val de: Any?,
    val no: Any?,
)

data class DescriptionTranslates(
    val en: String,
    val sv: String,
    val de: Any?,
    val no: Any?,
)

/*data class Benefit(
    val id: String,
    val name: String,
    val description: String,
    val partnerLogoPath: String,
)*/
