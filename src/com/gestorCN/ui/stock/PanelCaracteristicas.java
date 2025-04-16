package com.gestorCN.ui.stock;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.gestorCN.logic.stock.GestorPrendas;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import java.awt.Font;
import java.awt.Image;

import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.Toolkit;

public class PanelCaracteristicas extends JFrame implements ActionListener {
	
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	private GestorPrendas gestorPrendas;
	
	private JTextField textFieldMarca;
	private JTextField textFieldTipo;
	private JTextField textFieldTalle;
	private JTextField textFieldColor;
	private JButton btnCargar;
	private String msg = null;
	private JTextField textFieldCategoria;
	
	public PanelCaracteristicas(GestorPrendas gestorPrendas, PanelGestorStock panelGestorStock) {
		Image icono = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/images/icono.jpg"));
	    setIconImage(icono);
	    
		setTitle("Casa Nova - Cargar Caraceristicas");
		setType(Type.UTILITY);
		setResizable(false);
		setAlwaysOnTop(true);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 340, 521);
		setLocationRelativeTo(null);
		
		this.gestorPrendas = gestorPrendas;
		
		addWindowListener(new WindowAdapter() { //Para cuando cierre se active la pestaña anterior 
			@Override
			public void windowClosing(WindowEvent e) {
				panelGestorStock.setEnabled(true);
				panelGestorStock.toFront();
				panelGestorStock.actualizar();
			}
		});
		
		iniciarComponentes();
	}

	private void iniciarComponentes() {
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblMarca = new JLabel("Marca");
		lblMarca.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblMarca.setBounds(138, 11, 46, 19);
		contentPane.add(lblMarca);
		
		JLabel lblTipo = new JLabel("Tipo");
		lblTipo.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblTipo.setBounds(138, 171, 46, 19);
		contentPane.add(lblTipo);
		
		JLabel lblTalle = new JLabel("Talle");
		lblTalle.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblTalle.setBounds(138, 263, 46, 19);
		contentPane.add(lblTalle);
		
		JLabel lblColor = new JLabel("Color");
		lblColor.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblColor.setBounds(138, 356, 46, 19);
		contentPane.add(lblColor);
		
		textFieldMarca = new JTextField();
		textFieldMarca.setBounds(98, 40, 126, 20);
		contentPane.add(textFieldMarca);
		textFieldMarca.setColumns(10);
		
		textFieldTipo = new JTextField();
		textFieldTipo.setColumns(10);
		textFieldTipo.setBounds(98, 201, 126, 20);
		contentPane.add(textFieldTipo);
		
		textFieldTalle = new JTextField();
		textFieldTalle.setColumns(10);
		textFieldTalle.setBounds(98, 293, 126, 20);
		contentPane.add(textFieldTalle);
		
		textFieldColor = new JTextField();
		textFieldColor.setColumns(10);
		textFieldColor.setBounds(98, 386, 126, 20);
		contentPane.add(textFieldColor);
		
		btnCargar = new JButton("Cargar");
		btnCargar.setBounds(116, 433, 89, 23);
		btnCargar.addActionListener(this);
		contentPane.add(btnCargar);
		
		textFieldCategoria = new JTextField();
		textFieldCategoria.setColumns(10);
		textFieldCategoria.setBounds(98, 121, 126, 20);
		contentPane.add(textFieldCategoria);
		
		JLabel lblCategoria = new JLabel("Categoria");
		lblCategoria.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblCategoria.setBounds(127, 91, 68, 19);
		contentPane.add(lblCategoria);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		if (e.getSource() == btnCargar) {
			msg = "";
			boolean entro = false; 
			if(!textFieldMarca.getText().trim().isEmpty()) {
				msg = gestorPrendas.nuevaMarca(textFieldMarca.getText().trim().toUpperCase());
				textFieldMarca.setText("");
				msg += " en el campo marca\n";
				entro = true;
			}
			if (!textFieldTipo.getText().trim().isEmpty()){
				msg += gestorPrendas.nuevoTipo(textFieldTipo.getText().trim().toUpperCase());
				textFieldTipo.setText("");
				msg += " en el campo tipo\n";
				entro = true;
			}
			if (!textFieldTalle.getText().trim().isEmpty()){
				msg += gestorPrendas.nuevoTalle(textFieldTalle.getText().trim());
				textFieldTalle.setText("");
				msg += " en el campo talle\n";
				entro = true;
			}
			if (!textFieldColor.getText().trim().isEmpty()){
				msg += gestorPrendas.nuevoColor(textFieldColor.getText().trim().toUpperCase());
				textFieldColor.setText("");
				msg += " en el campo color\n";
				entro = true;
			} 
			if (!textFieldCategoria.getText().trim().isEmpty()){
				msg += gestorPrendas.nuevaCategoria(textFieldCategoria.getText().trim().toUpperCase());
				textFieldCategoria.setText("");
				msg += " en el campo categoria\n";
				entro = true;
			}
			if (!entro) {
				msg = "Complete algun campo";
			}
			JOptionPane.showMessageDialog(this, 
	                msg, 
	                "Notificación", 
	                JOptionPane.WARNING_MESSAGE);
			textFieldMarca.setText("");
		}
		gestorPrendas.getCaracteristicas();
	}
}
