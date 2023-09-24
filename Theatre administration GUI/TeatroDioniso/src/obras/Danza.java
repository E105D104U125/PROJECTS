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
 * Clase que representa todos los tipos de danzas.
 */
public class Danza extends Evento {

    /**
     *
     */
    private static final long serialVersionUID = 3077843700827257019L;
    private String[] bailarines;
    private String grupoMusical;
    private String directorOrquesta;

    /**
     * Constructor de Danza
     * 
     * @param titulo           título del evento
     * @param duracion         duración del evento
     * @param descripcion      descripción del evento
     * @param autor            autor del evento
     * @param director         director del evento
     * @param preciosZonas     precios por zona del evento
     * @param bailarines       bailarenes de la danza
     * @param grupoMusical     grupo musical de la danza
     * @param directorOrquesta director de la orquesta de la danza
     */
    public Danza(String titulo, double duracion, String descripcion, String autor, String director,
            ArrayList<PrecioZona> preciosZonas, String bailarines[], String grupoMusical, String directorOrquesta) {
        super(titulo, duracion, descripcion, autor, director, preciosZonas);
        this.bailarines = bailarines;
        this.grupoMusical = grupoMusical;
        this.directorOrquesta = directorOrquesta;
    }

    /**
     * Este método devuelve los bailarines de una danza
     * 
     * @return bailarines de una danza
     */
    public String[] getBailarines() {
        return this.bailarines;
    }

    /**
     * Este método asigna bailarines a una danza
     * 
     * @param bailarines bailarines de una danza
     */
    public void setOrquesta(String[] bailarines) {
        this.bailarines = bailarines;
    }

    /**
     * Este método devuelve el grupo musical de una danza
     * 
     * @return grupo musical de una danza
     */
    public String getGrupoMusical() {
        return this.grupoMusical;
    }

    /**
     * Este método asigna un grupo musical a una danza
     * 
     * @param grupoMusical grupo musical de una danza
     */
    public void setGrupoMusical(String grupoMusical) {
        this.grupoMusical = grupoMusical;
    }

    /**
     * Este método devuelve el director de orquesta de una danza
     * 
     * @return director de orquesta de una danza
     */
    public String getDirectorOrquesta() {
        return this.directorOrquesta;
    }

    /**
     * Este método asigna un director de orquesta a una danza
     * 
     * @param directorOrquesta directo de orquesta de una danza
     */
    public void setDirectorOrquesta(String directorOrquesta) {
        this.directorOrquesta = directorOrquesta;
    }
    
    /**
     * Devuelve una cadena de caracteres con informaci�n espec�fica
     * de la danza
     * 
     * @return informacion especifica de la danza
     */
    @Override
    public String toStringEspecifico() {
    	String s = "<html>Bailarines:";
    	for (String bailarin : this.getBailarines()) {
            s += "<br>"+bailarin;
        }
    	s += "<br>Grupo musical: " + this.getGrupoMusical() + "<br>Director de la orquesta: " + this.getDirectorOrquesta();
    	return s + "</html>";
    }
}
