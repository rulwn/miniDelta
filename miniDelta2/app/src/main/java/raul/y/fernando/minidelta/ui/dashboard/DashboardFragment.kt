package raul.y.fernando.minidelta.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import raul.y.fernando.minidelta.AdaptadorMedicamentos
import raul.y.fernando.minidelta.AdaptadorPacientes
import raul.y.fernando.minidelta.ClaseConexion
import raul.y.fernando.minidelta.R
import raul.y.fernando.minidelta.dataClassMedicamentos
import raul.y.fernando.minidelta.databinding.FragmentDashboardBinding
import java.sql.ResultSet
import java.sql.SQLException
import java.sql.Statement

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

        return root
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}