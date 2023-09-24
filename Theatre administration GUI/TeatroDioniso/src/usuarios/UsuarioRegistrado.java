/** 
 * 
 * @author Olmar Arranz olmar.arranz@estudiante.uam.es 
 * @author Cristina Madrigal cristina.madrigalb@estudiante.uam.es 
 * @author Eduardo Terrés Caballero eduardo.terres@estudiante.uam.es
 *  
 */
package usuarios;

import java.util.*;
import java.lang.Math;

import entradas.*;
import es.uam.eps.padsof.telecard.*;
import es.uam.eps.padsof.tickets.*;
import obras.*;
import zonas.*;
import teatro.*;

/**
 * Esta clase representa todos los usuarios registrados.
 */
public class UsuarioRegistrado extends Usuario {
    private static final long serialVersionUID = 7837290828176154540L;
    private String correo;
    private String password;

    private ArrayList<Abono> abonos = new ArrayList<>();
    private ArrayList<Notificacion> notificaciones = new ArrayList<>();
    private ArrayList<Ticket> entradas = new ArrayList<>();
    private ArrayList<Reserva> reservas = new ArrayList<>();

    /**
     * Constructor de UsuarioRegistrado
     * 
     * @param correo   correo del gestor
     * @param password contraseña del gestor
     */
    public UsuarioRegistrado(String correo, String password) {
        this.correo = correo;
        this.password = password;
        Teatro.addUsuario(this);
    }

    /**
     * Este método devuelve el correo de un usuario registrado
     * 
     * @return Cadena que representa correo del usuario registrado
     */
    public String getCorreo() {
        return this.correo;
    }

    /**
     * 
     * Este método asigna un correo a un usuario registrado
     * 
     * @param correo correo del usuario registrado
     * 
     * @return entero a modo de CdE
     */
    public int setCorreo(String correo) {
        for (UsuarioRegistrado u : Teatro.getUsuarios()) {
            if (u.getCorreo().equals(correo)) {
                return -1;
            }
        }
        this.correo = correo;
        return 0;
    }

    /**
     * Este método devuelve la lista de abonos de un usuario registrado
     * 
     * @return Lista de abonos de un usuario registrado
     */
    public ArrayList<Abono> getAbonosUsuario() {
        return this.abonos;
    }

    /**
     * Este método devuelve la lista de notificaciones de un usuario registrado
     * 
     * @return Lista de notificaciones de un usuario registrado
     */
    public ArrayList<Notificacion> getNotificaciones() {
        if (this.notificaciones == null)
            return null;
        return this.notificaciones;
    }

    /**
     * Este método devuelve la lista de entradas de un usuario registrado
     * 
     * @return Lista de abonos de un usuario registrado
     */
    public ArrayList<Ticket> getEntradasUsuario() {
        return this.entradas;
    }

    /**
     * Este método añade una notificación a la lista de notificaciones de un usuario
     * registrado
     * 
     * @param notificacion Notificación a añadir a la lista del usuario.
     */
    public void addNotificacion(Notificacion notificacion) {
        /* Si el usuario ya tiene la notificación no se vuelve a incluir */
        if (!this.notificaciones.contains(notificacion)) {
            this.notificaciones.add(notificacion);
        }
    }

    /**
     * Este método añade una entrada a la lista de entradas de un usuario registrado
     * 
     * @param entrada Ticket a añadir a la lista del usuario.
     */
    public void addEntrada(Ticket entrada) {
        /* Si el usuario ya tiene la notificación no se vuelve a incluir */
        if (!this.entradas.contains(entrada)) {
            this.entradas.add(entrada);
        }
    }

    /**
     * Este método añade un abono a la lista de abonos de un usuario registrado
     * 
     * @param abono Abono a añadir a la lista del usuario.
     */
    public void addAbono(Abono abono) {
        /* Si el usuario ya tiene la notificación no se vuelve a incluir */
        // preguntar si puedo tener varias veces el mismo abono
        if (!this.abonos.contains(abono)) {
            this.abonos.add(abono);
        }
    }

