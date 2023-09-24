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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.JTextComponent;

import entradas.AbonoAnual;
import entradas.AbonoCiclo;
import gui.VistaConfigurarTeatro.VistaDeshabilitarButaca;
import javafx.scene.control.CheckBox;
import obras.*;
import teatro.Teatro;
import zonas.*;

/**
 * Clase que implementa el conjunto de funcionalidades para la gestion de eventos
 */
public class VistaGestionarEventos {
	static Font font = new Font("", Font.ITALIC, 14);

	/* Permite introducir los precios asociados a cada precio zona */
	/**
	 * Constructor
	 */
	protected static class VistaPreciosZona extends JPanel {
		private JTable tabla;
		String[] titulos = { "Precio Zona", "Precio" };
		Object[][] objetosTabla = new Object[Teatro.getZonasSimples().size()][2];
		ArrayList<Zona> zonas = Teatro.getZonasSimples();

		public VistaPreciosZona() {
			this.setLayout(new BorderLayout());

			int i = 0;
			for (Object ob : Teatro.getZonasGUI()) {
				Object[] fila = new Object[2];
				fila[0] = ((Zona) ob).getNombre();
				fila[1] = new Double(0);
				objetosTabla[i++] = fila;
			}

			DefaultTableModel modelo = new DefaultTableModel(objetosTabla, titulos);
			tabla = new JTable(modelo);
			JScrollPane scrollPane = new JScrollPane(tabla);
			scrollPane.setPreferredSize(new Dimension(600, 220));
			scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
			tabla.getColumnModel().getColumn(0).setPreferredWidth(330);
			tabla.getColumnModel().getColumn(1).setPreferredWidth(330);

			JLabel label = new JLabel("Introduzca el precio por zona para el evento", SwingConstants.CENTER);
			label.setFont(new Font("", Font.ITALIC, 15));
			this.add(label, BorderLayout.NORTH);
			this.add(scrollPane, BorderLayout.SOUTH);
			tabla.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		}

		public JTable getTabla() {
			return this.tabla;
		}
	}

	/**
	 * Clase interna que implementa la funcionalidad de crear un evento
	 */
	public static class VistaCrearEvento extends JPanel {
		Font font = new Font("", Font.ITALIC, 14);
		Campo<String> titulo;
		Campo<Double> duracion;
		Campo<String> descripcion;
		Campo<String> autor;
		Campo<String> director;
		VistaGestionarEventos.VistaPreciosZona vPrecZona;
		Campo<String> artistas;
		Campo<String> trabajador;
		Campo<String> trabajador2;
		int selected = -1;

