package mx.cbisystems.x24

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.irozon.alertview.AlertActionStyle
import com.irozon.alertview.AlertStyle
import com.irozon.alertview.AlertView
import com.irozon.alertview.objects.AlertAction
import mx.cbisystems.x24.databinding.ActivityLoginBinding
import mx.cbisystems.x24.entities.MUser
import mx.cbisystems.x24.networking.AdminSQLiteOpenHelper
import mx.cbisystems.x24.networking.LoadingDialog
import mx.cbisystems.x24.networking.RestEngine
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_login)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.joinLoginButton.setOnClickListener {
            val mail = binding.loginEditText.text.toString().trim()
            val pass = binding.passEditText.text.toString().trim()

            hideKeyBoard(binding.passEditText)

            if (mail.isEmpty()){
                binding.loginEditText.error = "Correo requerido"
                binding.loginEditText.requestFocus()
                return@setOnClickListener
            }

            if (pass.isEmpty()){
                binding.passEditText.error = "Contraseña requerida"
                binding.passEditText.requestFocus()
                return@setOnClickListener
            }

            val context = this
            val db = AdminSQLiteOpenHelper(context)

            val loadingDialog = LoadingDialog(this)
            loadingDialog.showLoading()

            Log.i(TAG,"conexión iniciada")
            RestEngine.instance.loginUser(mail, pass)
                .enqueue(object: Callback<MUser>{
                    override fun onResponse(call: Call<MUser>, response: Response<MUser>) {
                        loadingDialog.hideLoading()

                        Log.i(TAG, "pasó la conexión ${response.body()}")
                        if (response.code() == 200){
                            val user = response.body()
                            if (user != null) {
                                user.password = pass
                                db.saveUser(user)

                                val intent = Intent()
                                finish()
                            }
                        }
                        else if (response.code() == 302){
                            val alert = AlertView("Contraseña incorrecta", "La contraseña es incorrecta", AlertStyle.DIALOG)
                            alert.addAction(AlertAction("Aceptar", AlertActionStyle.DEFAULT, { action ->
// Action 1 callback
                            }))
                            alert.show(this@LoginActivity)
                        }
                        else if (response.code() == 401){
                            val alert = AlertView("Valida tu cuenta", "Por favor revise su correo electrónico.", AlertStyle.DIALOG)
                            alert.addAction(AlertAction("Aceptar", AlertActionStyle.DEFAULT, { action ->
// Action 1 callback
                            }))
                            alert.show(this@LoginActivity)
                        }
                        else if (response.code() == 404){
                            val alert = AlertView("Usuario no encontrado", "Por favor verifica tus datos.", AlertStyle.DIALOG)
                            alert.addAction(AlertAction("Aceptar", AlertActionStyle.DEFAULT, { action ->
// Action 1 callback
                            }))
                            alert.show(this@LoginActivity)
                        }
                        else {
                            Log.i(TAG, "se recibió respuesta ${response.code()} del server")
                            val alert = AlertView("No se pudo establecer conexión con el servidor", "Verifique que el servidor está encendido y se encuentra funcionando correctamente", AlertStyle.DIALOG)
                            alert.addAction(AlertAction("Aceptar", AlertActionStyle.DEFAULT, { action ->
// Action 1 callback
                            }))
                            alert.show(this@LoginActivity)
                        }
                    }

                    override fun onFailure(call: Call<MUser>, t: Throwable) {
                        loadingDialog.hideLoading()

                        Log.i(TAG,"error en conexión " + t.message)
                        val alert = AlertView("No se pudo establecer conexión con el servidor", "Verifique que el servidor está encendido y se encuentra funcionando correctamente", AlertStyle.DIALOG)
                        alert.addAction(AlertAction("Aceptar", AlertActionStyle.DEFAULT, { action ->
// Action 1 callback
                        }))
                        alert.show(this@LoginActivity)
                    }

                })
        }

    }

    companion object {
        private const val TAG = "MyActivity"
    }

    private fun hideKeyBoard(view: View){
        val imm : InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
}