    /**
     * Este método devuelve el tamaño de la lista de las entradas de un usuario
     * 
     * @return Entero con el tamaño de la lista de las entradas de un usuario
     */
    public int getSizeEntradas() {
        return this.entradas.size();
    }

    /**
     * Este método devuelve la contraseña de un usuario registrado
     * 
     * @return Cadena que representa contraseña de un usuario registrado
     */
    public String getPassword() {
        return this.password;
    }

    /**
     * Este método asigna una contrase�a a un usuario
     * 
     * @param password contrase�a del usuario
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Este método devuelve la lista de reservas de un usuario registrado
     * 
     * @return Lista de reservas de un usuario registrado
     */
    public ArrayList<Reserva> getReservas() {
        return this.reservas;
    }

    /**
     * Este método añade una reserva a la lista de reservas de un usuario registrado
     * 
     * @param r Reserva a añadir a la lista del usuario.
     */
    public void addReserva(Reserva r) {
        if (!this.reservas.contains(r)) {
            this.reservas.add(r);
        }
    }

    /**
     * Este método devuelve el tamaño de la lista de las reservas de un usuario
     * 
     * @return Entero con el tamaño de la lista de las reservas de un usuario
     */
    public int getSizeReservas() {
        return this.reservas.size();
    }

    /**
     * Este método sirve para pagar entradas; es decir, cobrar el importe
     * 
     * @param tarjeta       String con la tarjeta que se compra
     * @param mensaje       String con el concepto de la compra
     * @param precioGeneral double con el importe a cobrar
     * @throws NonExistentFileException          Fichero inexistente
     * @throws UnsupportedImageTypeException     Formato de imagen no compatible
     * @throws InvalidCardNumberException        Número de tarjeta de crédito no
     *                                           válido
     * @throws FailedInternetConnectionException Red no válida
     * @throws OrderRejectedException            Compra rechazada
     * @return boolean que devuelve true si se ha realizado con éxito y false en
     *         caso contrario
     */
    public boolean pagar(String tarjeta, String mensaje, double precioGeneral)
            throws NonExistentFileException, UnsupportedImageTypeException, InvalidCardNumberException,
            FailedInternetConnectionException, OrderRejectedException {

        // Se permite pagar 0 euros
        if (precioGeneral < 0)
            return false;

        /* Si la tarjeta no es válida */
        if (TeleChargeAndPaySystem.isValidCardNumber(tarjeta) == false) {
            return false;
        }
        /* Se carga el importe en la tarjeta */
        TeleChargeAndPaySystem.charge(tarjeta, mensaje, precioGeneral);

        return true;
    }

