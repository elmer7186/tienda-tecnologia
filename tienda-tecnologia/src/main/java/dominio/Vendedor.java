package dominio;

import dominio.repositorio.RepositorioProducto;
import dominio.excepcion.GarantiaExtendidaException;
import dominio.repositorio.RepositorioGarantiaExtendida;

public class Vendedor {

	public static final String EL_PRODUCTO_TIENE_GARANTIA = "El producto ya cuenta con una garantia extendida";

	private RepositorioProducto repositorioProducto;
	private RepositorioGarantiaExtendida repositorioGarantia;

	public Vendedor(RepositorioProducto repositorioProducto, RepositorioGarantiaExtendida repositorioGarantia) {
		this.repositorioProducto = repositorioProducto;
		this.repositorioGarantia = repositorioGarantia;

	}

	public void generarGarantia(String codigo) {

		Producto producto1 = repositorioGarantia.obtenerProductoConGarantiaPorCodigo(codigo);
		if (producto1 != null) {
			throw new GarantiaExtendidaException(EL_PRODUCTO_TIENE_GARANTIA);
		}

		// TODO agregar datos de vencimiento de la garantia y valor

		Producto producto = repositorioProducto.obtenerPorCodigo(codigo);
		GarantiaExtendida garantiaExtendida = new GarantiaExtendida(producto);
		repositorioGarantia.agregar(garantiaExtendida);

		// throw new UnsupportedOperationException("Método pendiente por
		// implementar");

	}

	public boolean tieneGarantia(String codigo) {
		Producto producto = repositorioGarantia.obtenerProductoConGarantiaPorCodigo(codigo);
		return producto != null;
	}

}
