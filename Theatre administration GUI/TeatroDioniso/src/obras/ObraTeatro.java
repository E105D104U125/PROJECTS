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
 * Clase que representa todos los tipos de obras de teatro.
 */
public class ObraTeatro extends Evento {

    private static final long serialVersionUID = 3887428907995839939L;
    private String[] actores;

    /**
     * Constructor de ObraTeatro
     * 
     * @param titulo       tÃ­tulo del evento
     * @param duracion     duraciÃ³n del evento
     * @param descripcion  descripciÃ³n del evento
     * @param autor        autor del evento
     * @param director     director del evento
     * @param preciosZonas precios por zona del evento
     * @param actores      actores de la obra de teatro
     */
    public ObraTeatro(String titulo, double duracion, String descripcion, String autor, String director,
            ArrayList<PrecioZona> preciosZonas, String actores[]) {
        super(titulo, duracion, descripcion, autor, director, preciosZonas);
        this.actores = actores;
    }

    /**
     * Este metodo devuelve los actores de una obra de teatro
     * 
     * @return actores de una obra de teatro
     */
    public String[] getActores() {
        return this.actores;
    }

    /**
     * Este metodo asigna actores a una obra de teatro
     * 
     * @param actores actores de una obra de teatro
     */
    public void setActores(String[] actores) {
        this.actores = actores;
    }

    /**
     * Devuelve una cadena de caracteres con información específica
     * de la obra
     * 
     * @return informacion especifica de la obra de teatro
     */
    @Override
    public String toStringEspecifico() {
    	String s = "<html>Actores:";
    	for (String actor : this.getActores()) {
            s += "<br>"+actor;
        }
    	return s+"</html>";
    }

}
