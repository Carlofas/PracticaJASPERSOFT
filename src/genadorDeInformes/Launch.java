package genadorDeInformes;

/**
 * Clase para iniciar la aplicacion.
 * @author Carlos J. del Campo Cebrian
 * @version: 1.1.0
 *
 */
public class Launch {

	public static void main(String[] args) {
		
		VentanaInicial miVen = new VentanaInicial(new Conexion());
		Conexion.Conectar();
		miVen.CargarTablaBBDD();
	}

}
