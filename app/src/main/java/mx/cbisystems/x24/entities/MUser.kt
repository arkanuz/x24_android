package mx.cbisystems.x24.entities

import android.content.ContentValues
import android.util.Log
import mx.cbisystems.x24.networking.AdminSQLiteOpenHelper

data class MUser (
    var user_id : Int?,
    var name : String?,
    var lastName : String?,
    var email : String?,
    var phone : String?,
    var idProvider : Int?,
    var birthDate : String?,
    var sex : Int?,
    var userName : String?,
    var password : String?
) {
    constructor(): this(null, null, null, null, null, null, null, null, null, null){

    }
}