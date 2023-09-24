package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import entradas.EntradaNumerada;
import entradas.Ticket;
import entradas.TipoSeleccion;
import es.uam.eps.padsof.telecard.OrderRejectedException;
import es.uam.eps.padsof.tickets.NonExistentFileException;
import es.uam.eps.padsof.tickets.UnsupportedImageTypeException;
import obras.RepresentacionEvento;
import teatro.Teatro;
import usuarios.UsuarioRegistrado;
import zonas.*;

public class VistaEntradaNumMan extends JPanel{
	
	private int filas, columnas, i, j, flag=0;
	private boolean flag2=false;
	private ArrayList<Ticket> entradas = new ArrayList<>();
	private JCheckBox[][] butacas;
	
	public VistaEntradaNumMan(Zona z, RepresentacionEvento r) {
			
		filas=((ZSNumerada)z).getnFila();
		columnas=((ZSNumerada)z).getnColumna();
		
		int [][] casillas = new int[filas][columnas];
		
		butacas = new JCheckBox[filas][columnas];
		
		this.setLayout(new BorderLayout());

		JPanel jpCasillas = new JPanel();
		jpCasillas.setLayout(new GridBagLayout());
		GridBagConstraints constraints = new GridBagConstraints();
		
		JButtonRojo boton = new JButtonRojo("Confirmar");
		JButtonRojo reservar = new JButtonRojo("Reservar");

		
		this.add(new JLabel("Seleccione las butacas que desee comprar/reservar"), BorderLayout.NORTH);
		
		
		for(i=0; i<filas;i++) {
			for(j=0; j<columnas; j++) {
				JCheckBox jBox = new JCheckBox();
				if(r.btcDisponible(i+1, j+1, (ZSNumerada)z)==false) {
					jBox.setSelected(false);
					jBox.setEnabled(false);
					jBox.setBackground(new Color(255,0,0));
				}
				butacas[i][j] = jBox;
				constraints.gridx = j;
		        constraints.gridy = 1+i;
		        constraints.gridwidth = 1; 
		        constraints.gridheight = 1;
				jpCasillas.add(jBox, constraints);
			}
		}
		
		
		/* Texto que pide el número de tarjeta */
		JLabel labelPagar = new JLabel("Introduzca el numero de su tarjeta para pagar, por favor", SwingConstants.CENTER);
		labelPagar.setFont(new Font("", Font.ITALIC, 16));
		
		/* Barra para introducir datos de la tarjeta */
		JPanel jpBarraPagar = new JPanel();
		jpBarraPagar.setLayout(new GridBagLayout());
		
		/* Botón pagar */
		JButtonRojo pagar = new JButtonRojo("Pagar");
	    pagar.setVisible(false);
	    
	    JTextField tarjeta = new JTextField();
		tarjeta.setPreferredSize(new Dimension(300,30));
		
		boton.addActionListener(
		  new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		      pagar.setVisible(true);
		      jpBarraPagar.setVisible(true);
		      boton.setVisible(false);
			  reservar.setVisible(false);
		    }
		  }
		);
		
		//Asociar acciones a botón pagar
		pagar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				for(i=0; i<filas;i++) {
					for(j=0; j<columnas; j++) {
						if(butacas[i][j].isSelected()) {
							EntradaNumerada entr = r.ticketNum(i+1, j+1, (ZSNumerada)z);
							entradas.add(entr);
						}
					}
				}
				try {
					flag=Teatro.getUsuarioLogueado().comprarEntradas(entradas, tarjeta.getText());
				} catch (NonExistentFileException | UnsupportedImageTypeException | OrderRejectedException e1) {
					e1.printStackTrace();
				}
				if(flag>=0) {
					JOptionPane.showMessageDialog(null, "¡Muchas gracias por su compra!");
				}else if(flag==-2) {
					JOptionPane.showMessageDialog(null, "El numero de entradas supera el limite de entradas asociadas a esta representacion");
				}
				else{
					JOptionPane.showMessageDialog(null, "Ha habido un error. Lo sentimos, intentelo de nuevo mas tarde");
				}
				jpBarraPagar.setVisible(false);
				pagar.setVisible(false);
			    boton.setVisible(true);
			    reservar.setVisible(true);
			}
		});
			
		
		//Asociar acciones a botón reservar
		reservar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				for(i=0; i<filas;i++) {
					for(j=0; j<columnas; j++) {
						if(butacas[i][j].isSelected()) {
							EntradaNumerada entr = r.ticketNum(i+1, j+1, (ZSNumerada)z);
							entradas.add(entr);
						}
					}
				}
				if(Teatro.getUsuarioLogueado().realizarReserva(entradas)==true) {
					JOptionPane.showMessageDialog(null, "Reserva realizada con exito. Recuerde que puede confirmar/cancelar su reserva hasta dos dias antes de la fecha de la representacion");
				}else {
					JOptionPane.showMessageDialog(null, "Ha habido un error. Lo sentimos, intentelo de nuevo mas tarde");
				}
				jpBarraPagar.setVisible(false);
				pagar.setVisible(false);
			    boton.setVisible(true);
			}
		});
					
		constraints.gridx = 1;
		constraints.gridy = columnas+10;
		jpBarraPagar.add(labelPagar, constraints);
	
		constraints.gridx = 1;
		constraints.gridy = columnas+15;
		jpBarraPagar.add(tarjeta, constraints);
		
		constraints.gridx = 1;
		constraints.gridy =columnas+20;
		jpBarraPagar.add(pagar, constraints);	
		pagar.setVisible(false);
		
		
		JPanel jpSur = new JPanel();
		jpSur.setLayout(new BorderLayout());
		
		this.add(jpCasillas, BorderLayout.CENTER);
		
		jpSur.add(boton, BorderLayout.SOUTH);
		boton.setVisible(true);
		
		jpSur.add(reservar, BorderLayout.CENTER);
		reservar.setVisible(true);
		
		jpSur.add(jpBarraPagar, BorderLayout.NORTH);
		jpBarraPagar.setVisible(false);
		
		this.add(jpSur, BorderLayout.SOUTH);
        
		this.updateUI();
	}
	
}
