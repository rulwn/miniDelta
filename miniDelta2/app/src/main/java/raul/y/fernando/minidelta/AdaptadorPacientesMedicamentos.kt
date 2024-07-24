import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import raul.y.fernando.minidelta.R
import raul.y.fernando.minidelta.dataClassPacientesMedicamentos

class AdaptadorPacientesMedicamentos(
    private var items: List<dataClassPacientesMedicamentos>
) : RecyclerView.Adapter<AdaptadorPacientesMedicamentos.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nombreMed: TextView = view.findViewById(R.id.txtMedicamentoPaci)
        val hora: TextView = view.findViewById(R.id.txtHora)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.activity_detallepaciente, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.nombreMed.text = item.Nombre_Medicamento
        holder.hora.text = item.Hora_Aplicacion.toString()
    }

    override fun getItemCount(): Int {
        return items.size
    }
/*
    fun actualizarDatos(nuevosItems: List<dataClassPacientesMedicamentos>) {
        items = nuevosItems
        notifyDataSetChanged()
    }
 */
}
