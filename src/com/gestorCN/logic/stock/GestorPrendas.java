package com.gestorCN.logic.stock;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.GregorianCalendar;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.swing.JOptionPane;

import com.gestorCN.logic.exceptions.CampoVacio;
import com.gestorCN.logic.exceptions.NumeroInvalido;
import com.gestorCN.logic.exceptions.TextoInvalido;

public class GestorPrendas {
	
	private static GestorPrendas gestorPrendas;
	
	private ArrayList<Prenda> listaPrendas;
	private ArrayList<ArrayList<String>> caracteristicas;
	
	//private String url = "jdbc:oracle:thin:@//localhost:2426/CasaNovaBD";
	//private String usuario = "SYSTEM";
	//private String password = "Joaco1564";
		
	private String dbPath = new File("GestorRopa.accdb").getAbsolutePath();
	private final String url = "jdbc:ucanaccess://" + dbPath;
//	private final String url = "jdbc:ucanaccess://C:\\Users\\joaco\\OneDrive\\Desktop\\Proyecto\\Gestor JAR\\GestorRopa.accdb";
	private Connection conexion;
	
	public static GestorPrendas getInstance() {
		if (gestorPrendas != null) {
			return gestorPrendas;
		}
		return gestorPrendas = new GestorPrendas();
	}

	private GestorPrendas() {
		cargarStock();
		obtenerCaracteristicas();
	}

	public void actualizar() {
		obtenerCaracteristicas();
		cargarStock();
	}
	
	/* Prendas */
	private void cargarStock() {
		/* */
		listaPrendas = new ArrayList<Prenda>();
		/* */
		Statement stmt = null;
		PreparedStatement psCosto = null;
		PreparedStatement psPrecio = null;
		/* */
		try {
			Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
			conexion = DriverManager.getConnection(url);
			stmt = conexion.createStatement();
			
			String sqlPrendas = "SELECT r.*, m.marca, ti.tipo, c.color, t.talle, ca.categoria "
					+ "FROM ((((ropa AS r "
					+ "INNER JOIN marcas AS m ON r.marcaId = m.marcaId)"
					+ "INNER JOIN tipos AS ti ON r.tipoId = ti.tipoId)"
					+ "INNER JOIN talles AS t ON r.talleId = t.talleId)"
					+ "INNER JOIN colores AS c ON r.colorId = c.colorId)"
					+ "INNER JOIN categorias AS ca ON r.categoriaId = ca.categoriaId;";
			String sqlCostoPrenda = "SELECT * FROM costos WHERE ropaId = ?";
			String sqlPrecioPrenda = "SELECT * FROM precios WHERE ropaId = ?";
			
			ResultSet rsPrendas = stmt.executeQuery(sqlPrendas);
			psCosto = conexion.prepareStatement(sqlCostoPrenda);
			psPrecio = conexion.prepareStatement(sqlPrecioPrenda);
			
			while (rsPrendas.next()) {
				/* */
				int idPrenda = rsPrendas.getInt("ropaId");
				String talle = rsPrendas.getString("talle");
				int stock = rsPrendas.getInt("stock");
				int activo = rsPrendas.getInt("activo");
				String marca = rsPrendas.getString("marca");
				String tipo = rsPrendas.getString("tipo");
				String color = rsPrendas.getString("color");
				String descripcion = rsPrendas.getString("descripcion");
				String genero = rsPrendas.getString("genero");
				String categoria = rsPrendas.getString("categoria");
				/* Costo */
				psCosto.setInt(1, idPrenda);
				ResultSet rsCosto = psCosto.executeQuery();
				ArrayList<Costo> listaCosto = new ArrayList<Costo>();
				while (rsCosto.next()) {
					double costo = rsCosto.getDouble("costo");
					GregorianCalendar fechaDesde = new GregorianCalendar();
					GregorianCalendar fechaHasta = new GregorianCalendar();
					Timestamp timestapDesde = rsCosto.getTimestamp("desde");
					Timestamp timestapHasta = rsCosto.getTimestamp("hasta");
					if (timestapDesde != null) {
						fechaDesde.setTime(timestapDesde);
					} else {
						fechaDesde = null;
					}
					if (timestapHasta != null) {
						fechaHasta.setTime(timestapHasta);
					} else {
						fechaHasta = null;
					}
					listaCosto.add(new Costo(costo, fechaDesde, fechaHasta));
				}
				/* Precio */
				psPrecio.setInt(1, idPrenda);
				ResultSet rsPrecio = psPrecio.executeQuery();
				ArrayList<Precio> listaPrecio = new ArrayList<Precio>();
				while (rsPrecio.next()) {
					double precio = rsPrecio.getDouble("precio");
					GregorianCalendar fechaDesde = new GregorianCalendar();
					GregorianCalendar fechaHasta = new GregorianCalendar();
					Timestamp timestapDesde = rsPrecio.getTimestamp("desde");	
					Timestamp timestapHasta = rsPrecio.getTimestamp("hasta");
					if (timestapDesde != null) {
						fechaDesde.setTime(timestapDesde);
					} else {
						fechaDesde = null;
					}
					if (timestapHasta != null) {
						fechaHasta.setTime(timestapHasta);
					} else {
						fechaHasta = null;
					}
					listaPrecio.add(new Precio(precio, fechaDesde, fechaHasta));
				}
				/* */
				listaPrendas.add(new Prenda(idPrenda, tipo, color, talle, descripcion, 
						listaPrecio, listaCosto, marca, stock, activo, genero, categoria));
			}
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, 
			        "Error en la conexión con la BD:\n" + e.getMessage()+
			        e.getErrorCode(), 
			        "Error", 
			        JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		} catch (ClassNotFoundException e){
			System.out.println("No se encontró el driver de UCanAccess");
		    e.printStackTrace();
		} finally {
			try {
				if (psCosto != null) psCosto.close();
				if (psPrecio != null) psPrecio.close();
				if (stmt != null) stmt.close(); 
				if (conexion != null) conexion.close();
			} catch (SQLException e) {
				JOptionPane.showMessageDialog(null, 
				        "Error en la conexión con la BD:\n" + e.getMessage() +
				        e.getErrorCode(), 
				        "Error", 
				        JOptionPane.ERROR_MESSAGE);
			}
		}
	}
	
