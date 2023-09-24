/** 
 * 
 * @author Olmar Arranz olmar.arranz@estudiante.uam.es 
 * @author Cristina Madrigal cristina.madrigalb@estudiante.uam.es 
 * @author Eduardo Terrés Caballero eduardo.terres@estudiante.uam.es
 *  
 */
package obras;

import java.io.Serializable;
import java.time.*;
import java.util.*;
import java.time.temporal.ChronoUnit;

import teatro.*;
import entradas.*;
import estadisticas.*;
import usuarios.*;
import zonas.*;

/**
 * Clase que representa todos los tipos de representaciones de eventos.
 */
public class RepresentacionEvento implements Serializable {

    private static final long serialVersionUID = -489997710510915208L;
    private LocalDate fecha;
    private LocalTime hora;
    private Evento evento;
    private ArrayList<Ticket> entradas = new ArrayList<>();
    private ArrayList<UsuarioRegistrado> listaDeEspera = new ArrayList<>();
    private EstadisticaRepresentacion estadistica = new EstadisticaRepresentacion(0, 0);
    // Numero de personas que avisa en la lista de espera
    private static int avisoListaEspera = 5;

    /**
     * Constructor de RepresentacionEvento
     * 
     * @param fecha  fecha de la representación
     * @param hora   hora de la representación
     * @param evento evento al que pertenece la representación
     */

    public RepresentacionEvento(LocalDate fecha, LocalTime hora, Evento evento) {
        this.setFecha(fecha);
        this.hora = hora;
        this.evento = evento;
        this.entradas = generarEntradas();
        evento.addRepresentacion(this);
    }

    /**
     * Este método genera entradas de una zona simple numerada
     * 
     * @param z  zona simple numerada
     * @param pz precio de la zona
     * 
     * @return lista de tickets generados
     */
    private ArrayList<Ticket> generarEntradasZona(ZSNumerada z, PrecioZona pz) {
        ArrayList<Ticket> entradas = new ArrayList<>();
        Ticket ticketAux;

        for (Butaca b : z.getButacasHabilitadas()) {
            ticketAux = new EntradaNumerada(pz, false, this, b);
            entradas.add(ticketAux);
        }
        return entradas;
    }

    /**
     * Este método genera entradas de una zona simple no numerada
     * 
     * @param z  zona simple numerada
     * @param pz precio de la zona
     * 
     * @return lista de tickets generados
     */
    private ArrayList<Ticket> generarEntradasZona(ZSNoNumerada z, PrecioZona pz) {
        ArrayList<Ticket> entradas = new ArrayList<>();
        Ticket ticketAux;
        int i;

        for (i = 0; i < z.getAforoDisponible(); i++) {
            ticketAux = new Ticket(pz, false, this);
            entradas.add(ticketAux);
        }

        return entradas;
    }

    /**
     * Este método genera entradas de una zona compuesta
     * 
     * @param z  zona compuesta
     * @param pz precio de la zona
     * 
     * @return lista de tickets generados
     */
    private ArrayList<Ticket> generarEntradasZona(ZonaCompuesta z, PrecioZona pz) {
        ArrayList<Ticket> entradas = new ArrayList<>();

        for (Zona zona : z.getZonas()) {
            if (zona instanceof ZSNumerada) {
                entradas.addAll(this.generarEntradasZona((ZSNumerada) zona, pz));
            } else if (zona instanceof ZSNoNumerada) {
                entradas.addAll(this.generarEntradasZona((ZSNoNumerada) zona, pz));
            } else if (zona instanceof ZonaCompuesta) {
                entradas.addAll(this.generarEntradasZona((ZonaCompuesta) zona, pz));
            }
        }

        return entradas;
    }

    /**
     * Crea todas las entradas
     * 
     * @return lista de tickets generados
     */
    public ArrayList<Ticket> generarEntradas() {
        ArrayList<Ticket> entradas = new ArrayList<>();
        for (PrecioZona pz : this.getEvento().getPreciosZonas()) {
            Zona z = pz.getZona();
            if (z instanceof ZSNumerada) {
                entradas.addAll(this.generarEntradasZona((ZSNumerada) z, pz));
            } else if (z instanceof ZSNoNumerada) {
                entradas.addAll(this.generarEntradasZona((ZSNoNumerada) z, pz));
            } else if (z instanceof ZonaCompuesta) {
                entradas.addAll(this.generarEntradasZona((ZonaCompuesta) z, pz));
            }
        }
        return entradas;
    }

