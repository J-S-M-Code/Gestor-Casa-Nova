package com.gestorCN.ui.sales;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.stream.Collectors;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;


import javax.swing.JLabel;
import javax.swing.JTextField;
import com.jgoodies.forms.layout.FormLayout;
import com.gestorCN.logic.sales.Venta;
import com.gestorCN.logic.stock.GestorPrendas;
import com.gestorCN.logic.stock.Prenda;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.layout.FormSpecs;
import java.awt.Font;

public class PanelDetallesFacturacion extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private ArrayList<Venta> ventasFecha;
	private ArrayList<Prenda> listaPrenda;
	private JTextField textFieldVendidoHombre;
	private JTextField textFieldVendidoMujer;
	private JTextField textFieldCostoHombre;
	private JTextField textFieldCostoMujer;
	private JTextField textFieldGananciaMujer;
	private JTextField textFieldGananciaHombre;

	public PanelDetallesFacturacion(PanelReporteFacturas prf, ArrayList<Venta> ventasFecha, 
									GestorPrendas gp) {
		setType(Type.POPUP);
		setTitle("Casa Nova - Detalle de Facturación");
		setIconImage(Toolkit.getDefaultToolkit().getImage(""));
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setMinimumSize(new Dimension(500, 400));
		setBounds(100, 100, 535, 330);
		setLocationRelativeTo(null);
		
		this.ventasFecha = ventasFecha;
		this.listaPrenda = gp.getPrendas();
		
		addWindowListener(new WindowAdapter() { //Para cuando cierre se active la pestaña anterior 
			@Override
			public void windowClosing(WindowEvent e) {
				prf.setEnabled(true);
				prf.toFront();
			}
		});
		
		iniciarComponentes();
		cargarCampos();
	}

	private void cargarCampos() {
		ArrayList<Prenda> prendaVenta;
		double ventaHombre = 0;
		double ventaMujer = 0;
		double costoHombre = 0;
		double costoMujer = 0;
		for (Venta v : ventasFecha) {
			prendaVenta = listaPrenda.stream()
					.filter(p -> v.getIdPrendasVenta().contains(p.getIdRopa()))
					.collect(Collectors.toCollection(ArrayList<Prenda>::new));
			for (Prenda p : prendaVenta) {
				if (p.getGenero().equals("M")) {
					ventaHombre += p.getPrecioFecha(v.getFecha());
					costoHombre += p.getCostoFecha(v.getFecha());
				}
				if (p.getGenero().equals("F")) {
					ventaMujer += p.getPrecioFecha(v.getFecha());;
					costoMujer += p.getCostoFecha(v.getFecha());
				}
			}
		}
		textFieldVendidoHombre.setText(Double.toString(ventaHombre));
		textFieldVendidoMujer.setText(Double.toString(ventaMujer));
		textFieldCostoHombre.setText(Double.toString(costoHombre));
		textFieldCostoMujer.setText(Double.toString(costoMujer));
		textFieldGananciaMujer.setText(Double.toString(ventaMujer - costoMujer));
		textFieldGananciaHombre.setText(Double.toString(ventaHombre - costoHombre));
	}

	private void iniciarComponentes() {
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(new FormLayout(new ColumnSpec[] {
				FormSpecs.RELATED_GAP_COLSPEC,
				FormSpecs.DEFAULT_COLSPEC,
				FormSpecs.RELATED_GAP_COLSPEC,
				FormSpecs.DEFAULT_COLSPEC,
				FormSpecs.RELATED_GAP_COLSPEC,
				FormSpecs.DEFAULT_COLSPEC,
				FormSpecs.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),
				FormSpecs.RELATED_GAP_COLSPEC,
				FormSpecs.DEFAULT_COLSPEC,
				FormSpecs.RELATED_GAP_COLSPEC,
				FormSpecs.DEFAULT_COLSPEC,
				FormSpecs.RELATED_GAP_COLSPEC,
				FormSpecs.DEFAULT_COLSPEC,
				FormSpecs.RELATED_GAP_COLSPEC,
				FormSpecs.DEFAULT_COLSPEC,
				FormSpecs.RELATED_GAP_COLSPEC,
				FormSpecs.DEFAULT_COLSPEC,
				FormSpecs.RELATED_GAP_COLSPEC,
				FormSpecs.DEFAULT_COLSPEC,
				FormSpecs.RELATED_GAP_COLSPEC,
				FormSpecs.DEFAULT_COLSPEC,
				FormSpecs.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),
				FormSpecs.RELATED_GAP_COLSPEC,
				FormSpecs.DEFAULT_COLSPEC,
				FormSpecs.RELATED_GAP_COLSPEC,
				FormSpecs.DEFAULT_COLSPEC,
				FormSpecs.RELATED_GAP_COLSPEC,
				FormSpecs.DEFAULT_COLSPEC,},
			new RowSpec[] {
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,}));
		
		JLabel lblHombre = new JLabel("Hombre");
		lblHombre.setFont(new Font("Tahoma", Font.PLAIN, 15));
		contentPane.add(lblHombre, "8, 4, fill, default");
		
		JLabel lblMujer = new JLabel("Mujer");
		lblMujer.setFont(new Font("Tahoma", Font.PLAIN, 15));
		contentPane.add(lblMujer, "24, 4");
		
		JLabel lblTotalVendidoHombre = new JLabel("Total Vendido");
		lblTotalVendidoHombre.setFont(new Font("Tahoma", Font.PLAIN, 15));
		contentPane.add(lblTotalVendidoHombre, "8, 8");
		
		JLabel lblTotalVendidoMujer = new JLabel("Total Vendido");
		lblTotalVendidoMujer.setFont(new Font("Tahoma", Font.PLAIN, 15));
		contentPane.add(lblTotalVendidoMujer, "24, 8");
		
		textFieldVendidoHombre = new JTextField();
		textFieldVendidoHombre.setEditable(false);
		textFieldVendidoHombre.setFont(new Font("Tahoma", Font.PLAIN, 15));
		contentPane.add(textFieldVendidoHombre, "8, 10, fill, default");
		textFieldVendidoHombre.setColumns(10);
		
		textFieldVendidoMujer = new JTextField();
		textFieldVendidoMujer.setEditable(false);
		textFieldVendidoMujer.setFont(new Font("Tahoma", Font.PLAIN, 15));
		contentPane.add(textFieldVendidoMujer, "24, 10, fill, default");
		textFieldVendidoMujer.setColumns(10);
		
		JLabel lblCostoTotalHombre = new JLabel("Costo total");
		lblCostoTotalHombre.setFont(new Font("Tahoma", Font.PLAIN, 15));
		contentPane.add(lblCostoTotalHombre, "8, 14");
		
		JLabel lblCostoTotalMujer = new JLabel("Costo total");
		lblCostoTotalMujer.setFont(new Font("Tahoma", Font.PLAIN, 15));
		contentPane.add(lblCostoTotalMujer, "24, 14");
		
		textFieldCostoHombre = new JTextField();
		textFieldCostoHombre.setEditable(false);
		textFieldCostoHombre.setFont(new Font("Tahoma", Font.PLAIN, 15));
		contentPane.add(textFieldCostoHombre, "8, 16, fill, default");
		textFieldCostoHombre.setColumns(10);
		
		textFieldCostoMujer = new JTextField();
		textFieldCostoMujer.setEditable(false);
		textFieldCostoMujer.setFont(new Font("Tahoma", Font.PLAIN, 15));
		contentPane.add(textFieldCostoMujer, "24, 16, fill, default");
		textFieldCostoMujer.setColumns(10);
		
		JLabel lblGananciaHombre = new JLabel("Ganancia");
		lblGananciaHombre.setFont(new Font("Tahoma", Font.PLAIN, 15));
		contentPane.add(lblGananciaHombre, "8, 20");
		
		JLabel lblGananciaMujer = new JLabel("Ganancia");
		lblGananciaMujer.setFont(new Font("Tahoma", Font.PLAIN, 15));
		contentPane.add(lblGananciaMujer, "24, 20");
		
		textFieldGananciaHombre = new JTextField();
		textFieldGananciaHombre.setEditable(false);
		textFieldGananciaHombre.setFont(new Font("Tahoma", Font.PLAIN, 15));
		contentPane.add(textFieldGananciaHombre, "8, 22, fill, default");
		textFieldGananciaHombre.setColumns(10);
		
		textFieldGananciaMujer = new JTextField();
		textFieldGananciaMujer.setEditable(false);
		textFieldGananciaMujer.setFont(new Font("Tahoma", Font.PLAIN, 15));
		contentPane.add(textFieldGananciaMujer, "24, 22, fill, default");
		textFieldGananciaMujer.setColumns(10);
		
	}

}
