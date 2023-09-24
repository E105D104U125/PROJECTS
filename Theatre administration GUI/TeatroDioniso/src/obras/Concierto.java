/** 
 * 
 * @author Olmar Arranz olmar.arranz@estudiante.uam.es 
 * @author Cristina Madrigal cristina.madrigalb@estudiante.uam.es 
 * @author Eduardo Terrés Caballero eduardo.terres@estudiante.uam.es
 *  
 */

package obras;

import java.util.*;

import zonas.*;

/**
 * Clase que representa todos los tipos de conciertos.
 */
public class Concierto extends Evento {

    private static final long serialVersionUID = -5901044671420353671L;
    private String orquesta;
    private String[] interpretes;
    private String programa;

    /**
     * Constructor de Concierto
     * 
     * @param titulo       título del evento
     * @param duracion     duración del evento
     * @param descripcion  descripción del evento
     * @param autor        autor del evento
     * @param director     director del evento
     * @param preciosZonas precios por zona del evento
     * @param orquesta     orquesta del concierto
     * @param interpretes  intérpretes del concierto
     * @param programa     programa del concierto
     */
    public Concierto(String titulo, double duracion, String descripcion, String autor, String director,
            ArrayList<PrecioZona> preciosZonas, String orquesta, String interpretes[], String programa) {
        super(titulo, duracion, descripcion, autor, director, preciosZonas);
        this.orquesta = orquesta;
        this.interpretes = interpretes;
        this.programa = programa;
    }

    /**
     * Este método devuelve la orquesta de un concierto
     * 
     * @return orquesta del concierto
     */
    public String getOrquesta() {
        return this.orquesta;
    }

    /**
     * Este método asigna una orquesta a un concierto
     * 
     * @param orquesta orquesta de un concierto
     */
    public void setOrquesta(String orquesta) {
        this.orquesta = orquesta;
    }

    /**
     * Este método devuelve los intérpretes de un concierto
     * 
     * @return intérpretes del concierto
     */
    public String[] getInterpretes() {
        return this.interpretes;
    }

    /**
     * Este método asigna intérpretes a un concierto
     * 
     * @param interpretes intérpretes de un concierto
     */
    public void setInterpretes(String[] interpretes) {
        this.interpretes = interpretes;
    }

    /**
     * Este método devuelve el programa de un concierto
     * 
     * @return programa del concierto
     */
    public String getPrograma() {
        return this.programa;
    }

    /**
     * Este método asigna el programa de un concierto
     * 
     * @param programa programa de un concierto
     */
    public void setPrograma(String programa) {
        this.programa = programa;
    }

    /**
     * Devuelve una cadena de caracteres con informaci�n espec�fica
     * de concierto
     * 
     * @return informacion especifica del concierto
     */
    @Override
    public String toStringEspecifico() {
    	String s = "<html>Int�rpretes:";
    	for (String interprete : this.getInterpretes()) {
            s += "<br>"+interprete;
        }
    	s += "<br>Programa: " + this.getPrograma() + "<br> Orquesta:" + this.getOrquesta();
    	return s + "</html>";
    }
}
