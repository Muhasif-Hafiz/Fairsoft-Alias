package com.muhasib.aliascreation.mvvm

import com.muhasib.aliascreation.model.Account

//class AccountRepository(
//    private val api: Api
//) {
//    suspend fun getAccountNames(): List<String> {
//        return api.getAccounts().map { it.actName }
//    }
//}
class AccountRepository(
    private val api: Api
) {
    private val gson = com.google.gson.Gson()

    suspend fun getAccountNames(): List<String> {
        val response = api.getAccounts()
        return if (response.isSuccessful) {
            val jsonArrayString = response.body() ?: "[]"
            val listType = object : com.google.gson.reflect.TypeToken<List<Account>>() {}.type
            val accounts = gson.fromJson<List<Account>>(jsonArrayString, listType)
            accounts.map { it.actName }
        } else {
            throw Exception("Failed with code: ${response.code()}")
        }
    }
}

