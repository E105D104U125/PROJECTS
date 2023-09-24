/** 
 * 
 * @author Olmar Arranz olmar.arranz@estudiante.uam.es 
 * @author Cristina Madrigal cristina.madrigalb@estudiante.uam.es 
 * @author Eduardo Terrés Caballero eduardo.terres@estudiante.uam.es
 *  
 */
package usuarios;

import obras.*;
import zonas.*;
import entradas.*;

import es.uam.eps.padsof.telecard.*;
import es.uam.eps.padsof.tickets.*;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.time.*;
import java.util.ArrayList;
import java.util.List;

public class UsuarioRegistradoTest {

	private Notificacion nt;
	private UsuarioRegistrado usrr;
	private ObraTeatro obra;
	private ZSNumerada z1;
	private ZSNumerada z2;
	private ZSNoNumerada z3;
	private ArrayList<PrecioZona> pzArray;
	private PrecioZona pz1;
	private PrecioZona pz2;
	private RepresentacionEvento rp1;
	private AbonoAnual anual;

	@Before
	public void setUp() throws Exception {
		nt = new Notificacion("Mensaje de prueba");
		usrr = new UsuarioRegistrado("c@gmail.com", "skc$)=45");

		String[] actores = { "A1", "A2" };

		z3 = new ZSNoNumerada("Zona X", 70, 0);
		z1 = new ZSNumerada("Zona de prueba 1", 10, 10);
		z2 = new ZSNumerada("Zona de prueba 2", 7, 14);
		pzArray = new ArrayList<>();
		pz1 = new PrecioZona(10.5, z1);
		pz2 = new PrecioZona(7.7, z2);
		PrecioZona pz3 = new PrecioZona(4.3, z3);
		pzArray.add(pz1);
		pzArray.add(pz2);
		pzArray.add(pz3);
		obra = new ObraTeatro("Titulo de prueba", 120, "Descripcion X", "X", "Director X", pzArray, actores);
		rp1 = new RepresentacionEvento(LocalDate.now().plusDays(100), LocalTime.now(), obra);

		LocalDate fechaAnual = LocalDate.of(2021, 03, 8);
		anual = new AbonoAnual(100, z1, fechaAnual);
	}

	@Test
	public void testAddNotificacion() {
		ArrayList<Notificacion> ntArray = new ArrayList<>();
		ntArray.add(nt);
		usrr.addNotificacion(nt);
		assertEquals(ntArray, usrr.getNotificaciones());
	}

	@Test
	public void testAddEntrada() {
		Ticket t = rp1.getEntradas().get(0);
		usrr.addEntrada(t);
		assertNotNull(usrr.getEntradasUsuario());
		assertNotEquals(0, usrr.getEntradasUsuario().size());
		assertEquals(t, usrr.getEntradasUsuario().get(0));
	}

	@Test
	public void testAddAbono() {
		usrr.addAbono(anual);
		ArrayList<Abono> ab = new ArrayList<>();
		ab.add(anual);
		assertNotNull(usrr.getAbonosUsuario());
		assertNotEquals(0, usrr.getAbonosUsuario().size());
		assertEquals(ab, usrr.getAbonosUsuario());
	}

	@Test
	public void testAddReserva() {
		Butaca b = new Butaca(2, 1);
		EntradaNumerada entr = new EntradaNumerada(pz1, false, rp1, b);
		ArrayList<Ticket> entradas = new ArrayList<>();
		entradas.add(entr);

		Reserva r = new Reserva(rp1, entradas);
		ArrayList<Reserva> reservas = new ArrayList<>();
		reservas.add(r);
		usrr.addReserva(r);

		assertEquals(reservas, usrr.getReservas());
		assertNotEquals(0, usrr.getSizeReservas());
		assertNotNull(usrr.getReservas());
	}

