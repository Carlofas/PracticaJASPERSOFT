package genadorDeInformes;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;

/**
 * Vista responsable de la creacion de informes mediante el uso de JasperSoft 
 * @author Carlos J. del Campo Cebrian
 * @version: 1.1.0
 *
 */
public class Jdialog extends JDialog implements MouseListener{

	private final JPanel contentPanel = new JPanel();
	/* Componentes de la tabla */
	private JScrollPane scroll;
	private String [] cabecera;
	private Object [][] datos;
	private DefaultTableModel dtm;
	private JTable tabla;
	private JFileChooser jfcSelector;
	private String ruta;
	private int idFactura;

	/**
	 * Contructor del dialogo emergente. 
	 * @param id Valor correspondiente al identificador de factura
	 * @param c Recibimos la conexion a la base de datos
	 */
	public Jdialog(int id, Connection c) {
		setTitle("FACTURAS DEL CLIENTE");
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		
		
		/* INICIALIZACION DE LA TABLA */
		scroll = new JScrollPane();
		cabecera = new String[] {"ID","CUSTOMER ID","TOTAL"};
		dtm = new DefaultTableModel(datos,cabecera);
		tabla = new JTable(dtm);
		tabla.addMouseListener(this);
		contentPanel.setLayout(new BorderLayout(0, 0));
		scroll.setViewportView(tabla);
		contentPanel.add(scroll);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.setActionCommand("OK");
				okButton.addActionListener(new ActionListener() {	
					@Override
					public void actionPerformed(ActionEvent arg0) {
						// TODO Apéndice de método generado automáticamente
						dispose();
					}
				});
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.setActionCommand("Cancel");
				cancelButton.addActionListener(new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent arg0) {
						// TODO Apéndice de método generado automáticamente
						dispose();
					}
				});
				buttonPane.add(cancelButton);
			}
		}
		CargarTablaBBDD(id);
	}


	/**
	 * Metodo responsable de limpiar los valores en la tabla.
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
	 * Metodo que solicita la informacion a la base de datos y la carga en la tabla
	 * @param id Identificador del numero de cliente
	 */
	public void CargarTablaBBDD(int id)
	{
		BorrarTabla();
		String sqlStr = "SELECT * FROM INVOICE WHERE CUSTOMERID="+id;

		try {
			ResultSet rset = Conexion.stmt.executeQuery(sqlStr);
			int ID;
			int CUSTOMERID;
			int TOTAL;

			while(rset.next()) {
				ID = rset.getInt("ID");
				CUSTOMERID = rset.getInt("CUSTOMERID");
				TOTAL = rset.getInt("TOTAL");
				dtm.addRow(new Object[] {ID, CUSTOMERID, TOTAL});
			}

		}
		catch (Exception ex){
			ex.printStackTrace();
		}
	}


	/**
	 * Metodo responsable de la generacion del informe en formato PDF.
	 * La ruta de destino es elegida por el usuario.
	 */
	@SuppressWarnings("deprecation")
	private void crearInforme() {
		jfcSelector = new JFileChooser();
		FileNameExtensionFilter filtroPDF=new FileNameExtensionFilter("PDF","pdf");
		jfcSelector.setFileFilter(filtroPDF);
		if (jfcSelector.showSaveDialog(this) == jfcSelector.APPROVE_OPTION) {
			ruta = jfcSelector.getSelectedFile().getAbsolutePath();

			try {
				
				Map<String, Object> parametros = new HashMap<String, Object>();
				parametros.put("idfactura", idFactura);
				
				JasperReport subreport = (JasperReport) JRLoader.loadObject(getClass().getResource("/informes/subCliente.jasper"));
				parametros.put("subreport", subreport);
				
				JasperReport report = (JasperReport) JRLoader.loadObject(getClass().getResource("/informes/Ejercicio1.jasper"));

				JasperPrint print = JasperFillManager.fillReport(report, parametros, Conexion.conn);
				JasperViewer.viewReport(print,false);
				JasperExportManager.exportReportToPdfFile(print,  ruta);
			
			} catch (JRException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Apéndice de método generado automáticamente
		idFactura = (int) dtm.getValueAt(tabla.getSelectedRow(), 0);
		System.out.println(idFactura);
		this.dispose();
		crearInforme();
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
