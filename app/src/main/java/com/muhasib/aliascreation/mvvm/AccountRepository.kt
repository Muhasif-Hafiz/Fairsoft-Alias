package com.muhasib.aliascreation.mvvm

import android.util.Log
import com.muhasib.aliascreation.model.Account
import com.muhasib.aliascreation.model.AlternateNameResponse
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class AccountRepository(private val api: Api) {
    private val gson = Gson()
    private val apiKey = "JmSwTvNvDgKrPrRmSpSbJb"

    suspend fun getAccountNames(): List<Account> {
        val response = api.getAccounts()
        return if (response.isSuccessful) {
            val jsonArrayString = response.body() ?: "[]"
            val listType = object : TypeToken<List<Account>>() {}.type
            gson.fromJson<List<Account>>(jsonArrayString, listType) ?: emptyList()
        } else {
            throw Exception("Failed to fetch accounts: HTTP ${response.code()} - ${response.message()}")
        }
    }

    suspend fun getAlternateNamesById(compCode: String, type: String, actId: Int): AlternateNameResponse {
        val response = api.getAlternateNamesById(compCode, type, actId, apiKey)


        if (response.isSuccessful) {
            Log.d("Testing",response.body().toString())
            return response.body() ?: throw Exception("Empty response body")
        } else {
            throw Exception("Failed : ${response.message()}")
        }
    }
}