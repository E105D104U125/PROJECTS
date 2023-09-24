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

public class EstadisticaZonaTest {

	private EstadisticaZona ez;

	@Before
	public void setUp() throws Exception {
		ez = new EstadisticaZona(-999.99);
	}

	@Test
	public void testEstadisticaZona() {
		assertEquals(0, ez.getRecaudacion(), 0);
	}

}