    /**
     * Este método sirve para comprar entradas; es decir, seleccionarlas y generar
     * el pdf
     * 
     * @param entradas Lista de entradas
     * @param tarjeta  String con la tarjeta que se compra
     * @throws NonExistentFileException          Fichero inexistente
     * @throws UnsupportedImageTypeException     Formato de imagen no compatible
     * @throws InvalidCardNumberException        Número de tarjeta de crédito no
     *                                           válido
     * @throws FailedInternetConnectionException Red no válida
     * @throws OrderRejectedException            Compra rechazada
     * 
     * @return Entero a modo de control de errores (1 si todo ha ido bien)
     */
    public int comprarEntradas(List<Ticket> entradas, String tarjeta)
            throws NonExistentFileException, UnsupportedImageTypeException, InvalidCardNumberException,
            FailedInternetConnectionException, OrderRejectedException {

        /* No hay entradas */
        // Se comprueba la tarjeta de credito, para evitar calculos innecesarios
        if (entradas == null || TeleChargeAndPaySystem.isValidCardNumber(tarjeta) == false) {
            return -1;
        }

        if (entradas.size() == 0) {
            return -1;
        }

        if (entradas.get(0) != null) {
            Ticket entradaGeneral = entradas.get(0);
            /* Si no hay suficientes entradas */
            if (entradaGeneral.getRepresentacion().entradasDisponibles().size() < entradas.size()) {
                return -2;
            }
            double precioGeneral = 0;

            for (Ticket entrada : entradas) {
                if (entrada.getEntradaBloqueada() == true) {
                    return -3;
                }
                /* Sumamos los precios de cada entrada al total */
                precioGeneral += entrada.getPrecioZona().getPrecio();
            }

            for (Abono abono : this.getAbonosUsuario()) {
                /* Si el abono es AbonoAnual, una entrada sale gratis */
                if (abono instanceof AbonoAnual) {
                    for (Ticket entrada : entradas) {
                        /* Si la zona de la entrada coincide con la zona de la entrada */
                        if (entrada.getPrecioZona().getZona() == abono.getZonaAbono()) {
                            precioGeneral -= entrada.getPrecioZona().getPrecio();
                            /* Para que como mucho descuente 1 entrada */
                            break;
                        }
                    }
                }
                /* Si el abono es AbonoCiclo */
                else {
                    AbonoCiclo ac = (AbonoCiclo) abono;

                    /*
                     * Si el evento de la entrada pertenece a los eventos contenidos en el ciclo se
                     * aplicará descuento en una entrada siempre y cuando la zona de la entrada
                     * corresponda con la del abono de ciclo.
                     */
                    if (ac.getCicloAbonado().getEventos().contains(entradaGeneral.getRepresentacion().getEvento())) {
                        for (Ticket entrada : entradas) {
                            if (entrada.getPrecioZona().getZona() == ac.getZonaAbono()) {
                                precioGeneral -= entrada.getPrecioZona().getPrecio() * ac.getDescuento();
                                /* Para que como mucho descuente 1 entrada */
                                break;
                            }
                        }
                    }
                }
            }

            /* Pagar tickets */
            if (precioGeneral > 0) {
                if (pagar(tarjeta, "Compra de Tickets", precioGeneral) == false) {
                    return -4;
                }
            }
            /* CdE - precio negativo */
            else if (precioGeneral < 0) {
                return -5;
            }

            /* Se bloquean despues de pagar */
            for (Ticket entrada : entradas) {
                /* Se bloquea la entrada y se asocian entrada con usuario */
                entrada.comprarTicket(this);
                this.addEntrada(entrada);
                TicketSystem.createTicket(entrada, "tmp/");
            }

            // Se actualizan las estadisticas
            entradas.get(0).getRepresentacion().actualizarEstadistica(precioGeneral, entradas.size());
            entradas.get(0).getPrecioZona().getZona().actualizarEstadistica(precioGeneral);
        } else {
            /* No hay entradas que comprar */
            return -1;
        }
        return 1;
    }

    /**
     * Este método sirve para comprar entradas numeradas en funcion del tipo de
     * seleccion. Para el tipo de seleccion manual se deben seleccionar las butacas
     * y llamar al metodo con el mismo nombre y parametro de entrada la lista de
     * butacas
     * 
     * @param zona    zona simple numerada
     * @param repr    representacion del evento
     * @param tipo    Selección según TipoSeleccion
     * @param num     entero con el número de entradas a comprar
     * @param tarjeta String con la tarjeta que se compra
     * 
     * @throws NonExistentFileException          Fichero inexistente
     * @throws UnsupportedImageTypeException     Formato de imagen no compatible
     * @throws InvalidCardNumberException        Número de tarjeta de crédito no
     *                                           válido
     * @throws FailedInternetConnectionException Red no válida
     * @throws OrderRejectedException            Compra rechazada
     * 
     * @return Entero a modo de control de errores
     */
    public int comprarEntradas(ZSNumerada zona, RepresentacionEvento repr, TipoSeleccion tipo, int num, String tarjeta)
            throws NonExistentFileException, UnsupportedImageTypeException, InvalidCardNumberException,
            FailedInternetConnectionException, OrderRejectedException {
		/* No hay entradas */
		/*
		 * if (num <= 0 || zona == null || tipo == null ||
		 * TeleChargeAndPaySystem.isValidCardNumber(tarjeta) == false) return -1;
		 */
		if (num <= 0)
			return -15;

		if (zona == null)
			return -20;

		if (tipo == null)
			return -50;

		if (entradas == null)
			return -30;

		List<Ticket> entradas = seleccionButacas(zona, repr, tipo, num);

		if (entradas == null || entradas.size() == 0) // No habia entradas suficientes
			return -1;

        if (entradas.size() < num) {
            return -6;
        }
        return comprarEntradas(entradas, tarjeta);
    }

