package raul.ochoa.helpdesk.ui.home

import Modelo.ClaseConexion
import RecyclerViewHelper.AdaptadorTickets
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.core.R
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import raul.ochoa.helpdesk.databinding.FragmentHomeBinding
import java.sql.SQLException
import java.util.UUID

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
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val txtTitulo = binding.txtTitulo
        val txtDescripcion = binding.txtDescripcion
        val txtAutor = binding.txtAutor
        val txtEmailAutor = binding.txtEmailAutor
        val txtFechaCreacion = binding.txtFechaCreacion
        val spinEstado = binding.spinEstado
        val txtFechaFinalizado = binding.txtFechaFinalizado
        val btnAgregarTicket = binding.btnAgregarTicket
        val estados = listOf("Activo", "Finalizado")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, estados)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinEstado.adapter = adapter
        binding.btnAgregarTicket.setOnClickListener {

            GlobalScope.launch(Dispatchers.IO) {
                try {
                    val objConexion = ClaseConexion().cadenaConexion()

                    val addTicket = objConexion?.prepareStatement("insert into tbTickets (numeroTicket, titulo, descripcion, autor, emailAutor, fechaCreacion, estadoTicket, fechaFinalizado) values (?, ?, ?, ?, ?, ?, ?, ?)")!!
                    addTicket.setString(1, UUID.randomUUID().toString())
                    addTicket.setString(2, txtTitulo.text.toString())
                    addTicket.setString(3, txtDescripcion.text.toString())
                    addTicket.setString(4, txtAutor.text.toString())
                    addTicket.setString(5, txtEmailAutor.text.toString())
                    addTicket.setString(6, txtFechaCreacion.text.toString())
                    addTicket.setString(7, spinEstado.selectedItem.toString())
                    addTicket.setString(8, txtFechaFinalizado.text.toString())
                    val resultado = addTicket.executeUpdate()


                    withContext(Dispatchers.Main) {
                        Toast.makeText(requireContext(), "Ticket creado con éxito", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(requireContext(), "Error al crear el ticket: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
                    e.printStackTrace()
                }
            }
        }




        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}