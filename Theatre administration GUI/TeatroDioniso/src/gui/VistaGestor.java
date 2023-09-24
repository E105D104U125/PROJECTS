/** 
 * 
 * @author Olmar Arranz olmar.arranz@estudiante.uam.es 
 * @author Cristina Madrigal cristina.madrigalb@estudiante.uam.es 
 * @author Eduardo TerrÃ©s Caballero eduardo.terres@estudiante.uam.es
 *  
 */

package gui;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.Semaphore;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

@SuppressWarnings("serial")
/**
 * Clase Vista Gestor
 *
 */
public class VistaGestor extends JFrame {
	JPanel jpDchaGE, jpDchaCT;

	/**
	 * Constructor
	 * 
	 * @param sem semaforo
	 */
	public VistaGestor(Semaphore sem) {
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		Dimension dim = super.getToolkit().getScreenSize();
		super.getToolkit().getScreenSize();
		super.setSize(dim);
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				if (sem != null)
					sem.release();
			}
		});

		this.setBounds(new Rectangle(0, 0, dim.width, dim.height));

		Container contenedor = this.getContentPane();
		contenedor.setLayout(new BorderLayout());

		// Gestionar Eventos
		JPanel p1 = new JPanel();
		p1.setLayout(new BorderLayout());

		// Estadisticas
		JPanel p2 = new VistaEstadisticas();

		// Configurar Teatro
		JPanel p3 = new JPanel();
		p3.setLayout(new BorderLayout());

		JTabbedPane pestanias = new JTabbedPane(JTabbedPane.TOP);
		pestanias.addTab("Gestionar Eventos", p1);
		pestanias.addTab("Estadisticas", p2);
		pestanias.addTab("Configurar Teatro", p3);
		contenedor.add(pestanias, BorderLayout.CENTER);
		Font font = new Font("", Font.CENTER_BASELINE, 30);
		pestanias.setFont(font);

		try {
			BufferedImage img = ImageIO.read(new File("./resources/cabeceraGestor.png"));
			ImageIcon icon = new ImageIcon(img);
			Image imagen = icon.getImage().getScaledInstance(dim.width, dim.width * 230 / 1800, Image.SCALE_DEFAULT);
			ImageIcon icon2 = new ImageIcon(imagen);
			JLabel label = new JLabel(icon2);
			contenedor.add(label, BorderLayout.NORTH);
			/*
			 * BufferedImage img2 = ImageIO.read(new File("./resources/icon.png"));
			 * ImageIcon icon2 = new ImageIcon(img2); this.setIconImage(icon2.getImage());
			 */

		} catch (IOException e) {
			System.err.print(e);
			return;
		}

		ChangeListener changeListener = new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent changeEvent) {
				JTabbedPane sourceTabbedPane = (JTabbedPane) changeEvent.getSource();
				if (sourceTabbedPane.getSelectedIndex() == pestanias.indexOfTab("Estadisticas")) {
					pestanias.setComponentAt(pestanias.indexOfTab("Estadisticas"), new VistaEstadisticas());
				}
			}
		};
		pestanias.addChangeListener(changeListener);

		/*
		 * Gestionar Eventos
		 */

		JPanel jpBotones = new JPanel();
		jpBotones.setLayout(new GridLayout(0, 3, 20, 20));
		jpBotones.setBorder(new LineBorder(jpBotones.getBackground(), 20));

		// Cambiar por los JPanel especificos
		ArrayList<VistaGestor.JButtonGestion> botones = new ArrayList<>();

		botones.add(new VistaGestor.JButtonGestion("Posponer representacion", new JPanel()));
		botones.add(new VistaGestor.JButtonGestion("Cancelar representacion", new JPanel()));
		botones.add(new VistaGestor.JButtonGestion("Crear evento", new VistaGestionarEventos.VistaCrearEvento()));
		botones.add(new VistaGestor.JButtonGestion("Crear representación",
				new VistaGestionarEventos.VistaCrearRepresentacion()));
		botones.add(new VistaGestor.JButtonGestion("Crear ciclo", new VistaGestionarEventos.VistaCrearCiclo()));
		botones.add(new VistaGestor.JButtonGestion("Crear abono", new VistaGestionarEventos.VistaCrearAbono()));

		jpDchaGE = new JPanel();
		jpDchaGE.setLayout(new CardLayout(0, 0));
		jpDchaGE.setBorder(BorderFactory.createMatteBorder(10, 180, 10, 180, jpDchaGE.getBackground()));

		for (VistaGestor.JButtonGestion bt : botones) {
			bt.setBackground(Color.WHITE);
			bt.setPreferredSize(new Dimension(230, 210));
			jpBotones.add(bt);
			jpDchaGE.add(bt.getPanel(), bt.getNombre());
		}

		// Empty Panels
		for (int i = 0; i < 3; i++) {
			JPanel bEmpty = new JPanel();
			bEmpty.setBackground(Color.WHITE);
			bEmpty.setPreferredSize(new Dimension(230, 210));
			jpBotones.add(bEmpty);
		}

		// Se anyaden al contenedor principal
		p1.add(jpBotones, BorderLayout.WEST);
		p1.add(jpDchaGE, BorderLayout.CENTER);

		for (VistaGestor.JButtonGestion bt : botones) {
			// Accedemos al panel asociado al boton
			bt.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					CardLayout cl = (CardLayout) (jpDchaGE.getLayout());
					VistaGestor.this.reloadButtonPanelsGE();
					cl.show(jpDchaGE, bt.getNombre());
				}
			});
		}

		/*
		 * Configurar Teatro
		 */

		JPanel jpBotonesCT = new JPanel();
		jpBotonesCT.setLayout(new GridLayout(0, 3, 20, 20));
		jpBotonesCT.setBorder(new LineBorder(jpBotonesCT.getBackground(), 20));

		// Cambiar por los JPanel especificos
		ArrayList<VistaGestor.JButtonGestion> botonesCT = new ArrayList<>();

		botonesCT.add(new VistaGestor.JButtonGestion("Deshabilitar zona/Reducir aforo", new JPanel()));
		botonesCT.add(new VistaGestor.JButtonGestion("Deshabilitar butaca", new JPanel()));
		botonesCT.add(new VistaGestor.JButtonGestion("Habilitar butaca", new JPanel()));
		botonesCT.add(new VistaGestor.JButtonGestion("Habilitar zona", new JPanel()));
		botonesCT.add(new VistaGestor.JButtonGestion("Crear una zona", new JPanel()));
		botonesCT.add(new VistaGestor.JButtonGestion("Crear una zona compuesta", new JPanel()));

		jpDchaCT = new JPanel();
		jpDchaCT.setLayout(new CardLayout(0, 0));
		jpDchaCT.setBorder(BorderFactory.createMatteBorder(10, 180, 10, 180, jpDchaCT.getBackground()));

		for (VistaGestor.JButtonGestion bt : botonesCT) {
			bt.setBackground(Color.WHITE);
			bt.setPreferredSize(new Dimension(230, 210));
			jpBotonesCT.add(bt);
			jpDchaCT.add(bt.getPanel(), bt.getNombre());
		}

		// Empty Panels
		for (int i = 0; i < 3; i++) {
			JPanel bEmpty = new JPanel();
			bEmpty.setBackground(Color.WHITE);
			bEmpty.setPreferredSize(new Dimension(230, 210));
			jpBotonesCT.add(bEmpty);
		}

		// Se anyaden al contenedor principal
		p3.add(jpBotonesCT, BorderLayout.WEST);
		p3.add(jpDchaCT, BorderLayout.CENTER);

		for (VistaGestor.JButtonGestion bt : botonesCT) {
			// Accedemos al panel asociado al boton
			bt.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					CardLayout cl = (CardLayout) (jpDchaCT.getLayout());
					VistaGestor.this.reloadButtonPanelsCT();
					cl.show(jpDchaCT, bt.getNombre());
				}
			});
		}

		this.setVisible(true);
	}

	/**
	 * Crea nuevas instancias de los botones de Gestionar Eventos
	 */
	protected void reloadButtonPanelsGE() {
		VistaGestionarEventos.VistaPosponerRepresentacion v5 = new VistaGestionarEventos.VistaPosponerRepresentacion();
		v5.setBorder(BorderFactory.createMatteBorder(-1, -1, 50, -1, v5.getBackground()));
		jpDchaGE.add("Posponer representacion", v5);
		VistaGestionarEventos.VistaCancelarRepresentacion v6 = new VistaGestionarEventos.VistaCancelarRepresentacion();
		v6.setBorder(BorderFactory.createMatteBorder(-1, -1, 50, -1, v6.getBackground()));
		jpDchaGE.add("Cancelar representacion", v6);
		VistaGestionarEventos.VistaCrearEvento v1 = new VistaGestionarEventos.VistaCrearEvento();
		v1.setBorder(BorderFactory.createMatteBorder(-1, -1, 50, -1, v1.getBackground()));
		jpDchaGE.add("Crear evento", v1);
		VistaGestionarEventos.VistaCrearRepresentacion v2 = new VistaGestionarEventos.VistaCrearRepresentacion();
		v2.setBorder(BorderFactory.createMatteBorder(-1, -1, 50, -1, v2.getBackground()));
		jpDchaGE.add("Crear representación", v2);
		VistaGestionarEventos.VistaCrearCiclo v3 = new VistaGestionarEventos.VistaCrearCiclo();
		v3.setBorder(BorderFactory.createMatteBorder(-1, -1, 50, -1, v3.getBackground()));
		jpDchaGE.add("Crear ciclo", v3);
		VistaGestionarEventos.VistaCrearAbono v4 = new VistaGestionarEventos.VistaCrearAbono();
		v4.setBorder(BorderFactory.createMatteBorder(-1, -1, 50, -1, v4.getBackground()));
		jpDchaGE.add("Crear abono", v4);
	}

	/**
	 * Crea nuevas instancias de los botones de Configurar Teatro
	 */
	protected void reloadButtonPanelsCT() {
		VistaConfigurarTeatro.VistaDeshabilitarZona v1 = new VistaConfigurarTeatro.VistaDeshabilitarZona();
		v1.setBorder(BorderFactory.createMatteBorder(-1, -1, 50, -1, v1.getBackground()));
		jpDchaCT.add("Deshabilitar zona/Reducir aforo", v1);
		VistaConfigurarTeatro.VistaDeshabilitarButaca v5 = new VistaConfigurarTeatro.VistaDeshabilitarButaca();
		v5.setBorder(BorderFactory.createMatteBorder(-1, -1, 50, -1, v5.getBackground()));
		jpDchaCT.add("Deshabilitar butaca", v5);
		VistaConfigurarTeatro.VistaHabilitarButaca v6 = new VistaConfigurarTeatro.VistaHabilitarButaca();
		v6.setBorder(BorderFactory.createMatteBorder(-1, -1, 50, -1, v1.getBackground()));
		jpDchaCT.add("Habilitar butaca", v6);
		VistaConfigurarTeatro.VistaHabilitarZona v2 = new VistaConfigurarTeatro.VistaHabilitarZona();
		v2.setBorder(BorderFactory.createMatteBorder(-1, -1, 50, -1, v1.getBackground()));
		jpDchaCT.add("Habilitar zona", v2);
		VistaConfigurarTeatro.VistaCrearZona v3 = new VistaConfigurarTeatro.VistaCrearZona();
		v3.setBorder(BorderFactory.createMatteBorder(-1, -1, 50, -1, v1.getBackground()));
		jpDchaCT.add("Crear una zona", v3);
		VistaConfigurarTeatro.VistaCrearZonaComp v4 = new VistaConfigurarTeatro.VistaCrearZonaComp();
		v4.setBorder(BorderFactory.createMatteBorder(-1, -1, 50, -1, v1.getBackground()));
		jpDchaCT.add("Crear una zona compuesta", v4);
	}

	/**
	 * Clase Boton de gestion
	 */
	public static class JButtonGestion extends JButton {
		// Panel que muestra al pulsar el boton
		private JPanel panel;
		private String nombre;

		/**
		 * Constructor de JPVista
		 * @param str cadena para la etiqueta
		 * @param panel Jpanel contenedor
		 */
		public JButtonGestion(String str, JPanel panel) {
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

		/**
		* Método para añadir el panel

		* @param panel de esta clase
		*/
		public void setPanel(JPanel panel) {
			this.panel = panel;
		}
	}
}
