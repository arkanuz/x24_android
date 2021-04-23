package mx.cbisystems.x24

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.irozon.alertview.AlertActionStyle
import com.irozon.alertview.AlertStyle
import com.irozon.alertview.AlertView
import com.irozon.alertview.objects.AlertAction
import mx.cbisystems.x24.entities.MStore
import mx.cbisystems.x24.entities.StoreItem
import mx.cbisystems.x24.networking.RestEngine
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

private const val PERMISSION_REQUEST = 10

/**
 * A simple [Fragment] subclass.
 * Use the [StoresFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class StoresFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private var stores: MStore? = null

    // Variables para permisos de localización
    lateinit var locationManager: LocationManager
    private var hasGps = false
    private var hasNetwork = false
    private var locationGps: Location? = null
    private var locationNetwork: Location? = null
    private var permissions = arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

        // Solicitar/verificar permisos de localización
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkPermission(permissions)) {
                getLocation()
            } else {
                requestPermissions(permissions, PERMISSION_REQUEST)
            }
        } else {

        }
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_stores, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // LLamar un RecyclerView para poner los elementos
        val storesRecyclerView = view.findViewById<RecyclerView>(R.id.storesRecyclerView)
        // Meterlos en un layout horizontal
        storesRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        downloadStores()
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment StoresFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            StoresFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    // Descargar las tiendas
    fun downloadStores(){
        RestEngine.instance.listStores().enqueue(object : Callback<MStore> {
            override fun onResponse(call: Call<MStore>, response: Response<MStore>) {
                if (response.code() == 200) {
                    Log.i("Stores", "conexión stores correcta: ${response.body()}")

                    stores = response.body()

                    if (stores != null) {
                        showStoresSorted()
                    }
                } else {
                    Log.i("Stores", "conexión promos realizada con error")
                    val alert = AlertView("Error en  el servidor", "Ocurrió un problema en el servidor, por favor intente más tarde.", AlertStyle.DIALOG)
                    alert.addAction(AlertAction("Aceptar", AlertActionStyle.DEFAULT, { action ->
// Action 1 callback
                    }))
                    alert.show(activity as AppCompatActivity)
                }
            }

            override fun onFailure(call: Call<MStore>, t: Throwable) {
                Log.i("Stores", "No hay conexión promos")
                val alert = AlertView("Error al conectarr", "Ocurrió un problema con la conexión, por favor verifique su internet.", AlertStyle.DIALOG)
                alert.addAction(AlertAction("Aceptar", AlertActionStyle.DEFAULT, { action ->
// Action 1 callback
                }))
                alert.show(activity as AppCompatActivity)
            }

        })
    }

    // Ordena las tiendas en base a la cercania del dispositivo, en caso de poderse
    private fun showStoresSorted(){
        val storesRecyclerView: RecyclerView? = this@StoresFragment.view?.findViewById(R.id.storesRecyclerView)
        if (locationGps != null && stores != null){
            stores!!.sortWith(compareBy{it.distance(locationGps!!)})
            storesRecyclerView?.adapter = stores?.let { StoresAdapter(it) }
        }
        else if (locationNetwork != null && stores != null){
            stores!!.sortWith(compareBy{it.distance(locationNetwork!!)})
        }
        storesRecyclerView?.adapter = stores?.let { StoresAdapter(it) }
    }

    // LOCATION METHODS

    @SuppressLint("MissingPermission")
    private fun getLocation() {
        locationManager = context?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        hasGps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        hasNetwork = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        if (hasGps || hasNetwork) {

            if (hasGps) {
                Log.d("CodeAndroidLocation", "hasGps")
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 100F, object : LocationListener {
                    override fun onLocationChanged(location: Location) {
                        if (location != null) {
                            locationGps = location
                            Log.d("CodeAndroidLocation", " GPS Latitude : " + locationGps!!.latitude)
                            Log.d("CodeAndroidLocation", " GPS Longitude : " + locationGps!!.longitude)

                            if (stores != null){
                                showStoresSorted()
                            }
                        }
                    }

                    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {

                    }

                })

                val localGpsLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                if (localGpsLocation != null)
                    locationGps = localGpsLocation
            }
            else if (hasNetwork) {
                Log.d("CodeAndroidLocation", "hasGps")
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 0F, object : LocationListener {
                    override fun onLocationChanged(location: Location) {
                        if (location != null) {
                            locationNetwork = location
                            Log.d("CodeAndroidLocation", " Network Latitude : " + locationNetwork!!.latitude)
                            Log.d("CodeAndroidLocation", " Network Longitude : " + locationNetwork!!.longitude)
                        }

                        if (stores != null){
                            showStoresSorted()
                        }
                    }

                    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {

                    }

                })

                val localNetworkLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
                if (localNetworkLocation != null)
                    locationNetwork = localNetworkLocation
            }

            else if(locationGps!= null && locationNetwork!= null){
                if(locationGps!!.accuracy > locationNetwork!!.accuracy){
                    Log.d("CodeAndroidLocation", " Network Latitude : " + locationNetwork!!.latitude)
                    Log.d("CodeAndroidLocation", " Network Longitude : " + locationNetwork!!.longitude)

                    if (stores != null){
                        showStoresSorted()
                    }
                }else{
                    Log.d("CodeAndroidLocation", " GPS Latitude : " + locationGps!!.latitude)
                    Log.d("CodeAndroidLocation", " GPS Longitude : " + locationGps!!.longitude)

                    if (stores != null){
                        showStoresSorted()
                    }
                }
            }

        } else {
            startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
        }
    }

    private fun checkPermission(permissionArray: Array<String>): Boolean {
        var allSuccess = true
        for (i in permissionArray.indices) {
            if (context?.checkCallingOrSelfPermission(permissionArray[i]) == PackageManager.PERMISSION_DENIED)
                allSuccess = false
        }
        return allSuccess
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST) {
            var allSuccess = true
            for (i in permissions.indices) {
                if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                    allSuccess = false
                    val requestAgain = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && shouldShowRequestPermissionRationale(permissions[i])
                    if (requestAgain) {
                        Toast.makeText(context, "Permission denied", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, "Go to settings and enable the permission", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            if (allSuccess)
                getLocation()
        }
    }
}

// Stores Adapter

class StoresAdapter(val stores: MStore): RecyclerView.Adapter<StoresAdapter.StoresViewHolder>(){

    class StoresViewHolder(itemView: View, storesItem: StoreItem?):RecyclerView.ViewHolder(itemView){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoresViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.store_item, parent, false)

        return StoresAdapter.StoresViewHolder(view, null)
    }

    override fun onBindViewHolder(holder: StoresViewHolder, position: Int) {
        // Cambiar color de fondo de acuerdo a lugar
        var layout = holder.itemView.findViewById<ConstraintLayout>(R.id.storeItemLayout)
        if (position%2 == 0){
            layout.setBackgroundResource(R.color.hardRed)
        } else {
            layout.setBackgroundResource(R.color.orientalPink)
        }

        // Indicar nombre
        val store = stores[position]
        var nameTextView = holder.itemView.findViewById<TextView>(R.id.storeNameTextView)
        nameTextView.text = store.name

        layout.setOnClickListener {
            Log.i("Stores", "click en item ${store.name}: ${position}")

            val context = holder.itemView.context
            val intent = Intent(context, StoreActivity::class.java)

            intent.putExtra("store", store)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return stores.count()
    }

}