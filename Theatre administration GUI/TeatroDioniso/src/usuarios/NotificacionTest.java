/** 
 * 
 * @author Olmar Arranz olmar.arranz@estudiante.uam.es 
 * @author Cristina Madrigal cristina.madrigalb@estudiante.uam.es 
 * @author Eduardo Terrés Caballero eduardo.terres@estudiante.uam.es
 *  
 */
package usuarios;

import java.util.*;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class NotificacionTest {

	private Notificacion n;
	private String s;
	private UsuarioRegistrado u;

	@Before
	public void setUp() throws Exception {
		s = "Notificación de prueba";
		n = new Notificacion(s);
		u = new UsuarioRegistrado("usuario@correo.com", "1234");
	}

	@Test
	public void testMostrarNotificacion() {
		assertEquals(s, n.mostrarNotificacion());
	}

	@Test
	public void testEnviarNotificacion() {
		n.enviarNotificacion(u);
		ArrayList<Notificacion> nl = new ArrayList<>();
		nl.add(n);
		assertEquals(u.getNotificaciones(), nl);
	}

	@Test
	public void testToString() {
		String s = "Notificacion: " + n.mostrarNotificacion();
		assertEquals(s, n.toString());
	}

}
