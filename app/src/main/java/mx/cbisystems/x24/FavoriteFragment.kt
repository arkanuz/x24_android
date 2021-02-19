package mx.cbisystems.x24

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import com.squareup.picasso.Picasso
import mx.cbisystems.x24.entities.FavoriteItem
import mx.cbisystems.x24.entities.MFavorites
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
 * Use the [FavoriteFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class FavoriteFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    // Autogenerado
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    // Autogenerado
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favorite, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // LLamar un ReciclerView para poner los elementos
        val favoriteReciclerView = view.findViewById<RecyclerView>(R.id.favoriteReciclerView)
        // Meterlos en un layout horizontal
        favoriteReciclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        // paginar el scroll
        val snapHelper: SnapHelper = LinearSnapHelper()
        snapHelper.attachToRecyclerView(favoriteReciclerView)

        downloadFavorites()
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment FavoriteFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FavoriteFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    // Descargar las imagenes que se mostrarán
    fun downloadFavorites(){
        RestEngine.instance.listFavorites()
            .enqueue(object : Callback<MFavorites> {
                override fun onResponse(call: Call<MFavorites>, response: Response<MFavorites>) {
                    if (response.code() == 200) {
                        Log.i("Favorites", "conexión favoritos correcta: ${response.body()}")

                        val favorites = response.body()

                        val favoriteReciclerView =
                            this@FavoriteFragment.view?.findViewById<RecyclerView>(
                                R.id.favoriteReciclerView
                            )
                        favoriteReciclerView?.adapter = favorites?.let { FavoriteAdapter(it) }
                    } else {
                        Log.i("Favorites", "conexión favoritos realizada con error")
                    }
                }

                override fun onFailure(call: Call<MFavorites>, t: Throwable) {
                    Log.i("Favorites", "error en conexión favoritos: " + t.message)
                }
            })
    }
}

// Adaptador del visor de Favoritos. Se puede crear en otro archivo pero decidí dejarlo aquí
class FavoriteAdapter(val favorites: MFavorites) : RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.favorite_item, parent, false)
        return FavoriteViewHolder(view, null)
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        val favorite: FavoriteItem = favorites.get(position)
        var imageView = holder.itemView.findViewById<ImageView>(R.id.favoriteImageView)

        // Descargar y mostrar la imagen
        Picasso.get().load(favorite.urlImage).placeholder(R.drawable.welcome_1).error(R.drawable.welcome_2).into(imageView)
    }

    override fun getItemCount(): Int {
        val list: ArrayList<FavoriteItem> = favorites
        return list.size
    }

    // Clase generada para manejar más fácilmente los elementos que se mostrarán
    class FavoriteViewHolder(itemView: View, favoriteItem: FavoriteItem?) : RecyclerView.ViewHolder(itemView){

    }

}