    /**
     * Este método sirve para comprar entradas no numeradas. Hace una llamada a
     * comprarEntradas para la lista de entradas seleccionada
     * 
     * @param z       zona simple numerada
     * @param repr    representacion del evento
     * @param num     entero con el número de entradas a comprar
     * @param tarjeta String con la tarjeta que se compra
     * 
     * @throws NonExistentFileException          Fichero inexistente
     * @throws UnsupportedImageTypeException     Formato de imagen no compatible
     * @throws InvalidCardNumberException        Número de tarjeta de crédito no
     *                                           válido
     * @throws FailedInternetConnectionException Red no válida
     * @throws OrderRejectedException            Compra rechazada
     * 
     * @return -1 en caso de error; 0 en caso contrario
     */
    public int comprarEntradas(ZSNoNumerada z, RepresentacionEvento repr, int num, String tarjeta)
            throws NonExistentFileException, UnsupportedImageTypeException, InvalidCardNumberException,
            FailedInternetConnectionException, OrderRejectedException {
        ArrayList<Ticket> entradasAux = new ArrayList<>();
        ArrayList<Ticket> entradasSeleccion = new ArrayList<>();
        int cont = 1;

        if (z == null || repr == null || num <= 0 || TeleChargeAndPaySystem.isValidCardNumber(tarjeta) == false) {
            return -1;
        }

        if(num>repr.getEntradas().size()) {
        	return -2;
        }
        
        /* Si no hay suficientes entradas */
        for (Ticket e : repr.getEntradas()) {
            if (e.getPrecioZona().getZona() == z && e.getEntradaBloqueada() == false) {
                entradasAux.add(e);
            }
        }

        if (entradasAux.size() < num) {
            return -6;
        }

        for (Ticket e : entradasAux) {
            if (cont <= num) {
                entradasSeleccion.add(e);
            }
            cont += 1;
        }
        return comprarEntradas(entradasSeleccion, tarjeta);
    }

    /**
     * Este método duplica (clona) las butacas que se le pasen como argumento
     * 
     * @param bt array de butacas a clonar
     * @return copia del array de butacas de entrada
     */
    private ArrayList<Butaca> clonarButacas(ArrayList<Butaca> bt) {
        ArrayList<Butaca> copia = new ArrayList<>();

        // retorno vacio
        if (bt != null) {
            copia.addAll(bt);
        }

        return copia;
    }

    /**
     * Este método es auxiliar de seleccionButacas y sirve para obtener
     * combinaciones de butacas contiguas para una fila dada
     * 
     * @param filaButacas lista de butacas sobre las que se generan las
     *                    combinaciones
     * @param num         número de butacas a seleccionar
     * 
     * @return lista de listas de butacas contiguas para una fila dada
     */
    private ArrayList<ArrayList<Butaca>> getCombinationsContiguas(ArrayList<Butaca> filaButacas, int num) {
        ArrayList<ArrayList<Butaca>> combinacionButacas = new ArrayList<>();
        ArrayList<Butaca> butacasAux = new ArrayList<>();
        boolean flag;

        int i, j, nAux;

        for (i = 0; i <= filaButacas.size() - num; i++) {
            flag = true;
            for (j = 0; j < num - 1 && flag == true; j++) {
                nAux = filaButacas.get(i + j + 1).getColumna() - filaButacas.get(i + j).getColumna();
                if (Math.abs(nAux) > 1) { // valor absoluto
                    // En esta configuracion no hay num entradas contiguas
                    flag = false;
                }
            }

            // Hay entradas contiguas
            if (flag == true) {
                if (!butacasAux.isEmpty()) {
                    butacasAux.clear();
                }
                for (j = 0; j < num; j++) {
                    butacasAux.add(filaButacas.get(i + j));
                }
                combinacionButacas.add(this.clonarButacas(butacasAux));
            }
        }

        return combinacionButacas;
    }

