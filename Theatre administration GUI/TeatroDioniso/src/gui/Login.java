package gui;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.concurrent.Semaphore;

import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import teatro.Teatro;
import usuarios.Usuario;
import usuarios.UsuarioRegistrado;

@SuppressWarnings("serial")
public class Login extends JFrame{
		Container container = getContentPane();
	    JLabel lUsuario = new JLabel("USER");
	    JLabel lPass = new JLabel("PASSWORD");
	    JLabel textLogin = new JLabel("Introduzca su nombre de usuario y contraseña:");
	    JTextField textField = new JTextField();
	    JPasswordField passwordField = new JPasswordField();
	    JButtonRojo logButton = new JButtonRojo("LOGIN");
	    JButtonRojo resetButton= new JButtonRojo("RESET");
	    JButtonRojo regButton = new JButtonRojo("REGISTER");
	    JButtonRojo contSinReg = new JButtonRojo("CONTINUAR SIN REGISTRARSE");
	    JCheckBox showPassword = new JCheckBox("Mostrar Contraseña");
	    UsuarioLog user;
	 
	    
	    public enum UsuarioLog {
	    	USUARIOREG, USUARIO, GESTOR;
	    }
	 
	    public Login(Semaphore sem) {
	        this.setLayout(null);
	        this.setLocationAndSize();
	        this.addActionListeners();
	        this.setTitle("Login");
	        this.setBounds(10, 10, 400, 480);
	        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	        this.setResizable(false);
	        this.setVisible(true);
	        container.add(lUsuario);
	        container.add(lPass);
	        container.add(textField);
	        container.add(textLogin);
	        container.add(passwordField);
	        container.add(showPassword);
	        container.add(logButton);
	        container.add(resetButton);
	        container.add(regButton);
	        container.add(contSinReg);

			this.addComponentListener(new ComponentAdapter() {
				@Override
				public void componentHidden(ComponentEvent e) {
					if (sem != null)
						sem.release();
				}
			});
	    }
	    
	    public UsuarioLog getCurrentUser() {
	    	return user;
	    }
	 
	    public void setLocationAndSize() {
	    	lUsuario.setBounds(70, 100, 100, 30);
	    	lPass.setBounds(70, 150, 100, 30);
	    	textField.setBounds(150, 100, 150, 30);
	        passwordField.setBounds(150, 150, 150, 30);
	        showPassword.setBounds(165, 185, 150, 30);
	        logButton.setBounds(70, 250, 120, 30);
	        regButton.setBounds(200, 250, 120, 30);
	        resetButton.setBounds(240, 290, 80, 30);
	        textLogin.setBounds(67, 50, 300, 30);
	        contSinReg.setBounds(70, 330, 250, 30);
	    }
	    
	    public void addActionListeners() {
	    	logButton.addActionListener(
	        	new ActionListener() {
		        	public void actionPerformed(ActionEvent e) {
		        		String userName = textField.getText();
		                String pwdText = String.valueOf(passwordField.getPassword());
		                // Lamada a metodo de login
		                int ret = Teatro.logIn(userName, pwdText);
		                if (ret == -2 || ret == -1) {
		                	// Login incorrecto
		                    JOptionPane.showMessageDialog(null, "Introduzca un nombre y contraseña válidos.");	
		                } else if (ret == 1) {
		                	// Usuario Registrado
		                	user = Login.UsuarioLog.USUARIOREG;
		    				Login.this.setVisible(false);
		                } else if (ret == 0) {
		                	// Gestor
		                	user = Login.UsuarioLog.GESTOR;
		    				Login.this.setVisible(false);
		                }
		    	    }
	        	}
		       );
	    	
	        resetButton.addActionListener(
		        	new ActionListener() {
			        	public void actionPerformed(ActionEvent e) {
			        		textField.setText("");
				            passwordField.setText("");
			    	    }
		        	}
			       );
	        
	        showPassword.addActionListener(
		        	new ActionListener() {
			        	public void actionPerformed(ActionEvent e) {
			        		if (showPassword.isSelected()) {
				                passwordField.setEchoChar((char) 0);
				            } else {
				                passwordField.setEchoChar('*');
				            }
			    	    }
		        	}
			       );
	        
	        regButton.addActionListener(
		        	new ActionListener() {
			        	public void actionPerformed(ActionEvent e) {
			        		String userText = textField.getText();
			                String pwdText = String.valueOf(passwordField.getPassword());
			        		if (userText.length() == 0 || pwdText.length() == 0) {
			                    JOptionPane.showMessageDialog(null, "Introduzca un nombre y contraseña válidos.");	
			        		}
			        		else {
			        			// Llamada a la funcion de registro
			        			UsuarioRegistrado usr = Usuario.registrarUsuario(userText, pwdText);
			        			if ( usr == null) {
				        			JOptionPane.showMessageDialog(null, "El usuario ya existe");
			        			} else {
			        				Teatro.setUsuarioLogueado(usr);
				                	user = Login.UsuarioLog.USUARIOREG;
				    				Login.this.setVisible(false);
			        			}
						}
					}
				});

		contSinReg.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Teatro.setUsuarioLogueado(null);
				user = Login.UsuarioLog.USUARIO;
				Login.this.setVisible(false);
			}
		});

	}
	 
    /*public static void main(String[] a) {
        Login logFrame = new Login();
        logFrame.setTitle("Cuadro de login");
        //logFrame.setBounds(10, 10, 400, 380);
        logFrame.setBounds(10, 10, 400, 480);
        logFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        logFrame.setResizable(false);
        logFrame.setVisible(true);
 
    }*/
}
