package mx.cbisystems.x24

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.squareup.picasso.Picasso
import mx.cbisystems.x24.entities.MStore
import mx.cbisystems.x24.entities.StoreItem
import java.util.*

class StoreActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_store)

        val store = intent.extras?.get("store") as StoreItem

        val textView = findViewById<TextView>(R.id.cercaTextView)
        textView.text = "Cerca de ti"

        val nameTextView = findViewById<TextView>(R.id.storeNearNameTextView)
        nameTextView.text = store.name

        val addressTextview = findViewById<TextView>(R.id.directionNearTextView)
        addressTextview.text = store.address

        val storeImageView = findViewById<ImageView>(R.id.storeImage)
        Picasso.get().load(store.url_photo).placeholder(R.drawable.blank).error(R.drawable.store).into(storeImageView)

        Log.i("StoreActivity", "")

        val startRouteButton = findViewById<Button>(R.id.startRouteButton)
        startRouteButton.setOnClickListener {
            val uri = String.format(Locale.ENGLISH, "geo:%f,%f?=%s", store.latitude, store.longitude, store.address)
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
            startActivity(intent)
        }
    }
}