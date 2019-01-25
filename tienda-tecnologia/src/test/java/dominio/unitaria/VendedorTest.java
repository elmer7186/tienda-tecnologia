package dominio.unitaria;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Test;

import dominio.Vendedor;
import dominio.Producto;
import dominio.repositorio.RepositorioProducto;
import dominio.repositorio.RepositorioGarantiaExtendida;
import testdatabuilder.ProductoTestDataBuilder;

public class VendedorTest {

	private static final String CODIGO_PRODUCTO_TRES_VOCALES = "Aghe2750FE";
	private static final String CODIGO_PRODUCTO_DIFERENTE_TRES_VOCALES = "AEIghe2750FE";
	private static final SimpleDateFormat FORMATO_FECHA = new SimpleDateFormat("dd/MM/yyyy");
	private static final String FECHA_SOLICITUD_GARANTIA_1 = "16/08/2018";
	private static final String FECHA_ESPERADA_VENCIMIENTO_1 = "06/04/2019";
	private static final String FECHA_SOLICITUD_GARANTIA_2 = "22/12/2018";
	private static final String FECHA_ESPERADA_VENCIMIENTO_2 = "01/04/2019";
	private static final double VALOR_PRODUCTO_MAYOR = 650_000;
	private static final double VALOR_GARANTIA_MAYOR_ESPERADO = 130_000;
	private static final double VALOR_PRODUCTO_MENOR = 318_000;
	private static final double VALOR_GARANTIA_MENOR_ESPERADO = 31_800;

	/**
	 * Mockea escenario en el cual si existe la garantia para el producto
	 * 
	 * <b>Resultado:</b> Encuentra que el producto si tiene garantia extendida
	 */
	@Test
	public void productoYaTieneGarantiaTest() {
		// arrange
		ProductoTestDataBuilder productoTestDataBuilder = new ProductoTestDataBuilder();

		Producto producto = productoTestDataBuilder.build();

		RepositorioGarantiaExtendida repositorioGarantia = mock(RepositorioGarantiaExtendida.class);
		RepositorioProducto repositorioProducto = mock(RepositorioProducto.class);

		when(repositorioGarantia.obtenerProductoConGarantiaPorCodigo(producto.getCodigo())).thenReturn(producto);

		Vendedor vendedor = new Vendedor(repositorioProducto, repositorioGarantia);

		// act
		boolean existeProducto = vendedor.tieneGarantia(producto.getCodigo());

		// assert
		assertTrue(existeProducto);
	}

	/**
	 * Mockea escenario en el cual no existe la garantia para el producto
	 * 
	 * <b>Resultado:</b> Encuentra que el producto no tiene garantia extendida
	 */
	@Test
	public void productoNoTieneGarantiaTest() {
		// arrange
		ProductoTestDataBuilder productoestDataBuilder = new ProductoTestDataBuilder();

		Producto producto = productoestDataBuilder.build();

		RepositorioGarantiaExtendida repositorioGarantia = mock(RepositorioGarantiaExtendida.class);
		RepositorioProducto repositorioProducto = mock(RepositorioProducto.class);

		when(repositorioGarantia.obtenerProductoConGarantiaPorCodigo(producto.getCodigo())).thenReturn(null);

		Vendedor vendedor = new Vendedor(repositorioProducto, repositorioGarantia);

		// act
		boolean existeProducto = vendedor.tieneGarantia(producto.getCodigo());

		// assert
		assertFalse(existeProducto);
	}

	/**
	 * Calcular fecha de vencimiento de garantia para producto con precio mayor
	 * a 500.000
	 * 
	 * <b>Resultado:</b> La fecha es calculada de acuerdo al valor esperado
	 */
	@Test
	public void calcularFechaGarantiaPrecioMayor() {
		// arrange
		Vendedor vendedor = new Vendedor();

		// act
		Date fechaSolicitud = null;
		try {
			fechaSolicitud = FORMATO_FECHA.parse(FECHA_SOLICITUD_GARANTIA_1);
		} catch (ParseException e) {
			fail();
		}
		Date fechaFin = vendedor.calcularFechaVencimiento(fechaSolicitud, VALOR_PRODUCTO_MAYOR);
		String fechaFinString = FORMATO_FECHA.format(fechaFin);

		// assert
		assertEquals(FECHA_ESPERADA_VENCIMIENTO_1, fechaFinString);
	}

