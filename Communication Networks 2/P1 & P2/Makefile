CC = gcc
LIBS = srclib/lib_server.h srclib/pool_thread_lib.h
EXE = lib_server server pool_thread_lib main_thread_lib
.PHONY: clean all

#all: $(EXE) clear
all: clean
	gcc -c srclib/pool_thread_lib.c srclib/pool_thread_lib.h
	gcc -c srclib/main_thread_lib.c srclib/pool_thread_lib.h
	gcc -c srclib/lib_server.c $(LIBS)
	gcc -c srclib/server.c $(LIBS)
	gcc -o server server.o lib_server.o pool_thread_lib.o -lpthread

pool: clean_lib
	gcc -c srclib/pool_thread_lib.c srclib/pool_thread_lib.h
	gcc -c srclib/main_thread_lib.c srclib/pool_thread_lib.h
	gcc -o main_thread_lib main_thread_lib.o pool_thread_lib.o -lpthread
	

#$(EXE) : % : %.c
#	$(CC) -o $@ $^ $(LIBS)

clean:
	rm -f *.o *~ $(EXE)
	
clear:
	rm -f *.o *~
