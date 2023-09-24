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
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import obras.Evento;
import teatro.Teatro;
import usuarios.Gestor;

/**
 * Clase que implementa el conjunto de funcionalidades para la visualizacion de
 * notificaciones
 */
public class VistaEstadisticas extends JPanel {

	/**
	 * Constructor VistaEstadisticas
	 */
	public VistaEstadisticas() {
		this.setLayout(new GridLayout(0, 3));
		Teatro.setGestor(new Gestor());

		/*
		 * Panel Estadisticas Evento
		 */
		JPanel panelIzda = new VistaEstadisticas.EstadisticaEventosVista();
		this.add(panelIzda, BorderLayout.WEST);

		/*
		 * Panel Estadisticas Zonas
		 */
		JPanel panelCentro = new VistaEstadisticas.EstadisticaZonasVista();
		this.add(panelCentro/* , BorderLayout.CENTER */);

		/*
		 * Panel Estadisticas Representaciones
		 */
		JPanel panelDcha = new JPanel();
		panelDcha.setLayout(new BorderLayout());
		Font font = new Font("", Font.CENTER_BASELINE, 30);

		// Panel contenedor Texto y barra busqueda
		JPanel panelDchaSuperior = new JPanel();
		panelDchaSuperior.setLayout(new BorderLayout());
		panelDcha.add(panelDchaSuperior, BorderLayout.NORTH);

		// Texto
		JLabel label = new JLabel("REPRESENTACIONES", SwingConstants.CENTER);
		label.setFont(font);
		this.add(label/* , BorderLayout.NORTH */);

		// Texto indicacion busqueda
		JLabel labelIndicacion = new JLabel("Introduzca el nombre de un evento", SwingConstants.CENTER);
		labelIndicacion.setFont(new Font("", Font.ITALIC, 16));

		// Barra de busqueda
		JPanel barraBusqueda = new JPanel();
		panelDchaSuperior.add(barraBusqueda, BorderLayout.NORTH);
		barraBusqueda.setLayout(new FlowLayout());

		JPanel panelInfo = new JPanel();
		panelDcha.add(panelInfo, BorderLayout.CENTER);
		JTextField texto = new JTextField();
		texto.setPreferredSize(new Dimension(300, 30));
		JButton aceptar = new JButton("Aceptar");
		aceptar.setForeground(Color.WHITE);
		aceptar.setBackground(Color.RED);
		aceptar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Evento ev = Teatro.getEventoNombre(texto.getText());
				if (ev != null) {
					panelInfo.removeAll();
					panelInfo.add(new VistaEstadisticas.EstadisticaRepresentacionVista(ev), BorderLayout.NORTH);
					panelDcha.updateUI();
				} else {
					JLabel msgError = new JLabel("El evento introducido no existe");
					Font font = new Font("", Font.ITALIC, 16);
					msgError.setFont(font);
					msgError.setForeground(Color.RED);
					panelInfo.removeAll();
					panelInfo.add(msgError, BorderLayout.CENTER);
					panelDcha.updateUI();
				}
			}
		});

		barraBusqueda.add(texto);
		barraBusqueda.add(aceptar);

		panelDchaSuperior.add(barraBusqueda, BorderLayout.SOUTH);
		panelDchaSuperior.add(labelIndicacion, BorderLayout.CENTER);
		panelDchaSuperior.add(label, BorderLayout.NORTH);

		this.add(panelDcha/* , BorderLayout.EAST */);
	}

	/**
	 * Clase interna EstadisticaEventosVista - notificaciones de eventos
	 */
	public static class EstadisticaEventosVista extends JPanel {
		/**
		* Constructor EstadisticaEventosVista
		*/
		public EstadisticaEventosVista() {
			this.setLayout(new BorderLayout());
			Font font = new Font("", Font.CENTER_BASELINE, 30);

			String titulos[] = { "Evento", "Recaudacion", "Ocupacion" };
			DefaultTableModel modeloDatos = new DefaultTableModel(Teatro.getGestor().consultarEstadisticasEventos(),
					titulos);
			JTable tabla = new JTable(modeloDatos);

			// Muestra los datos ordenados
			TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(modeloDatos);
			tabla.setRowSorter(sorter);
			sorter.sort();

			List<RowSorter.SortKey> sortKeys = new ArrayList<>(3);
			sortKeys.add(new RowSorter.SortKey(0, SortOrder.ASCENDING));
			sortKeys.add(new RowSorter.SortKey(1, SortOrder.ASCENDING));
			sortKeys.add(new RowSorter.SortKey(2, SortOrder.ASCENDING));
			sorter.setSortKeys(sortKeys);

			JScrollPane scrollTable = new JScrollPane(tabla);
			this.add(scrollTable, BorderLayout.CENTER);
			JLabel label = new JLabel("EVENTOS", SwingConstants.CENTER);
			label.setFont(font);
			this.add(label, BorderLayout.NORTH);
		}
	}

	/**
	 * Clase interna EstadisticaZonasVista - notificaciones de zonas
	 */
	public static class EstadisticaZonasVista extends JPanel {
		/**
		* Constructor EstadisticaZonasVista
		*/
		public EstadisticaZonasVista() {
			this.setLayout(new BorderLayout());
			Font font = new Font("", Font.CENTER_BASELINE, 30);

			String titulos[] = { "Zona", "Recaudacion" };
			DefaultTableModel modeloDatos = new DefaultTableModel(Teatro.getGestor().consultarEstadisticasZonas(),
					titulos);
			JTable tabla = new JTable(modeloDatos);

			// Muestra los datos ordenados
			TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(modeloDatos);
			tabla.setRowSorter(sorter);
			sorter.sort();

			List<RowSorter.SortKey> sortKeys = new ArrayList<>(2);
			sortKeys.add(new RowSorter.SortKey(0, SortOrder.ASCENDING));
			sortKeys.add(new RowSorter.SortKey(1, SortOrder.ASCENDING));
			sorter.setSortKeys(sortKeys);

			JScrollPane scrollTable = new JScrollPane(tabla);
			this.add(scrollTable, BorderLayout.CENTER);
			JLabel label = new JLabel("ZONAS", SwingConstants.CENTER);
			label.setFont(font);
			this.add(label, BorderLayout.NORTH);
		}

	}

	/**
	 * Clase interna EstadisticaRepresentacionVista - notificaciones de
	 * reprsentaciones
	 */
	public static class EstadisticaRepresentacionVista extends JPanel {
		/**
		* Constructor EstadisticaRepresentacionVista
		* @param e Evento
		*/
		public EstadisticaRepresentacionVista(Evento e) {
			this.setLayout(new BorderLayout());

			String titulos[] = { "Representacion", "Recaudacion", "Ocupacion" };
			DefaultTableModel modeloDatos = new DefaultTableModel(
					Teatro.getGestor().consultarEstadisticasRepresentaciones(e), titulos);
			JTable tabla = new JTable(modeloDatos);

			// Muestra los datos ordenados
			TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(modeloDatos);
			tabla.setRowSorter(sorter);
			sorter.sort();

			List<RowSorter.SortKey> sortKeys = new ArrayList<>(4);
			sortKeys.add(new RowSorter.SortKey(0, SortOrder.ASCENDING));
			sortKeys.add(new RowSorter.SortKey(1, SortOrder.ASCENDING));
			sortKeys.add(new RowSorter.SortKey(2, SortOrder.ASCENDING));
			sorter.setSortKeys(sortKeys);

			JScrollPane scrollTable = new JScrollPane(tabla);
			this.add(scrollTable, BorderLayout.CENTER);
		}
	}
}
