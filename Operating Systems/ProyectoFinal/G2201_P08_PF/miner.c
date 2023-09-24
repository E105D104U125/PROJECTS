/**
 * Proyecto Final SOPER
 * Autores: Grupo 8
 * 
 *      Gregorio Blázquez Martínez
 *      Eduardo Terrés Caballero
 * 
 * */

#include <stdio.h>
#include <string.h>
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

//Declaraciones de funciones privadas
long int simple_hash(long int number);
void print_blocks(Block *plast_block, int num_wallets);
void* trabajador(void* args);
void free_blocks(Block *bloque_actual);
void freeAll(mqd_t queue, Block *bloque_actual, RangoBusqueda *rg, pthread_t *h);
void unmapAll(Block *mem_block, NetData *net_data);
void unlink_and_unmap(sem_t *sem_mutex1, Block *mem_block, sem_t *sem_mutex2, NetData *net_data);
void manejadorSIGINT(int sig);
void manejadorSIGUSR2(int sig);
void manejadorSIGALRM(int sig);

#define PRIME 99997669
#define BIG_X 435679812
#define BIG_Y 100001819

long int solucion = -1;
static volatile sig_atomic_t got_sigint = 0;
static volatile sig_atomic_t got_sigusr2 = 0;
static volatile sig_atomic_t got_sigarlm = 0;

long int simple_hash(long int number) {
    long int result = (number * BIG_X + BIG_Y) % PRIME;
    return result;
}

/** 
 * Autores: Grupo 8
 * 
 * Imprime en un fichero dado por el pid del proceso su cadena de bloques.
 * 
 * @param bloque_actual bloque actual a partir del cual vamos recorriendo los
 * bloques anteriores (bloque_actual->prev) como una lista enlazada.
 * */
void print_blocks(Block *plast_block, int num_wallets) {
    Block *block = NULL;
    int i, j;
    FILE *f=NULL;
    char pidNombre[50];

    sprintf(pidNombre, "%d", (int)getpid());
    strcat(pidNombre,".txt");

    f=fopen(pidNombre,"w");
    if(f==NULL) {
        fprintf(stderr, "Error al abrir el archivo para imprimir los bloques.\n");
    }


    for(i = 0, block = plast_block; block != NULL; block = block->prev, i++) {
        fprintf(f,"Block number: %d;\tTarget: %ld;\tSolution: %ld\n", block->id, block->target, block->solution);
        for(j = 0; j < num_wallets; j++) {
            fprintf(f, "%d: %d;         ", j, block->wallets[j]);
        }
        fprintf(f, "\n\n\n");
    }
    fprintf(f, "A total of %d blocks were printed", i);
    fflush(stdout);
    fclose(f);
}

/** 
 * Autores: Grupo 8
 * 
 * Busca la solucion a un target probando a fuerza bruta. Para ganar eficiencia los hilos
 * se reparten el trabajo comprobando cada uno una parte. Esto lo hacemos haciendo que cada
 * hilo pruebe los valores de un determinado resto (modulo) respecto al numero de hilos.
 * Es decir si hay tres hilos uno probara 0,3,6,9...; otro 1,4,7,... y el restante 2,5,8...
 * 
 * @param args Puntero a la estructura RangoBusqueda que contiene los parametros para 
 * gestionar la busqueda de la solucion.
 * */
void* trabajador(void* args){
    long int i;
    RangoBusqueda *rg = (RangoBusqueda*) args;

    for (i = rg->start; i < PRIME && solucion == -1; i += (long) rg->num_hilos) {
        if (rg->target == simple_hash(i)) {
            solucion = i;
            //Avisar solucion encontrada a su minero
            kill(getpid(), SIGUSR2);
            return NULL;
        }
    }
    return NULL;
}

