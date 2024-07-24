package raul.y.fernando.minidelta.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import raul.y.fernando.minidelta.AdaptadorPacientes
import raul.y.fernando.minidelta.ClaseConexion
import raul.y.fernando.minidelta.R
import raul.y.fernando.minidelta.dataClassPacientes
import raul.y.fernando.minidelta.databinding.FragmentHomeBinding
import java.sql.ResultSet
import java.sql.SQLException
import java.sql.Statement

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        val rcvPacientes = root.findViewById<RecyclerView>(R.id.recyclerView)
        rcvPacientes.layoutManager = LinearLayoutManager(requireContext())
        CoroutineScope(Dispatchers.IO).launch{
            val tratamientosDB = obtenerDatos()
            withContext(Dispatchers.Main){
                val miAdaptador = AdaptadorPacientes(tratamientosDB)
                rcvPacientes.adapter = miAdaptador

            }
        }
        fun actualizarDatos() {
            CoroutineScope(Dispatchers.IO).launch {
                val tratamientosDB = obtenerDatos()
                withContext(Dispatchers.Main) {
                    val miAdaptador = AdaptadorPacientes(tratamientosDB)
                    rcvPacientes.adapter = miAdaptador
                    miAdaptador.Actualizarlista(tratamientosDB)
                }
            }
        }
        obtenerDatos()
        actualizarDatos()
        return root
    }
    private fun obtenerDatos(): List<dataClassPacientes> {
        val pacientes = mutableListOf<dataClassPacientes>()
        val objConexion = ClaseConexion().cadenaConexion()
        var statement: Statement? = null
        var resultSet: ResultSet? = null

        try {
            if (objConexion != null) {
                statement = objConexion.createStatement()
                resultSet = statement.executeQuery("SELECT p.ID_Paciente, p.Nombres, p.Apellidos, p.Edad, p.Enfermedad,h.id_habitacion, h.Numero_Habitacion, h.Numero_Cama FROM PacientesBloom p INNER JOIN Habitaciones h ON p.id_habitacion = h.id_habitacion")

                while (resultSet.next()) {
                    val iD_Paciente = resultSet.getInt("ID_Paciente")
                    val nombre = resultSet.getString("Nombres")
                    val apellido = resultSet.getString("Apellidos")
                    val Edad = resultSet.getInt("Edad")
                    val enfermedad = resultSet.getString("Enfermedad")
                    val id_habitacion = resultSet.getInt("id_Habitacion")
                    val numero_habitacion = resultSet.getInt("Numero_Habitacion")
                    val Numero_cama = resultSet.getInt("Numero_Cama")
                    val paciente = dataClassPacientes(iD_Paciente, nombre, apellido, Edad, enfermedad, id_habitacion, Numero_cama, numero_habitacion)
                    pacientes.add(paciente)
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

        return pacientes
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}