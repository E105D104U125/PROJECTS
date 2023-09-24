#include <fcntl.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <sys/mman.h>
#include <sys/stat.h>
#include <sys/types.h>
#include <unistd.h>
#include <semaphore.h>
#include <errno.h>
#include <time.h>
#include <mqueue.h>

#define SHM_NAME "/sharedmem"
#define MQ_CLIENT "/mqclient"
#define TAM_BUF 5
#define TAM_MSG 64
#define MSG_MAX 10

/* Estructura que almacena la informacion en la
 * memoria compartida */
typedef struct {
    char buf[TAM_BUF];
    int post_pos, get_pos;
    sem_t sem_mutex, sem_fill, sem_empty;
}StructMemoria;

/**
 * stream-client
 * */
int main(int argc, char **argv){
    int fd_shm, ctrl = 0, err, end = 0;    
    char aux;
    FILE *pf = NULL;
    StructMemoria *shm_struct;
    struct timespec abs_time;
    mqd_t client_queue;
    char msg[TAM_MSG];
    
    if(argc != 2){
        fprintf(stderr, "stream-client: Numero de parámetros incorrecto.\n");
        exit(EXIT_FAILURE);
    }

    /*
     * Se abre el fichero con fopen para poder trabajar
     * en ASCII
     */
    pf = fopen(argv[1], "w");
    if (!pf){
        perror("fopen");
        exit(EXIT_FAILURE);
    }

    /* Obtiene la referencia a la memoria compartida*/
    if ((fd_shm = shm_open(SHM_NAME, O_RDWR, 0)) == -1) {
        perror("shm_open");
        fclose(pf);
        exit(EXIT_FAILURE);
    }

    /* Mapeo del segmento de memoria */
    shm_struct = mmap(NULL, sizeof(StructMemoria), PROT_READ | PROT_WRITE, MAP_SHARED, fd_shm, 0);
    close(fd_shm);
    if (shm_struct == MAP_FAILED) {
        perror("mmap");
        fclose(pf);
        exit(EXIT_FAILURE);
    }

    struct mq_attr attributes = {
        .mq_flags = 0,
        .mq_maxmsg = MSG_MAX,
        .mq_curmsgs = 0,
        .mq_msgsize = sizeof(msg)
    };

    if ((client_queue = mq_open(MQ_CLIENT,   O_CREAT | O_RDONLY, S_IRUSR | S_IWUSR, &attributes)) == (mqd_t)-1) {
        perror("mq_open");
        fclose(pf);
        exit(EXIT_FAILURE);
    }

    while(1){        
        /* Espera de mensajes */
        if (mq_receive(client_queue , (char*)msg , sizeof(msg), NULL ) == -1) {
            perror("mq_receive");          
            fclose(pf);
            mq_close(client_queue);
            exit(EXIT_FAILURE);
        }

        if (strcmp(msg, "get") == 0 && end == 0){
            /* Espera en caso de buffer vacio */
            if (clock_gettime(CLOCK_REALTIME, &abs_time) == -1){
                munmap(shm_struct, sizeof(StructMemoria));
                fclose(pf);
                mq_close(client_queue);
                exit(EXIT_FAILURE);
            }
            abs_time.tv_sec += 2;
            ctrl = sem_timedwait(&shm_struct->sem_fill, &abs_time);
            err = errno;
            if (ctrl == -1){
                if (err == ETIMEDOUT){
                    fprintf(stdout,"\nTiempo de espera máximo superado.\n");
                    fflush(stdout);
                }
                continue;
            }

            /* Zona critica */
            sem_wait(&shm_struct->sem_mutex);
            aux = shm_struct->buf[shm_struct->get_pos];
            shm_struct->get_pos = (shm_struct->get_pos + 1) % TAM_BUF;
            sem_post(&shm_struct->sem_mutex);

            /* Hay un hueco mas en el buffer */
            sem_post(&shm_struct->sem_empty);

            /* Fin de fichero */
            if (aux == '\0') end = 1;
            /* Impresion en el fichero de salida */
            fprintf(pf, "%c",aux);
        } 
        else if(strcmp(msg, "exit") == 0){
            break;
        }
    }

    munmap(shm_struct, sizeof(StructMemoria));
    fclose(pf);
    mq_close(client_queue);

    exit(EXIT_SUCCESS);
}