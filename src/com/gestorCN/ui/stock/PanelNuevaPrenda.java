package com.gestorCN.ui.stock;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.gestorCN.logic.stock.GestorPrendas;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JComboBox;
import java.awt.Font;
import java.awt.Image;

import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.JButton;
import java.awt.Color;
import java.awt.Cursor;

import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import java.awt.Toolkit;
import javax.swing.JRadioButton;

public class PanelNuevaPrenda extends JFrame implements ActionListener {

private static final long serialVersionUID = 1L;
	
	private GestorPrendas gestorPrendas;
	private PanelGestorStock panelGestorStock;
	
	private JPanel contentPane;
	private JButton btnCancelar;
	private JButton btnCargar;
	private JComboBox<?> cbMarcas;
	private JComboBox<?> cbTalles;
	private JComboBox<?> cbTipos;
	private JComboBox<?> cbColores;
	private JTextPane textDescripcion;
	private JTextField textFieldStock;
	private JTextField textFieldCosto;
	private JTextField textFieldPrecio;
	private JRadioButton rdbtnMujer;
	private JRadioButton rdbtnHombre;
	private JComboBox<?> cbCategorias;
	
	public PanelNuevaPrenda(GestorPrendas gestorPrendas, PanelGestorStock panelGestorStock) {
		Image icono = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/images/icono.jpg"));
	    setIconImage(icono);
	    
		setAlwaysOnTop(true);
		setTitle("Casa Nova - Nueva Prenda");
		setType(Type.UTILITY);
		setResizable(false);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 765, 346);
		setLocationRelativeTo(null);
		
		this.gestorPrendas = gestorPrendas;
		this.panelGestorStock = panelGestorStock;
		
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

