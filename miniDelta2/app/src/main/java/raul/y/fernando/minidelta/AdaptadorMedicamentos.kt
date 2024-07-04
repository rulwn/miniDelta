package raul.y.fernando.minidelta

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AdaptadorMedicamentos(private var Datos: List<dataClassMedicamentos>): RecyclerView.Adapter<viewHelperMedicamentos>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHelperMedicamentos {
        val vista = LayoutInflater.from(parent.context).inflate(R.layout.activity_item_medicamentos, parent, false)

        return viewHelperMedicamentos(vista)
    }

    override fun getItemCount() = Datos.size

    override fun onBindViewHolder(holder: viewHelperMedicamentos, position: Int) {
        val medicamentos = Datos[position]
        holder.txtMedicina.text = medicamentos.Nombre_Medicamento
        val item = Datos[position]
        holder.imgOpciones.setOnClickListener {
            val context = holder.itemView.context
            val pantalladetalles = Intent(context, activity_detallemedicamento::class.java)
            pantalladetalles.putExtra("id_medicamento", item.id_medicamento)
            pantalladetalles.putExtra("Nombre_Medicamento", item.Nombre_Medicamento)
            pantalladetalles.putExtra("Descripcion", item.Descripcion)
            context.startActivity(pantalladetalles)
        }
    }

    fun Actualizarlista(nuevalista: List<dataClassMedicamentos>){
        Datos = nuevalista
        notifyDataSetChanged()
    }

    fun Actualizarlistadespuesdecargardatos(id_medicamento: Int, nuevoNombre: String, nuevoDescripcion: String){
        val index = Datos.indexOfFirst { it.id_medicamento == id_medicamento }
        Datos[index].Nombre_Medicamento = nuevoNombre
        Datos[index].Descripcion = nuevoDescripcion
        notifyItemChanged(index)
    }
    fun Eliminarlista(nombrePaciente: String, posicion: Int){
        //1. crear clase conexion
        //2. Quitar elemento de la lista
        val listaDatos = Datos.toMutableList()
        listaDatos.removeAt(posicion)
        //Quitar de la base
        GlobalScope.launch(Dispatchers.IO){
            val objConexion = ClaseConexion().cadenaConexion()
            val delProductos = objConexion?.prepareStatement("Delete Medicamentos where nombres = ?")!!

            delProductos.setString(1, nombrePaciente)
            delProductos.executeUpdate()
            val commit = objConexion.prepareStatement("commit")!!
            commit.executeUpdate()
        }
        //notificamos que se eliminaron los datos
        Datos = listaDatos.toList()
        notifyItemRemoved(posicion)
        notifyDataSetChanged()
    }

    fun actualizarPacientes(id_medicamento: Int, nuevoNombre: String, nuevoDescripcion: String){
        GlobalScope.launch(Dispatchers.IO){
            val objConexion = ClaseConexion().cadenaConexion()
            val updateProductos = objConexion?.prepareStatement("Update Medicamentos set Nombre_Medicamento = ?, Descripcion = ? where id_medicamento = ?")!!
            updateProductos.setString(1, nuevoNombre)
            updateProductos.setString(2, nuevoDescripcion)
            updateProductos.executeUpdate()

            val commit = objConexion.prepareStatement("commit")!!
            commit.executeUpdate()

            withContext(Dispatchers.Main){
                Actualizarlistadespuesdecargardatos(id_medicamento, nuevoNombre, nuevoDescripcion)
            }
        }

    }
}