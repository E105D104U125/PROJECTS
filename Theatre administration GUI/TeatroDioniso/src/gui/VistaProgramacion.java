package gui;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

import obras.Evento;
import teatro.Teatro;

public class VistaProgramacion extends JPanel{

	public VistaProgramacion() {
		this.setLayout(new BorderLayout());
		
		JPanel pIzquierda = new JPanel();
		pIzquierda.setLayout(new GridBagLayout());
		GridBagConstraints constraints = new GridBagConstraints();
		
		Font font = new Font("", Font.CENTER_BASELINE, 30);
		JLabel label = new JLabel("  EVENTOS  ");
		label.setFont(font);
		
		String[] eves = new String[Teatro.getEventos().size()];
		int i;
		for(i=0; i<Teatro.getEventos().size() ; i++) {
			Evento ev=Teatro.getEventos().get(i);
			eves[i] = ev.getTitulo();
		}
		
		JList lista = new JList(eves);
		
		JScrollPane scroll = new JScrollPane(lista);
        
		// Añadir el scroll con la lista al panel donde se vaya a mostrar
		JPanel ejemploList = new JPanel();
		ejemploList.add(scroll);
		
		constraints.insets=new Insets(0,100,30,0);
		constraints.gridx = 0; 
		constraints.gridy = 0; 
		constraints.gridwidth = 2; 
		constraints.gridheight = 2;
		pIzquierda.add(label, constraints);
		
		//alargar en ancho con widhtx??
		constraints.gridx = 0; 
		constraints.gridy = 2;
		constraints.gridwidth = 2; 
		constraints.gridheight = 4;
		pIzquierda.add(ejemploList, constraints);
		
		this.add(pIzquierda, BorderLayout.WEST);
	}
}
