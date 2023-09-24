/** 
 * 
 * @author Olmar Arranz olmar.arranz@estudiante.uam.es 
 * @author Cristina Madrigal cristina.madrigalb@estudiante.uam.es 
 * @author Eduardo Terres Caballero eduardo.terres@estudiante.uam.es
 *  
 */
package gui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import obras.RepresentacionEvento;
import zonas.Butaca;
import zonas.PrecioZona;
import zonas.ZSNoNumerada;
import zonas.ZSNumerada;
import zonas.Zona;

/**
 * 
 * Clase que representa el panel para las entradas numeradas
 *
 */
public class VistaEntradaNum extends JPanel{

	private JPanel selec;
	private JPanel jpElegir;
	private JPanel jpCard;
	
	/**
	 * Constructor VistaEntradaNum
	 * @param z zona
	 * @param r reserva
	 */
	public VistaEntradaNum(Zona z, RepresentacionEvento r){
		
		/* Panel que muestra los tipos de compra a elegir */
		jpElegir = new JPanel();
		jpElegir.setLayout(new GridBagLayout());	
		GridBagConstraints constraints = new GridBagConstraints();
		
		ArrayList<JRadioButton> ops = new ArrayList<>();
		
		ops.add(new JRadioButton("Seleccion automatica"));
		ops.add(new JRadioButton("Seleccion manual"));
			
		JLabel etiqueta = new JLabel("Seleccione UN tipo de seleccion de butacas:");
		JButton boton   = new JButtonRojo("Haga click para continuar");
		ops.get(0).setSelected(true);
	

		// Crear un grupo para las opciones, el cual garantizará
		// que sólo una de las opciones está seleccionada
		ButtonGroup grupo = new ButtonGroup();
		// Añadir las opciones al grupo
		int cont=0;
		for(JRadioButton rb: ops) {
			grupo.add(rb);
			constraints.ipady = 2;
			constraints.ipadx = 0;
			constraints.gridx = 1;
			constraints.gridy = 12+cont;
			jpElegir.add(rb, constraints);
			cont++;
		}
		
		// asociar acciones a componentes
		boton.addActionListener(
			new ActionListener() {
				public void actionPerformed(ActionEvent e) {
			      if(ops.get(0).isSelected()){
			    	  selec = new VistaEntradaNumAutom(z,r);
			      }
			      else if(ops.get(1).isSelected()) {
			    	  selec = new VistaEntradaNumMan(z,r);
			      }
			  
				jpCard.add(selec, BorderLayout.CENTER);
				jpElegir.setVisible(false);
				getPanel().removeAll();
				getPanel().add(selec);
		    	selec.setVisible(true);
		    }
		  }
		);
		
		
		// Etiqueta
		constraints.ipady = 2;
		constraints.ipadx = 0;
		constraints.gridx = 1;
		constraints.gridy = 8;
		jpElegir.add(etiqueta, constraints);

		// Botón
		constraints.ipady = 2;
		constraints.ipadx = 0;
		constraints.gridx = 1;
		constraints.gridy = 15;
		jpElegir.add(boton, constraints);
		
		/* CardLayout para pasar a la siguiente ventana destinada a comprar/reservar */
		jpCard = new JPanel();
		jpCard.setLayout(new CardLayout(0,0));
		jpCard.setVisible(true);
		this.add(jpCard, BorderLayout.CENTER);
		
		jpCard.add(jpElegir, BorderLayout.CENTER);
    	
	}
	
	/**
	* Método para obtener el panel

	* @return panel de esta clase
	*/
	private JPanel getPanel() {
		return this;
	}

}