	public String nuevaPrenda(String marca, String tipo, String color, 
			String descripcion, String talle, int stock,
			double precio, double costo, String genero, String categoria) {
		String msg = "No hizo nada";
		int idNuevaPrenda = this.listaPrendas.size();
		
		try {
			Prenda nuevaPrenda = new Prenda(idNuevaPrenda+1, tipo, color, talle, descripcion,
					precio, costo, marca, stock, genero, categoria);
			if (verificarExistencia(nuevaPrenda)) {
				PreparedStatement psRopa = null;
				PreparedStatement psPrecio = null;
				PreparedStatement psCosto = null;
				
				this.listaPrendas.add(nuevaPrenda);
				
				try {
					conexion = DriverManager.getConnection(url);
					
					/* Obtengo los IDs correspondientes */
					String sqlMarca = "SELECT marcaId FROM marcas WHERE marca = ?";
					PreparedStatement psMarca = conexion.prepareStatement(sqlMarca);
					psMarca.setString(1, marca);
					ResultSet rsMarca = psMarca.executeQuery();
					int marcaId = rsMarca.next() ? rsMarca.getInt("marcaId") : 0;
					/* */
					String sqlTipo = "SELECT tipoId FROM tipos WHERE tipo = ?";
					PreparedStatement psTipo = conexion.prepareStatement(sqlTipo);
					psTipo.setString(1, tipo);
					ResultSet rsTipo = psTipo.executeQuery();
					int tipoId = rsTipo.next() ? rsTipo.getInt("tipoId") : 0;
					/* */
					String sqlColor = "SELECT colorId FROM colores WHERE color = ?";
					PreparedStatement psColor = conexion.prepareStatement(sqlColor);
					psColor.setString(1, color);
					ResultSet rsColor = psColor.executeQuery();
					int colorId = rsColor.next() ? rsColor.getInt("colorId") : 0;
					/* */
					String sqlTalle = "SELECT talleId FROM talles WHERE talle = ?";
					PreparedStatement psTalle = conexion.prepareStatement(sqlTalle);
					psTalle.setString(1, talle);
					ResultSet rsTalle = psTalle.executeQuery();
					int talleId = rsTalle.next() ? rsTalle.getInt("talleId") : 0;
					/* */
					String sqlCategoria = "SELECT categoriaId FROM categorias WHERE categoria = ?";
					PreparedStatement psCategoria = conexion.prepareStatement(sqlCategoria);
					psCategoria.setString(1, categoria);
					ResultSet rsCategoria = psCategoria.executeQuery();
					int categoriaId = rsCategoria.next() ? rsCategoria.getInt("categoriaId") : 0;
					
					/* Cargo la ropa */
					String sqlRopa = "INSERT INTO ropa "
						+ "(marcaId, tipoId, colorId, descripcion, talleId, stock,"
						+ " genero, activo, categoriaId)"
						+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
					psRopa = conexion.prepareStatement(sqlRopa);
					psRopa.setInt(1, marcaId);
					psRopa.setInt(2, tipoId);
					psRopa.setInt(3, colorId);
					psRopa.setString(4, descripcion);
					psRopa.setInt(5, talleId);
					psRopa.setInt(6, stock);
					psRopa.setString(7, genero);
					psRopa.setInt(8, 1);
					psRopa.setInt(9, categoriaId);
					psRopa.executeUpdate();
				
					ResultSet rs = psRopa.getGeneratedKeys();
					if (!rs.next()) {
						throw new SQLException("Error: No se pudo obtener el ID generado para el registro en ropa.");
					}
				
					if (nuevaPrenda.getIdRopa() == rs.getInt(1)) {
					
						/* Cargo el costo */
						String sqlCosto = "INSERT INTO costos"
							+ "(ropaId, costo, desde)"
							+ "VALUES (?, ?, ?)";
						psCosto = conexion.prepareStatement(sqlCosto);
						psCosto.setInt(1, nuevaPrenda.getIdRopa());
						psCosto.setDouble(2, nuevaPrenda.getUltimoCosto());
						psCosto.setDate(3, new java.sql.Date(nuevaPrenda.getListaCosto().get(0)
														.getFechaDesde().getTimeInMillis()));
						/* Cargo el precio */
						String sqlPrecio = "INSERT INTO precios"
							+ "(ropaId, precio, desde)"
							+ "VALUES (?, ?, ?)";
						psPrecio = conexion.prepareStatement(sqlPrecio);
						psPrecio.setInt(1, nuevaPrenda.getIdRopa());
						psPrecio.setDouble(2, nuevaPrenda.getUltimoPrecio());
						psPrecio.setDate(3, new java.sql.Date(nuevaPrenda.getListaPrecio().get(0)
														.getFechaDesde().getTimeInMillis()));
					
						psCosto.executeUpdate();
						psPrecio.executeUpdate();
					
						msg = "Prenda cargada con exito";
					} else {
						msg = "La prenda no fue cargada, error en id";
					}
				} catch (SQLException e) {
					msg = "Error en la base de datos";
					e.printStackTrace();
				} finally {
					try {
						if (psRopa != null) psRopa.close();
						if (psPrecio != null) psPrecio.close();
						if (psCosto != null) psCosto.close();
		                if (conexion != null) conexion.close();
		            } catch (SQLException e) {
		            	msg = "Error en la base de datos";
		            }
				}
			} else {
				msg = "La prenda ya existe";
			}
		} catch (CampoVacio e) {
			msg = "Se deben seleccionar los campos obligatorios";
		} catch (NumeroInvalido e) {
			msg = "Ingrese un numero valido en los campos";
		}
		
		return msg;
	}

