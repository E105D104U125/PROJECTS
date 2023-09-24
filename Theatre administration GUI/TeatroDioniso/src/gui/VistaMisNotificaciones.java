/** 
 * 
 * @author Olmar Arranz olmar.arranz@estudiante.uam.es 
 * @author Cristina Madrigal cristina.madrigalb@estudiante.uam.es 
 * @author Eduardo Terres Caballero eduardo.terres@estudiante.uam.es
 *  
 */
package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import usuarios.Notificacion;
import usuarios.UsuarioRegistrado;

/**
* Clase para visualizar mis notificaciones
*/
public class VistaMisNotificaciones extends JPanel{
	
	/**
	* Constructor 
	* @param u usuario registrado del que se visualizan las notificaciones
	*/
	public VistaMisNotificaciones(UsuarioRegistrado u) {
		
		this.setLayout(new BorderLayout());
		
		String[] nots = new String[u.getNotificaciones().size()];
		int i=0;
		
		JPanel panelBotones = new JPanel();
		panelBotones.setLayout(new GridBagLayout());

		GridBagConstraints constraints = new GridBagConstraints();
		
		final JFrame ventana = new JFrame();
		
		for(Notificacion n: u.getNotificaciones()) {
			nots[i]=n.mostrarNotificacion();
			i++;
		}
		
		
		JList lista = new JList(nots);
		lista.setPreferredSize(new Dimension(600,50));
		panelBotones.setVisible(true);
		
		
        JLabel etiqueta = new JLabel("NOTIFICACIONES");
        etiqueta.setFont(new Font("", Font.ITALIC, 16));
		this.add(etiqueta);
		
		JScrollPane scroll = new JScrollPane(lista);
        
		// Añadir el scroll con la lista al panel donde se vaya a mostrar
		JPanel notsList = new JPanel();
		notsList.add(scroll);
		
		// asociar acciones a componentes
		lista.addListSelectionListener(
		  new ListSelectionListener() {
		    public void valueChanged(ListSelectionEvent ev) {
		    	JList localJList = (JList) ev.getSource();
		    	if (! ev.getValueIsAdjusting() ) {
		    	  String valorSeleccionado = 
		    			         (String) lista.getSelectedValue();
		          JOptionPane.showMessageDialog(ventana, "Visualizando la notificacion: " 
		    			                               + valorSeleccionado);
		    	}
		    }
		  }
		);
		
		constraints.gridx = 0;
		constraints.gridy = 1;
		constraints.gridwidth = 1; 
		constraints.gridheight = 1;
		panelBotones.add(notsList, constraints);
		
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.gridwidth = 1; 
		constraints.gridheight = 1;
		panelBotones.add(etiqueta, constraints);
		notsList.setVisible(true);
		this.add(panelBotones, BorderLayout.CENTER);
		
        
	}
}
