/**
 * Proyecto Final SOPER
 * Autores: Grupo 8
 * 
 *      Gregorio Blázquez Martínez
 *      Eduardo Terrés Caballero
 * 
 * */

#include <stdio.h>
#include <stdlib.h>
#include <pthread.h>
#include <errno.h>
#include <string.h>
#include <fcntl.h>
#include <sys/mman.h>
#include <sys/stat.h>
#include <sys/types.h>
#include <unistd.h>
#include <semaphore.h>
#include <sys/wait.h>
#include <mqueue.h>

#define SHM_NAME_NET "/netdata"
#define SHM_NAME_BLOCK "/block"
#define MQ "/mq"
#define SEM_NAME1 "/sem1"
#define SEM_NAME2 "/sem2"

/* Este programa sirve para si se cierra de mala manera el programa no
   dejandole desligar los semaforos y la memoria compartida pueda eliminarse
   la referencia de sus nombres de manera rapida. */
int main(){
    sem_unlink(SEM_NAME1);
    sem_unlink(SEM_NAME2); 
    shm_unlink(SHM_NAME_NET);
    shm_unlink(SHM_NAME_BLOCK); 
    mq_unlink(MQ); //Mejorar ???
    exit(1);
}
