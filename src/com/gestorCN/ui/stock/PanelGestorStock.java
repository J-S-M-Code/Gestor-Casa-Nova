package com.gestorCN.ui.stock;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import java.awt.BorderLayout;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import com.gestorCN.logic.stock.GestorPrendas;
import com.gestorCN.logic.stock.Prenda;
import com.gestorCN.ui.sales.PanelGestorVentas;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Cursor;
import java.awt.Dimension;

import javax.swing.ListSelectionModel;
import java.awt.Toolkit;
import javax.swing.JCheckBox;
import javax.swing.SwingConstants;
import javax.swing.JComboBox;

public class PanelGestorStock extends JFrame implements ActionListener {
	private static final long serialVersionUID = 1L;
	public static int estado = 1;
	private ArrayList<Prenda> listaPrendas;
	private ArrayList<Prenda> prendasFiltradas;
	private Map<Integer, Prenda> mapaFilaRopa = new HashMap<>();
	
	private GestorPrendas gestorPrendas;
	private PanelGestorVentas panelGestorVentas;
	
	private JPanel contentPane;
	private JPanel panelFiltros;
	private JTable table;
	private DefaultTableModel model;
	private JButton btnVolver;
	private JButton btnNuevo;
	private JButton btnModificar;
	private JButton btnCambioEstado;
	private JButton btnCaracteristicas;
	private JButton btnHistorialPreciosCostos;
	private JButton btnAjustePrecio;
	private JButton btnLimpiarFiltros;
	private JCheckBox chckbxVistaActivos;
	private JComboBox<?> cbFiltroMarca;
	private JComboBox<?> cbFiltroTipo;
	private JComboBox<?> cbFiltroColor;
	private JComboBox<?> cbFiltroTalle;
	private JLabel lblFiltro;
	private JComboBox<?> cbFiltroCategoria;
	
	public PanelGestorStock(PanelGestorVentas panelGestorVentas, GestorPrendas gestorPrendas) {
		setTitle("Casa Nova - Gestor Stock");

		Image icono = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/images/icono.jpg"));
	    setIconImage(icono);
		
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		setBounds(100, 100, 935, 621);
		setMinimumSize(new Dimension(865, 600));
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		setLocationRelativeTo(null);
		
		this.panelGestorVentas = panelGestorVentas;
		this.gestorPrendas = gestorPrendas;
		
		addWindowListener(new WindowAdapter() { //Para cuando cierre se active la pestaña anterior 
			@Override
			public void windowClosing(WindowEvent e) {
				panelGestorVentas.setEnabled(true);
				panelGestorVentas.toFront();
			}
		});
		iniciarComponentes();
	}
	