	private boolean verificarExistencia(Prenda nuevaPrenda) {
		return this.listaPrendas.stream().noneMatch(p -> 
			p.getMarca().equals(nuevaPrenda.getMarca()) &&
			p.getCategoria().equals(nuevaPrenda.getCategoria()) &&
			p.getDescripcion().equals(nuevaPrenda.getDescripcion()) &&
			p.getTipo().equals(nuevaPrenda.getTipo()) &&
			p.getTalle().equals(nuevaPrenda.getTalle()) &&
			p.getColor().equals(nuevaPrenda.getColor())
			);
	}
	
	public ArrayList<Prenda> getPrendasActivas(){
		actualizar();
		return listaPrendas.stream()
				.filter(p -> p.getActivo() == 1)
				.sorted(Comparator.comparing(Prenda::getMarca)
						.thenComparing(Prenda::getCategoria)
						.thenComparing(Prenda::getTipo)
						.thenComparing(Prenda::getColor)
 			            .thenComparing(Prenda::getTalle))
		.collect(Collectors.toCollection(ArrayList<Prenda>::new));
	}
	
	public ArrayList<Prenda> getPrendasInactiva(){
		actualizar();
		return listaPrendas.stream()
				.filter(p -> p.getActivo() == 0)
				.sorted(Comparator.comparing(Prenda::getMarca)
						.thenComparing(Prenda::getCategoria)
						.thenComparing(Prenda::getTipo)
						.thenComparing(Prenda::getColor)
 			            .thenComparing(Prenda::getTalle))
		.collect(Collectors.toCollection(ArrayList<Prenda>::new));
	}
	