		/**
		 * Constructor
		 */
		public VistaCrearEvento() {
			this.setLayout(new BorderLayout());

			titulo = new Campo<>("Titulo", font, x -> x);
			duracion = new Campo<>("Duracion (min)", font, x -> Double.parseDouble(x));
			descripcion = new Campo<>("Descripcion", font, x -> x, new Dimension(400, 100));
			autor = new Campo<>("Autor", font, x -> x);
			director = new Campo<>("Director", font, x -> x);

			JPanel panelCampos = new JPanel();
			panelCampos.setLayout(new BoxLayout(panelCampos, BoxLayout.Y_AXIS));
			panelCampos.add(titulo.getPanel());
			panelCampos.add(duracion.getPanel());
			panelCampos.add(autor.getPanel());
			panelCampos.add(director.getPanel());
			panelCampos.add(descripcion.getPanel());
			this.add(panelCampos, BorderLayout.CENTER);
			vPrecZona = new VistaGestionarEventos.VistaPreciosZona();
			vPrecZona.setBorder(BorderFactory.createMatteBorder(-1, 110, -1, -1, panelCampos.getBackground()));
			this.add(vPrecZona, BorderLayout.SOUTH);

			JLabel label = new JLabel("Introduzca las caracteristicas generales del evento", SwingConstants.CENTER);
			label.setFont(font);
			this.add(label, BorderLayout.NORTH);
			// Boton creacion del evento
			JPanel panelBoton = new JPanel();
			panelBoton.setLayout(new BorderLayout());
			JButtonRojo siguiente = new JButtonRojo("Siguiente");
			siguiente.setPreferredSize(new Dimension(130, 30));
			panelBoton.add(siguiente, BorderLayout.NORTH);
			this.add(panelBoton, BorderLayout.EAST);

			siguiente.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if (comprobarCampos(vPrecZona, titulo, duracion, autor, director, descripcion) == false)
						VistaGestionarEventos.VistaCrearEvento.this.cambiarSeleccionTipoEvento();
				}
			});
		}

		/**
		 * Metodo que implementa la lógica a seguir para la creacion de distintos tipos
		 * de eventos
		 */
		private void cambiarSeleccionTipoEvento() {
			JPanel panelCampos = new JPanel();
			panelCampos.setLayout(new BoxLayout(panelCampos, BoxLayout.Y_AXIS));

			this.removeAll();
			JLabel label = new JLabel("Seleccione el tipo específico de evento", SwingConstants.CENTER);
			label.setFont(font);

			JPanel panelBotones = new JPanel();
			panelBotones.setLayout(new FlowLayout());
			JButtonRojo concierto = new JButtonRojo("Concierto");
			JButtonRojo danza = new JButtonRojo("Danza");
			JButtonRojo obraTeatro = new JButtonRojo("Obra de Teatro");
			panelBotones.add(concierto);
			panelBotones.add(danza);
			panelBotones.add(obraTeatro);

			concierto.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					artistas = new Campo<>("Interpretes (separado por coma)", font, x -> x, new Dimension(400, 100));
					trabajador = new Campo<>("Orquesta", font, x -> x);
					trabajador2 = new Campo<>("Programa", font, x -> x);
					panelCampos.removeAll();
					panelCampos.add(artistas.getPanel());
					panelCampos.add(trabajador2.getPanel());
					panelCampos.add(trabajador.getPanel());
					panelCampos.updateUI();
					selected = 3;
				}
			});

			danza.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					artistas = new Campo<>("Bailarines (separados por coma)", font, x -> x, new Dimension(400, 100));
					trabajador2 = new Campo<>("Grupo musical", font, x -> x);
					trabajador = new Campo<>("Director de orquesta", font, x -> x);
					panelCampos.removeAll();
					panelCampos.add(artistas.getPanel());
					panelCampos.add(trabajador2.getPanel());
					panelCampos.add(trabajador.getPanel());
					panelCampos.updateUI();
					selected = 1;
				}
			});

			obraTeatro.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					artistas = new Campo<>("Actores (separados por coma)", font, x -> x, new Dimension(400, 100));
					panelCampos.removeAll();
					panelCampos.add(artistas.getPanel());
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

			JButtonRojo aceptar = new JButtonRojo("Crear Evento");
			aceptar.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					boolean notValid = false;
					ArrayList<String> nombreArtistas = new ArrayList<>();
					int i;

					// Comprueba que todos los campos se han rellenado, en funcion del tipo de
					// evento
					if (artistas.getInfo().length() == 0) {
						notValid = true;
					} else {
						for (String str : artistas.getInfo().split(",")) {
							if (str.length() > 0)
								nombreArtistas.add(str);
						}
					}

					if ((selected == 3 || selected == 1) && trabajador2.getInfo().length() == 0
							&& trabajador.getInfo().length() == 0) {
						notValid = true;
					}

					if (!notValid) {
						ArrayList<PrecioZona> pZArray = new ArrayList<>();
						Zona zAux = null;
						for (i = 0; i < vPrecZona.getTabla().getRowCount(); i++) {
							for (Zona z : Teatro.getZonas()) {
								if (z.getNombre().equals((String) vPrecZona.getTabla().getValueAt(i, 0))) {
									zAux = z;
								}
							}
							PrecioZona pZAux = new PrecioZona(
									(double) Double.parseDouble(vPrecZona.getTabla().getValueAt(i, 1).toString()),
									zAux);
							pZArray.add(pZAux);
						}

						switch (selected) {
						case 1:
							new Danza(titulo.getInfo(), duracion.getInfo(), descripcion.getInfo(), autor.getInfo(),
									director.getInfo(), pZArray, (String[]) nombreArtistas.toArray(new String[0]),
									trabajador2.getInfo(), trabajador.getInfo());
							break;
						case 2:
							new ObraTeatro(titulo.getInfo(), duracion.getInfo(), descripcion.getInfo(), autor.getInfo(),
									director.getInfo(), pZArray, (String[]) nombreArtistas.toArray(new String[0]));
							break;
						case 3:
							new Concierto(titulo.getInfo(), (double) duracion.getInfo(), descripcion.getInfo(),
									autor.getInfo(), director.getInfo(), pZArray, trabajador.getInfo(),
									nombreArtistas.toArray(new String[0]), trabajador2.getInfo());
							break;
						}

						if (selected == 1 || selected == 2 || selected == 3) {
							VistaGestionarEventos.borradoExito("Se ha añadido el evento", VistaCrearEvento.this,
									panelCampos);
						}
					}
				}
			});

			aceptar.setPreferredSize(new Dimension(400, 50));
			this.add(aceptar, BorderLayout.SOUTH);
		}

		/**
		 * Metodo que comprueba que los campos tengan entradas correctas
		 * 
		 * @param vPrecZona   Campo a comprobar
		 * @param titulo      Campo a comprobar
		 * @param duracion    Campo a comprobar
		 * @param autor       Campo a comprobar
		 * @param director    Campo a comprobar
		 * @param descripcion Campo a comprobar
		 * 
		 * @return false en caso de fallo de algun campo; false en caso contrario
		 */
		private static boolean comprobarCampos(VistaGestionarEventos.VistaPreciosZona vPrecZona, Campo<String> titulo,
				Campo<Double> duracion, Campo<String> autor, Campo<String> director, Campo<String> descripcion) {
			boolean notValid = false;
			int i;
			try {
				// Comprueba que todos los campos se han rellenado
				for (i = 0; i < vPrecZona.getTabla().getRowCount(); i++) {
					if ((double) Double.parseDouble(vPrecZona.getTabla().getValueAt(i, 1).toString()) < 0) {
						notValid = true;
					}
				}
				if (titulo.getInfo().length() == 0 || duracion.getInfo() < 0 || autor.getInfo().length() == 0
						|| director.getInfo().length() == 0 || descripcion.getInfo().length() == 0) {
					notValid = true;
				}
			} catch (NumberFormatException e) {
				notValid = true;
			}
			return notValid;
		}
	}

	/**
	 * Clase interna que implementa la funcionalidad de crear una representacion
	 */
	public static class VistaCrearRepresentacion extends JPanel {
		private Campo<LocalDate> fecha;
		private Campo<LocalTime> hora;
		private Evento evento;
		Font font = new Font("", Font.ITALIC, 14);

		/**
		 * constructor
		 */
		public VistaCrearRepresentacion() {
			this.setLayout(new BorderLayout());
			fecha = new Campo<>("Fecha (formato: 2024-09-07)", font, x -> LocalDate.parse(x));
			hora = new Campo<>("Hora (formato: 12:05:00)", font, x -> LocalTime.parse(x));

			JPanel panelCampos = new JPanel();
			panelCampos.setLayout(new BoxLayout(panelCampos, BoxLayout.Y_AXIS));
			panelCampos.add(fecha.getPanel());
			panelCampos.add(hora.getPanel());
			this.add(panelCampos, BorderLayout.CENTER);
			// panelCampos.setBorder(BorderFactory.createMatteBorder(70, 10, 10, 100,
			// panelCampos.getBackground()));

			JLabel label = new JLabel("Introduzca las caracteristicas de la representacion", SwingConstants.CENTER);
			label.setFont(font);
			this.add(label, BorderLayout.NORTH);
			// Boton creacion del evento
			JPanel panelBoton = new JPanel();
			panelBoton.setLayout(new BorderLayout());
			JButtonRojo siguiente = new JButtonRojo("Crear Representacion");
			siguiente.setPreferredSize(new Dimension(0, 40));
			panelBoton.add(siguiente, BorderLayout.NORTH);
			this.add(panelBoton, BorderLayout.SOUTH);

			JLabel label2 = new JLabel("Seleccione un evento", SwingConstants.CENTER);
			label2.setFont(font);
			panelCampos.add(label2);
			String[] nombresEvento = Teatro.getEventos().stream().map((e) -> e.getTitulo()).collect(Collectors.toList())
					.toArray(new String[0]);
			// String[] nombresEvento =
			// {"aaaaa","bbbbb","ccccc","ddddd","eeeee","fffff","ggggg","hhhhh"};
			JList<String> list = new JList<>(nombresEvento);
			JScrollPane listScroller = new JScrollPane(list);
			listScroller.setPreferredSize(new Dimension(300, 300));
			listScroller.setMaximumSize(new Dimension(600, 200));
			listScroller.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
			list.setFixedCellWidth(20);
			list.setFixedCellHeight(50);

			DefaultListCellRenderer renderer = (DefaultListCellRenderer) list.getCellRenderer();
			renderer.setHorizontalAlignment(SwingConstants.CENTER);
			panelCampos.setBorder(BorderFactory.createEmptyBorder(-1, 100, 50, -1));

			panelCampos.add(listScroller);
			siguiente.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					boolean notValid = false;
					try {
						if (fecha.getText().length() <= 0 || hora.getText().length() <= 0
								|| list.getSelectedIndex() == -1) {
							notValid = true;
						}
						// Pueden dar error de parsing
						fecha.getInfo();
						hora.getInfo();

						if (notValid == false && Teatro.getEventoNombre(list.getSelectedValue()) == null) {
							notValid = true;
						}
					} catch (DateTimeParseException e1) {
						// Excepcion de parseo
						notValid = true;
					}
					if (notValid == false) {
						// Se anyade en el constructor
						new RepresentacionEvento(fecha.getInfo(), hora.getInfo(),
								Teatro.getEventoNombre((String) list.getSelectedValue()));
						if (LocalDate.now().compareTo(fecha.getInfo()) < 0) {
							VistaGestionarEventos.borradoExito("Se ha añadido la representacion con éxito",
									VistaCrearRepresentacion.this, panelCampos);
						} else {
							JPanel panelPrincipal = VistaCrearRepresentacion.this;
							if (((BorderLayout) panelPrincipal.getLayout())
									.getLayoutComponent(BorderLayout.NORTH) != null)
								panelPrincipal.remove(((BorderLayout) panelPrincipal.getLayout())
										.getLayoutComponent(BorderLayout.NORTH));

							JLabel msgError = new JLabel("Error al crear la representacion, fecha incorrecta.");
							msgError.setFont(font);
							msgError.setForeground(Color.RED);
							panelPrincipal.add(msgError, BorderLayout.NORTH);
							panelPrincipal.updateUI();
						}
					}
				}
			});
		}
	}

	/**
	 * Metodo que borra los elementos de un panel y anyade un mensaje de texto
	 * 
	 * @param text           texto del JLabel anyadido
	 * @param panelPrincipal panel principal
	 * @param panelCampos    panel que contiene los elementos a borrar
	 */
	public static void borradoExito(String text, JPanel panelPrincipal, JPanel panelCampos) {
		JLabel msgExito = new JLabel(text, SwingConstants.CENTER);
		msgExito.setFont(VistaGestionarEventos.font);
		msgExito.setForeground(Color.RED);
		panelCampos.removeAll();
		if (((BorderLayout) panelPrincipal.getLayout()).getLayoutComponent(BorderLayout.NORTH) != null)
			panelPrincipal.remove(((BorderLayout) panelPrincipal.getLayout()).getLayoutComponent(BorderLayout.NORTH));
		if (((BorderLayout) panelPrincipal.getLayout()).getLayoutComponent(BorderLayout.SOUTH) != null)
			panelPrincipal.remove(((BorderLayout) panelPrincipal.getLayout()).getLayoutComponent(BorderLayout.SOUTH));
		panelCampos.add(msgExito);
		panelCampos.updateUI();
		panelPrincipal.updateUI();
	}

	/**
	 * Clase interna que implementa la funcionalidad de crear un ciclo
	 */
	public static class VistaCrearCiclo extends JPanel {
		private Campo<String> nombre;
		private Campo<String> descripcion;
		private ArrayList<Evento> eventos;
		Font font = new Font("", Font.ITALIC, 14);

		/**
		 * Constructor
		 */
		public VistaCrearCiclo() {
			this.setLayout(new BorderLayout());
			nombre = new Campo<>("Nombre", font, x -> x);
			descripcion = new Campo<>("Descripcion", font, x -> x);

			JPanel panelCampos = new JPanel();
			panelCampos.setLayout(new BoxLayout(panelCampos, BoxLayout.Y_AXIS));
			panelCampos.add(nombre.getPanel());
			panelCampos.add(descripcion.getPanel());
			this.add(panelCampos, BorderLayout.CENTER);
			panelCampos.setBorder(BorderFactory.createMatteBorder(70, 10, 10, 100, panelCampos.getBackground()));

			JLabel label = new JLabel("Introduzca las caracteristicas del ciclo", SwingConstants.CENTER);
			label.setFont(font);
			this.add(label, BorderLayout.NORTH);
			// Boton creacion del evento
			JPanel panelBoton = new JPanel();
			panelBoton.setLayout(new BorderLayout());
			JButtonRojo siguiente = new JButtonRojo("Crear Ciclo");
			siguiente.setPreferredSize(new Dimension(0, 40));
			panelBoton.add(siguiente, BorderLayout.NORTH);
			this.add(panelBoton, BorderLayout.SOUTH);

			JLabel label2 = new JLabel("Seleccione los eventos del ciclo", SwingConstants.CENTER);
			label2.setFont(font);
			panelCampos.add(label2);
			String[] nombresEvento = Teatro.getEventos().stream().map((e) -> e.getTitulo()).collect(Collectors.toList())
					.toArray(new String[0]);

			JList<String> list = new JList<>(nombresEvento);
			list.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
			JScrollPane listScroller = new JScrollPane(list);
			listScroller.setPreferredSize(new Dimension(300, 300));
			listScroller.setMaximumSize(new Dimension(700, 200));
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
					eventos = new ArrayList<>(list.getSelectedValuesList().stream()
							.map((x) -> Teatro.getEventoNombre(x)).collect(Collectors.toList()));
					boolean notValid = false;
					if (nombre.getText().length() <= 0 || descripcion.getText().length() <= 0 || eventos.size() == 0) {
						notValid = true;
					}

					if (notValid == false) {
						// Se anyade en el constructor
						new Ciclo(nombre.getInfo(), descripcion.getInfo(), eventos);
						VistaGestionarEventos.borradoExito("Se ha añadido el ciclo", VistaCrearCiclo.this, panelCampos);
					}
				}
			});
		}
	}

	/**
	 * Clase interna que implementa la funcionalidad de crear un abono
	 */
	public static class VistaCrearAbono extends JPanel {
		private Campo<Double> precio;
		private Campo<Double> descuento;
		// private Campo<LocalDate> fechaIni;
		private JComboBox<String> boxZonas;
		private JComboBox<String> boxCiclos;
		int selected = 0;

		/**
		 * Constructor
		 */
		public VistaCrearAbono() {
			JPanel panelCampos = new JPanel();
			panelCampos.setLayout(new BoxLayout(panelCampos, BoxLayout.Y_AXIS));
			panelCampos.setBorder(BorderFactory.createMatteBorder(100, -1, 100, -1, panelCampos.getBackground()));

			this.setLayout(new BorderLayout());
			this.removeAll();
			JLabel label = new JLabel("Seleccione el tipo específico de abono", SwingConstants.CENTER);
			label.setFont(font);

			JPanel panelBotones = new JPanel();
			panelBotones.setLayout(new FlowLayout());
			JButtonRojo abonoCiclo = new JButtonRojo("Abono Ciclo");
			JButtonRojo abonoAnual = new JButtonRojo("Abono Anual");
			panelBotones.add(abonoCiclo);
			panelBotones.add(abonoAnual);

			abonoCiclo.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					precio = new Campo<>("Precio (formato X.x)", font, x -> Double.valueOf(x));
					descuento = new Campo<>("Descuento (formato X.x)", font, x -> Double.valueOf(x));

					// Combo boxes
					boxZonas = new JComboBox<>(Teatro.getZonas().stream().map((x) -> x.getNombre())
							.collect(Collectors.toList()).toArray(new String[0]));
					boxZonas.setBorder(new LineBorder(boxZonas.getBackground(), 20));

					boxCiclos = new JComboBox<>(Teatro.getCiclo().stream().map((x) -> x.getNombre())
							.collect(Collectors.toList()).toArray(new String[0]));
					boxCiclos.setBorder(new LineBorder(boxCiclos.getBackground(), 20));

					// Label
					JLabel label1 = new JLabel("Seleccione una zona", SwingConstants.CENTER);
					label1.setFont(font);

					JLabel label2 = new JLabel("Seleccione un ciclo", SwingConstants.CENTER);
					label2.setFont(font);

					panelCampos.removeAll();
					panelCampos.add(precio.getPanel());
					panelCampos.add(descuento.getPanel());
					panelCampos.add(label1);
					panelCampos.add(boxZonas);
					panelCampos.add(label2);
					panelCampos.add(boxCiclos);
					panelCampos.updateUI();
					selected = 1;
				}
			});

			abonoAnual.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					precio = new Campo<>("Precio (formato X.x)", font, x -> Double.valueOf(x));
					// fechaIni = new Campo<>("Fecha (formato 2024-09-07)", font, x ->
					// LocalDate.parse(x));

					// Combo boxes

					boxZonas = new JComboBox<>(Teatro.getZonas().stream().map((x) -> x.getNombre())
							.collect(Collectors.toList()).toArray(new String[0]));
					boxZonas.setBorder(new LineBorder(boxZonas.getBackground(), 20));

					// Label
					JLabel label1 = new JLabel("Seleccione una zona", SwingConstants.CENTER);
					label1.setFont(font);

					panelCampos.removeAll();
					panelCampos.add(precio.getPanel());
					// panelCampos.add(fechaIni.getPanel());
					panelCampos.add(label1);
					panelCampos.add(boxZonas);
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

			JButtonRojo aceptar = new JButtonRojo("Crear Abono");
			aceptar.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					boolean notValid = false;
					int i;

					switch (selected) {
					case 1:
						if (precio.getText().length() == 0 || descuento.getText().length() == 0
								|| VistaCrearAbono.this.boxZonas.getSelectedIndex() == -1
								|| VistaCrearAbono.this.boxCiclos.getSelectedIndex() == -1) {
							notValid = true;
						}
						if (!notValid) {
							try {
								// Errores de parsing - NumberFormatException
								precio.getInfo();
								descuento.getInfo();

								// Errores de busqueda - NullPointerException
								Teatro.getZonaNombre((String) VistaCrearAbono.this.boxZonas.getSelectedItem());
								Teatro.getCicloNombre((String) VistaCrearAbono.this.boxCiclos.getSelectedItem());
							} catch (NumberFormatException | NullPointerException excp) {
								notValid = true;
							}
						}
						if (!notValid) {
							new AbonoCiclo(precio.getInfo(),
									Teatro.getZonaNombre((String) VistaCrearAbono.this.boxZonas.getSelectedItem()),
									descuento.getInfo(),
									Teatro.getCicloNombre((String) VistaCrearAbono.this.boxCiclos.getSelectedItem()));
							VistaGestionarEventos.borradoExito("Se ha añadido el abono", VistaCrearAbono.this,
									panelCampos);
						}
						break;

					case 2:
						if (precio.getText().length() == 0 /* || fechaIni.getText().length() == 0 */
								|| VistaCrearAbono.this.boxZonas.getSelectedIndex() == -1) {
							notValid = true;
						}
						if (!notValid) {
							try {
								// Errores de parsing - NumberFormatException
								precio.getInfo();
								// fechaIni.getInfo();

								// Errores de busqueda - NullPointerException
								Teatro.getZonaNombre((String) VistaCrearAbono.this.boxZonas.getSelectedItem());
							} catch (NumberFormatException | NullPointerException excp) {
								notValid = true;
							}
						}
						if (!notValid) {
							new AbonoAnual(precio.getInfo(),
									Teatro.getZonaNombre((String) VistaCrearAbono.this.boxZonas.getSelectedItem()),
									LocalDate.of(1000, 1, 1)); // La fecha la decidira el usuario al comprar
							VistaGestionarEventos.borradoExito("Se ha añadido el abono", VistaCrearAbono.this,
									panelCampos);
						}
						break;
					}
				}
			});

			aceptar.setPreferredSize(new Dimension(400, 50));
			this.add(aceptar, BorderLayout.SOUTH);
		}

	}

	/**
	 * Clase interna que implementa la funcionalidad de posponer una representacion
	 */
	public static class VistaPosponerRepresentacion extends JPanel {
		private Campo<LocalDate> nuevaFecha;
		private Campo<LocalTime> nuevaHora;
		private JComboBox<String> boxEventos;
		private JComboBox<String> boxRepr;

		/**
		 * Constructor
		 */
		public VistaPosponerRepresentacion() {
			JPanel panelCampos = new JPanel();
			panelCampos.setLayout(new BoxLayout(panelCampos, BoxLayout.Y_AXIS));
			panelCampos.setBorder(BorderFactory.createMatteBorder(100, 50, 100, -1, panelCampos.getBackground()));

			this.setLayout(new BorderLayout());
			JLabel label = new JLabel("Seleccione el tipo específico de abono", SwingConstants.CENTER);
			label.setFont(font);

			nuevaFecha = new Campo<>("Fecha (formato 2024-09-07)", font, x -> LocalDate.parse(x));
			nuevaHora = new Campo<>("Hora (formato 12:05:00)", font, x -> LocalTime.parse(x));

			// Combo boxes
			String[] l1 = Teatro.getEventos().stream().map((x) -> x.getTitulo()).collect(Collectors.toList())
					.toArray(new String[0]);
			boxEventos = new JComboBox<>(l1);
			if (l1.length > 0)
				boxEventos.setSelectedIndex(0);
			boxEventos.setBorder(new LineBorder(boxEventos.getBackground(), 20));
			boxEventos.setPreferredSize(new Dimension(10, 80));
			boxEventos.setMaximumSize(new Dimension(800, 70));

			boxEventos.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent event) {
					boxRepr.removeAllItems();
					Teatro.getEventoNombre((String) boxEventos.getSelectedItem()).getRepresentaciones().stream()
							.map((x) -> "" + x.getFecha() + " a las " + x.getHora()).collect(Collectors.toList())
							.forEach(x -> boxRepr.addItem(x));
					panelCampos.updateUI();
				}
			});

			String[] l2;
			if (l1.length > 0) {
				l2 = Teatro.getEventoNombre((String) boxEventos.getSelectedItem()).getRepresentaciones().stream()
						.map((x) -> "" + x.getFecha() + " a las " + x.getHora()).collect(Collectors.toList())
						.toArray(new String[0]);
			} else {
				l2 = new String[0];
			}
			boxRepr = new JComboBox<>(l2);
			boxRepr.setBorder(new LineBorder(boxRepr.getBackground(), 20));
			boxRepr.setPreferredSize(new Dimension(10, 80));
			boxRepr.setMaximumSize(new Dimension(800, 70));

			// Label
			JLabel label1 = new JLabel("Seleccione un evento", SwingConstants.CENTER);
			label1.setFont(font);

			JLabel label2 = new JLabel("Seleccione una representacion", SwingConstants.CENTER);
			label2.setFont(font);

			panelCampos.add(label1);
			panelCampos.add(boxEventos);
			panelCampos.add(label2);
			panelCampos.add(boxRepr);
			panelCampos.add(nuevaFecha.getPanel());
			panelCampos.add(nuevaHora.getPanel());
			this.add(panelCampos, BorderLayout.CENTER);

			JButtonRojo aceptar = new JButtonRojo("Posponer representacion");
			aceptar.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					boolean notValid = false;
					Evento ev = null;
					RepresentacionEvento repr = null;
					if (boxRepr.getSelectedIndex() != -1 && boxEventos.getSelectedIndex() != -1
							|| nuevaHora.getText().length() == 0 || nuevaFecha.getText().length() == 0) {
						try {
							ev = Teatro.getEventoNombre((String) boxEventos.getSelectedItem());
							nuevaFecha.getInfo();
							nuevaHora.getInfo();

							String selected = (String) boxRepr.getSelectedItem();
							String[] selValues = selected.split(" a las ");
							LocalDate ld;
							LocalTime lt;
							if (selValues.length == 2) {
								ld = LocalDate.parse(selValues[0]);
								lt = LocalTime.parse(selValues[1]);
								repr = ev.getRepresentaciones().stream()
										.filter(x -> (x.getHora().equals(lt) && x.getFecha().equals(ld)))
										.collect(Collectors.toList()).get(0);
							} else {
								notValid = true;
							}
						} catch (NullPointerException | DateTimeParseException excp) {
							notValid = true;
							excp.printStackTrace();
						}

						if (!notValid && repr != null) {
							if (Teatro.getGestor().posponerRepresentacion(repr, nuevaFecha.getInfo(),
									nuevaHora.getInfo()) == -1) {
								JLabel msgError = new JLabel("Error al posponer la representacion, es muy tarde.");
								msgError.setFont(font);
								msgError.setForeground(Color.RED);
								VistaPosponerRepresentacion.this.add(msgError, BorderLayout.NORTH);
								VistaPosponerRepresentacion.this.updateUI();
							} else {
								VistaGestionarEventos.borradoExito("Se ha pospuesto la fecha para la representacion",
										VistaPosponerRepresentacion.this, panelCampos);
							}
						}
					}
				}
			});

			aceptar.setPreferredSize(new Dimension(400, 50));
			this.add(aceptar, BorderLayout.SOUTH);
		}
	}

	/**
	 * Clase interna que implementa la funcionalidad de cancelar una representacion
	 */
	public static class VistaCancelarRepresentacion extends JPanel {
		private JComboBox<String> boxEventos;
		private JComboBox<String> boxRepr;

		/**
		 * Constructor
		 */
		public VistaCancelarRepresentacion() {
			JPanel panelCampos = new JPanel();
			panelCampos.setLayout(new BoxLayout(panelCampos, BoxLayout.Y_AXIS));
			panelCampos.setBorder(BorderFactory.createMatteBorder(100, 50, 100, -1, panelCampos.getBackground()));

			this.setLayout(new BorderLayout());
			JLabel label = new JLabel("Seleccione el tipo específico de abono", SwingConstants.CENTER);
			label.setFont(font);

			// Combo boxes
			String[] l1 = Teatro.getEventos().stream().map((x) -> x.getTitulo()).collect(Collectors.toList())
					.toArray(new String[0]);
			boxEventos = new JComboBox<>(l1);
			if (l1.length > 0)
				boxEventos.setSelectedIndex(0);
			boxEventos.setBorder(new LineBorder(boxEventos.getBackground(), 20));
			boxEventos.setPreferredSize(new Dimension(10, 80));
			boxEventos.setMaximumSize(new Dimension(800, 70));

			boxEventos.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent event) {
					boxRepr.removeAllItems();
					Teatro.getEventoNombre((String) boxEventos.getSelectedItem()).getRepresentaciones().stream()
							.map((x) -> "" + x.getFecha() + " a las " + x.getHora()).collect(Collectors.toList())
							.forEach(x -> boxRepr.addItem(x));
					panelCampos.updateUI();
				}
			});

			String[] l2;
			if (l1.length > 0) {
				l2 = Teatro.getEventoNombre((String) boxEventos.getSelectedItem()).getRepresentaciones().stream()
						.map((x) -> "" + x.getFecha() + " a las " + x.getHora()).collect(Collectors.toList())
						.toArray(new String[0]);
			} else {
				l2 = new String[0];
			}
			boxRepr = new JComboBox<>(l2);
			boxRepr.setBorder(new LineBorder(boxRepr.getBackground(), 20));
			boxRepr.setPreferredSize(new Dimension(10, 80));
			boxRepr.setMaximumSize(new Dimension(800, 70));

			// Label
			JLabel label1 = new JLabel("Seleccione un evento", SwingConstants.CENTER);
			label1.setFont(font);

			JLabel label2 = new JLabel("Seleccione una representacion", SwingConstants.CENTER);
			label2.setFont(font);

			panelCampos.add(label1);
			panelCampos.add(boxEventos);
			panelCampos.add(label2);
			panelCampos.add(boxRepr);
			this.add(panelCampos, BorderLayout.CENTER);

			JButtonRojo aceptar = new JButtonRojo("Posponer representacion");
			aceptar.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					boolean notValid = false;
					Evento ev = null;
					RepresentacionEvento repr = null;
					if (boxRepr.getSelectedIndex() != -1 && boxEventos.getSelectedIndex() != -1) {
						try {
							ev = Teatro.getEventoNombre((String) boxEventos.getSelectedItem());

							String selected = (String) boxRepr.getSelectedItem();
							String[] selValues = selected.split(" a las ");
							LocalDate ld;
							LocalTime lt;
							if (selValues.length == 2) {
								ld = LocalDate.parse(selValues[0]);
								lt = LocalTime.parse(selValues[1]);
								repr = ev.getRepresentaciones().stream()
										.filter(x -> (x.getHora().equals(lt) && x.getFecha().equals(ld)))
										.collect(Collectors.toList()).get(0);
							} else {
								notValid = true;
							}
						} catch (NullPointerException | DateTimeParseException excp) {
							notValid = true;
							excp.printStackTrace();
						}

						if (!notValid && repr != null) {
							if (Teatro.getGestor().cancelarRepresentacion(repr) == -1) {
								JLabel msgError = new JLabel("Error al cancelar la representacion, es muy tarde.");
								msgError.setFont(font);
								msgError.setForeground(Color.RED);
								VistaCancelarRepresentacion.this.add(msgError, BorderLayout.NORTH);
								VistaCancelarRepresentacion.this.updateUI();
							} else {
								VistaGestionarEventos.borradoExito("Se ha cancelado la representacion",
										VistaCancelarRepresentacion.this, panelCampos);
							}
						}
					}
				}
			});

			aceptar.setPreferredSize(new Dimension(400, 50));
			this.add(aceptar, BorderLayout.SOUTH);
		}
	}
}
