/** 
 * 
 * @author Olmar Arranz olmar.arranz@estudiante.uam.es 
 * @author Cristina Madrigal cristina.madrigalb@estudiante.uam.es 
 * @author Eduardo Terrés Caballero eduardo.terres@estudiante.uam.es
 *  
 */
package teatro;

import java.io.*;
import java.util.*;
import java.util.concurrent.Semaphore;
import javax.swing.*;
import entradas.*;
import obras.*;
import usuarios.*;
import zonas.*;
import gui.*;

public class MenuNuevo {
    @SuppressWarnings({ "unchecked", "unused" }) // Se instancian los frames en este main, aunque no se usan
	public static void main(String[] arguments) {
        ArrayList<UsuarioRegistrado> usuarios = null;
        ArrayList<DeshabilitacionButaca> btcDeshabilitadas = null;
        ArrayList<Zona> zonas = null;
        ArrayList<Evento> eventos = null;
        ArrayList<Ciclo> ciclos = null;
        ArrayList<Abono> abonos = null;
        Gestor gestor = null;

        try {
            ObjectInputStream lecturaObjetos = new ObjectInputStream(
                    new FileInputStream("resources/teatroDioniso.ObjectData"));

            usuarios = (ArrayList<UsuarioRegistrado>) lecturaObjetos.readObject();
            btcDeshabilitadas = (ArrayList<DeshabilitacionButaca>) lecturaObjetos.readObject();
            zonas = (ArrayList<Zona>) lecturaObjetos.readObject();
            eventos = (ArrayList<Evento>) lecturaObjetos.readObject();
            ciclos = (ArrayList<Ciclo>) lecturaObjetos.readObject();
            abonos = (ArrayList<Abono>) lecturaObjetos.readObject();
            gestor = (Gestor) lecturaObjetos.readObject();
            Teatro.loadTeatro(usuarios, btcDeshabilitadas, zonas, eventos, ciclos, abonos, gestor);

            lecturaObjetos.close();
        } catch (FileNotFoundException e) {
            // Crea el teatro
            usuarios = new ArrayList<>();
            btcDeshabilitadas = new ArrayList<>();
            zonas = new ArrayList<>();
            eventos = new ArrayList<>();
            ciclos = new ArrayList<>();
            abonos = new ArrayList<>();
            gestor = new Gestor();
        } catch (IOException e2) {
            System.err.print(e2);
            return;
        } catch (ClassNotFoundException e3) {
            System.err.print(e3);
            return;
        }
        
        Semaphore sem = new Semaphore(0);
        
        /* Cuadro de Login - el programa principal se mantiene a la espera */
        Login loginFrame = new Login(sem);
        try {
			sem.acquire();
		} catch (InterruptedException e1) {
			System.err.print("Fallo de progama, no se guardaran los datos de la sesión.");
		}

        /* Ventana en funcion del LogIn - el programa principal se mantiene a la espera */
        if (loginFrame.getCurrentUser() == Login.UsuarioLog.GESTOR) {
            JFrame frame = new VistaGestor(sem);
            try {
    			sem.acquire();
    		} catch (InterruptedException e1) {
    			System.err.print("Fallo de progama, no se guardaran los datos de la sesión.");
    		}
        } else if (loginFrame.getCurrentUser() == Login.UsuarioLog.USUARIOREG
        		|| loginFrame.getCurrentUser() == Login.UsuarioLog.USUARIO) {
        	JFrame frame = new VistaUsuario(sem);
            try {
    			sem.acquire();
    		} catch (InterruptedException e1) {
    			System.err.print("Fallo de progama, no se guardaran los datos de la sesión.");
    		}
        }

        try {
            ObjectOutputStream escrituraObjetos = new ObjectOutputStream(
                    new FileOutputStream("resources/teatroDioniso.ObjectData"));
            usuarios = Teatro.getUsuarios();
            btcDeshabilitadas = Teatro.getBtcDeshabilitadas();
            zonas = Teatro.getZonas();
            eventos = Teatro.getEventos();
            ciclos = Teatro.getCiclo();
            abonos = Teatro.getAbonos();
            gestor = Teatro.getGestor();

            escrituraObjetos.writeObject(usuarios);
            escrituraObjetos.writeObject(btcDeshabilitadas);
            escrituraObjetos.writeObject(zonas);
            escrituraObjetos.writeObject(eventos);
            escrituraObjetos.writeObject(ciclos);
            escrituraObjetos.writeObject(abonos);
            escrituraObjetos.writeObject(gestor);
            escrituraObjetos.close();
        } catch (Exception e) {
            System.err.print(e);
        }

    }

}