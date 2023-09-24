/***********************************************/
/* Programa: ejercicio0     Fecha: 20/11/2020  */
/* Autores: Eduardo Terres y Victor Perez      */
/*                                             */
/* Programa que comprueba el funcionamiento de */
/* TAD Diccionario. La generacion de claves    */
/* es aleatoria.                               */
/*                                             */
/* Entrada: Linea de comandos                  */
/*   -tamanio: numero elementos diccionario    */
/*   -orden: tipo de diccionario; toma valores */
/*   0 == NO_ORDENADO, 1 == ORDENADO           */
/*                                             */
/* Salida: Muesta por pantalla los elementos   */
/* del diccionario                             */
/***********************************************/

#include<stdlib.h>
#include<stdio.h>
#include<string.h>
#include<time.h>

#include "permutaciones.h"
#include "busqueda.h"

int main(int argc, char** argv)
{
  int i, nob;
  unsigned int tamanio = 0, orden = 0;
  PDICC pdicc;
  int *perm;

  srand(time(NULL));

  if (argc != 5) {
    fprintf(stderr, "Error en los parametros de entrada (%d)\n\n", argc);
    fprintf(stderr, "%s -tamanio <int>\n", argv[0]);
    fprintf(stderr, "Donde:\n");
    fprintf(stderr, " -tamanio : numero elementos de la tabla.\n");
    fprintf(stderr, "Donde:\n");
    fprintf(stderr, " -orden : tipo de diccionario.\n");
    exit(-1);
  }

  printf("Practica numero 3, apartado 0\n");
  printf("Realizada por: Eduardo Terres y Victor Perez\n");
  printf("Grupo: 06\n");

  /* comprueba la linea de comandos */
  for(i = 1; i < argc; i++) {
    if (strcmp(argv[i], "-tamanio") == 0) {
      tamanio = atoi(argv[++i]);
    } else if (strcmp(argv[i], "-orden") == 0) {
      orden = atoi(argv[++i]);
    } else {
      fprintf(stderr, "Parametro %s es incorrecto\n", argv[i]);
    }
  }

  pdicc = ini_diccionario(tamanio,orden);

  if (pdicc == NULL) {
    /* error */
    printf("Error: No se puede Iniciar el diccionario\n");
    exit(-1);
  }

  perm = genera_perm(tamanio);

  if (perm == NULL) {
    /* error */
    printf("Error: No hay memoria\n");
    libera_diccionario(pdicc);
    exit(-1);
  }

  nob = insercion_masiva_diccionario(pdicc, perm, tamanio);

  if (nob == ERR) {
    /* error */
    printf("Error: No se puede crear el diccionario memoria\n");
    free(perm);
    libera_diccionario(pdicc);
    exit(-1);
  }

  /* Imprime el diccionario */
  printf("Diccionario:\n{ ");
  for(i = 0; i < pdicc->tamanio; i++){
    printf("%d\t", pdicc->tabla[i]);
  }
  printf("}\n");

  free(perm);
  libera_diccionario(pdicc);

  return 0;
}

