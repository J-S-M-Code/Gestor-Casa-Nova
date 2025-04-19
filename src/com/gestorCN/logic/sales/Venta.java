package com.gestorCN.logic.sales;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import com.gestorCN.logic.stock.Prenda;

public class Venta {
	private int nroVenta;
	private Calendar fecha;
	private double monto;
	private double costo; 
	private int cuotas;
	private String medioPago;
	private int cancelada;
	private ArrayList<Integer> idPrendasVenta;
	
	/* Constructo BD */
	public Venta(int nroVenta, GregorianCalendar fecha, double monto, double costo, 
			String medioPago, int cuotas, ArrayList<Integer> idPrendasVenta, int cancelada) {
		this.nroVenta = nroVenta;
		this.fecha = fecha;
		this.monto = monto;
		this.costo = costo;
		this.medioPago = medioPago;
		this.cuotas = cuotas;
		this.idPrendasVenta = idPrendasVenta;
		this.cancelada = cancelada;
	}
	
	/* Contructor nuevo */ 
	public Venta(int nroVenta, Calendar fecha, ArrayList<Prenda> prendasVenta, 
				String medioPago, int cuotas) {
		this.nroVenta = nroVenta;
		this.fecha = fecha;
		this.medioPago = medioPago;
		this.cuotas = cuotas;
		this.cancelada = 0;
		idPrendasVenta = new ArrayList<Integer>();
		for (Prenda r : prendasVenta) {
			this.monto += r.getUltimoPrecio();
			this.costo += r.getUltimoCosto();
			this.idPrendasVenta.add(r.getIdRopa());
		}
		if (medioPago.equals("TARJETA")) {
			this.monto = Math.round(this.monto * 1.10 * 10) / 10.0;
		}
	}
	
	public double getMonto() {
		return this.monto;
	}

	public double getCosto() {
		return this.costo;
	}

	public int getNroVenta() {
		return this.nroVenta;
	}
	
	public Calendar getFecha() {
		return fecha;
	}

	public int getCuotas() {
		return cuotas;
	}

	public String getMedioPago() {
		return medioPago;
	}

	public int getStatus() {
		return this.cancelada;
	}
	
 	public ArrayList<Integer> getIdPrendasVenta() {
		return idPrendasVenta;
	}

	@Override
	public String toString() {
		SimpleDateFormat sfd = new SimpleDateFormat("dd/MM/yyyy");
		return nroVenta  + ", " + sfd.format(fecha.getTime()) + ", " +
				monto + ", " + costo + ", " + medioPago + ", " +
				cuotas;
	}

	
	public String getFechaString() {
		SimpleDateFormat sfd = new SimpleDateFormat("dd/MM/yyyy");
		return sfd.format(fecha.getTime());
	}

	public void setStatus(int cancelada) {
		this.cancelada = cancelada;
	}
}
