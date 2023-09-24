/** 
 * 
 * @author Olmar Arranz olmar.arranz@estudiante.uam.es 
 * @author Cristina Madrigal cristina.madrigalb@estudiante.uam.es 
 * @author Eduardo Terrés Caballero eduardo.terres@estudiante.uam.es
 *  
 */
package zonas;

import java.io.Serializable;

/**
 * Clase que representa todos los tipos de precios de las zonas.
 */
public class PrecioZona implements Serializable {

    private static final long serialVersionUID = -5882888126646756349L;
    private double precio;
    private Zona zona;

    /**
     * Constructor de la clase PrecioZona
     * 
     * @param zona   Zona del teatro asociada al precio
     * @param precio double que representa el precio de la zona
     */
    public PrecioZona(double precio, Zona zona) {
        this.precio = precio;
        this.zona = zona;

    }

    /**
     * Este método devuelve el precio asociado a una zona del teatro
     * 
     * @return Double que representa el precio
     */
    public double getPrecio() {
        return this.precio;
    }

    /**
     * Este método asigna el precio asociado a una zona del teatro
     * 
     * @param precio precio de la zona
     */
    public void setPrecio(double precio) {
        if (precio > 0) {
            this.precio = precio;
        } else {
            this.precio = 0;
        }

    }

    /**
     * Este método devuelve la zona del teatro a la cual se asocia el precio
     * 
     * @return Zona zona del teatro con ese precio
     */
    public Zona getZona() {
        return this.zona;
    }

    /**
     * Este método asigna la zona asociada a una zona del teatro
     * 
     * @param zona zona del teatro
     */
    public void setZona(Zona zona) {
        this.zona = zona;
    }

    /**
     * Este método imprime la información de un AbonoAnual.
     * 
     * @return Cadena que representa este objeto.
     */
    @Override
    public String toString() {
        return getZona().getNombre() + ": " + getPrecio() + " �";
    }

}
