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

}