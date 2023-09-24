/** 
 * 
 * @author Olmar Arranz olmar.arranz@estudiante.uam.es 
 * @author Cristina Madrigal cristina.madrigalb@estudiante.uam.es 
 * @author Eduardo Terrés Caballero eduardo.terres@estudiante.uam.es
 *  
 */
package zonas;

import java.time.*;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ButacaTest {

	private DeshabilitacionButaca dbt;
	private Butaca b;
	private Butaca b2;
	private LocalDate fIni;
	private LocalDate fFin;

	@Before
	public void setUp() throws Exception {
		fIni = LocalDate.of(2021, 8, 10);
		fFin = LocalDate.of(2021, 9, 10);
		dbt = new DeshabilitacionButaca("Deshabilitada por pandemia", fIni, fFin);
		b = new Butaca(3, 4);
		b2 = new Butaca(7, 5);
	}

	@Test
	public void testFila() {
		b.setFila(10);
		assertEquals(10, b.getFila());
	}

	@Test
	public void testColumna() {
		b.setColumna(10);
		assertEquals(10, b.getColumna());
	}

	@Test
	public void testSetHabilitada() {
		b.setHabilitada(true);
		assertTrue(b.getHabilitada());
	}

	@Test
	public void testDeshabilitarButaca() {
		b.deshabilitarButaca(dbt);
		assertFalse(b.getHabilitada());
		assertEquals(dbt, b.getDeshabilitacionButaca());

		b2.deshabilitarButaca(dbt);
		assertFalse(b2.getHabilitada());
		assertEquals(dbt, b2.getDeshabilitacionButaca());

	}

	@Test
	public void testHabilitarButaca() {
		b.habilitarButaca();
		assertTrue(b.getHabilitada());
		assertNull(b.getDeshabilitacionButaca());
	}

	@Test
	public void testToString() {
		String s1 = "Butaca en la columna: " + b.getColumna() + " y fila: " + b.getFila() + ". Estado de la butaca: "
				+ b.getHabilitada() + ".\n";
		assertEquals(s1, b.toString());

		b2.deshabilitarButaca(dbt);
		String s2 = "Butaca en la columna: " + b2.getColumna() + " y fila: " + b2.getFila() + ". Estado de la butaca: "
				+ b2.getHabilitada() + ".\n";
		s2 += b2.getDeshabilitacionButaca().toString();

		assertEquals(s2, b2.toString());
	}

}