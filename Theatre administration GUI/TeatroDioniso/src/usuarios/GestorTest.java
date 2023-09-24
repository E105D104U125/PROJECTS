/** 
 * 
 * @author Olmar Arranz olmar.arranz@estudiante.uam.es 
 * @author Cristina Madrigal cristina.madrigalb@estudiante.uam.es 
 * @author Eduardo Terrés Caballero eduardo.terres@estudiante.uam.es
 *  
 */
package usuarios;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import obras.*;
import teatro.Teatro;
import zonas.*;
import java.util.*;
import java.time.*;
import entradas.*;
import es.uam.eps.padsof.telecard.*;
import es.uam.eps.padsof.tickets.*;

public class GestorTest {

	private Gestor g;
	private ZSNoNumerada zn;
	private Butaca b;
	private DeshabilitacionButaca dbt;
	private ObraTeatro obra;
	private PrecioZona precPista, precPlatea;
	private ZSNoNumerada pista;
	private UsuarioRegistrado u;
	private ArrayList<Ticket> entradas;
	private Notificacion n, n2;
	private LocalDate nuevaFecha;
	private LocalTime nuevaHora;
	private ArrayList<Notificacion> nots;
	private Danza danza;
	private ZSNumerada z1, z2, z3, platea;
	private ArrayList<PrecioZona> pzArray, precios;
	private PrecioZona pz3, pz2;
	private RepresentacionEvento repr2, repr;

	@Before
	public void setUp() throws Exception {
		/*
		 * Se eliminan los posibles eventos para poder ejecutar todos los test seguidos
		 * (Teatro tiene metodos estaticos)
		 */
		if (Teatro.getEventos().size() > 0) {
			Teatro.getEventos().clear();
		}
		if (Teatro.getEstadisticas().size() > 0) {
			Teatro.getEstadisticas().clear();
		}
		if (Teatro.getZonas().size() > 0) {
			Teatro.getZonas().clear();
		}
		if (Teatro.getBtcDeshabilitadas().size() > 0) {
			Teatro.getBtcDeshabilitadas().clear();
		}

		zn = new ZSNoNumerada("Zona no numerada", 50, 0);
		z1 = new ZSNumerada("Zona numerada 1", 5, 5);

		b = new Butaca(2, 3);

		LocalDate d1 = LocalDate.of(2021, 05, 20);
		LocalDate d2 = LocalDate.of(2021, 05, 27);
		dbt = new DeshabilitacionButaca("Prueba de deshabilitación", d1, d2);

		g = new Gestor();

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
		LocalDate fecha = LocalDate.of(2021, 05, 12);
		LocalTime hora = LocalTime.of(20, 00, 00);
		LocalTime hora2 = LocalTime.of(18, 00, 00);
		repr = new RepresentacionEvento(fecha, hora, obra);
		repr2 = new RepresentacionEvento(fecha, hora2, obra);

		entradas = new ArrayList<>();

		u = new UsuarioRegistrado("usuario@correo.com", "0124");

		nuevaFecha = LocalDate.of(2021, 05, 13);
		nuevaHora = LocalTime.of(18, 00, 00);

		n = new Notificacion("Se ha pospuesto la representación " + repr.getEvento().getTitulo() + ", del día "
				+ repr.getFecha().toString() + " a las " + repr.getHora().toString() + ". Nueva fecha: "
				+ nuevaFecha.toString() + " a las " + nuevaHora.toString() + ".\n");

		nots = new ArrayList<>();
		nots.add(n);

		n2 = new Notificacion("Se ha cancelado la representación " + repr.getEvento().getTitulo() + ", del día "
				+ repr.getFecha().toString() + " y hora " + repr.getHora().toString() + " \n");

		String[] bailarines = { "A1", "A2" };

		z3 = new ZSNumerada("Zona de prueba 3", 10, 10);
		z2 = new ZSNumerada("Zona de prueba 2", 7, 14);

		pzArray = new ArrayList<>();
		pz3 = new PrecioZona(10.5, z3);
		pz2 = new PrecioZona(7.7, z2);
		pzArray.add(pz3);
		pzArray.add(pz2);

		danza = new Danza("Titulo de prueba", 120, "Descripcion X", "X", "Director X", pzArray, bailarines, "Grupo X",
				"Director X");

	}