    /**
     * Este método es auxiliar de seleccionButacas y sirve para obtener una lista de
     * butacas centradas y contiguas
     * 
     * @param repr       representación de evento
     * @param zona       zona simple numerada
     * @param nRestantes numero de entradas restantes por asignar
     * @param nFila numero fila
     * 
     * @return lista de butacas seleccionadas en la fila nFila
     */
    private ArrayList<Butaca> seleccionCentradaContigua(RepresentacionEvento repr, ZSNumerada zona, int nRestantes,
            int nFila) {
        int nAux, nCerca, nCercaMin;
        ArrayList<ArrayList<Butaca>> combinacionButacas = new ArrayList<>();
        ArrayList<Butaca> filaButacas = new ArrayList<>();
        ArrayList<Butaca> butacasAux = new ArrayList<>();
        ArrayList<Butaca> butacasSel = null;

        // Seleccion de las butacas disponibles de la fila i
        for (Butaca b : zona.getButacas()) {
            if (b.getFila() == nFila && repr.isbutacaOcupada(b) == false) {
                /* Añadimos las butacas al array */
                filaButacas.add(b);
            }
        }

        if (nRestantes > zona.getnColumna()) {
            nAux = zona.getnColumna();
        } else {
            nAux = nRestantes;
        }
        // Combinaciones
        while (nAux > 0 && nRestantes > 0 && !filaButacas.isEmpty()) {
            while (!filaButacas.isEmpty() && nRestantes > 0) {
                combinacionButacas = getCombinationsContiguas(filaButacas, nAux);
                if (combinacionButacas != null && !combinacionButacas.isEmpty()) {
                    nCercaMin = nRestantes * zona.getnColumna();
                    // Encuentra la mas centrada
                    for (ArrayList<Butaca> grupoButaca : combinacionButacas) {
                        nCerca = 0;
                        for (Butaca b : grupoButaca) {
                            nCerca += Math.abs(b.getColumna() - (int) zona.getnColumna() / 2);
                        }
                        if (nCerca < nCercaMin) {
                            butacasSel = grupoButaca;
                            nCercaMin = nCerca;
                        }
                    }
                    // Añade las butacas
                    if (butacasSel != null && !butacasSel.isEmpty()) {
                        butacasAux.addAll(butacasSel);
                        nRestantes -= butacasSel.size();
                    }
                    filaButacas.removeAll(butacasSel);
                } else {
                    break;
                }
            }
            // Se reajusta la nueva búsqueda
            if (filaButacas.size() < nAux) {
                // Evita iteraciones innecesarias
                nAux = filaButacas.size();
            }
            nAux--;
        }

        return butacasAux;
    }

    /**
     * Este método es auxiliar de seleccionButacas y sirve para obtener una lista de
     * las butacas más alejadas del resto de butacas ocupadas
     * 
     * @param zona zona simple numerada en la que buscar las butacas
     * @param repr representación de evento
     * @param num  entero con el número de butacas a seleccionar
     * 
     * @return lista de butacas alejadas
     */
    private ArrayList<Butaca> butacasMasAlejada(ZSNumerada zona, RepresentacionEvento repr, int num) {
        ArrayList<Butaca> butacasLibres = new ArrayList<>();
        ArrayList<Butaca> butacasOcupadas = new ArrayList<>();
        ArrayList<Butaca> butacasSeleccionadas = new ArrayList<>();
        ArrayList<Butaca> butacasAux = new ArrayList<>();
        Butaca bAux;

        TreeMap<Integer, ArrayList<Butaca>> mapaButacas = new TreeMap<>();
        Map.Entry<Integer, ArrayList<Butaca>> entry;
        int distancia;
        int i;

        for (Butaca b : zona.getButacas()) {
            if (!repr.isbutacaOcupada(b)) {
                butacasLibres.add(b);
            } else {
                butacasOcupadas.add(b);
            }
        }

        if (butacasLibres.size() < num) {
            return butacasSeleccionadas;
        }

        for (Butaca b : butacasLibres) {
            distancia = 0;
            for (Butaca b2 : butacasOcupadas) {
                distancia += (Math.abs(b.getColumna() - b2.getColumna()) + Math.abs(b.getFila() - b2.getFila()));
            }
            if (mapaButacas.containsKey(distancia)) {
                mapaButacas.get(distancia).add(b);
            } else {
                if (!butacasAux.isEmpty()) {
                    butacasAux.clear();
                }
                butacasAux.add(b);
                mapaButacas.put(distancia, this.clonarButacas(butacasAux));
            }
        }

        for (i = 0; i < num; i++) {
            if (!mapaButacas.isEmpty()) {
                entry = mapaButacas.lastEntry();
                // No podra estar vacio
                bAux = entry.getValue().get(0);
                entry.getValue().remove(bAux);
                if (entry.getValue().isEmpty()) {
                    mapaButacas.remove(entry.getKey());
                }
                butacasSeleccionadas.add(bAux);
            } else {
                return butacasSeleccionadas;
            }
        }

        return butacasSeleccionadas;
    }

