package utilidades;

public class StringUtils {

	private StringUtils() {
		throw new IllegalStateException("Clase de utilidad no se debe instanciar");
	}

	public static int contarVocales(String codigo) {
		return codigo.replaceAll("[^aeiouAEIOUáéíóúÁÉÍÓÚ]", "").length();
	}

}
