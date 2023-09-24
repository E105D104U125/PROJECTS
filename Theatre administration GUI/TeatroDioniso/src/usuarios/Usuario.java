/** 
 * 
 * @author Olmar Arranz olmar.arranz@estudiante.uam.es 
 * @author Cristina Madrigal cristina.madrigalb@estudiante.uam.es 
 * @author Eduardo Terrés Caballero eduardo.terres@estudiante.uam.es
 *  
 */
package usuarios;

import obras.*;
import teatro.*;
import entradas.*;

import java.io.Serializable;
import java.util.*;

/**
 * Esta clase representa todos los usuarios.
 */
public class Usuario implements Serializable {

    private static final long serialVersionUID = 4786818118409193713L;

    /**
     * Constructor de Usuario
     * 
     */
    public Usuario() {

    }

    /**
     * Este método devuelve los eventos de un teatro.
     * 
     * @return Lista de eventos de un teatro.
     */
    public ArrayList<Evento> mostrarEventos() {
        return Teatro.getEventos();
    }

    /**
     * Este método devuelve las representaciones de un teatro.
     * 
     * @param e evento
     * @return Lista de representaciones de un teatro.
     */
    public ArrayList<RepresentacionEvento> mostrarRepresentaciones(Evento e) {
        return e.getRepresentaciones();
    }

    /**
     * Este método devuelve los abonos de un teatro.
     * 
     * @return Lista de eventos de un teatro.
     */
    public ArrayList<Abono> mostrarAbonos() {
        return Teatro.getAbonos();
    }

    /**
     * @param correo   Cadena con el correo del Usuario a registrar
     * @param password Cadena con la contraseña del Usuario a registrar
     * 
     * @return Usuario registrado que se crea o null en caso de error
     */
    public static UsuarioRegistrado registrarUsuario(String correo, String password) {
        for (UsuarioRegistrado u : Teatro.getUsuarios()) {
            if (u.getCorreo().equals(correo)) {
                return null;
            }
        }
        UsuarioRegistrado ur = new UsuarioRegistrado(correo, password);
        return ur;
    }

}