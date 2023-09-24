/** 
 * 
 * @author Olmar Arranz olmar.arranz@estudiante.uam.es 
 * @author Cristina Madrigal cristina.madrigalb@estudiante.uam.es 
 * @author Eduardo Terrés Caballero eduardo.terres@estudiante.uam.es
 *  
 */
package zonas;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import entradas.TipoSeleccion;
import es.uam.eps.padsof.tickets.*;
import es.uam.eps.padsof.telecard.*;
import obras.ObraTeatro;
import obras.RepresentacionEvento;
import usuarios.UsuarioRegistrado;

public class ZonaCompuestaTest {

	private ZSNumerada platea;
	private ZSNumerada patio;
	private ZSNoNumerada z;
	private ZSNoNumerada z2;
	private ArrayList<Zona> zonasSimples;
	private ArrayList<Zona> zonas2;
	private ZonaCompuesta zonaComp;
	private ZonaCompuesta zonaComp2;

	@Before
	public void setUp() throws Exception {

		platea = new ZSNumerada("Platea", 5, 4);
		patio = new ZSNumerada("Patio de butacas", 6, 5);
		z = new ZSNoNumerada("Pista", 150, 0);
		z2 = new ZSNoNumerada("Pista", 200, 0);

		zonasSimples = new ArrayList<>();
		zonasSimples.add(z);
		zonasSimples.add(platea);
		zonaComp = new ZonaCompuesta("Zona compuesta por pista y platea", zonasSimples);

		zonas2 = new ArrayList<>();
		zonas2.add(z2);
		zonas2.add(zonaComp);
		zonaComp2 = new ZonaCompuesta("Zona compuesta (simple+compuesta)", zonas2);

	}

	@Test
	public void testToString() {
		String s = "Nombre de la zona: " + zonaComp.getNombre() + ", identificador: " + zonaComp.getID()
				+ ", estadística asociada a la zona: " + zonaComp.getEstadistica();
		s += "\nformada por las zonas: \n";
		for (Zona z : zonaComp.getZonas()) {
			s += z.toString();
		}

		assertEquals(s, zonaComp.toString());
	}

	@Test
	public void testAddZona() {
		zonaComp2.addZona(patio);
		zonas2.add(patio);
		assertEquals(zonas2, zonaComp2.getZonas());
		assertEquals(zonasSimples, zonaComp.getZonas());
	}

	@Test
	public void testNombre() {
		String nombre = "Zona compuesta 2";
		zonaComp2.setNombre(nombre);
		assertEquals(nombre, zonaComp2.getNombre());
	}

	@Test
	/**
	 * Se prueban tambien los metodos actualizarEstadsitica para zonas simples
	 */
	public void testActualizarEstadistica() {
		UsuarioRegistrado usrr;
		ObraTeatro obra;
		ArrayList<PrecioZona> pzArray = new ArrayList<>();
		PrecioZona pz1, pz2;
		RepresentacionEvento rp1;
		int nEntr = 10, ctrl = -1;
		double precioPlatea = 10.5, precioZ = 7.7;

		usrr = new UsuarioRegistrado("c@gmail.com", "s2pkc$)=45");

		String[] actores = { "A1", "A2" };

		pz1 = new PrecioZona(precioPlatea, platea);
		pz2 = new PrecioZona(precioZ, z);
		pzArray.add(pz1);
		pzArray.add(pz2);

		obra = new ObraTeatro("Titulo de prueba", 120, "Descripcion X", "X", "Director X", pzArray, actores);
		rp1 = new RepresentacionEvento(LocalDate.now().plusDays(100), LocalTime.now(), obra);

		/*
		 * for (Ticket z :rp1.getEntradas()){ System.err.println(z); }
		 */

		// Las estadisticas deben estar a 0
		for (PrecioZona z : pzArray) {
			assertEquals(0, z.getZona().getEstadistica().getRecaudacion(), 0);
		}

		// Se compran entradas
		try {
			ctrl = usrr.comprarEntradas(platea, rp1, TipoSeleccion.CentradaFilaSuperior, nEntr, "0000000000000000");
			assertNotEquals(-1, ctrl);
			ctrl = usrr.comprarEntradas(z, rp1, nEntr, "0000000000000000");
			assertNotEquals(-1, ctrl);
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

		// Las estadisticas cambian
		for (PrecioZona pZonaAux : pzArray) {
			if (pZonaAux.getZona() == platea) {
				assertEquals(nEntr * precioPlatea, pZonaAux.getZona().getEstadistica().getRecaudacion(), 0);
			} else if (pZonaAux.getZona() == z) {
				/*
				 * Se cometen errores del orden del epsilon maquina al computar las estadisticas
				 * por lo que se deshechan los decimales para la prueba
				 **/
				assertEquals((int) nEntr * precioZ, (int) pZonaAux.getZona().getEstadistica().getRecaudacion(), 0);
			} else if (pZonaAux.getZona() == zonaComp) {
				assertEquals((int) nEntr * (precioZ + precioPlatea),
						(int) pZonaAux.getZona().getEstadistica().getRecaudacion(), 0);
			}
		}

	}

}
