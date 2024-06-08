package raul.ochoa.validaciones

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val txtNombre = findViewById<TextView>(R.id.txtNombre)
        val txtCorreo = findViewById<TextView>(R.id.txtCorreo)
        val txtContra = findViewById<TextView>(R.id.txtContra)
        val txtEdad = findViewById<TextView>(R.id.txtEdad)
        val txtDui = findViewById<TextView>(R.id.txtDui)
        val btnGuardar = findViewById<TextView>(R.id.btnSiguiente)

        btnGuardar.setOnClickListener {
           if (txtNombre.text.isEmpty() || txtCorreo.text.isEmpty() || txtContra.text.isEmpty() || txtEdad.text.isEmpty() || txtDui.text.isEmpty()) {
               Toast.makeText(this, "Ingrese todos los datos", Toast.LENGTH_SHORT).show()
           } else {
               if (!txtEdad.text.matches("[0-9]".toRegex())) {
                   Toast.makeText(this, "Datos guardados", Toast.LENGTH_SHORT).show()
               }
           }
        }
    }
}