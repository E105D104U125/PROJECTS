#include <fcntl.h>
#include <stdio.h>
#include <stdlib.h>
#include <sys/stat.h>
#include <unistd.h>

#define SHM_NAME "/shm_example"
#define MESSAGE "Test message"

#define MAX_BUF 100


int main(int argc, char *argv[]) {
    int fd;
    struct stat statbuf;
    char buf[MAX_BUF];
/*
    close(fd);
    exit(0);*/

    if (argc != 2) {
        fprintf(stderr, "Usage: %s <FILE>\n", argv[0]);
    }

    fd = open(argv[1], O_RDWR | O_CREAT | O_EXCL, S_IRUSR | S_IWUSR);
    if (fd == -1) {
        perror("open");
        exit(EXIT_FAILURE);
    }
    dprintf(fd, "%s", MESSAGE);
    /* Get size of the file. */
    if (fstat(fd, &statbuf) == -1){
        perror("fstat");
    }    
    fprintf(stdout,"Tamanio de fichero: %d\n", (int) statbuf.st_size);
    fflush(stdout);

    /* Truncate the file to size 5. */
    ftruncate(fd,5);

    close(fd);
    exit(EXIT_SUCCESS);
}
