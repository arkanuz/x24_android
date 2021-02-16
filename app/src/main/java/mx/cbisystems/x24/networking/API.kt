package mx.cbisystems.x24.networking

import mx.cbisystems.x24.entities.MStore
import mx.cbisystems.x24.entities.MUser
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface API {
    @GET("/appX24/catalog/stores2")
    fun listStores(): Call<List<MStore>>

    @FormUrlEncoded
    @POST("/appX24/security/login")
    fun loginUser(
        @Field("username") username: String,
        @Field("password") password: String
    ):Call<MUser>
}