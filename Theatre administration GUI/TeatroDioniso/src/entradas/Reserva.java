/** 
 * 
 * @author Olmar Arranz olmar.arranz@estudiante.uam.es 
 * @author Cristina Madrigal cristina.madrigalb@estudiante.uam.es 
 * @author Eduardo Terrés Caballero eduardo.terres@estudiante.uam.es
 *  
 */
package entradas;

import java.io.Serializable;
import java.time.*;
import java.util.*;

import obras.*;

/**
 * Clase que representa todos los tipos de reservas.
 */
public class Reserva implements Serializable {
    private static final long serialVersionUID = -3897289786116539630L;
    private LocalDate fLimite;
    private ArrayList<Ticket> entradas;
    private RepresentacionEvento representacion;

    /**
     * Constructor de la clase Reserva
     * 
     * @param representacion representación de la reserva
     * @param entradas2      lista de entradas
     */
    public Reserva(RepresentacionEvento representacion, ArrayList<Ticket> entradas2) {
        this.representacion = representacion;
        this.entradas = entradas2;
        this.fLimite = representacion.getFecha().minusDays(2);
        for (Ticket e : entradas2) {
            e.setReserva(this);
            e.setEntradaBloqueada(true);
        }
    }

    /**
     * Este método devuelve la fecha límite de una reserva
     * 
     * @return Fecha que representa la fecha límite de una reserva
     */
    public LocalDate getFLimite() {
        return this.fLimite;
    }

    /**
     * Este método devuelve las entradas de una reserva
     * 
     * @return Lista de entradas de una reserva
     */
    public ArrayList<Ticket> getEntradas() {
        return this.entradas;
    }

    /**
     * Este método añade una entrada de una reserva
     * 
     * @param entrada entrada a añadir en la reserva
     */
    public void addEntrada(Ticket entrada) {
        if (entrada == null) {
            return;
        }
        this.entradas.add(entrada);
    }

    /**
     * Este método devuelve el tamaño de la lista de las entradas de una reserva
     * 
     * @return Entero con el tamaño de la lista de entradas de una reserva
     */
    public int getSizeEntradas() {
        return this.entradas.size();
    }

    /**
     * Este método devuelve la representación de una reserva
     * 
     * @return Representacion de una reserva
     */
    public RepresentacionEvento getRepresentacion() {
        return this.representacion;
    }

    /**
     * Este método asigna una representación a una reserva
     * 
     * @param representacion representación de una reserva
     */
    public void setRepresentacion(RepresentacionEvento representacion) {
        if (representacion == null) {
            return;
        }
        this.representacion = representacion;
    }

    /**
     * Este método calcula el precio de una reserva
     * 
     * @return Precio total de la reserva
     */
    public double calcularPrecio() {
        Double precio = 0.0;
        for (Ticket e : this.entradas) {
            precio += e.getPrecioZona().getPrecio();
        }
        return precio;
    }

    /**
     * Este método imprime la información de una Reserva.
     * 
     * @return Cadena que representa este objeto.
     */
    @Override
    public String toString() {
        String s;
        s = "Reserva para la representación: " + this.getRepresentacion();
        s += "\nformada por las entradas: \n";
        for (Ticket e : this.getEntradas()) {
            s += e.toString();
        }
        return s;
    }

}