	public ArrayList<Prenda> getPrendas() {
		actualizar();
		return this.listaPrendas;
	}
	
	public String setEstado(Prenda prenda, int estado) {
		String msg = null;
		PreparedStatement psEstado = null;
		
		try {
			conexion = DriverManager.getConnection(url);
			
			String sqlEstado = "UPDATE ropa SET activo = ? WHERE ropaId = ?";
			psEstado = conexion.prepareStatement(sqlEstado);
			psEstado.setInt(1, estado);
			psEstado.setInt(2, prenda.getIdRopa());
			psEstado.executeUpdate();
			
			prenda.setActivo(estado);
			if (estado == 0) {
				msg = "Pantalon desactivado con exito";
			} else {
				msg = "Pantalon activado con exito";
			}
		} catch (SQLException e) {
			msg = "Error en la conexion con la base de dato";
		}
		
		actualizar();
		return msg;
	}
	
	public void modificarStock(Prenda prenda, int newStock) {
		if (!(prenda.getStock() == newStock)) {
			PreparedStatement psActualizar = null;
			try {
				conexion = DriverManager.getConnection(url);
				prenda.setStock(newStock);
				String sqlStock = "UPDATE ropa SET stock = ? WHERE ropaId = ?";
				psActualizar = conexion.prepareStatement(sqlStock);
				psActualizar.setInt(1, newStock);
				psActualizar.setInt(2, prenda.getIdRopa());
				psActualizar.executeUpdate();
			} catch (SQLException e) {
				System.out.println("Error en la base de datos");
    		} finally {
    			try {
    				if (psActualizar != null) psActualizar.close();
				} catch (SQLException e) {
					System.out.println("Error en la base de datos");
				}		
    		}
		}
	}
	
