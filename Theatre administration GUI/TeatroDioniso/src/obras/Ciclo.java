/** 
 * 
 * @author Olmar Arranz olmar.arranz@estudiante.uam.es 
 * @author Cristina Madrigal cristina.madrigalb@estudiante.uam.es 
 * @author Eduardo Terrés Caballero eduardo.terres@estudiante.uam.es
 *  
 */

package obras;

import java.io.Serializable;
import java.util.*;

import entradas.*;
import teatro.*;

/**
 * Clase que representa todos los tipos de ciclos.
 */
public class Ciclo implements Serializable {

    private static final long serialVersionUID = -6470581003983556514L;
    private String nombre;
    private String descripcion;
    private ArrayList<Evento> eventos = new ArrayList<>();
    private ArrayList<AbonoCiclo> abonosCiclo = new ArrayList<>();

    public Ciclo(String nombre, String descripcion, ArrayList<Evento> eventos) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.eventos = eventos;
        Teatro.addCiclo(this);
    }

    /**
     * Este método devuelve el nombre del ciclo
     * 
     * @return Cadena que representa el nombre del ciclo
     */
    public String getNombre() {
        return this.nombre;
    }

    /**
     * Este método asigna un nombre a un ciclo
     * 
     * @param nombre nombre de un ciclo
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Este método asigna una descripción a un ciclo
     * 
     * @param descripcion descripción de un ciclo
     */
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    /**
     * Este método devuelve la descrición del ciclo
     * 
     * @return Cadena que representa la descripcion del ciclo
     */
    public String getDescripcion() {
        return this.descripcion;
    }

    /**
     * Este método devuelve los abonos de este ciclo
     * 
     * @return Lista de abonos de ciclo
     */
    public ArrayList<AbonoCiclo> getAbonosCiclo() {
        return this.abonosCiclo;
    }

    /**
     * Este método añade un abono a un ciclo
     * 
     * @param abono abono a añadir al ciclo
     */
    public void addAbonosCiclo(AbonoCiclo abono) {
        if (abono == null) {
            return;
        }
        if (!this.abonosCiclo.contains(abono)) {
            this.abonosCiclo.add(abono);
        }
    }

    /**
     * Este método devuelve el tamaño de la lista de los abonos de un ciclo
     * 
     * @return Entero con el tamaño de la lista de los abonos de un ciclo
     */
    public int getSizeAbonosCiclo() {
        return this.abonosCiclo.size();
    }

    /**
     * Este método devuelve los eventos de este ciclo
     * 
     * @return Lista de eventos de ciclo
     */
    public ArrayList<Evento> getEventos() {
        return this.eventos;
    }

    /**
     * Este método añade un evetno a un ciclo
     * 
     * @param evento evento a añadir al ciclo
     */
    public void addEventos(Evento evento) {
        if (evento == null) {
            return;
        }
        if (!this.eventos.contains(evento)) {
            this.eventos.add(evento);
        }
    }

    /**
     * Este método devuelve el tamaño de la lista de los eventos de un ciclo
     * 
     * @return Entero con el tamaño de la lista de los eventos de un ciclo
     */
    public int getSizeEventos() {
        return this.eventos.size();
    }

    /**
     * Este método imprime la información de una EstadísticaZona.
     * 
     * @return Cadena que representa este objeto.
     */
    @Override
    @Deprecated
    public String toString() {
        String s = "Nombre del ciclo: " + getNombre() + ", descripción del ciclo: " + getDescripcion() + ".\n";
        s += "Ciclo formado por los eventos: ";
        for (Evento e : getEventos()) {
            s += e.getTitulo();
        }
        s += ".\n";
        s += "Abonos del ciclo: ";
        for (AbonoCiclo ac : getAbonosCiclo()) {
            s += ac.toString();
        }
        s += ".\n";
        return s;
    }
}
