package Modelo

import java.util.Date

data class tbTickets(
    val numeroTicket: String,
    var titulo: String,
    var descripcion: String,
    var autor: String,
    var emailautor: String,
    var fechacreacion: String,
    var estado: String,
    var fechafinalizado : String
)
