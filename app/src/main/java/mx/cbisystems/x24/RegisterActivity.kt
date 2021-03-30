package mx.cbisystems.x24

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.irozon.alertview.AlertActionStyle
import com.irozon.alertview.AlertStyle
import com.irozon.alertview.AlertView
import com.irozon.alertview.objects.AlertAction
import mx.cbisystems.x24.databinding.ActivityRegisterBinding
import mx.cbisystems.x24.entities.MRegister
import mx.cbisystems.x24.networking.LoadingFragment
import mx.cbisystems.x24.networking.RestEngine
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var loading: LoadingFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_register)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.pasaleRegisterButton.setOnClickListener {
            val name = binding.nameRegisterEditText.text
            val lastName = binding.apellidosRegisterEditText.text
            val email = binding.mailRegisterEditText.text
            val phone = binding.phoneRegisterEditText.text
            val pass = binding.passRegisterEditText.text

            hideKeyBoard(binding.passRegisterEditText)

            if (name.isEmpty()){
                binding.nameRegisterEditText.error = "Nombre requerido"
                binding.nameRegisterEditText.requestFocus()
                return@setOnClickListener
            }

            if (lastName.isEmpty()){
                binding.apellidosRegisterEditText.error = "Apellidos requeridos"
                binding.apellidosRegisterEditText.requestFocus()
                return@setOnClickListener
            }

            if (email.isEmpty()){
                binding.mailRegisterEditText.error = "Correo requerido"
                binding.mailRegisterEditText.requestFocus()
                return@setOnClickListener
            }

            if (phone.isEmpty()){
                binding.phoneRegisterEditText.error = "Número celular requerido"
                binding.phoneRegisterEditText.requestFocus()
                return@setOnClickListener
            }

            if (pass.isEmpty()){
                binding.passRegisterEditText.error = "Contraseña requerida"
                binding.passRegisterEditText.requestFocus()
                return@setOnClickListener
            }
            else if (pass.count() < 8){
                binding.passRegisterEditText.error = "Contraseña debe contener al menos 8 caracteres"
                binding.passRegisterEditText.requestFocus()
                return@setOnClickListener
            }

            val register = MRegister(0, name.toString(), lastName.toString(), email.toString(), phone.toString(), 0, null, 0, email.toString(), pass.toString(), 0, null, 0, 0)
            RestEngine.instance.register(register)
                    .enqueue(object: Callback<Void>{
                        override fun onResponse(call: Call<Void>, response: Response<Void>) {
                            if (response.code() == 201){
                                val intent = Intent(this@RegisterActivity, SuccessfulActivity::class.java)
                                startActivity(intent)
                            }
                            else if (response.code() == 409) {
                                // Usuario duplicado
                                val alert = AlertView("Este correo ya existe", "Ya se ha registrado un usuario con este correo.", AlertStyle.DIALOG)
                                alert.addAction(AlertAction("Aceptar", AlertActionStyle.DEFAULT, { action ->
// Action 1 callback
                                }))
                                alert.show(this@RegisterActivity)
                            }
                            else {
                                val alert = AlertView("Error ${response.code()} al registrar", "Ocurrió un error al registrar el usuario, favor de consultar con el proveedor.", AlertStyle.DIALOG)
                                alert.addAction(AlertAction("Aceptar", AlertActionStyle.DEFAULT, { action ->
// Action 1 callback
                                }))
                                alert.show(this@RegisterActivity)
                            }
                        }

                        override fun onFailure(call: Call<Void>, t: Throwable) {
                            val alert = AlertView("No se pudo establecer conexión con el servidor", "Verifique que el servidor está encendido y se encuentra funcionando correctamente.", AlertStyle.DIALOG)
                            alert.addAction(AlertAction("Aceptar", AlertActionStyle.DEFAULT, { action ->
// Action 1 callback
                            }))
                            alert.show(this@RegisterActivity)
                        }
                    })
        }
    }

    // Ocultar teclado al tapear fuera de un TextView
    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (currentFocus != null) {
            //val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            //imm.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
            hideKeyBoard(currentFocus!!)
        }
        return super.dispatchTouchEvent(ev)
    }

    // Función para ocultar el teclado
    private fun hideKeyBoard(view: View){
        val imm : InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
}

