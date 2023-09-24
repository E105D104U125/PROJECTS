/** 
 * 
 * @author Olmar Arranz olmar.arranz@estudiante.uam.es 
 * @author Cristina Madrigal cristina.madrigalb@estudiante.uam.es 
 * @author Eduardo Terrés Caballero eduardo.terres@estudiante.uam.es
 *  
 */
package obras;

import zonas.PrecioZona;
import zonas.ZSNumerada;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class DanzaTest {
	private Danza danza;
	private ZSNumerada z1;
	private ZSNumerada z2;
	private ArrayList<PrecioZona> pzArray;
	private PrecioZona pz1;
	private PrecioZona pz2;

	@Before
	public void setUp() throws Exception {
		String[] bailarines = { "A1", "A2" };

		z1 = new ZSNumerada("Zona de prueba 1", 10, 10);
		z2 = new ZSNumerada("Zona de prueba 2", 7, 14);

		pzArray = new ArrayList<>();
		pz1 = new PrecioZona(10.5, z1);
		pz2 = new PrecioZona(7.7, z2);
		pzArray.add(pz1);
		pzArray.add(pz2);

		danza = new Danza("Titulo de prueba", 120, "Descripcion X", "X", "Director X", pzArray, bailarines, "Grupo X",
				"Director X");
	}

	@Test
	public void testToString() {
		String s = "Título del evento: " + danza.getTitulo() + "\nduración del evento: " + danza.getDuracion()
				+ "\ndescripción del evento: " + danza.getDescripcion() + "\nautor del evento: " + danza.getAutor()
				+ "\ndirector: " + danza.getDirector() + " \nprecios por zona: " + danza.getPreciosZonas()
				+ "\nrepresentaciones del evento: ";
		for (RepresentacionEvento r : danza.getRepresentaciones()) {
			s += "{" + r.getFecha() + " a las " + r.getHora() + ", ";
		}
		if (danza.getRepresentaciones().size() != 0) {
			s = s.substring(0, s.length() - 2);
			s += "}";
		}

		s += ", bailarines: ";
		for (String bailarin : danza.getBailarines()) {
			s += bailarin + ", ";
		}
		// para que no imprima la última coma y espacio
		s = s.substring(0, s.length() - 2);
		s += "\ngrupo musical: " + danza.getGrupoMusical() + "\ndirector de la orquesta: " + danza.getDirectorOrquesta()
				+ ".\n";

		assertEquals(s, danza.toString());
	}

	/*
	 * @Test void testDanza() { fail("Not yet implemented"); }
	 * 
	 * @Test void testGetBailarines() { fail("Not yet implemented"); }
	 * 
	 * @Test void testSetOrquesta() { fail("Not yet implemented"); }
	 * 
	 * @Test void testGetGrupoMusical() { fail("Not yet implemented"); }
	 * 
	 * @Test void testSetGrupoMusical() { fail("Not yet implemented"); }
	 * 
	 * @Test void testGetDirectorOrquesta() { fail("Not yet implemented"); }
	 * 
	 * @Test void testSetDirectorOrquesta() { fail("Not yet implemented"); }
	 */

}
