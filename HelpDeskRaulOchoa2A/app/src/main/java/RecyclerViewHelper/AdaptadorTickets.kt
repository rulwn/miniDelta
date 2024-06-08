package RecyclerViewHelper

import Modelo.ClaseConexion
import Modelo.tbTickets
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import raul.ochoa.helpdesk.R

class AdaptadorTickets(private var Datos: List<tbTickets>): RecyclerView.Adapter<ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val vista = LayoutInflater.from(parent.context).inflate(R.layout.activity_item_card, parent, false)
        return ViewHolder(vista)
    }

    fun Actualizarlista(nuevalista: List<tbTickets>){
        Datos = nuevalista
        notifyDataSetChanged()
    }

    fun Actualizarlistadespuesdecargardatos(numeroTicket: String, nuevotitulo: String, nuevodescripcion: String, nuevoautor: String, nuevoemailautor: String, nuevofechacreacion: String, nuevoestado: String, nuevofechafinalizado: String){
        val index = Datos.indexOfFirst { it.numeroTicket == numeroTicket }
        Datos[index].titulo = nuevotitulo
        Datos[index].descripcion = nuevodescripcion
        Datos[index].autor = nuevoautor
        Datos[index].emailautor = nuevoemailautor
        Datos[index].fechacreacion = nuevofechacreacion
        Datos[index].estado = nuevoestado
        Datos[index].fechafinalizado = nuevofechafinalizado
        notifyItemChanged(index)
    }

    fun Eliminarlista(titulo: String, posicion: Int){
        //1. crear clase conexion
        //2. Quitar elemento de la lista
        val listaDatos = Datos.toMutableList()
        listaDatos.removeAt(posicion)
        //Quitar de la base
        GlobalScope.launch(Dispatchers.IO){
            val objConexion = ClaseConexion().cadenaConexion()
            val delTickets = objConexion?.prepareStatement("Delete tbTickets where titulo = ?")!!
            delTickets.setString(1, titulo)
            delTickets.executeUpdate()
            val commit = objConexion.prepareStatement("commit")!!
            commit.executeUpdate()
        }
        //notificamos que se eliminaron los datos
        Datos = listaDatos.toList()
        notifyItemRemoved(posicion)
        notifyDataSetChanged()
    }
    fun actualizarProductos(titulo: String, descripcion: String, autor: String, emailautor: String, fechacreacion: String, estado: String, fechafinalizado: String, numeroTicket: String){
        GlobalScope.launch(Dispatchers.IO){
            val objConexion = ClaseConexion().cadenaConexion()
            val updateProductos = objConexion?.prepareStatement("Update tbTickets set titulo = ?, descripcion = ?, autor = ?, emailautor = ?, fechacreacion = ?, estadoticket = ?, fechafinalizado = ? where numeroTicket = ?")!!
            updateProductos.setString(1, titulo)
            updateProductos.setString(2, descripcion)
            updateProductos.setString(3, autor)
            updateProductos.setString(4, emailautor)
            updateProductos.setString(5, fechacreacion)
            updateProductos.setString(6, estado)
            updateProductos.setString(7, fechafinalizado)
            updateProductos.setString(8, numeroTicket)
            updateProductos.executeUpdate()

            val commit = objConexion.prepareStatement("commit")!!
            commit.executeUpdate()

            withContext(Dispatchers.Main){
                Actualizarlistadespuesdecargardatos(numeroTicket, titulo, descripcion, autor, emailautor, fechacreacion, estado, fechafinalizado)
            }
        }

    }

    override fun getItemCount() = Datos.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val ticket = Datos[position]
        holder.txtNombreCard.text = ticket.titulo
        holder.txtDescripcionCard.text = ticket.descripcion
        holder.txtAutorCard.text = ticket.autor
        holder.txtEmailAutorCard.text = ticket.emailautor
        holder.txtFechaCreacionCard.text = ticket.fechacreacion
        holder.txtEstadoCard.text = ticket.estado
        holder.txtFechaFinalizacionCard.text = ticket.fechafinalizado

        val item = Datos[position]
        holder.imgborrar.setOnClickListener {
            val context = holder.itemView.context
            val builder = AlertDialog.Builder(context)

            builder.setTitle("Seguro?")
            builder.setMessage("Deseas eliminar el registro?")
            builder.setPositiveButton("Si") { dialog, which ->
                Eliminarlista(item.titulo, position)

            }
            builder.setNegativeButton("No") { dialog, which ->

            }
            val alertDialog = builder.create()
            alertDialog.show()
        }

        holder.imgeditar.setOnClickListener {
            val context = holder.itemView.context
            val builder = AlertDialog.Builder(context)
            builder.setTitle("Editar Ticket")

            val cuadritoNuevoTitulo = EditText(context).apply {
                setText(item.titulo)
            }
            val cuadritoNuevoDes = EditText(context).apply {
                setText(item.descripcion)
            }
            val cuadritoNuevoAutor = EditText(context).apply {
                setText(item.autor)
            }
            val cuadritoNuevoEmail = EditText(context).apply {
                setText(item.emailautor)
            }
            val cuadritoNuevoFechaCreacion = EditText(context).apply {
                setText(item.fechacreacion)
            }
            val cuadritoNuevoEstado = EditText(context).apply {
                setText(item.estado)
            }
            val cuadritoNuevoFechaFinalizado = EditText(context).apply {
                setText(item.fechafinalizado)
            }

            val layout = LinearLayout(context).apply {
                orientation = LinearLayout.VERTICAL
                addView(cuadritoNuevoTitulo)
                addView(cuadritoNuevoDes)
                addView(cuadritoNuevoAutor)
                addView(cuadritoNuevoEmail)
                addView(cuadritoNuevoFechaCreacion)
                addView(cuadritoNuevoEstado)
                addView(cuadritoNuevoFechaFinalizado)
            }
            builder.setView(layout)
            builder.setPositiveButton("Si") { dialog, which ->
                actualizarProductos(cuadritoNuevoTitulo.text.toString(), cuadritoNuevoDes.text.toString(), cuadritoNuevoAutor.text.toString(), cuadritoNuevoEmail.text.toString(), cuadritoNuevoFechaCreacion.text.toString(), cuadritoNuevoEstado.text.toString(), cuadritoNuevoFechaFinalizado.text.toString(), item.numeroTicket)
            }
            builder.setNegativeButton("No") { dialog, which ->
                dialog.dismiss()
            }
            val alertDialog = builder.create()
            alertDialog.show()
        }
    }
}