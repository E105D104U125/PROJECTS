EXE = proc_shell
AB = abort
LIB = -lpthread
make shell: abort
	gcc -c $(EXE).c
	gcc -o $(EXE) $(EXE).o $(LIB)
	clear
	./$(EXE)
abort: $(AB).c
	gcc -c $(AB).c
	gcc -o $(AB) $(AB).o
