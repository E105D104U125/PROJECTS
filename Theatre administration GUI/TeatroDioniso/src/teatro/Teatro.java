
/** 
 * 
 * @author Olmar Arranz olmar.arranz@estudiante.uam.es 
 * @author Cristina Madrigal cristina.madrigalb@estudiante.uam.es 
 * @author Eduardo Terrés Caballero eduardo.terres@estudiante.uam.es
 *  
 */

package teatro;

import entradas.*;
import estadisticas.*;
import obras.*;
import usuarios.*;
import zonas.*;

import java.util.ArrayList;
import java.util.stream.Collectors;
import java.io.*;

public class Teatro implements Serializable {

    private static final long serialVersionUID = 8278299983481575875L;
    private static String nombre = "Teatro Dioniso";
    private static String ubicacion = "C/ del alamo, 57";
    private static String contacto = "629374120";
    private static String correo = "teatrodioniso@teatro.com";
    private static Gestor gestor;
    private static UsuarioRegistrado usuarioLogueado;

    private static ArrayList<UsuarioRegistrado> usuarios = new ArrayList<>();
    private static ArrayList<Estadistica> estadisticas = new ArrayList<>();
    private static ArrayList<DeshabilitacionButaca> btcDeshabilitadas = new ArrayList<>();
    private static ArrayList<Zona> zonas = new ArrayList<>();
    private static ArrayList<Evento> eventos = new ArrayList<>();
    private static ArrayList<Ciclo> ciclos = new ArrayList<>();
    private static ArrayList<Abono> abonos = new ArrayList<>();

    /**
     * Constructor Teatro
     * 
     */
    public Teatro() {

    }

    /**
     * Este método devuelve el nombre del teatro
     * 
     * @return Cadena que representa el nombre de un teatro
     */
    public static String getNombre() {
        return nombre;
    }

    /**
     * Este método asigna un nombre a un teatro
     * 
     * @param n nombre del teatro
     */
    public static void setNombre(String n) {
        nombre = n;
    }

    /**
     * Este método devuelve la ubicación del teatro
     * 
     * @return Cadena que representa la ubicación de un teatro
     */
    public static String getUbicacion() {
        return ubicacion;
    }

    /**
     * Este método asigna la ubicación de un teatro
     * 
     * @param u ubicación del teatro
     */
    public static void setUbicacion(String u) {
        ubicacion = u;
    }

    /**
     * Este método devuelve el contacto del teatro
     * 
     * @return Cadena que representa el contacto de un teatro
     */
    public static String getContacto() {
        return contacto;
    }

    /**
     * Este método asigna el contacto de un teatro
     * 
     * @param c contacto del teatro
     */
    public static void setContacto(String c) {
        contacto = c;
    }

    /**
     * Este método devuelve el correo del teatro
     * 
     * @return Cadena que representa el correo de un teatro
     */
    public static String getCorreo() {
        return correo;
    }

    /**
     * Este método asigna el correo electrónico de un teatro
     * 
     * @param cor Dirección de correo electrónico del teatro
     */
    public static void setCorreo(String cor) {
        correo = cor;
    }

    /**
     * Este método devuelve el gestor del teatro
     * 
     * @return Gestor del teatro
     */
    public static Gestor getGestor() {
        return gestor;
    }

    /**
     * Este método asigna el gestor del teatro
     * 
     * @param g Gestor del teatro
     */
    public static void setGestor(Gestor g) {
        gestor = g;
    }

    /**
     * Este método devuelve los usuarios de un teatro.
     * 
     * @return Lista de usuarios de un teatro.
     */
    public static ArrayList<UsuarioRegistrado> getUsuarios() {
        return usuarios;
    }
    
    /**
     * @return Zonas del teatro en array de Object
     */
    public static Object[] getZonasGUI() {
    	Object[] zonasObjeto = new Object[Teatro.getZonasSimples().size()];
    	for (int i = 0; i < Teatro.getZonasSimples().size(); i++) {
    		zonasObjeto[i] = (Object) Teatro.getZonasSimples().get(i);
    	}
    	return zonasObjeto;
    }

    /**
     * Este método añade un usuario a la lista de usuarios de un teatro.
     * 
     * @param usuario Usuario a añadir al teatro.
     */
    public static void addUsuario(UsuarioRegistrado usuario) {
        if (!usuarios.contains(usuario)) {
            usuarios.add(usuario);
        }

        for (UsuarioRegistrado ur : usuarios) {
            if (ur.getCorreo().equals(usuario.getCorreo())) {
                return;
            }
        }
    }

    /**
     * Este método indica cuántos usuarios tiene un teatro.
     * 
     * @return Tamaño de la lista de usuarios de un teatro.
     */
    public static int getSizeUsuarios() {
        return usuarios.size();
    }

    /**
     * Este método devuelve las estadísticas de un teatro.
     * 
     * @return Lista de estadísticas de un teatro.
     */
    public static ArrayList<Estadistica> getEstadisticas() {
        return estadisticas;
    }

