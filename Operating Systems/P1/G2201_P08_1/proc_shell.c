/**
 * Practica 1 SOPER
 * Autores: grupo 8
 * 
 *      Gregorio Blázquez Martínez
 *      Eduardo Terrés Caballero
 * 
 * Ejecutar y compilar con make shell
 * - incluye compilado del programa abort -.
 * */

#include <stdio.h>
#include <stdlib.h>
#include <sys/wait.h>
#include <sys/types.h>
#include <pthread.h>
#include <unistd.h>
#include <errno.h>
#include <string.h>
#include <fcntl.h>

#define BUF_MAX 1024
#define S_BUF 200
#define COM_MAX 32
#define LEER 0
#define ESCRIBIR 1

/* Fichero registro donde guardar los comandos y su resultado */
#define HijoLog "log.txt"

/**
 * Estructura de linea de comando
 * */
typedef struct
{
	char buf[BUF_MAX];
	char *comando[COM_MAX];
} lineaComando;

/** 
 * Autores: Grupo 8
 * 
 * Esta función recive un puntero a estructura lineaComando
 * donde está la línea a descomponer y el array donde se guarda
 * el resultado de la línea descompuesta en palabras 
 * 
 * @param arg Entrada de texto del usuario
 * @param
 * */
void *command_prepare(void *arg)
{
	lineaComando *lc;
	const char s = ' ';
	int i = 1;

	if (arg == NULL)
		return NULL;
	lc = arg;

	/* Tomamos el primer fragmento */
	lc->comando[0] = strtok(lc->buf, &s);

	while ((lc->comando[i] = strtok(NULL, &s)) != NULL && i < COM_MAX)
	{
		i++;
	}

	return NULL;
}

/** 
 * Autores: Grupo 8
 * 
 * Convierte un entero en cadena para imprimir 
 * la señal de finalización de un poceso *
 * 
 * @param entero Entero a convertir
 * @param cadena Cadena de destino
 * @param
 * */
void enteroACadena(int entero, char *cadena)
{
	int i = 0;
	char cadenaAux[S_BUF];

	while ((entero > 0 && i < S_BUF) || i == 0)
	{
		cadenaAux[i] = '0' + entero % 10;
		entero = entero / 10;
		i++;
	}
	cadenaAux[i] = 0;
	for (i = 0; i < strlen(cadenaAux); i++)
	{
		cadena[i] = cadenaAux[strlen(cadenaAux) - 1 - i];
	}
	cadena[i] = 0;
}

/**
 * Autores: grupo 8
 * 
 * Ejecución de la shell - función principal
 * */