    /**
     * Este método sirve para que el programa seleccione las butacas automáticamente
     * según el tipo de selección deseado
     * 
     * @param zona zona simple numerada
     * @param repr representación de evento
     * @param tipo tipo de selección de las butacas
     * @param num  número de butacas a seleccionar
     * @return lista de tickets según las butacas seleccionadas
     */
    public List<Ticket> seleccionButacas(ZSNumerada zona, RepresentacionEvento repr, TipoSeleccion tipo, int num) {
        int i, j, nRestantes, nAux;
        ArrayList<Butaca> butacasSel = new ArrayList<>();
        ArrayList<Butaca> butacasAux;
        List<EntradaNumerada> entradasRepresentacion = new ArrayList<>();
        List<Ticket> entradasFinales = new ArrayList<>();

        // Se comprueba que existan num entradas disponibles
        if (repr.entradasDisponibles().size() < num) {
            return null;
        }

        // ORDEN DE PRIORIDAD:
        // 1. SUPERIOR
        // 2. CONTIGUAS
        // 3. CENTRADAS

        switch (tipo) {
        case CentradaFilaInferior:
            for (i = zona.getnFila(), nRestantes = num; i > 0 && nRestantes > 0; i--) {
                butacasAux = seleccionCentradaContigua(repr, zona, nRestantes, i);
                butacasSel.addAll(butacasAux);
                nRestantes -= butacasAux.size();
            }
            break;

        case CentradaFilaSuperior:
            for (i = 1, nRestantes = num; i <= zona.getnFila() && nRestantes > 0; i++) {
                butacasAux = seleccionCentradaContigua(repr, zona, nRestantes, i);
                butacasSel.addAll(butacasAux);
                nRestantes -= butacasAux.size();
            }
            break;

        case CentradaFilaColumna:
            for (i = 0, nRestantes = num; i <= zona.getnFila() / 2 && nRestantes > 0; i++) {
                for (j = 0; j < 2 && nRestantes > 0 && !(i == 0 && j > 0); j++) {
                    nAux = (int) (zona.getnFila() / 2 + Math.pow(-1, j) * i);
                    if (nAux <= 0 || nAux > zona.getnFila())
                        break;
                    butacasAux = seleccionCentradaContigua(repr, zona, nRestantes, nAux);
                    butacasSel.addAll(butacasAux);
                    nRestantes -= butacasAux.size();
                }
            }
            break;

        case Alejada:
            butacasSel = butacasMasAlejada(zona, repr, num);
        }

        // En caso de error, butacasSel no es nulo, sino un array vacio
        // Si no se han conseguido suficientes butacas, no se compra
        if (butacasSel.size() < num)
            return entradasFinales;

        // Se consiguen las entradas asociadas a las butacas
        /* Se obtienen las entradas numeradas de la representación */
        for (Ticket en : repr.getEntradas()) {
            if (en instanceof EntradaNumerada) {
                EntradaNumerada entrNum = (EntradaNumerada) en;
                entradasRepresentacion.add(entrNum);
            }
        }

        // Se consiguen las entradas
        for (Butaca b : butacasSel) {
            for (EntradaNumerada entrNum : entradasRepresentacion) {
                /* Si la butaca seleccionada coincide con la entrada */
                if (entrNum.getButaca() == b) {
                    /* Añado la entrada con butaca b al array de entradas que se retornará */
                    entradasFinales.add(entrNum);
                }
            }
        }

        return entradasFinales;
    }

