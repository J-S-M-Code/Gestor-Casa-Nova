package com.gestorCN.ui.sales;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;

import com.gestorCN.logic.sales.GestorVentas;
import com.gestorCN.logic.sales.Venta;
import com.gestorCN.logic.stock.GestorPrendas;
import com.toedter.calendar.JDateChooser;
import java.awt.Dimension;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JTextField;

public class PanelReporteFacturas extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private GestorVentas gestorVentas;
	private GestorPrendas gestorPrendas;
	private JDateChooser dateChooserDesde;
	private JDateChooser dateChooserHasta;
	private JTable table;
	private JButton btnCargar;
	private JButton btnItemsFactura;
	private JButton btnDetallesFacturacion;
	private ArrayList<Venta> ventasFechas;
	private DefaultTableModel model;
	private JTextField textFieldMonto;
	private JTextField textFieldCosto;
	private JTextField textFieldGanancia;
	private Map<Integer, Venta> mapaFilaVenta = new HashMap<>();
	
	public PanelReporteFacturas(PanelGestorVentas panelGestorVentas, 
			GestorVentas gestorVentas, GestorPrendas gestorprendas) {
		setTitle("Casa Nova - Reportes Facturas");
		
		Image icono = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/images/icono.jpg"));
	    setIconImage(icono);
		
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setMinimumSize(new Dimension(810, 600));
		setBounds(100, 100, 810, 600);
		setLocationRelativeTo(null);
		
		this.gestorVentas = gestorVentas;
		this.gestorPrendas = gestorprendas;
		
		addWindowListener(new WindowAdapter() { //Para cuando cierre se active la pestaña anterior 
			@Override
			public void windowClosing(WindowEvent e) {
				panelGestorVentas.setEnabled(true);
				panelGestorVentas.toFront();
			}
		});
		
		iniciarComponentes();
	}

	@SuppressWarnings("serial")
	private void iniciarComponentes() {
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		JPanel panelSuperior = new JPanel();
		contentPane.add(panelSuperior, BorderLayout.NORTH);
		
		JLabel lblFechaDesde = new JLabel("Desde: ");
		lblFechaDesde.setFont(new Font("Tahoma", Font.PLAIN, 15));
		
		dateChooserDesde = new JDateChooser();
		dateChooserDesde.getCalendarButton().setFont(new Font("Tahoma", Font.PLAIN, 11));
		dateChooserDesde.setMinimumSize(new Dimension(60, 20));
		dateChooserDesde.setFont(new Font("Tahoma", Font.PLAIN, 15));
		dateChooserDesde.setDateFormatString("dd/MM/yyyy");
		
		JLabel lblFechaHasta = new JLabel("Hasta: ");
		lblFechaHasta.setFont(new Font("Tahoma", Font.PLAIN, 15));
		
		dateChooserHasta = new JDateChooser();
		dateChooserHasta.getCalendarButton().setFont(new Font("Tahoma", Font.PLAIN, 15));
		dateChooserHasta.setMinimumSize(new Dimension(60, 20));
		dateChooserHasta.setFont(new Font("Tahoma", Font.PLAIN, 15));
		dateChooserHasta.setDateFormatString("dd/MM/yyyy");
		
		btnCargar = new JButton("Cargar");
		btnCargar.setFont(new Font("Tahoma", Font.PLAIN, 15));
		btnCargar.addActionListener(this);
		
		btnItemsFactura = new JButton("Items Venta");
		btnItemsFactura.setFont(new Font("Tahoma", Font.PLAIN, 15));
		btnItemsFactura.addActionListener(this);
		GroupLayout gl_panelSuperior = new GroupLayout(panelSuperior);
		gl_panelSuperior.setHorizontalGroup(
			gl_panelSuperior.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panelSuperior.createSequentialGroup()
					.addGap(71)
					.addComponent(lblFechaDesde)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(dateChooserDesde, GroupLayout.DEFAULT_SIZE, 124, Short.MAX_VALUE)
					.addGap(32)
					.addComponent(lblFechaHasta)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(dateChooserHasta, GroupLayout.DEFAULT_SIZE, 133, Short.MAX_VALUE)
					.addGap(33)
					.addComponent(btnCargar, GroupLayout.DEFAULT_SIZE, 75, Short.MAX_VALUE)
					.addGap(41)
					.addComponent(btnItemsFactura, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
					.addGap(19))
		);
		gl_panelSuperior.setVerticalGroup(
			gl_panelSuperior.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panelSuperior.createSequentialGroup()
					.addGap(18)
					.addGroup(gl_panelSuperior.createParallelGroup(Alignment.LEADING, false)
						.addComponent(dateChooserDesde, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addGroup(Alignment.TRAILING, gl_panelSuperior.createParallelGroup(Alignment.BASELINE)
							.addComponent(btnCargar, GroupLayout.PREFERRED_SIZE, 20, Short.MAX_VALUE)
							.addComponent(btnItemsFactura, 0, 0, Short.MAX_VALUE))
						.addComponent(dateChooserHasta, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(lblFechaHasta)
						.addGroup(gl_panelSuperior.createSequentialGroup()
							.addGap(1)
							.addComponent(lblFechaDesde)))
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);
		panelSuperior.setLayout(gl_panelSuperior);
		
		JPanel panelCentral = new JPanel();
		contentPane.add(panelCentral, BorderLayout.CENTER);
		panelCentral.setLayout(new BorderLayout(0, 0));
		
		JScrollPane scrollPane = new JScrollPane();
		panelCentral.add(scrollPane);
		
		String[] columnas = {"Nro. Venta", "Fecha", "Monto", "Costo", 
							"Medio Pago", "Coutas", "Items"};
		model = new DefaultTableModel(columnas, 0);
		
		table = new JTable(model) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		/* Alineacion de textos y numeros */
		DefaultTableCellRenderer alineacionDerecha = new DefaultTableCellRenderer();
        alineacionDerecha.setHorizontalAlignment(SwingConstants.RIGHT);
        DefaultTableCellRenderer alineacionIzquierda = new DefaultTableCellRenderer();
        alineacionIzquierda.setHorizontalAlignment(SwingConstants.LEFT);
        for (int i = 0; i <= 6; i++) {
        	table.getColumnModel().getColumn(i).setCellRenderer(alineacionDerecha);
        }
        table.getColumnModel().getColumn(4).setCellRenderer(alineacionIzquierda);
        
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setShowVerticalLines(false);
        table.setFont(new Font("Tahoma", Font.PLAIN, 15));
        table.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		scrollPane.setViewportView(table);
		
		JPanel panelInferioir = new JPanel();
		contentPane.add(panelInferioir, BorderLayout.SOUTH);
		
		JLabel lblMontoTotal = new JLabel("Monto Total: ");
		lblMontoTotal.setFont(new Font("Tahoma", Font.PLAIN, 15));
		panelInferioir.add(lblMontoTotal);
		
		textFieldMonto = new JTextField();
		textFieldMonto.setEditable(false);
		panelInferioir.add(textFieldMonto);
		textFieldMonto.setColumns(10);
		
		JLabel lblCostoTotal = new JLabel("Costo Total: ");
		lblCostoTotal.setFont(new Font("Tahoma", Font.PLAIN, 15));
		panelInferioir.add(lblCostoTotal);
		
		textFieldCosto = new JTextField();
		textFieldCosto.setEditable(false);
		panelInferioir.add(textFieldCosto);
		textFieldCosto.setColumns(10);
		
		JLabel lblGanancia = new JLabel("Ganancia: ");
		lblGanancia.setFont(new Font("Tahoma", Font.PLAIN, 15));
		panelInferioir.add(lblGanancia);
		
		textFieldGanancia = new JTextField();
		textFieldGanancia.setEditable(false);
		panelInferioir.add(textFieldGanancia);
		textFieldGanancia.setColumns(10);
		
		JLabel lblSeparador = new JLabel("                     ");
		panelInferioir.add(lblSeparador);
		
		btnDetallesFacturacion = new JButton("Detalles");
		btnDetallesFacturacion.setFont(new Font("Tahoma", Font.PLAIN, 15));
		btnDetallesFacturacion.addActionListener(this);
		panelInferioir.add(btnDetallesFacturacion);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btnCargar) {
			ventasFechas = new ArrayList<Venta>();
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			String fechaDesde = (dateChooserDesde.getDate() != null) 
					? sdf.format(dateChooserDesde.getDate()) : "No seleccionada";
			String fechaHasta = (dateChooserHasta.getDate() != null) 
					? sdf.format(dateChooserHasta.getDate()) : "No seleccionada";

			/* Llamar al gestor y traer las facturas comprendidas entre las fechas*/
			if(verificarFecha(fechaDesde, fechaHasta)) {
				ventasFechas = gestorVentas.getReporteVentas(fechaDesde, fechaHasta);
			}
			
			/* Cargo los elementos en la tabla */
			actualizarTabla();
			/* Calculo y cargo los campos*/
			cargarCampos();
		}
		
		if (e.getSource() == btnDetallesFacturacion) {
			if (ventasFechas == null || ventasFechas.size() == 0) {
				JOptionPane.showMessageDialog(this, 
			            "Seleccione un periodo con ventas", 
			            "Notificación", 
			            JOptionPane.INFORMATION_MESSAGE);
			} else {
				setEnabled(false);
				PanelDetallesFacturacion pdf = new PanelDetallesFacturacion(this, ventasFechas, gestorPrendas);
				pdf.setVisible(true);
				pdf.toFront();
			}
			
		}
		
		if (e.getSource() == btnItemsFactura) {
			int seleccion = table.getSelectedRow();
			if (seleccion != -1) {
				if (mapaFilaVenta.containsKey(seleccion)) {
					setEnabled(false);
					PanelItemsFactura pdf = new PanelItemsFactura(this, 
									mapaFilaVenta.get(seleccion), gestorPrendas);
					pdf.setVisible(true);
					pdf.toFront();
				}
			} else {
				JOptionPane.showMessageDialog(this, 
			            "Seleccione una venta", 
			            "Notificación", 
			            JOptionPane.INFORMATION_MESSAGE);
			}
		}
	}

	private void cargarCampos() {
		double montoTotal = 0;
		double costoTotal = 0;
		double gananciaTotal = 0;
		if (ventasFechas != null) {
			for (Venta v : ventasFechas) {
				montoTotal += v.getMonto();
				costoTotal += v.getCosto();
			}
		}
		gananciaTotal = montoTotal - costoTotal;
		
		textFieldMonto.setText(Double.toString(montoTotal));
		textFieldCosto.setText(Double.toString(costoTotal));
		textFieldGanancia.setText(Double.toString(gananciaTotal));
	}

	private void actualizarTabla() {
		model.setRowCount(0);
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		mapaFilaVenta.clear();
		int rowIndex = 0;
		if (ventasFechas != null) {
			for (Venta v : ventasFechas) {
				Object[] fila = {
						v.getNroVenta(),
						sdf.format(v.getFecha().getTime()),
						v.getMonto(),
						v.getCosto(),
						v.getMedioPago(),
						v.getCuotas(),
						v.getIdPrendasVenta().size()
				};
				model.addRow(fila);
				mapaFilaVenta.put(rowIndex++, v);
			}
		}	
	}

	private boolean verificarFecha(String fechaDesde, String fechaHasta) {
		String txtDefault = "No seleccionada";
		Calendar hoy = GregorianCalendar.getInstance();
		GregorianCalendar desde;
		GregorianCalendar hasta;
		if (!fechaDesde.equals(txtDefault) && !fechaHasta.equals(txtDefault)) {
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		    try {
		        desde = new GregorianCalendar();
		        desde.setTime(sdf.parse(fechaDesde));

		        hasta = new GregorianCalendar();
		        hasta.setTime(sdf.parse(fechaHasta));


		        if (hasta.before(hoy) || desde.before(hasta)) {
		            return true;
		        }
		    } catch (ParseException e) {
		        e.printStackTrace();
		    }
		}
		return false;
	}

}
