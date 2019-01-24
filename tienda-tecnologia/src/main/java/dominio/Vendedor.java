package dominio;

import dominio.repositorio.RepositorioProducto;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import dominio.excepcion.GarantiaExtendidaException;
import dominio.repositorio.RepositorioGarantiaExtendida;

public class Vendedor {

	public static final String EL_PRODUCTO_TIENE_GARANTIA = "El producto ya cuenta con una garantia extendida";
	public static final String EL_PRODUCTO_NO_TIENE_GARANTIA_EXTENDIDA = "Este producto no cuenta con garant�a extendida";
	public static final double PRECIO_LIMITE_GARANTIA = 500_000;
	public static final int DIAS_VIGENCIA_GARANTIA_MAYOR = 200;
	public static final int DIAS_VIGENCIA_GARANTIA_MENOR = 100;
	public static final int PORCENTAJE_VALOR_GARANTIA_MAYOR = 20;
	public static final int PORCENTAJE_VALOR_GARANTIA_MENOR = 10;

	private RepositorioProducto repositorioProducto;
	private RepositorioGarantiaExtendida repositorioGarantia;

	public Vendedor(RepositorioProducto repositorioProducto, RepositorioGarantiaExtendida repositorioGarantia) {
		this.repositorioProducto = repositorioProducto;
		this.repositorioGarantia = repositorioGarantia;

	}

	public void generarGarantia(String codigo, String nombreCliente) {

		if (!codigoConGarantia(codigo)) {
			throw new GarantiaExtendidaException(EL_PRODUCTO_NO_TIENE_GARANTIA_EXTENDIDA);
		}

		Producto productoComprobar = repositorioGarantia.obtenerProductoConGarantiaPorCodigo(codigo);
		if (productoComprobar != null) {
			throw new GarantiaExtendidaException(EL_PRODUCTO_TIENE_GARANTIA);
		}

		Producto producto = repositorioProducto.obtenerPorCodigo(codigo);

		Date fechaVencimiento = null;
		double precioGarantia = 0;
		if (producto.getPrecio() > PRECIO_LIMITE_GARANTIA) {
			fechaVencimiento = calcularFechaFinGarantia(DIAS_VIGENCIA_GARANTIA_MAYOR);
			precioGarantia = getValorGarantia(producto.getPrecio());
		} else {
			fechaVencimiento = calcularFechaFinGarantia(DIAS_VIGENCIA_GARANTIA_MENOR);
			precioGarantia = getValorGarantia(producto.getPrecio());
		}

		GarantiaExtendida garantiaExtendida = new GarantiaExtendida(producto, new Date(), fechaVencimiento,
				precioGarantia, nombreCliente);
		repositorioGarantia.agregar(garantiaExtendida);
	}

	private Date calcularFechaFinGarantia(int dias) {
		LocalDate fechaIteracion = LocalDate.of(2018, 8, 16);
		Date fechaFinGarantia = null;

		while (dias > 0) {
			if (fechaIteracion.getDayOfWeek() != DayOfWeek.MONDAY) {
				dias--;
			}
			fechaIteracion = fechaIteracion.plusDays(1);
		}
		if (fechaIteracion.getDayOfWeek() == DayOfWeek.SUNDAY) {
			fechaIteracion = fechaIteracion.plusDays(2);
		} else if (fechaIteracion.getDayOfWeek() == DayOfWeek.MONDAY) {
			fechaIteracion = fechaIteracion.plusDays(1);
		}
		fechaFinGarantia = Date.from(fechaIteracion.atStartOfDay(ZoneId.systemDefault()).toInstant());
		return fechaFinGarantia;
	}

	private double getValorGarantia(double precio) {
		double valorGarantia = 0;
		if (precio > PRECIO_LIMITE_GARANTIA) {
			valorGarantia = (precio * PORCENTAJE_VALOR_GARANTIA_MAYOR) / 100;
		} else {
			valorGarantia = (precio * PORCENTAJE_VALOR_GARANTIA_MENOR) / 100;
		}
		return valorGarantia;
	}

	private boolean codigoConGarantia(String codigo) {
		if (codigo == null) {
			return Boolean.FALSE;
		}
		int numeroVocales = codigo.replaceAll("[^aeiouAEIOU����������]", "").length();
		return !(numeroVocales == 3);
	}

	public boolean tieneGarantia(String codigo) {
		Producto producto = repositorioGarantia.obtenerProductoConGarantiaPorCodigo(codigo);
		return producto != null;
	}

}
