package mx.cbisystems.x24.networking

import android.text.Editable
import mx.cbisystems.x24.entities.*
import retrofit2.Call
import retrofit2.http.*

interface API {
    @Headers("Content-Type: application/json")
    @POST("/appX24/users")
    fun register(
            @Body register: MRegister
    ): Call<Void>

    @FormUrlEncoded
    @POST("/appX24/security/login")
    fun loginUser(
        @Field("username") username: String,
        @Field("password") password: String
    ):Call<MUser>

    @GET("/appX24/catalog/stores2")
    fun listStores(): Call<MStore>

    @GET("/appX24/catalog/home")
    fun listFavorites():Call<MFavorites>

    @GET("/appX24/catalog/promos")
    fun listPromos():Call<MPromos>

    @GET("/appX24/clientes/{userId}/perfil")
    fun profile(
        @Path("userId") userId: Int
    ):Call<MProfile>
}