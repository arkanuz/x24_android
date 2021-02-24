package mx.cbisystems.x24.entities

import com.google.gson.annotations.SerializedName

data class MProfile(
    @SerializedName("ID") val idProfile: Int?,
    val nombres: String?,
    val apellidos: String?,
    val foto: String?,
    val puntos: Float?,
    val importe:Float?
)