	@Test
	public void testConsultarEstadisticasEvento() {
		int cont = 0;
		for (Ticket t : repr.getEntradas()) {
			if (cont < 6) {
				t.comprarTicket(u);
			}
			cont++;
		}
		String str = "El evento " + obra.getTitulo() + " ha tenido una recaudación de " + obra.calcularRecaudacion()
				+ " € y una ocupación de " + obra.calcularOcupacion() + " entradas vendidas.\n" + "El evento "
				+ danza.getTitulo() + " ha tenido una recaudación de " + danza.calcularRecaudacion()
				+ " € y una ocupación de " + danza.calcularOcupacion() + " entradas vendidas.\n";

		assertEquals(str, g.consultarEstadisticasEventos());
	}

	@Test
	public void testConsultarEstadisticasRepresentaciones() {

		int cont = 0;
		for (Ticket t : repr.getEntradas()) {
			if (cont < 6) {
				t.comprarTicket(u);
			}
			cont++;
		}

		String str = "La representación del día " + repr.getFecha() + " y la hora " + repr.getHora()
				+ " ha tenido una recaudación de " + repr.getEstadistica().getRecaudacion() + "€ y una ocupación de "
				+ repr.getEstadistica().getOcupacion() + " entradas vendidas.\n" + "La representación del día "
				+ repr2.getFecha() + " y la hora " + repr2.getHora() + " ha tenido una recaudación de "
				+ repr2.getEstadistica().getRecaudacion() + "€ y una ocupación de "
				+ repr2.getEstadistica().getOcupacion() + " entradas vendidas.\n";
		assertEquals(str, g.consultarEstadisticasRepresentaciones(obra));
	}

	@Test
	public void testConsultarEstadisticasZonas() {

		String str = "La zona de " + zn.getNombre() + " ha recaudado " + zn.getEstadistica().getRecaudacion() + " €.\n";
		str += "La zona de " + z1.getNombre() + " ha recaudado " + z1.getEstadistica().getRecaudacion() + " €.\n";
		str += "La zona de " + pista.getNombre() + " ha recaudado " + pista.getEstadistica().getRecaudacion() + " €.\n";
		str += "La zona de " + platea.getNombre() + " ha recaudado " + platea.getEstadistica().getRecaudacion()
				+ " €.\n";
		str += "La zona de " + z3.getNombre() + " ha recaudado " + z3.getEstadistica().getRecaudacion() + " €.\n";
		str += "La zona de " + z2.getNombre() + " ha recaudado " + z2.getEstadistica().getRecaudacion() + " €.\n";

		assertEquals(str, g.consultarEstadisticasZonas());
	}

	@Test
	public void testModificarAforo() {
		int aforoPrev = zn.getAforo();
		g.modificarAforo(zn, 0.5);
		assertEquals(aforoPrev * 0.5, zn.getAforoDisponible(), 0);
	}

	@Test
	public void testDeshabilitarButaca() {

		Butaca butacaAux = platea.getButacas().get(0);
		assertEquals(0, g.deshabilitarButaca(butacaAux, dbt));

		// Una butaca cualquiera
		for (Ticket e : repr.getEntradas()) {
			if (e.getPrecioZona().getZona() == platea) {
				((EntradaNumerada) e).setEntradaBloqueada(true);
				butacaAux = ((EntradaNumerada) e).getButaca();
				break;
			}
		}

		g.deshabilitarButaca(butacaAux, dbt);
		assertEquals(-1, g.deshabilitarButaca(butacaAux, dbt));

	}

	@Test
	public void testHabilitarButaca() {
		b.setHabilitada(false);
		g.habilitarButaca(b);
		assertTrue(b.getHabilitada());
	}

	@Test
	public void testDeshabilitarZona() {

		g.deshabilitarZona(z1, dbt);
		for (Butaca btc : z1.getButacas()) {
			assertFalse(btc.getHabilitada());
		}

	}

