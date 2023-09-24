/**
 * 2201_08
 * @author Gregorio Blázquez Martínez
 * @author Eduardo Terrés Caballero
 *
 * Ejercicio 10 - Práctica 2
 * conc_cycle.c 
 * */

#include <stdio.h>
#include <stdlib.h>
#include <signal.h>
#include <sys/types.h>
#include <sys/wait.h>
#include <unistd.h>
#include <semaphore.h>
#include <fcntl.h>
#include <errno.h>

#define SEM_NAME "/sem"

static volatile sig_atomic_t got_sigterm = 0;
static volatile sig_atomic_t got_sigint = 0;

/* manejadorSIGTERM : rutina de tratamiento de la señal SIGTERM . */
void manejadorSIGTERM(int sig)
{
    got_sigterm = 1;
}

/* manejadorSIGUSR1 : rutina de tratamiento de la señal SIGTERM . */
void manejadorSIGUSR1(int sig)
{
    return;
}

/* manejadorINT : rutina de tratamiento de la señal SIGINT solo para el padre . */
void manejadorSIGINT(int sig)
{
    got_sigint = 1;
}

/* manejadorSIGUSR2 : rutina que mata a todos los procesos */
void manejadorSIGUSR2(int sig)
{
    /* Termina el ciclo */
    wait(NULL);
    exit(EXIT_FAILURE);
}

/**
 * @author Gregorio Blazquez Martinez
 * @author Eduardo Terres Caballero
 * 
 * Mata a los procesos generados durante la ejecucion de conc_cycle, mediante
 * el envio de la señal SIGUSR2 - no se debe bloquear -.
 * La ejecucion del manejador manejadorSIGUSR2 incluye la espera - wait - de los
 * hijos creados.
 * 
 * Libera:
 * + Memoria del vector pids
 * + Permite la liberacion del semaforo creado para la sincronizacion de los 
 *   procesos (unlink).
 * 
 * 
 * @param pids array de IDs de los procesos creados
 * @param tam tamanio de array pids
 * */
void killAll(pid_t *pids, int tam)
{
    int i;
    sem_unlink(SEM_NAME);
    for (i = 0; i < tam; i++)
    {
        /* Mata a todos menos a si mismo */
        if (pids[i] != getpid())
        {
            kill(pids[i], SIGUSR2);
        }
    }
    free(pids);
    wait(NULL);
}

