package mx.cbisystems.x24.networking

import android.util.Base64
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RestEngine {

    private val AUTH = "BASIC " + Base64.encodeToString("administrador@cbisystems.mx:cbi2020.".toByteArray(), Base64.NO_WRAP)
    private const val BASE_URL = "http://201.99.79.116:9090"

    private val okHttpClient = OkHttpClient.Builder()
            .addInterceptor { chain ->
                val original = chain.request()
                val requestBuilder = original.newBuilder()
                    .addHeader("Authorization", AUTH)
                    .addHeader("Device-Type", "1")
                    .method(original.method, original.body)
                val request = requestBuilder.build()
                chain.proceed(request)
            }.build()

    val instance: API by lazy {
        val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build()

        retrofit.create(API::class.java)
    }
}