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

  for (i = 0; i < n_claves; i++)
    claves[i] = 1 + (i % max);

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

  for (i = 0; i < n_claves; i++)
  {
    claves[i] = .5 + max / (1 + max * ((double)rand() / (RAND_MAX)));
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
PDICC ini_diccionario(int tamanio, char orden)
{
  PDICC dic = NULL;
  int *tabla = NULL;

  /* CdE */
  /* No admite un diccionario vacio */
  if (tamanio < 1)
    return NULL;
  if (orden != ORDENADO && orden != NO_ORDENADO)
    return NULL;

  dic = (PDICC)malloc(sizeof(dic[0]));
  if (dic == NULL)
    return NULL;

  dic->tamanio = tamanio;
  dic->n_datos = 0;
  dic->orden = orden;

  /* Tabla */
  dic->tabla = (int *)malloc(tamanio * sizeof(tabla[0]));
  if (dic->tabla == NULL)
  {
    free(dic);
    return NULL;
  }

  return dic;
}

/**
 * Autores: Eduardo Terres y Victor Perez         
 * Fecha: 20/11/2020                              
 *
 * Descripcion: La funcion libera toda la memoria asociada
 * a un diccionario.
 * 
 * Parametros entrada:
 *   -PDICC pdicc: diccionario que se quiere liberar
 * Retorno:
 * void
 **/
void libera_diccionario(PDICC pdicc)
{
  if (pdicc != NULL)
  {
    if (pdicc->tabla != NULL)
      free(pdicc->tabla);
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
 *   -PDICC pdicc: diccionario donde se inserta la clave
 *   -int clave: valor a introducir en el diccionario
 * Retorno:
 * La funcion retorna el numero de veces que se ejecuta la OB
 * - cdc -; ERR en caso de error.
 **/
int inserta_diccionario(PDICC pdicc, int clave)
{
  int i, a;

  /* CdE */
  if(pdicc == NULL) return ERR;
  assert(pdicc->orden == ORDENADO || pdicc->orden == NO_ORDENADO);
  /* Diccionario completo */
  if(pdicc->n_datos == pdicc->tamanio) return ERR;

  /* Inserta la clave en la ultima posicion */
  pdicc->tabla[pdicc->n_datos] = clave;

  /* Caso diccionario ordenado */
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
 *   -PDICC pdicc: diccionario donde se quiere insertar las claves
 *   -int *claves: array que contiene las claves
 *   -int n_claves: numero de claves a insertar
 * Retorno:
 * La funcion retorna el numero total de veces que se ejecuta la OB
 * - cdc - de inserta_diccionario; ERR en caso de error.
 **/
int insercion_masiva_diccionario(PDICC pdicc, int *claves, int n_claves)
{
  int i, ret = 0, cont = 0;

  /* CdE */
  if (pdicc == NULL || claves == NULL)
    return ERR;
  assert(n_claves >= 1);
  assert(pdicc->orden == ORDENADO || pdicc->orden == NO_ORDENADO);

  for (i = 0; i < n_claves; i++)
  {
    ret = inserta_diccionario(pdicc, claves[i]);
    if (ret == ERR)
      return ERR;
    cont += ret;
  }

  return cont;
}

/**
 * Autores: Eduardo Terres y Victor Perez         
 * Fecha: 20/11/2020                              
 *
 * Descripcion: La funcion busca una clave en un diccionario
 * introducido por pantalla. La funcion asume que los parametros 
 * de entrada han sido comprobados previamente
 * 
 * Parametros entrada:
 *   -PDICC pdicc: diccionario donde se inserta la clave
 *   -int clave: valor a introducir en el diccionario
 *   -int *ppos: puntero a variable de tipo entero. su estado inicial
 *    es irrelevante, pero debe estar declarado.
 *   -pfunc_busqueda metodo: algoritmo empleado para la busqueda
 * Retorno:
 *   -int *ppos: puntero a variable de tipo entero, donde se almacena
 *    la posicion del elemento buscado.
 * Retorna el numero de operaciones básicas realizadas por metodo;
 * ERR en caso de error.
 **/
int busca_diccionario(PDICC pdicc, int clave, int *ppos, pfunc_busqueda metodo)
{
  /* Previamente comprobados */
  assert(pdicc != NULL);
  assert(ppos != NULL);
  assert(metodo != NULL);
  return metodo(pdicc->tabla, 0, pdicc->n_datos - 1, clave, ppos);
}

/* Funciones de busqueda del TAD Diccionario */
/**
 * Autores: Eduardo Terres y Victor Perez         
 * Fecha: 27/11/2020                              
 *
 * Descripcion: La funcion busca una clave en un diccionario
 * introducido por pantalla. Hace uso del algoritmo de busqueda
 * binaria. Las tabla de entrada debe estar ordenada.
 * 
 * Parametros entrada:
 *   -int *tabla: array de enteros donde se encuentran los elementos
 *    susceptibles de busqueda  
 *   -int *ppos: puntero a variable de tipo entero. su estado inicial
 *    es irrelevante, pero debe estar declarado.
 *   -int P: indice inferior a partir del cual se busca la clave
 *   -int U: indice superior hasta el que se busca la clave
 * Retorno:
 *   -int *ppos: puntero a variable de tipo entero, donde se almacena
 *    la posicion del elemento buscado.
 *    IMPORTANTE: Los indices empiezan a contar desde el 0
 * Retorna el numero de operaciones básicas realizadas por metodo;
 * ERR en caso de error.
 **/
int bbin(int *tabla,int P,int U,int clave,int *ppos)
{
	int medio, cmp;
  int cont = 0;

  /* CdE */
  assert(tabla != NULL);
  assert(P >= 0);
  assert(U >= P);
  assert(ppos != NULL);
  
  while(P <= U){
    medio = (P + U) / 2;
    cmp = tabla[medio] - clave;

    cont ++;
    /* Clave encontrada */
    if(cmp == 0){
      *ppos = medio;
      return cont;
    }

    if(cmp > 0){
      /* Subtabla izquierda */
      U = medio - 1;
    }else{
      /* Subtabla derecha */
      P = medio + 1;
    }
  }

  /* Elemento no encontrado */
  *ppos = NO_ENCONTRADO;
  return cont;
}

/**
 * Autores: Eduardo Terres y Victor Perez         
 * Fecha: 27/11/2020                              
 *
 * Descripcion: La funcion busca una clave en un diccionario
 * introducido por pantalla. Hace uso del algoritmo de busqueda
 * lineal.
 * 
 * Parametros entrada:
 *   -int *tabla: array de enteros donde se encuentran los elementos
 *    susceptibles de busqueda  
 *   -int clave: valor a introducir en el diccionario
 *   -int *ppos: puntero a variable de tipo entero. su estado inicial
 *    es irrelevante, pero debe estar declarado.
 *   -int P: indice inferior a partir del cual se busca la clave
 *   -int U: indice superior hasta el que se busca la clave
 * Retorno:
 *   -int *ppos: puntero a variable de tipo entero, donde se almacena
 *    la posicion del elemento buscado; NO_ENCONTRADO si clave
 *    no se encuentra en la tabla.
 *    IMPORTANTE: Los indices empiezan a contar desde el 0
 * Retorna el numero de operaciones básicas realizadas por metodo;
 * ERR en caso de error.
 **/
int blin(int *tabla,int P,int U,int clave,int *ppos)
{
  int i, cont = 0;

  /* CdE */
  assert(tabla != NULL);
  assert(P >= 0);
  assert(U >= P);
  assert(ppos != NULL);

  for(i = P; i <= U; i++){
    cont ++;
    /* Elemento encontrado */
    if(tabla[i] == clave){
      *ppos = i;
      return cont;
    }
  }

  /* Elemento no encontrado */
  *ppos = NO_ENCONTRADO;
  return cont;
}

/**
 * Autores: Eduardo Terres y Victor Perez         
 * Fecha: 27/11/2020                              
 *
 * Descripcion: La funcion busca una clave en la tabla
 * pasada como argumento que este entre los índices P
 * y U. Emplea el algoritmo de búsqueda lineal 
 * autoorganizada: si se encuentra la clave, se
 * intercambia con el elemento en la posición anterior
 * siempre y cuando este no sea el primero. De esta manera
 * las claves más buscadas se encontraran primero.
 * IMPORTANTE: si la tabla esta ordenada, se desordenara
 * tras la llamada a la funcion.
 * 
 * Parametros entrada:
 *   -int *tabla: array de enteros donde se encuentran los elementos
 *    susceptibles de busqueda
 *   -int clave: valor a introducir en el diccionario
 *   -int *ppos: puntero a variable de tipo entero. su estado inicial
 *    es irrelevante, pero debe estar declarado.
 *   -int P: indice inferior a partir del cual se busca la clave
 *   -int U: indice superior hasta el que se busca la clave
 * Retorno:
 *   -int *ppos: puntero a variable de tipo entero, donde se almacena
 *    la posicion del elemento buscado; NO_ENCONTRADO si clave
 *    no se encuentra en la tabla.
 *    IMPORTANTE: si se ha realizado el swap, esta posición será la
 *    siguiente a la posición en la que se encuentra la clave tras
 *    la función.
 *    IMPORTANTE: Los indices empiezan a contar desde el 0
 * Retorna el numero de operaciones básicas realizadas por metodo;
 * ERR en caso de error.
 **/
int blin_auto(int *tabla,int P,int U,int clave,int *ppos)
{
  int i, cont = 0;

  /* CdE */
  assert(tabla != NULL);
  assert(P >= 0);
  assert(U >= P);
  assert(ppos != NULL);

  for(i = P; i <= U; i++){
    cont ++;
    /* Elemento encontrado */
    if(tabla[i] == clave){
      *ppos = i;
      
      /* Swap tabla[i] <--> tabla[i-1] */
      if(i != P){
        /* No hace falta variable auxiliar para el swap 
         * porque sabemos que tabla[i]=clave */
        tabla[i] = tabla[i-1];
        tabla[i-1] = clave; 
      }
      return cont;
    }
  }

  /* Elemento no encontrado */
  *ppos = NO_ENCONTRADO;
  return cont;
}