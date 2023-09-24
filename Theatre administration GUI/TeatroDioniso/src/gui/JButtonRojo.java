package gui;

import java.awt.Color;

import javax.swing.JButton;

public class JButtonRojo extends JButton{
	private static final long serialVersionUID = -429675785114605507L;

	public JButtonRojo(String str) {
		this.setText(str);
		this.setBackground(Color.RED);
		this.setForeground(Color.WHITE);
	}
}
