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
#include <assert.h>
#include <limits.h>

#include "tiempos.h"
#include "busqueda.h"
#include "permutaciones.h"

/* Declaracion funciones privadas */
int libera_memoria(PDICC dicc, int *perms, int *claves);


/* Funciones privadas */
/**
 * Autores: Eduardo Terres y Victor Perez         
 * Fecha: 4/12/2020                              
 *
 * Descripcion: Esta funcion auxiliar libera la memoria 
 * ocupada por los parametros de entrada
 * 
 * Parametros entrada:
 *   -PDICC pdicc: diccionario a liberar
 *   -int *perms: array de enteros a liberar
 *   -int *claves: array de enteros a liberar
 * Retorno:
 * ERR
 **/
int libera_memoria(PDICC dicc, int *perms, int *claves){
    if(dicc != NULL){
        libera_diccionario(dicc);
    }
    if(perms != NULL){
        free(perms);
    }
    if(claves != NULL){
        free(claves);
    }
    return ERR;
}

/*
 * Autores: Eduardo Terres y Victor Perez         
 * Fecha: 4/12/2020                              
 *
 * Descripcion: Genera un fichero con datos asociados a la
 * ejecucion de un algoritmo de busqueda. Se trata de la
 * funcion principal, que llama a la creacion y busqueda de
 * los datos, asi como su almacenamiento.
 * 
 * Parametros entrada:
 * pfunc_busqueda metodo: metodo empleado para la busqueda
 * pfunc_generador_claves generador: funcion generadora de
 * claves a buscar
 * int orden: tipo de tabla - ORDENADA o NO_ORDENADA -
 * int n_veces: numero de veces que busca las claves
 * int N: tamaño de la tabla de datos
 * PTIEMPO ptiempo: estructura que almacena los resultados
 * 
 * Retorno:
 * ERR en caso de error; OK en caso contrario
 **/
short tiempo_medio_busqueda(pfunc_busqueda metodo, pfunc_generador_claves generador,
                            int orden, int N, int n_veces, PTIEMPO ptiempo)
{
    int i, ob, ppos, ob_min, ob_max;
    long ob_suma;
    PDICC dicc = NULL;
    int *perm = NULL, *claves = NULL;
    clock_t ini_t, fin_t;

    /* Comprobaciones que realiza genera_tiempos_busqueda */
    assert(metodo != NULL);
    assert(generador != NULL);
    assert(ptiempo != NULL);
    assert(n_veces > 0);
    /* Comprobaciones que realiza ini_diccionario */
    assert(orden == ORDENADO || orden == NO_ORDENADO);
    assert(N > 0);

    dicc = ini_diccionario(N, orden);
    if(dicc == NULL) return ERR;

    perm = genera_perm(N);
    if(perm == NULL) return libera_memoria(dicc, NULL, NULL);

    if(insercion_masiva_diccionario(dicc, perm, N) == ERR){
        return libera_memoria(dicc, perm, NULL);
    }

    claves = (int *) malloc(sizeof(claves[0]) * N * n_veces);
    if(claves == NULL){
        return libera_memoria(dicc, perm, NULL);
    }

    generador(claves, n_veces * N, N);

    /* Iteraciones del metodo de busqueda */
    /* Clock init value */
    ini_t = clock();
    if (ini_t == (clock_t) - 1) {
        return libera_memoria(dicc, perm, claves);
    }

    /* Inicializacion de parametros */
    ob = 0;
    ob_min = INT_MAX;
    ob_max = 0;
    ob_suma = 0;

    for(i = 0; i < n_veces*N; i++){
        ob = busca_diccionario(dicc, claves[i], &ppos, metodo);
        if(ob == ERR) return libera_memoria(dicc, perm, claves);

        ob_suma += ob; 

        if(ob < ob_min){
            ob_min = ob;
        } else if(ob > ob_max){
            ob_max = ob;
        }
    }

    /* Clock fin value */
    fin_t = clock();
    if (fin_t == (clock_t) - 1) {
        return libera_memoria(dicc, perm, claves);
    }

    /* Almacena los resultados */
    ptiempo->max_ob = ob_max;
    ptiempo->min_ob = ob_min;
    ptiempo->medio_ob = (double) ob_suma/(N*n_veces);
    ptiempo->N = N;
    ptiempo->n_elems = N*n_veces;
    ptiempo->tiempo = (double) (fin_t - ini_t)*1000 / (n_veces * N * CLOCKS_PER_SEC);

    /* Liberacion de memoria */
    libera_memoria(dicc, perm, claves);

    return OK;
}

/*
 * Autores: Eduardo Terres y Victor Perez         
 * Fecha: 4/12/2020                              
 *
 * Descripcion: Genera un fichero con datos asociados a la
 * ejecucion de un algoritmo de busqueda. Se trata de la
 * funcion principal, que llama a la creacion y busqueda de
 * los datos, asi como su almacenamiento.
 * 
 * Parametros entrada:
 * pfunc_busqueda metodo: metodo empleado para la busqueda
 * pfunc_generador_claves generador: funcion generadora de
 * claves a buscar
 * int orden: tipo de tabla - ORDENADA o NO_ORDENADA -
 * char *fichero: fichero de salida para datos
 * int num_min: tamaño minimo de la tabla de datos
 * int num_max: tamaño maximo de la tabla de datos
 * int incr: incremento en tamaño de tabla
 * int n_veces: numero de veces que busca las claves
 * 
 * Retorno:
 * ERR en caso de error; OK en caso contrario
 **/
short genera_tiempos_busqueda(pfunc_busqueda metodo, 
        pfunc_generador_claves generador, 
        int orden, char* fichero, int num_min, 
        int num_max, int incr, int n_veces){

    PTIEMPO ptiempos = NULL;
    int n, i, j;

    /* CdE */
    if (metodo == NULL || generador == NULL || fichero == NULL)
        return ERR;
    assert(n_veces > 0);
    assert(incr > 0);
    assert(num_min >= 0);
    assert(num_max > num_min);
    assert(orden == ORDENADO || orden == NO_ORDENADO);

    /* Reajuste de num_max */
    num_max = num_max - (num_max - num_min) % incr;

    n = (num_max - num_min) / incr + 1;

    ptiempos = (PTIEMPO) malloc(n * sizeof(ptiempos[0]));
    if(ptiempos == NULL) return ERR;

    for(i = num_min, j = 0; i <= num_max && j < n; i += incr, j++){
        if( tiempo_medio_busqueda(metodo, generador,
            orden, i, n_veces, &ptiempos[j]) == ERR ){
            free(ptiempos);
            return ERR;
        }
    }

    if(guarda_tabla_tiempos(fichero, ptiempos, n) == ERR){
        free(ptiempos);
        return ERR;
    }

    free(ptiempos);
    return OK;
}

/*
 * Funcion: guarda_tabla_tiempos    Fecha: 9/10/2020 
 * Autores: Eduardo Terrés y Víctor Pérez            
 *                                                   
 * Descripcion: Función que escribe en un fichero los 
 * tiempos de ejecución dados por un array de estructuras 
 * de tiempo.                                           
 *                                                   
 * Parametros entrada:                                          
 * char* fichero: ruta del fichero a escribir        
 * PTIEMPO tiempo: array de estructuras de tiempo    
 * int n_tiempos: longitud del array tiempo  
 *         
 * Retorno:                                           
 * ERR en caso de error; OK en caso contrario        
 **/
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