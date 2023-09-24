#include "ordenacion.h"
#include <stdio.h>
#include <stdlib.h>
#include <assert.h>

/* 								     */
/* Declaracion de funciones privadas */
/* 									 */
int merge(int *tabla, int ip, int iu, int imedio);

int medio(int *tabla, int ip, int iu, int *pos);

int partir(int *tabla, int ip, int iu, int *pos);

/***************************************************/
/* Funcion: Merge         Fecha: 23/10/2020        */
/* Autores: Eduardo Terrés y Víctor Pérez          */
/*                                                 */
/* Funcion que combina dos tablas ordenadas en     */
/* otra tabla ordenada también. Se trata de una    */
/* funcion privada, auxiliar de MergeSort		   */
/*                                                 */
/* Entrada:                                        */
/* int* tabla: Array en el que se hace la          */
/* combinación                                     */
/* int ip: indice inferior de la primera tabla     */
/* int iu: indice superior de la segunda tabla     */
/* int imedio: indice superior de la primera tabla */
/* e inferior de la segunda                        */
/* Salida:                                         */
/* Numero de veces que se ejecuta la comparacion   */
/* de indices                                      */
/* ERR en caso de error                            */
/***************************************************/
int merge(int *tabla, int ip, int iu, int imedio)
{
	int tam, k, i, j, cont;
	int *tabla_aux = NULL;

	/* Control de Errores */
	/* Las comprobaciones ya han sido efectuadas por MergeSort */
	assert(ip <= iu);
	assert(ip >= 0);
	assert(tabla != NULL);

	/* Inicializacion de parametros */
	tam = iu - ip + 1;
	i = ip;
	j = imedio + 1;
	k = 0;

	tabla_aux = (int *)malloc(tam * sizeof(tabla_aux[0]));
	if (tabla_aux == NULL)
		return ERR;

	while (i <= imedio && j <= iu)
	{
		if (tabla[i] < tabla[j])
		{
			tabla_aux[k] = tabla[i];
			i++;
		}
		else
		{
			tabla_aux[k] = tabla[j];
			j++;
		}
		k++;
	}
	cont = k;

	/* Se terminan de vaciar las tablas */
	/* Tabla izquierda */
	while (i <= imedio)
	{
		tabla_aux[k] = tabla[i];
		i++;
		k++;
	}

	/* Tabla derecha */
	while (j <= iu)
	{
		tabla_aux[k] = tabla[j];
		j++;
		k++;
	}

	/* Copia tabla_aux en tabla en el intervalo [ip,iu] */
	for (i = 0; i < tam; i++)
	{
		tabla[ip + i] = tabla_aux[i];
	}

	/* Liberacion de la tabla auxiliar */
	free(tabla_aux);

	return cont;
}

/***************************************************/
/* Funcion: MergeSort     Fecha: 23/10/2020        */
/* Autores: Eduardo Terrés y Víctor Pérez          */
/*                                                 */
/* Funcion que ordena los elementos de menor a     */
/* mayor de una tabla dada siguiendo la idea de    */
/* divide y vencerás.                              */
/*                                                 */
/* Entrada:                                        */
/* int* tabla: Array de numeros a ordenar          */
/* int ip: indice inferior a partir del cual se    */
/* quiere ordenar                                  */
/* int iu: indice superior de la tabla hasta el    */
/* que se quiere ordenar                           */
/* Salida:                                         */
/* int cont: numero de veces que se ejecuta la     */
/* operacion básica.                               */
/* ERR en caso de error                            */
/***************************************************/
int mergesort(int *tabla, int ip, int iu)
{
	int imedio, cont1, cont2, cont3;

	/* Control de errores */
	if (ip > iu)
		return ERR;
	if (ip < 0)
		return ERR;
	if (tabla == NULL)
		return ERR;

	/* Caso base de recursion */
	if (iu == ip)
		return 1;

	/* Division de las tablas */
	imedio = (int)(iu + ip) / 2;

	cont1 = mergesort(tabla, ip, imedio);
	if (cont1 == ERR)
		return ERR;

	cont2 = mergesort(tabla, imedio + 1, iu);
	if (cont2 == ERR)
		return ERR;

	cont3 = merge(tabla, ip, iu, imedio);
	if (cont3 == ERR)
		return ERR;

	return cont1 + cont2 + cont3;
}

/***************************************************/
/* Funcion: Medio, auxiliar de QuickSort    	   */
/* Fecha: 30/10/2020     	 	     	           */
/* Autores: Eduardo Terrés y Víctor Pérez          */
/*                                                 */
/* Función que asigna el valor de ip al puntero pos*/
/*                                                 */
/* Entrada:                                        */
/* int* tabla: Array de numeros a ordenar          */
/* int ip: indice inferior a partir del cual se    */
/* quiere ordenar                                  */
/* int iu: indice superior de la tabla hasta el    */
/* que se quiere ordenar                           */
/* int pos: indice superior del pivote	           */
/* Salida:                                         */
/* int *pos: indice del pivote                     */
/* ERR en caso de error                            */
/***************************************************/
int medio(int *tabla, int ip, int iu, int *pos)
{
	/* Control de Errores */
	/* Las comprobaciones ya han sido efectuadas por QuickSort o QuickSort_src*/
	assert(ip >= 0);
	assert(ip <= iu);
	assert(tabla != NULL);

	if (pos == NULL)
		return ERR;

	*pos = ip;
	return 0;
}

