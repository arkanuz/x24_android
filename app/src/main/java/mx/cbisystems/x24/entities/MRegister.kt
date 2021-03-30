package mx.cbisystems.x24.entities

import com.google.gson.annotations.SerializedName

data class MRegister(
        @SerializedName("userId") var user_id : Int?,
        var name : String?,
        var lastName : String?,
        var email : String?,
        var phone : String?,
        var idProvider : Int?,
        var birthDate : String?,
        var sex : Int?,
        var username : String?,
        var password : String?,
        var status : Int?,
        var image : String?,
        var ltppAccepted : Int?,
        var role : Int?
)
