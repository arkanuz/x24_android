package mx.cbisystems.x24

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import mx.cbisystems.x24.networking.AdminSQLiteOpenHelper
import mx.cbisystems.x24.networking.LoadingDialog


@Suppress("DEPRECATION")
class WelcomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        this.supportActionBar?.hide()
        setContentView(R.layout.activity_welcome)

        // LLamar un RecyclerView para poner los elementos
        val welcomeRecyclerView = findViewById<RecyclerView>(R.id.welcomeRecyclerView)
        welcomeRecyclerView.adapter = WelcomeAdapter()
        // Meterlos en un layout horizontal
        welcomeRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        // paginar el scroll
        val snapHelper: SnapHelper = LinearSnapHelper()
        snapHelper.attachToRecyclerView(welcomeRecyclerView)
    }

    // Interceptamos el back para no permitir retroceder pantalla
    override fun onBackPressed() {

    }

    override fun onResume() {
        super.onResume()

        val db = AdminSQLiteOpenHelper(this)
        val user = db.getUser()
        if (user != null){
            val intent = Intent(this, TablayoutActivity::class.java)
            startActivity(intent)
        }
    }
}

class WelcomeAdapter(): RecyclerView.Adapter<WelcomeAdapter.WelcomeViewHolder>(){

    companion object {
        private const val WELCOME_ITEM_TYPE_MAIN = 0
        private const val WELCOME_ITEM_TYPE_BASE = 1
    }

    class WelcomeViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WelcomeViewHolder {
        return when (viewType){
            WELCOME_ITEM_TYPE_MAIN -> {
                val view: View = LayoutInflater.from(parent.context).inflate(R.layout.welcome_main_item_layout, parent, false)
                WelcomeViewHolder(view)
            }
            else -> {
                val view: View = LayoutInflater.from(parent.context).inflate(R.layout.welcome_item_layout, parent, false)
                WelcomeViewHolder(view)
            }
        }

    }

    override fun onBindViewHolder(holder: WelcomeViewHolder, position: Int) {
        if (holder.itemViewType == WELCOME_ITEM_TYPE_MAIN){
            val loginMainbutton = holder.itemView.findViewById<Button>(R.id.welcomeLoginMainButton)
            loginMainbutton.setOnClickListener {
                openLogin(holder.itemView.context)
            }

            val registerButton = holder.itemView.findViewById<Button>(R.id.welcomeRegisterMainButton)
            registerButton.setOnClickListener {
                openRegister(holder.itemView.context)
            }
        }

        else {
            var titleTextView = holder.itemView.findViewById<TextView>(R.id.titleTextView)
            var detailTextView = holder.itemView.findViewById<TextView>(R.id.detailTextView)
            var imageView = holder.itemView.findViewById<ImageView>(R.id.backgroundWelcomeImageView)

            when (position){
                1 -> {
                    titleTextView.setText("Recargas con \ncódigos QR")
                    detailTextView.setText("Garantizamos tu privacidad, haz recargas \nsin tener que dictar tu número.")
                    imageView.setImageResource(R.drawable.welcome_2)
                }
                2 -> {
                    titleTextView.setText("Acumula puntos \ncanjeables")
                    detailTextView.setText("Por cada compra recibe una bonificación \nque se acumula en tu código QR.")
                    imageView.setImageResource(R.drawable.welcome_3)
                }
                3 -> {
                    titleTextView.setText("No sufras por no encontrarnos")
                    detailTextView.setText("Ubica la sucursal más cercana a ti.")
                    imageView.setImageResource(R.drawable.welcome_4)
                }
            }

            val loginMainbutton = holder.itemView.findViewById<Button>(R.id.welcomeLoginButton)
            loginMainbutton.setOnClickListener {
                openLogin(holder.itemView.context)
            }

            val registerButton = holder.itemView.findViewById<Button>(R.id.welcomeRegisterButton)
            registerButton.setOnClickListener {
                openRegister(holder.itemView.context)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (position) {
            0 -> WELCOME_ITEM_TYPE_MAIN
            else -> WELCOME_ITEM_TYPE_BASE
        }
    }

    override fun getItemCount(): Int {
        return 4
    }

    fun openLogin(context: Context) {
        val intent = Intent(context, LoginActivity::class.java)
        intent.putExtra("isLogued", false)
        context.startActivity(intent)
    }

    fun openRegister(context: Context) {
        val intent = Intent(context, RegisterActivity::class.java)
        context.startActivity(intent)
    }

}