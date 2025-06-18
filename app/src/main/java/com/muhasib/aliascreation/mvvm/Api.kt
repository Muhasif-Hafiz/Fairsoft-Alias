package com.muhasib.aliascreation.mvvm

import com.muhasib.aliascreation.model.Account
import com.muhasib.aliascreation.model.AlternateNameResponse
import com.muhasib.aliascreation.model.DeleteAlternateNameResponse
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

//interface Api {
//    @GET("api/Pilloo/GetAccountsList/NADCf2025/All")
//    suspend fun getAccounts(): List<Account>
//}
interface Api {
    @GET("GetAccountsList/NADCf2025/All")
    suspend fun getAccounts(): retrofit2.Response<String>

    @GET("GetAlternateNamesById/{compCode}/{type}/{actId}")
    suspend fun getAlternateNamesById(
        @Path("compCode")  compCode : String,
        @Path("type"    )   type    : String,
        @Path("actId"   )  actId    : Int,
        @Header("x-api-key") apiKey: String

    ) : Response<AlternateNameResponse>

    @DELETE("DeleteAlternateName/{compCode}/{type}/{altId}")
    suspend fun deleteAlternateName(
        @Path("compCode") compCode: String,
        @Path("type") type: String,
        @Path("altId") altId: Int,
        @Header("x-api-key") apiKey: String
    )  : Response<DeleteAlternateNameResponse>
}
