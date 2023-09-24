#include "lib_server.h"

/**
 * @brief Inicializa un servidor en base a las especificaciones de configuración (parametros de entrada)
 * @param ???
 * @return 0 para salida correcta; codigo de salida errno de la llamada que causo el fallo
 * */

void server_init() {
    // Se crea el socket
    syslog(LOG_INFO, "Creando socket");
    int sfd = socket(AF_INET, SOCK_STREAM, TCP);
    if (sfd == -1) {
        syslog(LOG_ERR, "Error al crear el socket");
        exit(errno);
    }

    struct sockaddr_in saddr;
    bzero(&saddr, sizeof(saddr));
	saddr.sin_family = AF_INET;
	saddr.sin_port = htons(SERVER_PORT);
	saddr.sin_addr.s_addr = htonl(INADDR_ANY);
                            //INADDR_ANY

    // Se liga puerto al socket
    syslog(LOG_INFO, "Ligando socket");
    if ( bind(sfd, (struct sockaddr*)&saddr, sizeof(saddr)) != 0 )
	{
		syslog(LOG_ERR, "Error al ligar el socket");
        close(sfd);
        exit(errno);
	}
    
	// Listo para la escucha
	syslog(LOG_INFO, "Listo para la escucha");
    if (listen(sfd, MAX_CONN) != 0 )
	{
        syslog(LOG_ERR, "Error durante la escucha");
        close(sfd);
		exit(errno);
	}

    global_sfd = sfd;
}

/**
 * @brief TODO Gestiona las conexiones de clientes al servidor
 * @param sfd descriptor de fichero del socket
 * @return 0 para salida correcta; codigo de salida errno de la llamada que causo el fallo
 * */
void server_accept(){
    int fd, tam;
    struct sockaddr connection;

    tam = sizeof(connection);

    while(1) {
        pthread_mutex_lock(&lock);
        fd = accept(global_sfd, &connection, &tam);
        pthread_mutex_unlock(&lock);
        if (fd < 0) {
            syslog(LOG_ERR, "Error al aceptar la conexión");
            // !!
            exit(EXIT_FAILURE);
        }

        //El hilo trabaja
        process_request(fd);
        
        close(fd);
    }
}

/**
 * @brief TODO Procesa las peticiones de clientes al servidor
 * @param 
 * @return 
 * */
void process_request(int fd){
    char buf[MAX_LEN];

    if(recv(fd, buf, MAX_LEN, 0) <= 0){
        exit(EXIT_FAILURE);
    }

    for(int i=0; i < strlen(buf); i++){
        if(buf[i] >= 'a' && buf[i] <= 'z'){
            buf[i] -= ('a' - 'A');
        }
    }

    if(send(fd, buf, strlen(buf), 0) < 0){
        exit(EXIT_FAILURE);
    }
}

/**
 * @brief Cierra un servidor
 * @param sfd descriptor de fichero del socket
 * @return 0 para salida correcta; codigo de salida errno de la llamada que causo el fallo
 * */
int server_close(int sfd) {
    if (close(sfd) < 0) {
        syslog(LOG_ERR, "Error al cerrar el servidor");
        exit(errno);
    }
    return 0;
}