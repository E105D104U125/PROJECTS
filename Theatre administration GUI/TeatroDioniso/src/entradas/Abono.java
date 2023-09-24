/** 
 * 
 * @author Olmar Arranz olmar.arranz@estudiante.uam.es 
 * @author Cristina Madrigal cristina.madrigalb@estudiante.uam.es 
 * @author Eduardo Terrés Caballero eduardo.terres@estudiante.uam.es
 *  
 */

package entradas;

import zonas.*;

import java.io.Serializable;

/**
 * Clase que representa todos los tipos de abonos.
 */
public abstract class Abono implements Serializable {

	private static final long serialVersionUID = -557703909405352492L;
	private double precio;
	private Zona zona;

	/**
	 * Constructor de la clase Abono
	 * 
	 * @param ePrecio precio del abono
	 * @param zona    zona asociada al abono
	 */
	public Abono(double ePrecio, Zona zona) {
		setPrecioAbono(ePrecio);
		this.zona = zona;
	}

	/**
	 * Este método devuelve el precio de un abono
	 * 
	 * @return precio del abono
	 */
	public double getPrecioAbono() {
		return this.precio;
	}

	/**
	 * Este método asigna un precio a un abono
	 * 
	 * @param ePrecio precio de un abono
	 */
	public void setPrecioAbono(double ePrecio) {
		if (ePrecio < 0) {
			this.precio = 0;
			return;
		}
		this.precio = ePrecio;
	}

	/**
	 * Este método devuelve el zona abono
	 * 
	 * @return zona del abono
	 */
	public Zona getZonaAbono() {
		return this.zona;
	}

	/**
	 * Este método asigna una zona a un abono
	 * 
	 * @param eZona zona del abono
	 */
	public void setZonaAbono(Zona eZona) {
		if (eZona == null) {
			return;
		}
		this.zona = eZona;

	}

	/**
	 * Este método imprime la información de un Abono.
	 * 
	 * @return Cadena que representa este objeto.
	 */
	@Override
	public String toString() {
		String s = "Precio del abono: " + Double.toString(getPrecioAbono()) + ", zona: " + getZonaAbono().getNombre();
		return s;
	}

}
