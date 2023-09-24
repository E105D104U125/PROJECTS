/**
 *
 * Descripcion: Implementacion funciones para busqueda 
 *
 * Fichero: busqueda.c
 * Autor: Carlos Aguirre
 * Version: 1.0
 * Fecha: 11-11-2016
 *
 */

#include "busqueda.h"

#include <stdlib.h>
#include <math.h>
#include <assert.h>

/**
 *  Funciones de generacion de claves
 *
 *  Descripcion: Recibe el numero de claves que hay que generar
 *               en el parametro n_claves. Las claves generadas
 *               iran de 1 a max. Las claves se devuelven en 
 *               el parametro claves que se debe reservar externamente
 *               a la funcion.
 */
  
/**
 *  Funcion: generador_claves_uniforme
 *               Esta fucnion genera todas las claves de 1 a max de forma 
 *               secuencial. Si n_claves==max entonces se generan cada clave
 *               una unica vez.
 */
void generador_claves_uniforme(int *claves, int n_claves, int max)
{
  int i;

  for(i = 0; i < n_claves; i++) claves[i] = 1 + (i % max);

  return;
}

/**
 *  Funcion: generador_claves_potencial
 *               Esta funcion genera siguiendo una distribucion aproximadamente
 *               potencial. Siendo los valores mas pequenos mucho mas probables
 *               que los mas grandes. El valor 1 tiene una probabilidad del 50%,
 *               el dos del 17%, el tres el 9%, etc.
 */
void generador_claves_potencial(int *claves, int n_claves, int max)
{
  int i;

  for(i = 0; i < n_claves; i++) {
    claves[i] = .5+max/(1 + max*((double)rand()/(RAND_MAX)));
  }

  return;
}

/**
 * Autores: Eduardo Terres y Victor Perez         
 * Fecha: 20/11/2020                              
 *
 * Descripcion: La funcion crea un diccionario de tipo orden
 * y con un tamanio maximo de elementos introducido por
 * parametro de entrada
 * 
 * Parametros entrada:
 *   -int tamanio: numero de elementos maximos que admite el
 *    diccionario
 *   -int orden: tipo de diccionario. toma valores ORDENADO o
 *    NO_ORDENADO
 * Retorno:
 * La funcion retorna el diccionario creado; NULL en caso de
 * error
 **/
PDICC ini_diccionario (int tamanio, char orden)
{

  PDICC dicc;
  int *t;

  assert(tamanio>0);
  assert(orden==ORDENADO || orden==NO_ORDENADO);

  dicc = (PDICC) malloc(sizeof(dicc[0]));
  if (dicc==NULL) return NULL;
  t = (int*) malloc(tamanio*sizeof(t[0]));
  if (t==NULL) {
    free(dicc);
    return NULL;
  }

  dicc->tamanio = tamanio;
  dicc->n_datos = 0;
  dicc->orden = orden;
  dicc->tabla = t;

  return dicc; 
}

/**
 * Autores: Eduardo Terres y Victor Perez         
 * Fecha: 20/11/2020                              
 *
 * Descripcion: La funcion libera toda la memoria asociada
 * a un diccionario.
 * 
 * Parametros entrada:
 *   -int pdicc: diccionario que se quiere liberar
 * Retorno:
 * void
 **/
void libera_diccionario(PDICC pdicc)
{
  if (pdicc!=NULL) {
    if (pdicc->tabla!=NULL) free(pdicc->tabla);
    free(pdicc);
  }
  return;
}

/**
 * Autores: Eduardo Terres y Victor Perez         
 * Fecha: 20/11/2020                              
 *
 * Descripcion: La funcion inserta una clave en un diccionario,
 * respetando la ordenacion del mismo
 * 
 * Parametros entrada:
 *   -int pdicc: diccionario donde se inserta la clave
 *   -int clave: valor a introducir en el diccionario
 * Retorno:
 * La funcion retorna el numero de veces que se ejecuta la OB
 * - cdc -; ERR en caso de error.
 **/
