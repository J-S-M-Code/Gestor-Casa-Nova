package com.gestorCN.ui.sales;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import com.gestorCN.logic.sales.GestorVentas;
import com.gestorCN.logic.stock.GestorPrendas;
import com.gestorCN.logic.stock.Prenda;
import com.gestorCN.ui.stock.PanelGestorStock;

import java.awt.BorderLayout;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JButton;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import java.awt.FlowLayout;
import javax.swing.JLabel;
import javax.swing.JTextField;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.GregorianCalendar;
import java.util.List;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JList;
public class PanelGestorVentas extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	
	private GestorVentas gestorVentas;
	private GestorPrendas gestorPrendas;
	private ArrayList<Prenda> prendaSeleccionada;
	
	private JTable tableProductos;
	private JTextField textFieldFecha;
	private JTextField textFieldNumeroVenta;
	private JTextField textFieldBuscador;
	private DefaultTableModel model;
	private JButton btnGestorPrendas;
	private JButton btnGuardarVenta;
	private JButton btnCancelarVenta;
	private JButton btnReportesVentas;
	private JPanel panelInferior;
	private JButton btnEliminarPrenda;
	private JLabel lblItems;
	private JLabel lblTotal;
	private JTextField textFieldItems;
	private JTextField textFieldTotal;
	private JLabel lblCentrador;
	private JComboBox<?> comboBoxMediosPagos;
	private JComboBox<?> comboBoxCuotas;
	private JScrollPane scrollPaneSugerencia;
	private JList<Object> listSugerencias;
	private DefaultListModel<Object> modeloLista;

 	public PanelGestorVentas(GestorVentas gestorVentas, GestorPrendas gestorPrendas) {
 		setTitle("Casa Nova - Gestor Ventas");
 		
 		Image icono = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/images/icono.jpg"));
	    setIconImage(icono);
	    
	    addWindowListener(new WindowAdapter() {
	        @Override
	        public void windowClosing(WindowEvent e) {
	            cancelarVenta(); 
	            System.exit(0);  
	        }
	    });
		setBounds(100, 100, 677, 475);
		setMinimumSize(new Dimension(865, 600));
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		setLocationRelativeTo(null);
		
		this.gestorPrendas = gestorPrendas;
		this.gestorVentas = gestorVentas;
		this.prendaSeleccionada = new ArrayList<Prenda>();
		
		iniciarComponentes();
		caragarCampos();
	}

	private void caragarCampos() {
		GregorianCalendar fecha = new GregorianCalendar();
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		textFieldFecha.setText(sdf.format(fecha.getTime()));
		
		/* Obtengo la cantidad de ventas para calcular el numero de la proxima */
		int nroVenta = gestorVentas.getCantidadVentas() + 1;
		
		textFieldNumeroVenta.setText(Integer.toString(nroVenta));
	}

	@SuppressWarnings({ "serial", "unchecked", "rawtypes" })
	private void iniciarComponentes() {
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		JPanel panelInformacionVenta = new JPanel();
		FlowLayout flowLayout = (FlowLayout) panelInformacionVenta.getLayout();
		flowLayout.setAlignment(FlowLayout.LEFT);
		contentPane.add(panelInformacionVenta, BorderLayout.NORTH);
		
		JLabel lblFecha = new JLabel("Fecha:  ");
		lblFecha.setFont(new Font("Tahoma", Font.PLAIN, 15));
		panelInformacionVenta.add(lblFecha);
		
		textFieldFecha = new JTextField();
		textFieldFecha.setEditable(false);
		textFieldFecha.setFont(new Font("Tahoma", Font.PLAIN, 15));
		panelInformacionVenta.add(textFieldFecha);
		textFieldFecha.setColumns(10);
		
		JLabel lblNumeroVenta = new JLabel("N° Venta:  ");
		lblNumeroVenta.setFont(new Font("Tahoma", Font.PLAIN, 15));
		panelInformacionVenta.add(lblNumeroVenta);
		
		textFieldNumeroVenta = new JTextField();
		textFieldNumeroVenta.setEditable(false);
		textFieldNumeroVenta.setFont(new Font("Tahoma", Font.PLAIN, 15));
		panelInformacionVenta.add(textFieldNumeroVenta);
		textFieldNumeroVenta.setColumns(10);
		
		JPanel panelContenedor = new JPanel();
		contentPane.add(panelContenedor, BorderLayout.CENTER);
		panelContenedor.setLayout(new BorderLayout(0, 0));
		
		JPanel panelBusquedaProducto = new JPanel();
		panelBusquedaProducto.setMinimumSize(new Dimension(5, 5));
		panelBusquedaProducto.setMaximumSize(new Dimension(20, 20));
		panelContenedor.add(panelBusquedaProducto, BorderLayout.NORTH);
		panelBusquedaProducto.setLayout(new BorderLayout(0, 0));
		
		JPanel panelGestorRopa = new JPanel();

		panelGestorRopa.setMinimumSize(new Dimension(150, 50));
		panelGestorRopa.setPreferredSize(new Dimension(150, 50));
		panelBusquedaProducto.add(panelGestorRopa, BorderLayout.EAST);
		
		btnGestorPrendas = new JButton("Ver Ropa");
		btnGestorPrendas.setFont(new Font("Tahoma", Font.PLAIN, 15));
		btnGestorPrendas.addActionListener(this);
		GroupLayout gl_panelGestorRopa = new GroupLayout(panelGestorRopa);
		gl_panelGestorRopa.setHorizontalGroup(

			gl_panelGestorRopa.createParallelGroup(Alignment.LEADING)
				.addGroup(Alignment.TRAILING, gl_panelGestorRopa.createSequentialGroup()
					.addContainerGap()
					.addComponent(btnGestorPrendas, GroupLayout.DEFAULT_SIZE, 130, Short.MAX_VALUE)
					.addContainerGap())
		);
		gl_panelGestorRopa.setVerticalGroup(
			gl_panelGestorRopa.createParallelGroup(Alignment.TRAILING)
				.addGroup(Alignment.LEADING, gl_panelGestorRopa.createSequentialGroup()
					.addGap(63)
					.addComponent(btnGestorPrendas)
					.addContainerGap(68, Short.MAX_VALUE))
		);
		panelGestorRopa.setLayout(gl_panelGestorRopa);
		
		JPanel panelBuscador = new JPanel();
		panelBusquedaProducto.add(panelBuscador, BorderLayout.CENTER);
		panelBuscador.setLayout(new BorderLayout(0, 0));
		
		textFieldBuscador = new JTextField();
		textFieldBuscador.setFont(new Font("Tahoma", Font.PLAIN, 18));
		textFieldBuscador.setColumns(40);
		
		modeloLista = new DefaultListModel<>();
		listSugerencias = new JList<>(modeloLista);
		listSugerencias.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		scrollPaneSugerencia = new JScrollPane(listSugerencias);
		scrollPaneSugerencia.setBounds(20, 50, 340, 100);
		scrollPaneSugerencia.setVisible(true);

		panelBuscador.add(textFieldBuscador, BorderLayout.NORTH);
		panelBuscador.add(scrollPaneSugerencia, BorderLayout.CENTER);
		
		//Buscar mientras escribe
		textFieldBuscador.getDocument().addDocumentListener(new DocumentListener() {
			public void insertUpdate(DocumentEvent e) { mostrarSugerencias();}
			public void removeUpdate(DocumentEvent e) { mostrarSugerencias();}
			public void changedUpdate(DocumentEvent e) { mostrarSugerencias();}
		});
		// Click en item
		listSugerencias.addMouseListener(new MouseAdapter() {
		    public void mouseClicked(MouseEvent e) {
		        if (!listSugerencias.isSelectionEmpty()) {
		            Prenda prenda = (Prenda) listSugerencias.getSelectedValue();

		            if (prenda.getStock() == 0) {
		                JOptionPane.showMessageDialog(
		                    null,
		                    "No existe stock de la prenda",
		                    "Notificación",
		                    JOptionPane.INFORMATION_MESSAGE
		                );
		                textFieldBuscador.setText("");
		                mostrarSugerencias();
		            } else {
		                int confirmado = JOptionPane.showConfirmDialog(
		                    null,
		                    "¿Desea seleccionar esta prenda? \n" + prenda,
		                    "Confirmar",
		                    JOptionPane.OK_CANCEL_OPTION,
		                    JOptionPane.PLAIN_MESSAGE
		                );

		                if (confirmado == JOptionPane.OK_OPTION) {
		                    gestorPrendas.modificarStock(prenda, prenda.getStock() - 1);
		                    prendaSeleccionada.add(prenda);
		                    textFieldBuscador.setText("");
		                    actualizarProductos();
		                    mostrarSugerencias();
		                }
		            }
		        }
		    }
		});
		textFieldBuscador.addKeyListener(new KeyAdapter() {
		    public void keyPressed(KeyEvent e) {
		        if (e.getKeyCode() == KeyEvent.VK_ENTER && !listSugerencias.isSelectionEmpty()) {
		        	listSugerencias.dispatchEvent(new MouseEvent(
		        			listSugerencias,
		                MouseEvent.MOUSE_CLICKED,
		                System.currentTimeMillis(),
		                0,
		                0,
		                0,
		                1,
		                false
		            ));
		        }
		        if (e.getKeyCode() == KeyEvent.VK_DOWN) {
		            int index = listSugerencias.getSelectedIndex();
		            if (index < modeloLista.size() - 1) {
		                listSugerencias.setSelectedIndex(index + 1);
		                listSugerencias.ensureIndexIsVisible(index + 1);
		            }
		        } else if (e.getKeyCode() == KeyEvent.VK_UP) {
		            int index = listSugerencias.getSelectedIndex();
		            if (index > 0) {
		                listSugerencias.setSelectedIndex(index - 1);
		                listSugerencias.ensureIndexIsVisible(index - 1);
		            }
		        } 
		    }
		});

		
		JPanel panelAcciones = new JPanel();
		panelAcciones.setPreferredSize(new Dimension(150, 10));
		panelContenedor.add(panelAcciones, BorderLayout.EAST);
		
		btnGuardarVenta = new JButton("Guardar");
		btnGuardarVenta.setFont(new Font("Tahoma", Font.PLAIN, 15));
		btnGuardarVenta.setVisible(false);
		btnGuardarVenta.addActionListener(this);
		
		btnReportesVentas = new JButton("Reporte");
		btnReportesVentas.setFont(new Font("Tahoma", Font.PLAIN, 15));
		btnReportesVentas.addActionListener(this);
		
		btnEliminarPrenda = new JButton("Eliminar Prenda");
		btnEliminarPrenda.setFont(new Font("Tahoma", Font.PLAIN, 15));
		btnEliminarPrenda.addActionListener(this);
		
		comboBoxMediosPagos = new JComboBox();
		comboBoxMediosPagos.setFont(new Font("Tahoma", Font.PLAIN, 15));
		comboBoxMediosPagos.setModel(new DefaultComboBoxModel(new String[] {"Medios de pago", 
									"Efectivo", "Tarjeta", "Chachos", "Transferencia"}));
		comboBoxMediosPagos.addItemListener(e -> {
			if (e.getStateChange() == ItemEvent.SELECTED) {
				calcularMontoTotal();
				btnGuardarVenta.setVisible(comboBoxMediosPagos.getSelectedIndex() != 0
						&& comboBoxCuotas.getSelectedIndex() != 0);
			}
		});
		
		comboBoxCuotas = new JComboBox();
		comboBoxCuotas.setFont(new Font("Tahoma", Font.PLAIN, 15));
		comboBoxCuotas.setModel(new DefaultComboBoxModel(new String[] {"Coutas", "1", "3",
								"6", "9", "12"}));
		comboBoxCuotas.addItemListener(e -> {
			if (e.getStateChange() == ItemEvent.SELECTED) {
				btnGuardarVenta.setVisible(comboBoxCuotas.getSelectedIndex() != 0
						&& comboBoxMediosPagos.getSelectedIndex() != 0);
			}
		});
		
		GroupLayout gl_panelAcciones = new GroupLayout(panelAcciones);
		gl_panelAcciones.setHorizontalGroup(
			gl_panelAcciones.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panelAcciones.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panelAcciones.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panelAcciones.createSequentialGroup()
							.addGroup(gl_panelAcciones.createParallelGroup(Alignment.TRAILING)
								.addComponent(btnEliminarPrenda, Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 134, Short.MAX_VALUE)
								.addComponent(comboBoxMediosPagos, Alignment.LEADING, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(comboBoxCuotas, Alignment.LEADING, 0, 134, Short.MAX_VALUE)
								.addComponent(btnGuardarVenta, GroupLayout.DEFAULT_SIZE, 134, Short.MAX_VALUE))
							.addGap(6))
						.addGroup(Alignment.TRAILING, gl_panelAcciones.createSequentialGroup()
							.addComponent(btnReportesVentas, GroupLayout.DEFAULT_SIZE, 130, Short.MAX_VALUE)
							.addContainerGap())))
		);
		gl_panelAcciones.setVerticalGroup(
			gl_panelAcciones.createParallelGroup(Alignment.TRAILING)
				.addGroup(Alignment.LEADING, gl_panelAcciones.createSequentialGroup()
					.addGap(38)
					.addComponent(btnEliminarPrenda)
					.addGap(58)
					.addComponent(comboBoxMediosPagos, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addGap(48)
					.addComponent(comboBoxCuotas, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addGap(65)
					.addComponent(btnGuardarVenta)

					.addPreferredGap(ComponentPlacement.RELATED, 88, Short.MAX_VALUE)
					.addComponent(btnReportesVentas)
					.addContainerGap())
		);
		panelAcciones.setLayout(gl_panelAcciones);
		
		JPanel panelProductosVenta = new JPanel();
		panelContenedor.add(panelProductosVenta, BorderLayout.CENTER);
		panelProductosVenta.setLayout(new BorderLayout(0, 0));
		
		JScrollPane scrollPane = new JScrollPane();
		panelProductosVenta.add(scrollPane);
		

		String[] columnas = {"ID", "Marca", "Categoria", "Tipo",
				"Color", "Descripcion", "Talle", "Precio"};
		model = new DefaultTableModel(columnas, 0);
		tableProductos = new JTable(model) {
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
        tableProductos.getColumnModel().getColumn(0).setCellRenderer(alineacionDerecha);

        tableProductos.getColumnModel().getColumn(6).setCellRenderer(alineacionDerecha);
        tableProductos.getColumnModel().getColumn(7).setCellRenderer(alineacionDerecha);
        for (int i = 1; i <= 5; i++) {
        	tableProductos.getColumnModel().getColumn(i).setCellRenderer(alineacionIzquierda);
        }
        
		tableProductos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tableProductos.setShowVerticalLines(false);
		tableProductos.setFont(new Font("Tahoma", Font.PLAIN, 15));
		tableProductos.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		scrollPane.setViewportView(tableProductos);
		
		panelInferior = new JPanel();
		FlowLayout fl_panelInferior = (FlowLayout) panelInferior.getLayout();
		fl_panelInferior.setAlignment(FlowLayout.RIGHT);
		panelProductosVenta.add(panelInferior, BorderLayout.SOUTH);
		
		lblItems = new JLabel("Items: ");
		lblItems.setFont(new Font("Tahoma", Font.PLAIN, 15));
		panelInferior.add(lblItems);
		
		textFieldItems = new JTextField();
		textFieldItems.setEditable(false);
		textFieldItems.setFont(new Font("Tahoma", Font.PLAIN, 15));
		panelInferior.add(textFieldItems);
		textFieldItems.setColumns(10);
		
		lblTotal = new JLabel("Total: ");
		lblTotal.setFont(new Font("Tahoma", Font.PLAIN, 15));
		panelInferior.add(lblTotal);
		
		textFieldTotal = new JTextField();
		textFieldTotal.setEditable(false);
		textFieldTotal.setFont(new Font("Tahoma", Font.PLAIN, 15));
		panelInferior.add(textFieldTotal);
		textFieldTotal.setColumns(10);
		
		lblCentrador = new JLabel("             ");
		lblCentrador.setFont(new Font("Tahoma", Font.PLAIN, 15));
		panelInferior.add(lblCentrador);
		
		btnCancelarVenta = new JButton("Cancelar Venta");
		panelInferior.add(btnCancelarVenta);
		btnCancelarVenta.setFont(new Font("Tahoma", Font.PLAIN, 15));
		btnCancelarVenta.addActionListener(this);
	}
	
	private void mostrarSugerencias() {
        String texto = textFieldBuscador.getText().toUpperCase();
        
        if (texto.isEmpty()) {
        	modeloLista.clear();
        	return;
        }
        
        String[] palabrasClaves = texto.split(" ");
        List<Prenda> coincidencias = new ArrayList<Prenda>();
        for (Prenda ropa : gestorPrendas.getPrendasActivas()) {
        	boolean todasCoinciden = true;
        	for (String palabra : palabrasClaves) {
        		boolean coincidenAtributos = String.valueOf(ropa.getIdRopa()).contains(palabra) ||
        				ropa.getCategoria().contains(palabra) ||
        				ropa.getTipo().contains(palabra) ||
        				ropa.getColor().contains(palabra) ||
        				ropa.getDescripcion().contains(palabra) ||
        				ropa.getMarca().contains(palabra) ||
        				String.valueOf(ropa.getTalle()).contains(palabra);
        		if (!coincidenAtributos) {
                    todasCoinciden = false;
                    break;
                }
        	}
        	if (todasCoinciden) {
                coincidencias.add(ropa);
            }
        }
        modeloLista.clear();
        coincidencias.sort(Comparator.comparingInt(Prenda::getIdRopa));
        if (!coincidencias.isEmpty()) {
        	scrollPaneSugerencia.setVisible(true);
            for (Prenda p : coincidencias) {
            	modeloLista.addElement(p);
            }
            listSugerencias.setSelectedIndex(0); // Preseleccionar primer ítem
        } 
    }
	
	private void actualizarProductos() {
		model.setRowCount(0);
		if (prendaSeleccionada != null) {
			for (Prenda p : prendaSeleccionada) {
				Object[] fila = {
						p.getIdRopa(),
						p.getMarca(),
						p.getCategoria(),
						p.getTipo(),
						p.getColor(),
						p.getDescripcion(),
						p.getTalle(),
						p.getUltimoPrecio()
				};
				model.addRow(fila);
			}
		}
		textFieldItems.setText(Integer.toString(prendaSeleccionada.size()));
		calcularMontoTotal();
	}

	private void calcularMontoTotal() {
		double montoTotal = 0.0;
		for (Prenda r : prendaSeleccionada) {
			montoTotal += r.getUltimoPrecio();
		}
		if (comboBoxMediosPagos.getSelectedItem().toString()
				.toUpperCase().equals("TARJETA")) {
			montoTotal *= 1.10;
		}
		if (montoTotal != 0.0) {
			textFieldTotal.setText(String.format("%.1f", montoTotal));
		}
	}

	private void limpiar() {
		model.setRowCount(0);
		textFieldTotal.setText("");
		textFieldItems.setText("");
		textFieldBuscador.setText("");
		comboBoxCuotas.setSelectedIndex(0);
		comboBoxMediosPagos.setSelectedIndex(0);
		prendaSeleccionada.clear();
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btnGestorPrendas) {
			setEnabled(false);
			PanelGestorStock gr = new PanelGestorStock(PanelGestorVentas.this, gestorPrendas);	
			gr.setVisible(true);
			gr.toFront();
			gr.actualizar();
		}
		
		if (e.getSource() == btnEliminarPrenda) {
			int seleccion = tableProductos.getSelectedRow();
			if (seleccion != -1) {
				int confirmado = JOptionPane.showConfirmDialog(
			            this,
			            "¿Desea eliminar esta prenda? \n" + prendaSeleccionada.get(seleccion),
			            "Confirmar",
			            JOptionPane.OK_CANCEL_OPTION,
			            JOptionPane.PLAIN_MESSAGE);
            	if (confirmado == JOptionPane.OK_OPTION) {
            		gestorPrendas.modificarStock(prendaSeleccionada.get(seleccion), 
            							prendaSeleccionada.get(seleccion).getStock() +1);
            		prendaSeleccionada.remove(seleccion);
                    actualizarProductos();
                    calcularMontoTotal();
            	}
			} else {
				JOptionPane.showMessageDialog(this, 
                        "Selecione una prenda", 
                        "Notificación", 
                        JOptionPane.INFORMATION_MESSAGE);
			}
		}
		
		if (e.getSource() == btnGuardarVenta) {
			Calendar fechaVenta = GregorianCalendar.getInstance();
			fechaVenta.set(Calendar.HOUR_OF_DAY, 0);
			fechaVenta.set(Calendar.MINUTE, 0);
			fechaVenta.set(Calendar.SECOND, 0);
			fechaVenta.set(Calendar.MILLISECOND, 0);
			gestorVentas.nuevaVenta(Integer.parseInt(textFieldNumeroVenta.getText()), fechaVenta,
					prendaSeleccionada,
					comboBoxMediosPagos.getSelectedItem().toString().toUpperCase(),
					Integer.parseInt(comboBoxCuotas.getSelectedItem().toString()));
			
			limpiar();
			textFieldNumeroVenta.setText(Integer.toString(gestorVentas.getCantidadVentas()+1));
		}
		
		if (e.getSource() == btnCancelarVenta) {
			cancelarVenta();
		}

		if (e.getSource() == btnReportesVentas) {
			PanelReporteFacturas rv = new PanelReporteFacturas(this, gestorVentas, gestorPrendas);
			setEnabled(false);
			rv.setVisible(true);
			rv.toFront();
		}
	}

	private void cancelarVenta() {
		for (Prenda r : prendaSeleccionada) {
			gestorPrendas.modificarStock(r, r.getStock() + 1);
		}
		limpiar();
	}
	
}
