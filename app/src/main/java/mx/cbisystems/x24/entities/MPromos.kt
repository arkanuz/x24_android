package mx.cbisystems.x24.entities

import com.google.gson.annotations.SerializedName

class MPromos: ArrayList<PromosItem>()

data class  PromosItem(
    @SerializedName("FechaInicio") val fechaInicio: String,
    @SerializedName("FechaFin") val fechaFin: String,
    @SerializedName("Prioridad") val prioridad: Int,
    @SerializedName("Tipo") val tipo: String,
    @SerializedName("url_image") val urlImage: String,
    @SerializedName("IdPromocion") val idPromocion: Int,
    @SerializedName("Descripcion") val descripcion: String,
    @SerializedName("FechaActualizacion") val fechaActualizacion: String,
)