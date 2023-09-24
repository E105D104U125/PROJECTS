/** 
 * 
 * @author Olmar Arranz olmar.arranz@estudiante.uam.es 
 * @author Cristina Madrigal cristina.madrigalb@estudiante.uam.es 
 * @author Eduardo Terres Caballero eduardo.terres@estudiante.uam.es
 *  
 */
package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

import javax.swing.*;

import entradas.Abono;
import es.uam.eps.padsof.telecard.OrderRejectedException;
import es.uam.eps.padsof.tickets.NonExistentFileException;
import es.uam.eps.padsof.tickets.UnsupportedImageTypeException;
import obras.*;
import teatro.Teatro;
import zonas.PrecioZona;
import zonas.ZSNumerada;

/**
 * 
 * Clase que representa la compra de un abono
 *
 */
public class VistaCompraAbono extends JPanel {
	private int flag=-10;
	
	/**
	 * Constructor de VistaCompraAbono
	 * @param a Abono que se compra
	 */
	public VistaCompraAbono(Abono a) {
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
					flag = Teatro.getUsuarioLogueado().comprarAbono(a, tarjeta.getText());
				} catch (NonExistentFileException | UnsupportedImageTypeException | OrderRejectedException e1) {
					e1.printStackTrace();
				}
				if(flag>=0) {
					JOptionPane.showMessageDialog(null, "¡Muchas gracias por su compra!");
					getPanel().setVisible(false);
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
		
		this.setVisible(true);
	}
	
	/**
	* Método para obtener el panel

	* @return panel de esta clase
	*/
	private JPanel getPanel() {
		return this;
	}


}
