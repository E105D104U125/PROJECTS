/** 
 * 
 * @author Olmar Arranz olmar.arranz@estudiante.uam.es 
 * @author Cristina Madrigal cristina.madrigalb@estudiante.uam.es 
 * @author Eduardo Terres Caballero eduardo.terres@estudiante.uam.es
 *  
 */
package gui;

import java.awt.BorderLayout;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.RowSorter;
import javax.swing.SortOrder;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import teatro.Teatro;
import usuarios.UsuarioRegistrado;

/**
 * 
 * Clase que muestra el panel Mis Entradas
 *
 */
public class VistaMisEntradas extends JPanel{

	/**
	 * Constructor VistaMisEntradas
	 * @param u Usuario Registrado
	 */
	public VistaMisEntradas(UsuarioRegistrado u) {
		this.setLayout(new BorderLayout());
		
		String titulos[] = {"Evento", "Fecha Representación", "Hora Representación"};
		DefaultTableModel modeloDatos = new DefaultTableModel(u.consultarEntradas(), titulos);
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
	}

}
