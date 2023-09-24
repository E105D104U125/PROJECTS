/** 
 * 
 * @author Olmar Arranz olmar.arranz@estudiante.uam.es 
 * @author Cristina Madrigal cristina.madrigalb@estudiante.uam.es 
 * @author Eduardo Terrés Caballero eduardo.terres@estudiante.uam.es
 *  
 */

package zonas;

import java.io.Serializable;

import estadisticas.*;
import teatro.Teatro;

/**
 * Clase que representa todos los tipos de zonas.
 */
public abstract class Zona implements Serializable {

    private static final long serialVersionUID = 1726222535975929791L;
    private long id;
    private static int numZona = 0;
    private String nombre;
    private EstadisticaZona estadistica = new EstadisticaZona(0);

    /**
     * Constructor de la clase Zona
     * 
     * @param nombre Nombre de la zona
     */
    public Zona(String nombre) {
        numZona++;
        this.id = numZona;
        this.nombre = nombre;
        Teatro.addZona(this);
    }

    /**
     * Este método devuelve el ID de una zona
     * 
     * @return ID de la zona
     */
    public long getID() {
        return this.id;
    }

    /**
     * Este método devuelve el nombre de una zona
     * 
     * @return nombre de la Zona
     */
    public String getNombre() {
        return this.nombre;
    }

    /**
     * Asigna el valor de nombre de la zona al parámetro de entrada nombre
     * 
     * @param nombre Nombre de la zona
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Este método devuelve la estadística de una zona
     * 
     * @return estadística de una representación
     */
    public EstadisticaZona getEstadistica() {
        return this.estadistica;
    }

    /**
     * Este método asigna una estadística a una zona
     * 
     * @param estadistica estadística de una zona
     */
    public void setEstadistica(EstadisticaZona estadistica) {
        this.estadistica = estadistica;
    }

    /**
     * Este método devuelve el número de zonas creadas
     * 
     * @return Entero que representa el número de zonas
     */
    public static int getNumZona() {
        return numZona;
    }

    /**
     * Este método imprime la información de una Zona.
     * 
     * @return Cadena que representa este objeto.
     */
    @Override
    public String toString() {
        String s;
        s = "Nombre de la zona: " + getNombre() + ", identificador: " + getID() + ", estadística asociada a la zona: "
                + getEstadistica();
        return s;
    }

    /**
     * Este método actualiza una estadística
     *
     * @param rec recaudación de una zona
     */
    public void actualizarEstadistica(double rec) {
        if (rec <= 0) {
            return;
        }
        this.getEstadistica().setRecaudacion(this.getEstadistica().getRecaudacion() + rec);
    }

}