	/**
	 * Calcular fecha de vencimiento de garantia para producto con precio igual
	 * o menor a 500.000
	 * 
	 * <b>Resultado:</b> La fecha es calculada de acuerdo al valor esperado
	 */
	@Test
	public void calcularFechaGarantiaPrecioMenor() {
		// arrange
		Vendedor vendedor = new Vendedor();

		// act
		Date fechaSolicitud = null;
		try {
			fechaSolicitud = FORMATO_FECHA.parse(FECHA_SOLICITUD_GARANTIA_2);
		} catch (ParseException e) {
			fail();
		}
		Date fechaFin = vendedor.calcularFechaVencimiento(fechaSolicitud, VALOR_PRODUCTO_MENOR);
		String fechaFinString = FORMATO_FECHA.format(fechaFin);

		// assert
		assertEquals(FECHA_ESPERADA_VENCIMIENTO_2, fechaFinString);
	}

	/**
	 * Calcular el valor de garantia para producto con precio mayor a 500.000
	 * 
	 * <b>Resultado:</b> EL valor de la garantia concuerda con el valor esperado
	 */
	@Test
	public void calcularValorGarantiaPrecioMayor() {
		// arrange
		Vendedor vendedor = new Vendedor();

		// act
		double valorGarantia = vendedor.calcularValorGarantia(VALOR_PRODUCTO_MAYOR);

		// assert
		assertEquals(VALOR_GARANTIA_MAYOR_ESPERADO, valorGarantia, 0);
	}

	/**
	 * Calcular el valor de garantia para producto con precio igual o menor a
	 * 500.000
	 * 
	 * <b>Resultado:</b> EL valor de la garantia concuerda con el valor esperado
	 */
	@Test
	public void calcularValorGarantiaPrecioMenor() {
		// arrange
		Vendedor vendedor = new Vendedor();

		// act
		double valorGarantia = vendedor.calcularValorGarantia(VALOR_PRODUCTO_MENOR);

		// assert
		assertEquals(VALOR_GARANTIA_MENOR_ESPERADO, valorGarantia, 0);
	}

	/**
	 * Valida c&oacute;digo de producto con 3 vocales para garantia extendida
	 * 
	 * <b>Resultado:</b> El c&oacute;digo de producto no tiene garantia
	 * extendida
	 */
	@Test
	public void codigoProductoNoTieneGarantiaTest() {
		// arrange
		Vendedor vendedor = new Vendedor();

		// act
		boolean codigoConGarantia = vendedor.codigoConGarantiaExtendida(CODIGO_PRODUCTO_TRES_VOCALES);

		// assert
		assertFalse(codigoConGarantia);
	}

	/**
	 * Valida c&oacute;digo de producto diferente a 3 vocales para garantia
	 * extendida
	 * 
	 * <b>Resultado:</b> El c&oacute;digo de producto si tiene garantia
	 * extendida
	 */
	@Test
	public void codigoProductoTieneGarantiaTest() {
		// arrange
		Vendedor vendedor = new Vendedor();

		// act
		boolean codigoConGarantia = vendedor.codigoConGarantiaExtendida(CODIGO_PRODUCTO_DIFERENTE_TRES_VOCALES);

		// assert
		assertTrue(codigoConGarantia);
	}

	/**
	 * Valida c&oacute;digo de producto null para garantia extendida
	 * 
	 * <b>Resultado:</b> El c&oacute;digo de producto no tiene garantia
	 * extendida
	 */
	@Test
	public void codigoProductoNullNoTieneGarantiaTest() {
		// arrange
		Vendedor vendedor = new Vendedor();

		// act
		boolean codigoConGarantia = vendedor.codigoConGarantiaExtendida(null);

		// assert
		assertFalse(codigoConGarantia);
	}

}
