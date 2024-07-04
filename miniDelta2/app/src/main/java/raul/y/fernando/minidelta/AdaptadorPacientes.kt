package raul.y.fernando.minidelta

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AdaptadorPacientes(private var Datos: List<dataClassPacientes>): RecyclerView.Adapter<viewHelperPacientes>()  {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHelperPacientes {
        val vista = LayoutInflater.from(parent.context).inflate(R.layout.activity_item_card_pacientes, parent, false)

        return viewHelperPacientes(vista)
    }

    override fun getItemCount() = Datos.size

    override fun onBindViewHolder(holder: viewHelperPacientes, position: Int) {
        val pacientes = Datos[position]
        val nombreCompleto = holder.itemView.context.getString(R.string.nombre_completo, pacientes.Nombres, pacientes.Apellidos)
        holder.txtNombre.text = nombreCompleto
        val item = Datos[position]
        holder.imgOpciones.setOnClickListener {
            val context = holder.itemView.context
            val pantalladetalles = Intent(context, activity_detallepaciente::class.java)
            pantalladetalles.putExtra("ID_Paciente", item.ID_Paciente)
            pantalladetalles.putExtra("Nombres", item.Nombres)
            pantalladetalles.putExtra("Apellidos", item.Apellidos)
            pantalladetalles.putExtra("Edad", item.Edad)
            pantalladetalles.putExtra("Enfermedad", item.Enfermedad)
            pantalladetalles.putExtra("Numero_Habitacion", item.Numero_Habitacion)
            pantalladetalles.putExtra("Numero_cama", item.Numero_cama)
            pantalladetalles.putExtra("id_habitacion", item.id_habitacion)
            context.startActivity(pantalladetalles)
        }
    }

    fun Actualizarlista(nuevalista: List<dataClassPacientes>){
        Datos = nuevalista
        notifyDataSetChanged()
    }

    fun Actualizarlistadespuesdecargardatos(ID_Paciente: Int, nuevoNombre: String, nuevoApellido: String, nuevoEdad: Int, nuevaEnfermedad: String, nuevoNumero_Habitacion: Int){
        val index = Datos.indexOfFirst { it.ID_Paciente == ID_Paciente }
        Datos[index].Nombres = nuevoNombre
        Datos[index].Apellidos = nuevoApellido
        Datos[index].Edad = nuevoEdad
        Datos[index].Enfermedad = nuevaEnfermedad
        Datos[index].Numero_Habitacion = nuevoNumero_Habitacion
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
            val delProductos = objConexion?.prepareStatement("Delete PacientesBloom where nombres = ?")!!

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

    fun actualizarPacientes(ID_Paciente: Int, nuevoNombre: String, nuevoApellido: String, nuevoEdad: Int, nuevaEnfermedad: String, nuevoNumero_Habitacion: Int){
        GlobalScope.launch(Dispatchers.IO){
            val objConexion = ClaseConexion().cadenaConexion()
            val updateProductos = objConexion?.prepareStatement("Update PacientesBloom set nombres = ?, Apellidos = ?, Edad = ?, Enfermedad = ?, Numero_Habitacion = ? where ID_Paciente = ?")!!
            updateProductos.setString(1, nuevoNombre)
            updateProductos.setString(2, nuevoApellido)
            updateProductos.setInt(3, nuevoEdad)
            updateProductos.setString(4, nuevaEnfermedad)
            updateProductos.setInt(5, nuevoNumero_Habitacion)
            updateProductos.setInt(6, ID_Paciente)
            updateProductos.executeUpdate()

            val commit = objConexion.prepareStatement("commit")!!
            commit.executeUpdate()

            withContext(Dispatchers.Main){
                Actualizarlistadespuesdecargardatos(ID_Paciente, nuevoNombre, nuevoApellido, nuevoEdad, nuevaEnfermedad, nuevoNumero_Habitacion)
            }
        }

    }
}