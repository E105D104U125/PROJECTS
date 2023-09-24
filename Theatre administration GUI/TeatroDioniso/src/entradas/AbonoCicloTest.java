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
import java.util.*;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class AbonoCicloTest {

	private AbonoCiclo ac;
	private ArrayList<PrecioZona> precios;
	private Ciclo c;

	@Before
	public void setup() throws Exception {
		Zona pista = new ZSNoNumerada("Pista", 150, 0.5);
		PrecioZona precPista = new PrecioZona(20, pista);
		precios = new ArrayList<>();
		precios.add(precPista);

		ArrayList<Evento> eventos = new ArrayList<>();
		String[] actores = { "Pilar Avila", "Alexia Lorrio" };
		ObraTeatro eventoPrueba = new ObraTeatro("La casa de Bernarda Alba", 100, "Evento de prueba",
				"Federico GarcÃ­a Lorca", "Ã“scar Olmeda", precios, actores);
		eventos.add(eventoPrueba);

		c = new Ciclo("Ciclo de prueba", "Eventos al azar", eventos);

		ac = new AbonoCiclo(150, pista, 0.15, c);
	}

	@Test
	public void testDescuento() {
		ac.setDescuento(123.98);
		assertEquals(123.98, ac.getDescuento(), 0);
	}

	@Test
	public void testCicloAbonado() {
		String[] bailarines = { "Lucía Alonso", "Marcos Crespo" };
		Danza eventoPrueba2 = new Danza("Baile de Salón", 60, "Evento de prueba 2", "Paquito", "Juan", precios,
				bailarines, "Director", "Orquesta de acompañamiento");

		c.addEventos(eventoPrueba2);
		ac.setCicloAbonado(c);
		assertEquals(c, ac.getCicloAbonado());
	}

}
