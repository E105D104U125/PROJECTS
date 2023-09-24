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
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.*;

import obras.*;
import teatro.Teatro;
import zonas.PrecioZona;
import zonas.ZSNumerada;

/**
 * 
 * Clase que muestra la vista de cada evento
 *
 */
@SuppressWarnings("serial")
public class VistaEvento extends JPanel {
	private Evento ev;
	private JPanel jpCard;
	private JPanel elegirZona;
	private JPanel info;
	private RepresentacionEvento r;
	
	/**
	 * Constructor
	 * @param e evento asociado al panel
	 */
	public VistaEvento(Evento e) {
		
		this.setLayout(new BorderLayout());
		
		/* Panel que va a contener la información del evento y el combo box de las representaciones */
		info = new JPanel();
		GridBagConstraints constraints = new GridBagConstraints();
		info.setLayout(new GridBagLayout());
		
		/* La forma en que se muestran los eventos sigue un 
		 * patron definido */
		// Informacion general
		constraints.ipady = 10;
		constraints.ipadx = 180;
		constraints.gridx = 0;
		constraints.gridy = 0;
		info.add(new JLabel(e.toString()), constraints);
		
		// Precios por zona
		constraints.gridx = 1;
		constraints.gridy = 0;
		info.add(new JLabel(e.printPreciosZona()), constraints);
		
		// Especificidades
		constraints.gridx = 2;
		constraints.gridy = 0;
		info.add(new JLabel(e.toStringEspecifico()), constraints);
		
		// Descripcion - ocupa 3 celdas
		constraints.gridx = 0;
		constraints.gridy = 1;
		constraints.gridwidth = 3;
		e.getDescripcion();
		JLabel jl = new JLabel("<html><p style=\"width:500px\">" + e.getDescripcion() + "</p></html>");
		jl.setPreferredSize(new Dimension(600,100));
		info.add(jl, constraints);
		
		// Representaciones
		constraints.gridx = 1;
		constraints.gridy = 7;
		constraints.gridwidth = 1;
		JComboBoxRepresentaciones cbRepr=new JComboBoxRepresentaciones(e);
		info.add(cbRepr, constraints);
		
		this.add(info, BorderLayout.NORTH);
		
		/* CardLayout para pasar a la siguiente ventana destinada a comprar/reservar */
		jpCard = new JPanel();
		jpCard.setLayout(new CardLayout(0,0));
		this.add(jpCard, BorderLayout.CENTER);
		
		jpCard.add(info);
		

	}
	
	/* Desplegable que muestra las representaciones */
	public class JComboBoxRepresentaciones extends JPanel {
		
		/**
		* Constructor JComboBoxRepresentaciones
		* @param evento evento
		*/
		public JComboBoxRepresentaciones(Evento evento) {
			
			int i;
			String[] repr = new String[evento.getRepresentaciones().size()];
			
			this.setLayout(new GridBagLayout());	
			GridBagConstraints constraints = new GridBagConstraints();
			
			JLabel etiqueta = new JLabel("Seleccione una representacion");
			JButton boton = new JButtonRojo("Haga click para continuar");
			
			for(i=0; i<evento.getRepresentaciones().size(); i++) {
				repr[i]=evento.getRepresentaciones().get(i).toString();
			}
			
			final JComboBox combo = new JComboBox(repr);
			
			
			/* Si se hace click para continuar, se pasa a otra ventana */
			boton.addActionListener(
			  new ActionListener() {
			    public void actionPerformed(ActionEvent e) {
			    	int indxSelec = combo.getSelectedIndex();
			    	r = evento.getRepresentaciones().get(indxSelec);
			    	elegirZona = new VistaZonaEvento(evento,r);
			    	jpCard.add(elegirZona);
			    	//elegirZona.setVisible(false);
			    	info.setVisible(false);
			    	elegirZona.setVisible(true);
			    }
			  }
			);
			
			
			// Etiqueta
			constraints.ipady = 2;
			constraints.ipadx = 0;
			constraints.gridx = 1;
			constraints.gridy = 8;
			this.add(etiqueta, constraints);

			// Botón
			constraints.ipady = 2;
			constraints.ipadx = 0;
			constraints.gridx = 1;
			constraints.gridy = 15;
			this.add(boton, constraints);
			
			// Combo
			constraints.ipady = 2;
			constraints.ipadx = 0;
			constraints.gridx = 1;
			constraints.gridy = 12;
			this.add(combo, constraints);
						
		}
		
		/**
		* Método para obtener el panel

		* @return panel de esta clase
		*/
		private JPanel getPanel() {
			return this;
		}
	}


}
