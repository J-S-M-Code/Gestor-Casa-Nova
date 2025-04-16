package com.gestorCN.ui.sales;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.stream.Collectors;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import com.gestorCN.logic.sales.Venta;
import com.gestorCN.logic.stock.GestorPrendas;
import com.gestorCN.logic.stock.Prenda;

import java.awt.BorderLayout;
import java.awt.Cursor;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class PanelItemsFactura extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private Venta venta;
	private ArrayList<Prenda> listaPrenda;
	private JTable table;
	private DefaultTableModel model;
	private JPanel panelCentral;
	private JLabel lblNroVenta;
	private JTextField textFieldNroVenta;
	private JLabel lblFecha;
	private JTextField textFieldFecha;
	private JLabel lblMonto;
	private JTextField textFieldMonto;
	private JLabel lblCosto;
	private JTextField textFieldCosto;
	private JLabel lblSeparador1;
	private JLabel lblSeparador2;

	public PanelItemsFactura(PanelReporteFacturas prf, Venta venta, 
			GestorPrendas gp) {
		setResizable(false);
		setType(Type.POPUP);
		
		Image icono = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/images/icono.jpg"));
	    setIconImage(icono);
	    
	    setTitle("Casa Nova - Items Factura");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setMinimumSize(new Dimension(500, 400));
		setBounds(100, 100, 871, 462);
		setLocationRelativeTo(null);
		
		this.venta = venta;
		this.listaPrenda = gp.getPrendas();
		
		addWindowListener(new WindowAdapter() { //Para cuando cierre se active la pestaña anterior 
			@Override
			public void windowClosing(WindowEvent e) {
				prf.setEnabled(true);
				prf.toFront();
			}
		});
		
		iniciarComponentes();	
		cargarTabla();
	}

	@SuppressWarnings("serial")
	private void iniciarComponentes() {
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		JPanel panelSuperior = new JPanel();
		contentPane.add(panelSuperior, BorderLayout.NORTH);
		
		lblNroVenta = new JLabel("Numero de venta: ");
		panelSuperior.add(lblNroVenta);
		
		textFieldNroVenta = new JTextField();
		textFieldNroVenta.setEditable(false);
		panelSuperior.add(textFieldNroVenta);
		textFieldNroVenta.setText(Integer.toString(venta.getNroVenta()));
		textFieldNroVenta.setColumns(10);
		
		lblSeparador1 = new JLabel("          ");
		panelSuperior.add(lblSeparador1);
		
		lblFecha = new JLabel("Fecha: ");
		panelSuperior.add(lblFecha);
		
		textFieldFecha = new JTextField();
		textFieldFecha.setEditable(false);
		panelSuperior.add(textFieldFecha);
		textFieldFecha.setText(venta.getFechaString());
		textFieldFecha.setColumns(10);
		
		lblSeparador2 = new JLabel("               ");
		panelSuperior.add(lblSeparador2);
		
		lblMonto = new JLabel("Monto: ");
		panelSuperior.add(lblMonto);
		
		textFieldMonto = new JTextField();
		textFieldMonto.setEditable(false);
		panelSuperior.add(textFieldMonto);
		textFieldMonto.setText(Double.toString(venta.getMonto()));
		textFieldMonto.setColumns(10);
		
		lblCosto = new JLabel("   Costo: ");
		panelSuperior.add(lblCosto);
		
		textFieldCosto = new JTextField();
		textFieldCosto.setEditable(false);
		panelSuperior.add(textFieldCosto);
		textFieldCosto.setText(Double.toString(venta.getCosto()));
		textFieldCosto.setColumns(10);
		
		panelCentral = new JPanel();
		contentPane.add(panelCentral, BorderLayout.CENTER);
		panelCentral.setLayout(new BorderLayout(0, 0));
		
		String[] columnas = {"Marca", "Categoria",  "Tipo", "Color", 
							"Descripción", "Talle", "Costo", "Precio"};
		model = new DefaultTableModel(columnas, 0);
        table = new JTable(model) {
            @Override
            public boolean isCellEditable(int row, int column) {// Hace todas las celdas no editables
                return false; 
            }
        };
        table.setRowSelectionAllowed(false);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setFont(new Font("Tahoma", Font.PLAIN, 15));
        table.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
       
        /* Alineacion de textos y numeros */
		DefaultTableCellRenderer alineacionDerecha = new DefaultTableCellRenderer();
        alineacionDerecha.setHorizontalAlignment(SwingConstants.RIGHT);
        DefaultTableCellRenderer alineacionIzquierda = new DefaultTableCellRenderer();
        alineacionIzquierda.setHorizontalAlignment(SwingConstants.LEFT);
        for (int i = 0; i <= 4; i++) {
        	table.getColumnModel().getColumn(i).setCellRenderer(alineacionIzquierda);
        }
        for (int i = 5; i <= 7; i++) {
        	table.getColumnModel().getColumn(i).setCellRenderer(alineacionDerecha);
        }
        
        JScrollPane scrollPane = new JScrollPane(table);
		panelCentral.add(scrollPane, BorderLayout.CENTER);
	}

	private void cargarTabla() {
		ArrayList<Prenda> prendaVenta;
		prendaVenta = listaPrenda.stream()
				.filter(p -> venta.getIdPrendasVenta().contains(p.getIdRopa()))
				.collect(Collectors.toCollection(ArrayList<Prenda>::new));
		for (Prenda p : prendaVenta) {
			Object[] fila = {
					p.getMarca(),
					p.getCategoria(),
					p.getTipo(),
					p.getColor(),
					p.getDescripcion(),
					p.getTalle(),
					p.getCostoFecha(venta.getFecha()),
					p.getPrecioFecha(venta.getFecha())
			};
			model.addRow(fila);
		}
	}
}
