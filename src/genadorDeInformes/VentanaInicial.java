package genadorDeInformes;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.ResultSet;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;

/**
 * Esta clase se encarga de los componentes de la vista principal de la aplicacion.
 * Muestra los datos obtenidos de la conexion a la base de datos
 * @author Carlos J. del Campo Cebrian
 * @version: 1.1.0
 *
 */

public class VentanaInicial extends JFrame implements MouseListener {


	private JPanel pPrincipal;
	/* Componentes de la tabla */
	private JScrollPane scroll;
	private String [] cabecera;
	private Object [][] datos;
	private DefaultTableModel dtm;
	private JTable tabla;
	private JMenuBar barraMenu;
	private JMenu miMenu;
	private JMenuItem salir;
	private JLabel lblInfo;
	

	/**
	 * Constructor de la vista principal
	 * @param con Con este parametro recibimos la conexion.
	 */
	public VentanaInicial(Conexion con)
	{
		initComponents();
		setBounds(100, 100, 524, 250);
		setTitle("LISTADO DE CLIENTES");
		setLocation(275, 235);
		setVisible(true);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	/**
	 * Metodo encargado de la gestion de los componentes visuales
	 */
	public void initComponents()
	{
		pPrincipal = new JPanel();

		/* INICIALIZACION DE LA TABLA */
		scroll = new JScrollPane();
		cabecera = new String[] {"ID","NOMBRE","APELLIDO", "CALLE", "CIUDAD"};
		dtm = new DefaultTableModel(datos,cabecera);
		tabla = new JTable(dtm);
		tabla.addMouseListener(this);
		pPrincipal.setLayout(new BorderLayout(0, 0));
		scroll.setViewportView(tabla);
		pPrincipal.add(scroll, BorderLayout.CENTER);
		
		//MENU
		barraMenu = new JMenuBar();
		miMenu = new JMenu();
		miMenu.setText("Archivo");
		barraMenu.add(miMenu);	
		salir = new JMenuItem("Salir");
		salir.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Apéndice de método generado automáticamente
				Conexion.CerrarCon();
				JOptionPane.showMessageDialog(null, "Hasta pronto", "Salir", JOptionPane.INFORMATION_MESSAGE);
				System.exit(0);	
			}
		});
		miMenu.add(salir);
		pPrincipal.add(barraMenu, BorderLayout.NORTH);		
		lblInfo = new JLabel("Selecciona un cliente para visualizar todas sus facturas");
		lblInfo.setHorizontalAlignment(SwingConstants.CENTER);
		pPrincipal.add(lblInfo, BorderLayout.SOUTH);
		setContentPane(pPrincipal);
	}

	/**
	 * Metodo encargado de borrar todos los datos obtenidos en la tabla en caso de realizar una consulta nueva.
	 */
	public void BorrarTabla()
	{
		int iNumReg = dtm.getRowCount();
		
		for(int i=0; i< iNumReg; i++)
		{
			dtm.removeRow(0);
		}
	}
	
	/**
	 * Metodo responsable de la obtencion y volcado de datos obtenidos en la consulta a la BBDD
	 */
	public void CargarTablaBBDD()
	{
		BorrarTabla();
		String sqlStr = "SELECT * FROM CUSTOMER";
		try {
			ResultSet rset = Conexion.stmt.executeQuery(sqlStr);
			int ID;
			String firstname;
			String lastname;
			String street;
			String city;

			while(rset.next()) {
				ID = rset.getInt("ID");
				firstname = rset.getString("FIRSTNAME");
				lastname = rset.getString("LASTNAME");
				street = rset.getString("STREET");
				city = rset.getString("CITY");
				dtm.addRow(new Object[] {ID, firstname, lastname, street, city});
			}

		}
		catch (Exception ex){
			ex.printStackTrace();
		}

	}

	@Override
	public void mouseClicked(MouseEvent arg0) 
	{
		int idCliente = (int) dtm.getValueAt(tabla.getSelectedRow(), 0);

		Jdialog dialog = new Jdialog(idCliente,Conexion.conn);
		dialog.setModal(true);
		dialog.setVisible(true);
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {}
	@Override
	public void mouseExited(MouseEvent arg0) {}
	@Override
	public void mousePressed(MouseEvent arg0) {}
	@Override
	public void mouseReleased(MouseEvent arg0) {}

}
