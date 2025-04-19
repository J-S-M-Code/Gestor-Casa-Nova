package com.gestorCN.logic.stock;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

import com.gestorCN.logic.exceptions.CampoVacio;
import com.gestorCN.logic.exceptions.NumeroInvalido;

public class Prenda {
	private int idRopa;
	private String tipo;
	private String color;
	private String talle;
	private String descripcion;
	private ArrayList<Precio> listaPrecio;
	private ArrayList<Costo> listaCosto;
	private String marca;
	private int stock;
	private int activo;
	private String genero;
	private String categoria;
	
	/* New */
	public Prenda(int id, String tipo, String color, String talle, 
			String descripcion, double precio, double costo, String marca, 
			int cantidad, String genero, String categoria) throws CampoVacio, NumeroInvalido {
		validarDatosString( tipo, color, marca, genero);
		validarDatosNumericos(precio, costo, cantidad, talle);
		
		this.idRopa = id;
		this.tipo = tipo;
		this.color = color;
		this.talle = talle;
		this.descripcion = descripcion;
		this.marca = marca;
		this.stock = cantidad;
		this.activo = 1;
		this.genero = genero;
		this.categoria = categoria;
		
		this.listaPrecio = new ArrayList<Precio>();
		this.listaPrecio.add(new Precio(precio));
		this.listaCosto = new ArrayList<Costo>();
		listaCosto.add(new Costo(costo));
	}

	/* BD */
	public Prenda(int idPrenda, String tipo, String color, String talle, 
			String descripcion, ArrayList<Precio> listaPrecio,
			ArrayList<Costo> listaCosto, String marca, int stock, 
			int activo, String genero, String categoria) {
		this.idRopa = idPrenda;
		this.tipo = tipo;
		this.color = color;
		this.talle = talle;
		this.descripcion = descripcion; 
		this.marca = marca;
		this.stock = stock;
		this.listaPrecio = listaPrecio;
		this.listaCosto = listaCosto;
		this.activo = activo;
		this.genero = genero;
		this.categoria = categoria;
	}
	
	public void validarDatosNumericos(double precio, double costo, int cantidad, String talle) 
			throws NumeroInvalido {
		try {
			if ((precio < 0) || (costo < 0) || (cantidad <= -1)) {
				throw new NumeroInvalido("Ingrese un numero valido en los campos");
			}
			int talleNum = Integer.parseInt(talle);
			if (talleNum < 1) {
				throw new NumeroInvalido("Ingrese un numero valido en los campos");
			}
		} catch (NumberFormatException e) {
			String[] talles = {"ESPECIAL", "XS", "S", "M", "L", "XL", "XXL", "XXXL"};
			if (!Arrays.asList(talles).contains(talle)) {
				throw new NumeroInvalido("Ingrese un numero valido en los campos");
			}
		}
		
	}

	private void validarDatosString(String tipo, String color, String marca, String genero) 
			throws CampoVacio {
		String txtDefault = "Seleccione una opcion";
		if ((tipo.equals(txtDefault) || color.equals(txtDefault) || marca.equals(txtDefault)) 
				|| genero == null) {
			throw new CampoVacio("Se deben seleccionar los campos obligatorios (*)");
		}
	}
	
	public boolean actualizarStock(int cantidad) {
		if((this.stock + cantidad) >= 0) {
			this.stock = this.stock + cantidad;
			return true;
		}
		return false;
	}

	public int getIdRopa() {
		return idRopa;
	}

	public String getTipo() {
		return tipo;
	}

	public String getColor() {
		return color;
	}

	public String getTalle() {
		return talle;
	}
	
	public void setTalle(String talle) {
		this.talle = talle;
	}

	public String getDescripcion() {
		return descripcion;
	}
	
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getMarca() {
		return marca;
	}

	public int getStock() {
		return stock;
	}
	
	public void setStock(int stock) {
		this.stock = stock;
	}

	public int getActivo() {
		return activo;
	}
	
	public void setActivo(int estado) {
		this.activo = estado;
	}

	public String getGenero() {
		return genero;
	}
	
	public String getCategoria() {
		return this.categoria;
	}
	
	/* */
	public ArrayList<Precio> getListaPrecio() {
		return listaPrecio;
	}
	
	public double getUltimoPrecio() {
		if (listaPrecio.size() <= 0) {
			return 0;
		}
		return this.listaPrecio.get((listaPrecio.size()-1)).getValor();
	}
	
	public void actualizarPrecio(double precio) {
		if (listaPrecio.size() != 0) {
			listaPrecio.get(listaPrecio.size()-1).setFechaHasta();
		}
		listaPrecio.add(new Precio(precio));
	}
	
	public double getPrecioFecha(Calendar fecha) {
		return listaPrecio.stream()
		        .filter(p -> (fecha.compareTo(p.getFechaDesde()) >= 0) &&  
	                     (p.getFechaHasta() == null || fecha.compareTo(p.getFechaHasta()) < 0))
	        .map(Precio::getValor)
	        .findFirst()
	        .orElse(0.0);
	}
	
	/* */
	public ArrayList<Costo> getListaCosto() {
		return listaCosto;
	}
	
	public double getUltimoCosto() {
		if (listaCosto.size() <= 0) {
			return 0;
		}
		return this.listaCosto.get((listaCosto.size()-1)).getCosto();
	}

	public void actualizarCosto(double costo) {
		if (listaCosto.size() != 0) {
			listaCosto.get(listaCosto.size()-1).setFechaHasta();
		}
		listaCosto.add(new Costo(costo));
	}

	public double getCostoFecha(Calendar fecha) {
	    return listaCosto.stream()
	        .filter(p -> (fecha.compareTo(p.getFechaDesde()) >= 0) &&  
	                     (p.getFechaHasta() == null || fecha.compareTo(p.getFechaHasta()) < 0))
	        .map(Costo::getCosto)
	        .findFirst()
	        .orElse(0.0);
	}
	
	/* */
	@Override
	public String toString() {
		return idRopa + " | " +  marca + " | " + categoria + " | " + tipo + " | " + color + " | " + talle + " | "
				+ descripcion + " | " + stock ;
	}
	
}
