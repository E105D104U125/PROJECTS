#-----------------------------------------
# Grupo: 6 
# Autores: Eduardo Terrés y Víctor Pérez  
#-----------------------------------------

CC = gcc -ansi -pedantic
CFLAGS = -Wall -O3
EXE = ejercicio4_mergesort ejercicio5_mergesort ejercicio4_quicksort ejercicio5_quicksort ejercicio4_quicksort_src ejercicio5_quicksort_src

all : $(EXE)

.PHONY : clean
clean :
	rm -f *.o core $(EXE)

$(EXE) : % : %.o ordenacion.o tiempos.o permutaciones.o
	@echo "#---------------------------"
	@echo "# Generando $@ "
	@echo "# Depende de $^"
	@echo "# Ha cambiado $<"
	$(CC) $(CFLAGS) -o $@ $@.o ordenacion.o tiempos.o permutaciones.o

permutaciones.o : permutaciones.c permutaciones.h
	@echo "#---------------------------"
	@echo "# Generando $@"
	@echo "# Depende de $^"
	@echo "# Ha cambiado $<"
	$(CC) $(CFLAGS) -c $<

 ordenacion.o : ordenacion.c ordenacion.h
	@echo "#---------------------------"
	@echo "# Generando $@"
	@echo "# Depende de $^"
	@echo "# Ha cambiado $<"
	$(CC) $(CFLAGS) -c $<

 tiempos.o : tiempos.c tiempos.h
	@echo "#---------------------------"
	@echo "# Generando $@"
	@echo "# Depende de $^"
	@echo "# Ha cambiado $<"
	$(CC) $(CFLAGS) -c $<

ejercicio4_mergesort_test: ejercicio4_mergesort
	@echo "#----------------------------------------------------------#"
	@echo " Ejecutando ejercicio4_mergesort, haciendo uso de MergeSort"
	@./ejercicio4_mergesort -tamanio 100

ejercicio5_mergesort_test: ejercicio5_mergesort
	@echo "#----------------------------------------------------------#"
	@echo " Ejecutando ejercicio5_mergesort, haciendo uso de MergeSort"
	@./ejercicio5_mergesort -num_min 1000 -num_max 10000 -incr 500 -numP 500 -fichSalida mergesort.dat
	@echo " Salida de datos: "
	@cat mergesort.dat

ejercicio4_quicksort_test: ejercicio4_quicksort
	@echo "#----------------------------------------------------------#"
	@echo " Ejecutando ejercicio4_quicksort, haciendo uso de QuickSort"
	@./ejercicio4_quicksort -tamanio 100

ejercicio5_quicksort_test: ejercicio5_quicksort
	@echo "#----------------------------------------------------------#"
	@echo " Ejecutando ejercicio5_quicksort, haciendo uso de QuickSort"
	@./ejercicio5_quicksort -num_min 1000 -num_max 10000 -incr 500 -numP 500 -fichSalida quicksort.dat
	@echo " Salida de datos: "
	@cat quicksort.dat
	
ejercicio4_quicksort_src_test: ejercicio4_quicksort_src
	@echo "#------------------------------------------------------------------#"
	@echo " Ejecutando ejercicio4_quicksort_src, haciendo uso de QuickSort src"
	@./ejercicio4_quicksort_src -tamanio 100

ejercicio5_quicksort_src_test: ejercicio5_quicksort_src
	@echo "#------------------------------------------------------------------#"
	@echo " Ejecutando ejercicio5_quicksort_src, haciendo uso de QuickSort src"
	@./ejercicio5_quicksort_src -num_min 1000 -num_max 10000 -incr 500 -numP 500 -fichSalida quicksort_src.dat
	@echo " Salida de datos: "
	@cat quicksort_src.dat
