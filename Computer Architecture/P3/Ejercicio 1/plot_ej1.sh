# Plotting ejercicio 1

#!/bin/bash
Nrep=10
fDAT=slow_fast_time.dat
fPNG=slow_fast_time.png

# borrar el fichero DAT y el fichero PNG
rm -f $fPNG

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
