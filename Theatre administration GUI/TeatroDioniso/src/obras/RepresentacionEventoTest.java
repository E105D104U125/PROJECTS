/** 
 * 
 * @author Olmar Arranz olmar.arranz@estudiante.uam.es 
 * @author Cristina Madrigal cristina.madrigalb@estudiante.uam.es 
 * @author Eduardo Terrés Caballero eduardo.terres@estudiante.uam.es
 *  
 */
package obras;

import zonas.*;
import usuarios.*;
import estadisticas.*;
import java.util.*;
import java.time.*;
import entradas.*;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class RepresentacionEventoTest {

	private ObraTeatro obra;
	private LocalDate fecha;
	private LocalTime hora;
	private PrecioZona precPista;
	private PrecioZona precPlatea;
	private Zona pista;
	private ZSNumerada platea;
	private ArrayList<PrecioZona> precios;
	private RepresentacionEvento repr;
	private ArrayList<UsuarioRegistrado> usuarios;
	private UsuarioRegistrado usuario;
	private Butaca b;

	@Before
	public void setUp() throws Exception {
		pista = new ZSNoNumerada("Pista", 20, 0.5);
		platea = new ZSNumerada("Platea", 5, 4);
		precPista = new PrecioZona(10, pista);
		precPlatea = new PrecioZona(12.40, platea);
		precios = new ArrayList<>();
		precios.add(precPista);
		precios.add(precPlatea);

		String[] actores = { "Pilar Avila", "Alexia Lorrio" };
		obra = new ObraTeatro("Obra de teatro siglo XV", 100, "Danza para el ciclo sigo XV", "Federico Garcia Lorca",
				"Oscar Olmeda", precios, actores);
		fecha = LocalDate.of(2021, 05, 12);
		hora = LocalTime.of(20, 00, 00);
		repr = new RepresentacionEvento(fecha, hora, obra);

		usuarios = new ArrayList<>();
		usuario = new UsuarioRegistrado("usuario@teatro.com", "1234");
		usuarios.add(usuario);

		b = new Butaca(4, 2);
	}

	@Test
	public void testFecha() {
		LocalDate newFecha = LocalDate.now().plusDays(12);
		repr.setFecha(newFecha);
		assertEquals(newFecha, repr.getFecha());
	}

	@Test
	public void testHora() {
		LocalTime newHora = LocalTime.of(20, 30, 00);
		repr.setHora(newHora);
		assertEquals(newHora, repr.getHora());
	}

	@Test
	public void testGetSizeEntradas() {
		assertEquals(30, repr.getSizeEntradas());
	}

	@Test
	public void testAddListaDeEspera() {
		repr.addListaDeEspera(usuario);
		assertEquals(usuarios, repr.getListaDeEspera());
		assertEquals(1, repr.getSizeListaDeEspera());

		repr.avisarListaDeEspera();
		// Recibe la notificacion
		assertEquals(1, usuario.getNotificaciones().size());
	}

	@Test
	public void testSetEvento() {
		ZSNoNumerada pista = new ZSNoNumerada("Pista", 20, 0.5);
		PrecioZona precPista = new PrecioZona(10, pista);
		precios.add(precPista);
		String[] bailarines = { "María González" };
		Danza danza = new Danza("Danza siglo XV", 120, "Danza para el ciclo sigo XV", "Pedro Martínez", "Marta Gómez",
				precios, bailarines, "Orquesta medieval", "Pedro Abrisqueta");
		repr.setEvento(danza);
		assertEquals(danza, repr.getEvento());
	}

	@Test
	public void testSetEstadistica() {
		EstadisticaRepresentacion est = new EstadisticaRepresentacion(100, 20);
		repr.setEstadistica(est);
		assertEquals(est, repr.getEstadistica());
	}

	@Test
	public void testIsbutacaOcupada() {
		assertFalse(repr.isbutacaOcupada(b));
	}

	@Test
	public void testEntradasDisponibles() {
		ArrayList<Ticket> tDisp = new ArrayList<>();
		tDisp = repr.getEntradas();
		Ticket t = repr.getEntradas().get(0);
		tDisp.remove(t);
		t.setEntradaBloqueada(true);
		assertEquals(tDisp, repr.entradasDisponibles());
	}

	@Test
	public void testEntradasBloqueadas() {
		Ticket t = repr.getEntradas().get(0);
		t.setEntradaBloqueada(true);
		ArrayList<Ticket> bloqueadas = new ArrayList<>();
		bloqueadas.add(t);
		assertEquals(bloqueadas, repr.entradasBloqueadas());
	}

	@Test
	public void testToString() {
		String s = "Representación del evento " + repr.getEvento().getTitulo() + " el día " + repr.getFecha()
				+ " a la hora " + repr.getHora();

		assertEquals(s, repr.toString());
	}

	@Test
	public void testHorarioDisponible() {
		RepresentacionEvento repr2 = new RepresentacionEvento(fecha, hora, obra);
		assertFalse(repr2.horarioDisponible(fecha, hora));
	}
}
