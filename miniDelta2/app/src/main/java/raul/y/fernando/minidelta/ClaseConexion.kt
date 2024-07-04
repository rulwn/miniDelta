package raul.y.fernando.minidelta

import java.sql.Connection
import java.sql.DriverManager

class ClaseConexion {
    fun cadenaConexion(): Connection? {
        try {
            val url = "jdbc:oracle:thin:@192.168.1.76:1521:xe"
            val usuario = "RAUL_DEVELOPER"
            val contrasena = "deltawins"
            val connection = DriverManager.getConnection(url, usuario, contrasena)
            return connection
        }catch (e: Exception){
            println("Error en la conexion AQUI!!!: $e")
            return null
        }
    }
}