package mx.cbisystems.x24.entities

import com.google.gson.annotations.SerializedName

class MFavorites: ArrayList<FavoriteItem>()

data class FavoriteItem (
    @SerializedName("id") val idFavorite: Int,
    @SerializedName("create_date") val createDate: String,
    @SerializedName("url_action") val urlAction: String,
    @SerializedName("url_image") val urlImage: String,
    val order: Int,
    val status: Int,
    val image: String
    )