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
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.border.LineBorder;

import entradas.Abono;
import entradas.AbonoAnual;
import teatro.Teatro;

/**
 * 
 * Clase que representa el panel para los abonos anuales
 *
 */
public class VistaAbonosAnual extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6504208694916394630L;
	private final int nEleMostrados = 1;
	private int offset;
	private JPanel p1;
	private ArrayList<Abono> abonos = new ArrayList<>();
	private Abono actual;
	private JPanel mostraFecha;
	private JPanel jpSigPrev;
	private JButton bSiguiente;
	private JButton bPrevio;
	private JButton bVolver;
	private JPanel jpCentro;
	private static JPanel pCL;

	/**
	 * Constructor de VistaAbonosAnual
	 */
	public VistaAbonosAnual() {
		offset = 0;
		for (Abono a : Teatro.getAbonos()) {
			if (a instanceof AbonoAnual) {
				abonos.add(a);
			}
		}

		this.setLayout(new BorderLayout());
		///////////////
		String descrip1 = "Con un abono anual usted puede acceder a cualquier evento de cualquier representación.";
		String descrip2 = "Al comprar podrás seleccionar la zona en la que deseas comprarlo y la fecha de inicio.";
		String descrip3 = "El precio variará según la zona elegida y la duración es de 1 año.";

		Font font = new Font("", Font.CENTER_BASELINE, 30);
		Font t = new Font("", Font.CENTER_BASELINE, 15);

		pCL = new JPanel();
		pCL.setLayout(new CardLayout(0, 0));

		JPanel pNorte = new JPanel();
		pNorte.setLayout(new BorderLayout());

		JPanel pArriba = new JPanel();
		pArriba.setLayout(new BorderLayout());

		JLabel label = new JLabel("CARACTERÍSTICAS");
		label.setFont(font);
		pArriba.add(label);
		pNorte.add(pArriba, BorderLayout.NORTH);

		JPanel pMasAbajo = new JPanel();
		pMasAbajo.setLayout(new BorderLayout());

		JLabel titul = new JLabel("Mostrando precios del abono según la zona");
		titul.setFont(t);
		pMasAbajo.add(titul, BorderLayout.SOUTH);
		pNorte.add(pMasAbajo, BorderLayout.SOUTH);

		JPanel pAbajo = new JPanel();
		pAbajo.setLayout(new GridLayout(5, 0, 0, 0));

		// Para que aparezca debajo de CARACTERÍSTICAS y en 3 líneas
		JLabel texto1 = new JLabel(descrip1);
		texto1.setText(descrip1);
		pAbajo.add(texto1);

		JLabel texto2 = new JLabel(descrip2);
		texto2.setText(descrip2);
		pAbajo.add(texto2);

		JLabel texto3 = new JLabel(descrip3);
		texto3.setText(descrip3);
		pAbajo.add(texto3);

		pNorte.add(pAbajo, BorderLayout.CENTER);

		JPanel pNorte2 = new JPanel();
		JLabel tit = new JLabel("Fecha para dar de alta el abono");
		tit.setFont(t);
		pNorte2.add(tit, BorderLayout.SOUTH);

		pCL.add(pNorte);
		pCL.add(pNorte2);

		this.add(pCL, BorderLayout.NORTH);

		jpCentro = new JPanel();
		jpCentro.setLayout(new CardLayout(0, 0));

		/*
		 * Panel donde van a a colocarse los botones siguiente, previo y volver cuando
		 * corresponda
		 */
		jpSigPrev = new JPanel();
		jpSigPrev.setLayout(new BorderLayout());

		bSiguiente = new JButtonRojo("Siguiente");
		bPrevio = new JButtonRojo("Previo");

		jpSigPrev.add(bSiguiente, BorderLayout.SOUTH);
		jpSigPrev.add(bPrevio, BorderLayout.NORTH);
		this.add(jpSigPrev, BorderLayout.EAST);

		/* Botón para volver */
		bVolver = new JButtonRojo("Volver");
		jpSigPrev.add(bVolver, BorderLayout.CENTER);
		this.add(jpSigPrev, BorderLayout.EAST);
		bVolver.setVisible(false);

		JPanel panelCompra = new JPanel();
		panelCompra.setLayout(new BorderLayout());
		JButtonRojo bSigSel = new JButtonRojo("Siguiente Selección");
		bSigSel.setPreferredSize(new Dimension(200, 50));

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
				CardLayout cl = (CardLayout) (pCL.getLayout());
				cl.show(pCL, "panel1");
				pCL.setVisible(true);
				p1.setVisible(true);
				pNorte.setVisible(true);
				bSiguiente.setVisible(true);
				bPrevio.setVisible(true);
				bVolver.setVisible(false);
				bSigSel.setVisible(true);
				mostraFecha.setVisible(false);
			}
		});

		bSigSel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				CardLayout cl = (CardLayout) (jpCentro.getLayout());
				if (actual == null) {
					JOptionPane.showMessageDialog(null, "No hay abonos disponibles");
					return;
				}
				cl.show(jpCentro, "panel1");
				jpCentro.setVisible(true);
				p1.setVisible(false);
				mostraFecha = new VistaAnuales((AbonoAnual) actual);
				jpCentro.add(mostraFecha);
				mostraFecha.setVisible(true);
				pNorte.setVisible(false);
				pNorte2.setVisible(true);
				bSiguiente.setVisible(false);
				bPrevio.setVisible(false);
				bVolver.setVisible(true);
				bSigSel.setVisible(false);
			}
		});

		p1 = new JPanel();
		p1.setLayout(new GridLayout(2, 0, 10, 10));
		jpCentro.add(p1);
		// jpCentro.add(mostraFecha);
		this.add(jpCentro, BorderLayout.CENTER);

		this.add(jpSigPrev, BorderLayout.EAST);

		panelCompra.add(bSigSel, BorderLayout.EAST);
		panelCompra.setBorder(new LineBorder(panelCompra.getBackground(), 40));
		this.add(panelCompra, BorderLayout.SOUTH);

		this.actualizarEventosMostrados();
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
	 * Clase que representa el panel para introducir la fecha en anual
	 *
	 */
	public static class VistaAnuales extends JPanel {
		private JPanel mostraCompra;

		/**
		 * Constructor de VistaAnuales
		 * 
		 * @param a Abono a modificar
		 */
		public VistaAnuales(AbonoAnual a) {
			this.setLayout(new BorderLayout());
			JPanel ab = new JSeleccion(a);
			this.add(ab, BorderLayout.WEST);
		}

		/**
		 * 
		 * Clase Desplegable que muestra las representaciones
		 *
		 */
		public class JSeleccion extends JPanel {

			/**
			 * Constructor de VistaAnuales
			 * 
			 * @param a Abono en el que introducir la fecha
			 */
			public JSeleccion(AbonoAnual a) {
				GridBagConstraints constraints = new GridBagConstraints();
				this.setLayout(new GridBagLayout());

				// Texto indicacion busqueda
				JLabel labelIndicacion = new JLabel("Introduzca la fecha (formato yyyy-mm-dd)", SwingConstants.CENTER);
				labelIndicacion.setFont(new Font("", Font.ITALIC, 16));

				// Barra de busqueda
				JPanel barraBusqueda = new JPanel();
				barraBusqueda.setLayout(new FlowLayout());

				JPanel panelInfo = new JPanel();
				JTextField texto = new JTextField();
				texto.setPreferredSize(new Dimension(300, 30));
				JButtonRojo aceptar = new JButtonRojo("Aceptar");

				JButtonRojo pagar = new JButtonRojo("Comprar");
				pagar.setVisible(false);

				// Asociar acciones a botón aceptar
				aceptar.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						LocalDate actual = LocalDate.now();
						String fechaS = texto.getText();
						LocalDate fechaD = null;
						boolean notValid = false;
						try {
							fechaD = LocalDate.parse(fechaS);
						} catch (DateTimeParseException excep) {
							notValid = true;
						}

						if (!notValid) {
							// Días entre la fecha introducida y la actual
							long dias = ChronoUnit.DAYS.between(actual, fechaD);
							if (dias < 0) {
								JLabel msgError = new JLabel("La fecha debe ser mayor que la actual");
								Font font = new Font("", Font.ITALIC, 16);
								msgError.setFont(font);
								msgError.setForeground(Color.RED);
								panelInfo.removeAll();
								panelInfo.add(msgError, BorderLayout.CENTER);
							} else {
								String s = "La fecha de fin será " + fechaD.plusDays(365);
								// se establece la fecha elegida al abono
								if (fechaD != null)
									a.setFechaIni(fechaD);
								JLabel msgError = new JLabel(s);
								Font font = new Font("", Font.ITALIC, 16);
								msgError.setFont(font);
								msgError.setForeground(Color.BLUE);
								panelInfo.removeAll();
								panelInfo.add(msgError, BorderLayout.CENTER);
								pagar.setVisible(true);
							}
						}
					}
				});

				barraBusqueda.add(texto);
				barraBusqueda.add(aceptar);

				pagar.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						if (Teatro.getUsuarioLogueado() == null) {
							JOptionPane.showMessageDialog(null,
									"Para comprar abonos debe estar registrado y haber iniciado sesion");
						} else {
							pCL.setVisible(false);
							getPanel().removeAll();
							mostraCompra = new VistaCompraAbono(a);
							getPanel().add(mostraCompra, BorderLayout.CENTER);
							mostraCompra.setVisible(true);
						}
						pagar.setVisible(false);
						aceptar.setVisible(false);
						barraBusqueda.setVisible(false);
					}
				});

				JPanel vacio = new JPanel();
				JLabel truco = new JLabel("  ");

				truco.setPreferredSize(new Dimension(300, 30));
				vacio.add(truco, BorderLayout.CENTER);

				constraints.gridx = 0;
				constraints.gridy = 0;
				constraints.gridwidth = 2;
				constraints.gridheight = 2;
				this.add(vacio, constraints);

				constraints.gridx = 2;
				constraints.gridy = 0;
				constraints.gridwidth = 1;
				constraints.gridheight = 1;
				this.add(labelIndicacion, constraints);

				constraints.gridx = 2;
				constraints.gridy = 1;
				this.add(barraBusqueda, constraints);

				constraints.gridx = 2;
				constraints.gridy = 2;
				this.add(panelInfo, constraints);

				constraints.gridx = 2;
				constraints.gridy = 3;
				this.add(pagar, constraints);

			}
		}

		/**
		 * Método para obtener el panel
		 * 
		 * @return panel de esta clase
		 */
		private JPanel getPanel() {
			return this;
		}

	}

}
