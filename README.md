**1.1.0** - Se agregaron y modificaron funciones como:  
  **1.** Existencia de talles numericos y alfaveticos que dependiendo del tipo de prenda se muestra uno u otro.  
&nbsp;&nbsp;&nbsp;*1.1.* Modifada la base de datos (tabla talles, el talle es tipo texto y se agrego un campo para saber a que pertenece).  
&nbsp;&nbsp;&nbsp;*1.2.* Modificacion en el buscador en el panelGestorVentas para buscar talles por String.
  **2.** Solo se muestran las cuotas cuando se selecciona Tarjeta.  
  **3.** Modificacion en las ventas y sus reportes:  
&nbsp;&nbsp;&nbsp;*3.1.* Se agrega un estado a las facturas para saber su estado (1: activa, 0: cancelada).  
&nbsp;&nbsp;&nbsp;&nbsp;Esto lleva a una modificacion en la base de datos, agregando un campo para poder ver si fue cancelada  
&nbsp;&nbsp;&nbsp;*3.2.* Se agrega la funcion de mofidicar el estado de la factra seleccionada.  
&nbsp;&nbsp;&nbsp;*3.3.* Ahora se muestran todas las facturas cuando no se selecciona un periodo, ordenadas de la mas reciente a la mas antigua (hasta la antigudad de dos meses, antes de eso se debe seleccionar un periodo).  
  **4.** Modificacion en el panelGestorStock:  
&nbsp;&nbsp;&nbsp;*4.1.* El boton de agustar precio ahora pide ingresar un nuevo monto y no un porcentaje.  
&nbsp;&nbsp;&nbsp;*4.2.* Se agrega una nueva funcionalidad de "ajustar costos" con la misma funcionalidad que "ajustar precios" dirigada a la modificacion de los costos.    
**1.0.3** - Errores areglados.  
**1.0.2** - Desplegada con errores.  
