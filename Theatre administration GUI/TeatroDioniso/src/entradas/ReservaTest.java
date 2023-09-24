/** 
 * 
 * @author Olmar Arranz olmar.arranz@estudiante.uam.es 
 * @author Cristina Madrigal cristina.madrigalb@estudiante.uam.es 
 * @author Eduardo Terrés Caballero eduardo.terres@estudiante.uam.es
 *  
 */
package entradas;

import obras.*;
import zonas.*;
import java.util.*;
import java.time.*;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ReservaTest {

	private Reserva r;
	private ArrayList<Ticket> entradas;
	private ZSNoNumerada pista;
	private PrecioZona precPista;
	private ZSNumerada platea;
	private PrecioZona precPlatea;
	private ArrayList<PrecioZona> precios;
	private Ticket entr1;
	private EntradaNumerada entr2;
	private RepresentacionEvento reprPrueba;
	private ObraTeatro eventoPrueba;
	private Butaca b;
	private LocalDate fechaRepr;

	@Before
	public void setup() throws Exception {

		pista = new ZSNoNumerada("Pista2", 150, 0.5);
		platea = new ZSNumerada("Platea", 5, 4);
		precPista = new PrecioZona(20, pista);
		precPlatea = new PrecioZona(30.40, platea);
		precios = new ArrayList<>();
		precios.add(precPista);
		precios.add(precPlatea);

		fechaRepr = LocalDate.of(2021, 05, 12);
		LocalTime horaRepr = LocalTime.of(17, 00, 00);
		String[] actores = { "Pilar Avila", "Alexia Lorrio" };
		eventoPrueba = new ObraTeatro("La casa de Bernarda Alba", 100, "Evento de prueba", "Federico GarcÃ­a Lorca",
				"Oscar Olmeda", precios, actores);

		reprPrueba = new RepresentacionEvento(fechaRepr, horaRepr, eventoPrueba);

		b = new Butaca(2, 1);
		entr1 = new Ticket(precPista, false, reprPrueba);
		entr2 = new EntradaNumerada(precPlatea, false, reprPrueba, b);
		entradas = new ArrayList<>();
		entradas.add(entr1);

		r = new Reserva(reprPrueba, entradas);
	}

	@Test
	public void testAddEntrada() {
		ArrayList<Ticket> tArrayAux = new ArrayList<>();
		tArrayAux.add(entr1);
		tArrayAux.add(entr2);
		r.addEntrada(entr2);
		assertEquals(tArrayAux, r.getEntradas());
		assertEquals(2, r.getSizeEntradas());
	}

	@Test
	public void testSetRepresentacion() {
		LocalTime hora = LocalTime.of(21, 00, 00);
		RepresentacionEvento repr = new RepresentacionEvento(fechaRepr, hora, eventoPrueba);
		r.setRepresentacion(repr);
		assertEquals(repr, r.getRepresentacion());
	}

	@Test
	public void testCalcularPrecio() {
		r.addEntrada(entr2);
		assertEquals(50.40, r.calcularPrecio(), 0);
	}

	@Test
	public void testToString() {
		String s = "Reserva para la representación: " + r.getRepresentacion();
		s += "\nformada por las entradas: \n";
		for (Ticket e : r.getEntradas()) {
			s += e.toString();
		}
		assertEquals(s, r.toString());
	}

}
