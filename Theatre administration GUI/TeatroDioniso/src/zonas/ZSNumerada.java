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
 * Clase que representa todos los tipos de zonas simples
 */
public class ZSNumerada extends ZonaSimple {

    private static final long serialVersionUID = -1978264928219641260L;
    private int nFila;
    private int nColumna;
    private ArrayList<Butaca> butacas = new ArrayList<>();

    /**
     * Constructor ZSNumerada
     * 
     * @param nombre   nombre de la zona numerada
     * @param nFila    numero de filas de la zona numerada
     * @param nColumna numero de columnas de la zona numerada
     */
    public ZSNumerada(String nombre, int nFila, int nColumna) {
        super(nombre);
        this.setnFila(nFila);
        this.setnColumna(nColumna);
        this.butacas = this.crearButacas(nFila, nColumna);
    }

    /**
     * Crea las butacas de una zona rectangular de nFila filas y nColumna columnas
     * 
     * @param nFila    numero de filas de la zona numerada
     * @param nColumna numero de columnas de la zona numerada
     * 
     * @return lista de butacas creadas
     */
    private ArrayList<Butaca> crearButacas(int nFila, int nColumna) {
        int i, j;
        ArrayList<Butaca> butacas = new ArrayList<>();
        for (i = 1; i <= nFila; i++) {
            for (j = 1; j <= nColumna; j++) {
                butacas.add(new Butaca(j, i));
            }
        }
        return butacas;
    }

    /**
     * Este método asigna el numero de filas introducido por parametro de entrada
     * 
     * @param nFila numero de filas de la zona numerada
     */
    public void setnFila(int nFila) {
        if (nFila > 0) {
            this.nFila = nFila;
        }
    }

    /**
     * Este método asigna el numero de columnas introducido por parametro de entrada
     * 
     * @param nColumna numero de columnas de la zona numerada
     */
    public void setnColumna(int nColumna) {
        if (nColumna > 0) {
            this.nColumna = nColumna;
        }
    }

    /**
     * Este método devuelve el numero de filas de una zona numerada
     * 
     * @return numero de filas de la zona numerada
     */
    public int getnFila() {
        return this.nFila;
    }

    /**
     * Este método devuelve el numero de columnas de una zona numerada
     * 
     * @return numero de columnas de la zona numerada
     */
    public int getnColumna() {
        return this.nColumna;
    }

    /**
     * Este método devuelve la lista de butacas de una zona numerada
     * 
     * @return array de las butacas de la zona simple
     */
    public ArrayList<Butaca> getButacas() {
        return this.butacas;
    }

    /**
     * Este método devuelve la lista de butacas deshabilitadas de una zona numerada
     * 
     * @return array con las butacas deshabilitadas de la zona
     */
    public ArrayList<Butaca> getButacasDeshabilitadas() {
        ArrayList<Butaca> butacasDeshabilitadas = new ArrayList<>();
        for (Butaca b : butacas) {
            if (!b.getHabilitada()) {
                butacasDeshabilitadas.add(b);
            }
        }
        return butacasDeshabilitadas;
    }

    /**
     * Este método devuelve la lista de butacas habilitadas de una zona numerada
     * 
     * @return un array con las butacas habilitadas de la zona
     */
    public ArrayList<Butaca> getButacasHabilitadas() {
        ArrayList<Butaca> butacasHabilitadas = new ArrayList<>();
        for (Butaca b : butacas) {
            if (b.getHabilitada() == true) {
                butacasHabilitadas.add(b);
            }
        }
        return butacasHabilitadas;
    }

    /**
     * Este método imprime la información de una Zona Simple Numerada.
     * 
     * @return Cadena que representa este objeto.
     */
    @Override
    public String toString() {
        // int i, j;
        String s = super.toString();
        s += ", número de filas: " + getnFila() + ", número de columnas: " + getnColumna() + ".\n";
        /*
         * s += "Distribucion de butacas\n"; for (i = 0; i < this.getnFila(); i++) { for
         * (j = 0; j < this.getnColumna(); j++) { s += " o "; } s += '\n'; }
         */
        return s;
    }
}
