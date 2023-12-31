/**
 * Cambiado
 * */

//#include <sys/mman.h>
//#include <sys/stat.h>
#include <fcntl.h>
//#include <unistd.h>
//#include <sys/types.h>
#include <stdlib.h>
#include <errno.h>

#define SHM_NAME "/nombre"

int main(){
    int fd_shm;
/* This is only a code fragment, not a complete program... */
    fd_shm = shm_open (SHM_NAME, O_RDWR | O_CREAT | O_EXCL, S_IRUSR | S_IWUSR);
    if (fd_shm == -1) {
        if (errno == EEXIST) {
            fd_shm = shm_open(SHM_NAME, O_RDWR, 0);
            if (fd_shm == -1) {
                perror("Error opening the shared memory segment");
                exit(EXIT_FAILURE);
            }
        }
        else {
            perror("Error creating the shared memory segment\n");
            exit(EXIT_FAILURE);
        }
    }
    else {
        printf ("Shared memory segment created\n");
    }
}