	public String modificarPrenda(double precio, double costo, String talle, 
			int stock, String descripcion, Prenda prenda) {
		
		Predicate<Prenda> verificar = p -> 
		p.getUltimoPrecio() == precio &&
		p.getUltimoCosto() == costo &&
		p.getTalle().equals(talle) &&
		p.getStock() == stock &&
		p.getDescripcion().equals(descripcion);
		
		String msg = null;
		
		if (verificar.test(prenda)) {
			
			msg = "No se modifico ningun dato";
		} else {
			
			PreparedStatement psActualizar = null;
			try {
				conexion = DriverManager.getConnection(url);
				
				prenda.validarDatosNumericos(precio, costo, stock, talle);
				prenda.setStock(stock);
				prenda.setDescripcion(descripcion);
				prenda.setTalle(talle);
				
				String sqlTalle = "SELECT talleId FROM talles WHERE talle = ?";
				PreparedStatement psTalle = conexion.prepareStatement(sqlTalle);
				psTalle.setString(1, talle);
				ResultSet rsTalle = psTalle.executeQuery();
				int talleId = rsTalle.next() ? rsTalle.getInt("talleId") : 0;
				
				/* Actualizo la BD */
				String sqlActualizarDatos = "UPDATE ropa SET "
						+ "descripcion = ?, "
						+ "talleId = ?, "
						+ "stock = ? "
						+ "WHERE ropaId = ?";
				psActualizar = conexion.prepareStatement(sqlActualizarDatos);
				psActualizar.setString(1, prenda.getDescripcion());
				psActualizar.setInt(2, talleId);
				psActualizar.setInt(3, prenda.getStock());
				psActualizar.setInt(4, prenda.getIdRopa());
				psActualizar.executeUpdate();
				
				/* Actualizo costo */
				if (prenda.getUltimoCosto() != costo) {
					prenda.actualizarCosto(costo);
					int catidadCostos;
					
					if (prenda.getListaCosto().size() > 1) {
						catidadCostos = prenda.getListaCosto().size() - 2;
						psActualizar = null;
						
						String sqlCerrarCosto = "UPDATE costos SET "
								+ "hasta = ? "
								+ "WHERE ropaId = ? "
								+ "AND hasta IS NULL";
						psActualizar = conexion.prepareStatement(sqlCerrarCosto);
						psActualizar.setTimestamp(1, new Timestamp(prenda.getListaCosto()
								.get(catidadCostos).getFechaHasta().getTimeInMillis()));
						psActualizar.setInt(2, prenda.getIdRopa());
						psActualizar.executeUpdate();
					}	
			
					catidadCostos = prenda.getListaCosto().size() - 1;
					psActualizar = null;
					
					String sqlNuevoCosto = "INSERT INTO costos"
							+ "(ropaId, costo, desde) "
							+ "VALUES (?, ?, ?)";
					psActualizar = conexion.prepareStatement(sqlNuevoCosto);
					psActualizar.setInt(1, prenda.getIdRopa());
					psActualizar.setDouble(2, prenda.getUltimoCosto());
					psActualizar.setTimestamp(3, new Timestamp(prenda.getListaCosto()
								.get(catidadCostos).getFechaDesde().getTimeInMillis()));
					psActualizar.executeUpdate();
				}
				
				/* Actualizo precio */
				if (prenda.getUltimoPrecio() != precio) {
					prenda.actualizarPrecio(precio);
					int catidadPrecio;
					
					if (prenda.getListaPrecio().size() > 1) {
						catidadPrecio = prenda.getListaPrecio().size() - 2;
						psActualizar = null;
						
						String sqlCerrarPrecio = "UPDATE precios SET "
								+ "hasta = ? "
								+ "WHERE ropaId = ? "
								+ "AND hasta IS NULL";
						psActualizar = conexion.prepareStatement(sqlCerrarPrecio);
						psActualizar.setTimestamp(1, new Timestamp(prenda.getListaPrecio()
								.get(catidadPrecio).getFechaHasta().getTimeInMillis()));
						psActualizar.setInt(2, prenda.getIdRopa());
						psActualizar.executeUpdate();
					}	
					catidadPrecio = prenda.getListaPrecio().size() - 1;
					psActualizar = null;
					
					String sqlNuevoPrecio = "INSERT INTO precios "
							+ "(ropaId, precio, desde) "
							+ "VALUES (?, ?, ?)";
					psActualizar = conexion.prepareStatement(sqlNuevoPrecio);
					psActualizar.setInt(1, prenda.getIdRopa());
					psActualizar.setDouble(2, prenda.getUltimoPrecio());
					psActualizar.setTimestamp(3, new Timestamp(prenda.getListaPrecio()
							.get(catidadPrecio).getFechaDesde().getTimeInMillis()));
					psActualizar.executeUpdate();
				}
				
				msg = "Modificado Correctamente";
			} catch (NumeroInvalido e) {
				msg = e.getMessage();
			} catch (SQLException e) {
				msg = "Error en la conexion con la base de dato. ";
				msg += e.getMessage();
			} finally {
				try {
					if (psActualizar != null) psActualizar.close();
				} catch (SQLException e) {
					msg = "Error en la conexion con la base de dato. ";
					msg += e.getMessage();
				}
			}
		}
		return msg;
	}
	
