/**
 * Librería para la gestión del pool de threads - Header File
 * */
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <errno.h>
#include <sys/socket.h>
#include <sys/types.h>
#include <resolv.h>
#include <arpa/inet.h>
#include <syslog.h>
#include <netinet/in.h>
#include <errno.h>
#include <unistd.h>
#include <pthread.h>

typedef struct _SV_THREAD {
    pthread_t thread;
    int id;
} sv_thread;

typedef struct _SV_POOL {
    sv_thread **threads;
    volatile int num_threads;
} sv_pool;

typedef void (* thread_function);

sv_thread *sv_thread_create(thread_function fcn, int id);
sv_pool *sv_pool_create(thread_function fcn, int n_threads);
void sv_thread_destroy(sv_thread *thread);
void sv_pool_destroy(sv_pool *pool);
