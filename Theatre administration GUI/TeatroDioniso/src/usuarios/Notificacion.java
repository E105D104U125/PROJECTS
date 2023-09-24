/** 
 * 
 * @author Olmar Arranz olmar.arranz@estudiante.uam.es 
 * @author Cristina Madrigal cristina.madrigalb@estudiante.uam.es 
 * @author Eduardo Terrés Caballero eduardo.terres@estudiante.uam.es
 *  
 */

package usuarios;

import java.io.Serializable;

/**
 * Clase que representa todas las notificaciones
 */
public class Notificacion implements Serializable {
    private static final long serialVersionUID = -8326926176188938342L;
    private String mensaje;

    /**
     * Constructor de Notificacion
     * 
     * @param mensaje cadena con el texto de la notificación
     */
    public Notificacion(String mensaje) {
        this.mensaje = mensaje;
    }

    /**
     * Este método devuelve la notificación
     * 
     * @return Cadena que representa la notificación
     */
    public String mostrarNotificacion() {
        return this.mensaje;
    }

    /**
     * Este método añade una notificación a la lista de notificaciones de un usuario
     * registrado
     * 
     * @param u Usuario registrado al que se le envía la notificación.
     */
    public void enviarNotificacion(UsuarioRegistrado u) {
        u.addNotificacion(this);

    }

    /**
     * Este método imprime la información de una Notificación.
     * 
     * @return Cadena que representa este objeto.
     */
    @Override
    public String toString() {
        return "Notificacion: " + this.mostrarNotificacion();
    }

}
