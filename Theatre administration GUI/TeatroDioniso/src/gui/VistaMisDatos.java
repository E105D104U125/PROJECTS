package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import teatro.Teatro;
import usuarios.UsuarioRegistrado;
import zonas.ZSNoNumerada;

/**
 * Clase VistaMisDatos
 */
public class VistaMisDatos extends JPanel {

	/**
	 * Constructor
	 * 
	 * @param u usuario registrado
	 */
	public VistaMisDatos(UsuarioRegistrado u) {
		
		this.setLayout(new BorderLayout());
		
		/* Panel que va a contener los datos */
		JPanel info = new JPanel();
		GridBagConstraints constraints = new GridBagConstraints();
		info.setLayout(new GridBagLayout());
		
		// Correo del usuario
		
		JLabel lbCorreo = new JLabel("CORREO");
		lbCorreo.setFont(new Font("", Font.ITALIC, 16));
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.gridwidth = 1; 
		constraints.gridheight = 1;
		info.add(lbCorreo, constraints);
		
		constraints.gridx = 0;
		constraints.gridy = 1;
		info.add(new JLabel(u.getCorreo()), constraints);
		
		// Contraseña del usuario 
		JLabel lbPassw = new JLabel("CONTRASEÑA");
		lbPassw.setFont(new Font("", Font.ITALIC, 16));
		constraints.gridx = 0;
		constraints.gridy = 2;
		info.add(lbPassw, constraints);
		
		JLabel contra = new JLabel(u.getPassword());
		constraints.gridx = 0;
		constraints.gridy = 3;
		info.add(contra, constraints);
		
		// Botón cambio de contraseña
		constraints.insets=new Insets(10,0,0,0); //separación
		constraints.gridx = 0;
		constraints.gridy = 4;
		JButtonRojo changePassw = new JButtonRojo("Cambiar contraseña");
		info.add(changePassw, constraints);
		
		
		// Botón darse de baja
		constraints.insets=new Insets(10,0,0,0); //separación
		constraints.gridx = 0;
		constraints.gridy = 6;
		JButtonRojo bBaja = new JButtonRojo("Darse de baja");
		info.add(bBaja, constraints);
		
		
		//Asociar acciones a botón darse de baja
		bBaja.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(Teatro.cancelarUsuario(u)==true) {
					JOptionPane.showMessageDialog(null, "Se ha dado de baja correctamente");
					//cambiar de pestaniaaa!!!!
				}else {
					JOptionPane.showMessageDialog(null, "Lo sentimos, ha habido algun problema. Intentelo de nuevo mas tarde, gracias");
				}
			}
		});
		
		
		JTextField passw = new JTextField();
		passw.setPreferredSize(new Dimension(200,30));
		
		/* Texto que pide la nueva contraseña */
		JLabel labelPassw = new JLabel("Introduzca la nueva contraseña, por favor", SwingConstants.CENTER);
		labelPassw.setFont(new Font("", Font.ITALIC, 16));
		
		/* Barra para introducir datos de la tarjeta */

		//constraints.insets=new Insets(50,0,0,0); //separación
		constraints.gridx = 1;
		constraints.gridy = 1;
		info.add(labelPassw, constraints);
		
		//constraints.insets=new Insets(50,0,0,0); //separación
		constraints.gridx = 1;
		constraints.gridy = 3;
		info.add(passw, constraints);
		
		labelPassw.setVisible(false);
		passw.setVisible(false);
		
		JButtonRojo bConf = new JButtonRojo("Confirmar");
		//constraints.insets=new Insets(50,0,0,0); //separación
		constraints.gridx = 1;
		constraints.gridy = 5;
		info.add(bConf, constraints);
		bConf.setVisible(false);
		
		//Asociar acciones a botón cambiar contraseña
		changePassw.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				labelPassw.setVisible(true);
				passw.setVisible(true);
				bConf.setVisible(true);
			}
		});
		
		//Asociar acciones a botón confirmar cambio contraseña
		bConf.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//actualizar pass
				u.setPassword(passw.getText());
				contra.setText(u.getPassword());
				JOptionPane.showMessageDialog(null, "Cambio de contrasenia realizado");
				labelPassw.setVisible(false);
				passw.setVisible(false);
				bConf.setVisible(false);
			}
		});
		

		
		this.add(info, BorderLayout.CENTER);

	}
}