	@Test
	public void testPagar() {
		boolean b1 = false;
		boolean b2 = false;
		boolean b3 = false;
		try {
			b1 = usrr.pagar("0000000000000000", "Cargo excepcional", 10);
			b2 = usrr.pagar("TX23234242", "Cargo excepcional", 10);
			b3 = usrr.pagar("0000000000000000", "Cargo excepcional", -10);
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
		assertTrue(b1);
		assertFalse(b2);
		assertFalse(b3);
	}

	/*
	 * Incluye comprobacion de getCombinationsContiguas, seleccionCentradaContigua y
	 * clonarButacas
	 */
	@Test
	public void testSeleccionButacas() {
		ArrayList<TipoSeleccion> ts = new ArrayList<>();
		ts.add(TipoSeleccion.CentradaFilaColumna);
		ts.add(TipoSeleccion.CentradaFilaInferior);
		ts.add(TipoSeleccion.CentradaFilaSuperior);
		ts.add(TipoSeleccion.Alejada);
		List<Ticket> tArray;

		for (TipoSeleccion t : ts) {
			tArray = usrr.seleccionButacas(z1, rp1, t, 2);
		}
		// Comprobacion manual de que se eligen las correctas

		// Casos limite
		for (TipoSeleccion t : ts) {
			// Selecciona toda la zona
			tArray = usrr.seleccionButacas(z1, rp1, t, z1.getnColumna() * z1.getnFila());
			assertEquals(z1.getnColumna() * z1.getnFila(), tArray.size());

			// No selecciona ninguna
			tArray = usrr.seleccionButacas(z1, rp1, t, 0);
			assertEquals(0, tArray.size());

			// Selecciona mas entradas de las que hay
			// Consigue el maximo que quedan en la zona
			assertEquals(0, usrr.seleccionButacas(z1, rp1, t, 1 + z1.getnColumna() * z1.getnFila()).size());
			// Se tratara el problema en comprarEntradas
			assertNull(usrr.seleccionButacas(z1, rp1, t, rp1.getSizeEntradas() + 1));
		}
	}

	/**
	 * Incluye las dos funciones comprarEntradas
	 */
	@Test
	public void testComprarEntradas() {
		ArrayList<TipoSeleccion> ts = new ArrayList<>();
		int ctrl1 = -1, ctrl2 = -1, ctrl3 = -1, cont = 0;
		int nEntr = 24, nEntr2 = 2 * z1.getnColumna() * z1.getnFila();
		ts.add(TipoSeleccion.CentradaFilaColumna);
		ts.add(TipoSeleccion.CentradaFilaInferior);
		ts.add(TipoSeleccion.CentradaFilaSuperior);
		ts.add(TipoSeleccion.Alejada);

		for (TipoSeleccion t : ts) {
			cont += 1;
			try {
				ctrl1 = usrr.comprarEntradas(z1, rp1, t, nEntr, "0000000000000000");
				ctrl2 = usrr.comprarEntradas(z1, rp1, t, -nEntr, "0000000000000000");
				ctrl3 = usrr.comprarEntradas(z1, rp1, t, nEntr2, "0000000000000000");
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
			// Ejecucion correcta
			assertNotEquals(-1, ctrl1);
			// Fallo por numero de entradas negativo
			assertEquals(-1, ctrl2);
			// No habia suficientes entradas
			assertEquals(-1, ctrl3);

			// Se comprueba que, tras una compra exitosa, el usuario posee las entradas
			assertEquals(cont * nEntr, usrr.getEntradasUsuario().size());
		}

		// Quedan bloqueadas
		for (Ticket t : usrr.getEntradasUsuario()) {
			assertTrue(t.getEntradaBloqueada());
		}
	}

	@Test
	public void testComprarAbono() {
		int ctrl = -1;
		try {
			ctrl = usrr.comprarAbono(anual, "0000000000000000");
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
		// Ejecucion correcta
		assertNotEquals(-1, ctrl);

		// El usuario adquiere el abono
		assertEquals(anual, usrr.getAbonosUsuario().get(0));
	}

	/**
	 * Tambien comprueba realizarReserva implicitamente
	 */
	@Test
	public void testRealizarReserva() {
		boolean ctrl = false, ctrl2 = false, ctrl3 = false;

		ArrayList<Ticket> entradas = new ArrayList<>();
		entradas.add(rp1.getEntradas().get(0));
		entradas.add(rp1.getEntradas().get(1));

		// Tipo Seleccion
		ctrl = usrr.realizarReserva(z1, rp1, TipoSeleccion.CentradaFilaColumna, 3);
		assertTrue(ctrl);
		assertEquals(3, usrr.getReservas().get(0).getEntradas().size());

		// Zona no numerada
		ctrl2 = usrr.realizarReserva(z3, rp1, 30);
		assertTrue(ctrl2);
		assertEquals(30, usrr.getReservas().get(1).getEntradas().size());

		// Intenta reservar mas entradas de las posibles
		ctrl3 = usrr.realizarReserva(z3, rp1, 1000);
		assertFalse(ctrl3);

		assertNotNull(usrr.getReservas());
		for (Reserva res : usrr.getReservas()) {
			for (Ticket t : res.getEntradas()) {
				assertTrue(t.getEntradaBloqueada());
			}
		}
	}

	@Test
	public void testApuntarListaEspera() {
		usrr.apuntarListaEspera(rp1);

		// Recibe la notificacion
		assertEquals(1, usrr.getNotificaciones().size());
	}

	@Test
	public void testConfirmarReserva() {
		boolean ctrl2 = false;
		int ctrl1 = -1, nEntr = 30;

		ctrl2 = usrr.realizarReserva(z3, rp1, nEntr);
		assertTrue(ctrl2);

		assertEquals(0, usrr.getEntradasUsuario().size());
		assertNotEquals(usrr.getReservas().get(0).getEntradas(), usrr.getEntradasUsuario());

		try {
			ctrl1 = usrr.confirmarReserva(usrr.getReservas().get(0), "0000000000000000");
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
		assertNotEquals(-1, ctrl1);
		assertEquals(nEntr, usrr.getEntradasUsuario().size());

		assertNotNull(usrr.getReservas());
		for (Reserva res : usrr.getReservas()) {
			for (Ticket t : res.getEntradas()) {
				assertTrue(t.getEntradaBloqueada());
			}
		}

		assertEquals(1, usrr.getNotificaciones().size());
		assertEquals(0, usrr.getReservas().size());
	}

	@Test
	public void testCancelarReserva() {
		UsuarioRegistrado usrr2 = new UsuarioRegistrado("2@gmail.com", "aDSA3423");
		boolean ctrl2 = false;

		ctrl2 = usrr.realizarReserva(z3, rp1, (int) z3.getAforoDisponible());
		assertTrue(ctrl2);

		// Existe la reserva
		assertEquals(1, usrr.getReservas().size());

		if (usrr2.realizarReserva(z3, rp1, 10) == false) {
			usrr2.apuntarListaEspera(rp1);
		}

		usrr.cancelarReserva(usrr.getReservas().get(0));

		// Entradas desbloqueadas
		assertEquals(0, usrr.getReservas().size());
		for (Reserva res : usrr.getReservas()) {
			for (Ticket t : res.getEntradas()) {
				assertFalse(t.getEntradaBloqueada());
			}
		}

		// Se avisa a la lista de espera
		assertEquals(2, usrr2.getNotificaciones().size());
	}

}
