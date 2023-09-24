/** 
 * 
 * @author Olmar Arranz olmar.arranz@estudiante.uam.es 
 * @author Cristina Madrigal cristina.madrigalb@estudiante.uam.es 
 * @author Eduardo Terres Caballero eduardo.terres@estudiante.uam.es
 *  
 */
package gui;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

import entradas.Abono;
import entradas.AbonoCiclo;

/**
 * 
 * Clase que representa el panel de la visión de los precios, zonas y descuentos de los abonos
 *
 */
public class VistaAbono extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3072498192321169382L;
	/**
	 * Constructor de VistaAbono
	 * @param a abono asociado al panel
	 */
	public VistaAbono(Abono a) {
		GridBagConstraints constraints = new GridBagConstraints();
		this.setLayout(new GridBagLayout());
		
		/* La forma en que se muestran los abonos sigue un patron definido */
		// Informacion general
		constraints.ipady = 10;
		constraints.ipadx = 180;
		constraints.gridx = 0;
		constraints.gridy = 0;
		JLabel zona = new JLabel("ZONA: "+a.getZonaAbono().getNombre());
		Font t = new Font("", Font.CENTER_BASELINE, 15);
		zona.setFont(t);
		this.add(zona, constraints);
		
		// Precios por zona
		constraints.gridx = 1;
		constraints.gridy = 0;
		JLabel precio = new JLabel("PRECIO: "+a.getPrecioAbono());
		zona.setFont(t);
		this.add(precio, constraints);
		
		if(a instanceof AbonoCiclo) {
			JLabel desc = new JLabel("DESCUENTO: "+((AbonoCiclo) a).getDescuento()*100+"%");
			desc.setFont(t);
			constraints.gridx = 2;
			constraints.gridy = 0;
			this.add(desc, constraints);
		}
	}
}
