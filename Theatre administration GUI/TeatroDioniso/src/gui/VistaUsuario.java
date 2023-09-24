/** 
 * 
 * @author Olmar Arranz olmar.arranz@estudiante.uam.es 
 * @author Cristina Madrigal cristina.madrigalb@estudiante.uam.es 
 * @author Eduardo Terres Caballero eduardo.terres@estudiante.uam.es
 *  
 */
 
 package gui;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.Semaphore;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import teatro.Teatro;

/**
 * Clase vista de un usuario
 */
@SuppressWarnings("serial")
public class VistaUsuario extends JFrame{
	static int j=0;
	private JPanel p4;

	/**
	* Constructor
	* @param sem semáforo 
	*/
	public VistaUsuario(Semaphore sem) {
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Dimension dim = super.getToolkit().getScreenSize();
		super.getToolkit().getScreenSize();
		super.setSize(dim);

		this.addWindowListener(new WindowAdapter() {
	        @Override
	        public void windowClosing(WindowEvent e) {
	            if (sem != null) sem.release();
	        }
        });
		this.setBounds(new Rectangle(0,0,dim.width,dim.height));

		Container contenedor = this.getContentPane();
		contenedor.setLayout(new BorderLayout());
		
		JPanel p2 = new VistaEventosUs();
		JPanel p3 = new VistaAbonosUsuario();
		if(Teatro.getUsuarioLogueado()!=null) {
			p4 = new VistaMiCuentaUsReg();
		}
		JTabbedPane pestanias = new JTabbedPane(JTabbedPane.TOP);
		pestanias.addTab("Eventos", p2);
		pestanias.addTab("Abonos", p3);
		if(Teatro.getUsuarioLogueado()!=null) {
			pestanias.addTab("Mi cuenta", p4);		
		}
		contenedor.add(pestanias, BorderLayout.CENTER);
		Font font = new Font("", Font.CENTER_BASELINE, 30);
		pestanias.setFont(font);
		
		ChangeListener changeListener = new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent changeEvent) {
				JTabbedPane sourceTabbedPane = (JTabbedPane) changeEvent.getSource();
				if (Teatro.getUsuarioLogueado()!=null && sourceTabbedPane.getSelectedIndex() == pestanias.indexOfTab("Mi cuenta")) {
					pestanias.setComponentAt(pestanias.indexOfTab("Mi cuenta"), new VistaMiCuentaUsReg());
				}
			}
		};
		pestanias.addChangeListener(changeListener);
		
		
		// Nombre de las tabs
		try {
			pestanias.setTabComponentAt(0,new JLabel(new ImageIcon(ImageIO.read(new File("./resources/VPLabel2.png")))));
			pestanias.setTabComponentAt(1,new JLabel(new ImageIcon(ImageIO.read(new File("./resources/VPLabel3.png")))));
			if(Teatro.getUsuarioLogueado()!=null) {
				pestanias.setTabComponentAt(2,new JLabel(new ImageIcon(ImageIO.read(new File("./resources/VPLabel4.png")))));
			}
		} catch (IOException e1) {
			e1.printStackTrace();
			return;
		}

		try {
			BufferedImage img = ImageIO.read(new File("./resources/cabecera.png"));
			ImageIcon icon = new ImageIcon(img);
			Image imagen = icon.getImage().getScaledInstance(dim.width,dim.width*230/1800, Image.SCALE_DEFAULT);
			ImageIcon icon2 = new ImageIcon(imagen);
			JLabel label = new JLabel(icon2);
			contenedor.add(label, BorderLayout.NORTH);
		} catch (IOException e) {
			System.err.print(e);
			return;
		}
	
		this.setVisible(true);
		
	}
	
}