package gui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import usuarios.Notificacion;

@SuppressWarnings("serial")
public class VistaMiCuenta extends JPanel{

	private JPanel p1;
	
    public VistaMiCuenta() {

    	JPanel panelBotones = new JPanel();
    	//FlowLayout fl1 = new FlowLayout();
		panelBotones.setLayout(new BorderLayout());
		//fl1.setAlignment(FlowLayout.LEFT);
		//fl1.setHgap(80);
		//new FlowLayout(FlowLayout.RIGHT)
		
		
		String[] notifs =  {"Notif1 .......... blabla .....", "Notif2.......... blabla .....","ana","eduardo","esther","josé",
	              "juan","luis","maría","miguel","zoe"};
		JList lista = new JList(notifs);
		//this.setLayout(new BorderLayout());
		panelBotones.setVisible(true);
		
		
        JLabel etiqueta = new JLabel("NOTIFICACIONES");
		this.add(etiqueta);
		
		JScrollPane scroll = new JScrollPane(lista);
        
		// Añadir el scroll con la lista al panel donde se vaya a mostrar
		JPanel ejemploList = new JPanel();
		ejemploList.add(scroll);

		panelBotones.add(ejemploList, BorderLayout.WEST);
		panelBotones.add(etiqueta, BorderLayout.NORTH);
		ejemploList.setVisible(true);
		this.add(panelBotones, BorderLayout.EAST);
		
		this.setVisible(true);
        
        //String[] notificaciones =  {"Notificación de prueba", "Notificación de prueba2"};
        
    }
}