package utilidades;

/**
 * Contiene un grupo de utilidades aplicables sobre cadenas de tipo
 * {@link String}
 * 
 * @author Elmer Urrea
 * @since 23/01/2018
 */
public class StringUtils {

	private StringUtils() {
		throw new IllegalStateException("Clase de utilidad no se debe instanciar");
	}

	/**
	 * Permite contar el numero de vocales encontradas en una cadena
	 * 
	 * @param cadena de caracteres
	 * @return total de vocales encontradas en una cadena
	 */
	public static int contarVocales(String cadena) {
		return cadena.replaceAll("[^aeiouAEIOUáéíóúÁÉÍÓÚ]", "").length();
	}

}