    /**
     * Este método añade una estadística la lista de usuarios de un teatro.
     * 
     * @param estadistica Estadistica a añadir al teatro.
     */
    public static void addEstadistica(Estadistica estadistica) {
        if (!estadisticas.contains(estadistica)) {
            estadisticas.add(estadistica);
        }
    }

    /**
     * Este método indica cuántas estadísticas tiene un teatro.
     * 
     * @return Tamaño de la lista de estadísticas de un teatro.
     */
    public static int getSizeEstadisticas() {
        return estadisticas.size();
    }

    /**
     * Este método devuelve las butacas desahbilitadas de un teatro.
     * 
     * @return Lista de butacas deshabilitadas de un teatro.
     */
    public static ArrayList<DeshabilitacionButaca> getBtcDeshabilitadas() {
        return btcDeshabilitadas;
    }

    /**
     * Este método añade una estadística la lista de butacas deshabilitadas de un
     * teatro.
     * 
     * @param butaca Butaca añadir a las deshabilitadas del teatro.
     */
    public static void addBtcDeshabilitadas(DeshabilitacionButaca butaca) {
        if (!btcDeshabilitadas.contains(butaca)) {
            btcDeshabilitadas.add(butaca);
        }
    }

    /**
     * Este método indica cuántas butacas deshabilitadas tiene un teatro.
     * 
     * @return Tamaño de la lista de butacas deshabilitadas de un teatro.
     */
    public static int getSizeBtcDeshabilitadas() {
        return btcDeshabilitadas.size();
    }

    /**
     * Este método devuelve las zonas de un teatro.
     * 
     * @return Lista de zonas de un teatro.
     */
    public static ArrayList<Zona> getZonas() {
        return zonas;
    }
    
    /**
     * Este metodo devuelve una copia de las zonas simples de un Teatro
     * 
     * @return Lista de zonas de un teatro.
     */
    public static ArrayList<Zona> getZonasSimples() {
        return new ArrayList<>(zonas.stream().filter((x) -> x instanceof ZSNumerada || x instanceof ZSNoNumerada).collect(Collectors.toList()));
    }

    /**
     * Este método añade una zona a la lista de zonas de un teatro
     * 
     * @param zona Zona a añadir al teatro
     */
    public static void addZona(Zona zona) {
        if (!zonas.contains(zona)) {
            zonas.add(zona);
        }
    }

    /**
     * Este método indica cuántas zonas tiene un teatro.
     * 
     * @return Tamaño de la lista de zonas de un teatro.
     */
    public int getSizeZonas() {
        return zonas.size();
    }

    /**
     * Este método devuelve los eventos de un teatro.
     * 
     * @return Lista de eventos de un teatro.
     */
    public static ArrayList<Evento> getEventos() {
        return eventos;
    }

    /**
     * Este método añade un evento a la lista de eventos de un teatro.
     * 
     * @param evento Evento a añadir.
     */
    public static void addEvento(Evento evento) {
        if (!eventos.contains(evento)) {
            eventos.add(evento);
        }
    }

    /**
     * Este método indica cuántos eventos tiene un teatro.
     * 
     * @return Tamaño de la lista de eventos de un teatro.
     */
    public static int getSizeEventos() {
        return eventos.size();
    }

    /**
     * Este método añade un ciclo l teatro
     * 
     * @param c ciclo a añadir al teatro
     */
    public static void addCiclo(Ciclo c) {
        if (!ciclos.contains(c)) {
            ciclos.add(c);
        }
    }

    /**
     * Este método devuelve los ciclos de un teatro.
     * 
     * @return Lista de abonos de un teatro.
     */
    public static ArrayList<Ciclo> getCiclo() {
        return ciclos;
    }

    /**
     * Este método indica cuántos ciclos
     * 
     * @return Tamaño de la lista de ciclos de un teatro.
     */
    public static int getSizeCiclo() {
        return ciclos.size();
    }

    /**
     * Este método devuelve los eventos de un teatro.
     * 
     * @return Lista de eventos de un teatro.
     */
    public static ArrayList<Abono> getAbonos() {
        return abonos;
    }

    /**
     * Este método añade un evento a la lista de eventos de un teatro.
     * 
     * @param abono Abono a añadir.
     */
    public static void addAbonos(Abono abono) {
        if (!abonos.contains(abono)) {
            abonos.add(abono);
        }
    }

    /**
     * Este método indica cuántos eventos tiene un teatro.
     * 
     * @return Tamaño de la lista de eventos de un teatro.
     */
    public static int getSizeAbonos() {
        return abonos.size();
    }

    /**
     * Metodo recuperar el estado de un teatro; para el main menu
     * 
     * @param usuarios          lista us. registrads
     * @param btcDeshabilitadas lista butacas deshabilitadas
     * @param zonas             lista zonas
     * @param eventos           lista eventos
     * @param ciclos            lista ciclos
     * @param abonos            lista abonos
     * @param gestor            gestor del teatro
     */
    public static void loadTeatro(ArrayList<UsuarioRegistrado> usuarios,
            ArrayList<DeshabilitacionButaca> btcDeshabilitadas, ArrayList<Zona> zonas, ArrayList<Evento> eventos,
            ArrayList<Ciclo> ciclos, ArrayList<Abono> abonos, Gestor gestor) {
        if (usuarios == null || btcDeshabilitadas == null || zonas == null || eventos == null || ciclos == null
                || abonos == null || gestor == null)
            return;

        Teatro.usuarios = usuarios;
        Teatro.btcDeshabilitadas = btcDeshabilitadas;
        Teatro.zonas = zonas;
        Teatro.eventos = eventos;
        Teatro.ciclos = ciclos;
        Teatro.abonos = abonos;
        Teatro.gestor = gestor;
    }

