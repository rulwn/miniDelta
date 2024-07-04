package raul.y.fernando.minidelta

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.sql.SQLException
import java.sql.Statement

class activity_detallepaciente : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_detallepaciente)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val txtNombre = findViewById<TextView>(R.id.txtNombre)
        val txtEdad = findViewById<TextView>(R.id.txtEdad)
        val txtEnfermedad = findViewById<TextView>(R.id.txtEnfermedad)
        val txtHabitacion = findViewById<TextView>(R.id.txtHabitacion)
        val txtCama = findViewById<TextView>(R.id.txtCama)
        val imgAtras = findViewById<ImageView>(R.id.imgAtras)
        val btnBorrar = findViewById<Button>(R.id.btnBorrar)
        val btnEditar = findViewById<Button>(R.id.btnEditar)

        val ID_Paciente = intent.getIntExtra("ID_Paciente", 0)
        val nombres = intent.getStringExtra("Nombres")
        val apellidos = intent.getStringExtra("Apellidos")
        val edad = intent.getIntExtra("Edad", 0)
        val enfermedad = intent.getStringExtra("Enfermedad")
        val numero_habitacion = intent.getIntExtra("Numero_Habitacion", 0)
        val numero_cama = intent.getIntExtra("Numero_cama", 0)
        val id_habitacion = intent.getIntExtra("id_habitacion", 0)

        val nombreCompleto = getString(R.string.nombre_completo, nombres, apellidos)
        txtNombre.text = nombreCompleto
        txtEdad.text = edad.toString()
        txtEnfermedad.text = enfermedad
        txtHabitacion.text = numero_habitacion.toString()
        txtCama.text = numero_cama.toString()

         fun EliminarPaciente(idPaciente: Int) {
             GlobalScope.launch(Dispatchers.IO) {
                 val objConexion = ClaseConexion().cadenaConexion()
                 var statement: Statement? = null

                 try {
                     if (objConexion != null) {
                         statement = objConexion.createStatement()
                         val preparedStatement =
                             objConexion.prepareStatement("DELETE FROM PacientesBloom WHERE ID_Paciente = ?")
                         preparedStatement.setInt(1, idPaciente)
                         preparedStatement.executeUpdate()
                         val commit = objConexion.prepareStatement("commit")!!
                         commit.executeUpdate()
                     }
                 } catch (e: SQLException) {
                     e.printStackTrace()
                 } finally {
                     try {
                         statement?.close()
                         objConexion?.close()
                     } catch (e: SQLException) {
                         e.printStackTrace()
                     }
                 }
             }
        }

        btnBorrar.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Confirmación")
            builder.setMessage("¿Quieres eliminar este paciente?")

            builder.setPositiveButton("Sí") { dialog, which ->
                EliminarPaciente(ID_Paciente)
                Toast.makeText(this, "Paciente eliminado", Toast.LENGTH_SHORT).show()
                finish()
            }

            builder.setNegativeButton("No") { dialog, which ->
                dialog.dismiss()
            }

            val dialog = builder.create()
            dialog.show()
        }

        btnEditar.setOnClickListener {
            val builder = AlertDialog.Builder(this, R.style.CustomAlertDialog)
            builder.setCustomTitle(TextView(this).apply {
                text = "Editar datos del paciente"
                setTextAppearance(context, R.style.AlertDialogTitle)
            })

            val txtNuevoNombrePaciente = EditText(this).apply { setText(nombres) }
            val txtNuevoApellidoPaciente = EditText(this).apply { setText(apellidos) }
            val txtNuevaEdad = EditText(this).apply { setText(edad.toString()) }
            val txtNuevaEnfermedad = EditText(this).apply { setText(enfermedad) }
            val txtNuevoNumeroHabitacion = EditText(this).apply { setText(numero_habitacion.toString()) }
            val txtNuevoNumeroCama = EditText(this).apply { setText(numero_cama.toString()) }

            val layout = LinearLayout(this).apply {
                orientation = LinearLayout.VERTICAL
                addView(txtNuevoNombrePaciente)
                addView(txtNuevoApellidoPaciente)
                addView(txtNuevaEdad)
                addView(txtNuevaEnfermedad)
                addView(txtNuevoNumeroHabitacion)
                addView(txtNuevoNumeroCama)
            }
            builder.setView(layout)

            builder.setPositiveButton("Guardar") { dialog, which ->
                val nuevoNombre = txtNuevoNombrePaciente.text.toString()
                val nuevoApellido = txtNuevoApellidoPaciente.text.toString()
                val nuevaEdad = txtNuevaEdad.text.toString().toInt()
                val nuevaEnfermedad = txtNuevaEnfermedad.text.toString()
                val nuevoNumeroHabitacion = txtNuevoNumeroHabitacion.text.toString().toInt()
                val nuevoNumeroCama = txtNuevoNumeroCama.text.toString().toInt()

                actualizarPaciente( ID_Paciente, nuevoNombre, nuevoApellido,  nuevaEdad, nuevaEnfermedad, nuevoNumeroHabitacion, nuevoNumeroCama)
            }
            builder.setNegativeButton("Cancelar", null)

            val dialog = builder.create()
            dialog.show()
        }

        imgAtras.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("ir_atras", true)
            startActivity(intent)
        }
    }

    private fun actualizarPaciente(idPaciente: Int, nuevoNombre: String, nuevoApellido: String, nuevaEdad: Int, nuevaEnfermedad: String, nuevoNumeroHabitacion: Int, nuevoNumeroCama: Int) {
        GlobalScope.launch(Dispatchers.IO) {
            val objConexion = ClaseConexion().cadenaConexion()
            val statement: Statement? = null

            try {
                if (objConexion != null) {
                    val preparedStatement = objConexion.prepareStatement("UPDATE PacientesBloom SET Nombres = ?, Edad = ?, Enfermedad = ?, Numero_Habitacion = ?, Numero_Cama = ? WHERE ID_Paciente = ?")
                    preparedStatement.setString(1, nuevoNombre)
                    preparedStatement.setString(2, nuevoApellido)
                    preparedStatement.setInt(3, nuevaEdad)
                    preparedStatement.setString(4, nuevaEnfermedad)
                    preparedStatement.setInt(5, nuevoNumeroHabitacion)
                    preparedStatement.setInt(6, nuevoNumeroCama)
                    preparedStatement.setInt(7, idPaciente)
                    preparedStatement.executeUpdate()

                    val commit = objConexion.prepareStatement("commit")!!
                    commit.executeUpdate()

                    withContext(Dispatchers.Main) {
                        //Actualizarlistadespuesdecargardatos()
                    }

                }

            } catch (e: SQLException) {
                e.printStackTrace()
            } finally {
                try {
                    statement?.close()
                    objConexion?.close()
                } catch (e: SQLException) {
                    e.printStackTrace()
                }
            }
        }
    }
}
