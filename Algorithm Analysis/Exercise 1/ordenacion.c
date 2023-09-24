/**
 *
 * Descripcion: Implementacion de funciones de ordenacion 
 *
 * Fichero: ordenacion.c
 * Autor: Carlos Aguirre
 * Version: 1.0
 * Fecha: 16-09-2019
 *
 */


#include "ordenacion.h"
#include <stdio.h>
#include <stdlib.h>

/***************************************************/
/* Funcion: InsertSort    Fecha: 2/10/2020         */
/* Autores: Eduardo Terrés y Víctor Pérez          */
/*                                                 */
/* Funcion que ordena los elementos de menor a     */
/* mayor de una tabla dada.                        */
/*                                                 */
/* Entrada:                                        */
/* int* tabla: Array de numeros a ordenar          */
/* int ip: indice inferior a partir del cual se    */
/* quiere ordenar                                  */
/* int iu: indice superior de la tabla hasta el    */
/* que se quiere ordenar                           */
/* Salida:                                         */
/* int cont: numero de veces que se ejecuta la     */
/* operacion principal -la comparacion del bucle   */
/* while-                                          */
/* ERR en caso de error                            */
/***************************************************/
int InsertSort(int* tabla, int ip, int iu) {
    int i, j, aux, cont;

    /* Control de errores */
    if (tabla == NULL || ip < 0 || iu < ip) return ERR;

    cont = 0;

    for (i = ip + 1; i <= iu; i++) {
        aux = tabla[i];
        j = i - 1;
        while (j >= ip && aux < tabla[j]) {
            tabla[j + 1] = tabla[j];
            j--;
            cont++;
        }
        tabla[j + 1] = aux;
        cont++;
    }

    return cont;
}

/***************************************************/
/* Funcion: InsertSortInv Fecha: 10/10/2020        */
/* Autores: Eduardo Terrés y Víctor Pérez          */
/*                                                 */
/* Funcion que ordena los elementos de mayor a     */
/* menor de una tabla dada.                        */
/*                                                 */
/* Entrada:                                        */
/* int* tabla: Array de numeros a ordenar          */
/* int ip: indice inferior a partir del cual se    */
/* quiere ordenar                                  */
/* int iu: indice superior de la tabla hasta el    */
/* que se quiere ordenar                           */
/* Salida:                                         */
/* int cont: numero de veces que se ejecuta la     */
/* operacion principal -la comparacion del bucle   */
/* while-                                          */
/* ERR en caso de error                            */
/***************************************************/
int InsertSortInv(int* tabla, int ip, int iu) {
    int i, j, aux, cont;

    /* Control de errores */
    if (tabla == NULL || ip < 0 || iu < ip) return ERR;

    cont = 0;

    for (i = iu - 1; i >= ip; i--) {
        aux = tabla[i];
        j = i + 1;
        while (j <= iu && aux < tabla[j]) {
            tabla[j - 1] = tabla[j];
            j++;
            cont++;
        }
        tabla[j - 1] = aux;
        cont++;
    }

    return cont;
}

