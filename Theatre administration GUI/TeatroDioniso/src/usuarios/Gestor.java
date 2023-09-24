/**  
 * 
 * @author Olmar Arranz olmar.arranz@estudiante.uam.es 
 * @author Cristina Madrigal cristina.madrigalb@estudiante.uam.es 
 * @author Eduardo Terrés Caballero eduardo.terres@estudiante.uam.es
 *  
 */
package usuarios;

import java.util.*;
import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.Random;

import entradas.*;
import obras.*;
import teatro.*;
import zonas.*;

public class Gestor extends UsuarioRegistrado {
    private static final long serialVersionUID = 7632266993398169239L;

    /**
     * Constructor de Gestor
     * 
     */
    public Gestor() {
        /* Correo y contraseña del gestor */
        super("gestor@correo.com", "UAMeps");
        Teatro.setGestor(this);
    }

    /**
     * Este método imprime por pantalla la estadística de los eventos
     * 
     * @return cadena que representa la estadística de los eventos
     */
    public Object[][] consultarEstadisticasEventos() {
        ArrayList<Evento> eventos = Teatro.getEventos();
        Object[][] l = new Object[eventos.size()][3];
        double sumRec = 0;
        int sumOcup = 0;
        int i = 0;

        for (Evento evento : eventos) {
            sumOcup = evento.calcularOcupacion();
            sumRec = evento.calcularRecaudacion();
            l[i++] = Arrays.asList(evento.getTitulo(), String.valueOf(sumRec), String.valueOf(sumOcup)).toArray();
        }
        return l;
    }

    /**
     * Este método imprime por pantalla la estadística de las representaciones de un
     * evento
     * 
     * @param e evento del que obtener la estadística
     * @return cadena que representa las estadísticas de las representaciones de un
     *         evento
     */
    public Object[][] consultarEstadisticasRepresentaciones(Evento e) {
        ArrayList<RepresentacionEvento> representaciones = e.getRepresentaciones();
        Object[][] l = new Object[representaciones.size()][3];
        int i = 0;
        
        for (RepresentacionEvento representacion : representaciones) {
            l[i++] = Arrays.asList(representacion.getFecha()+" a las " + representacion.getHora(),
            		String.valueOf(representacion.getEstadistica().getRecaudacion()),
            		String.valueOf(representacion.getEstadistica().getOcupacion())).toArray();

        }
        return l;
    }

    /**
     * Este método imprime por pantalla la estadística de las zonas del teatro
     * 
     * @return cadena que representa la estadística de las zonas
     */
    public Object[][] consultarEstadisticasZonas() {
        ArrayList<Zona> zonas = Teatro.getZonas();
        Object[][] l = new Object[zonas.size()][2];
        int i = 0;
        
        for (Zona zona : zonas) {
            l[i++] = Arrays.asList(zona.getNombre(), String.valueOf(zona.getEstadistica().getRecaudacion())).toArray();
        }
        return l;
    }

    /**
     * Este método modifica el aforo de una zona simple no numerada
     * 
     * @param z              zona simple no numerada
     * @param reduccionAforo double que representa el porcentaje de reducción
     */
    public void modificarAforo(ZSNoNumerada z, double reduccionAforo) {
    	// En caso de pasar una copia de la zona
    	/*for (Zona z2:Teatro.getZonas()) {
    		if (z2.getID() == z.getID()) {
    	        z.reducirAforo(reduccionAforo);
    		}
    	}*/
    	z.reducirAforo(reduccionAforo);
    }

    /**
     * Este método deshabilita una butaca
     * 
     * @param b   butaca a deshabilitar
     * @param dbt deshabilitación de la butaca
     * @return entero a modo de CdE
     */
    public int deshabilitarButaca(Butaca b, DeshabilitacionButaca dbt) {
        /*
         * Solo se deshabilita la entrada en caso de no estar reservada o comprada en
         * ninguna representacion
         */
        for (Evento e : Teatro.getEventos()) {
            for (RepresentacionEvento r : e.getRepresentaciones()) {
                if (r.isbutacaOcupada(b))
                    return -1;
            }
        }
        b.deshabilitarButaca(dbt);
        return 0;
    }

    /**
     * Este método habilita una butaca
     * 
     * @param b Butaca a habilitar
     * 
     * @return 1 si ya estaba habilitada; 0 en caso contrario
     */
    public int habilitarButaca(Butaca b) {
    	if (b.getHabilitada()) return 1;
        b.habilitarButaca();
        return 0;
    }

    /**
     * Este método deshabilita parcialmente una zona no numerada
     * 
     * @param zs  zona simple no numerada a deshabilitar
     * @param num numero de espacio de aforo a deshabilitar
     * 
     * @return -1 en caso de fallo; 0 en caso contrario
     */
    public int deshabilitarZona(ZSNoNumerada zs, int num) {
    	double porcentaje;
        if (num < 0) {
            return -1;
        }
        // Se comprueba que no se haya vendido ninguna entrada para ningun evento
        for (Evento e : Teatro.getEventos()) {
            for (RepresentacionEvento r : e.getRepresentaciones()) {
                if (r.entradasBloqueadas().size() != 0) {
                    return -1;
                }
            }
        }
    	porcentaje = (double) num / zs.getAforo();
        if (porcentaje > 1) porcentaje = 1;
        zs.reducirAforo(porcentaje);
		return 0;
    }

