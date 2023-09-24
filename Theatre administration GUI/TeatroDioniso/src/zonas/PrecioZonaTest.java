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

public class PrecioZonaTest {

	private ZSNumerada platea;
	private ZSNoNumerada pista;
	private PrecioZona precPlatea;
	private PrecioZona precPista;

	@Before
	public void setUp() throws Exception {

		// Zona numerada con 15 filas y 20 columnas
		platea = new ZSNumerada("Platea", 5, 4);
		// Zona no numerada con reducción de aforo
		pista = new ZSNoNumerada("Pista2", 150, 0.5);

		/* Creamos precios para las zonas */
		// Para la platea
		precPlatea = new PrecioZona(30.40, platea);
		// Para la pista
		precPista = new PrecioZona(0, pista);

	}

	@Test
	public void testPrecio() {
		assertEquals(30.40, precPlatea.getPrecio(), 0);
		precPista.setPrecio(50);
		assertEquals(50, precPista.getPrecio(), 0);
	}

	@Test
	public void testZona() {
		assertEquals(pista, precPista.getZona());
		assertEquals(platea, precPlatea.getZona());
	}

	@Test
	public void testToString() {
		String s;
		s = "Precio de la zona: " + precPlatea.getPrecio() + " , zona -> " + precPlatea.getZona();
		assertEquals(s, precPlatea.toString());
	}

}
