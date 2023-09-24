/** 
 * 
 * @author Olmar Arranz olmar.arranz@estudiante.uam.es 
 * @author Cristina Madrigal cristina.madrigalb@estudiante.uam.es 
 * @author Eduardo Terrés Caballero eduardo.terres@estudiante.uam.es
 *  
 */
package zonas;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.time.*;
import java.util.ArrayList;

public class ZSNumeradaTest {

	private ZSNumerada platea;
	private ZSNumerada patio;
	private LocalDate fIni;
	private LocalDate fFin;
	private DeshabilitacionButaca dbt;

	@Before
	public void setUp() throws Exception {
		// Zona numerada con 15 filas y 20 columnas
		platea = new ZSNumerada("Platea", 15, 20);
		// Zona numerada con 30 filas y 10 columnas
		patio = new ZSNumerada("Patio de butacas", 30, 10);
		fIni = LocalDate.now();
		fFin = LocalDate.of(2021, 9, 1);
		dbt = new DeshabilitacionButaca("Prueba de deshabilitación", fIni, fFin);
	}

	@Test
	public void testToString() {
		String s = "Nombre de la zona: " + platea.getNombre() + ", identificador: " + platea.getID()
				+ ", estadística asociada a la zona: " + platea.getEstadistica();
		s += ", número de filas: " + platea.getnFila() + ", número de columnas: " + platea.getnColumna() + ".\n";

		assertEquals(s, platea.toString());
	}

	@Test
	public void testnFila() {
		patio.setnFila(10);
		assertEquals(10, patio.getnFila());
	}

	@Test
	public void testnColumna() {
		patio.setnColumna(8);
		assertEquals(8, patio.getnColumna());
	}

	@Test
	public void testButacas() {
		/* Deshabilitamos las butacas de la última fila de la platea */
		for (Butaca b : platea.getButacas()) {
			if (b.getFila() == 5) {
				b.deshabilitarButaca(dbt);
			}
		}

		for (Butaca b : platea.getButacas()) {
			if (b.getFila() == 5) {
				assertEquals(false, b.getHabilitada());
				assertEquals(dbt, b.getDeshabilitacionButaca());
			}
		}

	}

	@Test
	public void testGetButacasDesHabilitadas() {
		ArrayList<Butaca> butacasHabilitadas = new ArrayList<>();
		ArrayList<Butaca> butacasDeshabilitadas = new ArrayList<>();
		/* Deshabilitamos las butacas de la última fila de la platea */
		for (Butaca b : platea.getButacas()) {
			if (b.getFila() == 5) {
				b.deshabilitarButaca(dbt);
			}
		}

		/* test getButacasHabilitadas */
		for (Butaca b : platea.getButacas()) {
			if (b.getHabilitada() == true) {
				butacasHabilitadas.add(b);
			}
		}

		assertEquals(platea.getButacasHabilitadas(), butacasHabilitadas);

		/* test getButacasDeshabilitadas */
		for (Butaca b : platea.getButacas()) {
			if (b.getHabilitada() == false) {
				butacasDeshabilitadas.add(b);
			}
		}

		assertEquals(platea.getButacasDeshabilitadas(), butacasDeshabilitadas);
	}

	@Test
	public void testCrearButacas() {
		for (Butaca b : platea.getButacas()) {
			assertNotNull(b);
		}
	}

}
