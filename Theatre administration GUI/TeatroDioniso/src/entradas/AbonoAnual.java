/** 
 * 
 * @author Olmar Arranz olmar.arranz@estudiante.uam.es 
 * @author Cristina Madrigal cristina.madrigalb@estudiante.uam.es 
 * @author Eduardo Terrés Caballero eduardo.terres@estudiante.uam.es
 *  
 */

package entradas;

import zonas.*;
import java.time.*;
import teatro.*;

/**
 * Clase que representa todos los tipos de abonos anuales.
 */
public class AbonoAnual extends Abono {

	private static final long serialVersionUID = 3705163226781791865L;
	private LocalDate fechaIni;
	private LocalDate fechaFin;

	/**
	 * Constructor de la clase AbonoAnual
	 * 
	 * @param precio   precio del abono anual
	 * @param zona     zona asociada al abono anual
	 * @param fechaIni fecha de inicio del abono anual
	 */
	public AbonoAnual(double precio, Zona zona, LocalDate fechaIni) {
		super(precio, zona);
		this.fechaIni = fechaIni;
		this.fechaFin = fechaIni.plusDays(365);
		Teatro.addAbonos(this);
	}

	/**
	 * Este método devuelve la fecha de inicio del abono anual.
	 * 
	 * @return Fecha que indica la fecha de inicio del abono anual.
	 */
	public LocalDate getFechaIni() {
		return this.fechaIni;
	}

	/**
	 * Este método asigna la fecha de inicio del abono anual.
	 * 
	 * @param fechaIni Fecha de inicio del abono anual.
	 */

	public void setFechaIni(LocalDate fechaIni) {
		this.fechaIni = fechaIni;
		this.fechaFin = fechaIni.plusDays(365);
	}

	/**
	 * Este método devuelve la fecha de fin del abono anual.
	 * 
	 * @return Fecha que indica la fecha de fin del abono anual.
	 */
	public LocalDate getFechaFin() {
		return this.fechaFin;
	}

	/**
	 * Este método imprime la información de un AbonoAnual.
	 * 
	 * @return Cadena que representa este objeto.
	 */
	@Override
	public String toString() {
		return super.toString() + ", la fecha inicial del abono es: " + getFechaIni() + " , y por tanto la final es: "
				+ getFechaFin();
	}

}
