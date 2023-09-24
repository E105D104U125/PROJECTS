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
int aleat_num(int inf, int sup)
{
    int diff;

    diff = sup - inf + 1;
    return ((int)((double)rand() * diff / RAND_MAX) + inf);
}

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
int *genera_perm(int N)
{
    int *perm = NULL;

    int i, rand_index, aux = 0;

    /* Control de errores */
    if (N < 1)
        return NULL;

    perm = (int *)malloc(N * sizeof(int));

    /* Control de errores */
    if (perm == NULL)
        return NULL;

    for (i = 1; i <= N; i++)
    {
        perm[i - 1] = i;
    }

    for (i = 0; i < N; i++)
    {
        rand_index = aleat_num(i, N - 1);
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

int **genera_permutaciones(int n_perms, int N)
{
    int **v_perm = NULL;
    int i, j;

    /* Control de errores */
    if (N < 1 || n_perms < 1)
        return NULL;

    v_perm = (int **)malloc(n_perms * sizeof(v_perm[0]));

    /* Control de errores */
    if (v_perm == NULL)
        return NULL;

    for (i = 0; i < n_perms; i++)
    {
        v_perm[i] = genera_perm(N);
        if (v_perm[i] == NULL)
        {
            for (j = 0; j < i; j++)
            {
                free(v_perm[j]);
            }
            free(v_perm);
            return NULL;
        }
    }

    return v_perm;
}
