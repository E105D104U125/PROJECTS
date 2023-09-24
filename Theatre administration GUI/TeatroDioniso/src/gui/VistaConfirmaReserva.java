/** 
 * 
 * @author Olmar Arranz olmar.arranz@estudiante.uam.es 
 * @author Cristina Madrigal cristina.madrigalb@estudiante.uam.es 
 * @author Eduardo Terres Caballero eduardo.terres@estudiante.uam.es
 *  
 */
package gui;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import entradas.Abono;
import entradas.Reserva;
import es.uam.eps.padsof.telecard.OrderRejectedException;
import es.uam.eps.padsof.tickets.NonExistentFileException;
import es.uam.eps.padsof.tickets.UnsupportedImageTypeException;
import teatro.Teatro;

@SuppressWarnings("serial")
/**
 * 
 * Clase que representa la confirmación de reservas
 *
 */
public class VistaConfirmaReserva extends JPanel{
private int flag=-10;
	
	/**
	 * Constructor de VistaConfirmaReserva
	 * @param r Reserva que se realiza
	 */
	public VistaConfirmaReserva(Reserva r) {
		GridBagConstraints constraints = new GridBagConstraints();
		this.setLayout(new GridBagLayout());
		
		/* Texto que pide el número de tarjeta */
		JLabel labelPagar = new JLabel("Introduzca el numero de su tarjeta para pagar, por favor", SwingConstants.CENTER);
		labelPagar.setFont(new Font("", Font.ITALIC, 16));
		
		/* Barra para introducir datos de la tarjeta */
		JPanel jpBarraPagar = new JPanel();
		jpBarraPagar.setLayout(new FlowLayout());
		
		/* Botón pagar */
		JButtonRojo pagar = new JButtonRojo("Pagar");
		
		JTextField tarjeta = new JTextField();
		tarjeta.setPreferredSize(new Dimension(300,30));
		
		//Asociar acciones a botón pagar
		pagar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					flag = Teatro.getUsuarioLogueado().confirmarReserva(r, tarjeta.getText());
				} catch (NonExistentFileException | UnsupportedImageTypeException | OrderRejectedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				if(flag>=0) {
					JOptionPane.showMessageDialog(null, "¡Muchas gracias por su compra!");
				}
				else {
					JOptionPane.showMessageDialog(null, "Ha habido un error. Lo sentimos, intentelo de nuevo mas tarde");
				}
			}
		});
		
		jpBarraPagar.add(tarjeta);
		jpBarraPagar.add(pagar);
		
		constraints.insets=new Insets(20,0,0,0); //separación
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.gridwidth = 1; 
		constraints.gridheight = 1;
		this.add(labelPagar, constraints);
		
		constraints.gridx = 0;
		constraints.gridy = 1;
		this.add(jpBarraPagar, constraints);	
	}
}
