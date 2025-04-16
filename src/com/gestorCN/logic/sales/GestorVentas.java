package com.gestorCN.logic.sales;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.swing.JOptionPane;

import com.gestorCN.logic.stock.Prenda;

public class GestorVentas {
	
	private static GestorVentas gestorVentas;
	private ArrayList<Venta> listaVentas; 
	
	//private String url = "jdbc:oracle:thin:@//localhost:2426/CasaNovaBD";
	//private String usuario = "SYSTEM";
	//private String password = "Joaco1564";
	
	private String dbPath = new File("GestorRopa.accdb").getAbsolutePath();
	private final String url = "jdbc:ucanaccess://" + dbPath;
//	private final String url = "jdbc:ucanaccess://C:\\Users\\joaco\\OneDrive\\Desktop\\Proyecto\\Gestor JAR\\GestorRopa.accdb";
	private Connection conexion;
	
	public static GestorVentas getInstance() {
		if (gestorVentas != null) {
			return gestorVentas;
		}
		return gestorVentas = new GestorVentas();
	}

	private GestorVentas() {
		listaVentas = new ArrayList<Venta>();
		cargarVentas();
	}

	private void cargarVentas() {
		Statement stmt = null;
		PreparedStatement psItems = null;
		
		String sqlVentas;
		String sqlItems; 
		
		try {
			//conexion = DriverManager.getConnection(url, usuario, password);
			Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
			conexion = DriverManager.getConnection(url);
			
			stmt = conexion.createStatement();
			sqlVentas = "SELECT * "
					+ "FROM ventas ";
			sqlItems = "SELECT * "
					+ "FROM items "
					+ "WHERE nroVenta = ?";
			ResultSet rsVentas = stmt.executeQuery(sqlVentas);
			psItems = conexion.prepareStatement(sqlItems);
			while (rsVentas.next()) {
				int nroVenta = rsVentas.getInt("nroVenta");
				double monto = rsVentas.getDouble("monto");
				double costo = rsVentas.getDouble("costo");
				int cuotas = rsVentas.getInt("cuotas");
				String medioPago = rsVentas.getString("medioPago"); 
				GregorianCalendar fecha = new GregorianCalendar();
				Timestamp timestampFecha = rsVentas.getTimestamp("fecha");
				if (timestampFecha != null) {
					fecha.setTime(timestampFecha);
				} else {
					fecha = null;
				}
				
				/* Obtengo los id de la ropa */
				psItems.setInt(1, nroVenta);
				ResultSet rsItems = psItems.executeQuery();
				ArrayList<Integer> idRopaVenta = new ArrayList<Integer>();
				while (rsItems.next()) {
					int idRopa = rsItems.getInt("ropaId");
					idRopaVenta.add(idRopa);
				}
				
				listaVentas.add(new Venta(nroVenta, fecha, monto, 
						costo, medioPago, cuotas, idRopaVenta));
			}
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, 
			        "Error en la conexión con la BD:\n" + e.getMessage(), 
			        "Error", 
			        JOptionPane.ERROR_MESSAGE);
		} catch (ClassNotFoundException e){
			System.out.println("No se encontró el driver de UCanAccess");
		    e.printStackTrace();
		}
	}

	public int getCantidadVentas() {
		listaVentas.clear();
		cargarVentas();
		return this.listaVentas.size();
	}
	
	public String nuevaVenta(int nroVenta, Calendar fecha, ArrayList<Prenda> 
	prendasVenta, String medioPago, int cuotas) {
		String msg = "";
		PreparedStatement psNuevaVenta = null;
		
		try {
			//conexion = DriverManager.getConnection(url, usuario, password);
			conexion = DriverManager.getConnection(url);

			Venta venta = new Venta(nroVenta, fecha, prendasVenta, medioPago, cuotas);

			String sqlNuevaVenta = "INSERT INTO ventas "
						+ "(nroVenta, fecha, monto, costo, medioPago, cuotas)"
						+ "VALUES (?, ?, ?, ?, ?, ?)";
			psNuevaVenta = conexion.prepareStatement(sqlNuevaVenta, Statement.RETURN_GENERATED_KEYS);
			psNuevaVenta.setInt(1, nroVenta);
			psNuevaVenta.setDate(2, new java.sql.Date(fecha.getTimeInMillis()));
			psNuevaVenta.setDouble(3, venta.getMonto());
			psNuevaVenta.setDouble(4, venta.getCosto());
			psNuevaVenta.setString(5, medioPago.toUpperCase());
			psNuevaVenta.setInt(6, cuotas);
			psNuevaVenta.executeUpdate();

			ResultSet rs = psNuevaVenta.getGeneratedKeys();
			if (!rs.next()) {
				throw new SQLException("Error: No se pudo obtener el ID generado para el registro en ropa.");
			}
			if (venta.getNroVenta() == rs.getInt(1)) {
				/* Cargo los items */
				for (Prenda r : prendasVenta) {
					PreparedStatement psItems = null;
					
					String sqlItems = "INSERT INTO items"
							+ "(nroVenta, ropaId)"
							+ "VALUES (?, ?)";
					psItems = conexion.prepareStatement(sqlItems);
					psItems.setInt(1, nroVenta);
					psItems.setInt(2, r.getIdRopa());
					psItems.executeUpdate();
					psItems.close();
				}
			}
			msg = "Venta cargada con exito";
		} catch (SQLException e) {
			msg = "Error en la base de datos";
		} finally {
			try {
				if (psNuevaVenta != null) psNuevaVenta.close();
			} catch (SQLException e) {
				msg = "Error en la base de datos";
			}	
		}
		return msg;
	}
	
	public ArrayList<Venta> getReporteVentas(String fechaDesde, String fechaHasta) {
		ArrayList<Venta> listaVentasFecha = new ArrayList<Venta>();
		ArrayList<Integer> listaItems;
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		
		PreparedStatement psVentasFecha = null;
		PreparedStatement psItemsFecha = null;
		
		int numeroVenta;
		GregorianCalendar fecha;
		double monto;
		double costo;
		String medioPago;
		int cuotas;
		int item;
		
		try {
			java.util.Date utilDateDesde = sdf.parse(fechaDesde);
			java.util.Date utilDateHasta = sdf.parse(fechaHasta);
			java.sql.Date desde = new java.sql.Date(utilDateDesde.getTime());
			java.sql.Date hasta = new java.sql.Date(utilDateHasta.getTime());
	        
			//conexion = DriverManager.getConnection(url, usuario, password);
			conexion = DriverManager.getConnection(url);
			
			String sqlVentasFechas = "SELECT * FROM ventas " 
			         + "WHERE fecha >= ? AND fecha <= ?";
			psVentasFecha = conexion.prepareStatement(sqlVentasFechas);
			psVentasFecha.setDate(1, desde);
			psVentasFecha.setDate(2, hasta);
			ResultSet rsVentasFecha = psVentasFecha.executeQuery();
			
			while (rsVentasFecha.next()) {
				/* Venta */
				numeroVenta = rsVentasFecha.getInt("nroVenta");
				monto = rsVentasFecha.getDouble("monto");
				costo = rsVentasFecha.getDouble("costo");
				medioPago = rsVentasFecha.getString("medioPago");
				cuotas = rsVentasFecha.getInt("cuotas");
				
				fecha = new GregorianCalendar();
				Timestamp timestampFecha = rsVentasFecha.getTimestamp("fecha");
				if (timestampFecha != null) {
					fecha.setTime(timestampFecha);
				} else {
					fecha = null;
				}
				
				/* Items */
				listaItems = new ArrayList<Integer>();
				String sqlItemsVenta = "SELECT * FROM items "
						+ "WHERE nroVenta = ?";
				psItemsFecha = conexion.prepareStatement(sqlItemsVenta);
				psItemsFecha.setInt(1, numeroVenta);
				ResultSet rsItemsVenta = psItemsFecha.executeQuery();
				while (rsItemsVenta.next()) {
					item = rsItemsVenta.getInt("ropaId");
					listaItems.add(item);
				}
				listaVentasFecha.add(new Venta(numeroVenta, fecha, monto, 
						costo, medioPago, cuotas, listaItems));
			}
	    } catch (ParseException e) {
	    	JOptionPane.showMessageDialog(null, 
			        "Error en la conexión con la BD:\n" + e.getMessage() +
			        e.getErrorOffset(), 
			        "Error", 
			        JOptionPane.ERROR_MESSAGE);
	    } catch (SQLException e) {
	    	JOptionPane.showMessageDialog(null, 
			        "Error en la conexión con la BD:\n" + e.getMessage() +
			        e.getErrorCode(), 
			        "Error", 
			        JOptionPane.ERROR_MESSAGE);
	    }
		return listaVentasFecha;
	}	
	
}
