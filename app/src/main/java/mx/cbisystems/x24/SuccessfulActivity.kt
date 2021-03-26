package mx.cbisystems.x24

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer

class SuccessfulActivity : AppCompatActivity() {
    private lateinit var timer: CountDownTimer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_successful)
    }

    override fun onStart() {
        super.onStart()

        // Crear un contador para que se muestre mensaje por 2.5 segundos
        timer = object: CountDownTimer(2500, 500){
            override fun onTick(millisUntilFinished: Long) {

            }

            override fun onFinish() {
                val intent = Intent(this@SuccessfulActivity, WelcomeActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                startActivity(intent)
            }

        }
    }

    override fun onDestroy() {
        super.onDestroy()

        timer.cancel()
    }
}