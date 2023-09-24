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
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import obras.Evento;
import teatro.Teatro;

/**
 * 
 * Clase que muestra los eventos
 *
 */
@SuppressWarnings("serial")
public class VistaEventosUs extends JPanel {

	private final int nEleMostrados = 12;
	private int offset;
	private JPanel p1;
	private JPanel most;
	private JPanel jpDcha;
	private JPanel jpSigPrev;
	private JButton bSiguiente;
	private JButton bPrevio;
	private JButton bVolver;
	private ArrayList<Evento> eventos;
	private ArrayList<VistaEventosUs.JButtonGestion> event_nom ;
	
	/**
	 * Constructor VistaEventosUs
	 */
	public VistaEventosUs() {
		
		this.setLayout(new BorderLayout());
		
		offset=0;
		eventos = Teatro.getEventos();
		
		/* Panel con eventos mostrados (solo nombre para elegir) */
		p1 = new JPanel();
		p1.setLayout(new GridLayout(3,4,0,0));
		this.add(p1, BorderLayout.WEST);
		p1.setBorder(new LineBorder(p1.getBackground(), 40));

		
		/* Panel donde van a a colocarse los botones siguiente, previo y volver cuando corresponda */
		jpSigPrev = new JPanel();
		jpSigPrev.setLayout(new BorderLayout());
		
		bSiguiente = new JButtonRojo("Siguiente");
		bPrevio = new JButtonRojo("Previo");
		
		jpSigPrev.add(bSiguiente, BorderLayout.SOUTH);
		jpSigPrev.add(bPrevio, BorderLayout.NORTH);
		this.add(jpSigPrev, BorderLayout.EAST);
		
		/* Botón para volver a todos los eventos cuando se ha entrado en uno concreto*/
		bVolver = new JButtonRojo("Volver");
		jpSigPrev.add(bVolver, BorderLayout.CENTER);
		this.add(jpSigPrev, BorderLayout.EAST);
		bVolver.setVisible(false);			
				
		
		/* CardLayout */
		jpDcha = new JPanel();
		jpDcha.setLayout(new CardLayout(0,0));
		this.add(jpDcha, BorderLayout.SOUTH);
		
	
		/* ArrayList con los nombres de los eventos */
		event_nom = new ArrayList<>();
		
		int cont=0;
		for(Evento e: eventos) {
			if(cont<nEleMostrados) {
				event_nom.add(new VistaEventosUs.JButtonGestion(e.getTitulo(), new JPanel()));
				cont++;
			}
		}
			

		for (VistaEventosUs.JButtonGestion ev :event_nom) {
			ev.setBackground(Color.WHITE);
			p1.add(ev,BorderLayout.CENTER);
			jpDcha.add(ev.getPanel(), ev.getNombre());
			ev.setPreferredSize(new Dimension(250,75));
		}
		
	
		for(VistaEventosUs.JButtonGestion ev: event_nom) {
			/* Si se pulsa un evento, cambiamos de pantalla para mostrar su información */
			/* También mostramos un botón para volver a la ventana anterior en la que se muestran todos los eventos */
			ev.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
				    CardLayout cl = (CardLayout)(jpDcha.getLayout());
				    cl.show(jpDcha, ev.getNombre());
				    most = new VistaEvento(eventos.get(event_nom.indexOf(ev)));
				    getPanel().add(most, BorderLayout.CENTER);
				    most.setVisible(true);
				    p1.setVisible(false);
				    bSiguiente.setVisible(false);
				    bPrevio.setVisible(false);
				    bVolver.setVisible(true);
				}
			});
		}
		
		bSiguiente.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (offset + nEleMostrados < eventos.size()) {
					offset = (offset + nEleMostrados) % eventos.size();
				}else {
					return;
				}
				actualizarEventosMostrados();
			}
		});

		bPrevio.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (offset < nEleMostrados) {
					// Primera pagina
					offset = 0;
					return;
				} else {
					offset = Math.abs(offset - nEleMostrados) % eventos.size();
				}
				actualizarEventosMostrados();
			}
		});
		
		
		bVolver.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				CardLayout cl = (CardLayout)(jpDcha.getLayout());
			    p1.setVisible(true);
			    most.setVisible(false);
			    bSiguiente.setVisible(true);
			    bPrevio.setVisible(true);
			    bVolver.setVisible(false);
			}
		});
		
		this.setVisible(true);
		
	}

	/**
	* Método para obtener el panel

	* @return panel de esta clase
	*/
	private JPanel getPanel() {
		return this;
	}
	
	/**
	* Método para actualizar los eventos en cada página
	*/
	private void actualizarEventosMostrados() {
		int i;
		p1.removeAll();
		VistaEventosUs.JButtonGestion event;
	
		for (i = 0; i < nEleMostrados && i < eventos.size() && (i + offset) < eventos.size(); i++) {
			event_nom.add(new VistaEventosUs.JButtonGestion(eventos.get((offset + i) % eventos.size()).getTitulo(), new JPanel()));
			event = event_nom.get((offset + i) % eventos.size());
			event.setBackground(Color.WHITE);
			p1.add(event,BorderLayout.CENTER);
			event.setPreferredSize(new Dimension(250,75));
		}
		
		for(VistaEventosUs.JButtonGestion ev: event_nom) {
			/* Si se pulsa un evento, cambiamos de pantalla para mostrar su información */
			/* También mostramos un botón para volver a la ventana anterior en la que se muestran todos los eventos */
			ev.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
				    CardLayout cl = (CardLayout)(jpDcha.getLayout());
				    cl.show(jpDcha, ev.getNombre());
				    most = new VistaEvento(eventos.get(event_nom.indexOf(ev)));
				    getPanel().add(most, BorderLayout.CENTER);
				    most.setVisible(true);
				    p1.setVisible(false);
				    bSiguiente.setVisible(false);
				    bPrevio.setVisible(false);
				    bVolver.setVisible(true);
				}
			});
		}
			
		p1.updateUI();	
	}
	
	/**
	* 
	* Clase que representa el panel JPVista
	*
	*/
	public static class JPVista extends JPanel {
		private JButton jb;

		/**
		 * Constructor de JPVista
		 * @param str cadena para la etiqueta
		 * @param str2 cadena para el botón
		 */
		public JPVista (String str, String str2) {
			this.setLayout(new BorderLayout());
			this.add(new JLabel(str), BorderLayout.CENTER);
			this.jb = new JButton(str2);
			this.add(jb, BorderLayout.CENTER);
		}
		
		/**
		* Método para obtener el botón

		* @return botón de este panel
		*/
		public JButton getButton() {
			return this.jb;
		}
	}
	
	public static class JButtonGestion extends JButton {
		// Panel que muestra al pulsar el boton
		private JPanel panel;
		private String nombre;
		
		
		public JButtonGestion(String str, JPanel panel) {
			this.setText(str);
			this.panel = panel;
			this.panel.add(new JLabel(str),BorderLayout.CENTER);
			this.nombre = str;
		}
		
		/**
		* Método para obtener el panel

		* @return panel de esta clase
		*/
		public JPanel getPanel() {
			return this.panel;
		}
		
		public String getNombre() {
			return this.nombre;
		}
	}
}