	/* Caracteristicas */
	private void obtenerCaracteristicas() {
		Statement stmt = null;
		caracteristicas = new ArrayList<ArrayList<String>>();
		
		try {
			conexion = DriverManager.getConnection(url);
			stmt = conexion.createStatement();
			
			ArrayList<String> tipos = new ArrayList<String>();
			ArrayList<String> marcas = new ArrayList<String>();
			ArrayList<String> colores = new ArrayList<String>();
			ArrayList<String> talles = new ArrayList<String>();
			ArrayList<String> categorias = new ArrayList<String>();
			
			/* SQL */
			String sqlTipos = "SELECT * FROM tipos";
			String sqlMarcas = "SELECT * FROM marcas";
			String sqlColores = "SELECT * FROM colores";
			String sqlTalles = "SELECT * FROM talles";
			String sqlCategorias = "SELECT * FROM categorias";
			
			ResultSet rsMarcas = stmt.executeQuery(sqlMarcas);
			while (rsMarcas.next()) {
				marcas.add(rsMarcas.getString("marca"));
			}
			stmt.close();
			/*				*/
			stmt = conexion.createStatement();
			ResultSet rsTipos = stmt.executeQuery(sqlTipos);
			while (rsTipos.next()) {
				tipos.add(rsTipos.getString("tipo"));
			}
			stmt.close();
			/*				*/
			stmt = conexion.createStatement();
			ResultSet rsTalles = stmt.executeQuery(sqlTalles);
			while (rsTalles.next()) {
				talles.add(rsTalles.getString("talle"));
			}
			Collections.sort(talles, new Comparator<String>() {
				@Override
				public int compare(String n1, String n2) {
					return n1.compareTo(n2);
				}
			});
			stmt.close();
			/*				*/
			stmt = conexion.createStatement();
			ResultSet rsColores = stmt.executeQuery(sqlColores);
			while (rsColores.next()) {
				colores.add(rsColores.getString("color"));
			}
			stmt.close();
			/*				*/
			stmt = conexion.createStatement();
			ResultSet rsCategorias = stmt.executeQuery(sqlCategorias);
			while (rsCategorias.next()) {
				categorias.add(rsCategorias.getString("categoria"));
			}
			/*				*/
			caracteristicas.add(marcas);
			caracteristicas.add(tipos);
			caracteristicas.add(talles);
			caracteristicas.add(colores);
			caracteristicas.add(categorias);
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, 
			        "Error en la conexión con la BD:\n" + e.getMessage()+
			        e.getErrorCode(), 
			        "Error", 
			        JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		} finally {
			try {
				if (stmt != null) stmt.close();
				if (conexion != null) conexion.close();
			} catch (SQLException e) {
				JOptionPane.showMessageDialog(null, 
				        "Error en la conexión con la BD:\n" + e.getMessage()+
				        e.getErrorCode(), 
				        "Error", 
				        JOptionPane.ERROR_MESSAGE);
			} 
		}
	}

	public String nuevaMarca(String marca) {
		String msg = "La marca ya existe";
		PreparedStatement psMarca = null;
		if (caracteristicas.get(0).stream().noneMatch(p-> p.equals(marca))) {
			try {
				verificarTexto(marca);
				conexion = DriverManager.getConnection(url);
				String sqlMarcas = "INSERT INTO marcas (marca) VALUES (?)";
				psMarca = conexion.prepareStatement(sqlMarcas);
				psMarca.setString(1, marca);
				psMarca.executeUpdate();
				msg = "Marca cargada con exito";
			} catch (TextoInvalido e) {
				msg = e.getMessage();
			} catch (SQLException e) {
				msg = "Error en la base de dato";
			} finally {
				try {
					if (psMarca != null) psMarca.close();
	                if (conexion != null) conexion.close();
 				} catch (SQLException e) {
					msg = "Error en la base de dato";
				}
			}
		}
		return msg;
	}

	public String nuevoTipo(String tipo) {
		String msg = "El tipo ya existe";
		PreparedStatement psTipo = null;
		if (caracteristicas.get(1).stream().noneMatch(p-> p.equals(tipo))) {
			try {
				verificarTexto(tipo);
				conexion = DriverManager.getConnection(url);
				String sqlTipos = "INSERT INTO tipos (tipo) VALUES (?)";
				psTipo = conexion.prepareStatement(sqlTipos);
				psTipo.setString(1, tipo);
				psTipo.executeUpdate();
				msg = "Tipo cargada con exito";
			} catch (TextoInvalido e) {
				msg = e.getMessage();
			} catch (SQLException e) {
				msg = "Error en la base de dato";
			} finally {
				try {
					if (psTipo != null) psTipo.close();
	                if (conexion != null) conexion.close();
 				} catch (SQLException e) {
					msg = "Error en la base de dato";
				}
			}
		}
		return msg;
	}

