package raul.y.fernando.minidelta

import java.sql.Connection
import java.sql.DriverManager

class ClaseConexion {
    fun cadenaConexion(): Connection? {
        try {
            val url = "jdbc:oracle:thin:@192.168.1.12:1521:xe"
            val usuario = "SYSTEM"
            val contrasena = "ITR2024"
            val connection = DriverManager.getConnection(url, usuario, contrasena)
            return connection
        }catch (e: Exception){
            println("Error en la conexion AQUI!!!: $e")
            return null
        }
    }
}

/*
--Raul--
val url = "jdbc:oracle:thin:@192.168.1.76:1521:xe"
val usuario = "RAUL_DEVELOPER"
val contrasena = "deltawins"

--Huezo--
val url = "jdbc:oracle:thin:@192.168.1.12:1521:xe"
val usuario = "SYSTEM"
val contrasena = "ITR2024"
*/
