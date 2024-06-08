package raul.ochoa.helpdesk

import Modelo.ClaseConexion
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.UUID

class activity_registrarse : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_registrarse)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val txtNombre = findViewById<EditText>(R.id.txtNombre)
        val txtEmail = findViewById<EditText>(R.id.txtEmail)
        val txtContrasena = findViewById<EditText>(R.id.txtContrasena)
        val btnRegistrarse = findViewById<Button>(R.id.btnRegistrarse)

        btnRegistrarse.setOnClickListener {
            GlobalScope.launch(Dispatchers.IO) {
                try {
                    val objConexion = ClaseConexion().cadenaConexion()
                    val addUser = objConexion?.prepareStatement("INSERT INTO tbUsuarios (uuid, nombre, email, contrasena) VALUES (?, ?, ?, ?)")!!

                    addUser.setString(1, UUID.randomUUID().toString())
                    addUser.setString(2, txtNombre.text.toString())
                    addUser.setString(3, txtEmail.text.toString())
                    addUser.setString(4, txtContrasena.text.toString())

                    val verificarFilas = addUser.executeUpdate()
                    if (verificarFilas > 0) {
                        withContext(Dispatchers.Main) {
                            Toast.makeText(this@activity_registrarse, "Usuario creado correctamente", Toast.LENGTH_SHORT).show()
                            limpiar(txtNombre, txtEmail, txtContrasena)
                            val intent = Intent(this@activity_registrarse, MainActivity::class.java)
                            startActivity(intent)
                        }
                    } else {
                        withContext(Dispatchers.Main) {
                            Toast.makeText(this@activity_registrarse, "Error al crear el usuario", Toast.LENGTH_SHORT).show()
                        }
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@activity_registrarse, "Error: ${e.message}", Toast.LENGTH_LONG).show()
                    }
                    e.printStackTrace()
                }
            }
        }
    }
     fun limpiar(txtNombre: EditText, txtEmail: EditText, txtContrasena: EditText) {
        txtNombre.setText("")
        txtEmail.setText("")
        txtContrasena.setText("")
    }
}