	public String nuevoColor(String color) {
		String msg = "El color ya existe";
		PreparedStatement psColor = null;
		if (caracteristicas.get(3).stream().noneMatch(p-> p.equals(color))) {
			try {
				verificarTexto(color);
				conexion = DriverManager.getConnection(url);
				String sqlColores = "INSERT INTO colores (color) VALUES (?)";
				psColor = conexion.prepareStatement(sqlColores);
				psColor.setString(1, color);
				psColor.executeUpdate();
				msg = "Color cargado con exito";
			} catch (TextoInvalido e) {
				msg = e.getMessage();
			} catch (SQLException e) {
				msg = "Error en la base de dato";
			} finally {
				try {
					if (psColor != null) psColor.close();
	                if (conexion != null) conexion.close();
 				} catch (SQLException e) {
					msg = "Error en la base de dato";
				}
			}
		}
		return msg;
	}
	
	
	/**
	 * Cuando se agrege nueva funcionalidad para caracteristicas se puede cargar
	 * tanto numericos como alfaveticos, por el momento solo numericos
	 * */
	public String nuevoTalle(String talle) {
		String msg = "El talle ya existe";
		int nroTalle;
		PreparedStatement psTalle = null;
		try {
			nroTalle = Integer.parseInt(talle);
			if (nroTalle < 0 || nroTalle > 100) {
				throw new NumeroInvalido("Ingrese un numero valido mayor a 0 y menor a 100");
			}
			if(caracteristicas.get(2).stream().noneMatch(p-> p.equals(talle))) {
				conexion = DriverManager.getConnection(url);
				String sqlTalles = "INSERT INTO talles (talle, tren) VALUES (?, ?)";
				psTalle = conexion.prepareStatement(sqlTalles);
				psTalle.setInt(1, nroTalle);
				psTalle.setInt(1, 0);
				psTalle.executeUpdate();
				msg = "Talle cargado con exito";
			}
		} catch (NumberFormatException e) {
			msg = "Ingrese un numero";
		} catch (NumeroInvalido e) {
			msg = e.getMessage();
		} catch (SQLException e) {
			msg = "Error en la base de dato";
		} finally {
			try {
				if (psTalle != null) psTalle.close();
                if (conexion != null) conexion.close();
				} catch (SQLException e) {
					msg = "Error en la base de dato";
			}
		}
		return msg;
	}
	
	public String nuevaCategoria(String categoria) {
		String msg = "La categoria ya existe";
		PreparedStatement psCategoria = null;
		if (caracteristicas.get(4).stream().noneMatch(p-> p.equals(categoria))) {
			try {
				verificarTexto(categoria);
				conexion = DriverManager.getConnection(url);
				String sqlCategoria = "INSERT INTO categorias (categoria) VALUES (?)";
				psCategoria = conexion.prepareStatement(sqlCategoria);
				psCategoria.setString(1, categoria);
				psCategoria.executeUpdate();
				msg = "Categoria cargada con exito";
			} catch (TextoInvalido e) {
				msg = e.getMessage();
			} catch (SQLException e) {
				msg = "Error en la base de dato";
			} finally {
				try {
					if (psCategoria != null) psCategoria.close();
	                if (conexion != null) conexion.close();
 				} catch (SQLException e) {
					msg = "Error en la base de dato";
				}
			}
		}
		return msg;
	}
	
	public ArrayList<ArrayList<String>> getCaracteristicas(){
		actualizar();
		return this.caracteristicas;
	}
	
	private void verificarTexto(String txt) throws TextoInvalido {
		if (!txt.matches("[A-ZÁÉÍÓÚÜÑ_& -]+") || txt.equals("")) {
			throw new TextoInvalido("El texto no debe contener caracteres especiales no permitidos o numeros");
		}
	}
	
}
