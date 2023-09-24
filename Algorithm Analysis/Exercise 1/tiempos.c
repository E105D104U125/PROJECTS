/**
 *
 * Descripcion: Implementacion de funciones de tiempo
 *
 * Fichero: tiempos.c
 * Autor: Carlos Aguirre Maeso
 * Version: 1.0
 * Fecha: 16-09-2017
 *
 */

#include "tiempos.h"
#include "permutaciones.h"
#include "ordenacion.h"
#include <limits.h>
#include <stdlib.h>
#include <stdio.h>
#include <time.h>

/***************************************************/
/*                Funciones privadas               */
/***************************************************/
void libera_mem(int **perms, int n_perms);

void libera_mem(int **perms, int n_perms) {
    int j;

    if (perms == NULL) return;
    for (j = 0; j < n_perms; j++) {
        free(perms[j]);
    }

    free(perms);
}

/*****************************************************/
/* Funcion: tiempo_medio_ordenacion Fecha: 9/10/2020 */
/* Autores: Eduardo Terrés y Víctor Pérez            */
/*                                                   */
/* Función que calcula el tiempo medio de ejecución, */
/* y el número máximo, mínimo y medio de ejecuciones */
/* de la operación básica del algoritmo de           */
/* ordenacion dado por metodo.                       */
/*                                                   */
/* Entrada:                                          */
/* pfunc_ordena metodo: puntero a la función del     */
/*      algoritmo de ordenacion                      */
/* int n_perms: numero de tablas que va a ordenar el */
/*      algoritmo                                    */
/* int N: longitud de las tablas a ordenar           */
/* PTIEMPO ptiempo: estructura de tiempo en la que   */
/*      se va a guardar la información del algoritmo */
/* Salida:                                           */
/* ERR en caso de error, OK en caso contrario        */
/*****************************************************/
short tiempo_medio_ordenacion(pfunc_ordena metodo,
        int n_perms,
        int N,
        PTIEMPO ptiempo) {
    int **perms = NULL;
    int i, min_ob, max_ob, ob, sum_medio_ob;
    clock_t ini_t, fin_t;

    /* Control de Erorres */
    if (metodo == NULL || ptiempo == NULL || N < 1 || n_perms < 1) return ERR;

    min_ob = INT_MAX;
    max_ob = 0;
    sum_medio_ob = 0;

    perms = genera_permutaciones(n_perms, N);
    if (perms == NULL) return ERR;

    /* Clock init value */
    ini_t = clock();
    if (ini_t == (clock_t) - 1) {
        libera_mem(perms, n_perms);
        return ERR;
    }

    for (i = 0; i < n_perms; i++) {
        ob = metodo(perms[i], 0, N - 1);
        /* Libera la memoria */
        if (ob == ERR) {
            libera_mem(perms, n_perms);
            return ERR;
        }

        sum_medio_ob += ob;

        if (ob < min_ob) {
            min_ob = ob;
        } else if (ob > max_ob) {
            max_ob = ob;
        }
    }

    /* Clock fin value */
    fin_t = clock();
    if (fin_t == (clock_t) - 1) {
        libera_mem(perms, n_perms);
        return ERR;
    }

    /* Asignación valores */

    ptiempo->N = N;
    ptiempo->n_elems = n_perms;
    ptiempo->tiempo = (double) (fin_t - ini_t)*1000 / (n_perms * CLOCKS_PER_SEC);
    ptiempo->min_ob = min_ob;
    ptiempo->max_ob = max_ob;
    ptiempo->medio_ob = (double) sum_medio_ob / n_perms;

    /* Liberación de memoria */
    libera_mem(perms, n_perms);

    return OK;
}


/*****************************************************/
/* Funcion: genera_tiempos_ordenacion                */
/* Fecha: 9/10/2020                                  */
/* Autores: Eduardo Terrés y Víctor Pérez            */
/*                                                   */
/* Función que escribe en un fichero los tiempos de  */
/* ejecución de un algoritmo de ordenación para un   */
/* número dado de permutaciones de longitud variable */
/* entre un mínimo y un máximo con un incremento.    */
/* Ajusta el valor de num_max al múltiplo de incr    */
/* + min_max más cercano.                            */
/*                                                   */
/* Entrada:                                          */
/* pfunc_ordena metodo: puntero a la función del     */
/*      algoritmo de ordenacion                      */
/* char* fichero: ruta del fichero a escribir        */
/* int num_min: longitud mínima de las permutaciones */
/* int num_max: longitud máxima de las permutaciones */
/* int incr: incremento en la longitud de las        */
/*      permutaciones                                */
/* int n_perms: numero de tablas que va a ordenar el */
/*      algoritmo                                    */
/* Salida:                                           */
/* ERR en caso de error, OK en caso contrario        */
/*****************************************************/
short genera_tiempos_ordenacion(pfunc_ordena metodo, char* fichero,
        int num_min, int num_max,
        int incr, int n_perms) {
    PTIEMPO tiempos = NULL;
    int i, j, n, st;
    /* Control de Errores */
    if (metodo == NULL || fichero == NULL || num_min > num_max
            || num_min < 1 || incr < 1 || n_perms < 1) {
        return -1;
    }

    /* Reajuste de num_max */
    num_max = num_max - (num_max - num_min) % incr;

    n = (int) (num_max - num_min) / incr + 1;

    tiempos = (PTIEMPO) malloc(n * sizeof (TIEMPO));
    if (tiempos == NULL) return ERR;

    for (i = num_min, j = 0; i <= num_max; i += incr, j++) {

        st = tiempo_medio_ordenacion(metodo, n_perms, i, &tiempos[j]);

        if (st == ERR) {
            free(tiempos);
            return ERR;
        }

    }

    st = guarda_tabla_tiempos(fichero, tiempos, n);
    free(tiempos);
    return st;
}


/*****************************************************/
/* Funcion: guarda_tabla_tiempos    Fecha: 9/10/2020 */
/* Autores: Eduardo Terrés y Víctor Pérez            */
/*                                                   */
/* Función que escribe en un fichero los tiempos de  */
/* ejecución dados por un array de estructuras de    */
/* tiempo.                                           */
/*                                                   */
/* Entrada:                                          */
/* char* fichero: ruta del fichero a escribir        */
/* PTIEMPO tiempo: array de estructuras de tiempo    */
/* int n_tiempos: longitud del array tiempo          */
/* Salida:                                           */
/* ERR en caso de error, OK en caso contrario        */
/*****************************************************/
short guarda_tabla_tiempos(char* fichero, PTIEMPO tiempo, int n_tiempos) {
    int i;
    FILE *f = NULL;
    f = fopen(fichero, "w");

    if (f == NULL) return ERR;

    for (i = 0; i < n_tiempos; i++) {
        fprintf(f, "%d %f %f %d %d\n", tiempo[i].N,
                tiempo[i].tiempo,
                tiempo[i].medio_ob,
                tiempo[i].min_ob,
                tiempo[i].max_ob);
    }

    if (fclose(f) != 0) return ERR;
    return OK;
}


