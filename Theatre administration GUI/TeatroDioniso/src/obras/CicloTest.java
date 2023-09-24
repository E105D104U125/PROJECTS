/** 
 * 
 * @author Olmar Arranz olmar.arranz@estudiante.uam.es 
 * @author Cristina Madrigal cristina.madrigalb@estudiante.uam.es 
 * @author Eduardo Terrés Caballero eduardo.terres@estudiante.uam.es
 *  
 */
package obras;

import zonas.*;
import entradas.*;
import java.util.*;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CicloTest {
	private Ciclo c;
	private ArrayList<Evento> eventos;
	private Evento eventoPrueba;
	private Evento eventoPrueba2;
	private ZSNoNumerada pista;
	private PrecioZona precPista;
	private ZSNumerada platea;
	private PrecioZona precPlatea;
	private ArrayList<PrecioZona> precios;
	private AbonoCiclo ac;

	/**
	 * @throws java.lang.Exception excepcion
	 */
	@Before
	public void setUp() throws Exception {
		pista = new ZSNoNumerada("Pista2", 150, 0.5);
		platea = new ZSNumerada("Platea", 5, 4);
		precPista = new PrecioZona(20, pista);
		precPlatea = new PrecioZona(30.40, platea);
		precios = new ArrayList<>();
		precios.add(precPista);
		precios.add(precPlatea);

		eventos = new ArrayList<>();
		String[] actores = { "Pilar Avila", "Alexia Lorrio" };
		eventoPrueba = new ObraTeatro("La casa de Bernarda Alba", 100, "Evento de prueba", "Federico GarcÃ­a Lorca",
				"Ã“scar Olmeda", precios, actores);
		eventoPrueba2 = new Danza("Baile de Salón", 60, "Evento de prueba 2", "Paquito", "Juan", precios, actores,
				"Director", "Orquesta de acompañamiento");
		eventos.add(eventoPrueba);
		eventos.add(eventoPrueba2);

		c = new Ciclo("Ciclo de prueba", "Eventos al azar", eventos);

		ac = new AbonoCiclo(300, platea, 0.4, c);

	}

	@Test
	public void testSetNombre() {
		c.setNombre("Cambio de nombre");
		assertEquals("Cambio de nombre", c.getNombre());
	}

	@Test
	public void testSetDescripcion() {
		c.setDescripcion("Nueva descripción");
		assertEquals("Nueva descripción", c.getDescripcion());
	}

	@Test
	public void testAddAbonosCiclo() {
		AbonoCiclo ac2 = new AbonoCiclo(275, pista, 0.4, c);
		ArrayList<AbonoCiclo> abonos = new ArrayList<>();
		abonos.add(ac);
		abonos.add(ac2);
		c.addAbonosCiclo(ac2);
		assertEquals(abonos, c.getAbonosCiclo());
		assertEquals(abonos.size(), c.getSizeAbonosCiclo());
	}

	@Test
	public void testAddEventos() {
		String[] interpretes = { "Pilar Avila", "Alexia Lorrio" };
		Evento e = new Concierto("Concierto musica", 100, "Evento a a�adir", "Raquel", "Lucas", precios,
				"Orquesta popular", interpretes, "Porgrama del concierto");
		c.addEventos(e);
		eventos.add(e);
		assertEquals(eventos, c.getEventos());
		assertEquals(eventos.size(), c.getSizeEventos());
	}

}
