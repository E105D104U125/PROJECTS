/** 
 * 
 * @author Olmar Arranz olmar.arranz@estudiante.uam.es 
 * @author Cristina Madrigal cristina.madrigalb@estudiante.uam.es 
 * @author Eduardo Terrés Caballero eduardo.terres@estudiante.uam.es
 *  
 */
package entradas;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import zonas.*;
import java.time.*;

public class AbonoAnualTest {

	private ZSNoNumerada pista;
	private AbonoAnual anual;
	private LocalDate fechaAnual;

	@Before
	public void setup() throws Exception {
		pista = new ZSNoNumerada("Pista2", 150, 0.5);
		fechaAnual = LocalDate.of(2021, 03, 8);
		anual = new AbonoAnual(120, pista, fechaAnual);
		anual.setPrecioAbono(83.05);
	}

	@Test
	public void testToString() {
		String s;
		s = "Precio del abono: " + anual.getPrecioAbono() + ", zona: " + anual.getZonaAbono().getNombre();
		s += ", la fecha inicial del abono es: " + anual.getFechaIni() + " , y por tanto la final es: "
				+ anual.getFechaFin();
		assertEquals(s, anual.toString());
	}

	@Test
	public void testPrecioAbono() {
		assertEquals(83.05, anual.getPrecioAbono(), 0);
		anual.setPrecioAbono(-83.05);
		assertEquals(0, anual.getPrecioAbono(), 0);
	}

	@Test
	public void testZonaAbono() {
		assertEquals(pista, anual.getZonaAbono());
		ZSNumerada platea = new ZSNumerada("Platea", 5, 4);
		anual.setZonaAbono(platea);
		assertEquals(platea, anual.getZonaAbono());

	}

	@Test
	public void testFechaIniFin() {
		assertEquals(fechaAnual, anual.getFechaIni());
		LocalDate fechaFin = LocalDate.of(2022, 03, 8);
		assertEquals(fechaFin, anual.getFechaFin());
		anual.setFechaIni(fechaFin);
		assertEquals(fechaFin.plusDays(365), anual.getFechaFin());
	}

}
