import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextClock
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import raul.y.fernando.minidelta.R
import raul.y.fernando.minidelta.dataClassPacientesMedicamentos

class ViewHelperPacientesMedicamento(view: View) : RecyclerView.ViewHolder(view) {

    val txtHora: TextClock = view.findViewById(R.id.txtHora)
    val txtMedicamentoPaci: TextView = view.findViewById(R.id.txtMedicamentoPaci)

    class AdaptadorPacientesMedicamentos(
        private var datos: List<dataClassPacientesMedicamentos>,
        private val onClick: (dataClassPacientesMedicamentos) -> Unit
    ) : RecyclerView.Adapter<ViewHelperPacientesMedicamento>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHelperPacientesMedicamento {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.activity_item_pacientemed, parent, false)
            return ViewHelperPacientesMedicamento(view)
        }

        override fun onBindViewHolder(holder: ViewHelperPacientesMedicamento, position: Int) {
            val mediPaci = datos[position]
            holder.txtMedicamentoPaci.text = mediPaci.Nombre_Medicamento
            holder.txtHora.text = mediPaci.Hora_Aplicacion.toString()
        }
        override fun getItemCount(): Int {
            return datos.size
        }

        fun actualizarLista(newList: List<dataClassPacientesMedicamentos>) {
            datos = newList
            notifyDataSetChanged()
        }
    }
}