    /**
     * @param ur Usuario registrado a dar de baja
     * 
     * @return booleano que indica si se ha realizado correctamente (true) o no
     *         (false)
     */
    public static boolean cancelarUsuario(UsuarioRegistrado ur) {

        /* Si el usuario no está registrado */
        if (!getUsuarios().contains(ur)) {
            return false;
        }

        /* Se elimina el usuario de la lista */
        getUsuarios().remove(ur);

        return true;
    }

    /**
     * Método para borrar el teatro
     */
    public static void clearTeatro() {
        usuarios.clear();
        estadisticas.clear();
        btcDeshabilitadas.clear();
        zonas.clear();
        eventos.clear();
        ciclos.clear();
        abonos.clear();
    }
    
    /**
     * Aniade un gestor al teatro
     * El usuario y contrasenia quedan definidos en la clase Gestor
     */
    private static void addGestor() {
    	Teatro.gestor = new Gestor();
    }

    /**
     * Este método permite que un usuario inicie sesión
     * 
     * @param correo   Correo del usuario
     * @param password Contraseña del usuario
     * @return Entero según el usuario que inicia sesión: 0 : gestor 1 : usuario
     *         registrado -1: la contraseña es incorrecta -2: el correo es
     *         incorrecto, por tanto no hay usuario registrado 
     */
    public static int logIn(String correo, String password) {

        if (getUsuarios().size() < 0) {
            return -2;
        }

        /* Siempre debe haber un gestor */
        if(getGestor() == null) {
        	Teatro.addGestor();
        }
        
        /* Si el usuario que hace logIn es el gestor */
        if (getGestor().getCorreo().equals(correo)
                && getGestor().getPassword().equals(password)) {
             usuarioLogueado = getGestor();
            return 0;
        }

        /* Buscamos el usuario que se loguea en la lista de usuarios del teatro */
        for (UsuarioRegistrado u : getUsuarios()) {
            if (u.getCorreo().equals(correo)) {
                if (u.getPassword().equals(password)) {
                    usuarioLogueado = u;
                    return 1;
                } else {
                    usuarioLogueado = null;
                    return -1;
                }
            }
        }
        usuarioLogueado = null;
        return -2;
    }

    /**
     * Este método devuelve un usuario a partir de su correo y su contraseña
     * 
     * @param correo Correo del usuario
     * @param pass   Contraseña del usuario
     * @return Usuario registrado asociado a ese correo y esa contraseña
     */
    public static UsuarioRegistrado conseguirUsuario(String correo, String pass) {
        if (getUsuarios().size() == 0) {
            return null;
        }

        for (UsuarioRegistrado u : getUsuarios()) {
            if (u.getCorreo().equals(correo) && u.getPassword().equals(pass)) {
                return u;
            }
        }
        return null;

    }
    
    /**
     * Devuelve el usuario logueado en el momento de ejecucion
     * @return Usuario logueado
     */
    public static UsuarioRegistrado getUsuarioLogueado() {
    	return usuarioLogueado;
    }
    
    /**
	 * Establece el usuario Logueado 
	 * 
	 * @param usr usuario
     */
    public static void setUsuarioLogueado(UsuarioRegistrado usr) {
    	// El parametro de entrada puede ser null
    	usuarioLogueado = usr;
    }
    
    
    
    /**
     * Devuelve el evento de nombre introducido por parametros;
     * null en caso de no encontrarse
     * 
     * @param nombre nombre del evento
     * @return evento
     */
    public static Evento getEventoNombre(String nombre) {
    	for (Evento e: Teatro.getEventos()) {
    		if (e.getTitulo().equals(nombre)) {
    			return e;
    		}
    	}
    	return null;
    }
    
    /**
     * Devuelve la zona de nombre introducido por parametros;
     * null en caso de no encontrarse
     * 
     * @param nombre nombre de la zona
     * @return zona
     */
    public static Zona getZonaNombre(String nombre) {
    	for (Zona z: Teatro.getZonas()) {
    		if (z.getNombre().equals(nombre)) {
    			return z;
    		}
    	}
    	return null;
    }

    /**
     * Devuelve el ciclo de nombre introducido por parametros;
     * null en caso de no encontrarse
     * 
     * @param nombre nombre del ciclo
     * @return nombre
     */
	public static Ciclo getCicloNombre(String nombre) {
    	for (Ciclo c: Teatro.getCiclo()) {
    		if (c.getNombre().equals(nombre)) {
    			return c;
    		}
    	}
    	return null;
    }
}
