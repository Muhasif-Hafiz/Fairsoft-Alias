package com.muhasib.aliascreation.model

import com.google.gson.annotations.SerializedName

data class Account(
    @SerializedName("actId") val actId: Int,
    @SerializedName("actName") val actName: String,
    val actCode: String,
    val actType: String,
    val phone: String,
    val mobile: String,
    val emailId: String,
    val alias1: String,
    val alias2: String,
    val alias3: String,
    val alias4: String,
    val alterName: String
)