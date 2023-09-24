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

public class ZSNoNumeradaTest {

	private ZSNoNumerada z;
	private ZSNoNumerada z2;

	@Before
	public void setUp() throws Exception {
		// Zona numerada con 15 filas y 20 columnas
		z = new ZSNoNumerada("Pista", 150, 0);
		// Zona no numerada con reducción de aforo
		z2 = new ZSNoNumerada("Pista", 200, 0);
	}

	@Test
	public void testToString() {
		String s = "Nombre de la zona: " + z.getNombre() + ", identificador: " + z.getID()
				+ ", estadística asociada a la zona: " + z.getEstadistica();
		s += ", aforo: " + z.getAforo() + ", reducción del aforo: " + z.getReduccionAforo() + ".\n";
		assertEquals(s, z.toString());
	}

	@Test
	public void testReducirAforo() {
		z2.reducirAforo(0.5);
		assertEquals(200, z2.getAforo());
		assertEquals(0.5, z2.getReduccionAforo(), 0);
		assertEquals(100, z2.getAforoDisponible(), 0);
	}

	@Test
	public void testAforo() {
		z.setAforo(1000);
		assertEquals(1000, z.getAforo());
	}

}
