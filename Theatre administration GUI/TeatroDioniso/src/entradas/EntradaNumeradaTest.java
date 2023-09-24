/** 
 * 
 * @author Olmar Arranz olmar.arranz@estudiante.uam.es 
 * @author Cristina Madrigal cristina.madrigalb@estudiante.uam.es 
 * @author Eduardo Terrés Caballero eduardo.terres@estudiante.uam.es
 *  
 */
package entradas;

import zonas.*;
import obras.*;
import java.time.*;
import java.util.*;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class EntradaNumeradaTest {

	private EntradaNumerada e;
	private PrecioZona precPlatea;
	private Butaca b;
	private RepresentacionEvento r;

	@Before
	public void setup() throws Exception {

		ZSNumerada platea = new ZSNumerada("Platea", 10, 20);
		precPlatea = new PrecioZona(20, platea);
		ArrayList<PrecioZona> precios = new ArrayList<>();
		precios.add(precPlatea);

		b = new Butaca(2, 1);

		LocalDate fechaRepr = LocalDate.of(2021, 05, 12);
		LocalTime horaRepr = LocalTime.of(17, 00, 00);
		String[] actores = { "Pilar Avila", "Alexia Lorrio" };
		ObraTeatro eventoPrueba = new ObraTeatro("La casa de Bernarda Alba", 100, "Evento de prueba",
				"Federico GarcÃ­a Lorca", "Oscar Olmeda", precios, actores);

		r = new RepresentacionEvento(fechaRepr, horaRepr, eventoPrueba);

		e = new EntradaNumerada(precPlatea, false, r, b);
	}

	@Test
	public void testButaca() {
		Butaca btc = new Butaca(1, 3);
		e.setButaca(btc);
		assertEquals(btc, e.getButaca());
	}

	@Test
	public void testPrecioZona() {
		ZSNumerada patio = new ZSNumerada("Patio de butacas", 6, 5);
		PrecioZona precPatio = new PrecioZona(25, patio);
		e.setPrecioZona(precPatio);
		assertEquals(precPatio, e.getPrecioZona());
	}

	@Test
	public void testEntradaBloqueada() {
		e.setEntradaBloqueada(true);
		assertTrue(e.getEntradaBloqueada());
	}

	@Test
	public void testToString() {
		String s;
		s = "Ticket con id: " + e.getIdTicket() + " , para la representación: "
				+ e.getRepresentacion().getEvento().getTitulo() + " , con precio: " + e.getPrecioZona().getPrecio()
				+ " , ¿se encuentra bloqueada? " + e.getEntradaBloqueada();
		s += "Butaca asociada a la entrada: " + e.getButaca() + ".\n";
		assertEquals(s, e.toString());
	}

}
