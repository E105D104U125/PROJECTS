/**
 * Proyecto Final SOPER
 * Autores: Grupo 8
 * 
 *      Gregorio Blázquez Martínez
 *      Eduardo Terrés Caballero
 * 
 * */

#include <unistd.h>

#define OK 0
#define MAX_WORKERS 10
#define BUF_MAX 1024
#define COM_MAX 32

#define SHM_NAME_NET "/netdata"
#define SHM_NAME_BLOCK "/block"

#define MQ "/mq"

#define SEM_NAME1 "/sem1"
#define SEM_NAME2 "/sem2"

#define MAX_MINERS 200

typedef struct _Block {
    int wallets[MAX_MINERS];
    long int target;
    long int solution;
    int id;
    int is_valid;
    int opened_block; // Numero de mineros con la memoria del bloque abierto, aunque no esten minando 
    struct _Block *next;
    struct _Block *prev;
} Block;

typedef struct _NetData {
    pid_t miners_pid[MAX_MINERS];
    char voting_pool[MAX_MINERS];
    int last_miner;
    int total_miners;
    int total_miners_aux; // Almacena el valor total_miners pero se puede modificar durante las rondas
    int opened_net; // Numero de mineros con la red abierta, aunque no esten minando 
    sem_t sem_ronda;
    sem_t sem_ganador;
    sem_t sem_perdedor;
    pid_t monitor_pid;
    pid_t last_winner;
} NetData;

typedef struct _RangoBusqueda {
	int num_hilos;
	int start;
    long target;
} RangoBusqueda;

long int simple_hash(long int number);

void print_blocks(Block * plast_block, int num_wallets);
