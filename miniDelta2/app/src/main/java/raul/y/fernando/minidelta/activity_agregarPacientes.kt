package raul.y.fernando.minidelta

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.UUID

class activity_agregarPacientes : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_agregar_pacientes)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        supportActionBar?.hide()

        val txtNombre = findViewById<EditText>(R.id.nombreEditText)
        val txtApellido = findViewById<EditText>(R.id.apellidoEditText)
        val txtEdad = findViewById<EditText>(R.id.EdadEditText)
        val txtCama = findViewById<EditText>(R.id.CamaEditText)
        val spHabitacion = findViewById<Spinner>(R.id.spSeguro)
        val btnAgregar = findViewById<Button>(R.id.btnAgregarMedicamento)
        val imgAtras = findViewById<ImageView>(R.id.imgAtras)
        val txtEnfermedad = findViewById<EditText>(R.id.txtEnfermedad)

        btnAgregar.setOnClickListener {
            GlobalScope.launch(Dispatchers.IO) {
                try {
                    val objConexion = ClaseConexion().cadenaConexion()

                    val addPaciente = objConexion?.prepareStatement("INSERT INTO PacientesBloom (Nombres, Apellidos, Edad, Enfermedad, id_habitacion) VALUES (?, ?, ?, ?, ?)")!!

                    addPaciente.setString(1, txtNombre.text.toString())
                    addPaciente.setString(2, txtApellido.text.toString())
                    addPaciente.setInt(3, txtEdad.text.toString().toInt())
                    addPaciente.setString(4, txtEnfermedad.text.toString())
                    addPaciente.setInt(5, spHabitacion.selectedItem.toString().toInt())
                    addPaciente.executeUpdate()

                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@activity_agregarPacientes, "Paciente agregado con éxito", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@activity_agregarPacientes, "Error al agregar el paciente: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
                    e.printStackTrace()
                }
            }
        }


         fun ObtenerDoctores(): List<dataClassHabitaciones>{
            val objConexion = ClaseConexion().cadenaConexion()
            val statement = objConexion?.createStatement()
            val resultSet = statement?.executeQuery("SELECT * FROM Habitaciones")!!
            val listaDoctores = mutableListOf<dataClassHabitaciones>()
            while (resultSet.next() ){
                val id = resultSet.getInt("id_habitacion")
                val numeroHabitacion = resultSet.getInt("Numero_Habitacion")
                val numeroCama = resultSet.getInt("Numero_cama")
                val doctorCompleto = dataClassHabitaciones(id, numeroHabitacion, numeroCama)
                listaDoctores.add(doctorCompleto)
            }
            return listaDoctores

        }

        GlobalScope.launch(Dispatchers.IO) {
            val listadoDoctores = ObtenerDoctores()
            val numeroHabitacion = listadoDoctores.map { it.Numero_Habitacion }

            withContext(Dispatchers.Main) {
                val miAdaptador = ArrayAdapter(this@activity_agregarPacientes, android.R.layout.simple_spinner_dropdown_item, numeroHabitacion)
                spHabitacion.adapter = miAdaptador
            }
        }

        imgAtras.setOnClickListener {
            finish()
        }
    }
}