int inserta_diccionario(PDICC pdicc, int clave)
{
  int i, a;

  assert(pdicc!=NULL);
  assert(pdicc->tabla!=NULL);

  pdicc->tabla[pdicc->n_datos] = clave;
  if (pdicc->orden==ORDENADO) {
    a = pdicc->tabla[pdicc->n_datos];
    i = pdicc->n_datos - 1;
    while (i>=0 && a<pdicc->tabla[i]) {
      pdicc->tabla[i+1] = pdicc->tabla[i];
      i--;
    }
    pdicc->tabla[i+1] = a;
  }
  pdicc->n_datos++;

  return OK;
}

/**
 * Autores: Eduardo Terres y Victor Perez         
 * Fecha: 20/11/2020                              
 *
 * Descripcion: La funcion inserta una clave en un diccionario,
 * respetando la ordenacion del mismo
 * 
 * Parametros entrada:
 *   -int pdicc: diccionario donde se quiere insertar las claves
 *   -int *claves: array que contiene las claves
 *   -int n_claves: tamanio de claves; numero de claves a insertar
 * Retorno:
 * La funcion retorna el numero total de veces que se ejecuta la OB
 * - cdc - de inserta_diccionario; ERR en caso de error.
 **/
int insercion_masiva_diccionario (PDICC pdicc,int *claves, int n_claves)
{
  int i, ret = 0, cont = 0;

  /* CdE */
	if(pdicc == NULL || claves == NULL || n_claves < 1) return ERR;
  assert(pdicc->orden == ORDENADO || pdicc->orden == NO_ORDENADO);

  for(i = 0; i < n_claves; i++){
    ret = inserta_diccionario(pdicc, claves[i]);
    if(ret == ERR) return ERR;
    cont += ret;
  }

  return cont;
}

/**
 * Autores: Eduardo Terres y Victor Perez         
 * Fecha: 20/11/2020                              
 *
 * Descripcion: La funcion busca una clave en un diccionario
 * introducido por pantalla
 * 
 * Parametros entrada:
 *   -int pdicc: diccionario donde se inserta la clave
 *   -int clave: valor a introducir en el diccionario
 *   -int *ppos: puntero a variable de tipo entero. su estado inicial
 *    es irrelevante, pero debe estar declarado.
 * Retorno:
 *   -int *ppos: puntero a variable de tipo entero, donde se almacena
 *    la posicion del elemento buscado.
 * Retorna el numero de operaciones básicas realizadas por metodo.
 **/
int busca_diccionario(PDICC pdicc, int clave, int *ppos, pfunc_busqueda metodo)
{
	if(pdicc == NULL || ppos == NULL || metodo == NULL) return ERR;
  return metodo(pdicc->tabla, 0, pdicc->n_datos-1, clave, ppos);
}

/**
 * Autores: Eduardo Terres y Victor Perez         
 * Fecha: 27/11/2020                              
 *
 * Descripcion: La funcion busca una clave en la tabla
 * pasada como argumento que este entre los índices P
 * y U, y asigna al puntero ppos la posición de esa
 * clave o NO_ENCONTRADO si no se encuentra.
 * Para ello lleva a cabo el algoritmo de búsqueda
 * binaria, por lo que es NECESARIO que la tabla esté
 * ordenada.
 * 
 * Parametros entrada:
 *   -int *tabla: array ordenado de números entre los
 *    que buscar la clave
 *   -int P: primera posición desde la que buscar en la tabla
 *   -int U: posición hasta la que buscar en la tabla
 *   -int clave: valor a buscar en la tabla
 *   -int *ppos: puntero a variable de tipo entero. su estado inicial
 *    es irrelevante, pero debe estar declarado.
 * Retorno:
 *   -int *ppos: puntero a variable de tipo entero, donde se almacena
 *    la posicion del elemento buscado.
 * Retorna el numero de operaciones básicas realizadas por el
 *  algoritmo de búsqueda binaria.
 **/
int bbin(int *tabla,int P,int U,int clave,int *ppos)
{
  int ini, fin, m, res, cont;

  assert(tabla != NULL);
  assert(P <= U);
  assert(P >= 0);

  ini = P;
  fin = U;
  cont = 0;

  while (ini <= fin) {
    m = (ini+fin) / 2;
    res = tabla[m] - clave;
    if (res == 0) {
      *ppos = m;
      return cont;
    }
    else if (res < 0) {
      ini = m + 1;
    }
    else {
      fin = m - 1;
    }
    cont++; /* Si se encuentra la clave no se ejecuta */
  }

  *ppos = NO_ENCONTRADO;
  return cont;
}

