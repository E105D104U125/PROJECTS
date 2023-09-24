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
import obras.*;
import usuarios.*;
import java.time.*;
import java.util.*;

public class TicketTest {

	private ZSNoNumerada pista;
	private ZSNumerada platea;
	private Ticket entr1;
	private EntradaNumerada entr2;
	private PrecioZona precPista;
	private RepresentacionEvento reprPrueba;
	private RepresentacionEvento reprPrueba2;
	private ObraTeatro eventoPrueba;
	private Danza eventoPrueba2;
	private ArrayList<PrecioZona> precios;
	private PrecioZona precPlatea;
	private ArrayList<Ticket> entradas;
	private Butaca b;
	private UsuarioRegistrado us1;

	@Before
	public void setup() throws Exception {
		pista = new ZSNoNumerada("Pista2", 150, 0.5);
		platea = new ZSNumerada("Platea", 5, 4);
		precPista = new PrecioZona(20, pista);
		precPlatea = new PrecioZona(30.40, platea);
		precios = new ArrayList<>();
		precios.add(precPista);
		precios.add(precPlatea);

		LocalDate fechaRepr = LocalDate.of(2021, 05, 12);
		LocalTime horaRepr = LocalTime.of(17, 00, 00);
		LocalTime horaRepr2 = LocalTime.of(20, 00, 00);
		String[] actores = { "Pilar Avila", "Alexia Lorrio" };
		eventoPrueba = new ObraTeatro("La casa de Bernarda Alba", 100, "Evento de prueba", "Federico Garcia Lorca",
				"Anar Olmeda", precios, actores);
		eventoPrueba2 = new Danza("Baile de Salón", 60, "Evento de prueba 2", "Paquito", "Juan", precios, actores,
				"Director", "Orquesta de acompañamiento");
		b = new Butaca(1, 1);
		reprPrueba = new RepresentacionEvento(fechaRepr, horaRepr, eventoPrueba);
		reprPrueba2 = new RepresentacionEvento(fechaRepr, horaRepr2, eventoPrueba2);
		entr1 = new Ticket(precPista, false, reprPrueba);
		entr2 = new EntradaNumerada(precPlatea, false, reprPrueba2, b);
		entradas = new ArrayList<>();
		entradas.add(entr1);
		entradas.add(entr2);

		us1 = new UsuarioRegistrado("holacaracola@gmail.com", "Password*2021");

	}

	@Test
	public void testPrecioZona() {
		precPlatea.setPrecio(21);
		entr1.setPrecioZona(precPlatea);
		assertEquals(21, entr1.getPrecioZona().getPrecio(), 0);
	}

	@Test
	public void testEntradaBloqueada() {
		assertFalse(entr2.getEntradaBloqueada());
		entr2.setEntradaBloqueada(true);
		assertTrue(entr2.getEntradaBloqueada());
	}

	@Test
	public void testReserva() {
		assertNull(entr1.getReserva());
		Reserva r = new Reserva(reprPrueba, entradas);
		assertEquals(r, entr1.getReserva());
		entradas.remove(entr2);
		assertEquals(r, entr1.getReserva());
	}

	@Test
	public void testRepresentacion() {
		assertEquals(reprPrueba, entr1.getRepresentacion());
		entr2.setRepresentacion(reprPrueba2);
		assertEquals(reprPrueba2, entr2.getRepresentacion());
	}

	@Test
	public void testUsuarioTicket() {
		assertNull(entr1.getUsuario());
		entr1.reservarTicket(us1);
		assertEquals(us1, entr1.getUsuario());
		assertNull(entr2.getUsuario());
		assertTrue(entr1.getEntradaBloqueada());
	}

	@Test
	public void testRetirar() {
		Ticket entr3 = new Ticket(precPista, false, reprPrueba);
		us1.addEntrada(entr1);
		us1.addEntrada(entr2);
		entr3.comprarTicket(us1);

		entradas.add(entr3);
		assertEquals(reprPrueba.getEntradas(), entr3.getRepresentacion().getEntradas());

		entr3.retirar();
		// quitar a usuario
		entradas.remove(entr3);
		assertEquals(entradas, us1.getEntradasUsuario());
		// quitar a reservas de usuario
		Reserva r = new Reserva(reprPrueba, entradas);
		entr1.setReserva(r);
		entr1.reservarTicket(us1);
		ArrayList<Reserva> reservas = new ArrayList<>();
		reservas.add(r);
		us1.realizarReserva(entradas);
		// entradas contiene entr1 y entr2
		assertEquals(reservas.size(), us1.getReservas().size());
		// quitar de la representacion
		ArrayList<Ticket> entr = new ArrayList<>();
		entr = entr3.getRepresentacion().getEntradas();
		assertEquals(entr, entr3.getRepresentacion().getEntradas());
	}

	@Test
	public void testToString() {
		String s = "Ticket con id: " + entr1.getIdTicket() + " , para la representación: "
				+ entr1.getRepresentacion().getEvento().getTitulo() + " , con precio: "
				+ entr1.getPrecioZona().getPrecio() + " , ¿se encuentra bloqueada? " + entr1.getEntradaBloqueada();
		assertEquals(s, entr1.toString());
	}

}
