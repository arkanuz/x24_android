package mx.cbisystems.x24.entities

import android.location.Location
import android.location.LocationManager
import android.os.Parcel
import android.os.Parcelable
import android.util.Log
import com.google.gson.annotations.SerializedName

class MStore : ArrayList<StoreItem>()

data class StoreItem(
    @SerializedName("id_store") val id_store : Int,
    val name : String,
    val address: String,
    val phone : String,
    val url_photo : String,
    var latitude : Double = 19.5272,
    var longitude : Double = -96.9231,
    val id_ciudad : Int,
    val create_date : String,
    val status : Int
) : Parcelable {
    constructor(parcel: Parcel): this (
            parcel.readInt(),
            parcel.readString().toString(),
            parcel.readString().toString(),
            parcel.readString().toString(),
            parcel.readString().toString(),
            parcel.readDouble(),
            parcel.readDouble(),
            parcel.readInt(),
            parcel.readString().toString(),
            parcel.readInt()
            ) {}

    override fun writeToParcel(parcel: Parcel?, flags: Int) {
        parcel?.writeInt(id_store)
        parcel?.writeString(name)
        parcel?.writeString(address)
        parcel?.writeString(phone)
        parcel?.writeString(url_photo)
        parcel?.writeDouble(latitude)
        parcel?.writeDouble(longitude)
        parcel?.writeInt(id_ciudad)
        parcel?.writeString(create_date)
        parcel?.writeInt(status)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<StoreItem>{
        override fun createFromParcel(parcel: Parcel): StoreItem {
            return StoreItem(parcel)
        }

        override fun newArray(size: Int): Array<StoreItem?> {
            return arrayOfNulls(size)
        }
    }

    fun distance(location: Location): Float{
        val toLocation = Location(LocationManager.GPS_PROVIDER)
        toLocation.latitude = this.latitude
        toLocation.longitude = this.longitude

        return location.distanceTo(toLocation)
    }

}