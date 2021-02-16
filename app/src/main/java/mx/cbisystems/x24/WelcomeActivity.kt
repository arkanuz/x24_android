package mx.cbisystems.x24

import android.R.attr
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity


@Suppress("DEPRECATION")
class WelcomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        this.supportActionBar?.hide()

        setContentView(R.layout.activity_welcome)

        var welcomeConstraint = findViewById<LinearLayout>(R.id.welcomeHorizontalScrollLayout)
        var inflater = LayoutInflater.from(this)
        var displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getRealMetrics(displayMetrics)
        var widthScreen : Int = displayMetrics.widthPixels


        var mainView = inflater.inflate(
            R.layout.welcome_main_item_layout,
            findViewById(R.id.welcomeScrollView),
            false
        )
        mainView.layoutParams.width = widthScreen
        welcomeConstraint.addView(mainView)

        var loginMainbutton = mainView.findViewById<Button>(R.id.loginMainButton)
        loginMainbutton.setOnClickListener {
            openLogin()
        }

        var view1 = inflater.inflate(R.layout.welcome_item_layout, welcomeConstraint, false)
        var titleTextView1 = view1.findViewById<TextView>(R.id.titleTextView)
        titleTextView1.setText("Recargas con \ncódigos QR")
        var detailTextView1 = view1.findViewById<TextView>(R.id.detailTextView)
        detailTextView1.setText("Garantizamos tu privacidad, haz recargas \nsin tener que dictar tu número.")
        var imageView1 = view1.findViewById<ImageView>(R.id.backgroundWelcomeImageView)
        imageView1.setImageResource(R.drawable.welcome_2)
        view1.layoutParams.width = widthScreen
        welcomeConstraint.addView(view1)

        var view2 = inflater.inflate(R.layout.welcome_item_layout, welcomeConstraint, false)
        var titleTextView2 = view2.findViewById<TextView>(R.id.titleTextView)
        titleTextView2.setText("Acumula puntos \ncanjeables")
        var detailTextView2 = view2.findViewById<TextView>(R.id.detailTextView)
        detailTextView2.setText("Por cada compra recibe una bonificación \nque se acumula en tu código QR.")
        var imageView2 = view2.findViewById<ImageView>(R.id.backgroundWelcomeImageView)
        imageView2.setImageResource(R.drawable.welcome_3)
        view2.layoutParams.width = widthScreen
        welcomeConstraint.addView(view2)

        var view3 = inflater.inflate(R.layout.welcome_item_layout, welcomeConstraint, false)
        var titleTextView3 = view3.findViewById<TextView>(R.id.titleTextView)
        titleTextView2.setText("No sufras por no \nencontrarnos")
        var detailTextView3 = view3.findViewById<TextView>(R.id.detailTextView)
        detailTextView3.setText("Ubica la sucursal más cercana a ti.")
        var imageView3 = view3.findViewById<ImageView>(R.id.backgroundWelcomeImageView)
        imageView3.setImageResource(R.drawable.welcome_4)
        view3.layoutParams.width = widthScreen
        welcomeConstraint.addView(view3)
    }

    fun openLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        //intent.putExtra("isLogged", false)
        startActivityForResult(intent, 1)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.v("onActivityResult", "onActivityResult")

        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                //get extra data from data intent here
                val isLoggued = data?.getBooleanExtra ("isLogged", false)
                if (isLoggued == true){
                    val intent = Intent(this, TablayoutActivity::class.java)
                    startActivity(intent)
                }
            }
        }
    }
}