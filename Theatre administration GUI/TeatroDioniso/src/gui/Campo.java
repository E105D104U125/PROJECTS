package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.function.Function;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.text.JTextComponent;

public class Campo<T> {
		private JPanel panel = new JPanel();
		private JTextComponent text;
		private JLabel label = new JLabel();
		Function<String, T> funConversion;

		public Campo(String str, Font font, Function<String, T> funConversion) {
			this(str, font, funConversion, new Dimension(300, 30));
		}

		// Constructor para textField de tamanio especifico
		public Campo(String str, Font font, Function<String, T> funConversion, Dimension dim) {
			this.label.setFont(font);
			this.label.setText(String.format("%-20s", str));
			this.funConversion = funConversion;
			JPanel panelAux = new JPanel();
			panelAux.setLayout(new FlowLayout());

			this.panel.setLayout(new BorderLayout());
			if (dim.getHeight() > 70) { this.text = new JTextArea();}
			else { this.text = new JTextField();}
			this.text.setPreferredSize(dim);
			this.panel.add(panelAux, BorderLayout.EAST);
			panelAux.add(this.label);
			panelAux.add(this.text);
		}

		/**
		* Método para obtener el panel

		* @return panel de esta clase
		*/
		public JPanel getPanel() {
			return this.panel;
		}

		public String getText() {
			return this.text.getText();
		}

		public T getInfo() {
			return this.funConversion.apply(this.getText());
		}
	}