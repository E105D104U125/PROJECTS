/** 
 * 
 * @author Olmar Arranz olmar.arranz@estudiante.uam.es 
 * @author Cristina Madrigal cristina.madrigalb@estudiante.uam.es 
 * @author Eduardo Terrés Caballero eduardo.terres@estudiante.uam.es
 *  
 */
package entradas;

import java.io.Serializable;

import es.uam.eps.padsof.tickets.ITicketInfo;
import obras.RepresentacionEvento;
import teatro.*;
import usuarios.*;
import zonas.*;

/**
 * Clase que representa todos los tipos de entradas.
 */
public class Ticket implements ITicketInfo, Serializable {

    private static final long serialVersionUID = -1067001881855387477L;
    private boolean bloqueada = false;
    private PrecioZona precioZona;
    private Reserva reserva;
    private UsuarioRegistrado usuario;
    private RepresentacionEvento representacion;
    private static int numTickets = 0;
    private int id;

    /**
     * Constructor de la clase Ticket
     * 
     * @param precioZona     precio de la entrada
     * @param bloqueada      indica si la entrada está o no bloqueada
     * @param representacion representación asociada a la entrada
     */
    public Ticket(PrecioZona precioZona, boolean bloqueada, RepresentacionEvento representacion) {
        numTickets++;
        this.id = numTickets;
        this.setPrecioZona(precioZona);
        this.bloqueada = bloqueada;
        this.setRepresentacion(representacion);
    }

    /**
     * Este método devuelve el número de entradas
     * 
     * @return Número de entradas
     */
    public static int getNumTickets() {
        return numTickets;
    }

    /**
     * Este método devuelve el ID de una entrada
     * 
     * @return ID de la entrada
     */
    // public static int getIdTicket() {
    // return id;
    // }

    /**
     * Este método devuelve el precio de una entrada
     * 
     * @return Precio zona de la entrada
     */
    public PrecioZona getPrecioZona() {
        return this.precioZona;
    }

    /**
     * Este método asigna un precio a una entrada
     * 
     * @param precioZona Precio de la entrada
     */
    public void setPrecioZona(PrecioZona precioZona) {
        if (precioZona == null) {
            return;
        }
        this.precioZona = precioZona;
    }

    /**
     * Este método devuelve el estado bloqueado de una entrada
     * 
     * @return TRUE en caso de reservada; FALSE en caso contrario
     */
    public boolean getEntradaBloqueada() {
        return this.bloqueada;
    }

    /**
     * Este método asigna si una entrada está bloqueada o no
     * 
     * @param bloqueada Booleano con el estado de la entrada
     */
    public void setEntradaBloqueada(boolean bloqueada) {
        this.bloqueada = bloqueada;
    }

    /**
     * Este método devuelve la reserva de una entrada
     * 
     * @return Reserva que representa una reserva de una entrada
     */
    public Reserva getReserva() {
        return this.reserva;
    }

    /**
     * Este método asigna una reserva a una entrada
     * 
     * @param reserva Reserva de la entrada
     */
    public void setReserva(Reserva reserva) {
        if (reserva == null) {
            return;
        }
        this.reserva = reserva;
    }

    /**
     * Este método devuelve la reserva de una entrada
     * 
     * @return Reserva que representa una reserva de una entrada
     */
    public RepresentacionEvento getRepresentacion() {
        return this.representacion;
    }

    /**
     * Este método asigna una reserva a una entrada
     * 
     * @param representacion Representacion de la entrada
     */
    public void setRepresentacion(RepresentacionEvento representacion) {
        if (representacion == null) {
            return;
        }
        this.representacion = representacion;
    }

    /**
     * Este mnétodo asocia una entrada a un usuario
     * 
     * @param usuario usuario a asociar con la entrada
     */
    public void asociarUsuario(UsuarioRegistrado usuario) {
        this.usuario = usuario;
    }

    /**
     * @return Usuario asociado a una entrada
     */
    public UsuarioRegistrado getUsuario() {
        return this.usuario;
    }

    /**
     * Este método bloquea una entrada y la asocia a un usuario
     * 
     * @param u usuario registrado que bloquea la entrada
     */
    public void comprarTicket(UsuarioRegistrado u) {
        if (u == null) {
            return;
        }
        this.asociarUsuario(u);
        this.setEntradaBloqueada(true);
        u.addEntrada(this);
    }

    /**
     * Este método bloquea una entrada y la asocia a un usuario
     * 
     * @param u usuario registrado que bloquea la entrada
     */
    public void reservarTicket(UsuarioRegistrado u) {
        if (u == null) {
            return;
        }
        this.asociarUsuario(u);
        this.setEntradaBloqueada(true);
    }

    /**
     * Este método retira una entrada
     */
    public void retirar() {
        if (this.getUsuario() != null) {
            if (this.getReserva() == null) {
                this.getUsuario().getEntradasUsuario().remove(this);
            } else {
                this.getReserva().getEntradas().remove(this);
                if (this.getReserva().getEntradas().size() == 0) {
                    this.getUsuario().getReservas().remove(this.getReserva());
                }
            }

            this.getUsuario().getEntradasUsuario().remove(this);
        } else {
            this.getRepresentacion().getEntradas().remove(this);
        }
    }

    /**
     * Este método imprime la información de un Ticket.
     * 
     * @return Cadena que representa este objeto.
     */
    @Override
    public String toString() {
        String s;
        s = "Ticket con id: " + this.getIdTicket() + " , para la representación: "
                + this.getRepresentacion().getEvento().getTitulo() + " , con precio: "
                + this.getPrecioZona().getPrecio() + " , ¿se encuentra bloqueada? " + this.getEntradaBloqueada();
        return s;
    }

    /**
     * @return id del ticket
     */
    @Override
    public int getIdTicket() {
        return this.id;
    }

    /**
     * @return nombre del teatro
     */
    @Override
    public String getTheaterName() {
        return Teatro.getNombre();
    }

    /**
     * @return nombre del evento
     */
    @Override
    public String getEventName() {
        return this.representacion.getEvento().getTitulo();
    }

    /**
     * @return fecha del evento
     */
    @Override
    public String getEventDate() {
        String s = "";
        return s + this.representacion.getFecha();
    }

    /**
     * @return asiento
     */
    @Override
    public String getSeatNumber() {

        if (this instanceof EntradaNumerada) {
            EntradaNumerada en = (EntradaNumerada) this;
            return "Zona " + en.getPrecioZona().getZona().getNombre() + ", Fila " + en.getButaca().getFila()
                    + ", Asiento " + en.getButaca().getColumna();
        } else {
            return "Entrada no numerada";
        }
    }

    /**
     * @return imagen del teatro
     */
    @Override
    public String getPicture() {
        /* Imagen */
        return "resources/pic.jpg"; // jpg, gif and png formats are supported;
    }

}