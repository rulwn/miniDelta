package raul.y.fernando.minidelta

import AdaptadorPacientesMedicamentos
import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Paint.Style
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
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
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.resources.TextAppearance
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import raul.y.fernando.minidelta.ui.home.HomeFragment
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException
import java.sql.Statement


@Suppress("UNREACHABLE_CODE")
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
        val rcvPacienteMedicamento =
            findViewById<RecyclerView>(R.id.recyclerViewPacienteMedicamento)
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

        rcvPacienteMedicamento.layoutManager = LinearLayoutManager(this)
        CoroutineScope(Dispatchers.IO).launch {
            val medicamentosPaciente = mostrarPacientesMedicamento(ID_Paciente, )
            withContext(Dispatchers.Main) {
                val miAdaptador = AdaptadorPacientesMedicamentos(medicamentosPaciente)
                rcvPacienteMedicamento.adapter = miAdaptador
            }
        }

        fun EliminarPaciente(idPaciente: Int) {
            GlobalScope.launch(Dispatchers.IO) {
                try {
                    val objConexion = ClaseConexion().cadenaConexion()
                    if (objConexion != null) {
                        val preparedStatement =
                            objConexion.prepareStatement("DELETE FROM PacientesBloom WHERE ID_Paciente = ?")
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
            val txtNuevoNumeroHabitacion =
                EditText(this).apply { setText(numero_habitacion.toString()) }

            val layout = LinearLayout(this).apply {
                orientation = LinearLayout.VERTICAL
                addView(txtNuevoNombrePaciente)
                addView(txtNuevoApellidoPaciente)
                addView(txtNuevaEdad)
                addView(txtNuevaEnfermedad)
                addView(txtNuevoNumeroHabitacion)
            }
            builder.setView(layout)

            builder.setPositiveButton("Guardar") { dialog, which ->

                val nuevoNombre = txtNuevoNombrePaciente.text.toString()
                val nuevoApellido = txtNuevoApellidoPaciente.text.toString()
                val nuevaEdad = txtNuevaEdad.text.toString().toInt()
                val nuevaEnfermedad = txtNuevaEnfermedad.text.toString()
                val nuevoNumeroHabitacion = txtNuevoNumeroHabitacion.text.toString().toInt()

                actualizarPaciente(
                    ID_Paciente,
                    nuevoNombre,
                    nuevoApellido,
                    nuevaEdad,
                    nuevaEnfermedad,
                    nuevoNumeroHabitacion
                )
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
            RecetarDialog()
        }

    }


    fun RecetarDialog() {
        val dialog = Dialog(this, R.style.customdialog)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.customdialog)

        val window = dialog.window
        if (window != null) {
            val layoutParams = WindowManager.LayoutParams()
            layoutParams.copyFrom(window.attributes)
            layoutParams.width = (resources.displayMetrics.widthPixels * 0.9).toInt()
            layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT
            window.attributes = layoutParams
        }

        val spnMedicamento = dialog.findViewById<Spinner>(R.id.spnMedicamento)

        // Mueve el código de obtención de medicamentos dentro de una corrutina
        CoroutineScope(Dispatchers.IO).launch {
            val listaMedicamentos = obtenerMedicamentos()
            val nombreMedicamento = listaMedicamentos.map { it.Nombre_Medicamento }

            withContext(Dispatchers.Main) {
                val miAdaptador = ArrayAdapter(this@activity_detallepaciente, android.R.layout.simple_spinner_dropdown_item, nombreMedicamento)
                spnMedicamento.adapter = miAdaptador
            }
        }

        val horario = dialog.findViewById<TextView>(R.id.txtHorario)
        horario.setOnClickListener {
            val calendario = java.util.Calendar.getInstance()

            val hora = calendario.get(java.util.Calendar.HOUR_OF_DAY)
            val minuto = calendario.get(java.util.Calendar.MINUTE)

            val timePickerDialog = TimePickerDialog(
                this,
                { _, horaSeleccionada, minutoSeleccionado ->
                    val horaSeleccionadaFormato = String.format("%02d:%02d", horaSeleccionada, minutoSeleccionado)
                    horario.text = horaSeleccionadaFormato
                },
                hora, minuto, true
            )
            timePickerDialog.show()
        }

        val btnRecetado = dialog.findViewById<Button>(R.id.btnRecetado)
        btnRecetado.setOnClickListener {
            dialog.dismiss() // Cierra el diálogo en lugar de finalizar la actividad
        }

        val txtCancel = dialog.findViewById<TextView>(R.id.txtCancel)
        txtCancel.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }

    private suspend fun obtenerMedicamentos(): List<dataClassMedicamentos> = withContext(Dispatchers.IO) {
        val listaMedicamentos = mutableListOf<dataClassMedicamentos>()
        var objConexion: Connection? = null
        var statement: Statement? = null
        var resultSet: ResultSet? = null

        try {
            objConexion = ClaseConexion().cadenaConexion()
            statement = objConexion?.createStatement()

            if (statement != null) {
                resultSet = statement.executeQuery("SELECT * FROM Medicamentos")

                while (resultSet.next()) {
                    val ID_Medicamento = resultSet.getInt("ID_Medicamento")
                    val Nombre_Medicamento = resultSet.getString("Nombre_Medicamento")
                    val Descripcion = resultSet.getString("Descripcion")
                    val mediCompleto = dataClassMedicamentos(ID_Medicamento, Nombre_Medicamento, Descripcion)
                    listaMedicamentos.add(mediCompleto)
                }
            }
        } catch (e: SQLException) {
            e.printStackTrace()
        } finally {
            try {
                resultSet?.close()
                statement?.close()
                objConexion?.close()
            } catch (e: SQLException) {
                e.printStackTrace()
            }
        }
        return@withContext listaMedicamentos
    }

    private fun actualizarPaciente(idPaciente: Int, nuevoNombre: String, nuevoApellido: String, nuevaEdad: Int, nuevaEnfermedad: String, nuevoNumeroHabitacion: Int) {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val objConexion = ClaseConexion().cadenaConexion()
                if (objConexion != null) {
                    val preparedStatement =
                        objConexion.prepareStatement("UPDATE PacientesBloom SET Nombres = ?, Apellidos = ?, Edad = ?, Enfermedad = ?, id_habitacion = ? WHERE ID_Paciente = ?")
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

    suspend fun mostrarPacientesMedicamento(idPaciente: Int): List<dataClassPacientesMedicamentos> = withContext(Dispatchers.IO) {
        val listaMedicamentos = mutableListOf<dataClassPacientesMedicamentos>()
        var objConexion: Connection? = null
        var preparedStatement: PreparedStatement? = null
        var resultSet: ResultSet? = null

        try {
            objConexion = ClaseConexion().cadenaConexion()
            if (objConexion != null) {
                preparedStatement = objConexion.prepareStatement(
                    """
                SELECT 
                    pm.ID_pacientes_medicamentos,
                    pm.Hora_Aplicacion,
                    p.ID_Paciente,
                    m.Nombre_Medicamento
                FROM 
                    Pacientes_Medicamentos pm
                INNER JOIN 
                    PacientesBloom p ON pm.ID_Paciente = p.ID_Paciente
                INNER JOIN 
                    Medicamentos m ON pm.ID_Medicamento = m.ID_Medicamento
                WHERE 
                    p.ID_Paciente = ?
                """
                )
                preparedStatement.setInt(1, idPaciente)
                resultSet = preparedStatement.executeQuery()
                while (resultSet.next()) {
                    val ID_pacientes_medicamentos = resultSet.getInt("ID_pacientes_medicamentos")
                    val Hora_Aplicacion = resultSet.getDate("Hora_Aplicacion")
                    val ID_Paciente = resultSet.getInt("ID_Paciente")
                    val Nombre_Medicamento = resultSet.getString("Nombre_Medicamento")

                    val medicamento = dataClassPacientesMedicamentos(ID_pacientes_medicamentos, ID_Paciente, Nombre_Medicamento, Hora_Aplicacion)
                    listaMedicamentos.add(medicamento)
                }
            }
        } catch (e: SQLException) {
            e.printStackTrace()
        } finally {
            try {
                resultSet?.close()
                preparedStatement?.close()
                objConexion?.close()
            } catch (e: SQLException) {
                e.printStackTrace()
            }
        }

        return@withContext listaMedicamentos
    }
}
