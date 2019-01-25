package dominio;

import java.util.Date;

import dominio.excepcion.GarantiaExtendidaException;
import dominio.repositorio.RepositorioGarantiaExtendida;
import dominio.repositorio.RepositorioProducto;
import utilidades.CalendarUtils;
import utilidades.StringUtils;

public class Vendedor {

	public static final String EL_PRODUCTO_TIENE_GARANTIA = "El producto ya cuenta con una garantia extendida";
	public static final String EL_PRODUCTO_NO_TIENE_GARANTIA_EXTENDIDA = "Este producto no cuenta con garantía extendida";
	public static final double PRECIO_LIMITE_GARANTIA = 500_000;
	public static final int DIAS_VIGENCIA_GARANTIA_MAYOR = 200;
	public static final int DIAS_VIGENCIA_GARANTIA_MENOR = 100;
	public static final int PORCENTAJE_VALOR_GARANTIA_MAYOR = 20;
	public static final int PORCENTAJE_VALOR_GARANTIA_MENOR = 10;

	private RepositorioProducto repositorioProducto;
	private RepositorioGarantiaExtendida repositorioGarantia;

	public Vendedor() {
	}

	public Vendedor(RepositorioProducto repositorioProducto, RepositorioGarantiaExtendida repositorioGarantia) {
		this.repositorioProducto = repositorioProducto;
		this.repositorioGarantia = repositorioGarantia;
	}

	public void generarGarantia(String codigo, String nombreCliente) {
		if (!codigoConGarantiaExtendida(codigo)) {
			throw new GarantiaExtendidaException(EL_PRODUCTO_NO_TIENE_GARANTIA_EXTENDIDA);
		}

		Producto productoComprobar = repositorioGarantia.obtenerProductoConGarantiaPorCodigo(codigo);
		if (productoComprobar != null) {
			throw new GarantiaExtendidaException(EL_PRODUCTO_TIENE_GARANTIA);
		}

		Producto producto = repositorioProducto.obtenerPorCodigo(codigo);

		Date fechaSolicitud = new Date();
		Date fechaVencimiento = calcularFechaVencimiento(fechaSolicitud, producto.getPrecio());
		double precioGarantia = calcularValorGarantia(producto.getPrecio());

		GarantiaExtendida garantiaExtendida = new GarantiaExtendida(producto, fechaSolicitud, fechaVencimiento,
				precioGarantia, nombreCliente);
		repositorioGarantia.agregar(garantiaExtendida);
	}

	/**
	 * Permite calcular la fecha de vencimiento de la garantia extendida
	 * 
	 * @param fechaInicio
	 *            fecha de inicio o solicitud de la garantía extendida
	 * @param precio
	 *            valor del producto
	 * @return
	 */
	public Date calcularFechaVencimiento(Date fechaInicio, double precio) {
		Date fechaVencimiento = null;
		if (precio > PRECIO_LIMITE_GARANTIA) {
			fechaVencimiento = CalendarUtils.calcularFechaDiasHabiles(fechaInicio, DIAS_VIGENCIA_GARANTIA_MAYOR);
		} else {
			fechaVencimiento = CalendarUtils.calcularFechaDiasCalendario(fechaInicio, DIAS_VIGENCIA_GARANTIA_MENOR);
		}
		return fechaVencimiento;
	}

	/**
	 * Permite calcular el valor de la garantia extendida
	 * 
	 * @param precio
	 *            valor del producto
	 * @return
	 */
	public double calcularValorGarantia(double precio) {
		double precioGarantia = 0;
		if (precio > PRECIO_LIMITE_GARANTIA) {
			precioGarantia = (precio * PORCENTAJE_VALOR_GARANTIA_MAYOR) / 100;
		} else {
			precioGarantia = (precio * PORCENTAJE_VALOR_GARANTIA_MENOR) / 100;
		}
		return precioGarantia;
	}

	/**
	 * Permite verificar si el c&oacute;digo cumple con las caracteristicas de
	 * garant&iacute;a extendida
	 * 
	 * @param codigo
	 *            cadena que contiene el c&oacute;digo de un producto
	 * @return <tt>true<tt> si al c&oacute;digo se le puede aplicar garantia
	 */
	public boolean codigoConGarantiaExtendida(String codigo) {
		if (codigo == null) {
			return Boolean.FALSE;
		}
		return StringUtils.contarVocales(codigo) != 3;
	}

	/**
	 * Permite verificar si el producto tiene garant&iacute;a
	 * 
	 * @param codigo
	 *            cadena que contiene el c&oacute;digo de un producto
	 * @return <tt>true<tt> si el producto tiene garantia asignada
	 */
	public boolean tieneGarantia(String codigo) {
		Producto producto = repositorioGarantia.obtenerProductoConGarantiaPorCodigo(codigo);
		return producto != null;
	}

}
