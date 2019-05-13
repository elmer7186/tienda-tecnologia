# tienda-tecnologia 

Resultados de la prueba técnica tienda de tecnología para Ceiba Software

# Problema de negocio

Consiste en un sistema que simula el comportamiento del vigilante de un parqueadero, las reglas de negocio son las siguientes:

* El parqueadero recibe carros y motos 
* El parqueadero solo puede tener 20 carros y 10 motos simultaneamente
* Las placas que inician por la letra "A" solo pueden ingresar al parqueadero los días Domingo y Lunes, de lo contrario debe mostrar un mensaje de que no esta autorizado a ingresar.
* La tabla de precios es la siguiente:<br>
Valor hora carro = 1000<br>
Valor hora moto = 500<br>
valor día carro = 8000<br>
valor día moto = 4000<br>
* Las motos que tengan un cilindraje mayor a 500 CC paga 2000 de mas al valor total.
Cuando sale un carro del parqueadero se cobra por horas si permaneció menos de 9 horas en el parqueadero, de lo contrario se cobra por días.
* El valor del día comienza entre las 9 horas de parqueo y finaliza pasadas 24 horas de parqueo.
* El parqueadero funciona 24 horas, los 7 días de la semana.<br>
#EJEMPLOS: 
* Si el carro permaneció un día y 3 horas se debe cobrar 11.000
* Si la moto permaneció un 10 horas y es de 650CC se cobra 6.000
