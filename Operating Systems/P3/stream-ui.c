/**
 * 2201_08
 * @author Gregorio Blázquez Martínez
 * @author Eduardo Terrés Caballero
 *
 * Ejercicio 10 - Práctica 3
 * stream-ui.c 
 * */

#include <fcntl.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <sys/mman.h>
#include <sys/stat.h>
#include <sys/types.h>
#include <unistd.h>
#include <semaphore.h>
#include <sys/wait.h>
#include <mqueue.h>

#define SHM_NAME "/sharedmem"
#define MQ_SERVER "/mqserver"
#define MQ_CLIENT "/mqclient"
#define TAM_BUF 5
#define TAM_MSG 64

/* Estructura que almacena la informacion en la
 * memoria compartida */
typedef struct {
    char buf[TAM_BUF];
    int post_pos, get_pos;
    sem_t sem_mutex, sem_fill, sem_empty;
}StructMemoria;

void queue_free(mqd_t client_queue, mqd_t server_queue){
    mq_close(client_queue);
    mq_close(server_queue);
    mq_unlink(MQ_SERVER);
    mq_unlink(MQ_CLIENT);
}

void full_memory_free(sem_t *sem_mutex, sem_t *sem_empty, sem_t *sem_fill){
/* Elimina el nombre de la memoria compartida
 * como los procesos hijos ya terminaron, se eliminara la 
 * memoria tras esta llamada */
    shm_unlink(SHM_NAME);
    if (sem_mutex != NULL) sem_destroy(sem_mutex);
    if (sem_mutex != NULL) sem_destroy(sem_empty);
    if (sem_mutex != NULL) sem_destroy(sem_fill);
}

int main(int argc, char **argv){
    int fd_shm, cmp = 1;
    StructMemoria *mem;
    sem_t sem_mutex, sem_empty, sem_fill;
    char *args_server[3] = {"stream-server","",NULL};
    char *args_client[3] = {"stream-client","",NULL};
    char msg[TAM_MSG];
    mqd_t server_queue, client_queue;

    if (argc != 3){
        fprintf(stderr, "[stream-ui] Uso: ./stream-ui <fichero_lectura> <fichero_escritura>\n");
        exit(EXIT_FAILURE);
    }
        shm_unlink(SHM_NAME);

    args_server[1] = argv[1];
    args_client[1] = argv[2];

    /*if ((fd_shm = shm_open(SHM_NAME, O_RDWR | O_CREAT | O_EXCL,  S_IRUSR | S_IWUSR)) == -1) {
        shm_unlink(SHM_NAME);
    }*/
    
    /* Crea el segemento de memoria */
    if ((fd_shm = shm_open(SHM_NAME, O_RDWR | O_CREAT | O_EXCL,  S_IRUSR | S_IWUSR)) == -1) {
        perror("shm_open");
        exit(EXIT_FAILURE);
    }

    /* Ajusta el tamanio del segmento al de la estructura creada */
    if (ftruncate(fd_shm, sizeof(StructMemoria)) == -1) {
        perror("ftruncate");
        shm_unlink(SHM_NAME);
        exit(EXIT_FAILURE);
    }

    /* Mapeo del segmento de memoria*/
    mem = mmap(NULL, sizeof(StructMemoria), PROT_WRITE | PROT_READ, MAP_SHARED, fd_shm, 0);
    close(fd_shm);
    if (mem == MAP_FAILED) {
        perror("mmap");
        exit(EXIT_FAILURE);
    }
    printf("Pointer UI: %p\n", (void*)mem);

    /* Se crean los semaforos */
    if ((sem_init(&mem->sem_mutex,1,1)) == -1)
    {
        perror("sem_init");        
        shm_unlink(SHM_NAME);
        exit(EXIT_FAILURE);
    }

    if ((sem_init(&mem->sem_fill,1,0)) == -1)
    {
        perror("sem_init");
        full_memory_free(&sem_mutex, NULL, NULL);
        exit(EXIT_FAILURE);
    }

    if ((sem_init(&mem->sem_empty,1,TAM_BUF)) == -1)
    {
        perror("sem_init");
        full_memory_free(&sem_mutex, &sem_fill, NULL);
        exit(EXIT_FAILURE);
    }

    /* Se introducen los semaforos y los indices inicializados
     * en la memoria compartida */
    mem->post_pos = mem->get_pos = 0;

    munmap(mem, sizeof(StructMemoria));

    /* Iniciamos los atributos de las dos colas a crear */
    struct mq_attr attributes = {
        .mq_flags = 0,
        .mq_maxmsg = 10,
        .mq_curmsgs = 0,
        .mq_msgsize = sizeof(msg)
    };

    if ((server_queue = mq_open(MQ_SERVER, O_CREAT | O_WRONLY | O_NONBLOCK, S_IRUSR | S_IWUSR, &attributes)) == (mqd_t)-1) {
        perror("mq_open");
        full_memory_free(&sem_mutex, &sem_empty, &sem_fill);
        exit(EXIT_FAILURE);
    }

    if ((client_queue = mq_open(MQ_CLIENT, O_CREAT | O_WRONLY | O_NONBLOCK, S_IRUSR | S_IWUSR, &attributes)) == (mqd_t)-1) {
        perror("mq_open");
        full_memory_free(&sem_mutex, &sem_empty, &sem_fill);
        exit(EXIT_FAILURE);
    }

    /* Lanza los procesos */
    if (!fork()){
        if(!fork()){
            if (execv("./stream-server", args_server) == -1){
                perror("execv");
                full_memory_free(&sem_mutex, &sem_empty, &sem_fill);
                queue_free(client_queue, server_queue);
                exit(EXIT_FAILURE);
            }
        } 
        else{
            if (execv("./stream-client", args_client) == -1){
                perror("execv");
                full_memory_free(&sem_mutex, &sem_empty, &sem_fill);
                queue_free(client_queue, server_queue);
                exit(EXIT_FAILURE);
            }
            exit(0);
        }
    }

    /* Lectura de comandos */
    while(cmp != 0){
        fprintf(stdout, "Introduce una instruccion: ");
        scanf("%s", msg);
        cmp = strcmp(msg, "exit");
        /* Envio del mensaje al servidor */
        if (strcmp(msg, "post") == 0){
            if (mq_send(server_queue, (char *)msg, sizeof(msg), 1) == -1){
                fprintf(stderr, "Error en el envio del mensaje\n");
                break;
            }
        }        
        /* Envio del mensaje al cliente */
        else if (strcmp(msg, "get") == 0){
            if (mq_send(client_queue, (char *)msg, sizeof(msg), 1) == -1){
                fprintf(stderr, "Error en el envio del mensaje\n");
                break;
            }
        }
        else if (cmp == 0){
            if (mq_send(server_queue, (char *)msg, sizeof(msg), 1) == -1){
                fprintf(stderr, "Error en el envio del mensaje\n");
                break;
            }
            if (mq_send(client_queue, (char *)msg, sizeof(msg), 1) == -1){
                fprintf(stderr, "Error en el envio del mensaje\n");
                break;
            }
        }
    }

    /* Espera a los hijos */
    wait(NULL);
    wait(NULL);

    full_memory_free(&sem_mutex, &sem_empty, &sem_fill);
    queue_free(client_queue, server_queue);

    exit(EXIT_SUCCESS);
}