/** 
 * 
 * @author Olmar Arranz olmar.arranz@estudiante.uam.es 
 * @author Cristina Madrigal cristina.madrigalb@estudiante.uam.es 
 * @author Eduardo Terrés Caballero eduardo.terres@estudiante.uam.es
 *  
 */

package zonas;

import java.util.ArrayList;

/**
 * Clase que representa todos los tipos de zonas compuestas.
 */
public class ZonaCompuesta extends Zona {

    private static final long serialVersionUID = 8557842998892887808L;
    private ArrayList<Zona> zonas = new ArrayList<>();

    /**
     * Constructor de la clase ZonaCompuesta
     * 
     * @param nombre Nombre de la zona
     * @param zonas  Lista de zonas
     */
    public ZonaCompuesta(String nombre, ArrayList<Zona> zonas) {
        super(nombre);
        this.zonas = zonas;
    }

    /**
     * Añade la zona al grupo de zonas compuestas
     * 
     * @param z zona a añadir
     */
    public void addZona(Zona z) {
        if (z != null) {
            zonas.add(z);
        }
    }

    /**
     * Este método devuelve las zonas por las cuales está formada la zona compuesta
     * 
     * @return Lista de zonas que forman la zona compuesta
     */
    public ArrayList<Zona> getZonas() {
        return this.zonas;
    }

    /**
     * Este método imprime la información de una Zona Simple Compuesta.
     * 
     * @return Cadena que representa este objeto.
     */
    @Override
    public String toString() {
        String s = super.toString();
        s += "\nformada por las zonas: \n";
        for (Zona z : this.getZonas()) {
            s += z.toString();
        }
        return s;
    }

    /**
     * Este método actualiza una estadística
     *
     * @param rec recaudación de una zona
     */
    @Override
    public void actualizarEstadistica(double rec) {
        for (Zona z : this.getZonas()) {
            this.getEstadistica().setRecaudacion(z.getEstadistica().getRecaudacion() + rec);
        }
    }

}