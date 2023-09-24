/** 
 * 
 * @author Olmar Arranz olmar.arranz@estudiante.uam.es 
 * @author Cristina Madrigal cristina.madrigalb@estudiante.uam.es 
 * @author Eduardo Terrés Caballero eduardo.terres@estudiante.uam.es
 *  
 */
package zonas;

import java.lang.Math;

/**
 * Clase que representa todos los tipos de zonas simples no numeradas
 */
public class ZSNoNumerada extends ZonaSimple {

    private static final long serialVersionUID = -7173532809951550049L;
    private int aforo;
    private double reduccionAforo;

    /**
     * Constructor ZSNoNumerada
     * 
     * @param nombre         Nombre de la zona
     * @param aforo          Aforo de la zona no numerada
     * @param reduccionAforo reduccion del Aforo de la zona no numerada
     */
    public ZSNoNumerada(String nombre, int aforo, double reduccionAforo) {
        super(nombre);
        this.setAforo(aforo);
        this.reducirAforo(reduccionAforo);
    }

    /**
     * Este método asigna la reducción de aforo introducida por parámetro de entrada
     * a la zona
     * 
     * @param reduccionAforo reduccion del aforo de la zona no numerada
     */
    public void reducirAforo(double reduccionAforo) {
        if (reduccionAforo >= 0 && reduccionAforo <= 1) {
            this.reduccionAforo = reduccionAforo;
        }
    }

    /**
     * Este método asigna el aforo introducido por parámetro de entrada a la zona no
     * numerada
     * 
     * @param aforo Aforo de la zona no numerada
     */
    public void setAforo(int aforo) {
        if (aforo > 0) {
            this.aforo = aforo;
        }
    }

    /**
     * Este método devuelve el aforo de una zona no numerada
     * 
     * @return aforo de una zona no numerada
     */
    public int getAforo() {
        return this.aforo;
    }

    /**
     * Este método devuelve el aforo disponible de una zona no numerada
     * 
     * @return aforo disponible de una zona no numerada
     */
    public double getAforoDisponible() {
        return Math.floor(this.aforo * (1 - this.reduccionAforo));
    }

    /**
     * Este método devuelve el procentaje de reducción de aforo de una zona no
     * numerada
     * 
     * @return aforo del porcentaje de reduccion de aforo de una zona no numerada
     */
    public double getReduccionAforo() {
        return this.reduccionAforo;
    }

    /**
     * Este método imprime la información de una Zona Simple No Numerada.
     * 
     * @return Cadena que representa este objeto.
     */
    @Override
    public String toString() {
        String s = super.toString();
        s += ", aforo: " + getAforo() + ", reducción del aforo: " + getReduccionAforo() + ".\n";
        return s;
    }
}
