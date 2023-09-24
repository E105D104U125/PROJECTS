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

#include "miner.h"

#define BUF_BLOCK 10
#define LEER 0
#define ESCRIBIR 1

/**
 * Copia toda la informacion de un bloque
 * 
 * @param origen bloque origen
 * @param destino bloque destino
 * */
void copy_block(Block* origen, Block* destino){
    int i;
    
    destino->id = origen->id;
    destino->target = origen->target;
    destino->solution = origen->solution;
    for (i = 0; i < MAX_MINERS; i++){
        destino->wallets[i] = origen->wallets[i];
    }
    destino->id = origen->id;
    destino->is_valid = origen->is_valid;
    destino->prev = origen->prev;
    destino->next = origen->next;
}

static volatile sig_atomic_t got_sigalarm = 0;
static volatile sig_atomic_t got_sigint = 0;

/* manejadorALARM : rutina de tratamiento de la señal ALARM solo para el padre . */
void manejadorSIGALARM(int sig)
{
    got_sigalarm = 1;
}

/* manejadorINT : rutina de tratamiento de la señal INT solo para el padre . */
void manejadorSIGINT(int sig)
{
    got_sigint = 1;
}

/* Libera la memoria de una cadena de bloques a partir del ultimo bloque */
void free_blocks(Block *bloque_actual){
    Block *liberar;
    if(bloque_actual){
        while(bloque_actual->prev!=NULL){
            liberar=bloque_actual;
            bloque_actual=bloque_actual->prev;
            free(liberar);
        }
        free(bloque_actual);
    }
    
}

/**
 * monitor.c
 * */