    /**
     * Este método sirve para comprar un abono
     * 
     * @param abono   Abono
     * @param tarjeta String con la tarjeta que se compra
     * 
     * @throws NonExistentFileException          Fichero inexistente
     * @throws UnsupportedImageTypeException     Formato de imagen no compatible
     * @throws InvalidCardNumberException        Número de tarjeta de crédito no
     *                                           válido
     * @throws FailedInternetConnectionException Red no válida
     * @throws OrderRejectedException            Compra rechazada
     * 
     * @return Entero a modo de control de errores (CdE)
     */
    public int comprarAbono(Abono abono, String tarjeta) throws NonExistentFileException, UnsupportedImageTypeException,
            InvalidCardNumberException, FailedInternetConnectionException, OrderRejectedException {

        String mensaje = "";

        if (abono instanceof AbonoAnual) {
            mensaje += "Abono anual";
        } else {
            mensaje += "Abono ciclo";
        }

        /* Cargamos el importe en la tarjeta */
        if (pagar(tarjeta, mensaje, abono.getPrecioAbono()) == false) {
            return -1;
        }

        /* Añadimos abono a la lista de abonos del usuario */
        this.addAbono(abono);

        // Se actualiza la estadística de zona
        abono.getZonaAbono().actualizarEstadistica(abono.getPrecioAbono());

        return 1;
    }

    /**
     * Este método sirve para realizar una reserva a partir de las entradas
     * 
     * @param entradasZona lista de tickets a reservar
     * @return Booleano que devuelve true si se puede realizar la reserva
     */
    public boolean realizarReserva(ArrayList<Ticket> entradasZona) {
        if (entradasZona.size() < 0)
            return false;

        Reserva r = new Reserva(entradasZona.get(0).getRepresentacion(), (ArrayList<Ticket>) entradasZona);

        for (Ticket t : entradasZona) {
            // Las entradas han sido bloqueadas en el constructor de reserva
            t.reservarTicket(this);
            t.setReserva(r);
            this.addReserva(r);
        }
        return true;
    }

    /**
     * Este método sirve para realizar una reserva con butacas
     * 
     * @param z    zona simple numerada
     * @param repr representacion de evento
     * @param tipo tipo de selección de las butacas
     * @param num  número de butacas a seleccionar
     * @return Booleano que devuelve true si se puede realizar la reserva y false en
     *         caso contrario
     */
    public boolean realizarReserva(ZSNumerada z, RepresentacionEvento repr, TipoSeleccion tipo, int num) {
        List<Ticket> entradasZona = new ArrayList<>();

        if (z == null || num <= 0) {
            return false;
        }

        /* Si no hay suficientes entradas */
        for (Ticket e : repr.getEntradas()) {
            if (e.getPrecioZona().getZona() == z) {
                entradasZona.add(e);
            }
        }

        if (entradasZona.size() < num) {
            return false;
        }

        entradasZona = seleccionButacas((ZSNumerada) z, repr, tipo, num);
        if (entradasZona == null) {
            return false;
        }

        return realizarReserva((ArrayList<Ticket>) entradasZona);
    }

    /**
     * Este método sirve para realizar una reserva sin butacas
     * 
     * @param z    zona simple no numerada
     * @param repr representación de evento
     * @param num  número de entradas a reservar
     * @return Booleano que devuelve true si se puede realizar la reserva y false en
     *         caso contrario
     */
    public boolean realizarReserva(ZSNoNumerada z, RepresentacionEvento repr, int num) {
        ArrayList<Ticket> entradas = new ArrayList<>();
        ArrayList<Ticket> entradasSeleccion = new ArrayList<>();
        int cont = 1;

        if (z == null || num <= 0) {
            return false;
        }

        /* Si no hay suficientes entradas */
        for (Ticket e : repr.getEntradas()) {
            if (e.getPrecioZona().getZona() == z && e.getEntradaBloqueada() == false) {
                entradas.add(e);
            }
        }

        if (entradas.size() < num) {
            return false;
        }

        for (Ticket e : entradas) {
            if (cont <= num) {
                entradasSeleccion.add(e);
            }
            cont += 1;
        }

        return realizarReserva(entradasSeleccion);
    }

