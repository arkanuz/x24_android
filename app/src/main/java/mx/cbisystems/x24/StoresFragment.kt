package mx.cbisystems.x24

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
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
import java.io.Serializable

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [StoresFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class StoresFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
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
        RestEngine.instance.listStores().enqueue(object : Callback<MStore>{
            override fun onResponse(call: Call<MStore>, response: Response<MStore>) {
                if (response.code() == 200){
                    Log.i("Stores", "conexión stores correcta: ${response.body()}")

                    val stores = response.body()
                    if (stores != null){
                        val storesRecyclerView: RecyclerView? = this@StoresFragment.view?.findViewById(R.id.storesRecyclerView)
                        storesRecyclerView?.adapter = stores?.let { StoresAdapter(it) }
                    }
                }
                else {
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
}

class StoresAdapter(val stores: MStore): RecyclerView.Adapter<StoresAdapter.StoresViewHolder>(){

    class StoresViewHolder (itemView: View, storesItem: StoreItem?):RecyclerView.ViewHolder(itemView){

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