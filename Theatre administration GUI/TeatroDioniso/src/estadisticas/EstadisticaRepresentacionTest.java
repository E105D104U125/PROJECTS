/** 
 * 
 * @author Olmar Arranz olmar.arranz@estudiante.uam.es 
 * @author Cristina Madrigal cristina.madrigalb@estudiante.uam.es 
 * @author Eduardo Terrés Caballero eduardo.terres@estudiante.uam.es
 *  
 */
package estadisticas;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class EstadisticaRepresentacionTest {
	private EstadisticaRepresentacion erp;
	double iniRec;
	int iniOc;

	@Before
	public void setUp() throws Exception {
		iniRec = 450.4;
		iniOc = 15;
		erp = new EstadisticaRepresentacion(iniRec, iniOc);
	}

	@Test
	public void testOcupacion() {
		assertEquals(iniOc, erp.getOcupacion());
		erp.setOcupacion(20);
		assertEquals(20, erp.getOcupacion());
	}

	@Test
	public void testRecaudacion() {
		assertEquals(iniRec, erp.getRecaudacion(), 0);
		erp.setRecaudacion(150.4);
		assertEquals(150.4, erp.getRecaudacion(), 0);
	}

	@Test
	public void testToString() {
		String s = "Recaudacion: " + erp.getRecaudacion() + " Ocupacion: " + erp.getOcupacion() + "\n";
		assertEquals(s, erp.toString());
	}

}
