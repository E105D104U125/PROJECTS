/** 
 * 
 * @author Olmar Arranz olmar.arranz@estudiante.uam.es 
 * @author Cristina Madrigal cristina.madrigalb@estudiante.uam.es 
 * @author Eduardo Terres Caballero eduardo.terres@estudiante.uam.es
 *  
 */
package gui;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import entradas.Reserva;
import obras.Evento;
import teatro.Teatro;
import usuarios.UsuarioRegistrado;

/**
* Clase para visualizar mis reservas
*/
public class VistaMisReservas extends JPanel{

	private JPanel mostraCompra;
	
	/**
	* Constructor
	* @param u usuario registrado del que se visualizan las reservas
	*/
	public VistaMisReservas(UsuarioRegistrado u) {
		
		GridBagConstraints constraints = new GridBagConstraints();
		//this.setLayout(new GridBagLayout());
		this.setLayout(new BorderLayout());
		int i;
		String[] res = new String[u.getSizeReservas()];
		JLabel etiqueta = new JLabel("Seleccione una reserva");
		Font t = new Font("", Font.CENTER_BASELINE, 15);
		etiqueta.setFont(t);
		ArrayList<Reserva> reservas = u.getReservas();
		
		//if()
		
		for(i=0; i<u.getSizeReservas(); i++) {
			Reserva r = reservas.get(i);
			Evento e = r.getRepresentacion().getEvento();
			res[i]="Evento: "+e.getTitulo()+". Fecha: "+r.getRepresentacion().getFecha()+". Hora: "+r.getRepresentacion().getHora();
		}
		
		
		JComboBox combo = new JComboBox(res);
		
		JPanel pCentro = new JPanel();
		pCentro.setLayout(new GridBagLayout());
		this.add(pCentro, BorderLayout.CENTER);
		
		constraints.gridx = 0;
		constraints.gridy = 1;
		constraints.gridwidth = 1; 
		constraints.gridheight = 1;
		pCentro.add(combo, constraints);
		
		constraints.gridx = 0;
		constraints.gridy = 0;
		pCentro.add(etiqueta, constraints);
		
		JPanel panelDerecha = new JPanel();
		
		JButtonRojo confirmar = new JButtonRojo("Confirmar Reserva");
		JButtonRojo cancelar = new JButtonRojo("Cancelar Reserva");
		confirmar.setVisible(false);
		cancelar.setVisible(false);
		
		JButtonRojo bVolver = new JButtonRojo("Volver");
		
		
		JLabel lab = new JLabel("       NO TIENE NINGUNA RESERVA         ");
		Font font = new Font("", Font.CENTER_BASELINE, 30);
		lab.setFont(font);
		lab.setVisible(false);
		
		combo.addActionListener(
		  new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		    	int indxSelec = combo.getSelectedIndex();	
		    	if(u.getSizeReservas()==0) {
		    		lab.setVisible(true);
		    		getPanel().removeAll();
		    		getPanel().add(lab, BorderLayout.CENTER);
		    	}
		    	else {
		    		String sp = "Número de entradas reservadas:  "+reservas.get(indxSelec).getSizeEntradas();
		    		JLabel msgPrecio = new JLabel(sp);
					Font font = new Font("", Font.BOLD, 16);
					msgPrecio.setFont(font);
					panelDerecha.removeAll();
					panelDerecha.add(msgPrecio, BorderLayout.CENTER);
					panelDerecha.updateUI();
					confirmar.setVisible(true);
					cancelar.setVisible(true);
					bVolver.setVisible(false);
		    	}
		    }
		  }
		);
		
		
		confirmar.addActionListener(
		  new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		    	int indxSelec = combo.getSelectedIndex();
		    	getPanel().removeAll();
		    	getPanel().add(bVolver, BorderLayout.EAST);
		    	mostraCompra = new VistaConfirmaReserva(reservas.get(indxSelec));
				getPanel().add(mostraCompra, BorderLayout.CENTER);
				pCentro.setVisible(false);
				bVolver.setVisible(true);

		    }
		  }
		);
		
		
		cancelar.addActionListener(
		  new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		    	int indxSelec = combo.getSelectedIndex();
		    	Teatro.getUsuarioLogueado().cancelarReserva(reservas.get(indxSelec));
		    	JOptionPane.showMessageDialog(null, "Reserva cancelada con éxito");
		    	combo.removeItem(res[indxSelec]);
		    	if(u.getSizeReservas()==0) {
		    		lab.setVisible(true);
		    		getPanel().removeAll();
		    		getPanel().add(lab, BorderLayout.CENTER);
		    	}
		    	else {
		    		for(int i=0; i<u.getSizeReservas(); i++) {
		    			Reserva r = reservas.get(i);
		    			Evento ev = r.getRepresentacion().getEvento();
		    			res[i]="Evento: "+ev.getTitulo()+". Fecha: "+r.getRepresentacion().getFecha()+". Hora: "+r.getRepresentacion().getHora();
		    		}
		    	}
		    }
		  }
		);
		
		bVolver.addActionListener(
		  new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		    	getPanel().add(pCentro, BorderLayout.CENTER);
		    	mostraCompra.setVisible(false);
		    	pCentro.setVisible(true);
				bVolver.setVisible(false);
		    }
		  }
		);
		
		
		constraints.insets=new Insets(0,100,10,0); //separación
		constraints.gridx = 2;
		constraints.gridy = 0;
		constraints.gridwidth = 1; 
		constraints.gridheight = 1;
		pCentro.add(panelDerecha, constraints);
		
		constraints.gridx = 2;
		constraints.gridy = 1;
		pCentro.add(confirmar, constraints);
		
		constraints.insets=new Insets(10,100,0,0); 
		constraints.gridx = 2;
		constraints.gridy = 4;
		pCentro.add(cancelar, constraints);
		
		
	}
	
	/**
	* Método para obtener el panel

	* @return panel de esta clase
	*/
	private JPanel getPanel() {
		return this;
	}

}
