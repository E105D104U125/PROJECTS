/** 
 * 
 * @author Olmar Arranz olmar.arranz@estudiante.uam.es 
 * @author Cristina Madrigal cristina.madrigalb@estudiante.uam.es 
 * @author Eduardo Terrés Caballero eduardo.terres@estudiante.uam.es
 *  
 */
package zonas;

/**
 * Clase que representa todos los tipos de zonas simples.
 */
public abstract class ZonaSimple extends Zona {

    private static final long serialVersionUID = 3590527649874039343L;

    /**
     * Constructor ZonaSimple
     * 
     * @param nombre Nombre de la zona
     */
    public ZonaSimple(String nombre) {
        super(nombre);
    }

}
