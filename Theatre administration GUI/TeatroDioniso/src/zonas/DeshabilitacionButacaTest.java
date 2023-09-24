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

public class DeshabilitacionButacaTest {

	private DeshabilitacionButaca dbt;
	private LocalDate fIni;
	private LocalDate fFin;
	private DeshabilitacionButaca dbt2;

	@Before
	public void setUp() throws Exception {
		fIni = LocalDate.of(2021, 8, 10);
		fFin = LocalDate.of(2021, 9, 10);
		dbt = new DeshabilitacionButaca("", fIni, fFin);
		dbt2 = new DeshabilitacionButaca("Prueba2", fFin, fIni);
	}

	@Test
	public void testMotivo() {
		String s = "Prueba";
		dbt.setMotivo(s);
		assertEquals(s, dbt.getMotivo());
	}

	@Test
	public void testToString() {
		String s = "Motivo de la deshabilitación: " + dbt.getMotivo() + ". Fecha de inicio de la deshabilitación: "
				+ dbt.getfIni() + ". Fecha de fin de la deshabilitación: " + dbt.getfFin() + ".\n";
		assertEquals(s, dbt.toString());

		String s2 = "Esta deshabilitación tiene fechas incorrectas\n";
		assertEquals(s2, dbt2.toString());
	}

}