    /**
     * Este método devuelve la fecha de una representación
     * 
     * @return fecha de una representación
     */
    public LocalDate getFecha() {
        return this.fecha;
    }

    /**
     * Este método asigna una fecha a una representación
     * 
     * @param fecha fecha de una representación
     */
    public void setFecha(LocalDate fecha) {
        if (ChronoUnit.DAYS.between(fecha, LocalDate.now()) >= 0) {
            return;
        }
        this.fecha = fecha;
    }

    /**
     * Este método devuelve la hora de una representación
     * 
     * @return hora de una representación
     */
    public LocalTime getHora() {
        return this.hora;
    }

    /**
     * Este método asigna una fecha a una representación
     * 
     * @param hora hora de una representación
     */
    public void setHora(LocalTime hora) {
        this.hora = hora;
    }

    /**
     * Este método devuelve las entradas de una representación
     * 
     * @return entradas de una representación
     */
    public ArrayList<Ticket> getEntradas() {
        return this.entradas;
    }

    /**
     * Este método añade una entrada de una representación
     * 
     * @param entrada Ticket a añadir a una representación
     */
    public void addEntrada(Ticket entrada) {
        if (!this.entradas.contains(entrada)) {
            this.entradas.add(entrada);
        }
    }

    /**
     * Este método indica cuántas entradas hay de una representacion
     * 
     * @return Tamaño de la lista de entradas de una representación
     */
    public int getSizeEntradas() {
        return entradas.size();
    }

    /**
     * Este método devuelve las entradas de una representación
     * 
     * @return entradas de una representación
     */
    public ArrayList<UsuarioRegistrado> getListaDeEspera() {
        return this.listaDeEspera;
    }

    /**
     * Este método añade un usuario a la lista de espera de una representación
     * 
     * @param usuario Usuario a añadir a la lista de espera de una representación
     */
    public void addListaDeEspera(UsuarioRegistrado usuario) {
        if (!this.listaDeEspera.contains(usuario)) {
            listaDeEspera.add(usuario);
        }
    }

    /**
     * Este método indica cuántos usuarios hay en una lista de espera de una
     * representación
     * 
     * @return Tamaño de la lista de usuarios en la lista de espera de una
     *         representación
     */
    public int getSizeListaDeEspera() {
        return listaDeEspera.size();
    }

    /**
     * Este método devuelve la fecha de una representación
     * 
     * @return fecha de una representación
     */
    public Evento getEvento() {
        return this.evento;
    }

    /**
     * Este método asigna un evento a una representaci�n
     * 
     * @param evento evento de una representación
     */
    public void setEvento(Evento evento) {
        this.evento.getRepresentaciones().remove(this);
        this.evento = evento;
        this.evento.addRepresentacion(this);
    }

    /**
     * Este método devuelve la estadística de una representación
     * 
     * @return estadística de una representación
     */
    public EstadisticaRepresentacion getEstadistica() {
        return this.estadistica;
    }

    /**
     * Este método asigna una estadística a una representación
     * 
     * @param estadistica estadística de una representación
     */
    public void setEstadistica(EstadisticaRepresentacion estadistica) {
        this.estadistica = estadistica;
    }

