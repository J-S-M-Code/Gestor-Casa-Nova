package com.gestorCN.ui.stock;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.border.EmptyBorder;

import com.gestorCN.logic.exceptions.NumeroInvalido;
import com.gestorCN.logic.stock.GestorPrendas;
import com.gestorCN.logic.stock.Prenda;

public class PanelModificarPrenda extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	
	private GestorPrendas gestorPrendas;
	private Prenda prenda;
	private PanelGestorStock panelGestorStock;
	
	private JTextField textFieldMarca;
	private JTextField textFieldColor;
	private JTextField textFieldStock;
	private JTextField textFieldPrecio;
	private JTextField textFieldTipo;
	private JButton btnCargar;
	private JButton btnCancelar;
	private JTextPane textDescripcion;
	private JComboBox<String> cbTalles;
	private JTextField textFieldIngresoStock;
	private JLabel lblCosto;
	private JTextField textFieldCosto;
	
	public PanelModificarPrenda(GestorPrendas gestorPrendas, PanelGestorStock panelGestorStock, 
			Prenda prenda)  {
		Image icono = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/images/icono.jpg"));
	    setIconImage(icono);
	    
		setTitle("Casa Nova - Modificar Prenda");
		
		this.gestorPrendas = gestorPrendas;
		this.panelGestorStock = panelGestorStock;
		this.prenda = prenda;
		
		setType(Type.UTILITY);
		setResizable(false);
		setAlwaysOnTop(true);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 545, 475);
		setLocationRelativeTo(null);
		addWindowListener(new WindowAdapter() { //Para cuando cierre se active la pestaña anterior 
			@SuppressWarnings("static-access")
			@Override
			public void windowClosing(WindowEvent e) {
				panelGestorStock.setEnabled(true);
				panelGestorStock.toFront();
				panelGestorStock.actualizarListado(panelGestorStock.estado);
			}
		});
		
		iniciarComponentes();
	}

	private void iniciarComponentes() {
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblMarca = new JLabel("Marca: ");
		lblMarca.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblMarca.setBounds(10, 22, 55, 19);
		contentPane.add(lblMarca);
		
		JLabel lblTipo = new JLabel("Tipo:");
		lblTipo.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblTipo.setBounds(247, 22, 40, 19);
		contentPane.add(lblTipo);
		
		JLabel lblColor = new JLabel("Color: ");
		lblColor.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblColor.setBounds(10, 84, 55, 19);
		contentPane.add(lblColor);
		
		JLabel lblTalle = new JLabel("Talle: ");
		lblTalle.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblTalle.setBounds(247, 84, 40, 19);
		contentPane.add(lblTalle);
		
		JLabel lblStock = new JLabel("Stock actual: ");
		lblStock.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblStock.setBounds(10, 152, 89, 19);
		contentPane.add(lblStock);
		
		JLabel lblPrecio = new JLabel("Precio: ");
		lblPrecio.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblPrecio.setBounds(204, 222, 55, 19);
		contentPane.add(lblPrecio);
		
		JLabel lblDescripcion = new JLabel("Descripcion:");
		lblDescripcion.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblDescripcion.setBounds(10, 274, 84, 19);
		contentPane.add(lblDescripcion);
		
		textDescripcion = new JTextPane();
		textDescripcion.setBounds(10, 304, 417, 82);
		contentPane.add(textDescripcion);
		
		btnCancelar = new JButton("Cancelar");
		btnCancelar.addActionListener(this);
		btnCancelar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnCancelar.setBackground(new Color(253, 91, 91));
		btnCancelar.setBounds(297, 402, 89, 23);
		contentPane.add(btnCancelar);
		
		btnCargar = new JButton("Cargar");
		btnCargar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnCargar.setBackground(new Color(40, 255, 51));
		btnCargar.setBounds(430, 402, 89, 23);
		btnCargar.addActionListener(this);
		contentPane.add(btnCargar);
		
		textFieldMarca = new JTextField();
		textFieldMarca.setEditable(false);
		textFieldMarca.setBounds(65, 23, 161, 20);
		contentPane.add(textFieldMarca);
		textFieldMarca.setColumns(10);
		
		textFieldColor = new JTextField();
		textFieldColor.setEditable(false);
		textFieldColor.setColumns(10);
		textFieldColor.setBounds(65, 85, 161, 20);
		contentPane.add(textFieldColor);
		
		textFieldStock = new JTextField();
		textFieldStock.setEditable(false);
		textFieldStock.setColumns(10);
		textFieldStock.setBounds(109, 153, 84, 21);
		contentPane.add(textFieldStock);
		
		textFieldPrecio = new JTextField();
		textFieldPrecio.setColumns(10);
		textFieldPrecio.setBounds(259, 221, 84, 20);
		contentPane.add(textFieldPrecio);
		
		textFieldTipo = new JTextField();
		textFieldTipo.setEditable(false);
		textFieldTipo.setColumns(10);
		textFieldTipo.setBounds(297, 23, 161, 20);
		contentPane.add(textFieldTipo);
		
		cbTalles = new JComboBox<String>();
		cbTalles.setBounds(297, 84, 161, 22);
		contentPane.add(cbTalles);
		
		JLabel lblIngresoStock = new JLabel("Ingreso stock:");
		lblIngresoStock.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblIngresoStock.setBounds(203, 152, 106, 18);
		contentPane.add(lblIngresoStock);
		
		textFieldIngresoStock = new JTextField();
		textFieldIngresoStock.setFont(new Font("Tahoma", Font.PLAIN, 15));
		textFieldIngresoStock.setBounds(308, 153, 86, 20);
		contentPane.add(textFieldIngresoStock);
		textFieldIngresoStock.setColumns(10);
		
		lblCosto = new JLabel("Costo:");
		lblCosto.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblCosto.setBounds(11, 223, 55, 19);
		contentPane.add(lblCosto);
		
		textFieldCosto = new JTextField();
		textFieldCosto.setText("0.0");
		textFieldCosto.setColumns(10);
		textFieldCosto.setBounds(66, 222, 84, 20);
		contentPane.add(textFieldCosto);
		
		cargarComboBox();
		cargarCampos();
	}
	
	private void cargarCampos() {
		textFieldMarca.setText(prenda.getMarca());
		textFieldColor.setText(prenda.getColor());
		textFieldStock.setText(String.valueOf(prenda.getStock()));
		textFieldCosto.setText(String.valueOf(prenda.getUltimoCosto()));
		textFieldPrecio.setText(String.valueOf(prenda.getUltimoPrecio()));
		textFieldTipo.setText(prenda.getTipo());
		textDescripcion.setText(prenda.getDescripcion());
	}

	private void cargarComboBox() {
		ArrayList<ArrayList<String>> caracteristicas;
		caracteristicas = gestorPrendas.getCaracteristicas();
		int cantidad;
		//Talles
		cantidad = 0;
		cantidad = caracteristicas.get(2).size();
		String[] selecciones = new String[cantidad+1];
		selecciones[0] = "Seleccione un talle";

		for (int i = 1; i <= cantidad; i++) {
			selecciones[i] = caracteristicas.get(2).get(i-1);
		}
		cbTalles.setModel(new DefaultComboBoxModel<String>(selecciones));
		cbTalles.setSelectedItem(String.valueOf(prenda.getTalle()));
	
	}
	
	@SuppressWarnings("static-access")
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btnCargar) {
			if(modificarDatos()) {
				dispose();
				panelGestorStock.setEnabled(true);
				panelGestorStock.toFront();
				panelGestorStock.actualizarListado(panelGestorStock.estado);
			}
		}
		
		if (e.getSource() == btnCancelar) {
			dispose();
			panelGestorStock.setEnabled(true);
			panelGestorStock.toFront();
			panelGestorStock.actualizarListado(panelGestorStock.estado);
		}
	}
	
	private boolean modificarDatos() {
		String msg = null;
		String descripcion;
		double precio = -1;
		double costo = -1;
		int talle = -1;
		int stock;
		if (textFieldIngresoStock.getText().trim().isEmpty()) {
			textFieldIngresoStock.setText("0");
        }
		boolean modificado = false;
		try{
			if((cbTalles.getSelectedIndex() != 0) && (!textFieldPrecio.getText().equals(""))
					&& (!textFieldCosto.getText().equals(""))) {
				costo = Double.parseDouble(textFieldCosto.getText().trim());
				precio = Double.parseDouble(textFieldPrecio.getText().trim());
				talle = Integer.parseInt((String) cbTalles.getSelectedItem());
				
				if (prenda.getStock() + Integer.parseInt(textFieldIngresoStock.getText().trim()) < 0) {
					throw new NumeroInvalido("El numero no puede ser menor que el stock");
				}
				
				stock = prenda.getStock() + Integer.parseInt(textFieldIngresoStock.getText().trim());
				descripcion = textDescripcion.getText().trim();
				msg = gestorPrendas.modificarPrenda(precio, costo, talle, stock, descripcion, prenda);
				
				if((!msg.equals("No se modifico ningun dato")) &&
						(!msg.equals("Ingrese un numero valido en los campos"))) {
					modificado = true;
				}
			}
		} catch (NumberFormatException e) {
			msg = "Ingrese un numero";
		} catch (NumeroInvalido e) {
			msg = "Ingrese un numero valido";
			msg = e.getMessage();
		}
		JOptionPane.showMessageDialog(this, 
                msg, 
                "Notificación", 
                JOptionPane.INFORMATION_MESSAGE);
		return modificado;	
	}
	
}