    /**
     * Este método deshabilita una zona numerada entera
     * 
     * @param zona zona simple numerada a deshabilitar
     * @param dbt  deshabilitacion de la butaca
     * 
     * @return entero a modo de CdE
     */
    public int deshabilitarZona(ZSNumerada zona, DeshabilitacionButaca dbt) {

        // Se comprueba que no se haya vendido ninguna entrada para ningun evento
        for (Evento e : Teatro.getEventos()) {
            for (RepresentacionEvento r : e.getRepresentaciones()) {
                if (r.entradasBloqueadas().size() != 0) {
                    return -1;
                }
            }
        }

        // No CdE ya que ya se ha comprobado que no hay entradas vendidas y es el único
        // caso
        // en el que la deshabilitación daría error
        for (Butaca b : zona.getButacas()) {
            this.deshabilitarButaca(b, dbt);
        }

        return 0;
    }

    /**
     * Este método deshabilita una cantidad de butacas especificada de una zona de
     * asientos numerados
     * 
     * @param zona zona simple numerada
     * @param dbt  deshabilitacion de la butaca
     * @param num  entero con el número de butacas a deshabilitar
     * 
     * @return entero a modo de CdE
     */
    public int deshabilitarZona(ZSNumerada zona, DeshabilitacionButaca dbt, int num) {
        ArrayList<Butaca> butacasAnalizar = new ArrayList<>();
        Butaca butacaAux = null;
        Random random = new Random();
        int size, i;

        // Se comprueba que no se haya vendido ninguna entrada para ningun evento
        for (Evento e : Teatro.getEventos()) {
            for (RepresentacionEvento r : e.getRepresentaciones()) {
                if (r.entradasBloqueadas().size() != 0) {
                    return -1;
                }
            }
        }

        if (num < 0)
            return -1;
        else if (num == 0)
            return 0;
        // Se deshabilita toda la zona
        else if (num >= zona.getnFila() * zona.getnColumna()) {
            return this.deshabilitarZona(zona, dbt);
        }

        // Comprueba si hay butacas deshabilitadas para restringir
        size = zona.getButacasDeshabilitadas().size();

        // Hay más butacas deshabilitadas de las que se quiere en total
        if (size > num || size == num) {
            // Al estar ya deshabilitadas, no se hace nada
            return 0;
        } else if (size > 0 && size < num) {
            size = num - size;
            butacasAnalizar.addAll(zona.getButacasHabilitadas());
        } else { // size == 0
            size = num;
            butacasAnalizar.addAll(zona.getButacas());
        }

        // Toda la zona esta deshabilitada
        if (zona.getButacasHabilitadas().size() == 0) {
            return 0;
        }

        for (i = 0; i < size && butacasAnalizar.size() > 0; i++) {
            butacaAux = butacasAnalizar.get(random.nextInt(butacasAnalizar.size()));
            butacaAux.deshabilitarButaca(dbt);
            butacasAnalizar.remove(butacaAux);
        }

        return 0;
    }

    /**
     * Este método habilita una zona simple numerada
     * 
     * @param zona zona simple numerada a habilitar
     */
    public void habilitarZona(ZSNumerada zona) {
        for (Butaca b : zona.getButacas()) {
            habilitarButaca(b);
        }
    }
    
    /**
     * Este método pospone una representación, enviando una notificacion a los
     * usuarios que tenían entrada
     * 
     * @param r          representación de un evento a posponer
     * @param nuevaFecha fecha en la que se pospone
     * @param nuevaHora  hora en la que se pospone
     * 
     * @return -1 en caso de error; 0 en caso contrario
     */
    public int posponerRepresentacion(RepresentacionEvento r, LocalDate nuevaFecha, LocalTime nuevaHora) {
    	if(nuevaFecha ==  null || nuevaHora == null) return -1;
    	if (nuevaFecha.compareTo(r.getFecha()) < 0 || nuevaFecha.plusDays(1).compareTo(LocalDate.now()) < 0) return -1;
        Notificacion n = new Notificacion("Se ha pospuesto la representacion " + r.getEvento().getTitulo()
                + ", del dia " + r.getFecha().toString() + " a las " + r.getHora().toString() + ". Nueva fecha: "
                + nuevaFecha.toString() + " a las " + nuevaHora.toString() + ".\n");
        r.setHora(nuevaHora);
        r.setFecha(nuevaFecha);
        for (Ticket e : r.getEntradas()) {
            if (e.getUsuario() != null) {
                n.enviarNotificacion(e.getUsuario());
            }
        }
        return 0;
    }

    /**
     * Este método cancela una representación, enviando una notificacion a los
     * usuarios que tenían entrada
     * 
     * @param r representación de un evento a cancelar
     * 
     * @return entero a modo de CdE
     */
    public int cancelarRepresentacion(RepresentacionEvento r) {
        Notificacion n = new Notificacion("Se ha cancelado la representación " + r.getEvento().getTitulo()
                + ", del día " + r.getFecha().toString() + " y hora " + r.getHora().toString() + " \n");

        if (ChronoUnit.DAYS.between(r.getFecha(), LocalDate.now()) >= 0) {
            return -1;
        }
        for (Ticket e : r.getEntradas()) {
            if (e.getUsuario() != null) {
                n.enviarNotificacion(e.getUsuario());
                e.retirar();
            }
        }
        r.getEvento().eliminarRepresentacion(r);

        return 0;
    }

}
