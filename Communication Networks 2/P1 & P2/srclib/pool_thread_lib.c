/**
 * Librería para la gestión del pool de threads
 * */

#include "pool_thread_lib.h"

sv_thread *sv_thread_create(thread_function fcn, int id) {
    sv_thread *server_thread = NULL;
    int t_ret;

    server_thread = (sv_thread *) malloc(sizeof(sv_thread));
    if(!server_thread) return NULL;

    server_thread->id = id;
    t_ret = pthread_create(&server_thread->thread, NULL, fcn, NULL);
    return server_thread;
}

sv_pool *sv_pool_create(thread_function fcn, int n_threads) {
    sv_pool *pool;
    int tp, thread_id;

    if (n_threads < 1 || !fcn) {
        return NULL;
    }

    pool = (sv_pool *) malloc(sizeof(sv_pool));
    if (!pool) {
        perror(strerror(errno));
        return NULL;
    }
    
    pool->num_threads = n_threads;

    pool->threads = (sv_thread **) malloc(sizeof(sv_pool*)*n_threads);
    if (!pool->threads) {
        perror(strerror(errno));
        free(pool);
        return NULL;
    }

    for (thread_id = 0; thread_id < n_threads; thread_id++) {
        pool->threads[thread_id] = sv_thread_create(fcn, thread_id);
    }

    return pool;
}

void sv_thread_destroy(sv_thread *thread) {
    if (!thread) return;
    pthread_cancel(thread->thread);
    free(thread);
}

void sv_pool_destroy(sv_pool *pool) {
    int i;

    if (!pool) return;

    for (i = 0; i < pool->num_threads; i++) {
        sv_thread_destroy(pool->threads[i]);
    }

    free(pool->threads);
    free(pool);
}
