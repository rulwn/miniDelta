package raul.y.fernando.minidelta

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class viewHelperMedicamentos(view: View) : RecyclerView.ViewHolder(view) {
    val txtMedicina: TextView = view.findViewById(R.id.txtMedicina)
    val imgOpciones: ImageView = view.findViewById(R.id.imgOpciones)
}