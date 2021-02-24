package mx.cbisystems.x24.entities

import com.google.gson.annotations.SerializedName

class MStore : ArrayList<StoreItem>()

data class StoreItem(
    @SerializedName("id_store") val id_store : Int,
    val name : String,
    val address: String,
    val phone : String,
    val url_photo : String,
    val latitude : Double,
    val longitude : Double,
    val id_ciudad : Int,
    val create_date : String,
    val status : Int
)