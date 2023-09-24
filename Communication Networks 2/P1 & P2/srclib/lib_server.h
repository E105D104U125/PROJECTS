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
#include <signal.h>

#define IP 0
#define TCP 6
// Cambiar por conf
#define MAX_CONN 20
#define MAX_LEN 512
#define SERVER_PORT 5000

#define ERROR -1
#define OK 0

int global_sfd;

pthread_mutex_t lock;

void server_init();
void server_accept();
int server_close();
void process_request(int fd);
