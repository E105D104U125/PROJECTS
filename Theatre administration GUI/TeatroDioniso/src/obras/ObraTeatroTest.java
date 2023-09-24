/** 
 * 
 * @author Olmar Arranz olmar.arranz@estudiante.uam.es 
 * @author Cristina Madrigal cristina.madrigalb@estudiante.uam.es 
 * @author Eduardo Terrés Caballero eduardo.terres@estudiante.uam.es
 *  
 */
package obras;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import zonas.PrecioZona;
import zonas.ZSNumerada;

public class ObraTeatroTest {
	private ObraTeatro obra;
	private ZSNumerada z1;
	private ZSNumerada z2;
	private ArrayList<PrecioZona> pzArray;
	private PrecioZona pz1;
	private PrecioZona pz2;

	@Before
	public void setUp() throws Exception {
		String[] actores = { "A1", "A2" };

		z1 = new ZSNumerada("Zona de prueba 1", 10, 10);
		z2 = new ZSNumerada("Zona de prueba 2", 7, 14);

		pzArray = new ArrayList<>();
		pz1 = new PrecioZona(10.5, z1);
		pz2 = new PrecioZona(7.7, z2);
		pzArray.add(pz1);
		pzArray.add(pz2);

		obra = new ObraTeatro("Titulo de prueba", 120, "Descripcion X", "X", "Director X", pzArray, actores);
	}

	@Test
	public void testToString() {
		String s = "Título del evento: " + obra.getTitulo() + "\nduración del evento: " + obra.getDuracion()
				+ "\ndescripción del evento: " + obra.getDescripcion() + "\nautor del evento: " + obra.getAutor()
				+ "\ndirector: " + obra.getDirector() + " \nprecios por zona: " + obra.getPreciosZonas()
				+ "\nrepresentaciones del evento: ";
		for (RepresentacionEvento r : obra.getRepresentaciones()) {
			s += "{" + r.getFecha() + " a las " + r.getHora() + ", ";
		}
		if (obra.getRepresentaciones().size() != 0) {
			s = s.substring(0, s.length() - 2);
			s += "}";
		}

		s += ", actores: ";
		for (String actor : obra.getActores()) {
			s += actor + ", ";
		}
		// para que no imprima la última coma y espacio
		s = s.substring(0, s.length() - 2);
		s += ".\n";

		assertEquals(s, obra.toString());
	}

}
