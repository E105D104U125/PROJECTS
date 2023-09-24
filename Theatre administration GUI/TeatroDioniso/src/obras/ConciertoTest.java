/** 
 * 
 * @author Olmar Arranz olmar.arranz@estudiante.uam.es 
 * @author Cristina Madrigal cristina.madrigalb@estudiante.uam.es 
 * @author Eduardo Terrés Caballero eduardo.terres@estudiante.uam.es
 *  
 */

package obras;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import zonas.PrecioZona;
import zonas.ZSNumerada;

public class ConciertoTest {
	private ZSNumerada z1;
	private ZSNumerada z2;
	private ArrayList<PrecioZona> pzArray;
	private PrecioZona pz1;
	private PrecioZona pz2;
	private Concierto concierto;
	private RepresentacionEvento rp;
	private RepresentacionEvento rp2;

	@Before
	public void setUp() throws Exception {
		String[] interpretes = { "A1", "A2" };

		z1 = new ZSNumerada("Zona de prueba 1", 10, 10);
		z2 = new ZSNumerada("Zona de prueba 2", 7, 14);

		pzArray = new ArrayList<>();
		pz1 = new PrecioZona(10.5, z1);
		pz2 = new PrecioZona(7.7, z2);
		pzArray.add(pz1);
		pzArray.add(pz2);

		concierto = new Concierto("Titulo de prueba", 120, "Descripcion X", "X", "Director X", pzArray, "Orquesta X",
				interpretes, "Programa X");

		rp = new RepresentacionEvento(LocalDate.now().plusDays(100), LocalTime.now(), concierto);
		rp2 = new RepresentacionEvento(LocalDate.now().plusDays(80), LocalTime.now(), concierto);
	}

	@Test
	public void testToString() {
		String s = "Título del evento: " + concierto.getTitulo() + "\nduración del evento: " + concierto.getDuracion()
				+ "\ndescripción del evento: " + concierto.getDescripcion() + "\nautor del evento: "
				+ concierto.getAutor() + "\ndirector: " + concierto.getDirector() + " \nprecios por zona: "
				+ concierto.getPreciosZonas() + "\nrepresentaciones del evento: ";
		for (RepresentacionEvento r : concierto.getRepresentaciones()) {
			s += "{" + r.getFecha() + " a las " + r.getHora() + ", ";
		}
		if (concierto.getRepresentaciones().size() != 0) {
			s = s.substring(0, s.length() - 2);
			s += "}";
		}

		s += ", orquesta: " + concierto.getOrquesta();
		s += ", intérpretes: ";
		for (String interprete : concierto.getInterpretes()) {
			s += interprete + ", ";
		}
		// para que no imprima la última coma y espacio
		s = s.substring(0, s.length() - 2);

		s += ", programa: " + concierto.getPrograma() + ".\n";

		assertEquals(s, concierto.toString());
	}

	@Test
	public void testAddPreciosZonas() {
		ZSNumerada z3 = new ZSNumerada("Zona de prueba 3", 3, 4);
		PrecioZona pz3 = new PrecioZona(20.1, z3);

		ArrayList<PrecioZona> pz2Array = new ArrayList<>();
		pz2Array.addAll(pzArray);
		pz2Array.add(pz3);

		concierto.addPreciosZonas(pz3);
		assertEquals(concierto.getPreciosZonas(), pz2Array);

		/* No se pueden insertar copias */
		concierto.addPreciosZonas(pz3);
		assertEquals(concierto.getPreciosZonas(), pz2Array);
	}

	@Test
	public void testGetSizePreciosZonas() {
		assertEquals(concierto.getPreciosZonas().size(), pzArray.size());
	}

	@Test
	public void testAddRepresentacion() {
		ArrayList<RepresentacionEvento> rpArray = new ArrayList<>();
		rpArray.add(rp);
		rpArray.add(rp2);

		concierto.addRepresentacion(rp);
		concierto.addRepresentacion(rp2);

		assertEquals(concierto.getRepresentaciones(), rpArray);

		/* No se pueden insertar copias */
		concierto.addRepresentacion(rp2);
		assertEquals(concierto.getRepresentaciones(), rpArray);
	}

	@Test
	public void testEliminarRepresentacion() {
		ArrayList<RepresentacionEvento> rpArray = new ArrayList<>();
		rpArray.add(rp);

		concierto.addRepresentacion(rp);
		concierto.addRepresentacion(rp2);

		concierto.eliminarRepresentacion(rp2);

		assertEquals(concierto.getRepresentaciones(), rpArray);

		/* Si no se encuentra, no sucede nada */
		concierto.eliminarRepresentacion(rp2);
		assertEquals(concierto.getRepresentaciones(), rpArray);
	}

	@Test
	public void testCalcularRecaudacion() {
		double sumRec = 0;
		for (RepresentacionEvento representacion : concierto.getRepresentaciones()) {
			sumRec += representacion.getEstadistica().getRecaudacion();
		}

		assertEquals(sumRec, concierto.calcularRecaudacion(), 0);
	}

	@Test
	public void testCalcularOcupacion() {
		int sumOc = 0;
		for (RepresentacionEvento representacion : concierto.getRepresentaciones()) {
			sumOc += representacion.getEstadistica().getOcupacion();
		}

		assertEquals(sumOc, concierto.calcularOcupacion());
	}

}
