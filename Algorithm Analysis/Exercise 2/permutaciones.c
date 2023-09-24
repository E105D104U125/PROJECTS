/**
 *
 * Descripcion: Implementacion de funciones de generacion de permutaciones
 *
 * Fichero: permutaciones.c
 * Autor: Carlos Aguirre
 * Version: 1.0
 * Fecha: 16-09-2017
 *
 */


#include "permutaciones.h"
#include <stdio.h>
#include <stdlib.h>
#include <math.h>


/***************************************************/
/* Funcion: aleat_num Fecha: 30/9/2020             */
/* Autores: Eduardo Terrés y Víctor Pérez          */
/*                                                 */
/* Rutina que genera un numero aleatorio           */
/* entre dos numeros dados                         */
/*                                                 */
/* Entrada:                                        */
/* int inf: limite inferior                        */
/* int sup: limite superior                        */
/* Salida:                                         */
/* int num_rand: numero aleatorio que pertenece a  */
/* [inf,sup]                                       */
/***************************************************/
/* IMPLEMENTACIÓN 1 */
int aleat_num(int inf, int sup)
{
    int num_rand,k;
    double ratio,rat1,rat2;
    int diff,flag=0;
    
    /* Control de Errores */
    if (inf < 0 || inf > sup) return ERR;
    
    num_rand=rand();
    diff=sup-inf+1;
    
    ratio=(double)(num_rand)/(double)RAND_MAX;
    
    for(k=0;k<diff && flag==0;k++){
        flag=0;
        
        rat1 = (double)k/(double)diff;
        rat2 = (double)(k+1)/(double)diff;
        
        if((ratio >= rat1 && ratio < rat2))flag=1;
    }
    return k-1+inf;
}
/* IMPLEMENTACIÓN 2 */
/*
int aleat_num(int inf, int sup)
{
  int diff;

  diff = sup - inf + 1;
  return ((int)((double)rand()*(double)diff/(double)RAND_MAX)+inf);
}
*/

/***************************************************/
/* Funcion: genera_perm Fecha: 1/10/2020           */
/* Autores: Eduardo Terrés y Víctor Pérez          */
/*                                                 */
/* Rutina que genera una permutacion               */
/* aleatoria                                       */
/*                                                 */
/* Entrada:                                        */
/* int n: Numero de elementos de la                */
/* permutacion                                     */
/* Salida:                                         */
/* int *: puntero a un array de enteros            */
/* que contiene a la permutacion                   */
/* o NULL en caso de error                         */

/***************************************************/
int* genera_perm(int N) 
{
    int *perm = NULL;

    int i, rand_index, aux=0;
    
    /* Control de Errores */
    if(N < 1) return NULL;

    perm = (int*) malloc(N * sizeof (int));

    /*Control de errores*/
    if (perm == NULL) return NULL;

    /*inicializacion de valores*/
    for (i = 0; i < N; i++) {
        perm[i] = i;
    }

    for (i = 0; i < N; i++) {
        rand_index = aleat_num(i,N-1);
        aux = perm[rand_index];
        perm[rand_index] = perm[i];
        perm[i] = aux;
    }
    
    return perm;
}

/***************************************************/
/* Funcion: genera_permutaciones Fecha: 2/10/2020  */
/* Autores: Eduardo Terrés y Víctor Pérez          */
/*                                                 */
/* Funcion que genera n_perms permutaciones        */
/* aleatorias de tamanio elementos                 */
/*                                                 */
/* Entrada:                                        */
/* int n_perms: Numero de permutaciones            */
/* int N: Numero de elementos de cada              */
/* permutacion                                     */
/* Salida:                                         */
/* int** v_perm: Array de punteros a enteros       */
/* que apuntan a cada una de las                   */
/* permutaciones                                   */
/* NULL en caso de error                           */
/***************************************************/
int** genera_permutaciones(int n_perms, int N)
{
    int** v_perm = NULL;
    int i,j;
    
    /* Control de Errores */
    if(N < 1 || n_perms < 1) return NULL;
    
    v_perm = (int**) malloc(n_perms*sizeof(v_perm[0]));
    
    /* Control de Errores */
    if(v_perm == NULL) return NULL;

    for (i = 0; i < n_perms; i++) {
        v_perm[i] = genera_perm(N);
        if (v_perm[i] == NULL) {
            for (j = 0; j < i; j++) {
                free(v_perm[j]);
            }
            free(v_perm);
            return NULL;
        }
    }

    return v_perm;
}
