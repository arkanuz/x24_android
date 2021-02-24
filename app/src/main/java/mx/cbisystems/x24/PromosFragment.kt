package mx.cbisystems.x24

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import com.irozon.alertview.AlertActionStyle
import com.irozon.alertview.AlertStyle
import com.irozon.alertview.AlertView
import com.irozon.alertview.objects.AlertAction
import com.squareup.picasso.Picasso
import mx.cbisystems.x24.entities.FavoriteItem
import mx.cbisystems.x24.entities.MFavorites
import mx.cbisystems.x24.entities.MPromos
import mx.cbisystems.x24.entities.PromosItem
import mx.cbisystems.x24.networking.RestEngine
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [PromosFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PromosFragment : Fragment() {
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
        return inflater.inflate(R.layout.fragment_promos, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // LLamar un RecyclerView para poner los elementos
        val promoRecyclerView = view.findViewById<RecyclerView>(R.id.promosRecyclerView)
        // Meterlos en un layout horizontal
        promoRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        // paginar el scroll
        val snapHelper: SnapHelper = LinearSnapHelper()
        snapHelper.attachToRecyclerView(promoRecyclerView)

        downloadPromos()
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment PromosFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            PromosFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    // Descargar las imagenes que se mostrarán
    fun downloadPromos(){
        RestEngine.instance.listPromos()
            .enqueue(object : Callback<MPromos> {
                override fun onResponse(call: Call<MPromos>, response: Response<MPromos>) {
                    if (response.code() == 200) {
                        Log.i("Promos", "conexión promos correcta: ${response.body()}")

                        val promos = response.body()

                        val promosRecyclerView =
                            this@PromosFragment.view?.findViewById<RecyclerView>(
                                R.id.promosRecyclerView
                            )
                        promosRecyclerView?.adapter = promos?.let { PromosAdapter(it) }
                    } else {
                        Log.i("Promos", "conexión promos realizada con error")
                        val alert = AlertView("No se pudo establecer conexión con el servidor", "Verifique su conexión a internet", AlertStyle.DIALOG)
                        alert.addAction(AlertAction("Aceptar", AlertActionStyle.DEFAULT, { action ->
// Action 1 callback
                        }))
                        alert.show(activity as AppCompatActivity)
                    }
                }

                override fun onFailure(call: Call<MPromos>, t: Throwable) {
                    Log.i("Promos", "error en conexión promos: " + t.message)
                    val alert = AlertView("No se pudo establecer conexión con el servidor", "Verifique que el servidor está encendido y se encuentra funcionando correctamente", AlertStyle.DIALOG)
                    alert.addAction(AlertAction("Aceptar", AlertActionStyle.DEFAULT, { action ->
// Action 1 callback
                    }))
                    alert.show(activity as AppCompatActivity)
                }
            })
    }
}

// Adaptador del visor de Promos. Se puede crear en otro archivo pero decidí dejarlo aquí
class PromosAdapter(val promos: MPromos) : RecyclerView.Adapter<PromosAdapter.PromosViewHolder>(){
    class PromosViewHolder (itemView: View, promosItem: PromosItem?): RecyclerView.ViewHolder(itemView){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PromosViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.promo_item, parent, false)
        return PromosAdapter.PromosViewHolder(view, null)
    }

    override fun onBindViewHolder(holder: PromosViewHolder, position: Int) {
        val promo: PromosItem = promos.get(position)
        var imageView = holder.itemView.findViewById<ImageView>(R.id.promoImageView)

        // Descargar y mostrar la imagen
        Picasso.get().load(promo.urlImage).placeholder(R.drawable.welcome_1).error(R.drawable.welcome_2).into(imageView)
    }

    override fun getItemCount(): Int {
        val list: ArrayList<PromosItem> = promos
        return list.size
    }

}