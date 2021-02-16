package mx.cbisystems.x24

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import mx.cbisystems.x24.networking.AdminSQLiteOpenHelper

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onResume() {
        super.onResume()

        val db = AdminSQLiteOpenHelper(this)
        val user = db.getUser()
        if (user != null){
            val intent = Intent(this, TablayoutActivity::class.java)
            startActivity(intent)
        } else {
            val intent = Intent(this, WelcomeActivity::class.java)
            startActivity(intent)
        }

    }
}