package raul.ochoa.helpdesk.ui.dashboard

import Modelo.ClaseConexion
import Modelo.tbTickets
import RecyclerViewHelper.AdaptadorTickets
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import raul.ochoa.helpdesk.databinding.FragmentDashboardBinding

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val dashboardViewModel =
            ViewModelProvider(this).get(DashboardViewModel::class.java)
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val rcvTickets = binding.rcvTickets

        rcvTickets.layoutManager = LinearLayoutManager(this.context)

        CoroutineScope(Dispatchers.IO).launch {
            val HelpDeskDB = obtenerTickets()
            withContext(Dispatchers.Main){
                val adapter = AdaptadorTickets(HelpDeskDB)
                rcvTickets.adapter = adapter
            }
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    fun obtenerTickets(): List<tbTickets>{
        val objConexion = ClaseConexion().cadenaConexion()
        val statement = objConexion?.createStatement()
        val resultSet = statement?.executeQuery("SELECT * FROM tbTickets")!!
        val ListaTickets = mutableListOf<tbTickets>()
        while (resultSet?.next() == true){
            val numeroTicket = resultSet.getString("numeroTicket")
            val titulo = resultSet.getString("titulo")
            val descripcion = resultSet.getString("descripcion")
            val autor = resultSet.getString("autor")
            val emailautor = resultSet.getString("emailautor")
            val fechacreacion = resultSet.getString("fechaCreacion")
            val estadoticket = resultSet.getString("estadoTicket")
            val fechafinalizado = resultSet.getString("fechaFinalizado")
            val ticket = tbTickets(numeroTicket, titulo, descripcion, autor, emailautor, fechacreacion, estadoticket, fechafinalizado)
            ListaTickets.add(ticket)
        }
        return ListaTickets
    }
}