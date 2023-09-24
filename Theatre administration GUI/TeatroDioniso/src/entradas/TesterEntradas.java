/** 
 * 
 * @author Olmar Arranz olmar.arranz@estudiante.uam.es 
 * @author Cristina Madrigal cristina.madrigalb@estudiante.uam.es 
 * @author Eduardo Terrés Caballero eduardo.terres@estudiante.uam.es
 *  
 */
package entradas;

import zonas.*;
import obras.*;
import java.time.*;
import java.util.*;

public class TesterEntradas {

    /**
     * Tester para el paquete Entradas
     * 
     * @param args argumentos del main
     */
    public static void main(String[] args) throws NullPointerException {

        System.out.println("-----------------------------");
        System.out.println("Tester Unitario módulo Entradas");
        System.out.println("-----------------------------");

        /* Creamos varios zonas simples numeradas y no numeradas */
        // Zona no numerada con reducción de aforo
        ZSNoNumerada pista = new ZSNoNumerada("Pista2", 150, 0.5);
        // Zona numerada con 5 filas y 4 columnas
        ZSNumerada platea = new ZSNumerada("Platea", 5, 4);

        /* Creamos un abono anual */
        LocalDate fechaAnual = LocalDate.of(2021, 03, 8);
        AbonoAnual anual = new AbonoAnual(120, pista, fechaAnual);
        System.out.println(anual);
        // AÃ±adimos precio negativo al abono
        anual.setPrecioAbono(-83.05);
        System.out.println(anual);
        System.out.println("-----------------------------");

        /* Creamos precios para las zonas */
        // Para la platea
        PrecioZona precPlatea = new PrecioZona(30.40, platea);
        System.out.println(precPlatea);
        // Para la pista
        PrecioZona precPista = new PrecioZona(20, pista);
        System.out.println(precPista);
        // Ponemos precio negativo y se deberÃ­a poner a 0
        precPista.setPrecio(-15.83);
        System.out.println(precPista);
        // Volvemos a poner el precio normal
        precPista.setPrecio(20);
        System.out.println(precPista);
        System.out.println("-----------------------------");

        /* Creamos una representacion y dos entradas */
        // Evento y Representacion
        LocalDate fechaRepr = LocalDate.of(2021, 05, 12);
        ArrayList<PrecioZona> precios = new ArrayList<>();
        precios.add(precPista);
        precios.add(precPlatea);
        String[] actores = { "Pilar Avila", "Alexia Lorrio" };
        ObraTeatro eventoPrueba = new ObraTeatro("La casa de Bernarda Alba", 100, "Evento de prueba",
                "Federico Garcia�a Lorca", "Anar Olmeda", precios, actores);
        LocalTime horaRepr = LocalTime.of(17, 00, 00);
        RepresentacionEvento reprPrueba = new RepresentacionEvento(fechaRepr, horaRepr, eventoPrueba);
        // Entradas
        Ticket entr1 = new Ticket(precPista, false, reprPrueba);
        Butaca b = new Butaca(1, 1);
        EntradaNumerada entr2 = new EntradaNumerada(precPlatea, false, reprPrueba, b);
        System.out.println(entr1);
        System.out.println(entr2);
        System.out.println("-----------------------------");

        /* Creamos una reserva de las entradas anteriores */
        // Reserva con una entrada
        ArrayList<Ticket> entradas = new ArrayList<>();
        entradas.add(entr1);
        Reserva reserva = new Reserva(reprPrueba, entradas);
        System.out.println(reserva);
        // Añadimos otra entrada
        reserva.addEntrada(entr2);
        System.out.println(reserva);
        // Imprimimos el precio total de la reserva
        System.out.println("Precio total de la reserva: " + reserva.calcularPrecio());
        System.out.println("-----------------------------");
    }
}
