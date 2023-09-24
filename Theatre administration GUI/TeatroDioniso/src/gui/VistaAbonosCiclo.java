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
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.*;
import javax.swing.border.LineBorder;

import entradas.Abono;
import entradas.AbonoAnual;
import entradas.AbonoCiclo;
import teatro.Teatro;
import obras.Ciclo;
import obras.Evento;
import obras.ObraTeatro;

@SuppressWarnings("serial")
/**
 * 
 * Clase que representa el panel para los abonos de ciclo
 *
 */
public class VistaAbonosCiclo extends JPanel {
	private final int nEleMostrados = 1;
	private int offset;
	private JPanel p1;
	private ArrayList<Abono> abonos = new ArrayList<>();
	private Abono actual;
	private JPanel mostraCompra;
	private JPanel mostraZona;
	private JPanel jpSigPrev;
	private JButton bSiguiente;
	private JButton bPrevio;
	private JButton bVolver;
	
	/**
	 * Constructor de VistaAbonosCiclo
	 */
	public VistaAbonosCiclo() {
		offset = 0;
		
		for(Abono a: Teatro.getAbonos()) {
			if(a instanceof AbonoCiclo) {
				abonos.add(a);
			}
		}
		

		this.setLayout(new BorderLayout());
		
		String descrip1 = "Con un abono ciclo usted puede acceder a cualquier representación de aquellos eventos que pertenezcan al ciclo.";
		String descrip2 ="El abono te permitirá tener un descuento en esos eventos. Podrás seleccionar la zona en la que deseas comprarlo.";
		String descrip3 ="El precio y el descuento variarán según la zona elegida y el ciclo.";
		
		Font font = new Font("", Font.CENTER_BASELINE, 30);
		Font t = new Font("", Font.CENTER_BASELINE, 15);
		
		JPanel pCL = new JPanel();
		pCL.setLayout(new CardLayout(0,0));
		
		
		JPanel pNorte = new JPanel();
		pNorte.setLayout(new BorderLayout());
		
		JPanel pArriba = new JPanel();
		pArriba.setLayout(new BorderLayout());
		
		JLabel label = new JLabel("CARACTERÍSTICAS");
		label.setFont(font);
		pArriba.add(label);
		pNorte.add(pArriba, BorderLayout.NORTH);
		
		JPanel pAbajo = new JPanel();
		pAbajo.setLayout(new GridLayout(3, 0, 0, 0));

		
		//Para que aparezca debajo de CARACTERÍSTICAS y en 3 líneas
		JLabel texto1 = new JLabel(descrip1);
		texto1.setText(descrip1);
		pAbajo.add(texto1);
		
		JLabel texto2 = new JLabel(descrip2);
		texto2.setText(descrip2);
		pAbajo.add(texto2);
		
		JLabel texto3 = new JLabel(descrip3);
		texto3.setText(descrip3);
		pAbajo.add(texto3);
		
		pNorte.add(pAbajo, BorderLayout.SOUTH);
		
		JPanel pNorte2 = new JPanel();
		JLabel tit = new JLabel("Mostrando precios del abono según la zona");
		tit.setFont(t);
		pNorte2.add(tit, BorderLayout.SOUTH);
		
		pCL.add(pNorte);
		pCL.add(pNorte2);
		
		
		this.add(pCL, BorderLayout.NORTH);
		
		/* Panel donde van a a colocarse los botones siguiente, previo y volver cuando corresponda */
		jpSigPrev = new JPanel();
		jpSigPrev.setLayout(new BorderLayout());
		
		bSiguiente = new JButtonRojo("Siguiente");
		bPrevio = new JButtonRojo("Previo");
		
		jpSigPrev.add(bSiguiente, BorderLayout.SOUTH);
		jpSigPrev.add(bPrevio, BorderLayout.NORTH);
		this.add(jpSigPrev, BorderLayout.EAST);
		
		/* Botón para volver a todos los eventos cuando se ha entrado en uno concreto*/
		bVolver = new JButtonRojo("Volver");
		jpSigPrev.add(bVolver, BorderLayout.CENTER);
		this.add(jpSigPrev, BorderLayout.EAST);
		jpSigPrev.setVisible(false);
		
		
		JPanel panelCompra = new JPanel();
		panelCompra.setLayout(new BorderLayout());
		JButtonRojo bCompra = new JButtonRojo("Comprar");
		bCompra.setPreferredSize(new Dimension(100,50));
		bCompra.setVisible(false);
		JButtonRojo bSiguienteSel = new JButtonRojo("Siguiente Selección");
		bSiguienteSel.setPreferredSize(new Dimension(200,50));
		
		JPanel jpCentro = new JPanel();
		jpCentro.setLayout(new CardLayout(0,0));
		
		// Panel con abonos mostrados
		p1 = new JPanel();
		p1.setLayout(new GridLayout(2, 0, 10, 10));
		mostraZona = new VistaCiclos();
		jpCentro.add(mostraZona);
		jpCentro.add(p1);
		this.add(jpCentro, BorderLayout.CENTER);

		bSiguiente.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (offset + nEleMostrados < abonos.size()) {
					offset = (offset + nEleMostrados) % abonos.size();
				}
				actualizarEventosMostrados();
			}
		});

		bPrevio.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (offset < nEleMostrados) {
					// Primera pagina
					offset = 0;
				} else {
					offset = Math.abs(offset - nEleMostrados) % abonos.size();
				}
				actualizarEventosMostrados();
			}
		});
		
		bVolver.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				jpCentro.setVisible(true);
				mostraZona.setVisible(true);
				pCL.setVisible(true);
				pNorte.setVisible(true);
				bSiguienteSel.setVisible(true);
				bPrevio.setVisible(false);
				bSiguiente.setVisible(false);
			    bVolver.setVisible(false);
			    bCompra.setVisible(false);
			}
		});
		
		bSiguienteSel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				p1.setVisible(true);
				pNorte.setVisible(false);
				pNorte2.setVisible(true);
				bSiguiente.setVisible(true);
			    bPrevio.setVisible(true);
			    bVolver.setVisible(true);
				mostraZona.setVisible(false);
				bCompra.setVisible(true);
				bSiguienteSel.setVisible(false);
				jpSigPrev.setVisible(true);
				CardLayout cl = (CardLayout)(jpCentro.getLayout());
			    cl.show(jpCentro, "panel1");
			    jpCentro.setVisible(true);
			    p1.setVisible(true);
		    	mostraZona.setVisible(false);
			}
		});
		
		bCompra.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				if(Teatro.getUsuarioLogueado()==null){
				     JOptionPane.showMessageDialog(null, "Para comprar abonos debe estar registrado y haber iniciado sesion");
				}
				else {
					jpCentro.setVisible(false);
					pCL.setVisible(false);
					bSiguiente.setVisible(false);
				    bPrevio.setVisible(false);
				    bVolver.setVisible(true);
					mostraCompra = new VistaCompraAbono(actual);
					getPanel().add(mostraCompra, BorderLayout.CENTER);
					mostraCompra.setVisible(true);
					bCompra.setVisible(false);
				}
			}
		});

		this.add(jpSigPrev, BorderLayout.EAST);
		
		panelCompra.add(bCompra, BorderLayout.EAST);
		panelCompra.add(bSiguienteSel, BorderLayout.WEST);
		panelCompra.setBorder(new LineBorder(panelCompra.getBackground(), 40));
		this.add(panelCompra, BorderLayout.SOUTH);
		
		this.actualizarEventosMostrados();
	}
	
	/**
	* Método para obtener el panel

	* @return panel de esta clase
	*/
	private JPanel getPanel() {
		return this;
	}

	private void actualizarEventosMostrados() {
		int i;
		p1.removeAll();
		for (i = 0; i < nEleMostrados && i < abonos.size() && (i + offset) < abonos.size(); i++) {
			actual = abonos.get((offset + i) % abonos.size());
			p1.add(new VistaAbono(actual));
		}
		p1.updateUI();
	}
	
	/**
	* 
	* Clase que representa el panel para seleccionar el ciclo
	*
	*/
	public static class VistaCiclos extends JPanel {
		
		public VistaCiclos() {
			GridBagConstraints constraints = new GridBagConstraints();
			this.setLayout(new GridBagLayout());
			int i;
			String[] cicl = new String[Teatro.getCiclo().size()];
			JLabel etiqueta = new JLabel("Seleccione un ciclo");
			Font t = new Font("", Font.CENTER_BASELINE, 15);
			etiqueta.setFont(t);
			ArrayList<Ciclo> ciclos = new ArrayList<>();
			
			for(i=0; i<Teatro.getCiclo().size(); i++) {
				Ciclo c = Teatro.getCiclo().get(i);
				cicl[i]=c.getNombre();
				ciclos.add(c);
			}
			
			JComboBox combo = new JComboBox(cicl);
			
			constraints.gridx = 0;
			constraints.gridy = 1;
			constraints.gridwidth = 1; 
			constraints.gridheight = 1;
			this.add(combo, constraints);
			
			constraints.gridx = 0;
			constraints.gridy = 0;
			this.add(etiqueta, constraints);
			
			JPanel panelDerecha = new JPanel();
			
			combo.addActionListener(
			  new ActionListener() {
			    public void actionPerformed(ActionEvent e) {
			    	int indxSelec = combo.getSelectedIndex();
			    	ArrayList<String> s = new ArrayList<>();
			    	for(Evento ev: ciclos.get(indxSelec).getEventos()) {
						s.add(ev.getTitulo());
					}
			    	
			    	String sp = "Eventos:  "+s;
			    	
			    	JLabel msgPrecio = new JLabel(sp);
					Font font = new Font("", Font.BOLD, 16);
					msgPrecio.setFont(font);
					panelDerecha.removeAll();
					panelDerecha.add(msgPrecio, BorderLayout.CENTER);
					panelDerecha.updateUI();
			    }
			  }
			);
			
			
			constraints.insets=new Insets(0,100,0,0); //separación
			constraints.gridx = 2;
			constraints.gridy = 0;
			constraints.gridwidth = 1; 
			constraints.gridheight = 1;
			this.add(panelDerecha, constraints);
			
		}
	}
}