int main(void)
{
	/* Estructura para guardar la información del comando a ejecutar */
	lineaComando lc;
	lc.buf[0] = 0;
	lc.comando[0] = NULL;

	/* Para la ejecución de los comandos */
	pthread_t h1;
	int error;
	pid_t pid;
	int i = 0;
	char terminacion[S_BUF], cadena[BUF_MAX];
	int wstatus;

	/* Para la tubería entre el Hijo log y el proceso padre */
	int fd[2];
	char readbuffer[80], comando[S_BUF];
	int pipe_status;
	pid_t childpid;
	ssize_t nbytes;
	int log;

	/* Creamos la tubería antes de crear el proceso hijo */
	pipe_status = pipe(fd);
	if (pipe_status == -1)
	{
		perror("pipe");
		exit(EXIT_FAILURE);
	}

	/* Creamos el proceso hijo que se comunicará con 
	el padre a través de la tubería */
	childpid = fork();
	if (childpid == -1)
	{
		perror("fork");
		exit(EXIT_FAILURE);
	}
	/* El Hijo log lee de la tuberia el resultado de la ejecucion
	y lo escribe en el archivo log.txt */
	else if (childpid == 0)
	{
		/* Creamos el "registro"/fichero log */
		if ((log = open(HijoLog, O_CREAT | O_TRUNC | O_WRONLY, S_IRUSR | S_IWUSR | S_IRGRP | S_IWGRP)) == -1)
		{
			perror("open");
			exit(EXIT_FAILURE);
		}
		/* Cierre del descriptor a la tubería en modo escritura */
		close(fd[ESCRIBIR]);
		/* Lectura de la tubería */
		nbytes = 0;
		do
		{
			nbytes = read(fd[LEER], readbuffer, sizeof(readbuffer));
			if (nbytes == -1)
			{
				perror("read");
				exit(EXIT_FAILURE);
			}
			if (nbytes > 0)
			{
				/* Escritura en el fichero log.txt */
				dprintf(log, "%s", readbuffer);
			}
		} while (nbytes != 0);
		close(log);
		close(fd[LEER]);
		exit(EXIT_SUCCESS);
	}
	/* El padre lee la entrada de la shell, crea los procesos que ejecutan los comandos
	y tras esperarlos envía por la tubería la información al Hijo log */
	else
	{
		/* Cierre del descriptor de lectura */
		close(fd[LEER]);
		/* Bucle principal donde leemos las intrucciones y las ejecutamos */
		while (1)
		{
			i = 0;
			/* Leemos la linea y le quitamos el fin de linea */
			fgets(lc.buf, BUF_MAX, stdin);
			strcpy(comando, lc.buf);
			lc.buf[strlen(lc.buf) - 1] = '\0';

			/* Comprobamos si ha llegado EOF y si es así cerramos la tubería para que acabe
			el hijo conectado a la tubería, lo esperamos y salimos del programa principal */
			if (feof(stdin))
			{
				/* Cerramos el descriptor de escritura, cuando el proceso
				hijo trate de leer en la tubería al no haber ningún descriptor
				de escritura abierto dejará de leer y terminará el proceso */
				close(fd[ESCRIBIR]);
				/* Esperamos al Hijo Log*/
				wait(NULL);
				break;
			}

			/* Creamos el hilo que separa el comando en sus argumentos */
			error = pthread_create(&h1, NULL, command_prepare, &lc);
			if (error != 0)
			{
				fprintf(stderr, "pthread_create: %s\n", strerror(error));
				exit(EXIT_FAILURE);
			}
			/* Esperamos a que el hilo acabe para poder ejecutar el comando */
			error = pthread_join(h1, NULL);
			if (error != 0)
			{
				fprintf(stderr, "pthread_join: %s\n", strerror(error));
				exit(EXIT_FAILURE);
			}

			/* Imprimimos el comando */
			/*while(lc.comando[i]!=NULL){
				printf("%s ",lc.comando[i]);
				i++;
			}
			printf("\n");*/

			/* Creamos el proceso que ejecutará el comando */
			pid = fork();
			if (pid < 0)
			{
				perror("fork");
				exit(EXIT_FAILURE);
			}
			else if (pid == 0)
			{
				/* El hijo ejecuta el comando */
				if (execvp(lc.comando[0], lc.comando))
				{
					perror("execvp");
					exit(EXIT_FAILURE);
				}
			}
			else
			{
				/* El padre espera a que este acabe de ejecutar el comando y luego
				envía el resultado de la ejecución a Hijo log a través de la tubería */
				wait(&wstatus);

				/* Preparamos una cadena con los valores de salida del proceso */
				cadena[0] = 0;
				terminacion[0] = 0;
				if (WIFSIGNALED(wstatus))
				{
					enteroACadena(WTERMSIG(wstatus), terminacion);
					sprintf(cadena, "%sTerminated by signal %s\n", comando, terminacion);
				}
				else if (WIFEXITED(wstatus))
				{
					enteroACadena(WEXITSTATUS(wstatus), terminacion);
					sprintf(cadena, "%sExited with value %s\n", comando, terminacion);
				}

				/* Escribimos en la tubería la salida del proceso */
				nbytes = write(fd[ESCRIBIR], cadena, strlen(cadena) + 1);
				if (nbytes == -1)
				{
					perror("write");
					exit(EXIT_FAILURE);
				}
			}
		}
		/* Cerramos el descriptor de escritura, cuando el proceso
		hijo trate de leer en la tubería al no haber ningún descriptor
		de escritura abierto dejará de leer y terminará el proceso */
		close(fd[ESCRIBIR]);
		/* Esperamos al Hijo Log*/
		wait(NULL);
	}
}