/** 
 * Autores: Grupo 8
 * 
 * Libera la memoria de una cadena de bloques a partir del ultimo bloque
 * 
 * @param bloque_actual bloque actual a partir del cual vamos recorriendo los
 * bloques anteriores (bloque_actual->prev) como una lista enlazada.
 * */
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
 * Libera la memoria empleada. Libera todos los bloques
 * mediante una llamada a free_blocks.
 * 
 * @param sem1 semaforo
 * @param sem2 semaforo
 * @param queue cola de mensajes
 * @param bloque_actual ultimo bloque de la lista enlazada
 * @param rg estructura auxiliar
 * @param h array de pid_t se los hilos
 * */
void freeAll(mqd_t queue, Block *bloque_actual, RangoBusqueda *rg, pthread_t *h){
    if(queue){
        mq_close(queue);
    }
    if(bloque_actual){
        free_blocks(bloque_actual);
    }
    if(rg){
        free(rg);
    }
    if(h){
        free(h);
    }
}

void unmapAll(Block *mem_block, NetData *net_data){
    if (mem_block) munmap(mem_block, sizeof(Block));
    if (net_data) munmap(net_data, sizeof(NetData));
}

void unlink_and_unmap(sem_t *sem_mutex1, Block *mem_block, sem_t *sem_mutex2, NetData *net_data) {
    int unlink_semaphore = 0;
    if (sem_mutex1 != NULL && mem_block != NULL){
        sem_wait(sem_mutex1);
        // El ultimo minero libera las memoria bloques
        if (mem_block->opened_block == 1) {
            shm_unlink (SHM_NAME_BLOCK);
            unlink_semaphore = 1;
        }
        mem_block->opened_block --;
        sem_post(sem_mutex1);
    }
    if (sem_mutex1 != NULL) {
        sem_close(sem_mutex1);
        if (unlink_semaphore == 1) sem_unlink(SEM_NAME1);
    }
    unlink_semaphore = 0;
    if (sem_mutex2 != NULL && net_data != NULL) {
        sem_wait(sem_mutex2);
        // El ultimo minero libera las memoria net
        if (net_data->opened_net == 1) {
            sem_destroy(&net_data->sem_ronda);
            sem_destroy(&net_data->sem_ganador);
            sem_destroy(&net_data->sem_perdedor);
            shm_unlink (SHM_NAME_NET);
            mq_unlink(MQ);
            unlink_semaphore = 1;
        }
        net_data->opened_net --;
        sem_post(sem_mutex2);
    }
    if (sem_mutex2 != NULL){
        sem_close(sem_mutex2);
        if (unlink_semaphore == 1) sem_unlink(SEM_NAME2);
    }
    unmapAll(mem_block, net_data);
}

/* manejadorINT : rutina de tratamiento de la señal INT */
void manejadorSIGINT(int sig)
{
    got_sigint = 1;
}

/* manejadorUSR2 : rutina de tratamiento de la señal USR2 */
void manejadorSIGUSR2(int sig)
{
    got_sigusr2 = 1;
}

/* manejadorARLM : rutina de tratamiento de la señal USR2 */
void manejadorSIGALRM(int sig)
{
    got_sigarlm = 1;
}


/**
 * miner.c
 * */
