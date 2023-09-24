#include <stdio.h>
#include <stdlib.h>
#include <errno.h>
#include <string.h>
#include <time.h>
#include <unistd.h>

#define SLP 1
/**
 * Autores: grupo 8
 * 
 * El programa imprime una serie de frases por pantalla
 * para finalmente ser abortado.
 * */
int main(void){
	printf("Soy un programa que imprime letras.\n A");
	fflush(stdout);
	sleep(SLP);
	printf("\tB");
	fflush(stdout);
	sleep(SLP);
	printf("\tC");
	fflush(stdout);
	sleep(SLP);
	printf("\tD");
	fflush(stdout);
	sleep(SLP);
	fprintf(stderr, "\nNo me encuentro muy bien...\n");
	fprintf(stderr, "abortando...\n");
	abort();

	return 0;
}
