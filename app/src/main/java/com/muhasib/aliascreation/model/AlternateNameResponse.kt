package com.muhasib.aliascreation.model

data class AlternateNameResponse(
    val message: String,
    val data: List<AlternateNameData>
)

data class AlternateNameData(
    val altId: Int,
    val actName: String,
    val actId: Int,
    val gstNumber: String,
    val createdDate: String
)