/** 
 * 
 * @author Olmar Arranz olmar.arranz@estudiante.uam.es 
 * @author Cristina Madrigal cristina.madrigalb@estudiante.uam.es 
 * @author Eduardo Terrés Caballero eduardo.terres@estudiante.uam.es
 *  
 */

package zonas;

import java.util.*;
import java.time.*;

public class TesterZonas {

    /**
     * Tester para el paquete Zonas
     */
    public static void main(String[] args) {

        System.out.println("-----------------------------");
        System.out.println("Tester Unitario modulo Zona");
        System.out.println("-----------------------------");

        /* Creamos varios zonas simples numeradas y no numeradas */
        // Zona no numerada si reducción de aforo
        ZSNoNumerada pista = new ZSNoNumerada("Pista", 150, 0);
        // Zona no numerada con reducción de aforo
        ZSNoNumerada pista2 = new ZSNoNumerada("Pista2", 150, 0.5);
        // Zona numerada con 15 filas y 20 columnas
        ZSNumerada platea = new ZSNumerada("Platea", 5, 4);
        // Zona numerada con 30 filas y 10 columnas
        ZSNumerada patio = new ZSNumerada("Patio de butacas", 6, 5);

        System.out.println(pista);
        System.out.println(pista2);
        System.out.println(platea);
        System.out.println(patio);
        System.out.println("-----------------------------");

        /* Deshabilitamos las butacas de la última fila de la platea */
        LocalDate fIni = LocalDate.now();
        LocalDate fFin = LocalDate.of(2021, 9, 1);
        DeshabilitacionButaca dbt = new DeshabilitacionButaca("Prueba de deshabilitación", fIni, fFin);
        for (Butaca b : platea.getButacas()) {
            if (b.getFila() == 5) {
                b.deshabilitarButaca(dbt);
            }
            System.out.println(b);
        }

        System.out.println("-----------------------------");

        /* Comprobamos el control de errores de DeshabilitacionButaca */
        LocalDate fCtl1 = LocalDate.of(2017, 4, 8);
        LocalDate fCtl2 = LocalDate.of(2017, 3, 2);
        DeshabilitacionButaca dbtCtrl = new DeshabilitacionButaca("Prueba de deshabilitación", fCtl1, fCtl2);
        System.out.println(dbtCtrl);

        System.out.println("-----------------------------");

        /* Creamos una zona compuesta */
        ArrayList<Zona> zonasSimples = new ArrayList<>();
        // Zona compuesta que contiene pista y platea
        zonasSimples.add(pista);
        zonasSimples.add(platea);
        ZonaCompuesta zonaComp = new ZonaCompuesta("Zona compuesta por pista y platea", zonasSimples);
        // Añado una zona simple a la zona compuesta
        zonaComp.addZona(patio);
        // Comprobamos que la zona compuesta se ha creado bien y que se ha añadido bien
        // la útlima zona
        System.out.println(zonaComp);

        System.out.println("-----------------------------");

        /*
         * Creamos otra zona compuesta formada por una zona simple y por otra compuesta
         */
        ArrayList<Zona> zonas2 = new ArrayList<>();
        zonas2.add(pista2);
        zonas2.add(zonaComp);
        ZonaCompuesta zonaComp2 = new ZonaCompuesta("Zona compuesta (simple+compuesta)", zonas2);
        System.out.println(zonaComp2);
        System.out.println("-----------------------------");
        // Eliminamos una zona
        zonaComp2.getZonas().remove(zonaComp);
        System.out.println(zonaComp2);

    }
}