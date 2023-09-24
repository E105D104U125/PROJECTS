ARQO3_ROUTE=../FicherosC/arqo3
EXE=multiplicacion_normal multiplicacion_transpuesta
FLAGS = -O3 -g -Wall -D_GNU_SOURCE

.PHONY: clean all

all:multiplicacion_normal multiplicacion_transpuesta

multiplicacion_normal: multiplicacion_normal.c $(ARQO3_ROUTE).c
	gcc $(FLAGS) -o $@ $^ $(ARQO3_ROUTE).h
	
multiplicacion_transpuesta: multiplicacion_transpuesta.c $(ARQO3_ROUTE).c
	gcc $(FLAGS) -o $@ $^ $(ARQO3_ROUTE).h

clean:
	rm -f *.o *~ $(EXE)
