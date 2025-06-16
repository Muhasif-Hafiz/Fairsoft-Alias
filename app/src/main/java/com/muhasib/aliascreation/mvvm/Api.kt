package com.muhasib.aliascreation.mvvm

import com.muhasib.aliascreation.model.Account
import retrofit2.http.GET

//interface Api {
//    @GET("api/Pilloo/GetAccountsList/NADCf2025/All")
//    suspend fun getAccounts(): List<Account>
//}
interface Api {
    @GET("api/Pilloo/GetAccountsList/NADCf2025/All")
    suspend fun getAccounts(): retrofit2.Response<String>
}