int main(int argc, char **argv){
    sem_t *sem_mutex = NULL;
    NetData *net_data = NULL;
    mqd_t queue;
    struct stat statbuf;
    struct sigaction act;
    int pipe_status, fd[2], buffer = 0, flag_found = 0, i, fd_mem, j;
    pid_t childpid;
    Block buffer_block[10], block, *bloque_inicio = NULL, *bloque_actual = NULL, *bloque_previo = NULL, *b_aux=NULL;

    /* Se inicializan los IDs del buffer */
    for(i=0; i < BUF_BLOCK; i++){
        buffer_block[i].id =-1;
    }

    /* Modificamos el manejador de SIGINT */
    act.sa_handler = manejadorSIGINT;
    sigemptyset(&(act.sa_mask));
    act.sa_flags = 0;
    if (sigaction(SIGINT, &act, NULL) < 0)
    {
        perror("sigaction");
        exit(EXIT_FAILURE);
    }

    /* Se crea la tubería antes de crear el proceso hijo */
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
        close(fd[LEER]);
        close(fd[ESCRIBIR]);
		exit(EXIT_FAILURE);
	}
    if(childpid == 0){
        /*  Cierre  del  descriptor  de  escritura  en el hijo */
        close(fd[ESCRIBIR]);
        FILE *f = NULL;

        /* Modificamos el manejador de SIGALARM */
        act.sa_handler = manejadorSIGALARM;
        sigemptyset(&(act.sa_mask));
        act.sa_flags = 0;
        if (sigaction(SIGALRM, &act, NULL) < 0)
        {
            perror("sigaction");
            close(fd[LEER]);
            exit(EXIT_FAILURE);
        }

        // Tareas del hijo
        while(1){
            got_sigalarm = 0;
            alarm(5);
            
            while(!got_sigalarm){
                bloque_actual = (Block*) malloc(sizeof(Block));
                if(!bloque_actual) {
                    perror("malloc");
                    close(fd[LEER]);
                    free_blocks(bloque_previo);
                    exit(EXIT_FAILURE);
                }

                // Lee el nuevo bloque
                if (read(fd[LEER], bloque_actual, sizeof(Block)) == -1) {
                    /* Si la alarma se carga el read */
                    if(got_sigalarm){
                        free(bloque_actual);
                        bloque_actual = bloque_previo;
                        break;
                    }
                    perror("read");
                    close(fd[LEER]);
                    free(bloque_actual);
                    free_blocks(bloque_previo);
                    exit(EXIT_FAILURE);
                }
                bloque_actual->prev = bloque_previo;
                // Posicion en la lista enlazada
                bloque_previo = bloque_actual;
                //Imprimir 

                if(got_sigint == 1){
                    break;
                }
                bloque_actual = bloque_actual->next;
            }

            // En caso de terminar, no realiza una ultima impresion
            if(got_sigint == 1){
                break;
            }
            
            //Imprimir en un fichero
            f = fopen("red.log", "w");
            if(!f){
                perror("fopen");
                break;
            }
            for(b_aux = bloque_actual; b_aux != NULL; b_aux = b_aux->prev) {
                fprintf(f, "Block number: %d; Target: %ld;    Solution: %ld\n", b_aux->id, b_aux->target, b_aux->solution);
                for(j = 0; j < MAX_MINERS; j++) {
                    if (bloque_actual->wallets[j] != 0)
                    fprintf(f, "%d: %d;  ", j, b_aux->wallets[j]);
                }
                fprintf(f, "\n\n\n");
            }
            fclose(f);
        }

        free_blocks(bloque_actual);
        close(fd[LEER]);
        exit(EXIT_SUCCESS);
    }

    /*  Cierre  del  descriptor  de  lectura  en el padre  */
    close(fd[LEER]);

    if ((sem_mutex = sem_open(SEM_NAME2, O_CREAT, S_IRUSR | S_IWUSR, 1)) == SEM_FAILED) {
        perror("sem_open");
        close(fd[ESCRIBIR]);
        kill(childpid, SIGINT);
        wait(NULL);
        exit(EXIT_FAILURE);
    }

    /* El monitor abre la memoria del net_data */
    /* Los mineros se encargan de abrirla y si no esta abierta */
    if ((fd_mem = shm_open(SHM_NAME_NET, O_RDWR,  S_IRUSR | S_IWUSR)) == -1) {
        fprintf(stderr, "No existe una red activa\n");
        sem_close(sem_mutex);
        close(fd[ESCRIBIR]);
        kill(childpid, SIGINT);
        wait(NULL);
        exit(EXIT_FAILURE);
    }
    if (fstat(fd_mem, &statbuf) == -1){
        perror("fstat");
        sem_close(sem_mutex);
        close(fd[ESCRIBIR]);
        kill(childpid, SIGINT);
        wait(NULL);
        exit(EXIT_FAILURE);
    }
    if(statbuf.st_size != sizeof(NetData)){
        fprintf(stderr, "Tamaño de memoria compartida incorrecto.\n");
        sem_close(sem_mutex);
        close(fd[1]);
        kill(childpid, SIGINT);
        wait(NULL);
        exit(EXIT_FAILURE);
    }

    /* Mapeo del segmento de memoria*/
    net_data = mmap(NULL, sizeof(NetData), PROT_WRITE | PROT_READ, MAP_SHARED, fd_mem, 0);
    close(fd_mem);
    if (net_data == MAP_FAILED) {
        perror("mmap");
        sem_close(sem_mutex);
        close(fd[1]);
        kill(childpid, SIGINT);
        wait(NULL);
        exit(EXIT_FAILURE);
    }

    /* Iniciamos los atributos de la cola a crear */
    struct mq_attr attributes = {
        .mq_flags = 0,
        .mq_maxmsg = 10,
        .mq_curmsgs = 0,
        .mq_msgsize = sizeof(Block)
    };    

    if ((queue = mq_open(MQ, O_RDONLY , S_IRUSR | S_IWUSR, &attributes)) == (mqd_t)-1) {
        perror("mq_open");
        sem_close(sem_mutex);
        munmap(net_data, sizeof(NetData));
        close(fd[ESCRIBIR]);
        kill(childpid, SIGINT);
        wait(NULL);
        exit(EXIT_FAILURE);
    }

    sem_wait(sem_mutex);
    if (net_data->monitor_pid != -1){
        fprintf(stderr, "Ya existe un monitor. ");
        sem_close(sem_mutex);
        munmap(net_data, sizeof(NetData));
        close(fd[1]);
        kill(childpid, SIGINT);
        wait(NULL);
        exit(EXIT_FAILURE);
    }
    net_data->monitor_pid = getpid();
    sem_post(sem_mutex);

    while (1){
        if (mq_receive(queue , (char *) &block, sizeof(Block), NULL ) == -1) {
            perror("mq_receive");  
            break;
        }

        for(i = 0, flag_found = 0; i < BUF_BLOCK; i++){
            if (buffer_block[i].id == block.id){
                if(buffer_block[i].target == block.target 
                    && buffer_block[i].solution == block.solution
                    && block.is_valid == 1){
                    fprintf(stdout, "Verified block %d with solution %ld for target %ld\n",
                    block.id, block.solution, block.target);
                    fflush(stdout);
                    break;
                }
                else{
                    fprintf(stdout, "Error in block %d with solution %ld for target %ld\n",
                    block.id, block.solution, block.target);
                    fflush(stdout);
                    break;
                }
                flag_found = 1;
            }
        }

        if(flag_found == 0){
            copy_block(&block, &buffer_block[buffer]);
            buffer = (buffer + 1) % BUF_BLOCK;

            // No es necesario mutex porque solo hay un slot y dos procesos
            /* Escribimos en la tubería el nuevo bloque para que el hijo lo reciva */
            if (write(fd[ESCRIBIR], &block, sizeof(Block)) == -1)
            {
                perror("write"); 
                break;
            }
        }

        if(got_sigint){
            kill(childpid, SIGINT);
            break;
        }
    }
    
    /* Ponemos el campo monitor a -1 en Net_data */
    sem_wait(sem_mutex);
    net_data->monitor_pid = -1;
    sem_post(sem_mutex);

    /* Esperamos al hijo y liberamos toda la memoria */
    mq_close(queue);
    close(fd[1]);
    sem_close(sem_mutex);
    munmap(net_data, sizeof(NetData));
    sem_unlink(SEM_NAME2);
    wait(NULL);
    exit(EXIT_SUCCESS);
}