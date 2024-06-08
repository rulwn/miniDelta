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

class activity_login : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val txtNombre = findViewById<EditText>(R.id.txtLoginNombre)
        val txtContrasena = findViewById<EditText>(R.id.txtLoginContra)
        val btnIniciar = findViewById<Button>(R.id.btnAgregar)
        btnIniciar.setOnClickListener {
            GlobalScope.launch(Dispatchers.IO) {
                val nombre = txtNombre.text.toString()
                val contrasena = txtContrasena.text.toString()

                try {
                    val objConexion = ClaseConexion().cadenaConexion()
                    val verificar = objConexion?.prepareStatement("SELECT COUNT(*) FROM tbUsuarios WHERE nombre = ? AND contrasena = ?")
                    verificar?.setString(1, nombre)
                    verificar?.setString(2, contrasena)
                    val resultado = verificar?.executeQuery()

                    if (resultado != null && resultado.next() && resultado.getInt(1) > 0) {
                        withContext(Dispatchers.Main) {
                            Toast.makeText(
                                this@activity_login,
                                "Inicio de sesión exitoso",
                                Toast.LENGTH_SHORT
                            ).show()
                            val intent = Intent(this@activity_login, MainActivity::class.java)
                            startActivity(intent)
                        }
                    } else {
                        withContext(Dispatchers.Main) {
                            Toast.makeText(
                                this@activity_login,
                                "Usuario o contraseña incorrectos",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@activity_login, "Error: ${e.message}", Toast.LENGTH_LONG)
                            .show()
                    }
                    e.printStackTrace()
                }
            }
        }
    }
}