	@SuppressWarnings({ "rawtypes", "serial" })
	private void iniciarComponentes() {
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		JPanel panelSuperior = new JPanel();
		contentPane.add(panelSuperior, BorderLayout.NORTH);
		panelSuperior.setLayout(new BorderLayout(0, 0));
		
		btnVolver = new JButton("Volver");
		btnVolver.addActionListener(this);
		btnVolver.setFont(new Font("Tahoma", Font.PLAIN, 15));
		panelSuperior.add(btnVolver, BorderLayout.WEST);
		
		JPanel panelSuperiorBtn = new JPanel();
		//FlowLayout fl_panelSuperiorBtn = (FlowLayout) panelSuperiorBtn.getLayout();
		panelSuperior.add(panelSuperiorBtn, BorderLayout.CENTER);
		
		btnNuevo = new JButton("Nuevo");
		btnNuevo.setFont(new Font("Tahoma", Font.PLAIN, 15));
		btnNuevo.addActionListener(this);
		panelSuperiorBtn.add(btnNuevo);
		
		btnModificar = new JButton("Modificar");
		btnModificar.setFont(new Font("Tahoma", Font.PLAIN, 15));
		btnModificar.addActionListener(this);
		panelSuperiorBtn.add(btnModificar);
		
		btnCambioEstado = new JButton("Desactivar");
		btnCambioEstado.setFont(new Font("Tahoma", Font.PLAIN, 15));
		btnCambioEstado.addActionListener(this);
		panelSuperiorBtn.add(btnCambioEstado);
		
		chckbxVistaActivos = new JCheckBox("Activos");
		chckbxVistaActivos.setHorizontalTextPosition(SwingConstants.LEFT);
		chckbxVistaActivos.setHorizontalAlignment(SwingConstants.LEFT);
		chckbxVistaActivos.setFont(new Font("Tahoma", Font.PLAIN, 15));
		chckbxVistaActivos.setSelected(true);
		chckbxVistaActivos.addActionListener(this);
		
		btnHistorialPreciosCostos = new JButton("Costos-Precios");
		btnHistorialPreciosCostos.setFont(new Font("Tahoma", Font.PLAIN, 15));
		btnHistorialPreciosCostos.addActionListener(this);
		panelSuperiorBtn.add(btnHistorialPreciosCostos);
		
		btnAjustePrecio = new JButton("Ajuste Precio");
		btnAjustePrecio.addActionListener(this);
		btnAjustePrecio.setFont(new Font("Tahoma", Font.PLAIN, 13));
		panelSuperiorBtn.add(btnAjustePrecio);
		panelSuperiorBtn.add(chckbxVistaActivos);
		
		btnCaracteristicas = new JButton("Caracteristicas");
		btnCaracteristicas.addActionListener(this);
		panelSuperior.add(btnCaracteristicas, BorderLayout.EAST);
		
		panelFiltros = new JPanel();
		FlowLayout flowLayout = (FlowLayout) panelFiltros.getLayout();
		flowLayout.setAlignment(FlowLayout.LEFT);
		panelSuperior.add(panelFiltros, BorderLayout.SOUTH);
		
		lblFiltro = new JLabel("Filtros: ");
		lblFiltro.setFont(new Font("Tahoma", Font.PLAIN, 15));
		panelFiltros.add(lblFiltro);
		
		cbFiltroMarca = new JComboBox();
		cbFiltroMarca.setFont(new Font("Tahoma", Font.PLAIN, 15));
		cbFiltroMarca.addActionListener(this);
		panelFiltros.add(cbFiltroMarca);
		
		cbFiltroCategoria = new JComboBox();
		cbFiltroCategoria.setFont(new Font("Tahoma", Font.PLAIN, 15));
		cbFiltroCategoria.addActionListener(this);
		panelFiltros.add(cbFiltroCategoria);
		
		
		cbFiltroTipo = new JComboBox();
		cbFiltroTipo.setFont(new Font("Tahoma", Font.PLAIN, 15));
		cbFiltroTipo.addActionListener(this);
		panelFiltros.add(cbFiltroTipo);
		
		cbFiltroColor = new JComboBox();
		cbFiltroColor.setFont(new Font("Tahoma", Font.PLAIN, 15));
		cbFiltroColor.addActionListener(this);
		panelFiltros.add(cbFiltroColor);
		
		cbFiltroTalle = new JComboBox();
		cbFiltroTalle.setFont(new Font("Tahoma", Font.PLAIN, 15));
		cbFiltroTalle.addActionListener(this);
		panelFiltros.add(cbFiltroTalle);

		btnLimpiarFiltros = new JButton("Limpiar filtros");
		btnLimpiarFiltros.setFont(new Font("Tahoma", Font.PLAIN, 15));
		btnLimpiarFiltros.addActionListener(this);
		panelFiltros.add(btnLimpiarFiltros);
		cargarComboBox();
		
		JPanel panelCentral = new JPanel();
		contentPane.add(panelCentral, BorderLayout.CENTER);
		panelCentral.setLayout(new BorderLayout(0, 0));
		
		String[] columnas = {"Marca", "Categoria", "Tipo", "Color", "Descripción", "Talle",
								"Stock", "Costo", "Precio"};
		model = new DefaultTableModel(columnas, 0);
		table = new JTable(model) {
            @Override
            public boolean isCellEditable(int row, int column) {// Hace todas las celdas no editables
                return false; 
            }
        };

        table.getTableHeader().setReorderingAllowed(false);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setFont(new Font("Tahoma", Font.PLAIN, 15));
		table.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		/* Alineacion de textos y numeros */
		DefaultTableCellRenderer alineacionDerecha = new DefaultTableCellRenderer();
        alineacionDerecha.setHorizontalAlignment(SwingConstants.RIGHT);
        DefaultTableCellRenderer alineacionIzquierda = new DefaultTableCellRenderer();
        alineacionIzquierda.setHorizontalAlignment(SwingConstants.LEFT);
        for (int i = 0; i <= 3; i++) {
        	table.getColumnModel().getColumn(i).setCellRenderer(alineacionIzquierda);
        }
        for (int i = 4; i <= 7; i++) {
        	table.getColumnModel().getColumn(i).setCellRenderer(alineacionIzquierda);
        }
		
		JScrollPane scrollPane = new JScrollPane(table);
		panelCentral.add(scrollPane, BorderLayout.CENTER);
		
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void cargarComboBox() {
		ArrayList<ArrayList<String>> caracteristicas;
		caracteristicas = gestorPrendas.getCaracteristicas();
		int cantidad;
		//Marcas
		cantidad = caracteristicas.get(0).size();
		String[] selecciones = new String[cantidad+1];
		selecciones[0] = "Seleccione una marca";
		for (int i = 1; i <= cantidad; i++) {
			selecciones[i] = caracteristicas.get(0).get(i-1);
		}
		cbFiltroMarca.setModel(new DefaultComboBoxModel(selecciones));
		
		//Tipos
		cantidad = 0;
		selecciones = null;
		
		cantidad = caracteristicas.get(1).size();
		selecciones = new String[cantidad+1];
		selecciones[0] = "Seleccione un tipo";
		for (int i = 1; i <= cantidad; i++) {
			selecciones[i] = caracteristicas.get(1).get(i-1);
		}
		cbFiltroTipo.setModel(new DefaultComboBoxModel(selecciones));
		
		//Talles
		cantidad = 0;
		selecciones = null;
		
		cantidad = caracteristicas.get(2).size();
		selecciones = new String[cantidad+1];
		selecciones[0] = "Seleccione un talle";
		for (int i = 1; i <= cantidad; i++) {
			selecciones[i] = caracteristicas.get(2).get(i-1);
		}
		cbFiltroTalle.setModel(new DefaultComboBoxModel(selecciones));
		
		//Colores
		cantidad = 0;
		selecciones = null;
		
		cantidad = caracteristicas.get(3).size();
		selecciones = new String[cantidad+1];
		selecciones[0] = "Seleccione un color";
		for (int i = 1; i <= cantidad; i++) {
			selecciones[i] = caracteristicas.get(3).get(i-1);
		}
		cbFiltroColor.setModel(new DefaultComboBoxModel(selecciones));
		
		//Categorias
		cantidad = 0;
		selecciones = null;
			
		cantidad = caracteristicas.get(4).size();
		selecciones = new String[cantidad+1];
		selecciones[0] = "Seleccione una categoria";
		for (int i = 1; i <= cantidad; i++) {
			selecciones[i] = caracteristicas.get(4).get(i-1);
		}
		cbFiltroCategoria.setModel(new DefaultComboBoxModel(selecciones));
	}
		
	private void cargarRopa(int estado) {
		if (estado == 1) {
			this.listaPrendas = gestorPrendas.getPrendasActivas();
		} else {
			this.listaPrendas = gestorPrendas.getPrendasInactiva();
		}
	}
	
	@SuppressWarnings("static-access")
	public void actualizarListado(int estado) {
		this.estado = estado;
		cargarRopa(estado);
		model.setRowCount(0);
		mapaFilaRopa.clear();
		int rowIndex = 0;
		if (this.listaPrendas != null) {
			for (Prenda p : listaPrendas) {
				if (!p.getCategoria().equals("PANTALON") 
						&& !p.getCategoria().equals("POLLERA")
						&& !p.getCategoria().equals("SHORT") 
						&& !p.getCategoria().equals("VERMUDA")) {
					String talleRemera = talleRemerasString(p.getTalle(), p.getGenero());
					Object[] fila = {
							p.getMarca(),
							p.getCategoria(),
							p.getTipo(),
							p.getColor(),
							p.getDescripcion(),
							talleRemera,
							p.getStock(),
							p.getUltimoCosto(),
							p.getUltimoPrecio()
					};
					model.addRow(fila);
				} else {
					Object[] fila = {
							p.getMarca(),
							p.getCategoria(),
							p.getTipo(),
							p.getColor(),
							p.getDescripcion(),
							p.getTalle(),
							p.getStock(),
							p.getUltimoCosto(),
							p.getUltimoPrecio()
					};
					model.addRow(fila);
				}
				
				mapaFilaRopa.put(rowIndex, p);
				rowIndex++;
			}
		}
	}

	public void actualizarListado(Predicate<Prenda> filtro) {
		cargarRopa(estado);
		model.setRowCount(0);
		mapaFilaRopa.clear();
		prendasFiltradas = listaPrendas.stream()
				.filter(filtro)
				.collect(Collectors.toCollection(ArrayList<Prenda>::new));
		int rowIndex = 0;
		if (prendasFiltradas != null) {
			for (Prenda p : prendasFiltradas) {
				if (!p.getCategoria().equals("PANTALON") && !p.getCategoria().equals("POLLERA")
						&& !p.getCategoria().equals("SHORT") && !p.getCategoria().equals("VERMUDA")) {
					String talleRemera = talleRemerasString(p.getTalle(), p.getGenero());
					Object[] fila = {
							p.getMarca(),
							p.getCategoria(),
							p.getTipo(),
							p.getColor(),
							p.getDescripcion(),
							talleRemera,
							p.getStock(),
							p.getUltimoCosto(),
							p.getUltimoPrecio()
					};
					model.addRow(fila);
				} else {
					Object[] fila = {
							p.getMarca(),
							p.getCategoria(),
							p.getTipo(),
							p.getColor(),
							p.getDescripcion(),
							p.getTalle(),
							p.getStock(),
							p.getUltimoCosto(),
							p.getUltimoPrecio()
					};
					model.addRow(fila);
				}
				
				mapaFilaRopa.put(rowIndex, p);
				rowIndex++;
			}
		}
	}
	
	private String talleRemerasString(int talle, String genero) {
		if(genero.equals("F")) {
			if (talle == 24) {
				return "S";
			}
			else if (talle == 26) {
				return "M";
			}
			else if (talle == 28) {
				return "L";
			}
			else if (talle == 30) {
				return "XL";
			}
			else if (talle == 32) {
				return "XXL";
			}
			else if (talle == 34) {
				return "XXXL";
			}
		} else if (genero.equals("M")){
			if (talle == 28) {
				return "S";
			}
			else if (talle == 30) {
				return "M";
			}
			else if (talle == 32) {
				return "L";
			}
			else if (talle == 34) {
				return "XL";
			}
			else if (talle == 36) {
				return "XXL";
			}
			else if (talle == 38) {
				return "XXXL";
			}
		}
		return "ESPECIAL";
	}

	private void limpiarFiltros() {
		cbFiltroColor.setSelectedIndex(0);
		cbFiltroMarca.setSelectedIndex(0);
		cbFiltroTalle.setSelectedIndex(0);
		cbFiltroTipo.setSelectedIndex(0);
		cbFiltroCategoria.setSelectedIndex(0);
		if (estado == 1) {
			chckbxVistaActivos.setSelected(true);
			btnCambioEstado.setText("Desactivar");
		} else {
			chckbxVistaActivos.setSelected(false);
			btnCambioEstado.setText("Activar");
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btnVolver) {
			limpiarFiltros();
			dispose();
			panelGestorVentas.setEnabled(true);
			panelGestorVentas.toFront();
		}
		
		if (e.getSource() == btnNuevo) {
			setEnabled(false);
			PanelNuevaPrenda carga = new PanelNuevaPrenda(gestorPrendas, this);
			carga.setVisible(true);
			carga.toFront();
		}
		
		if (e.getSource() == btnModificar) {
			int seleccion = table.getSelectedRow();
			if (seleccion != -1) { 
			    if (mapaFilaRopa.containsKey(seleccion)) {
			    	setEnabled(false);
			        PanelModificarPrenda mod = new PanelModificarPrenda(gestorPrendas, 
			        						this, mapaFilaRopa.get(seleccion));
			        mod.setVisible(true);
			        mod.toFront();
			    }
			} else {
				JOptionPane.showMessageDialog(this, 
			            "Seleccione una prenda", 
			            "Notificación", 
			            JOptionPane.INFORMATION_MESSAGE);
			}
		}
		
		if (e.getSource() == btnCambioEstado) {
			int seleccion = table.getSelectedRow();
			if (seleccion != -1) {
				String msg = "";
				int confirmado = JOptionPane.showConfirmDialog(
			            this,
			            "¿Realizar cambio de estado?",
			            "Confirmar cambio de estado",
			            JOptionPane.OK_CANCEL_OPTION,
			            JOptionPane.PLAIN_MESSAGE);
				if (confirmado == JOptionPane.OK_OPTION) {
					if(mapaFilaRopa.get(seleccion).getActivo() == 1) {
						msg = gestorPrendas.setEstado(mapaFilaRopa.get(seleccion), 0);
					} else {
						msg = gestorPrendas.setEstado(mapaFilaRopa.get(seleccion), 1);
					}
					limpiarFiltros();
					JOptionPane.showMessageDialog(this, 
							msg, 
	                        "Notificación", 
	                        JOptionPane.INFORMATION_MESSAGE);
				}
			} else {
				JOptionPane.showMessageDialog(this, 
                        "Selecione una prenda", 
                        "Notificación", 
                        JOptionPane.INFORMATION_MESSAGE);
			}	
			if (!chckbxVistaActivos.isSelected()) {
				actualizarListado(0);
			} else {
				actualizarListado(1);
			}
		}
		
		if (e.getSource() == btnCaracteristicas) {
			setEnabled(false);
			PanelCaracteristicas caracteristicas = new PanelCaracteristicas(gestorPrendas, this);
			caracteristicas.setVisible(true);
		}
		
		if (e.getSource() == chckbxVistaActivos) {
			if (!chckbxVistaActivos.isSelected()) {
				actualizarListado(0);
				limpiarFiltros();
				btnCambioEstado.setText("Activar");
				chckbxVistaActivos.setSelected(false);
			} else {
				actualizarListado(1);
				limpiarFiltros();
				btnCambioEstado.setText("Desactivar");
				chckbxVistaActivos.setSelected(true);
			}
		}
		
		if (e.getSource() == cbFiltroColor || e.getSource() == cbFiltroMarca
				|| e.getSource() == cbFiltroTalle || e.getSource() == cbFiltroTipo
				|| e.getSource() == cbFiltroCategoria) {
			
			Predicate<Prenda> marcaFiltro = p -> cbFiltroMarca.getSelectedIndex() <= 0 
											|| p.getMarca().equals(cbFiltroMarca.getSelectedItem().toString());
			Predicate<Prenda> tipoFiltro = p -> cbFiltroTipo.getSelectedIndex() <= 0 
											|| p.getTipo().equals(cbFiltroTipo.getSelectedItem().toString());
			Predicate<Prenda> colorFiltro = p -> cbFiltroColor.getSelectedIndex() <= 0 
											|| p.getColor().equals(cbFiltroColor.getSelectedItem().toString());
			Predicate<Prenda> talleFiltro = p -> cbFiltroTalle.getSelectedIndex() <= 0 
											|| p.getTalle() == (Integer.parseInt(cbFiltroTalle.getSelectedItem().toString()));
			Predicate<Prenda> categoriaFiltro = p -> cbFiltroCategoria.getSelectedIndex() <= 0 
											|| p.getCategoria().equals(cbFiltroCategoria.getSelectedItem().toString());

			Predicate<Prenda> filtroFinal = marcaFiltro.and(tipoFiltro).and(colorFiltro).and(talleFiltro)
					.and(categoriaFiltro);
			actualizarListado(filtroFinal);
		}	
		
		if (e.getSource() == btnHistorialPreciosCostos) {
			int seleccion = table.getSelectedRow();
			if (seleccion != -1) { 
			    if (mapaFilaRopa.containsKey(seleccion)) {
			    	setEnabled(false);
			        PanelHistorialPrecioCosto mod = new PanelHistorialPrecioCosto(this, 
			        		mapaFilaRopa.get(seleccion));
			        mod.setVisible(true);
			        mod.toFront();
			    }
			}
			else {
				JOptionPane.showMessageDialog(this, 
			            "Seleccione un pantalón", 
			            "Notificación", 
			            JOptionPane.INFORMATION_MESSAGE);
			}
		}
		
		if(e.getSource() == btnAjustePrecio) {
			String procentaje = JOptionPane.showInputDialog(this, 
		            "Ingrese el porcentaje (positivo para aumento, negativo para descuento):", 
		            "Ajuste de Precios", 
		            JOptionPane.QUESTION_MESSAGE);
			if(procentaje != null) {
				int confirmado = JOptionPane.showConfirmDialog(
		            this,
		            "¿Realizar cambio de precio a los panralones?",
		            "Confirmar el cambio",
		            JOptionPane.OK_CANCEL_OPTION,
		            JOptionPane.PLAIN_MESSAGE);
				if (confirmado == JOptionPane.OK_OPTION) {
					try {
						int porcentajeModificar = Integer.parseInt(procentaje.trim());
						modificarPrecio(porcentajeModificar);
					} catch (NumberFormatException ex) {
						 JOptionPane.showMessageDialog(this, 
				                    "Porcentaje inválido. Por favor ingrese un número.", 
				                    "Error", 
				                    JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		}
		if (e.getSource() == btnLimpiarFiltros) {
			limpiarFiltros();
		}
	}

	private void modificarPrecio(int porcentajeModificar) {
		for (Prenda p : prendasFiltradas) {
			double valorAumento;
			valorAumento = (p.getUltimoPrecio() * porcentajeModificar) / 100;
			gestorPrendas.modificarPrenda((p.getUltimoPrecio() + valorAumento), 
								p.getUltimoCosto(), p.getTalle(), p.getStock(), 
								p.getDescripcion(), p);
			actualizarListado(estado);
		}
	}

	public void actualizar() {
		cargarComboBox();
		if (chckbxVistaActivos.isSelected()) {
			actualizarListado(1);
		} else {
			actualizarListado(0);
		}
		
	}
	
}
