package com.gestorCN.ui.stock;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import com.gestorCN.logic.stock.Costo;
import com.gestorCN.logic.stock.Precio;
import com.gestorCN.logic.stock.Prenda;

import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import java.awt.Font;
import java.awt.Toolkit;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.awt.GridLayout;
import java.awt.Image;

public class PanelHistorialPrecioCosto extends JFrame {
	
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private DefaultTableModel modelPrecios;
	private DefaultTableModel modelCostos;
	private JTextField textFieldMarca;
	private JTextField textFieldTipo;
	private JTextField textFieldColor;
	private JTextField textFieldTalle;
	private JTextArea textAreaDescripcion;
	private JTable tablaPrecios;
	private JTable tableCostos;
	
	public PanelHistorialPrecioCosto(PanelGestorStock panelGestorPrendas, Prenda prenda) {
		setTitle("Casa Nova - Historial de Precios");

		Image icono = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/images/icono.jpg"));
	    setIconImage(icono);
		
		setAlwaysOnTop(true);
		setResizable(false);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 584, 275);
		setType(Type.UTILITY);
		setLocationRelativeTo(null);
		
		addWindowListener(new WindowAdapter() { //Para cuando cierre se active la pestaña anterior 
			@SuppressWarnings("static-access")
			@Override
			public void windowClosing(WindowEvent e) {
				panelGestorPrendas.setEnabled(true);
				panelGestorPrendas.toFront();
				panelGestorPrendas.actualizarListado(panelGestorPrendas.estado);
			}
		});
		
		iniciarComponentes();
		cargarPrecios(prenda);
		cargarCostos(prenda);
		cargarDatos(prenda);
	}
	
	@SuppressWarnings("serial")
	private void iniciarComponentes() {
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		
		JPanel panelDatosRopa = new JPanel();
		
		JPanel panelHistorial = new JPanel();
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addComponent(panelDatosRopa, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 558, Short.MAX_VALUE)
				.addComponent(panelHistorial, GroupLayout.DEFAULT_SIZE, 558, Short.MAX_VALUE)
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addComponent(panelDatosRopa, GroupLayout.PREFERRED_SIZE, 71, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(panelHistorial, GroupLayout.DEFAULT_SIZE, 276, Short.MAX_VALUE))
		);
		panelHistorial.setLayout(new GridLayout(1, 0, 1, 0));
		
		
		String[] columnasPrecios = {"Precios", "Desde", "Hasta"};
		String[] columnasCostos = {"Costo", "Desde", "Hasta"};
		modelPrecios = new DefaultTableModel(columnasPrecios, 0);
		modelCostos = new DefaultTableModel(columnasCostos, 0);
		
		JScrollPane scrollPaneCosto = new JScrollPane();
		panelHistorial.add(scrollPaneCosto);
		tableCostos = new JTable();
		tableCostos = new JTable(modelCostos) {
            @Override
            public boolean isCellEditable(int row, int column) {// Hace todas las celdas no editables
                return false; 
            }
        };
        tableCostos.setFont(new Font("Tahoma", Font.PLAIN, 15));
		scrollPaneCosto.setViewportView(tableCostos);
		panelDatosRopa.setLayout(null);
		
		JScrollPane scrollPanePrecio = new JScrollPane();
		panelHistorial.add(scrollPanePrecio);
		tablaPrecios = new JTable(modelPrecios) {
            @Override
            public boolean isCellEditable(int row, int column) {// Hace todas las celdas no editables
                return false; 
            }
        };
		tablaPrecios.setFont(new Font("Tahoma", Font.PLAIN, 15));
		scrollPanePrecio.setViewportView(tablaPrecios);
		panelDatosRopa.setLayout(null);
		
		
		
		JLabel lblMarca = new JLabel("Marca:");
		lblMarca.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblMarca.setBounds(10, 8, 80, 20);
		panelDatosRopa.add(lblMarca);
		
		textFieldMarca = new JTextField();
		textFieldMarca.setEditable(false);
		textFieldMarca.setBounds(94, 10, 86, 20);
		panelDatosRopa.add(textFieldMarca);
		textFieldMarca.setColumns(10);
		
		JLabel lblTipo = new JLabel("Tipo:");
		lblTipo.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblTipo.setBounds(185, 10, 65, 17);
		panelDatosRopa.add(lblTipo);
		
		textFieldTipo = new JTextField();
		textFieldTipo.setEditable(false);
		textFieldTipo.setBounds(260, 10, 86, 20);
		panelDatosRopa.add(textFieldTipo);
		textFieldTipo.setColumns(10);
		
		JLabel lblTalle = new JLabel("Talle:");
		lblTalle.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblTalle.setBounds(10, 42, 80, 17);
		panelDatosRopa.add(lblTalle);
		
		textFieldColor = new JTextField();
		textFieldColor.setEditable(false);
		textFieldColor.setBounds(427, 10, 86, 20);
		panelDatosRopa.add(textFieldColor);
		textFieldColor.setColumns(10);
		
		JLabel lblColor = new JLabel("Color:");
		lblColor.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblColor.setBounds(352, 11, 65, 20);
		panelDatosRopa.add(lblColor);
		
		textFieldTalle = new JTextField();
		textFieldTalle.setEditable(false);
		textFieldTalle.setBounds(94, 42, 86, 20);
		panelDatosRopa.add(textFieldTalle);
		textFieldTalle.setColumns(10);
		
		JLabel lblDescripcion = new JLabel("Descripción:");
		lblDescripcion.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblDescripcion.setBounds(185, 39, 80, 20);
		panelDatosRopa.add(lblDescripcion);
		
		textAreaDescripcion = new JTextArea();
		textAreaDescripcion.setEditable(false);
		textAreaDescripcion.setBounds(270, 40, 288, 29);
		panelDatosRopa.add(textAreaDescripcion);
		contentPane.setLayout(gl_contentPane);
	}
	
	private void cargarDatos(Prenda prenda) {
		textFieldMarca.setText(prenda.getMarca());
		textFieldTipo.setText(prenda.getTipo());
		textFieldColor.setText(prenda.getColor());
		textFieldTalle.setText(prenda.getTalle());
		textAreaDescripcion.setText(prenda.getDescripcion());
	}
	
	private void cargarPrecios(Prenda prenda) {
		ArrayList<Precio> preciosRopa = prenda.getListaPrecio();
		
		
		int valoresAMostrar = Math.max(0, preciosRopa.size()-5);
		if (preciosRopa != null) {
			for (int i = preciosRopa.size()-1; i >= valoresAMostrar; i--) {
				Precio p = preciosRopa.get(i);
				Object[] fila = {
						p.getValor(),
						p.getFechaDesdeString(),
						p.getFechaHastaString()
				};
				modelPrecios.addRow(fila);
				
			}
		}
	}
	
	private void cargarCostos(Prenda prenda) {
		ArrayList<Costo> costosRopa = prenda.getListaCosto();
		int valoresAMostrar = Math.max(0, costosRopa.size()-5);
		
		if (costosRopa != null) {
			for (int i = costosRopa.size()-1; i >= valoresAMostrar; i--) {
				Costo c = costosRopa.get(i);
				Object[] fila = {
						c.getCosto(),
						c.getFechaDesdeString(),
						c.getFechaHastaString(),
						"|", 
						"",
						"",
						""
				};
				modelCostos.addRow(fila);
			}
		}
	}
}
