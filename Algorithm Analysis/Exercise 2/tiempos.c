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

#include <stdio.h>
#include <stdlib.h>
#include <time.h>

#include "tiempos.h"
#include "ordenacion.h"
#include "permutaciones.h"

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
                              PTIEMPO ptiempo)
{
    int i, j, ob, M_ob, m_ob, s_ob;
    int **perms;
    clock_t ini_t, fin_t;

    perms = genera_permutaciones(n_perms, N);
    if (perms==NULL) return ERR;

    m_ob = -1;
    M_ob = 0;
    s_ob = 0;
    ini_t = clock();
    if (ini_t == (clock_t) -1) {
        for (j=0; j<n_perms; j++) free(perms[j]);
        free(perms);
        return ERR;
    }
    for (i=0; i<n_perms; i++) {
        ob = metodo(perms[i], 0, N-1);
        if (ob==ERR) {
            for (j=0; j<n_perms; j++) free(perms[j]);
            free(perms);
            return ERR;
        }
        if (ob > M_ob) M_ob = ob;
        if (m_ob==-1) m_ob = ob;
        else if (ob < m_ob) m_ob = ob;
        s_ob += ob;
    }
    fin_t = clock();
    if (fin_t == (clock_t) -1) {
        for (j=0; j<n_perms; j++) free(perms[j]);
        free(perms);
        return ERR;
    }

    ptiempo->N = N;
    ptiempo->n_elems = n_perms;
    ptiempo->tiempo = ((double)(fin_t - ini_t) / ((double)n_perms*(double)CLOCKS_PER_SEC)) * 1000.0;
    ptiempo->medio_ob = (double) s_ob/n_perms;
    ptiempo->min_ob = m_ob;
    ptiempo->max_ob = M_ob;

    for (i=0; i<n_perms; i++) free(perms[i]);
    free(perms);

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
                                int incr, int n_perms)
{
    PTIEMPO tiempos;
    int i, j, n, flag;
    
    n = (int) (num_max-num_min)/incr + 1;
    tiempos = (PTIEMPO) malloc(n*sizeof(TIEMPO));
    if (tiempos==NULL) return ERR;
    
    for (i=num_min, j=0; i<=num_max; i+=incr, j++) {
        flag = tiempo_medio_ordenacion(metodo, n_perms, i, &tiempos[j]);
        if (flag==ERR) {
            free(tiempos);
            return ERR;
        }
    }

    flag = guarda_tabla_tiempos(fichero, tiempos, n);
    free(tiempos);
    return flag;
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
short guarda_tabla_tiempos(char* fichero, PTIEMPO tiempo, int n_tiempos)
{
    FILE *f;
    int i;

    f = fopen(fichero, "w");
    if (f==NULL) return ERR;

    for (i=0; i<n_tiempos; i++) {
        fprintf(f, "%d %f %f %d %d\n", tiempo[i].N, tiempo[i].tiempo, tiempo[i].medio_ob, tiempo[i].min_ob, tiempo[i].max_ob);
    }

    if (fclose(f)!=0) return ERR;
    return OK;
}


