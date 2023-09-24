/** 
 * 
 * @author Olmar Arranz olmar.arranz@estudiante.uam.es 
 * @author Cristina Madrigal cristina.madrigalb@estudiante.uam.es 
 * @author Eduardo Terrés Caballero eduardo.terres@estudiante.uam.es
 *  
 */
package entradas;

import obras.*;
import zonas.*;

/**
 * Clase que representa todos los tipos de entradas numeradas.
 */
public class EntradaNumerada extends Ticket {

    private static final long serialVersionUID = -4259368539720449657L;
    private Butaca butaca;

    /**
     * Constructor de EntradaNumerada
     * 
     * @param precioZona     precio de la zona al que pertenece la entrada
     * @param bloqueada      estado de la entrada
     * @param representacion representación del evento al que pertenece la entrada
     * @param butaca         asociada a la entrada
     */
    public EntradaNumerada(PrecioZona precioZona, boolean bloqueada, RepresentacionEvento representacion,
            Butaca butaca) {
        super(precioZona, bloqueada, representacion);
        this.butaca = butaca;
    }

    /**
     * Este método devuelve la butaca de una entrada numerada
     * 
     * @return Butaca que representa la butaca de una entrada numerada
     */
    public Butaca getButaca() {
        return this.butaca;
    }

    /**
     * Este método asigna una butaca a una entrada numerada
     * 
     * @param butaca butaca de la entrada
     */
    public void setButaca(Butaca butaca) {
        if (butaca == null) {
            return;
        }
        this.butaca = butaca;
    }

    /**
     * Este método imprime la información de un EntradaNumerada.
     * 
     * @return Cadena que representa este objeto.
     */
    @Override
    public String toString() {
        String s;
        s = super.toString();
        s += "Butaca asociada a la entrada: " + this.getButaca() + ".\n";
        return s;
    }

}
