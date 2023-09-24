/** 
 * 
 * @author Olmar Arranz olmar.arranz@estudiante.uam.es 
 * @author Cristina Madrigal cristina.madrigalb@estudiante.uam.es 
 * @author Eduardo Terres Caballero eduardo.terres@estudiante.uam.es
 *  
 */
package gui;

import java.awt.BorderLayout;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import usuarios.Notificacion;

/**
 * Clase para visualizar las notificaciones
 */
@SuppressWarnings("serial")
public class VistaNotificaciones extends JPanel{
    private ArrayList<Notificacion> notificaciones;
	private JPanel p1;
	private Notificacion n;
	private String s;
	private Notificacion n2;
	private String s2;

    /**
	 * Constructor 
	 */
    public VistaNotificaciones() {
        final JFrame ventana = new JFrame("Mi GUI");

        JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
        //panelBotones.setLayout(new FlowLayout());

        JPanel panelNoti = new JPanel();
        JLabel etiqueta = new JLabel("Selecciona uno de la lista");
        s = "Notificación de prueba";
        n = new Notificacion(s);
        s2 = "Notificación de prueba2";
        n2 = new Notificacion(s2);
        String[] notificaciones =  {s, s2};
        //ArrayList<Notificacion> notificaciones = new ArrayList<>();
        //notificaciones.add(n);
        //notificaciones.add(n2);

        JButton bNoti = new JButton("Notificaciones");
        this.add(bNoti);

        JList lista = new JList(notificaciones);

        // asociar acciones a componentes
        lista.addListSelectionListener(
            new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent ev) {
                JList localJList = (JList) ev.getSource();
                System.out.println( lista.getSelectedValue() );
                if (! ev.getValueIsAdjusting() ) {
                    String valorSeleccionado = 
                                    (String) lista.getSelectedValue();
                    JOptionPane.showMessageDialog(ventana, "Seleccionaste: " 
                                                        + valorSeleccionado);
                }
            }
            }
        );

        // Por defecto se pueden seleccionar varias filas en la lista. 
		// Para que sólo se pueda seleccionar una, usar setSelectionMode
		lista.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); 
		        
		// Es aconsejable crear una barra de scroll para la lista,
		// por si el número de elementos supera el tamaño previsto 
		JScrollPane scroll = new JScrollPane(lista);
		        
		// Añadir el scroll con la lista al panel donde se vaya a mostrar
		JPanel ejemploList = new JPanel();
		ejemploList.add(scroll);


		// añadir componentes al contenedor
		panel.add(etiqueta);
		panel.add(ejemploList);
		ejemploList.setVisible(true);

		// mostrar ventana
		ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		ventana.setSize(200,340);
		ventana.setVisible(true);
    }
}