	@Test
	public void testDeshabilitarZonaCantidad() {
		g.deshabilitarZona(zn, 20);
		int aforoDisp = zn.getAforo() - 20;
		assertEquals(aforoDisp, zn.getAforoDisponible(), 0);

	}

	@Test
	public void testDeshabilitarZonaNumCantidad() {
		// Deshabilitar una capacidad mayor a la de la zona
		g.deshabilitarZona(platea, dbt, 25);
		for (Butaca btc : platea.getButacas()) {
			assertFalse(btc.getHabilitada());
		}

		// Si ya había alguna butaca deshabilitada, deshabilita las restantes
		Butaca btc = z1.getButacas().get(0);
		btc.deshabilitarButaca(dbt);
		g.deshabilitarZona(z1, dbt, 2);
		assertEquals(2, z1.getButacasDeshabilitadas().size());

		// Deshabilitar 4 butacas de una zona sin que haya ninguna deshabilitada
		// previamente y sin entradas vendidas
		g.deshabilitarZona(z3, dbt, 4);
		assertEquals(4, z3.getButacasDeshabilitadas().size());

		// Si hay entradas vendidas no se puede deshabiltiar la zona
		u.realizarReserva(platea, repr, TipoSeleccion.CentradaFilaColumna, 3);
		assertEquals(-1, g.deshabilitarZona(platea, dbt, 3));
	}

	@Test
	public void testHabilitarZona() {
		g.habilitarZona(z1);
		for (Butaca btc : z1.getButacas()) {
			assertTrue(btc.getHabilitada());
		}
	}

	@Test
	public void testPosponerRepresentacion() {

		Ticket e = repr.getEntradas().get(0);
		entradas.add(e);

		try {
			u.comprarEntradas(entradas, "0000000000000000");
		} catch (NonExistentFileException e1) {
			System.err.print(e1);
			return;
		} catch (UnsupportedImageTypeException e2) {
			System.err.print(e2);
			return;
		} catch (InvalidCardNumberException e3) {
			System.err.print(e3);
			return;
		} catch (FailedInternetConnectionException e4) {
			System.err.print(e4);
			return;
		} catch (OrderRejectedException e5) {
			System.err.print(e5);
			return;
		}
		g.posponerRepresentacion(repr, nuevaFecha, nuevaHora);
		assertEquals(nuevaFecha, repr.getFecha());
		assertEquals(nuevaHora, repr.getHora());
		assertEquals(nots.get(0).mostrarNotificacion(), u.getNotificaciones().get(0).mostrarNotificacion());
	}

	@Test
	public void testCancelarRepresentacion() {

		Ticket e = repr.getEntradas().get(0);
		entradas.add(e);

		try {
			u.comprarEntradas(entradas, "0000000000000000");
		} catch (NonExistentFileException e1) {
			System.err.print(e1);
			return;
		} catch (UnsupportedImageTypeException e2) {
			System.err.print(e2);
			return;
		} catch (InvalidCardNumberException e3) {
			System.err.print(e3);
			return;
		} catch (FailedInternetConnectionException e4) {
			System.err.print(e4);
			return;
		} catch (OrderRejectedException e5) {
			System.err.print(e5);
			return;
		}

		/*
		 * Este caso solo se puede comprobar cuando ha llegado el dia de la
		 * representacion y se intenta cancelar. La llamada a setFecha no modificara la
		 * fecha de la repre- sentacion a la de hoy por el control de errores.
		 */
		/*
		 * repr.setFecha(LocalDate.now()); assertEquals(-1,
		 * g.cancelarRepresentacion(repr));
		 */

		nots.remove(n);
		nots.add(n2);

		repr.setFecha(LocalDate.of(2021, 05, 12));
		assertEquals(0, g.cancelarRepresentacion(repr));

		assertEquals(nots.get(0).mostrarNotificacion(), u.getNotificaciones().get(0).mostrarNotificacion());

		assertEquals(1, obra.getRepresentaciones().size());

	}

}
