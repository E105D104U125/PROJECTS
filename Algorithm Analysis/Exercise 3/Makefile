CC = gcc -ansi -pedantic
CFLAGS = -Wall -O3
EXE = ejercicio0 ejercicio1 ejercicio2

all : $(EXE)

.PHONY : clean
clean :
	rm -f *.o core $(EXE)

$(EXE) : % : %.o permutaciones.o busqueda.o tiempos.o
	@echo "#---------------------------"
	@echo "# Generando $@ "
	@echo "# Depende de $^"
	@echo "# Ha cambiado $<"
	$(CC) $(CFLAGS) -o $@ $@.o permutaciones.o busqueda.o tiempos.o

permutaciones.o : permutaciones.c permutaciones.h
	@echo "#---------------------------"
	@echo "# Generando $@"
	@echo "# Depende de $^"
	@echo "# Ha cambiado $<"
	$(CC) $(CFLAGS) -c $<
	
busqueda.o: busqueda.c busqueda.h
	@echo "#---------------------------"
	@echo "# Generando $@"
	@echo "# Depende de $^"
	@echo "# Ha cambiado $<"
	$(CC) $(CFLAGS) -c $<
	
ejercicio0_test: permutaciones.o busqueda.o
	@echo "Ejecutando ejercicio0.c para comprobar TAD Diccionario"
	@./ejercicio0 -tamanio 20 -orden 0

ejercicio1_test: permutaciones.o busqueda.o
	@echo "Ejecutando ejercicio1.c para comprobar TAD Diccionario"
	@./ejercicio1 -tamanio 50 -clave 50

ejercicio2_test: permutaciones.o busqueda.o tiempos.o
	@echo "Ejecutando ejercicio2.c para comprobar tiempos.c"
	@./ejercicio2 -num_min 50 -num_max 100 -incr 10 -n_veces 2 -fichSalida tiemposBORRAR.dat