int main(int argc, char *argv[])
{
    int num_proc = 0, i;
    pid_t childpid, antecesorpid, *pids;
    struct sigaction act;
    sigset_t set, oset;
    sem_t *sem = NULL;
    int err = 0;

    /* CdE */
    if (argc != 2)
    {
        printf("Parametros incorrectos. Introduzca numero de procesos concurrentes.");
        exit(EXIT_FAILURE);
    }

    /* Conversion string a int del numero de procesos concurrentes */
    num_proc = atoi(argv[1]);
    if (num_proc < 1)
    {
        printf("Numero de procesos concurrentes incorrecto.");
        exit(EXIT_FAILURE);
    }

    /* Array de todos los pids para la llamada a killAll */
    pids = (pid_t *)malloc(num_proc * sizeof(pid_t));
    if (!pids)
    {
        perror("malloc");
        exit(EXIT_FAILURE);
    }

    /* Creamos el semaforo que gestionara los accesos a stdout - impresion */
    if ((sem = sem_open(SEM_NAME, O_CREAT | O_EXCL, S_IRUSR | S_IWUSR, 1)) == SEM_FAILED)
    {
        perror("sem_open");
        free(pids);
        exit(EXIT_FAILURE);
    }

    /* Guardamos el pid del proceso original */
    antecesorpid = getpid();

    /* Se cambian las mascaras */
    /* Bloqueamos la señal SIGUSR1 para gestionarlas
       en el momento preciso, evitando perdida de señales */
    sigemptyset(&set);
    sigaddset(&set, SIGUSR1);
    /* Se bloquea a todos los procesos porque al enviar 
     * Ctrl + C desde la terminal, no solo la recibe el ancestro
     * De esta forma, nos aseguramos de que solo el ancestro 
     * procese SIGINT  */
    sigaddset(&set, SIGINT);
    /* Se bloquea a todos los procesos
     * Mas tarde se desbloqueara al ancestro
     * Evita condicion de carrera*/
    sigaddset(&set, SIGTERM);
    if (sigprocmask(SIG_BLOCK, &set, &oset) < 0)
    {
        perror("sigprocmask");
        sem_close(sem);
        sem_unlink(SEM_NAME);
        free(pids);
        exit(EXIT_FAILURE);
    }

    /* Modificamos el manejador de SIGUSR1
     * se hereda tras los forks */
    act.sa_handler = manejadorSIGUSR1;
    sigemptyset(&(act.sa_mask));
    act.sa_flags = 0;
    if (sigaction(SIGUSR1, &act, NULL) < 0)
    {
        perror("sigaction");
        sem_close(sem);
        sem_unlink(SEM_NAME);
        free(pids);
        exit(EXIT_FAILURE);
    }

    /* Creación de los procesos */
    /* Cada proceso crea a un hijo hasta alcanzar el número de procesos pedidos */
    i = 1;
    while (i < num_proc)
    {
        /* El padre crea un proceso y se sale del bucle */
        childpid = fork();
        if (childpid == -1)
        {
            perror("fork");
            killAll(pids, i);
            exit(EXIT_FAILURE);
        }
        else if (childpid)
        {
            /* Se aniade un proceso al array de procesos - auxiliar de killAll */
            /* el proceso que realiza la llamada no necesita encontrarse en el array
            * en caso de fallo en los forks */
            pids[i - 1] = getpid();
            break;
        }
        i++;
    }

    /* Se aniade el ultimo proceso */
    if (childpid == 0)
        pids[i - 1] = getpid();

    /* MANEJADORES ESPECIFICOS */
    /* Manejadores y mascara: Excepto proceso ancestro */
    if (getpid() != antecesorpid)
    {
        /* Manejador SIGTERM */
        act.sa_handler = manejadorSIGTERM;
        sigemptyset(&(act.sa_mask));
        act.sa_flags = 0;
        if (sigaction(SIGTERM, &act, NULL) < 0)
        {
            perror("sigaction");
            /* Se terminan todos los procesos */
            sem_close(sem);
            killAll(pids, num_proc); //incluye EXIT
        }
    }

    /* Manejadores y mascara: Proceso ancestro */
    if (getpid() == antecesorpid)
    {
        /* Modificamos el manejador del proceso original para SIGINT */
        act.sa_handler = manejadorSIGINT;
        sigemptyset(&(act.sa_mask));
        act.sa_flags = 0;
        if (sigaction(SIGINT, &act, NULL) < 0)
        {
            perror("sigaction");
            sem_close(sem);
            killAll(pids, num_proc);
            exit(EXIT_FAILURE);
        }
        /* Modificamos el manejador del proceso original para SIGALARM */
        /* Nos sirve el mismo manejador para poner la variable got_sigint a 1 
           para que el padre envie la señal SIGTERM a su hijo. */
        if (sigaction(SIGALRM, &act, NULL) < 0)
        {
            perror("sigaction");
            sem_close(sem);
            killAll(pids, num_proc);
            exit(EXIT_FAILURE);
        }

        /*  Nueva mascara */
        sigemptyset(&set);
        sigaddset(&set, SIGUSR1);
        sigaddset(&set, SIGINT);
        sigaddset(&set, SIGALRM);
        if (sigprocmask(SIG_SETMASK, &set, &oset) < 0)
        {
            perror("sigprocmask");
            sem_close(sem);
            killAll(pids, num_proc);
            exit(EXIT_FAILURE);
        }

        if (alarm(10))
        {
            fprintf(stderr, "Existe una alarma previa establecida.\n");
        }
    }

    /* PREPARAMOS LAS MASCARAS PARA SIGSUSPEND */
    i = 1;
    /* Todos los procesos pueden recibir SIGUSR1 */
    sigfillset(&set);
    sigdelset(&set, SIGUSR1);
    /* Solo el proceso padre puede recibir SIGINIT y SIGALRM */
    if (getpid() == antecesorpid)
    {
        sigdelset(&set, SIGINT);
        sigdelset(&set, SIGALRM);
    }
    /* Todos menos el padre pueden recibir SIGTERM */
    else
    {
        sigdelset(&set, SIGTERM);
    }

    /* Para que comiencen en orden hacemos esperar a todos los procesos
    que no sean el original */
    if (getpid() != antecesorpid)
    {
        sigsuspend(&set);
    }

    /* CICLOS */
    while (1)
    {   
        /* Bloqueamos el acceso a la zona critica al resto de procesos */
        /* La espera del semaforo no se interrumpe por una señal */
        while(sem_wait(sem) == -1);

        /* ENVIO DE SEÑALES */
        /* Todos los procesos con hijo envian la señal SIGUSR1 a su hijo */
        if (childpid)
        {
            if (kill(childpid, SIGUSR1) == -1)
            {
                perror("kill");
                /* El hijo ha muerto, se matan todos los procesos */
                sem_close(sem);
                killAll(pids, num_proc);
                exit(EXIT_FAILURE);
            }
        }
        /* El ultimo proceso envia la señal SIGUSR1 al proceso inicial */
        else
        {
            if (kill(antecesorpid, SIGUSR1) == -1)
            {
                perror("kill");
                sem_close(sem);
                killAll(pids, num_proc);
                exit(EXIT_FAILURE);
            }
        }
        /* IMPRESION */
        fprintf(stdout, "Numero de ciclo %d (PID=%d)\n", i, getpid());

        /* Permitimos el acceso a la zona critica al siguiente proceso
        que solicite entrar que sera el hijo del proceso que acaba de
        enviar la señal SIGUSR1 */
        sem_post(sem);

        /* ESPERA DE SEÑALES */
        sigsuspend(&set);

        /* Gestion de las señales */
        if (got_sigint == 1)
        {
            kill(childpid, SIGTERM);
            wait(NULL);
            /* LIBERAR RECURSOS */
            sem_close(sem);
            sem_unlink(SEM_NAME);
            free(pids);
            /* Salida del proceso */
            exit(EXIT_SUCCESS);
        }
        if (got_sigterm == 1)
        {
            /* Envio de SIGTERM al hijo */
            if (childpid)
            {
                kill(childpid, SIGTERM);
                wait(NULL);
            }
            /* LIBERAR RECURSOS */
            sem_close(sem);
            free(pids);
            /* Salida del proceso */
            exit(EXIT_SUCCESS);
        }
        i++;
    }
}