    /**
     * Este método asigna un usuario registrado a la lista de espera de una
     * representación
     * 
     * @param r Representación del evento
     */
    public void apuntarListaEspera(RepresentacionEvento r) {
        Notificacion noti = new Notificacion("Anyadido a la lista de espera de la representacion con fecha "
                + r.getFecha() + " y hora " + r.getHora() + " del evento " + r.getEvento().getTitulo());

        /* Añade al usuario al array de usuarios en la lista de espera */
        r.addListaDeEspera(this);

        /* Añade la notificación al array de notificaciones del usuario */
        this.addNotificacion(noti);
    }

    /**
     * Este método hace que un usuario registrado pueda confirmar una reserva
     * 
     * @param r       Reserva a confirmar
     * @param tarjeta String con la tarjeta a pagar
     * 
     * @throws NonExistentFileException          Fichero inexistente
     * @throws UnsupportedImageTypeException     Formato de imagen no compatible
     * @throws InvalidCardNumberException        Número de tarjeta de crédito no
     *                                           válido
     * @throws FailedInternetConnectionException Red no válida
     * @throws OrderRejectedException            Compra rechazada
     * 
     * @return entero a modo de CdE (-1 en caso de error; 0 en caso contrario)
     */
    public int confirmarReserva(Reserva r, String tarjeta)
            throws NonExistentFileException, UnsupportedImageTypeException, InvalidCardNumberException,
            FailedInternetConnectionException, OrderRejectedException {
        Notificacion nt;

        if (!this.reservas.contains(r)) {
            return -1;
        }

        // Se desbloquean las entradas para proceder segun el flujo natural de compra
        for (Ticket t : r.getEntradas()) {
            t.setEntradaBloqueada(false);
        }
        if (comprarEntradas(r.getEntradas(), tarjeta) == -1) {
            return -1;
        }

        // se elimina la reserva
        this.getReservas().remove(r);
        for (Ticket t : r.getEntradas()) {
            t.setReserva(null);
        }

        nt = new Notificacion("Reserva confirmada para la representacion: " + r.getRepresentacion());
        nt.enviarNotificacion(this);
        return 0;
    }

    /**
     * Este método hace que un usuario registrado pueda cancelar una reserva
     * 
     * @param r Reserva a cancelar
     */

    public void cancelarReserva(Reserva r) {
        if (!this.getReservas().contains(r)) {
            return;
        }

        /* Quitamos la reserva del usuario */
        this.getReservas().remove(r);

        for (Ticket e : r.getEntradas()) {
            /* Desbloqueamos la entrada */
            e.setEntradaBloqueada(false);
            /* La entrada ya no se asocia a ningún usuario */
            e.asociarUsuario(null);
        }

        r.getRepresentacion().avisarListaDeEspera();
    }

    /**
     * @param correo   Cadena con el correo del Usuario a registrar
     * @param password Cadena con la contraseña del Usuario a registrar
     * 
     * @return null, ya que un usuario registrado no puede registrarse otra vez
     */
    public static UsuarioRegistrado registrarUsuario(String correo, String password) {
        return null;
    }
    
    /**
     * Devuelve un doble array con las entradas
     * @return doble array con las entradas
     */
    public Object[][] consultarEntradas() {
        ArrayList<Ticket> entradas = this.getEntradasUsuario();
        Object[][] l = new Object[entradas.size()][3];
        RepresentacionEvento rep = null;
        int i = 0;

        for (Ticket entr : entradas) {
            rep = entr.getRepresentacion();
            l[i++] = Arrays.asList(rep.getEvento().getTitulo(), rep.getFecha(), rep.getHora()).toArray();
        }
        return l;
    }

}