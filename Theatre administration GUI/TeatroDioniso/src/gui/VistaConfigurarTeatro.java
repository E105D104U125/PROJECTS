/** 
 * 
 * @author Olmar Arranz olmar.arranz@estudiante.uam.es 
 * @author Cristina Madrigal cristina.madrigalb@estudiante.uam.es 
 * @author Eduardo Terres Caballero eduardo.terres@estudiante.uam.es
 *  
 */
package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.stream.Collectors;

import javax.swing.*;

import teatro.Teatro;
import zonas.DeshabilitacionButaca;
import zonas.ZSNoNumerada;
import zonas.ZSNumerada;
import zonas.Zona;
import zonas.ZonaCompuesta;

/**
 * Clase que implementa el conjunto de funcionalidades para la configuracion del
 * teatro
 */
public class VistaConfigurarTeatro {
	static Font font = new Font("", Font.ITALIC, 14);

	/**
	 * Clase interna que implementa la funcionalidad de deshabilitar una zona
	 */
	public static class VistaDeshabilitarZona extends JPanel {
		private Campo<Integer> cantidad;
		// Deshabilitacion Butaca
		private Campo<String> motivo;
		private Campo<LocalDate> fIni;
		private Campo<LocalDate> fFin;
		int selected = 0;
		JPanel panelCampos;
		Font font = new Font("", Font.ITALIC, 14);

