#include "lib_server.h"
#include "pool_thread_lib.h"

pthread_mutex_t lock = PTHREAD_MUTEX_INITIALIZER;
static volatile sig_atomic_t got_signal = 0;

/* manejador_sigint : rutina de tratamiento de la señal SIGINT . */
void manejador_sigint(int sig) {
    got_signal = 1;
}

int main()  {
	pthread_t tid;

	server_init();

	sv_pool *s = sv_pool_create(&server_accept, MAX_CONN);
	if (!s) exit(EXIT_FAILURE);

	signal(SIGINT, manejador_sigint);
	
	while (1) {
		if(got_signal){
			break;
		}
	};

    sv_pool_destroy(s);
	server_close();
	exit(EXIT_SUCCESS);
}