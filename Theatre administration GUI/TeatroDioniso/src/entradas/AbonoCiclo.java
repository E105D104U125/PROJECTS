
/** 
 * 
 * @author Olmar Arranz olmar.arranz@estudiante.uam.es 
 * @author Cristina Madrigal cristina.madrigalb@estudiante.uam.es 
 * @author Eduardo Terrés Caballero eduardo.terres@estudiante.uam.es
 *  
 */
package entradas;

import zonas.*;
import obras.*;
import teatro.*;

/**
 * Clase que representa todos los tipos de abonos de ciclos.
 */
public class AbonoCiclo extends Abono {

    private static final long serialVersionUID = 4270734115274202141L;
    private double descuento;
    private Ciclo ciclo;

    /**
     * Constructor de la clase AbonoCiclo
     * 
     * @param precio    precio del abono de ciclo
     * @param zona      zona asociada al abono de ciclo
     * @param descuento descuento asociado al abono de ciclo
     * @param ciclo     ciclo al que se asocia el abono
     */
    public AbonoCiclo(double precio, Zona zona, double descuento, Ciclo ciclo) {
        super(precio, zona);
        this.descuento = descuento;
        this.ciclo = ciclo;
        ciclo.addAbonosCiclo(this);
        Teatro.addAbonos(this);
    }

    /**
     * Este método devuelve el descuento de una abono de ciclo.
     * 
     * @return Double que indica el descuento del abono de ciclo.
     */
    public Double getDescuento() {
        return this.descuento;
    }

    /**
     * Este método asigna el descuento de un abono de ciclo.
     * 
     * @param descuento Descuento del abono de ciclo.
     */
    public void setDescuento(Double descuento) {
        if (descuento < 0) {
            return;
        }
        this.descuento = descuento;
    }

    /**
     * Este método devuelve el ciclo asociado al abono.
     * 
     * @return ciclo asociado al abono.
     */
    public Ciclo getCicloAbonado() {
        return this.ciclo;
    }

    /**
     * Este método asigna un ciclo a un abono.
     * 
     * @param ciclo ciclo a asignar al abono.
     */
    public void setCicloAbonado(Ciclo ciclo) {
        if (ciclo == null) {
            return;
        }
        this.ciclo = ciclo;
    }

    /**
     * Este metodo imprime la información de un AbonoCiclo.
     * 
     * @return Cadena que representa este objeto.
     */
    @Override
    public String toString() {
        return super.toString() + " ciclo: " + getCicloAbonado().getNombre()
                + " , el descuento que aplicar este abono es: " + getDescuento().toString();
    }
}
