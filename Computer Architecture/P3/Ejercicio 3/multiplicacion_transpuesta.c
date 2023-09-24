/**
	* Multiplicación transpuesta de matrices
	* Pareja: 12
	* Autores: Eduardo Terrés Caballero y Diego Vicente Miguel	
	**/
	
#include <stdio.h>
#include <stdlib.h>
#include <sys/time.h>
#include "../FicherosC/arqo3.h"

void multiplicacion_trans(tipo **A, tipo **B, int N, tipo **R)
{
	int i,j,k;
	tipo s;
	
	if (!A || !B || !R) return;
	
	for (i = 0; i < N; i++){
		for (j = 0; j < N; j++){
			s = 0;
			for (k = 0; k < N; k++){
				s += A[i][k]*B[j][k];
			}
			R[i][j] = s;
		}
	}
}


void transpose(tipo **A, int N){
	int i, j;
	
	if(!A) return;
	tipo **Aux = generateEmptyMatrix(N);
	if(!Aux) return;
	
	for (i = 0; i < N; i++){
		for (j = 0; j < N; j++){
			Aux[j][i] = A[i][j];
		}
	}
	
	for (i = 0; i < N; i++){
		for (j = 0; j < N; j++){
			A[i][j] = Aux[i][j];
		}
	}
	
	freeMatrix(Aux);
}


int main(int argc, char *argv[]){
	tipo **A = NULL, **B = NULL, **R = NULL;
	int N;
	struct timeval fin,ini;

	if( argc!=2 )
	{
		printf("Error: ./%s <matrix size>\n", argv[0]);
		return -1;
	}
	N = atoi(argv[1]);
	
	// Generación de matrices
	A = generateMatrix(N);
	B = generateMatrix(N);
	// Se transpone la matriz B
	transpose(B, N);
	R = generateEmptyMatrix(N);
	
	// Control de errores
	if( !A || !B || !R){
		if (B != NULL) freeMatrix(B);
		if (A != NULL) freeMatrix(A);
		if (R != NULL) freeMatrix(R);
		return -1;
	}
	
	/* Inicio tiempo */
	gettimeofday(&ini,NULL);
	/*Multiplicación de matrices */
	multiplicacion_trans(A, B, N, R);
	/* Fin tiempo */
	gettimeofday(&fin,NULL);
	
	/* Impresión matriz */
	/*
	for (int i = 0; i < N; i++){
		for (int j = 0; j < N; j++){
			printf("%lf ",R[i][j]);
		}
		printf("\n");
	}*/
	
	printf("Execution time: %f\n", ((fin.tv_sec*1000000+fin.tv_usec)-(ini.tv_sec*1000000+ini.tv_usec))*1.0/1000000.0);
	freeMatrix(A);
	freeMatrix(B);
	freeMatrix(R);
	return 0;
}
