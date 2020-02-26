package genadorDeInformes;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Esta clase establece una conexion con una base de datos 
 * @author Carlos J. del Campo Cebrian
 * @version: 1.1.0
 *
 */


public class Conexion {
	
	static Connection conn = null; 
	static Statement stmt = null;

	/**
	 * Estable la conexion con una base de datos para poder realizar consultas en ella.
	 * @exception SQLException si la conexion no se consigue realizar con exito
	 * @return devuelve la conexion 
	 */
	public static Connection Conectar()
	{
		String db ="jdbc:hsqldb:hsql://localhost/testdb;ifexists=true";
		String user = "SA";
		String password = "";

		try {

			conn = DriverManager.getConnection(db,user,password);
			stmt = conn.createStatement();
		}
		catch (SQLException ex){
			ex.printStackTrace();
		}

		return conn;
	}

	/**
	 * Metodo para cerrar la conexion.
	 * @exception SQLException si no es posible cerrar la conexion
	 */
	public static void CerrarCon()
	{

		try {
			if (stmt != null) stmt.close();
			if (conn != null) conn.close();
		}
		catch (SQLException ex) {
			ex.printStackTrace();
		}
	}
}