    /**
     * Este método devuelve el estado ocupado de una butaca
     * 
     * @param b butaca a comprobar
     * 
     * @return true si la entrada numerada asociada a la butaca se encuentra
     *         ocupada; false en caso contrario
     */
    public boolean isbutacaOcupada(Butaca b) {
        // Aunque una entrada este reservada se puede deshabilitar
        // Si la entrada esta bloqueada NO se puede deshabilitar
        for (Ticket e : this.entradas) {
            if (e instanceof EntradaNumerada) {
                if (((EntradaNumerada) e).getButaca().equals(b) && e.getEntradaBloqueada() == true) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Este método avisa a todos los usuarios de la lista de espera de una
     * representación enviándoles una notificación
     */
    public void avisarListaDeEspera() {
        int cont = 0;
        Notificacion n = new Notificacion("La(s) entrada(s) para la cual(es) estaba en lista de espera del evento "
                + this.getEvento().getTitulo() + " el día " + this.getFecha() + " a la hora " + this.getHora()
                + " están a disposición de compra.\n");

        if (this.getSizeListaDeEspera() != 0) {
            for (UsuarioRegistrado u : this.getListaDeEspera()) {
                if (cont > avisoListaEspera) {
                    break;
                }
                n.enviarNotificacion(u);
                cont += 1;
            }
        }

        return;
    }

    /**
     * Este método devuelve las entradas disponibles de una representación
     * 
     * @return Lista con las entradas disponibles de una representación
     */
    public ArrayList<Ticket> entradasDisponibles() {
        ArrayList<Ticket> disp = new ArrayList<>();
        for (Ticket e : this.entradas) {
            if (e.getEntradaBloqueada() == false) {
                disp.add(e);
            }
        }
        return disp;
    }

    /**
     * Este método devuelve las entradas bloqueadas (o reservadas) de una
     * representación
     * 
     * @return Lista con las entradas bloqueadas de una representación
     */
    public ArrayList<Ticket> entradasBloqueadas() {
        ArrayList<Ticket> disp = new ArrayList<>();
        for (Ticket e : this.entradas) {
            if (e.getEntradaBloqueada()) {
                disp.add(e);
            }
        }
        return disp;
    }

    /**
     * Este método imprime la información de una Representación.
     * 
     * @return Cadena que representa este objeto.
     */
    @Override
    public String toString() {
        String s = "Representacion del evento " + this.getEvento().getTitulo() + " el dia " + this.getFecha()
                + " a la hora " + this.getHora();
        return s;
    }

    /**
     * Este método devuelve la disponibilidad de un horario
     * 
     * @param f fecha en la que comprobar la disponibilidad
     * @param h hora en la que comprobar la disponibilidad
     * @return booleano que devuelve true si no hay ninguna representación para esa
     *         feha y hora; y false en caso contrario
     */
    public boolean horarioDisponible(LocalDate f, LocalTime h) {
        for (Evento e : Teatro.getEventos()) {
            for (RepresentacionEvento r : e.getRepresentaciones()) {
                if (r.getFecha() == f && r.getHora() == h) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Este método actualiza la estadística de una representación
     * 
     * @param rec recaudacion
     * @param oc  ocupacion
     */
    public void actualizarEstadistica(double rec, int oc) {
        if (rec <= 0 || oc <= 0) {
            return;
        }
        this.getEstadistica().setOcupacion(this.getEstadistica().getOcupacion() + oc);
        this.getEstadistica().setRecaudacion(this.getEstadistica().getRecaudacion() + rec);
    }
    
    /**
     * Dice si una butaca est� disponible o no sabiendo la fila y la columna
     * @param fila fila
     * @param columna columna
     * @param z zona
     * 
     * @return boolean que devuelve true si se puede comprar/reservar la butaca
     */
    public boolean btcDisponible(int fila, int columna, ZSNumerada z){
    	
    	for(Ticket t: getEntradas()) {
    		if(t instanceof EntradaNumerada && t.getPrecioZona().getZona() == z) {
    			if (((EntradaNumerada) t).getButaca().getFila() == fila && ((EntradaNumerada) t).getButaca().getColumna() == columna) {
    				if(((EntradaNumerada) t).getButaca().getHabilitada()==true && t.getEntradaBloqueada()==false) {
        				return true;
    				}else {
    					return false;
    				}
    			}
    		}
    	}
    	return false;
    }
    
    /**
     * M�todo para obtener la entrada de una zona indicando fila y columna de la butaca
     * @param fila fila 
     * @param columna columna
     * @param z zona
     * 
     * @return entrada asociada a esa fila y a esa columna en la zona indicada
     */
    public EntradaNumerada ticketNum(int fila, int columna, ZSNumerada z) {
    	
    	for(Ticket t: getEntradas()) {
    		if(t instanceof EntradaNumerada && t.getPrecioZona().getZona()==z) {
    			if (((EntradaNumerada) t).getButaca().getFila()==fila && ((EntradaNumerada) t).getButaca().getColumna()==columna) {
    				return (EntradaNumerada)t;
    			}
    		}
    	}
    	return null;
    }

}
