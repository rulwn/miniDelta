package RecyclerViewHelper

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import raul.ochoa.helpdesk.R

class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
    val txtNombreCard = view.findViewById<TextView>(R.id.txtNombreCard)
    val txtDescripcionCard = view.findViewById<TextView>(R.id.txtDescripcionCard)
    val txtAutorCard = view.findViewById<TextView>(R.id.txtAutorCard)
    val txtEmailAutorCard = view.findViewById<TextView>(R.id.txtEmailAutorCard)
    val txtFechaCreacionCard = view.findViewById<TextView>(R.id.txtFechaCreacionCard)
    val txtEstadoCard = view.findViewById<TextView>(R.id.txtEstadoCard)
    val txtFechaFinalizacionCard = view.findViewById<TextView>(R.id.txtFechaFinalizadoCard)
    val imgborrar: ImageView = view.findViewById(R.id.imgBorrar)
    val imgeditar: ImageView = view.findViewById(R.id.imgEditar)
}