package raul.y.fernando.minidelta

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class viewHelperPacientes(view: View) : RecyclerView.ViewHolder(view) {
    val txtNombre: TextView = view.findViewById(R.id.txtNombre)
    val imgOpciones: ImageView = view.findViewById(R.id.imgOpciones)
}