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

import teatro.Teatro;
import zonas.*;

/**
 * Clase que representa todos los tipos de eventos.
 */
public abstract class Evento implements Serializable {

    private static final long serialVersionUID = 281265086020302921L;
    private String titulo;
    private double duracion;
    private String descripcion;
    private String autor;
    private String director;
    private ArrayList<PrecioZona> preciosZonas = new ArrayList<>();
    private ArrayList<RepresentacionEvento> representaciones = new ArrayList<>();

    /**
     * Constructor de Evento
     * 
     * @param titulo       título del evento
     * @param duracion     duración del evento
     * @param descripcion  descripción del evento
     * @param autor        autor del evento
     * @param director     director del evento
     * @param preciosZonas precios por zona del evento
     */
    public Evento(String titulo, double duracion, String descripcion, String autor, String director,
            ArrayList<PrecioZona> preciosZonas) {
        this.titulo = titulo;
        this.duracion = duracion;
        this.descripcion = descripcion;
        this.autor = autor;
        this.director = director;
        this.preciosZonas = preciosZonas;
        Teatro.addEvento(this);
    }

    /**
     * Este método devuelve el título del evento
     * 
     * @return título del evento
     */
    public String getTitulo() {
        return this.titulo;
    }

    /**
     * Este método asigna un título a un evento
     * 
     * @param titulo título del evento
     */
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    /**
     * Este método devuelve la duración del evento
     * 
     * @return duración del evento
     */
    public double getDuracion() {
        return this.duracion;
    }

    /**
     * Este método asigna la duración a un evento
     * 
     * @param duracion duración del evento
     */
    public void setDuracion(double duracion) {
        if (duracion <= 0) {
            return;
        }
        this.duracion = duracion;
    }

    /**
     * Este método devuelve la descripción del evento
     * 
     * @return descripción del evento
     */
    public String getDescripcion() {
        return this.descripcion;
    }

    /**
     * Este método asigna un título a un evento
     * 
     * @param descripcion descripción del evento
     */
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    /**
     * Este método devuelve el autor del evento
     * 
     * @return autor del evento
     */
    public String getAutor() {
        return this.autor;
    }

    /**
     * Este método asigna un autor a un evento
     * 
     * @param autor autor del evento
     */
    public void setAutor(String autor) {
        this.autor = autor;
    }

    /**
     * Este método devuelve el director del evento
     * 
     * @return director del evento
     */
    public String getDirector() {
        return this.director;
    }

    /**
     * Este método asigna un director a un evento
     * 
     * @param director director del evento
     */
    public void setDirector(String director) {
        this.director = director;
    }

    /**
     * Este método devuelve los precios por zona del evento
     * 
     * @return precios por zona del evento
     */
    public ArrayList<PrecioZona> getPreciosZonas() {
        return this.preciosZonas;
    }

    /**
     * Este método asigna precios por zona a un evento
     * 
     * @param precioZona precios por zona del evento
     */
    public void addPreciosZonas(PrecioZona precioZona) {
        if (precioZona == null) {
            return;
        }
        if (!this.preciosZonas.contains(precioZona)) {
            this.preciosZonas.add(precioZona);
        }
    }

    /**
     * Este metodo indica cuántos precios por zona hay.
     * 
     * @return Tamaño de la lista de precios por zona.
     */
    public int getSizePreciosZonas() {
        return preciosZonas.size();
    }

    /**
     * Este metodo devuelve las representaciones de un evento.
     * 
     * @return Lista de representaciones de un teatro.
     */
    public ArrayList<RepresentacionEvento> getRepresentaciones() {
        return this.representaciones;
    }

    /**
     * Este método añade una representación a la lista de representaciones de un
     * teatro.
     * 
     * @param representacion Representacion a añadir.
     */
    public void addRepresentacion(RepresentacionEvento representacion) {
        if (representacion == null) {
            return;
        }
        if (representacion.horarioDisponible(representacion.getFecha(), representacion.getHora()) == false) {
            return;
        }
        if (!this.representaciones.contains(representacion)) {
            this.representaciones.add(representacion);
        }
    }

    /**
     * Este metodo indica cuantas representaciones tiene un evento.
     * 
     * @return Tamanio de la lista de representaciones de un teatro.
     */
    public int getSizeRepresentaciones() {
        return representaciones.size();
    }

    /**
     * Este metodo eimina una representación a la lista de representaciones de un
     * teatro.
     * 
     * @param r Representacion a añadir.
     */
    public void eliminarRepresentacion(RepresentacionEvento r) {
        representaciones.remove(r);
    }

    /**
     * Este método imprime la información de un Evento.
     * 
     * @return Cadena que representa este objeto.
     */
    @Override
    public String toString() {
        String s;
        s = "<html>" + this.getTitulo() + "<br>Duracion: " + this.getDuracion() + "<br>Autor: " + this.getAutor() 
        		+ "<br>Director: " + this.getDirector();
        return s + "</html>";
    }
    
    /**
     * Devuelve una cadena de caracteres con informaci�n espec�fica
     * del evento (ignora la informacion proporcionada por toString)
     * 
     * @return informacion especifica del evento
     */
    public abstract String toStringEspecifico();
    
    /**
     * Imprime los precios de las zonas del evento
     * 
     * @return string con los precios por zona
     */
    public String printPreciosZona() {
    	String s = "<html> Precios por zona:<br>";
    	for(PrecioZona z: this.getPreciosZonas()) {
    		s += z.toString() + "<br>";
    	}
    	s += "</html>";
    	return s;
    }

    /**
     * Este método suma todas las recaudaciones de las representaciones de un mismo
     * evento
     * 
     * @return double que representa la recaudación del evento
     */
    public double calcularRecaudacion() {
        ArrayList<RepresentacionEvento> representaciones = this.getRepresentaciones();
        double sumRec = 0;
        for (RepresentacionEvento representacion : representaciones) {
            sumRec += representacion.getEstadistica().getRecaudacion();
        }
        return sumRec;
    }

    /**
     * Este método suma todas las ocupaciones de las representaciones de un mismo
     * evento
     * 
     * @return entero que representa la ocupación del evento
     */
    public int calcularOcupacion() {
        ArrayList<RepresentacionEvento> representaciones = this.getRepresentaciones();
        int sumOc = 0;
        for (RepresentacionEvento representacion : representaciones) {
            sumOc += representacion.getEstadistica().getOcupacion();
        }
        return sumOc;
    }

}
