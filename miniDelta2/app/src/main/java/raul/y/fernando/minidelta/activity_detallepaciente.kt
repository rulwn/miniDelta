package raul.y.fernando.minidelta

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Paint.Style
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.resources.TextAppearance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import raul.y.fernando.minidelta.ui.home.HomeFragment
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
        getSupportActionBar()?.hide()
        val txtNombre = findViewById<TextView>(R.id.txtNombre)
        val txtEdad = findViewById<TextView>(R.id.txtEdad)
        val txtEnfermedad = findViewById<TextView>(R.id.txtEnfermedad)
        val txtHabitacion = findViewById<TextView>(R.id.txtHabitacion)
        val txtCama = findViewById<TextView>(R.id.txtCama)
        val imgAtras = findViewById<ImageView>(R.id.imgAtras)
        val btnBorrar = findViewById<Button>(R.id.btnBorrar)
        val btnEditar = findViewById<Button>(R.id.btnEditar)
        val btnRecetar = findViewById<Button>(R.id.btnContinuar)
        val rcvPacienteMedicamento = findViewById<RecyclerView>(R.id.recyclerViewPacienteMedicamento)
        val btnAgregarMedicamento = findViewById<Button>(R.id.btnAgregarMedicamento)


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
                 try {
                     val objConexion = ClaseConexion().cadenaConexion()
                     if (objConexion != null) {
                         val preparedStatement = objConexion.prepareStatement("DELETE FROM PacientesBloom WHERE ID_Paciente = ?")
                         preparedStatement.setInt(1, idPaciente)
                         preparedStatement.executeUpdate()
                         val commit = objConexion.prepareStatement("commit")!!
                         commit.executeUpdate()
                     }
                 } catch (e: SQLException) {
                     e.printStackTrace()
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
            val txtNuevoNombrePaciente = EditText(this).apply { setText(nombres) }
            val txtNuevoApellidoPaciente = EditText(this).apply { setText(apellidos) }
            val txtNuevaEdad = EditText(this).apply { setText(edad.toString()) }
            val txtNuevaEnfermedad = EditText(this).apply { setText(enfermedad) }
            val txtNuevoNumeroHabitacion = EditText(this).apply { setText(numero_habitacion.toString()) }

            val layout = LinearLayout(this).apply {
                orientation = LinearLayout.VERTICAL
                addView(txtNuevoNombrePaciente)
                addView(txtNuevoApellidoPaciente)
                addView(txtNuevaEdad)
                addView(txtNuevaEnfermedad)
                addView(txtNuevoNumeroHabitacion)
            }
            builder.setView(layout)

            builder.setPositiveButton("Guardar" ) { dialog, which ->

                val nuevoNombre = txtNuevoNombrePaciente.text.toString()
                val nuevoApellido = txtNuevoApellidoPaciente.text.toString()
                val nuevaEdad = txtNuevaEdad.text.toString().toInt()
                val nuevaEnfermedad = txtNuevaEnfermedad.text.toString()
                val nuevoNumeroHabitacion = txtNuevoNumeroHabitacion.text.toString().toInt()

                actualizarPaciente( ID_Paciente, nuevoNombre, nuevoApellido,  nuevaEdad, nuevaEnfermedad, nuevoNumeroHabitacion)
                Toast.makeText(this, "Paciente actualizado", Toast.LENGTH_SHORT).show()
                finish()
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

        btnAgregarMedicamento.setOnClickListener {
            println()
        }

        btnRecetar.setOnClickListener{
            //aqui llamar al dialog

        }
    }

    class RecetarDialog(
        context: Context,
        private val medicamentos: List<String>,
        private val listener: OnRecetarListener
    ) : Dialog(context) {

        interface OnRecetarListener {
            fun onRecetar(medicamento: String, dosis: String)
        }

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.customdialog)

            val spnMedicamento: Spinner = findViewById(R.id.spnMedicamento)
            val txtHorario: EditText = findViewById(R.id.txtHorario)
            val btnRecetado: Button = findViewById(R.id.btnRecetado)
            val txtCancel: TextView = findViewById(R.id.txtCancel)

            val adapter = ArrayAdapter(context, android.R.layout.simple_spinner_item, medicamentos)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spnMedicamento.adapter = adapter

            btnRecetado.setOnClickListener {
                val medicamento = spnMedicamento.selectedItem.toString()
                txtHorario.setOnClickListener {

                }
                val horario = txtHorario.text.toString()
                listener.onRecetar(medicamento, horario)
                dismiss()
            }

            txtCancel.setOnClickListener {
                dismiss()
            }
        }
    }

    private fun actualizarPaciente(idPaciente: Int, nuevoNombre: String, nuevoApellido: String, nuevaEdad: Int, nuevaEnfermedad: String, nuevoNumeroHabitacion: Int) {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val objConexion = ClaseConexion().cadenaConexion()
                if (objConexion != null) {
                    val preparedStatement = objConexion.prepareStatement("UPDATE PacientesBloom SET Nombres = ?, Apellidos = ?, Edad = ?, Enfermedad = ?, id_habitacion = ? WHERE ID_Paciente = ?")
                    preparedStatement.setString(1, nuevoNombre)
                    preparedStatement.setString(2, nuevoApellido)
                    preparedStatement.setInt(3, nuevaEdad)
                    preparedStatement.setString(4, nuevaEnfermedad)
                    preparedStatement.setInt(5, nuevoNumeroHabitacion)
                    preparedStatement.setInt(6, idPaciente)
                    preparedStatement.executeUpdate()

                    val commit = objConexion.prepareStatement("commit")!!
                    commit.executeUpdate()
                }

            } catch (e: SQLException) {
                e.printStackTrace()
            } finally {
                try {
                } catch (e: SQLException) {
                    e.printStackTrace()
                }
            }
        }
    }
}
