package utilidades;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class CalendarUtils {

	private CalendarUtils() {
		throw new IllegalStateException("Clase de utilidad no se debe instanciar");
	}

	public static Date calcularFechaDiasHabiles(int dias) {
		LocalDate fechaIteracion = LocalDate.now();
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

	public static Date calcularFechaDiasCalendario(int dias) {
		LocalDate fechaVenciento = LocalDate.now().plusDays(dias);
		return Date.from(fechaVenciento.atStartOfDay(ZoneId.systemDefault()).toInstant());
	}

}