	@SuppressWarnings("rawtypes")
	private void iniciarComponentes() {
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblMarca = new JLabel("Marca *");
		lblMarca.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblMarca.setBounds(57, 7, 62, 18);
		contentPane.add(lblMarca);
		
		cbMarcas = new JComboBox();
		cbMarcas.setFont(new Font("Tahoma", Font.PLAIN, 15));
		cbMarcas.setBounds(10, 36, 136, 22);
		contentPane.add(cbMarcas);
		
		JLabel lblTalle = new JLabel("Talle *");
		lblTalle.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblTalle.setBounds(56, 106, 62, 22);
		contentPane.add(lblTalle);
		
		cbTalles = new JComboBox();
		cbTalles.setFont(new Font("Tahoma", Font.PLAIN, 15));
		cbTalles.setBounds(10, 137, 136, 22);
		contentPane.add(cbTalles);
		
		JLabel lblTipo = new JLabel("Tipo *");
		lblTipo.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblTipo.setBounds(466, 6, 62, 22);
		contentPane.add(lblTipo);
		
		cbTipos = new JComboBox();
		cbTipos.setFont(new Font("Tahoma", Font.PLAIN, 15));
		cbTipos.setBounds(412, 37, 136, 22);
		contentPane.add(cbTipos);
		
		JLabel lblColor = new JLabel("Color *");
		lblColor.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblColor.setBounds(650, 7, 46, 22);
		contentPane.add(lblColor);
		
		cbColores = new JComboBox();
		cbColores.setFont(new Font("Tahoma", Font.PLAIN, 15));
		cbColores.setBounds(603, 40, 136, 22);
		contentPane.add(cbColores);
		
		JLabel lblPrecio = new JLabel("Precio *");
		lblPrecio.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblPrecio.setBounds(646, 106, 62, 20);
		contentPane.add(lblPrecio);
		
		textFieldPrecio = new JTextField();
		textFieldPrecio.setFont(new Font("Tahoma", Font.PLAIN, 15));
		textFieldPrecio.setBounds(603, 139, 136, 20);
		contentPane.add(textFieldPrecio);
		textFieldPrecio.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				char c = e.getKeyChar();
				if((!Character.isDigit(c)) && (c != '.')) {
					e.consume();
				}
			}
		});
		textFieldPrecio.setColumns(10);
		
		JLabel lblDescripcion = new JLabel("Descripcion");
		lblDescripcion.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblDescripcion.setBounds(10, 196, 89, 18);
		contentPane.add(lblDescripcion);
		
		textDescripcion = new JTextPane();
		textDescripcion.setBounds(10, 223, 376, 72);
		contentPane.add(textDescripcion);
		
		btnCargar = new JButton("Cargar");
		btnCargar.setBackground(new Color(40, 255, 51));
		btnCargar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnCargar.setFont(new Font("Tahoma", Font.PLAIN, 15));
		btnCargar.setBounds(603, 194, 89, 23);
		btnCargar.addActionListener(this);
		contentPane.add(btnCargar);
		
		btnCancelar = new JButton("Cancelar");
		btnCancelar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnCancelar.setBackground(new Color(253, 91, 91));
		btnCancelar.setFont(new Font("Tahoma", Font.PLAIN, 15));
		btnCancelar.setBounds(603, 251, 89, 23);
		btnCancelar.addActionListener(this);
		contentPane.add(btnCancelar);
		
		JLabel lblStock = new JLabel("Stock");
		lblStock.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblStock.setBounds(252, 107, 62, 20);
		contentPane.add(lblStock);
		
		textFieldStock = new JTextField();
		textFieldStock.setFont(new Font("Tahoma", Font.PLAIN, 15));
		textFieldStock.setColumns(10);
		textFieldStock.setBounds(210, 139, 136, 20);
		textFieldStock.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				char c = e.getKeyChar();
				if(!Character.isDigit(c)) {
					e.consume();
				}
			}
		});
		contentPane.add(textFieldStock);
		
		JLabel lblCosto = new JLabel("Costo *");
		lblCosto.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblCosto.setBounds(455, 106, 62, 20);
		contentPane.add(lblCosto);
		
		textFieldCosto = new JTextField();
		textFieldCosto.setFont(new Font("Tahoma", Font.PLAIN, 15));
		textFieldCosto.setColumns(10);
		textFieldCosto.setBounds(412, 139, 136, 20);
		contentPane.add(textFieldCosto);
		
		ButtonGroup gtGroup = new ButtonGroup();
		
		rdbtnMujer = new JRadioButton("Mujer");
		rdbtnMujer.setBounds(438, 224, 109, 23);
		gtGroup.add(rdbtnMujer);
		contentPane.add(rdbtnMujer);
		
		rdbtnHombre = new JRadioButton("Hombre");
		rdbtnHombre.setBounds(438, 253, 109, 23);
		gtGroup.add(rdbtnHombre);
		contentPane.add(rdbtnHombre);
		
		JLabel lblGenero = new JLabel("Genero *");
		lblGenero.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblGenero.setBounds(438, 196, 62, 20);
		contentPane.add(lblGenero);
		
		JLabel lblCategoria = new JLabel("Categoria *");
		lblCategoria.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblCategoria.setBounds(232, 7, 97, 22);
		contentPane.add(lblCategoria);
		
		cbCategorias = new JComboBox();
		cbCategorias.setFont(new Font("Tahoma", Font.PLAIN, 15));
		cbCategorias.setBounds(210, 40, 136, 22);
		contentPane.add(cbCategorias);
		
		cargarComboBox();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void cargarComboBox() {
		ArrayList<ArrayList<String>> caracteristicas;
		caracteristicas = gestorPrendas.getCaracteristicas();
		int cantidad;
		//Marcas
		cantidad = caracteristicas.get(0).size();
		String[] selecciones = new String[cantidad+1];
		selecciones[0] = "Seleccione una opcion";
		for (int i = 1; i <= cantidad; i++) {
			selecciones[i] = caracteristicas.get(0).get(i-1);
		}
		cbMarcas.setModel(new DefaultComboBoxModel(selecciones));
		
		//Tipos
		cantidad = 0;
		selecciones = null;
		
		cantidad = caracteristicas.get(1).size();
		selecciones = new String[cantidad+1];
		selecciones[0] = "Seleccione una opcion";
		for (int i = 1; i <= cantidad; i++) {
			selecciones[i] = caracteristicas.get(1).get(i-1);
		}
		cbTipos.setModel(new DefaultComboBoxModel(selecciones));
		
		//Talles
		cantidad = 0;
		selecciones = null;
		
		cantidad = caracteristicas.get(2).size();
		selecciones = new String[cantidad+1];
		selecciones[0] = "Seleccione una opcion";
		for (int i = 1; i <= cantidad; i++) {
			selecciones[i] = caracteristicas.get(2).get(i-1);
		}
		cbTalles.setModel(new DefaultComboBoxModel(selecciones));
		
		//Colores
		cantidad = 0;
		selecciones = null;
		
		cantidad = caracteristicas.get(3).size();
		selecciones = new String[cantidad+1];
		selecciones[0] = "Seleccione una opcion";
		for (int i = 1; i <= cantidad; i++) {
			selecciones[i] = caracteristicas.get(3).get(i-1);
		}
		cbColores.setModel(new DefaultComboBoxModel(selecciones));
		
		//Categorias
		cantidad = 0;
		selecciones = null;
		
		cantidad = caracteristicas.get(4).size();
		selecciones = new String[cantidad+1];
		selecciones[0] = "Seleccione una opcion";
		for (int i = 1; i <= cantidad; i++) {
			selecciones[i] = caracteristicas.get(4).get(i-1);
		}
		cbCategorias.setModel(new DefaultComboBoxModel(selecciones));
	}

	@SuppressWarnings("static-access")
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btnCargar) {
			if(cargarNuevaRopa()) {
//				dispose();
//				panelGestorStock.setEnabled(true);
//				panelGestorStock.toFront();
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
	
	private void limpiarCasillas() {
		cbMarcas.setSelectedIndex(0);
		cbTalles.setSelectedIndex(0);
		cbTipos.setSelectedIndex(0);
		cbColores.setSelectedIndex(0);
		cbCategorias.setSelectedIndex(0);
		textDescripcion.setText("");
		textFieldStock.setText("");
		textFieldPrecio.setText("");
		textFieldCosto.setText("");
		rdbtnMujer.setSelected(false);
		rdbtnHombre.setSelected(false);
	}
	
	private boolean cargarNuevaRopa() {
		String msg = null;
		String marca;
		String tipo;
		String color;
		String descripcion;
		String talleAux;
		String genero = null;
		String categoria = null;
		String talle;
		int stock;
		double precio;
		double costo;
		
		boolean cargado = false;
		
		marca = (String) cbMarcas.getSelectedItem();
		tipo = (String) cbTipos.getSelectedItem();
		color = (String) cbColores.getSelectedItem();
		descripcion = textDescripcion.getText().trim();
		talleAux = (String) cbTalles.getSelectedItem();
		categoria = (String) cbCategorias.getSelectedItem();
		try {
			if ((!talleAux.equals("Seleccione una opcion") && 
					(!textFieldStock.getText().equals("")) &&
					(!textFieldPrecio.getText().equals("")))) {
				talle = talleAux;
				stock = Integer.parseInt(textFieldStock.getText().trim());
				precio = Double.parseDouble(textFieldPrecio.getText().trim());
				costo = Double.parseDouble(textFieldCosto.getText().trim());
				if (precio > 1000000 || costo > 1000000) {
					throw new NumberFormatException();
				}
			} else {
				talle = "-1";
				stock = 0;
				precio = -1;
				costo = -1;
			}
			if (rdbtnMujer.isSelected()) {
				genero = "F";
			} 
			else if (rdbtnHombre.isSelected()) {
				genero = "M";
			}
			msg = gestorPrendas.nuevaPrenda(marca, tipo, color, descripcion, talle, 
						stock, precio, costo, genero, categoria);
			if((!msg.equals("Se deben seleccionar los campos obligatorios")) &&
				(!msg.equals("Ingrese un numero valido en los campos"))
				&& (!msg.equals("La prenda ya existe"))) {
				limpiarCasillas();
				cargado = true;
			} else {
				cargado = false;
			}
		} catch (NumberFormatException e) {
			msg = "Ingrese un numero valido";
			cargado = false;
		}
		JOptionPane.showMessageDialog(this, 
                msg, 
                "Notificación", 
                JOptionPane.INFORMATION_MESSAGE);
		
		return cargado;
	}
	
}