/**
 * Autores: Eduardo Terres y Victor Perez         
 * Fecha: 27/11/2020                              
 *
 * Descripcion: La funcion busca una clave en la tabla
 * pasada como argumento que este entre los índices P
 * y U, y asigna al puntero ppos la posición de esa
 * clave o NO_ENCONTRADO si no se encuentra.
 * Para ello lleva a cabo el algoritmo de búsqueda
 * lineal, por lo que no es necesario que la tabla esté
 * ordenada.
 * 
 * Parametros entrada:
 *   -int *tabla: array de números entre los que buscar la
 *    clave
 *   -int P: primera posición desde la que buscar en la tabla
 *   -int U: posición hasta la que buscar en la tabla
 *   -int clave: valor a buscar en la tabla
 *   -int *ppos: puntero a variable de tipo entero. su estado inicial
 *    es irrelevante, pero debe estar declarado.
 * Retorno:
 *   -int *ppos: puntero a variable de tipo entero, donde se almacena
 *    la posicion del elemento buscado.
 * Retorna el numero de operaciones básicas realizadas por el
 *  algoritmo de búsqueda lineal.
 **/
int blin(int *tabla,int P,int U,int clave,int *ppos)
{
  int i;

  assert(tabla != NULL);
  assert(P <= U);
  assert(P >= 0);

  for (i=0; i<U; i++) {
    if (tabla[i]==clave) {  /* <-- Operación básica */
      *ppos = i;
      return i; /* i es el número de iteraciones del bucle hasta el momento, y por tanto el número de OBs */
    }
  }

  *ppos = NO_ENCONTRADO;
  return i;
}

/**
 * Autores: Eduardo Terres y Victor Perez         
 * Fecha: 27/11/2020                              
 *
 * Descripcion: La funcion busca una clave en la tabla
 * pasada como argumento que este entre los índices P
 * y U, y asigna al puntero ppos la posición de esa
 * clave o NO_ENCONTRADO si no se encuentra.
 * Para ello lleva a cabo el algoritmo de búsqueda
 * lineal autoorganizada: si se encuentra la clave, se
 * intercambia con el elemento en la posición anterior
 * siempre y cuando este no sea el primero. De esta manera
 * las claves más buscadas se encontraran primero.
 * IMPORTANTE: esté o no ordenada la tabla, este orden se
 * romperá al llamar a esta función.
 * 
 * Parametros entrada:
 *   -int *tabla: array de números entre los que buscar la
 *    clave
 *   -int P: primera posición desde la que buscar en la tabla
 *   -int U: posición hasta la que buscar en la tabla
 *   -int clave: valor a buscar en la tabla
 *   -int *ppos: puntero a variable de tipo entero. su estado inicial
 *    es irrelevante, pero debe estar declarado.
 * Retorno:
 *   -int *ppos: puntero a variable de tipo entero, donde se almacena
 *    la posicion en la que se encontraba el elemento buscado.
 *    IMPORTANTE: si se ha realizado el swap, esta posición será la
 *    siguiente a la posición en la que se encuentra la clave tras
 *    la función.
 * Retorna el numero de operaciones básicas realizadas por el
 *  algoritmo de búsqueda lineal autoorganizada.
 **/
int blin_auto(int *tabla,int P,int U,int clave,int *ppos)
{
  int i;

  assert(tabla != NULL);
  assert(P <= U);
  assert(P >= 0);

  for (i=0; i<U; i++) {
    if (tabla[i]==clave) {  /* <-- Operación básica */
      if (i > 0) {
        tabla[i] = tabla[i-1];
        tabla[i-1] = clave; /* No hace falta variable auxiliar para el swap porque sabemos que tabla[i]=clave */
      }
      *ppos = i;
      return i; /* i es el número de iteraciones del bucle hasta el momento, y por tanto el número de OBs */
    }
  }

  *ppos = NO_ENCONTRADO;
  return i;
}


