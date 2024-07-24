package raul.y.fernando.minidelta

import java.sql.Date

data class dataClassPacientesMedicamentos(
    val ID_pacientes_medicamentos: Int,
    val id_paciente: Int,
    val Nombre_Medicamento: String,
    val Hora_Aplicacion: Date
)