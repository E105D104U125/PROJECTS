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
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import es.uam.eps.padsof.telecard.OrderRejectedException;
import es.uam.eps.padsof.tickets.NonExistentFileException;
import es.uam.eps.padsof.tickets.UnsupportedImageTypeException;
import obras.RepresentacionEvento;
import teatro.Teatro;
import zonas.ZSNoNumerada;
import zonas.Zona;

/**
 * 
 * Clase que representa el panel para las entradas no numeradas
 *
 */
public class VistaEntradaNoNum extends JPanel{
	
	private int flag=-10;
	private boolean flag2=false;
	private int entradas;
	
	/**
	 * Constructor VistaEntradaNoNum
	 * @param z zona
	 * @param r reserva
	 */
	public VistaEntradaNoNum(Zona z, RepresentacionEvento r){
		
		this.setLayout(new BorderLayout());
		
		/* Texto que pide el número de entradas */
		JLabel labelIndicacion = new JLabel("Introduzca el numero de entradas que desea comprar/reservar", SwingConstants.CENTER);
		labelIndicacion.setFont(new Font("", Font.ITALIC, 16));
		
		/* Barra para introducir datos */
		JPanel jpBarra = new JPanel();
		jpBarra.setLayout(new GridBagLayout());
		GridBagConstraints constraints = new GridBagConstraints();
		
		/* Botón comprar */
		JButtonRojo comprar = new JButtonRojo("Comprar");
		/* Botón reservar */
		JButtonRojo reservar = new JButtonRojo("Reservar");
		/* Botón lista de espera */
		JButtonRojo listEspera = new JButtonRojo("Apuntarse a la lista de espera");
		
		
		/* Texto que pide el número de tarjeta */
		JLabel labelPagar = new JLabel("Introduzca el numero de su tarjeta para pagar, por favor", SwingConstants.CENTER);
		labelPagar.setFont(new Font("", Font.ITALIC, 16));
		
		/* Barra para introducir datos de la tarjeta */
		JPanel jpBarraPagar = new JPanel();
		jpBarraPagar.setLayout(new GridBagLayout());
		GridBagConstraints constraints2 = new GridBagConstraints();
		
		/* Botón pagar */
		JButtonRojo pagar = new JButtonRojo("Pagar");
		
		
		JTextField texto = new JTextField();
		texto.setPreferredSize(new Dimension(300,30));
		
		JTextField tarjeta = new JTextField();
		tarjeta.setPreferredSize(new Dimension(300,30));
		
		//Asociar acciones a botón comprar
		comprar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				boolean notValid = false;
				try {
					entradas = Integer.parseInt(texto.getText());
				} catch (NumberFormatException excep) {
					notValid = true;
				}
				if (!notValid) {
					if(entradas<=0) {
						JOptionPane.showMessageDialog(null, "El numero de entradas para comprar debe ser superior a 0");
					}
					else if(Teatro.getUsuarioLogueado()==null){
					     JOptionPane.showMessageDialog(null, "Para comprar entradas debe estar registrado y haber iniciado sesion");
					}else {
						jpBarraPagar.setVisible(true);
						pagar.setVisible(true);
					}
				}
			}
		});
		
		//Asociar acciones a botón reservar
		reservar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				boolean notValid = false;
				try {
					entradas = Integer.parseInt(texto.getText());
				} catch (NumberFormatException excep) {
					notValid = true;
				}
				if (!notValid) {
					if(entradas<=0) {
						JOptionPane.showMessageDialog(null, "El numero de entradas a reservar debe ser superior a 0");
					}
					else if(Teatro.getUsuarioLogueado()==null){
					     JOptionPane.showMessageDialog(null, "Para reservar entradas debe estar registrado y haber iniciado sesion");
					}else {
						flag2 = Teatro.getUsuarioLogueado().realizarReserva((ZSNoNumerada)z, r, entradas);
						if(flag2==true) {
							JOptionPane.showMessageDialog(null, "¡Muchas gracias por su reserva! Recuerde que puede confirmarla o cancelarla hasta 2 dias antes de la representacion");
						}else {
							JOptionPane.showMessageDialog(null, "Ha habido un error. Lo sentimos, intentelo de nuevo mas tarde");					
						}
					}
				}
			}
		});
		
		//Asociar acciones a botón apuntarse a lista de espera
		listEspera.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(Teatro.getUsuarioLogueado()==null){
					JOptionPane.showMessageDialog(null, "Para apuntarse a la lista de espera de una representacion debe estar registrado y haber iniciado sesion");					
				}else {
					Teatro.getUsuarioLogueado().apuntarListaEspera(r);
					JOptionPane.showMessageDialog(null, "Añadido a la lista de espera con exito. Le avisaremos si alguna entrada pasa a estar disponible. Gracias");
				}
			}
		});
		
		//Asociar acciones a botón pagar
		pagar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					flag = Teatro.getUsuarioLogueado().comprarEntradas((ZSNoNumerada)z,r,entradas,tarjeta.getText());
				} catch (NonExistentFileException | UnsupportedImageTypeException | OrderRejectedException e1) {
					e1.printStackTrace();
				}
				if(flag>=0) {
					JOptionPane.showMessageDialog(null, "¡Muchas gracias por su compra!");
				}else if(flag==-6){
					JOptionPane.showMessageDialog(null, "No hay entradas suficientes. Puede apuntarse a la lista de espera si lo desea");
					listEspera.setVisible(true);
				}else if(flag==-2) {
					JOptionPane.showMessageDialog(null, "El numero de entradas disponibles supera el limite de entradas asociadas a esta representacion");
				}
				else {
					JOptionPane.showMessageDialog(null, "Ha habido un error. Lo sentimos, intentelo de nuevo mas tarde");
				}
				jpBarraPagar.setVisible(false);
				pagar.setVisible(false);
			}
		});
		
		constraints.ipady = 2;
		constraints.ipadx = 0;
		constraints.gridx = 1;
		constraints.gridy = 10;
		jpBarra.add(labelIndicacion, constraints);
		
		constraints.ipady = 2;
		constraints.ipadx = 0;
		constraints.gridx = 1;
		constraints.gridy = 13;
		jpBarra.add(texto, constraints);
		
		constraints.ipady = 2;
		constraints.ipadx = 0;
		constraints.gridx = 1;
		constraints.gridy = 17;
		jpBarra.add(reservar, constraints);	
		
		constraints.ipady = 2;
		constraints.ipadx = 0;
		constraints.gridx = 1;
		constraints.gridy = 19;
		jpBarra.add(comprar, constraints);
		
		constraints.ipady = 2;
		constraints.ipadx = 0;
		constraints.gridx = 1;
		constraints.gridy = 30;
		jpBarra.add(listEspera, constraints);
		listEspera.setVisible(false);
		
		constraints.ipady = 2;
		constraints.ipadx = 0;
		constraints.gridx = 1;
		constraints.gridy = 50;
		jpBarraPagar.add(labelPagar, constraints);
		
		constraints.ipady = 2;
		constraints.ipadx = 0;
		constraints.gridx = 1;
		constraints.gridy = 70;
		jpBarraPagar.add(tarjeta, constraints);
		
		
		JPanel vacio = new JPanel();
        JLabel truco = new JLabel("  ");
        truco.setPreferredSize(new Dimension(300,30));
        vacio.add(truco, BorderLayout.SOUTH);
        constraints.gridx =1; 
        constraints.gridy = 75; 
        constraints.gridwidth = 2; 
        constraints.gridheight = 2;
        jpBarra.add(vacio, constraints);
		
		constraints.ipady = 2;
		constraints.ipadx = 0;
		constraints.gridx = 1;
		constraints.gridy =74;
		jpBarraPagar.add(pagar, constraints);	
		pagar.setVisible(false);

		this.add(jpBarra, BorderLayout.CENTER);
		this.add(jpBarraPagar, BorderLayout.SOUTH);
		jpBarraPagar.setVisible(false);

	}
	
}
