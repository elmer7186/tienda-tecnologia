package dominio.integracion;

import static org.junit.Assert.fail;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import dominio.Vendedor;
import dominio.GarantiaExtendida;
import dominio.Producto;
import dominio.excepcion.GarantiaExtendidaException;
import dominio.repositorio.RepositorioProducto;
import dominio.repositorio.RepositorioGarantiaExtendida;
import persistencia.sistema.SistemaDePersistencia;
import testdatabuilder.ProductoTestDataBuilder;

public class VendedorTest {

	private static final String COMPUTADOR_LENOVO = "Computador Lenovo";
	private static final String NOMBRE_CLIENTE = "Javier Mendez";
	private static final String CODIGO_PRODUCTO_TRES_VOCALES = "1239AEI";
	private static final SimpleDateFormat FORMATO_FECHA = new SimpleDateFormat("dd/MM/yyyy");
	private static final double VALOR_PRODUCTO_MAYOR = 720_000;
	private static final double VALOR_GARANTIA_MAYOR_ESPERADO = 144_000;
	private static final double VALOR_PRODUCTO_MENOR = 500_000;
	private static final double VALOR_GARANTIA_MENOR_ESPERADO = 50_000;

	private SistemaDePersistencia sistemaPersistencia;

	private RepositorioProducto repositorioProducto;
	private RepositorioGarantiaExtendida repositorioGarantia;

	@Before
	public void setUp() {

		sistemaPersistencia = new SistemaDePersistencia();

		repositorioProducto = sistemaPersistencia.obtenerRepositorioProductos();
		repositorioGarantia = sistemaPersistencia.obtenerRepositorioGarantia();

		sistemaPersistencia.iniciar();
	}

	@After
	public void tearDown() {
		sistemaPersistencia.terminar();
	}

	/**
	 * Genera garantia extendida de prueba
	 * 
	 * <b>Resultado:<b> La garantia extendida es asociada al producto
	 */
	@Test
	public void generarGarantiaTest() {
		// arrange
		Producto producto = new ProductoTestDataBuilder().conNombre(COMPUTADOR_LENOVO).build();
		repositorioProducto.agregar(producto);
		Vendedor vendedor = new Vendedor(repositorioProducto, repositorioGarantia);

		// act
		vendedor.generarGarantia(producto.getCodigo(), NOMBRE_CLIENTE);

		// assert
		Assert.assertTrue(vendedor.tieneGarantia(producto.getCodigo()));
		Assert.assertNotNull(repositorioGarantia.obtenerProductoConGarantiaPorCodigo(producto.getCodigo()));

	}

	/**
	 * Verifica que a un producto no se le genere 2 veces una garantia extendida
	 * 
	 * <b>Resultado:</b> Genera excepci&oacute;n indicando que ya tiene garantia
	 * extendida y con el mensaje indicado
	 */
	@Test
	public void productoYaTieneGarantiaTest() {
		// arrange
		Producto producto = new ProductoTestDataBuilder().conNombre(COMPUTADOR_LENOVO).build();

		repositorioProducto.agregar(producto);

		Vendedor vendedor = new Vendedor(repositorioProducto, repositorioGarantia);

		// act
		vendedor.generarGarantia(producto.getCodigo(), NOMBRE_CLIENTE);
		try {

			vendedor.generarGarantia(producto.getCodigo(), NOMBRE_CLIENTE);
			fail();

		} catch (GarantiaExtendidaException e) {
			// assert
			Assert.assertEquals(Vendedor.EL_PRODUCTO_TIENE_GARANTIA, e.getMessage());
		}
	}

	/**
	 * Verifica que no genere garantía extendida para productos con
	 * c&oacute;digo de producto invalidos (Contenga 3 vocales)
	 * 
	 * <b>Resultado:</b> Genera excepci&oacute;n indicando que la garantia no
	 * puede generarse
	 */
	@Test
	public void productoNoTieneGarantiaExtendidaTest() {
		// arrange
		Producto producto = new ProductoTestDataBuilder().conCodigo(CODIGO_PRODUCTO_TRES_VOCALES).build();

		repositorioProducto.agregar(producto);

		Vendedor vendedor = new Vendedor(repositorioProducto, repositorioGarantia);

		// act
		try {
			vendedor.generarGarantia(producto.getCodigo(), NOMBRE_CLIENTE);
			fail();
		} catch (GarantiaExtendidaException e) {
			// assert
			Assert.assertEquals(Vendedor.EL_PRODUCTO_NO_TIENE_GARANTIA_EXTENDIDA, e.getMessage());
		}
	}

	/**
	 * Genera garantia extendida de prueba para producto por encima de los
	 * 500.000
	 * 
	 * <b>Resultado:</b> Compara que los valores concuerden con los esperados en
	 * la inserci&oacute;n de la garantia extendida
	 */
	@Test
	public void generarGarantiaMayorTest() {
		// arrange
		Producto producto = new ProductoTestDataBuilder().conPrecio(VALOR_PRODUCTO_MAYOR).build();
		repositorioProducto.agregar(producto);
		Vendedor vendedor = new Vendedor(repositorioProducto, repositorioGarantia);

		// act
		vendedor.generarGarantia(producto.getCodigo(), NOMBRE_CLIENTE);
		GarantiaExtendida garantiaGenerada = repositorioGarantia.obtener(producto.getCodigo());
		Date fechaFinEsperada = vendedor.calcularFechaVencimiento(new Date(), VALOR_PRODUCTO_MAYOR);
		String fechaFinEsperadaString = FORMATO_FECHA.format(fechaFinEsperada);
		String fechaFinCalculadaString = FORMATO_FECHA.format(garantiaGenerada.getFechaFinGarantia());

		// assert
		Assert.assertEquals(NOMBRE_CLIENTE, garantiaGenerada.getNombreCliente());
		Assert.assertEquals(VALOR_GARANTIA_MAYOR_ESPERADO, garantiaGenerada.getPrecioGarantia(), 0);
		Assert.assertEquals(fechaFinEsperadaString, fechaFinCalculadaString);
	}

	/**
	 * Genera garantia extendida de prueba para producto con valor igual o
	 * inferior a 500.000
	 * 
	 * <b>Resultado:</b> Compara que los valores concuerden con los esperados en
	 * la inserci&oacute;n de la garantia extendida
	 */
	@Test
	public void generarGarantiaMenorTest() {
		// arrange
		Producto producto = new ProductoTestDataBuilder().conPrecio(VALOR_PRODUCTO_MENOR).build();
		repositorioProducto.agregar(producto);
		Vendedor vendedor = new Vendedor(repositorioProducto, repositorioGarantia);

		// act
		vendedor.generarGarantia(producto.getCodigo(), NOMBRE_CLIENTE);
		GarantiaExtendida garantiaGenerada = repositorioGarantia.obtener(producto.getCodigo());
		Date fechaFinEsperada = vendedor.calcularFechaVencimiento(new Date(), VALOR_PRODUCTO_MENOR);
		String fechaFinEsperadaString = FORMATO_FECHA.format(fechaFinEsperada);
		String fechaFinCalculadaString = FORMATO_FECHA.format(garantiaGenerada.getFechaFinGarantia());

		// assert
		Assert.assertEquals(NOMBRE_CLIENTE, garantiaGenerada.getNombreCliente());
		Assert.assertEquals(VALOR_GARANTIA_MENOR_ESPERADO, garantiaGenerada.getPrecioGarantia(), 0);
		Assert.assertEquals(fechaFinEsperadaString, fechaFinCalculadaString);
	}
}
