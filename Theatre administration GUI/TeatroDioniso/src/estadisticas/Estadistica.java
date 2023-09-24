/** 
 * 
 * @author Olmar Arranz olmar.arranz@estudiante.uam.es 
 * @author Cristina Madrigal cristina.madrigalb@estudiante.uam.es 
 * @author Eduardo Terres Caballero eduardo.terres@estudiante.uam.es
 *  
 */
package estadisticas;

import teatro.*;

import java.io.Serializable;

/**
 * Clase que representa todos los tipos de estadísticas.
 */
public abstract class Estadistica implements Serializable {
    
    private static final long serialVersionUID = 9146169650571786483L;
    private double rec;

    /**
     * Constructor de la clase Estadistica
     * 
     * @param rec recaudacion
     */
    public Estadistica(double rec) {
        this.setRecaudacion(rec);
        Teatro.addEstadistica(this);
    }

    /**
     * Este metodo devuelve la recaudacion de una estadistica
     * 
     * @return recaudacion de una estadistica
     */
    public double getRecaudacion() {
        return this.rec;
    }

    /**
     * Este metodo asigna una recaudacion a una estadistica
     * 
     * @param rec recaudacion de una estadistica
     * 
     */
    public void setRecaudacion(double rec) {
        if (rec >= 0) {
            this.rec = rec;
        } else {
            this.rec = 0;
        }
    }

    /**
     * Este metodo imprime la informacion de una Estadistica.
     * 
     * @return Cadena que representa este objeto.
     */
    public String toString() {
        return "Recaudacion: " + this.getRecaudacion() + "\n";
    }
}