		/**
		 * Constructor
		 */
		public VistaDeshabilitarZona() {
			this.setLayout(new BorderLayout());
			cantidad = new Campo<>("Cantidad de plazas/asientos", font, x -> Integer.valueOf(x));

			panelCampos = new JPanel();
			panelCampos.setLayout(new BoxLayout(panelCampos, BoxLayout.Y_AXIS));
			this.add(panelCampos, BorderLayout.CENTER);

			JLabel label = new JLabel("Seleccione una zona", SwingConstants.CENTER);
			label.setFont(font);
			this.add(label, BorderLayout.NORTH);

			// Boton creacion del evento
			JPanel panelBoton = new JPanel();
			panelBoton.setLayout(new BorderLayout());
			JButtonRojo siguiente = new JButtonRojo("Deshabilitar Zona");
			siguiente.setPreferredSize(new Dimension(0, 40));
			panelBoton.add(siguiente, BorderLayout.NORTH);
			this.add(panelBoton, BorderLayout.SOUTH);

			String[] nombreZonas = Teatro.getZonasSimples().stream().map((e) -> e.getNombre())
					.collect(Collectors.toList()).toArray(new String[0]);
			JList<String> list = new JList<>(nombreZonas);
			JScrollPane listScroller = new JScrollPane(list);
			listScroller.setPreferredSize(new Dimension(300, 500));
			listScroller.setMaximumSize(new Dimension(600, 500));
			listScroller.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
			listScroller.setBorder(BorderFactory.createEmptyBorder(-1, -1, -1, 60));
			list.setFixedCellWidth(20);
			list.setFixedCellHeight(50);
			list.setSelectionBackground(Color.RED);
			list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

			DefaultListCellRenderer renderer = (DefaultListCellRenderer) list.getCellRenderer();
			renderer.setHorizontalAlignment(SwingConstants.CENTER);
			panelCampos.setBorder(BorderFactory.createEmptyBorder(-1, 100, 50, -1));

			panelCampos.add(listScroller);

			list.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent evt) {
					Zona zona = Teatro.getZonaNombre(list.getSelectedValue());
					if (evt.getClickCount() == 1) {
						if (zona instanceof ZSNumerada) {
							motivo = new Campo<>("Motivo de deshabilitacion", font, x -> x);
							fIni = new Campo<>("Fecha Inicio (formato: 2024-09-07)", font, x -> LocalDate.parse(x));
							fFin = new Campo<>("Fecha Finalizacion (formato: 2024-09-07)", font,
									x -> LocalDate.parse(x));
							VistaDeshabilitarZona.this.panelCampos.removeAll();
							if (((BorderLayout) VistaDeshabilitarZona.this.getLayout())
									.getLayoutComponent(BorderLayout.NORTH) != null)
								VistaDeshabilitarZona.this
										.remove(((BorderLayout) VistaDeshabilitarZona.this.getLayout())
												.getLayoutComponent(BorderLayout.NORTH));

							JLabel label = new JLabel("La zona tiene un total de "
									+ ((ZSNumerada) zona).getnColumna() * ((ZSNumerada) zona).getnFila() + " asientos");
							label.setFont(font);
							// label.setBorder(BorderFactory.createEmptyBorder(50, -1, 50, -1));
							VistaDeshabilitarZona.this.panelCampos.add(label);
							VistaDeshabilitarZona.this.panelCampos.add(cantidad.getPanel());

							VistaDeshabilitarZona.this.panelCampos.add(motivo.getPanel());
							VistaDeshabilitarZona.this.panelCampos.add(fIni.getPanel());
							VistaDeshabilitarZona.this.panelCampos.add(fFin.getPanel());
							VistaDeshabilitarZona.this.panelCampos.updateUI();
							selected = 1;

						} else if (zona instanceof ZSNoNumerada || !list.getSelectedValue().equals("bbbbb")) {
							VistaDeshabilitarZona.this.panelCampos.removeAll();
							if (((BorderLayout) VistaDeshabilitarZona.this.getLayout())
									.getLayoutComponent(BorderLayout.NORTH) != null)
								VistaDeshabilitarZona.this
										.remove(((BorderLayout) VistaDeshabilitarZona.this.getLayout())
												.getLayoutComponent(BorderLayout.NORTH));

							JLabel label = new JLabel("La zona tiene un aforo de " + ((ZSNoNumerada) zona).getAforo());
							label.setFont(font);
							// label.setBorder(BorderFactory.createEmptyBorder(50, -1, 50, -1));
							VistaDeshabilitarZona.this.panelCampos.add(label);
							VistaDeshabilitarZona.this.panelCampos.add(cantidad.getPanel());
							VistaDeshabilitarZona.this.panelCampos.updateUI();
							selected = 2;
						}
					}
				}
			});

			siguiente.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					boolean notValid = false;
					int error = 0;
					if ((selected == 1 || selected == 2) && cantidad.getText().length() == 0) {
						notValid = true;
					}
					if (!notValid) {
						switch (selected) {
						case 1:
							if (motivo.getText().length() == 0 || fIni.getText().length() == 0
									|| fFin.getText().length() == 0 || cantidad.getText().length() == 0) {
								notValid = true;
							}
							try {
								// Pueden dar error de parsing
								motivo.getInfo();
								fIni.getInfo();
								fFin.getInfo();
								cantidad.getInfo();

								if (notValid == false && Teatro.getZonaNombre(list.getSelectedValue()) == null) {
									notValid = true;
								}
							} catch (DateTimeParseException e1) {
								notValid = true;
							}
							if (!notValid) {
								DeshabilitacionButaca dbt = new DeshabilitacionButaca(motivo.getInfo(), fIni.getInfo(),
										fFin.getInfo());
								if (Teatro.getGestor().deshabilitarZona(
										(ZSNumerada) Teatro.getZonaNombre(list.getSelectedValue()), dbt,
										cantidad.getInfo()) != -1) {
									int aforo = ((ZSNumerada) Teatro.getZonaNombre(list.getSelectedValue()))
											.getnColumna()
											* ((ZSNumerada) Teatro.getZonaNombre(list.getSelectedValue())).getnFila();
									VistaGestionarEventos.borradoExito("Se han deshabilitado "
											+ (cantidad.getInfo() >= aforo ? aforo : cantidad.getInfo())
											+ " asientos de la zona", VistaDeshabilitarZona.this, panelCampos);
								} else {
									error = 1;
								}
							}

							break;

						case 2:
							if (cantidad.getText().length() == 0) {
								notValid = true;
							}
							try {
								// Pueden dar error de parsing
								cantidad.getInfo();

								if (notValid == false && Teatro.getZonaNombre(list.getSelectedValue()) == null) {
									notValid = true;
								}
							} catch (DateTimeParseException e1) {
								notValid = true;
							}
							if (!notValid) {
								if (Teatro.getGestor().deshabilitarZona(
										(ZSNoNumerada) Teatro.getZonaNombre(list.getSelectedValue()),
										cantidad.getInfo()) != -1) {
									int aforo = ((ZSNoNumerada) Teatro.getZonaNombre(list.getSelectedValue()))
											.getAforo();
									VistaGestionarEventos.borradoExito(
											"Se ha reducido el aforo de la zona en "
													+ (cantidad.getInfo() >= aforo ? aforo : cantidad.getInfo()),
											VistaDeshabilitarZona.this, panelCampos);
								} else {
									error = 1;
								}
							}

							break;
						}
						if (error == 1) {
							JLabel msgError = new JLabel(
									"Error durante la deshabilitación, ya se han vendido entradas");
							msgError.setFont(font);
							msgError.setForeground(Color.RED);
							VistaDeshabilitarZona.this.add(msgError, BorderLayout.NORTH);
						}
					}
				}
			});
		}
	}

	/**
	 * Clase interna que implementa la funcionalidad de habilitar una zona
	 */
	public static class VistaHabilitarZona extends JPanel {
		Font font = new Font("", Font.ITALIC, 14);

		/**
		 * Constructor
		 */
		public VistaHabilitarZona() {
			// Deshabilitacion Butaca
			JPanel panelCampos;
			this.setLayout(new BorderLayout());

			panelCampos = new JPanel();
			panelCampos.setLayout(new BoxLayout(panelCampos, BoxLayout.Y_AXIS));
			this.add(panelCampos, BorderLayout.CENTER);

			JLabel label = new JLabel("Seleccione una zona", SwingConstants.CENTER);
			label.setFont(font);
			this.add(label, BorderLayout.NORTH);

			// Boton creacion del evento
			JPanel panelBoton = new JPanel();
			panelBoton.setLayout(new BorderLayout());
			JButtonRojo siguiente = new JButtonRojo("Habilitar Zona");
			siguiente.setPreferredSize(new Dimension(0, 40));
			panelBoton.add(siguiente, BorderLayout.NORTH);
			this.add(panelBoton, BorderLayout.SOUTH);

			String[] nombreZonas = Teatro.getZonasSimples().stream().map((e) -> e.getNombre())
					.collect(Collectors.toList()).toArray(new String[0]);
			JList<String> list = new JList<>(nombreZonas);
			JScrollPane listScroller = new JScrollPane(list);
			listScroller.setPreferredSize(new Dimension(300, 500));
			listScroller.setMaximumSize(new Dimension(600, 500));
			listScroller.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
			listScroller.setBorder(BorderFactory.createEmptyBorder(-1, -1, -1, 60));
			list.setFixedCellWidth(20);
			list.setFixedCellHeight(50);
			list.setSelectionBackground(Color.RED);
			list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

			DefaultListCellRenderer renderer = (DefaultListCellRenderer) list.getCellRenderer();
			renderer.setHorizontalAlignment(SwingConstants.CENTER);
			panelCampos.setBorder(BorderFactory.createEmptyBorder(-1, 100, 50, -1));

			panelCampos.add(listScroller);

			siguiente.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					int habilitada = 0;
					Zona zona = Teatro.getZonaNombre(list.getSelectedValue());
					if (list.getSelectedIndex() != -1) {
						if (zona instanceof ZSNumerada) {
							Teatro.getGestor().habilitarZona((ZSNumerada) zona);
							habilitada = 1;
						} else if (zona instanceof ZSNoNumerada) {
							Teatro.getGestor().modificarAforo((ZSNoNumerada) zona, 0);
							habilitada = 1;
						}
						if (habilitada == 1) {
							VistaGestionarEventos.borradoExito("Zona habilitada con éxito", VistaHabilitarZona.this,
									panelCampos);
						}
					}
				}
			});
		}
	}

	/**
	 * Clase interna que implementa la funcionalidad de crear una zona
	 */
	public static class VistaCrearZona extends JPanel {
		private Campo<String> nombre;
		private Campo<Integer> aforo;
		private Campo<Double> reduccionAforo;
		private Campo<Integer> nFila;
		private Campo<Integer> nCol;
		int selected = 0;

		/**
		 * Constructor
		 */
		public VistaCrearZona() {
			JPanel panelCampos = new JPanel();
			panelCampos.setLayout(new BoxLayout(panelCampos, BoxLayout.Y_AXIS));
			panelCampos.setBorder(BorderFactory.createMatteBorder(100, -1, 100, -1, panelCampos.getBackground()));
			nombre = new Campo<>("Nombre de la zona", font, x -> x);

			this.setLayout(new BorderLayout());
			this.removeAll();
			JLabel label = new JLabel("Seleccione el tipo de zona", SwingConstants.CENTER);
			label.setFont(font);

			JPanel panelBotones = new JPanel();
			panelBotones.setLayout(new FlowLayout());
			JButtonRojo zonaNumerada = new JButtonRojo("Zona numerada");
			JButtonRojo zonaNoNumerada = new JButtonRojo("Zona no numerada");
			panelBotones.add(zonaNumerada);
			panelBotones.add(zonaNoNumerada);

			zonaNumerada.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					nFila = new Campo<>("Numero de filas", font, x -> Integer.valueOf(x));
					nCol = new Campo<>("Numero de columnas", font, x -> Integer.valueOf(x));

					// Label
					JLabel label1 = new JLabel("", SwingConstants.CENTER);
					label1.setFont(font);

					JLabel label2 = new JLabel("Seleccione un ciclo", SwingConstants.CENTER);
					label2.setFont(font);

					panelCampos.removeAll();
					panelCampos.add(nombre.getPanel());
					panelCampos.add(nFila.getPanel());
					panelCampos.add(nCol.getPanel());
					panelCampos.updateUI();
					selected = 1;
				}
			});

			zonaNoNumerada.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					aforo = new Campo<>("Aforo", font, x -> Integer.valueOf(x));
					reduccionAforo = new Campo<>("Reduccion inicial del aforo", font, x -> Double.valueOf(x));

					panelCampos.removeAll();
					panelCampos.add(nombre.getPanel());
					panelCampos.add(aforo.getPanel());
					panelCampos.add(reduccionAforo.getPanel());
					panelCampos.updateUI();
					selected = 2;
				}
			});

			JPanel zonaNorte = new JPanel();
			zonaNorte.setLayout(new BorderLayout());
			zonaNorte.add(panelBotones, BorderLayout.SOUTH);
			zonaNorte.add(label, BorderLayout.NORTH);

			this.add(panelCampos, BorderLayout.CENTER);
			this.add(zonaNorte, BorderLayout.NORTH);
			this.updateUI();

			JButtonRojo aceptar = new JButtonRojo("Crear Zona");
			aceptar.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					boolean notValid = false;
					if (nombre.getText().length() == 0) {
						notValid = true;
					}
					if (!notValid) {
						switch (selected) {
						case 1:
							try {
								if (nFila.getText().length() == 0 || nCol.getText().length() == 0) {
									notValid = true;
								}
								// Error de parsing
								nFila.getInfo();
								nCol.getInfo();
							} catch (NumberFormatException excep) {
								notValid = true;
							}
							if (!notValid) {
								new ZSNumerada(nombre.getInfo(), nFila.getInfo(), nCol.getInfo());
								VistaGestionarEventos.borradoExito("Zona creada con éxito", VistaCrearZona.this,
										panelCampos);
							}

							break;
						case 2:
							try {
								if (aforo.getText().length() == 0 || reduccionAforo.getText().length() == 0) {
									notValid = true;
								}
								// Error de parsing
								aforo.getInfo();
								reduccionAforo.getInfo();
							} catch (NumberFormatException excep) {
								notValid = true;
							}
							if (!notValid) {
								new ZSNoNumerada(nombre.getInfo(), aforo.getInfo(), reduccionAforo.getInfo());
								VistaGestionarEventos.borradoExito("Zona creada con éxito", VistaCrearZona.this,
										panelCampos);
							}
							break;
						}
					}
				}
			});

			aceptar.setPreferredSize(new Dimension(400, 50));
			this.add(aceptar, BorderLayout.SOUTH);
		}
	}

	/**
	 * Clase interna que implementa la funcionalidad de crear una zona compuesta
	 */
	public static class VistaCrearZonaComp extends JPanel {
		private Campo<String> nombre = new Campo<>("Nombre de la zona", font, x -> x);

		/**
		 * Constructor
		 */
		public VistaCrearZonaComp() {
			this.setLayout(new BorderLayout());
			JPanel panelCampos = new JPanel();
			panelCampos.setLayout(new BoxLayout(panelCampos, BoxLayout.Y_AXIS));
			panelCampos.setBorder(BorderFactory.createMatteBorder(100, -1, 100, -1, panelCampos.getBackground()));

			JLabel label2 = new JLabel("Seleccione las zonas contenidas en la zona compuesta", SwingConstants.CENTER);
			label2.setFont(font);
			label2.setBorder(BorderFactory.createEmptyBorder(30, -1, 30, -1));
			panelCampos.add(nombre.getPanel());
			panelCampos.add(label2);
			String[] nombreZonas = Teatro.getZonas().stream().map((e) -> e.getNombre()).collect(Collectors.toList())
					.toArray(new String[0]);
			this.add(panelCampos, BorderLayout.CENTER);

			JButtonRojo siguiente = new JButtonRojo("Crear Zona Compuesta");
			siguiente.setPreferredSize(new Dimension(0, 40));
			this.add(siguiente, BorderLayout.SOUTH);

			JList<String> list = new JList<>(nombreZonas);
			list.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
			JScrollPane listScroller = new JScrollPane(list);
			listScroller.setPreferredSize(new Dimension(300, 300));
			listScroller.setMaximumSize(new Dimension(600, 200));
			listScroller.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
			list.setFixedCellWidth(20);
			list.setFixedCellHeight(50);

			list.getSelectedValuesList();
			DefaultListCellRenderer renderer = (DefaultListCellRenderer) list.getCellRenderer();
			renderer.setHorizontalAlignment(SwingConstants.CENTER);
			panelCampos.setBorder(BorderFactory.createEmptyBorder(-1, 100, 50, -1));

			list.setSelectionBackground(Color.RED);
			list.setSelectionModel(new DefaultListSelectionModel() {
				@Override
				public void setSelectionInterval(int ind0, int ind1) {
					if (super.isSelectedIndex(ind0)) {
						super.removeSelectionInterval(ind0, ind1);
					} else {
						super.addSelectionInterval(ind0, ind1);
					}
				}
			});

			panelCampos.add(listScroller);
			siguiente.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					ArrayList<Zona> zonas;
					boolean notValid = false;
					if (nombre.getText().length() <= 0 || list.getSelectedIndices().length == 0) {
						notValid = true;
					}
					if (!notValid) {
						zonas = new ArrayList<>(list.getSelectedValuesList().stream()
								.map((x) -> Teatro.getZonaNombre(x)).collect(Collectors.toList()));
						new ZonaCompuesta(nombre.getInfo(), zonas);
						VistaGestionarEventos.borradoExito("Zona compuesta creada con éxito", VistaCrearZonaComp.this,
								panelCampos);
					}
				}
			});

		}
	}

	/**
	 * Clase interna que implementa la funcionalidad de deshabilitar una butaca
	 */
	public static class VistaDeshabilitarButaca extends JPanel {
		private Campo<Integer> nFila;
		private Campo<Integer> nCol;
		private Campo<String> motivo;
		private Campo<LocalDate> fIni;
		private Campo<LocalDate> fFin;

		/**
		 * Constructor
		 */
		public VistaDeshabilitarButaca() {
			nFila = new Campo<>("Nº Fila", font, x -> Integer.valueOf(x), new Dimension(50, 30));
			nCol = new Campo<>("Nº Columna", font, x -> Integer.valueOf(x), new Dimension(50, 30));
			motivo = new Campo<>("Motivo de deshabilitacion", font, x -> x);
			fIni = new Campo<>("Fecha Inicio (formato: 2024-09-07)", font, x -> LocalDate.parse(x));
			fFin = new Campo<>("Fecha Finalizacion (formato: 2024-09-07)", font, x -> LocalDate.parse(x));

			// Deshabilitacion Butaca
			JPanel panelCampos;
			this.setLayout(new BorderLayout());

			panelCampos = new JPanel();
			panelCampos.setLayout(new BoxLayout(panelCampos, BoxLayout.Y_AXIS));
			this.add(panelCampos, BorderLayout.CENTER);

			JLabel label = new JLabel("Seleccione una zona numerada", SwingConstants.CENTER);
			label.setFont(font);
			this.add(label, BorderLayout.NORTH);

			// Boton creacion del evento
			JPanel panelBoton = new JPanel();
			panelBoton.setLayout(new BorderLayout());
			JButtonRojo siguiente = new JButtonRojo("Deshabilitar Butaca");
			siguiente.setPreferredSize(new Dimension(0, 40));
			panelBoton.add(siguiente, BorderLayout.NORTH);
			this.add(panelBoton, BorderLayout.SOUTH);

			// Coge solamente las zonas simples numeradas
			String[] nombreZonas = Teatro.getZonasSimples().stream().filter(x -> x instanceof ZSNumerada)
					.map((e) -> e.getNombre()).collect(Collectors.toList()).toArray(new String[0]);
			JList<String> list = new JList<>(nombreZonas);
			JScrollPane listScroller = new JScrollPane(list);
			listScroller.setPreferredSize(new Dimension(300, 350));
			listScroller.setMaximumSize(new Dimension(600, 350));
			listScroller.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
			listScroller.setBorder(BorderFactory.createEmptyBorder(-1, -1, -1, 60));
			list.setFixedCellWidth(20);
			list.setFixedCellHeight(50);
			list.setSelectionBackground(Color.RED);
			list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

			DefaultListCellRenderer renderer = (DefaultListCellRenderer) list.getCellRenderer();
			renderer.setHorizontalAlignment(SwingConstants.CENTER);
			panelCampos.setBorder(BorderFactory.createEmptyBorder(-1, 100, 50, -1));

			panelCampos.add(listScroller);

			list.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent evt) {
					Zona zona;

					if (evt.getClickCount() == 1 && list.getSelectedIndex() != -1) {
						zona = Teatro.getZonaNombre(list.getSelectedValue());

						JPanel panelPrincipal = VistaDeshabilitarButaca.this;
						if (((BorderLayout) panelPrincipal.getLayout()).getLayoutComponent(BorderLayout.NORTH) != null)
							panelPrincipal.remove(
									((BorderLayout) panelPrincipal.getLayout()).getLayoutComponent(BorderLayout.NORTH));

						panelCampos.removeAll();
						panelCampos.add(motivo.getPanel());
						panelCampos.add(fIni.getPanel());
						panelCampos.add(fFin.getPanel());
						panelCampos.add(nFila.getPanel());
						panelCampos.add(nCol.getPanel());
					}
				}
			});

			siguiente.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					boolean notValid = false;
					int habilitada = 0;

					try {
						if (nFila.getText().length() == 0 || nCol.getText().length() == 0
								|| motivo.getText().length() == 0 || fIni.getText().length() == 0
								|| fFin.getText().length() == 0 || list.getSelectedIndex() == -1) {
							notValid = true;
						}
						nFila.getInfo();
						nCol.getInfo();
						motivo.getInfo();
						fIni.getInfo();
						fFin.getInfo();
						/*
						 * Deberia estar en un primer lugar, pero se comprueba igualmente Si falla, la
						 * funcionalidad para la zona concreta quedará inutilizada
						 */
						Teatro.getZonaNombre(list.getSelectedValue());
					} catch (NumberFormatException | DateTimeParseException | NullPointerException excep) {
						notValid = true;
					}

					ZSNumerada zona = (ZSNumerada) Teatro.getZonaNombre(list.getSelectedValue());
					if (zona.getnColumna() < nCol.getInfo() || nCol.getInfo() < 0 || zona.getnFila() < nFila.getInfo()
							|| nFila.getInfo() < 0) {
						notValid = true;
					}
					if (!notValid) {
						DeshabilitacionButaca dbt = new DeshabilitacionButaca(motivo.getInfo(), fIni.getInfo(),
								fFin.getInfo());
						if (Teatro.getGestor().deshabilitarButaca(zona.getButacas().stream()
								.filter(x -> (x.getColumna() == nCol.getInfo() && x.getFila() == nFila.getInfo()))
								.collect(Collectors.toList()).get(0), dbt) != -1) {
							VistaGestionarEventos.borradoExito("Butaca deshabilitada con éxito",
									VistaDeshabilitarButaca.this, panelCampos);
						} else {
							JLabel msgError = new JLabel(
									"Error durante la deshabilitación, algún parámetro es incorrecto");
							msgError.setFont(font);
							msgError.setForeground(Color.RED);
							VistaDeshabilitarButaca.this.add(msgError, BorderLayout.NORTH);
							VistaDeshabilitarButaca.this.updateUI();
						}
					}
				}
			});
		}
	}

	/**
	 * Clase interna que implementa la funcionalidad de habilitar una butaca
	 */
	public static class VistaHabilitarButaca extends JPanel {
		private Campo<Integer> nFila;
		private Campo<Integer> nCol;

		/**
		 * Constructor
		 */
		public VistaHabilitarButaca() {
			nFila = new Campo<>("Nº Fila", font, x -> Integer.valueOf(x), new Dimension(50, 30));
			nCol = new Campo<>("Nº Columna", font, x -> Integer.valueOf(x), new Dimension(50, 30));

			// Deshabilitacion Butaca
			JPanel panelCampos;
			this.setLayout(new BorderLayout());

			panelCampos = new JPanel();
			panelCampos.setLayout(new BoxLayout(panelCampos, BoxLayout.Y_AXIS));
			this.add(panelCampos, BorderLayout.CENTER);

			JLabel label = new JLabel("Seleccione una zona numerada", SwingConstants.CENTER);
			label.setFont(font);
			this.add(label, BorderLayout.NORTH);

			// Boton creacion del evento
			JPanel panelBoton = new JPanel();
			panelBoton.setLayout(new BorderLayout());
			JButtonRojo siguiente = new JButtonRojo("Deshabilitar Butaca");
			siguiente.setPreferredSize(new Dimension(0, 40));
			panelBoton.add(siguiente, BorderLayout.NORTH);
			this.add(panelBoton, BorderLayout.SOUTH);

			// Coge solamente las zonas simples numeradas
			String[] nombreZonas = Teatro.getZonasSimples().stream().filter(x -> x instanceof ZSNumerada)
					.map((e) -> e.getNombre()).collect(Collectors.toList()).toArray(new String[0]);
			JList<String> list = new JList<>(nombreZonas);
			JScrollPane listScroller = new JScrollPane(list);
			listScroller.setPreferredSize(new Dimension(300, 350));
			listScroller.setMaximumSize(new Dimension(600, 350));
			listScroller.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
			listScroller.setBorder(BorderFactory.createEmptyBorder(-1, -1, -1, 60));
			list.setFixedCellWidth(20);
			list.setFixedCellHeight(50);
			list.setSelectionBackground(Color.RED);
			list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

			DefaultListCellRenderer renderer = (DefaultListCellRenderer) list.getCellRenderer();
			renderer.setHorizontalAlignment(SwingConstants.CENTER);
			panelCampos.setBorder(BorderFactory.createEmptyBorder(-1, 100, 50, -1));

			panelCampos.add(listScroller);

			list.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent evt) {
					Zona zona;

					if (evt.getClickCount() == 1 && list.getSelectedIndex() != -1) {
						zona = Teatro.getZonaNombre(list.getSelectedValue());

						JPanel panelPrincipal = VistaHabilitarButaca.this;
						if (((BorderLayout) panelPrincipal.getLayout()).getLayoutComponent(BorderLayout.NORTH) != null)
							panelPrincipal.remove(
									((BorderLayout) panelPrincipal.getLayout()).getLayoutComponent(BorderLayout.NORTH));

						JLabel label = new JLabel("             ");
						label.setOpaque(true);
						label.setPreferredSize(new Dimension(700, 200));
						JLabel label2 = new JLabel("             ");
						label2.setOpaque(true);
						label2.setPreferredSize(new Dimension(700, 200));
						panelCampos.removeAll();
						panelCampos.add(label);
						panelCampos.add(label2);
						panelCampos.add(nFila.getPanel());
						panelCampos.add(nCol.getPanel());
						panelCampos.add(label2);
					}
				}
			});

			siguiente.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					boolean notValid = false;
					int habilitada = 0;

					try {
						if (nFila.getText().length() == 0 || nCol.getText().length() == 0
								|| list.getSelectedIndex() == -1) {
							notValid = true;
						}
						nFila.getInfo();
						nCol.getInfo();
						/*
						 * Deberia estar en un primer lugar, pero se comprueba igualmente Si falla, la
						 * funcionalidad para la zona concreta quedará inutilizada
						 */
						Teatro.getZonaNombre(list.getSelectedValue());
					} catch (NumberFormatException excep) {
						notValid = true;
					}

					ZSNumerada zona = (ZSNumerada) Teatro.getZonaNombre(list.getSelectedValue());
					if (zona.getnColumna() < nCol.getInfo() || nCol.getInfo() < 0 || zona.getnFila() < nFila.getInfo()
							|| nFila.getInfo() < 0) {
						notValid = true;
					}
					if (!notValid) {
						if (Teatro.getGestor()
								.habilitarButaca(((ZSNumerada) Teatro.getZonaNombre(list.getSelectedValue()))
										.getButacas().stream()
										.filter(x -> (x.getColumna() == nCol.getInfo()
												&& x.getFila() == nFila.getInfo()))
										.collect(Collectors.toList()).get(0)) == 1) {
							VistaGestionarEventos.borradoExito("La butaca ya se encontraba habilitada",
									VistaHabilitarButaca.this, panelCampos);
						} else {
							VistaGestionarEventos.borradoExito("Butaca habilitada con éxito", VistaHabilitarButaca.this,
									panelCampos);
						}
					}
				}
			});
		}
	}
}
