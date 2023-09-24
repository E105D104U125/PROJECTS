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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import entradas.TipoSeleccion;
import es.uam.eps.padsof.telecard.OrderRejectedException;
import es.uam.eps.padsof.tickets.NonExistentFileException;
import es.uam.eps.padsof.tickets.UnsupportedImageTypeException;
import obras.RepresentacionEvento;
import teatro.Teatro;
import zonas.ZSNoNumerada;
import zonas.ZSNumerada;
import zonas.Zona;

/**
 * 
 * Clase que representa el panel para las entradas numeradas de forma automática
 *
 */
public class VistaEntradaNumAutom extends JPanel {

	private int flag = -10;
	private boolean flag2 = false;
	private int entradas;
	private TipoSeleccion selec;
	private JPanel jpCard;
	private int flagElec = 0;

	/**
	 * Constructor VistaEntradaNumAutom
	 * @param z zona
	 * @param r reserva
	 */
	public VistaEntradaNumAutom(Zona z, RepresentacionEvento r) {

		this.setLayout(new BorderLayout());

		/* Texto que pide el número de entradas */
		JLabel labelIndicacion = new JLabel("Introduzca el numero de entradas que desea comprar/reservar",
				SwingConstants.CENTER);
		labelIndicacion.setFont(new Font("", Font.ITALIC, 16));

		/* Barra para introducir datos */
		JPanel jpBarra = new JPanel();
		jpBarra.setLayout(new GridBagLayout());
		GridBagConstraints constraints = new GridBagConstraints();

		/* Botón comprar */
		JButtonRojo comprar = new JButtonRojo("Comprar");
		comprar.setVisible(false);
		/* Botón reservar */
		JButtonRojo reservar = new JButtonRojo("Reservar");
		reservar.setVisible(false);
		/* Botón lista de espera */
		JButtonRojo listEspera = new JButtonRojo("Apuntarse a la lista de espera");
		listEspera.setVisible(false);

		/* Texto que pide el número de tarjeta */
		JLabel labelPagar = new JLabel("Introduzca el numero de su tarjeta para pagar, por favor",
				SwingConstants.CENTER);
		labelPagar.setFont(new Font("", Font.ITALIC, 16));

		/* Barra para introducir datos de la tarjeta */
		JPanel jpBarraPagar = new JPanel();
		jpBarraPagar.setLayout(new GridBagLayout());

		/* Botón pagar */
		JButtonRojo pagar = new JButtonRojo("Pagar");

		JTextField texto = new JTextField();
		texto.setPreferredSize(new Dimension(300, 30));

		JTextField tarjeta = new JTextField();
		tarjeta.setPreferredSize(new Dimension(300, 30));

		/* Para pedir el tipo de selección */
		JPanel jpElec = new JPanel();
		jpElec.setLayout(new GridBagLayout());

		/* Texto que pide el tipo de selección */
		JLabel labelElec = new JLabel("Introduzca el tipo de seleccion que desea", SwingConstants.CENTER);
		labelElec.setFont(new Font("", Font.ITALIC, 16));

		/* Botón aceptar selección */
		JButtonRojo bElec = new JButtonRojo("Aceptar");

		final JRadioButton opcion1 = new JRadioButton("Centrada fila y columna");
		final JRadioButton opcion2 = new JRadioButton("Centrada fila inferior");
		final JRadioButton opcion3 = new JRadioButton("Centrada fila superior");
		final JRadioButton opcion4 = new JRadioButton("Alejada");
		opcion1.setSelected(true);

		// Crear un grupo para las opciones, el cual garantizará
		// que sólo una de las opciones está seleccionada
		ButtonGroup grupo = new ButtonGroup();
		// Añadir las opciones al grupo
		grupo.add(opcion1);
		grupo.add(opcion2);
		grupo.add(opcion3);
		grupo.add(opcion4);

		// asociar acciones a componentes
		bElec.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String salida = "";
				if (opcion1.isSelected()) {
					flagElec = 1;
				}
				if (opcion2.isSelected()) {
					flagElec = 2;
				}
				if (opcion3.isSelected()) {
					flagElec = 3;
				}
				if (opcion4.isSelected()) {
					flagElec = 4;
				}
				jpBarra.setVisible(true);
				reservar.setVisible(true);
				comprar.setVisible(true);
				labelIndicacion.setVisible(true);
				texto.setVisible(true);
			}
		});

		// Asociar acciones a botón comprar
		comprar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				boolean notValid = false;
				try {
					entradas = Integer.parseInt(texto.getText());
				} catch(NumberFormatException excep) {
					notValid = true;
				}
				if (!notValid) {
					if (entradas <= 0) {
						JOptionPane.showMessageDialog(null, "El numero de entradas para comprar debe ser superior a 0");
					} else if (Teatro.getUsuarioLogueado() == null) {
						JOptionPane.showMessageDialog(null,
								"Para comprar entradas debe estar registrado y haber iniciado sesion");
					} else {
						jpBarraPagar.setVisible(true);
						pagar.setVisible(true);
					}
				}
			}
		});

		// Asociar acciones a botón apuntarse a lista de espera
		listEspera.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (Teatro.getUsuarioLogueado() == null) {
					JOptionPane.showMessageDialog(null,
							"Para apuntarse a la lista de espera de una representacion debe estar registrado y haber iniciado sesion");
				} else {
					Teatro.getUsuarioLogueado().apuntarListaEspera(r);
					JOptionPane.showMessageDialog(null,
							"Añadido a la lista de espera con exito. Le avisaremos si alguna entrada pasa a estar disponible. Gracias");
				}
			}
		});

		// Asociar acciones a botón pagar
		pagar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					if (flagElec == 1) {
						selec = TipoSeleccion.CentradaFilaColumna;
					} else if (flagElec == 2) {
						selec = TipoSeleccion.CentradaFilaInferior;
					} else if (flagElec == 3) {
						selec = TipoSeleccion.CentradaFilaSuperior;
					} else if (flagElec == 4) {
						selec = TipoSeleccion.Alejada;
					} else {
						JOptionPane.showMessageDialog(null,
								"Ha habido un error. Lo sentimos, intentelo de nuevo mas tarde");
						return;
					}
					flag = Teatro.getUsuarioLogueado().comprarEntradas((ZSNumerada) z, r, selec,
							Integer.parseInt(texto.getText()), tarjeta.getText());
				} catch (NonExistentFileException | UnsupportedImageTypeException | OrderRejectedException e1) {
					e1.printStackTrace();
				}
				if (flag >= 0) {
					JOptionPane.showMessageDialog(null, "¡Muchas gracias por su compra!");
				} else if (flag == -6) {
					JOptionPane.showMessageDialog(null,
							"No hay entradas suficientes. Puede apuntarse a la lista de espera si lo desea");
					listEspera.setVisible(true);
				} else if (flag == -2) {
					JOptionPane.showMessageDialog(null,
							"El numero de entradas supera el limite de entradas asociadas a esta representacion");
				} else {
					JOptionPane.showMessageDialog(null,
							"Ha habido un error. Lo sentimos, intentelo de nuevo mas tarde");
				}
				jpBarraPagar.setVisible(false);
				pagar.setVisible(false);

			}
		});

		// Asociar acciones a botón pagar
		reservar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				boolean notValid = false;
				try {
					entradas = Integer.parseInt(texto.getText());
				} catch(NumberFormatException excep) {
					notValid = true;
				}				
				if (!notValid) {
					if (entradas <= 0) {
						JOptionPane.showMessageDialog(null, "El numero de entradas a reservar debe ser superior a 0");
					} else if (Teatro.getUsuarioLogueado() == null) {
						JOptionPane.showMessageDialog(null,
								"Para reservar entradas debe estar registrado y haber iniciado sesion");
					} else {
						if (flagElec == 1) {
							selec = TipoSeleccion.CentradaFilaColumna;
						} else if (flagElec == 2) {
							selec = TipoSeleccion.CentradaFilaInferior;
						} else if (flagElec == 3) {
							selec = TipoSeleccion.CentradaFilaSuperior;
						} else if (flagElec == 4) {
							selec = TipoSeleccion.Alejada;
						} else {
							JOptionPane.showMessageDialog(null,
									"Ha habido un error. Lo sentimos, intentelo de nuevo mas tarde");
							return;
						}
						if (Teatro.getUsuarioLogueado().realizarReserva((ZSNumerada) z, r, selec, entradas) == true) {
							JOptionPane.showMessageDialog(null,
									"Reserva realizada con exito. Recuerde que puede confirmarla o cancelarla hasta dos dias antes de la fecha de la representacion");
						} else {
							JOptionPane.showMessageDialog(null,
									"Ha habido un error. Lo sentimos, intentelo de nuevo mas tarde");
						}
					}
				}
			}
		});

		// Label
		constraints.ipady = 2;
		constraints.ipadx = 0;
		constraints.gridx = 1;
		constraints.gridy = 18;
		jpElec.add(labelElec, constraints);
		labelElec.setVisible(true);

		// Añadir las opciones al grupo
		constraints.ipady = 2;
		constraints.ipadx = 0;
		constraints.gridx = 1;
		constraints.gridy = 25;
		jpElec.add(opcion1, constraints);
		constraints.ipady = 2;
		constraints.ipadx = 0;
		constraints.gridx = 1;
		constraints.gridy = 27;
		jpElec.add(opcion2, constraints);
		constraints.ipady = 2;
		constraints.ipadx = 0;
		constraints.gridx = 1;
		constraints.gridy = 29;
		jpElec.add(opcion3, constraints);
		constraints.ipady = 2;
		constraints.ipadx = 0;
		constraints.gridx = 1;
		constraints.gridy = 31;
		jpElec.add(opcion4, constraints);

		// Botón
		constraints.ipady = 2;
		constraints.ipadx = 0;
		constraints.gridx = 1;
		constraints.gridy = 34;
		jpElec.add(bElec, constraints);
		bElec.setVisible(true);

		constraints.ipady = 2;
		constraints.ipadx = 0;
		constraints.gridx = 1;
		constraints.gridy = 40;
		jpBarra.add(labelIndicacion, constraints);
		labelIndicacion.setVisible(false);

		constraints.ipady = 2;
		constraints.ipadx = 0;
		constraints.gridx = 1;
		constraints.gridy = 43;
		jpBarra.add(texto, constraints);
		texto.setVisible(false);

		constraints.ipady = 2;
		constraints.ipadx = 0;
		constraints.gridx = 1;
		constraints.gridy = 47;
		jpBarra.add(reservar, constraints);

		constraints.ipady = 2;
		constraints.ipadx = 0;
		constraints.gridx = 1;
		constraints.gridy = 50;
		jpBarra.add(comprar, constraints);

		constraints.ipady = 2;
		constraints.ipadx = 0;
		constraints.gridx = 1;
		constraints.gridy = 60;
		jpBarra.add(listEspera, constraints);

		constraints.ipady = 2;
		constraints.ipadx = 0;
		constraints.gridx = 1;
		constraints.gridy = 75;
		jpBarraPagar.add(labelPagar, constraints);

		constraints.ipady = 2;
		constraints.ipadx = 0;
		constraints.gridx = 1;
		constraints.gridy = 85;
		jpBarraPagar.add(tarjeta, constraints);

		constraints.ipady = 2;
		constraints.ipadx = 0;
		constraints.gridx = 1;
		constraints.gridy = 100;
		jpBarraPagar.add(pagar, constraints);
		pagar.setVisible(false);

		this.add(jpBarra, BorderLayout.CENTER);
		this.add(jpBarraPagar, BorderLayout.SOUTH);
		jpBarraPagar.setVisible(false);
		this.add(jpElec, BorderLayout.NORTH);

	}
}
