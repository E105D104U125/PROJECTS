/** 
 * 
 * @author Olmar Arranz olmar.arranz@estudiante.uam.es 
 * @author Cristina Madrigal cristina.madrigalb@estudiante.uam.es 
 * @author Eduardo Terrés Caballero eduardo.terres@estudiante.uam.es
 *  
 */
package zonas;

import teatro.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Clase que representa todos los tipos de deshabilitación butacas.
 */
public class DeshabilitacionButaca implements Serializable {
    private static final long serialVersionUID = -4721298599284408647L;
    private String motivo;
    private LocalDate fIni;
    private LocalDate fFin;

    /**
     * Constructor DeshabilitacionButaca
     * 
     * @param motivo motivo de la deshabilitacion
     * @param fIni   fecha de inicio de la deshabilitacion
     * @param fFin   fecha de finalizacion de la deshabilitacion
     */
    public DeshabilitacionButaca(String motivo, LocalDate fIni, LocalDate fFin) {
        this.motivo = motivo;
        this.setfFin(fFin);
        this.setfIni(fIni);
        Teatro.addBtcDeshabilitadas(this);
    }

    /**
     * Este método devuelve el motivo de la deshabilitación de una butaca
     * 
     * @return motivo de la deshabilitación de una butaca
     */
    public String getMotivo() {
        return this.motivo;
    }

    /**
     * Este método asigna el motivo de la deshabilitación de una butaca
     *
     * @param motivo motivo de la deshabilitación de una butaca
     */
    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    /**
     * Este método devuelve la fecha de inicio de la deshabilitación de una butaca
     * 
     * @return fecha de inicio de la deshabilitacion
     */
    public LocalDate getfIni() {
        return this.fIni;
    }

    /**
     * Este método devuelve la fecha de finalización de la deshabilitación de una
     * butaca
     * 
     * @return fecha de finalizacion de la deshabilitacion
     */
    public LocalDate getfFin() {
        return this.fFin;
    }

    /**
     * Este método asigna el valor de fecha de inicio de la deshabilitación de una
     * butaca
     * 
     * @param fIni fecha de inicio de la deshabilitacion
     */
    private void setfIni(LocalDate fIni) {
        if (this.getfFin() == null)
            return;
        if (ChronoUnit.DAYS.between(fIni, this.getfFin()) < 0 || ChronoUnit.DAYS.between(LocalDate.now(), fIni) < 0) {
            return;
        }
        this.fIni = fIni;
    }

    /**
     * Este método asigna el valor de fecha de finalización de la deshabilitación de
     * una butaca
     * 
     * @param fFin fecha de finalización de la deshabilitacion
     */
    private void setfFin(LocalDate fFin) {
        if (ChronoUnit.DAYS.between(LocalDate.now(), fFin) < 0) {
            return;
        }
        this.fFin = fFin;
    }

    /**
     * Este método imprime la información de una Butaca.
     * 
     * @return Cadena que representa este objeto.
     */
    @Override
    public String toString() {
        String s;
        if (this.getfIni() == null || this.getfFin() == null) {
            s = "Esta deshabilitación tiene fechas incorrectas\n";
        } else {
            s = "Motivo de la deshabilitación: " + getMotivo() + ". Fecha de inicio de la deshabilitación: " + getfIni()
                    + ". Fecha de fin de la deshabilitación: " + getfFin() + ".\n";
        }
        return s;
    }
}
