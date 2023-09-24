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
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

import entradas.Abono;
import entradas.AbonoAnual;
import obras.Evento;
import teatro.Teatro;
import zonas.ZSNoNumerada;

/**
 * 
 * Clase que representa el panel para los abonos del usuario
 *
 */
public class VistaAbonosUsuario extends JPanel{
	
	/**
	 * Constructor de VistaAbonosUsuario
	 */
	public VistaAbonosUsuario() {
		this.setLayout(new BorderLayout());
		
		
		String[] Abonos = {
		         "Abono Anual",
		         "Abono Ciclo"};
		
		JPanel jpBotones = new JPanel();
		jpBotones.setLayout(new GridLayout(2,0, 40, 40));
		
		ArrayList<VistaAbonosUsuario.JButtonGestion> botones = new ArrayList<>();
		
		// Cambiar por los JPanel especificos
		botones.add(new VistaAbonosUsuario.JButtonGestion(Abonos[0], new JPanel()));
		botones.add(new VistaAbonosUsuario.JButtonGestion(Abonos[1], new JPanel()));
	
		JPanel jpDcha = new JPanel();
		jpDcha.setLayout(new CardLayout(0,0));
		
		for (VistaAbonosUsuario.JButtonGestion bt : botones) {
			bt.setBackground(Color.WHITE);
			jpBotones.add(bt);
			jpDcha.add(bt.getPanel(), bt.getNombre());
			bt.setPreferredSize(new Dimension(300,300));
		}
		
		//para separarlo del borde
		jpBotones.setBorder(new LineBorder(jpBotones.getBackground(), 40));
		
		// Se añaden
		this.add(jpBotones, BorderLayout.WEST);
		this.add(jpDcha, BorderLayout.EAST);
		
		//CardLayout con lo que se escribe en cada botón
		JPanel jpCentro = new JPanel();
		jpCentro.setLayout(new CardLayout(0,0));
		JPanel vaa = new VistaAbonosAnual();
		JPanel vac = new VistaAbonosCiclo();
		
		jpCentro.add(vac);
		jpCentro.add(vaa);
		this.add(jpCentro, BorderLayout.CENTER);
		jpCentro.setVisible(false);
		
		for(VistaAbonosUsuario.JButtonGestion bt : botones) {
			//Accedemos al panel asociado al boton
			bt.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
				    CardLayout cl = (CardLayout)(jpDcha.getLayout());
				    cl.show(jpDcha, bt.getNombre());
				    jpCentro.setVisible(true);
				    if(bt.getNombre().equals("Abono Anual")) {
				    	vaa.setVisible(true);
				    	vac.setVisible(false);
				    }
				    //Abono Ciclo
				    else {
				    	vac.setVisible(true);
				    	vaa.setVisible(false);	
				    }
				    
				}
			});
		}
		
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
		private String s="Hola";
		
		public JButtonGestion(String str, JPanel panel) {
			//Nombre que aparecerá en el botón
			this.setText(str);
			this.panel = panel;
			this.nombre = str;
		}
		
		/**
		* Método para obtener el panel

		* @return panel de esta clase
		*/
		public JPanel getPanel() {
			return this.panel;
		}
		
		/**
		* Método para obtener el nombre

		* @return nombre de este panel
		*/
		public String getNombre() {
			return this.nombre;
		}
	}
}