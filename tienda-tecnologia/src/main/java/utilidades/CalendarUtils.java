package utilidades;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

/**
 * Contiene las utilidades necesarias para operaciones con fechas de calendario
 * 
 * @author Elmer Urrea
 * @since 23/01/2018
 */
public class CalendarUtils {

	private CalendarUtils() {
		throw new IllegalStateException("Clase de utilidad no se debe instanciar");
	}

	/**
	 * Permite sumarle d&iacute;as a una fecha teniendo en cuenta d&iacute;as no
	 * habiles
	 * 
	 * @param dias
	 *            numero de d&iacute;as para sumar a una fecha
	 * @param fechaInicio
	 *            fecha en que se inicia el calculo
	 * @return fecha calculada
	 */
	public static Date calcularFechaDiasHabiles(Date fechaInicio, int dias) {
		LocalDate fechaIteracion = toLocalDate(fechaInicio);
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
		return toDate(fechaIteracion);
	}

	/**
	 * Permite sumarle d&iacute;as a una fecha
	 * 
	 * @param dias
	 *            numero de d&iacute;as para sumar en una fecha
	 * @param fechaInicio
	 *            fecha en que se inicia el calculo
	 * @return fecha calculada
	 */
	public static Date calcularFechaDiasCalendario(Date fechaInicio, int dias) {
		LocalDate fechaVenciento = toLocalDate(fechaInicio).plusDays(dias);
		return toDate(fechaVenciento);
	}

	/**
	 * Permite convertir fecha en formato {@link Date} a formato
	 * {@link LocalDate}
	 * 
	 * @param fecha
	 *            en formato {@link Date}
	 * @return fecha en formato {@link LocalDate}
	 */
	public static LocalDate toLocalDate(Date fecha) {
		return fecha.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
	}

	/**
	 * Permite convertir fecha en formato {@link LocalDate} a formato
	 * {@link Date}
	 * 
	 * @param fecha
	 *            en formato {@link LocalDate}
	 * @return fecha en formato {@link Date}
	 */
	public static Date toDate(LocalDate fecha) {
		return Date.from(fecha.atStartOfDay(ZoneId.systemDefault()).toInstant());
	}

}
