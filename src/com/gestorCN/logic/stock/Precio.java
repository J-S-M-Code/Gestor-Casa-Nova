package com.gestorCN.logic.stock;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class Precio {
	public double valor;
	public Calendar fechaDesde = null;
	public Calendar fechaHasta = null;
	
	/* Nuevo Precio */
	public Precio(double valor) {
		this.valor = redondearValor(valor);
		this.fechaDesde = GregorianCalendar.getInstance();
		fechaDesde.set(Calendar.HOUR_OF_DAY, 0);
		fechaDesde.set(Calendar.MINUTE, 0);
		fechaDesde.set(Calendar.SECOND, 0);
		fechaDesde.set(Calendar.MILLISECOND, 0);
		this.fechaHasta = null;
	}
	
	private double redondearValor(double valor) {
		double decenas = valor % 100;
		if (decenas > 50) {
			valor -= decenas;
			return valor += 100;
		}
		if (decenas < 50) {
			return valor -= decenas;
		}
		return valor;
	}
	
	/* Cargar de la bd */
	public Precio(double valor, Calendar fechaDesde, Calendar fechaHasta) {
		this.valor = valor;
		this.fechaDesde = fechaDesde;
		this.fechaHasta = fechaHasta;
	}
	
	/* Set y Get*/
	public double getValor() {
		return valor;
	}
	
	public void setFechaDesde(Calendar fechaDesde) {
		this.fechaDesde = fechaDesde;
		fechaDesde.set(Calendar.HOUR_OF_DAY, 0);
		fechaDesde.set(Calendar.MINUTE, 0);
		fechaDesde.set(Calendar.SECOND, 0);
		fechaDesde.set(Calendar.MILLISECOND, 0);
	}
	
	public void setFechaHasta() {
		this.fechaHasta = GregorianCalendar.getInstance();
		fechaHasta.set(Calendar.HOUR_OF_DAY, 0);
		fechaHasta.set(Calendar.MINUTE, 0);
		fechaHasta.set(Calendar.SECOND, 0);
		fechaHasta.set(Calendar.MILLISECOND, 0);
	}
	
	public String getFechaDesdeString() {
		if (this.fechaDesde == null) {
			return null;
		}
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy");
		return sdf.format(this.fechaDesde.getTime());
	}
	
	public Calendar getFechaDesde() {
		if (this.fechaDesde == null) {
			return null;
		}
		return fechaDesde;
	}
	
	public String getFechaHastaString() {
		if (this.fechaHasta == null) {
			return null;
		}
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy");
		return sdf.format(this.fechaHasta.getTime());
	}
	
	public Calendar getFechaHasta() {
		if (this.fechaHasta == null) {
			return null;
		}
		return fechaHasta;
	}

}