int main(int argc, char *argv[]) {
    int error, i, j, num_hilos, rondas, flag_create = 0, votosFavor, votos, monitor, flag_valid = 0;
    int en_red, n_enviadas, win_round, ctrl, err, supuesta_sol, k, piratas;
    struct timespec abs_time;
    pthread_t *h=NULL;
    RangoBusqueda *rg = NULL;
    NetData *net_data=NULL;
    Block *bloque_actual=NULL, *mem_block=NULL;
    mqd_t fd_mem=0;
    mqd_t queue=0;
    sem_t *sem_mutex1=NULL, *sem_mutex2=NULL;
    sigset_t set, oset;
    struct sigaction act;

    /* Comprobacion argumentos de entrada */
    if (argc != 3) {
        fprintf(stderr, "Usage: %s <NUM_HILOS> <ROUNDS>\n", argv[0]);
        exit(EXIT_FAILURE);
    }

    num_hilos = atol(argv[1]);
    if (num_hilos <= 0){
        exit(EXIT_FAILURE);
    }
    rondas = atol(argv[2]);
    
    /* Se cambian la mascara de seniales */
    /* Bloqueamos la señal SIGINT hasta terminar la inicializacion y entrada en red */
    sigemptyset(&set);
    sigaddset(&set, SIGINT);
    if (sigprocmask(SIG_BLOCK, &set, &oset) < 0)
    {
        perror("sigprocmask");
        exit(EXIT_FAILURE);
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
    /* Modificamos el manejador de SIGUSR2 */
    act.sa_handler = manejadorSIGUSR2;
    act.sa_flags = 0;
    if (sigaction(SIGUSR2, &act, NULL) < 0)
    {
        perror("sigaction");
        exit(EXIT_FAILURE);
    }
    /* Modificamos el manejador de SIGUSR1 */
    act.sa_handler = SIG_IGN;
    if (sigaction(SIGUSR1, &act, NULL) < 0)
    {
        perror("sigaction");
        exit(EXIT_FAILURE);
    }
    /* Modificamos el manejador de SIGALARM */
    act.sa_handler = manejadorSIGALRM;
    if (sigaction(SIGALRM, &act, NULL) < 0)
    {
        perror("sigaction");
        exit(EXIT_FAILURE);
    }
    
    /* Creamos el semaforo que gestionara los accesos a stdout - impresion */
    if ((sem_mutex1 = sem_open(SEM_NAME1, O_CREAT, S_IRUSR | S_IWUSR, 1)) == SEM_FAILED)
    {
        perror("sem_open");
        exit(EXIT_FAILURE);
    }

    /* Creamos el semaforo que gestionara la memoria compartida de la red */
    if ((sem_mutex2 = sem_open(SEM_NAME2, O_CREAT, S_IRUSR | S_IWUSR, 1)) == SEM_FAILED)
    {
        perror("sem_open");
        // Se trata de un error grave, la red no se recuperaria
        sem_close(sem_mutex1);
        sem_unlink(SEM_NAME1);
        exit(EXIT_FAILURE);
    }

    /* Iniciamos los atributos de la cola a crear */
    struct mq_attr attributes = {
        .mq_flags = 0,
        .mq_maxmsg = 10,
        .mq_curmsgs = 0,
        .mq_msgsize = sizeof(Block)
    };    

    /* Abrimos la cola de mensajes con el monitor */
    if ((queue = mq_open(MQ, O_CREAT | O_WRONLY | O_NONBLOCK, S_IRUSR | S_IWUSR, &attributes)) == (mqd_t)-1) {
        perror("mq_open");
        fprintf(stderr, "Ha fallado la apertura de la cola de mensajes, se continuará sin posibilidad de conectar un monitor.\n");
    }

    /* El primer minero crea la memoria compartida para el Bloque actual */
    if ((fd_mem = shm_open(SHM_NAME_BLOCK, O_RDWR | O_CREAT | O_EXCL,  S_IRUSR | S_IWUSR)) == -1) {
        if ((fd_mem = shm_open(SHM_NAME_BLOCK, O_RDWR | O_CREAT,  S_IRUSR | S_IWUSR)) == -1) {
            perror("shm_open");
            unlink_and_unmap(sem_mutex1, NULL, sem_mutex2, NULL);
            freeAll(queue,NULL,NULL,NULL);
            exit(EXIT_FAILURE);
        }
    } else{
        /* Ajusta el tamanio del segmento al de la estructura creada */
        if (ftruncate(fd_mem, sizeof(Block)) == -1) {
            perror("ftruncate");
            unlink_and_unmap(sem_mutex1, NULL, sem_mutex2, NULL);
            freeAll(queue,NULL,NULL,NULL);
            exit(EXIT_FAILURE);
        }
        flag_create = 1;
    }

    /* Mapeo del segmento de memoria*/
    mem_block = mmap(NULL, sizeof(Block), PROT_WRITE | PROT_READ, MAP_SHARED, fd_mem, 0);
    close(fd_mem);
    if (mem_block == MAP_FAILED) {
        perror("mmap");
        unlink_and_unmap(sem_mutex1, NULL, sem_mutex2, NULL);
        freeAll(queue,NULL,NULL,NULL);
        exit(EXIT_FAILURE);
    }

    sem_wait(sem_mutex1);
    /* Inicializa el primer bloque */
    if (flag_create == 1){
        mem_block->is_valid = 0;
        mem_block->id = 0;
        mem_block->target = rand() % PRIME;
        mem_block->solution = -1;
        mem_block->prev = NULL;
        mem_block->next = NULL;
        mem_block->opened_block = 0;
        for(i=0; i<MAX_MINERS; i++){
            mem_block->wallets[i] = 0;
        }
    }
    mem_block->opened_block ++;
    sem_post(sem_mutex1);
    flag_create = 0;

    /* El primer minero crea la memoria compartida para Net SData */
    if ((fd_mem = shm_open(SHM_NAME_NET, O_RDWR | O_CREAT | O_EXCL,  S_IRUSR | S_IWUSR)) == -1) {
        if ((fd_mem = shm_open(SHM_NAME_NET, O_RDWR | O_CREAT,  S_IRUSR | S_IWUSR)) == -1) {
            perror("shm_open");
            unlink_and_unmap(sem_mutex1, mem_block, sem_mutex2, NULL);
            freeAll(queue,NULL,NULL,NULL);
            exit(EXIT_FAILURE);
        }
    } else{
        /* Ajusta el tamanio del segmento al de la estructura creada */
        if (ftruncate(fd_mem, sizeof(NetData)) == -1) {
            perror("ftruncate");
            unlink_and_unmap(sem_mutex1, mem_block, sem_mutex2, NULL);
            freeAll(queue,NULL,NULL,NULL);
            exit(EXIT_FAILURE);
        }
        flag_create = 1;
    }

    /* Mapeo del segmento de memoria*/
    net_data = mmap(NULL, sizeof(NetData), PROT_WRITE | PROT_READ, MAP_SHARED, fd_mem, 0);
    close(fd_mem);
    if (net_data == MAP_FAILED) {
        perror("mmap");
        unlink_and_unmap(sem_mutex1, mem_block, sem_mutex2, NULL);
        freeAll(queue,NULL,NULL,NULL);
        exit(EXIT_FAILURE);
    }

    sem_wait(sem_mutex2);
    // Inicializa la memoria de la red
    if(flag_create == 1){
        for (i = 0; i < MAX_MINERS; i++){
            net_data->miners_pid[i] = -1;
        }
        net_data->last_winner = -1;
        net_data->monitor_pid = -1;
        net_data->last_miner = 0;
        net_data->total_miners = 0;
        net_data->total_miners_aux = 0;
        net_data->opened_net = 0;

        // Inicializa el semaforo de la memoria
        if (sem_init(&net_data->sem_ronda,1,0) == -1 ||
            sem_init(&net_data->sem_ganador,1,0) == -1 ||
            sem_init(&net_data->sem_perdedor,1,0) == -1){
            perror("sem_init");
            sem_post(sem_mutex2);
            unlink_and_unmap(sem_mutex1, mem_block, sem_mutex2, NULL);
            freeAll(queue,NULL,NULL,NULL);
            exit(EXIT_FAILURE);
        }
    }
    net_data->opened_net ++;
    sem_post(sem_mutex2);

    /* Una vez tenemos la memoria compartida preparada, 
     * se preparan los hilos */
    //Array pid_t de los hilos 
    h = (pthread_t*) malloc(num_hilos*sizeof(pthread_t));
    if(!h){
        perror("malloc");
        unlink_and_unmap(sem_mutex1, mem_block, sem_mutex2, net_data);
        freeAll(queue,NULL,NULL,NULL);
        exit(EXIT_FAILURE);
    }

    // Estructura interna llamada a trabajador
    rg = (RangoBusqueda*) malloc(num_hilos*sizeof(RangoBusqueda));
    if(!h){
        perror("malloc");
        free(h);
        unlink_and_unmap(sem_mutex1, mem_block, sem_mutex2, net_data);
        freeAll(queue,NULL,NULL,h);
        exit(EXIT_FAILURE);
    }

    /* Se bloquea SIGUSR2 */
    sigemptyset(&set);
    sigaddset(&set, SIGUSR2);
    sigaddset(&set, SIGINT);
    if (sigprocmask(SIG_SETMASK, &set, &oset) < 0)
    {
        perror("sigprocmask");
        unlink_and_unmap(sem_mutex1, mem_block, sem_mutex2, net_data);
        freeAll(queue,NULL,rg,h);
        exit(EXIT_FAILURE);
    }

    // Notificas que entras a la red
    sem_wait(sem_mutex2);
    net_data->total_miners ++;
    sem_post(sem_mutex2);

    /**
     * RONDAS DE MINADO
     * */
    for(j = 0, en_red = 0; (j < rondas || rondas <= 0) ; j++){
        // Espera hasta el comienzo de la nueva ronda
        // Caso del primer minero de la red

        sem_wait(sem_mutex2);
        if(net_data->total_miners == 1 && !en_red){
            if(sem_post(&net_data->sem_ronda) == -1){
                perror("sem_post");
                sem_post(sem_mutex2);
                break;
            }
        }
        sem_post(sem_mutex2);

        /* 
         * Espera para la nueva ronda de minado 
         * Permite no hacer suposiciones sobre la duracion maxima de 
         * minado de las rondas. Cada cierto tiempo el minero comprobara 
         * que no se ha quedado solo esperando
         * */
        ctrl = -1; err = ETIMEDOUT;
        while (ctrl == -1 && err == ETIMEDOUT) {
            clock_gettime(CLOCK_REALTIME, &abs_time);
            abs_time.tv_sec += 4;
            if ((ctrl = sem_timedwait(&net_data->sem_ronda, &abs_time)) == -1){
                err = errno;
            }
            // El minero se ha quedado solo, por lo que puede avanzar
            sem_wait(sem_mutex2);
            if (net_data->total_miners == 1){
                sem_post(sem_mutex2);
                break;
            }
            sem_post(sem_mutex2);
        }
        /* En caso de recibir SIGINT durante la creacion de la memoria, o durante
           el  */
        sigpending(&set);
        if (sigismember(&set,SIGINT)) {
            break;
        }

        // La primera ronda deben introducirse en la red
        if (j == 0 && !en_red){
            sem_wait(sem_mutex2);
            if(net_data->last_miner + 1 > MAX_MINERS){
                fprintf(stderr, "Exceso Mineros");
                break;
            }
            net_data->miners_pid[net_data->last_miner] = getpid();
            net_data->last_miner ++;
            sem_post(sem_mutex2);
            en_red = 1;
        }

        //Creamos el bloque de la ronda actual
        if(!bloque_actual){
            bloque_actual = (Block*) malloc(sizeof(Block));
            if(!bloque_actual){
                perror("malloc");
                break;
            }
            bloque_actual->next=NULL;
            bloque_actual->prev=NULL;
        }
        else{
            bloque_actual->next = (Block*) malloc(sizeof(Block));
            if(!bloque_actual->next){
                perror("malloc");
                break;
            }
            bloque_actual->next->prev = bloque_actual;
            bloque_actual = bloque_actual->next;
            bloque_actual->next=NULL;
        }

        //Leer bloque memoria compartida
        sem_wait(sem_mutex1);
        bloque_actual->id = mem_block->id;
        bloque_actual->target = mem_block->target;
        bloque_actual->is_valid = mem_block->is_valid;
        bloque_actual->solution = mem_block->solution;
        for(i=0; i<MAX_MINERS; i++){
            bloque_actual->wallets[i] = mem_block->wallets[i];
        }
        sem_post(sem_mutex1);

        // Crea los trabajadores
        solucion = -1;
        for(i = 0; i < num_hilos; i++){
            rg[i].start = i;
            rg[i].num_hilos = num_hilos;
            rg[i].target = bloque_actual->target;
            error = pthread_create(&h[i], NULL, trabajador, rg + i);
            if (error != 0) {
                perror("pthread_create");
                break;
            }
        }
        
        /* Todos los procesos pueden recibir SIGUSR2 */
        got_sigusr2 = 0;
        sigfillset(&set);
        sigdelset(&set, SIGUSR2);
        // sigsuspend esperando que un minero encuentre la solucion
        sigsuspend(&set);

        /* No se permite finalizar por SIGINT a ningun minero hasta acabar la ronda */
        
        // Se ha recibido SIGUSR2
        win_round = 0;
        if (solucion != -1) {
            sem_wait(sem_mutex2);
            if (net_data->last_winner == -1) {
                net_data->last_winner = getpid();
                win_round = 1;
            }
            sem_post(sem_mutex2);
        }

        
        /* Ganador */
        if (win_round) {
            // A priori no se conoce si se dieron de baja mineros
            sem_wait(sem_mutex2);
            net_data->total_miners_aux = net_data->total_miners;
            sem_post(sem_mutex2);

            // Espera a los hilos
            for (i = 0; i < num_hilos; i++){
                pthread_join(h[i], NULL);
            }
            
            bloque_actual->solution = solucion;
            
            //Pone en memoria compartida su supuesta solucion
            sem_wait(sem_mutex1);
            mem_block->solution = solucion;
            sem_post(sem_mutex1);

            fprintf(stderr, "Ganador, solucion del bloque %ld\n", solucion);

            // Notifica a los mineros perdedores para que paren la mineria
            for (i = 0, n_enviadas = 0; i < MAX_MINERS; i++){
                if (net_data->miners_pid[i] != -1 && net_data->miners_pid[i] != getpid()){
                    if (kill (net_data->miners_pid[i], SIGUSR2) != -1) {
                        n_enviadas ++;
                    }
                }
            }

            sem_wait(sem_mutex2);
            net_data->total_miners_aux = n_enviadas;
            sem_post(sem_mutex2);

            // En caso de que haya algun minero mas para votar
            if (n_enviadas != 0) {
                // Permite a los perdedores deshechar las señales sobrantes
                for (i = 0; i < n_enviadas; i++) sem_post(&net_data->sem_perdedor);

                // Sin espera
                sem_wait(&net_data->sem_ganador);

                // Quorum
                sem_wait(sem_mutex2);
                for (i = 0, n_enviadas = 0; i < MAX_MINERS; i++){
                    if (net_data->miners_pid[i] != -1 && net_data->miners_pid[i] != getpid()){
                        if (kill(net_data->miners_pid[i], SIGUSR1) == 0){
                            n_enviadas ++;
                        }
                    }
                }
                // Establece el quorum
                net_data->total_miners_aux = n_enviadas;

                // Despertar a los perdedores para el voto
                for (i = 0, k = 0; i < MAX_MINERS && k < n_enviadas; i++){
                    if (net_data->miners_pid[i] != -1 && net_data->miners_pid[i] != getpid()){
                        if (kill (net_data->miners_pid[i], SIGUSR2) != -1){
                            k++;
                        } else{
                            net_data->miners_pid[i] = -1;
                        }
                    }
                    net_data->voting_pool[i] = -1;
                }
                sem_post(sem_mutex2);

                // Espera a que voten los perdedores
                clock_gettime(CLOCK_REALTIME, &abs_time);
                abs_time.tv_sec += 5;
                if (sem_timedwait(&net_data->sem_ganador, &abs_time) == -1){
                    // Se ha corrompido la red, porque no hay votantes
                    break;
                }

                // Comprueba el resultado de la votacion
                sem_wait(sem_mutex2);
                net_data->total_miners_aux = n_enviadas;   
                for(i = 0, k = 0, ctrl = 1, piratas=0; i < MAX_MINERS && k < n_enviadas; i++) {
                    if (net_data->voting_pool[i] == 1) ctrl ++;
                    else if (net_data->voting_pool[i] == 0){
                        ctrl --;
                        /* Añadido: Para hacer la red mas segura frente a piratas, 
                        * si un minero vota en contra se le expulsa de la red */
                        kill(net_data->miners_pid[i], SIGINT);
                        piratas++;
                        fprintf(stderr, "Se ha detectado un pirata.\n");
                    }
                    if (net_data->voting_pool[i] != -1) k++;
                }
                 
                // Consigue la posicion relativa de su cartera
                for (i = 0; i < MAX_MINERS && net_data->miners_pid[i] != getpid(); i++);
                sem_post(sem_mutex2);

                // Actualiza el bloque
                sem_wait(sem_mutex1);
                if (ctrl > 0){
                    mem_block->is_valid = 1;
                    // Suma 1 a su wallet
                    mem_block->wallets[i] ++;
                    bloque_actual->is_valid=mem_block->is_valid;
                    bloque_actual->solution=mem_block->solution;
                    bloque_actual->wallets[i] = mem_block->wallets[i];
                }
                sem_post(sem_mutex1);

                // Reestablece el quorum
                sem_wait(sem_mutex2);
                net_data->total_miners_aux = n_enviadas;
                sem_post(sem_mutex2);

                // Despierta a los mineros para que copien el bloque
                for (i = 0; i < n_enviadas; i++) sem_post(&net_data->sem_perdedor);

            }
            else{
                ctrl=1;

                sem_wait(sem_mutex2);
                // Consigue la posicion relativa de su cartera
                for (i = 0; i < MAX_MINERS && net_data->miners_pid[i] != getpid(); i++);
                sem_post(sem_mutex2);

                sem_wait(sem_mutex1);
                mem_block->is_valid = 1;
                mem_block->wallets[i] ++;
                // Suma 1 a su wallet
                bloque_actual->is_valid=mem_block->is_valid;
                bloque_actual->solution=mem_block->solution;
                bloque_actual->wallets[i] = mem_block->wallets[i];
                sem_post(sem_mutex1);
            }

            //Envio del bloque actual a la cola, con prioridad 2
            sem_wait(sem_mutex1);
            monitor = net_data->monitor_pid;
            sem_post(sem_mutex1);
            if(monitor != -1){
                mq_send(queue, (char *) bloque_actual, sizeof(Block), 2);
            }
            
            // Espera a que copien el bloque anterior
            if (n_enviadas != 0) {
                clock_gettime(CLOCK_REALTIME, &abs_time);
                abs_time.tv_sec += 5;
                if (sem_timedwait(&net_data->sem_ganador, &abs_time) == -1){
                    // Se ha corrompido la red, porque no hay votantes
                    break;
                }
            }

            // Creacion del nuevo bloque
            sem_wait(sem_mutex1);
            // Si no se ha validado, se repite el mismo bloque
            if (ctrl > 0) {
                mem_block->target = bloque_actual->solution;
                mem_block->id ++;
            }
            mem_block->solution = -1;
            mem_block->is_valid = 0;
            sem_post(sem_mutex1);

            // Levanta el semaforo para la nueva ronda
            sem_wait(sem_mutex2);
            for (i = 0; i < net_data->total_miners; i++){
                sem_post(&net_data->sem_ronda);
            }
            for (i = 0; i < MAX_MINERS; i++){
                net_data->voting_pool[i] = -1;
            }
            net_data->last_winner = -1;
            sem_post(sem_mutex2);
        }
        /* Perdedor */
        else {
            // Recoge los hilos
            for (i = 0; i < num_hilos; i++){
                //Es más seguro que los trabajadores terminen de forma natural
                solucion=1;
                //pthread_cancel(h[i]);
                pthread_join(h[i], NULL);
                solucion=-1;
            }

            /* Sincronizacion con el ganador para que inicie el proceso
             * de voto y vaciar la cola de señales pendientes para los 
             * perdedores */
            // Espera que permite resetear las señales bloqueadas antes
            // de recibir SIGUSR2
            clock_gettime(CLOCK_REALTIME, &abs_time);
            abs_time.tv_sec += 5;
            if (sem_timedwait(&net_data->sem_perdedor, &abs_time) == -1){
                // Se ha corrompido la red, porque no hay votantes
                break;
            }

            /* En caso de haber encontrado la respuesta tarde,
             * el minero recibira dos SIGUSR2 (se deshecha)
             **/
            sigpending(&set);
            if (sigismember(&set,SIGUSR2)) {
                sigfillset(&set);
                sigdelset(&set, SIGUSR2);
                sigsuspend(&set);
            }

            sem_wait(sem_mutex2);
            net_data->total_miners_aux --;
            // El ultimo debe despertar al ganador
            if (net_data->total_miners_aux == 0) {
                sem_post(&net_data->sem_ganador);
            }
            sem_post(sem_mutex2);

            // Espera para votar
            sigfillset(&set);
            sigdelset(&set, SIGUSR2);
            sigdelset(&set, SIGALRM);
            /* Los alarms y semtimedwait pueden corromper la red, en
             * caso de que un proceso sufra un periodo de  inanicion 
             * mayor al timeout - ha ocurrido*/ 
            alarm(3);
            got_sigarlm = 0;
            sigsuspend(&set);

            if (got_sigarlm){
                break;
            }
            // Cancela las alarmas pendientes
            alarm(0);
        
            // Calculo de comprobacion
            sem_wait(sem_mutex1);
            supuesta_sol = simple_hash(mem_block->solution);
            sem_post(sem_mutex1);

            // Lee la supuesta solucion y vota
            sem_wait(sem_mutex2);                
            for (i = 0; i < MAX_MINERS && net_data->miners_pid[i] != getpid(); i++);
            if (supuesta_sol == bloque_actual->target){
                net_data->voting_pool[i] = 1;
            } else {
                net_data->voting_pool[i] = 0;
            }
            net_data->total_miners_aux --;
            // Despiertan al ganador para que compruebe el resultado
            if (net_data->total_miners_aux == 0){
                // Si murio el ganador los mineros finalizaran por timeout
                sem_post(&net_data->sem_ganador);
            }
            sem_post(sem_mutex2);

            // Espera para conocer si el bloque fue validado
            clock_gettime(CLOCK_REALTIME, &abs_time);
            abs_time.tv_sec += 4;
            if (sem_timedwait(&net_data->sem_perdedor, &abs_time) == -1){
                // Se ha corrompido la red, porque no hay votantes
                break;
            }

            // Consigue la posicion relativa de la cartera del ganador
            sem_wait(sem_mutex2);
            for (i = 0; i < MAX_MINERS && net_data->miners_pid[i] != net_data->last_winner; i++);
            sem_post(sem_mutex2);

            //Copia el bloque de memoria
            sem_wait(sem_mutex1);
            if (mem_block->is_valid) {
                bloque_actual->is_valid=mem_block->is_valid;
                bloque_actual->solution=mem_block->solution;
                bloque_actual->wallets[i] = mem_block->wallets[i];
            }
            sem_post(sem_mutex1);

            // Despiertan al ganador para que prepare el nuevo bloque
            sem_wait(sem_mutex2);
            net_data->total_miners_aux --;
            if (net_data->total_miners_aux == 0){
                sem_post(&net_data->sem_ganador);
            }
            sem_post(sem_mutex2);

            //Envio del bloque actual a la cola, con prioridad 1
            sem_wait(sem_mutex1);
            monitor = net_data->monitor_pid;
            sem_post(sem_mutex1);
            if(monitor != -1){
                mq_send(queue, (char *) bloque_actual, sizeof(Block), 1); 
            }
        }
    }

    //Antes de acabar imprime en un fichero identificado por su PID su cadena de bloques
    print_blocks(bloque_actual, MAX_MINERS);

    // Se desliga de la red y libera recursos
    sem_wait(sem_mutex2);
    for (i = 0; i < MAX_MINERS && net_data->miners_pid[i] != getpid(); i++);
    net_data->miners_pid[i] = -1;
    net_data->total_miners --;
    sem_post(sem_mutex2);
    unlink_and_unmap(sem_mutex1, mem_block, sem_mutex2, net_data);
    freeAll(queue,bloque_actual,rg,h);
    fprintf(stdout, "Finalizo correctamente\n");
    exit(EXIT_SUCCESS);
}