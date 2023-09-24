/** 
 * 
 * @author Olmar Arranz olmar.arranz@estudiante.uam.es 
 * @author Cristina Madrigal cristina.madrigalb@estudiante.uam.es 
 * @author Eduardo Terrés Caballero eduardo.terres@estudiante.uam.es
 *  
 */

/**
 * Clase que representa todos los tipos de estadísticas de representaciones.
 */
package estadisticas;

public class EstadisticaRepresentacion extends Estadistica {
	private static final long serialVersionUID = 2167547294632967774L;
	private int ocupacion;

	/**
	 * 
	 * @param rec       recaudación de la representación
	 * @param ocupacion ocupación de la representación
	 */
	public EstadisticaRepresentacion(double rec, int ocupacion) {
		super(rec);
		this.setOcupacion(ocupacion);
	}

	/**
	 * Este método devuelve la ocupación de la estadística de una representación
	 * 
	 * @return ocupacion de la estadística de una representacion
	 */
	public int getOcupacion() {
		return this.ocupacion;
	}

	/**
	 * Este método asigna una ocupación a una estadística
	 * 
	 * @param ocupacion ocupación de una representación
	 */
	public void setOcupacion(int ocupacion) {
		if (ocupacion >= 0) {
			this.ocupacion = ocupacion;
		} else {
			this.ocupacion = 0;
		}
	}

	/**
	 * Este método imprime la información de una EstadísticaZona.
	 * 
	 * @return Cadena que representa este objeto.
	 */
	@Override
	public String toString() {
		// No llama a super.toString() para evitar el salto de linea
		return "Recaudacion: " + this.getRecaudacion() + " Ocupacion: " + this.getOcupacion() + "\n";
	}
}
