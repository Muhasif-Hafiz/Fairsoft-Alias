package com.muhasib.aliascreation.model

data class Item(val name: String, val id: Int) {
    override fun toString(): String = name
}