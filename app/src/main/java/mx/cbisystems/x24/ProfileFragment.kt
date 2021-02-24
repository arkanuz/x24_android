package mx.cbisystems.x24

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.irozon.alertview.AlertActionStyle
import com.irozon.alertview.AlertStyle
import com.irozon.alertview.AlertView
import com.irozon.alertview.objects.AlertAction
import mx.cbisystems.x24.entities.MProfile
import mx.cbisystems.x24.entities.MPromos
import mx.cbisystems.x24.networking.AdminSQLiteOpenHelper
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
 * Use the [ProfileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProfileFragment : Fragment() {
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

        downloadProfile()

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var inflater = LayoutInflater.from(activity)
        var mainView = inflater.inflate(
            R.layout.fragment_profile,
            view.findViewById(R.id.profileScrollView),
            false
        )

        val exitButton = view.findViewById<Button>(R.id.profileExitButton)
        exitButton.setOnClickListener {
            // Borrar usuario
            val context = activity
            val db = context?.let { AdminSQLiteOpenHelper(it) }
            db?.deleteUser()

            // Regresar a la pantalla de welcome
            val intent = Intent(activity as AppCompatActivity, WelcomeActivity::class.java)
            startActivity(intent)
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ProfileFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ProfileFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    // Descargar el perfil del usuario
    fun downloadProfile() {
        val context = activity
        val db = context?.let { AdminSQLiteOpenHelper(it) }
        val user = db?.getUser()

        RestEngine.instance.profile(user!!.user_id!!).enqueue(object : Callback<MProfile> {
            override fun onResponse(call: Call<MProfile>, response: Response<MProfile>) {
                if (response.code() == 200){
                    val profile: MProfile? = response.body()
                    if (profile != null){
                        var nameTextView: TextView? = view?.findViewById(R.id.profileNameTextView)
                        var pointsTextView: TextView? = view?.findViewById(R.id.profilePointsTextView)

                        nameTextView!!.text = profile.nombres + " " + profile.apellidos
                        pointsTextView!!.text = profile.puntos.toString()
                    }
                }
                else {
                    val alert = AlertView("Error al descargar monedero", "Ocurrió un problema al conectarse con el server, por favor intente más tarde.", AlertStyle.DIALOG)
                    alert.addAction(AlertAction("Aceptar", AlertActionStyle.DEFAULT, { action ->
// Action 1 callback
                    }))
                    alert.show(activity as AppCompatActivity)
                }
            }

            override fun onFailure(call: Call<MProfile>, t: Throwable) {
                Log.i("Perfil", "error en conexión perfil: ${t.message}")
                val alert = AlertView("Error al descargar monedero", "No se pudo realizar la conexión con el server, por favor revise su conexión a internet.", AlertStyle.DIALOG)
                alert.addAction(AlertAction("Aceptar", AlertActionStyle.DEFAULT, { action ->
// Action 1 callback
                }))
                alert.show(activity as AppCompatActivity)
            }

        })
    }
}