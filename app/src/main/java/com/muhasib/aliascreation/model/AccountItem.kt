package com.muhasib.aliascreation.model

data class AccountItem(
    val id       : String,
    val name     : String,
    val type     : String,
    val aliases  : List<String>
)
