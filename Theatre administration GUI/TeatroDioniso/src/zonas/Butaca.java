/** 
 * 
 * @author Olmar Arranz olmar.arranz@estudiante.uam.es 
 * @author Cristina Madrigal cristina.madrigalb@estudiante.uam.es 
 * @author Eduardo Terrés Caballero eduardo.terres@estudiante.uam.es
 *  
 */
package zonas;

import java.io.Serializable;

/**
 * Clase que representa todos los tipos de butacas.
 */
public class Butaca implements Serializable {

    private static final long serialVersionUID = 6174166502797427186L;
    private int columna;
    private int fila;
    private boolean habilitada;
    private DeshabilitacionButaca dbt = null;

    /**
     * Constructor de Butaca
     * 
     * @param columna columna de la butaca
     * @param fila    fila de la butaca
     */
    public Butaca(int columna, int fila) {
        this.setColumna(columna);
        this.setFila(fila);
        this.habilitada = true;
    }

    /**
     * Asigna la fila introducida por parametro de entrada a la butaca
     * 
     * @param fila fila de la butaca
     */
    public void setFila(int fila) {
        if (fila > 0) {
            this.fila = fila;
        }
    }

    /**
     * Asigna la columna introducida por parametro de entrada a la butaca
     * 
     * @param columna columna de la butaca
     */
    public void setColumna(int columna) {
        if (columna > 0) {
            this.columna = columna;
        }
    }

    /**
     * Este método asigna el estado de habilitada o deshabilitada a una butaca
     * 
     * @param habilitada estado de la butaca
     */
    public void setHabilitada(boolean habilitada) {
        this.habilitada = habilitada;
    }

    /**
     * Este método devuelve la fila de la butaca
     * 
     * @return fila de la butaca
     */
    public int getFila() {
        return this.fila;
    }

    /**
     * Este método devuelve la columna de la butaca
     * 
     * @return columna de la butaca
     */
    public int getColumna() {
        return this.columna;
    }

    /**
     * Este método devuelve habilitación de una butaca
     * 
     * @return true si la butaca esta habilitada; false en caso contrario
     */
    public boolean getHabilitada() {
        return this.habilitada;
    }

    /**
     * Este métodos deshabilita una butaca
     * 
     * @param dbt deshabilitación de la butaca
     */
    public void deshabilitarButaca(DeshabilitacionButaca dbt) {
        this.setHabilitada(false);
        this.dbt = dbt;
    }

    /**
     * Este métodos habilita una butaca
     */
    public void habilitarButaca() {
        this.setHabilitada(true);
        this.dbt = null;
    }

    /**
     * Este método devuelve la deshabilitación de una butaca
     * 
     * @return DeshabilitacionButaca de una butaca
     */
    public DeshabilitacionButaca getDeshabilitacionButaca() {
        return this.dbt;
    }

    /**
     * Este método imprime la información de una Butaca.
     * 
     * @return Cadena que representa este objeto.
     */
    @Override
    public String toString() {
        String s;
        s = "Butaca en la columna: " + getColumna() + " y fila: " + getFila() + ". Estado de la butaca: "
                + getHabilitada() + ".\n";
        if (getDeshabilitacionButaca() != null) {
            s += dbt.toString();
        }
        return s;
    }

}
