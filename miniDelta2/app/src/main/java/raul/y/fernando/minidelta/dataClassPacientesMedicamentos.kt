package raul.y.fernando.minidelta

import oracle.sql.DATE

data class dataClassPacientesMedicamentos(
    val ID_pacientes_medicamentos: Int,
    val id_paciente: Int,
    val id_medicamento: Int,
    val Hora_Aplicacion: DATE
)