/***************************************************/
/* Funcion: Partir, auxiliar de QuickSort 	       */
/* Fecha: 30/10/2020     	 		               */
/* Autores: Eduardo Terrés y Víctor Pérez          */
/*                                                 */
/* Funcion desarrolla la logica del algoritmo      */
/* Quicksort                                       */
/*                                                 */
/* Entrada:                                        */
/* int* tabla: Array de numeros a ordenar          */
/* int ip: indice inferior a partir del cual se    */
/* quiere ordenar                                  */
/* int iu: indice superior de la tabla hasta el    */
/* que se quiere ordenar                           */
/* int pos: indice superior del pivote	           */
/* Salida:                                         */
/* int *pos: indice del pivote                     */
/* ERR en caso de error                            */
/***************************************************/
int partir(int *tabla, int ip, int iu, int *pos)
{
	int t_aux, p_aux, i;
	int cont = 0;

	/* Control de Errores */
	assert(ip >= 0);
	assert(ip <= iu);
	if (pos == NULL || tabla == NULL)
		return ERR;

	if (medio(tabla, ip, iu, pos) == ERR)
		return ERR;

	/* Almacena el valor del pivote */
	p_aux = tabla[*pos];

	*pos = ip;

	/* swap T[pos] <-> T[ip]*/
	t_aux = tabla[*pos];
	tabla[*pos] = tabla[ip];
	tabla[ip] = t_aux;

	for (i = ip + 1; i <= iu; i++)
	{
		if (tabla[i] < p_aux)
		{
			(*pos)++;
			/* swap T[pos] <-> T[i] */
			t_aux = tabla[*pos];
			tabla[*pos] = tabla[i];
			tabla[i] = t_aux;
		}
		cont++;
	}

	/* swap T[pos] <-> T[ip] */
	t_aux = tabla[*pos];
	tabla[*pos] = tabla[ip];
	tabla[ip] = t_aux;

	return cont;
}

/***************************************************/
/* Funcion: QuickSort Fecha: 30/10/2020     	   */
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
/* operacion principal                             */
/* ERR en caso de error                            */
/***************************************************/
int quicksort(int *tabla, int ip, int iu)
{
	int cont = 0, cont1 = 0, cont2 = 0, ret;
	int pos;

	/* Control de Errores */
	if (ip > iu)
		return ERR;
	if (ip < 0)
		return ERR;

	/* Caso base de recursion */
	if (ip == iu)
		return 0;

	if (tabla == NULL)
		return ERR;

	ret = partir(tabla, ip, iu, &pos);
	if (ret == ERR)
		return ERR;
	cont += ret;

	/* Subtabla izquierda */
	if (pos - 1 >= ip)
	{
		ret = quicksort(tabla, ip, pos - 1);
		if (ret == ERR)
			return ERR;
		cont1 += ret;
	}

	/* Subtabla derecha */
	if (pos + 1 <= iu)
	{
		ret = quicksort(tabla, pos + 1, iu);
		if (ret == ERR)
			return ERR;
		cont2 += ret;
	}

	return cont + cont1 + cont2;
}

/***************************************************/
/* Funcion: QuickSort_SRC Fecha: 6/11/2020         */
/* Autores: Eduardo Terrés y Víctor Pérez          */
/*                                                 */
/* Funcion que ordena los elementos de mayor a     */
/* menor de una tabla dada. Es analoga a quicksort */
/* pero eliminando la recursion de cola	           */
/*                                                 */
/* Entrada:                                        */
/* int* tabla: Array de numeros a ordenar          */
/* int ip: indice inferior a partir del cual se    */
/* quiere ordenar                                  */
/* int iu: indice superior de la tabla hasta el    */
/* que se quiere ordenar                           */
/* Salida:                                         */
/* numero de veces que se ejecuta la operacion     */
/* principal                           	           */
/* ERR en caso de error                            */
/***************************************************/
int quicksort_src(int *tabla, int ip, int iu)
{
	int cont = 0, cont1 = 0, ret;
	int pos;

	/* Control de Errores */
	if (ip > iu)
		return ERR;
	if (ip < 0)
		return ERR;
	if (tabla == NULL)
		return ERR;

	while (ip <= iu)
	{
		ret = partir(tabla, ip, iu, &pos);
		if (ret == ERR)
			return ERR;
		cont += ret;

		/* subtabla izquierda */
		if (ip <= pos - 1)
		{
			ret = quicksort_src(tabla, ip, pos - 1);
			if (ret == ERR)
				return ERR;
			cont1 += ret;
		}

		ip = pos + 1;
	}

	return cont + cont1;
}
