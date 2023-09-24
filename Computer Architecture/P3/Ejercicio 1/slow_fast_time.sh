# Ejemplo script, para P3 arq 2019-2020
# Ejercicio 1 - tiempos de ejecución slow y fast

#!/bin/bash

# inicializar variables
Ninicio=1024
Npaso=1024
Nveces=16
Nfinal=$((Ninicio*Nveces))
Nrep=10
fDAT=slow_fast_time.dat
fPNG=slow_fast_time.png

# Ruta relativa de los archivos
SLOW_ROUTE=../FicherosC/slow
FAST_ROUTE=../FicherosC/fast

# borrar el fichero DAT y el fichero PNG
rm -f $fDAT $fPNG

# generar el fichero DAT vacío
touch $fDAT

echo "Running slow and fast..."
# bucle para N desde P hasta Q

# Inicializacion
for J in $(seq 0 1 $((Nveces-1))); do
	slowTime[$J]=0
	fastTime[$J]=0
done

for J in $(seq 1 1 $Nrep); do
	# Slow
	echo "Repeticion: $J / $Nrep"
	echo "Slow..."
	for N in $(seq $Ninicio $Npaso $Nfinal);do
		
		echo "N: $N / $Nfinal..."
		# ejecutar los programas slow y fast consecutivamente con tamaño de matriz N
		# para cada uno, filtrar la línea que contiene el tiempo y seleccionar la
		# tercera columna (el valor del tiempo). Dejar los valores en variables
		# para poder imprimirlos en la misma línea del fichero de datos
		slowTime[$(((N-Ninicio)/Npaso))]=$(echo "scale=8; ${slowTime[$((N/Ninicio - 1))]}+$(./$SLOW_ROUTE $N | grep 'time' | awk '{print $3}')" | bc)
	done
	# Fast
	echo "Fast..."
	for N in $(seq $Ninicio $Npaso $Nfinal);do
		
		echo "N: $N / $Nfinal..."
		# ejecutar los programas slow y fast consecutivamente con tamaño de matriz N
		# para cada uno, filtrar la línea que contiene el tiempo y seleccionar la
		# tercera columna (el valor del tiempo). Dejar los valores en variables
		# para poder imprimirlos en la misma línea del fichero de datos
		fastTime[$(((N-Ninicio)/Npaso))]=$(echo "scale=8; ${fastTime[$((N/Ninicio - 1))]}+$(./$FAST_ROUTE $N | grep 'time' | awk '{print $3}')" | bc)
	done
done

# Medias
for J in $(seq 0 1 $((Nveces-1))); do
	fastTime[$J]=$(echo "scale=8; ${fastTime[$J]}/$Nveces" | bc)
	slowTime[$J]=$(echo "scale=8; ${slowTime[$J]}/$Nveces" | bc)
	echo "$((Ninicio + J*Npaso))	${slowTime[$J]} ${fastTime[$J]}" >> $fDAT
done

echo "Generating plot..."
# llamar a gnuplot para generar el gráfico y pasarle directamente por la entrada
# estándar el script que está entre "<< END_GNUPLOT" y "END_GNUPLOT"
gnuplot << END_GNUPLOT
# Tamaño y nombre del fichero de salida
set size ratio 0.5
set term png size 1600, 900
set output "$fPNG"
set key right bottom
set grid

# Título y labels
set title "Slow-Fast Execution Time"
set ylabel "Execution time (s)"
set xlabel "Matrix Size"

# Tamaño de fuentes
set title font ",20"
set tics font ",14"
set xlabel font ",14"
set ylabel font ",14"

plot "$fDAT" using 1:2 with lines lw 2 title "slow", \
     "$fDAT" using 1:3 with lines lw 2 title "fast"
replot
quit

END_GNUPLOT
