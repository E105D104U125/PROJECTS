/** 
 * 
 * @author Olmar Arranz olmar.arranz@estudiante.uam.es 
 * @author Cristina Madrigal cristina.madrigalb@estudiante.uam.es 
 * @author Eduardo Terrés Caballero eduardo.terres@estudiante.uam.es
 *  
 */
package usuarios;

import obras.*;
import teatro.Teatro;
import zonas.*;
import entradas.*;
import java.time.*;
import java.util.*;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class UsuarioTest {

	private Usuario u;
	private ArrayList<Evento> eventos;
	private Danza ev2;
	private ObraTeatro ev1;
	private ZSNoNumerada pista;
	private ZSNumerada platea;
	private PrecioZona precPista;
	private PrecioZona precPlatea;
	private AbonoAnual anual;
	private AbonoCiclo ac;
	private RepresentacionEvento repr1;
	private RepresentacionEvento repr2;
	private ArrayList<RepresentacionEvento> representaciones;
	private ArrayList<Abono> abonos;

	@Before
	public void setUp() throws Exception {
		if (!Teatro.getAbonos().isEmpty()) {
			Teatro.getAbonos().clear();
		}
		if (!Teatro.getEventos().isEmpty()) {
			Teatro.getEventos().clear();
		}

		String[] actores = { "Pilar Avila", "Alexia Lorrio" };
		pista = new ZSNoNumerada("Pista2", 150, 0.5);
		platea = new ZSNumerada("Platea", 5, 4);
		precPista = new PrecioZona(20, pista);
		precPlatea = new PrecioZona(30.40, platea);
		ArrayList<PrecioZona> precios = new ArrayList<>();
		precios.add(precPista);
		precios.add(precPlatea);

		eventos = new ArrayList<Evento>();
		ev1 = new ObraTeatro("La casa de Bernarda Alba", 100, "Evento de prueba", "Federico Garci­a Lorca",
				"Óscar Olmeda", precios, actores);
		ev2 = new Danza("Baile de Salón", 60, "Evento de prueba 2", "Paquito", "Juan", precios, actores, "Director",
				"Orquesta de acompañamiento");

		eventos.add(ev1);
		eventos.add(ev2);

		representaciones = new ArrayList<>();
		LocalDate fechaRepr = LocalDate.of(2021, 05, 12);
		LocalTime horaRepr = LocalTime.of(17, 00, 00);
		LocalTime horaRepr2 = LocalTime.of(20, 00, 00);
		repr1 = new RepresentacionEvento(fechaRepr, horaRepr, ev1);
		repr2 = new RepresentacionEvento(fechaRepr, horaRepr2, ev1);
		representaciones.add(repr1);
		representaciones.add(repr2);

		u = new Usuario();

		LocalDate fechaAnual = LocalDate.of(2021, 03, 8);
		anual = new AbonoAnual(100, pista, fechaAnual);
		Ciclo ciclo = new Ciclo("Ciclo", "Prueba", eventos);
		ac = new AbonoCiclo(120, pista, 0.1, ciclo);
		abonos = new ArrayList<>();
		abonos.add(anual);
		abonos.add(ac);

	}

	@Test
	public void testMostrarEventos() {
		assertEquals(eventos, u.mostrarEventos());
	}

	@Test
	public void testMostrarRepresentaciones() {
		assertEquals(representaciones, u.mostrarRepresentaciones(ev1));
	}

	@Test
	public void testMostrarAbonos() {
		// La ejecucion de este test debe ser individual
		// en caso de que las variables estaticas de Teatro
		// almacenen informacion de ejecuciones previas de otros tests
		assertEquals(abonos, u.mostrarAbonos());
	}

}
