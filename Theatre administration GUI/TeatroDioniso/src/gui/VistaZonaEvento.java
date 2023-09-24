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
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import obras.*;
import teatro.Teatro;
import zonas.*;

/**
 * Clase para seleccionar la zona de un evento
 */
public class VistaZonaEvento extends JPanel{
	
	private Zona eleccion;
	private JPanel entr;
	private JPanel jpElegir;
	private JPanel jpCard;
	
	/**
	 * Constructor 
	 * @param e evento
	 * @param r representación
	 */
	public VistaZonaEvento(Evento e, RepresentacionEvento r) {
		

		/* Panel que muestra las zonas a elegir */
		jpElegir = new JPanel();
		jpElegir.setLayout(new GridBagLayout());	
		GridBagConstraints constraints = new GridBagConstraints();
		
		ArrayList<JRadioButton> ops = new ArrayList<>();
		ArrayList<Zona> opsZona = new ArrayList<>();
		
		for(PrecioZona pz: e.getPreciosZonas()) {
			ops.add(new JRadioButton(pz.getZona().getNombre()));
			opsZona.add(pz.getZona());
		}
		
		JLabel etiqueta = new JLabel("Seleccione UNA zona:");
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
			      int i;
			      for(i=0; i<ops.size(); i++) {
			    	  if(ops.get(i).isSelected()){
							jpElegir.setVisible(false);
							eleccion=opsZona.get(i);
							if(eleccion instanceof ZSNumerada) {
								entr = new VistaEntradaNum(eleccion,r);
							}
							else if (eleccion instanceof ZSNoNumerada) {
								entr = new VistaEntradaNoNum(eleccion,r);
							}
							jpCard.add(entr, BorderLayout.CENTER);
					    	entr.setVisible(true);
					 }
			      }
		    }
		  }
		);
		
		
		// Etiqueta
		constraints.ipady = 2;
		constraints.ipadx = 0;
		constraints.gridx = 1;
		constraints.gridy = cont+2;
		jpElegir.add(etiqueta, constraints);

		// Botón
		constraints.ipady = 2;
		constraints.ipadx = 0;
		constraints.gridx = 1;
		constraints.gridy = cont+5;
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

