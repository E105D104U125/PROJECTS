/** 
 * 
 * @author Olmar Arranz olmar.arranz@estudiante.uam.es 
 * @author Cristina Madrigal cristina.madrigalb@estudiante.uam.es 
 * @author Eduardo Terres Caballero eduardo.terres@estudiante.uam.es
 *  
 */
package gui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

import teatro.Teatro;

/**
 * 
 * Clase que muestra el panel MiCuenta en usuario registrado
 *
 */
public class VistaMiCuentaUsReg extends JPanel{
	
	/**
	 * Constructor VistaMiCuentaUsReg
	 */
	public VistaMiCuentaUsReg() {
		
		this.setLayout(new BorderLayout());
		
		JPanel p1 = new VistaMisEntradas(Teatro.getUsuarioLogueado());
		JPanel p2 = new VistaMisReservas(Teatro.getUsuarioLogueado());
		JPanel p3 = new VistaMisNotificaciones(Teatro.getUsuarioLogueado());
		JPanel p4 = new VistaMisDatos(Teatro.getUsuarioLogueado());
		JPanel p5 = new VistaMisAbonos(Teatro.getUsuarioLogueado());

		
		JTabbedPane pestanias = new JTabbedPane(JTabbedPane.TOP);
		pestanias.addTab("Mis entradas", p1);
		pestanias.addTab("Mis reservas", p2);
		pestanias.addTab("Mis notificaciones", p3);
		pestanias.addTab("Mis datos", p4);
		pestanias.addTab("Mis abonos", p5);
		
		Font font = new Font("", Font.CENTER_BASELINE, 15);
		pestanias.setFont(font);
		
		this.add(pestanias, BorderLayout.CENTER);
	}
}

