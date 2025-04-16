package com.gestorCN.logic;

import com.gestorCN.logic.sales.GestorVentas;
import com.gestorCN.logic.stock.GestorPrendas;
import com.gestorCN.ui.sales.PanelGestorVentas;

public class Main {

	public static void main(String[] args) {
		GestorVentas gestorVentas = GestorVentas.getInstance();
		GestorPrendas gestorPrendas = GestorPrendas.getInstance();
		
		PanelGestorVentas panelGestorVentas = 
				new PanelGestorVentas(gestorVentas, gestorPrendas);
		
		panelGestorVentas.setVisible(true);
	}

}
