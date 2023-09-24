# Plotting ejercicio 3 - tiempos de ejecución

#!/bin/bash

fDAT=mult.dat
fPNG1=mult_time.png
fPNG2=mult_cache.png
fPNG3=mult_cache_escritura.png

# borrar los ficheros PNG
rm -f $fPNG1 $fPNG2 $fPNG3

echo "Plotting tiempos de ejecución..."
gnuplot << END_GNUPLOT
# Tamaño y nombre del fichero de salida
set size ratio 0.5
set term png size 1600, 900
set output "$fPNG1"
set key right bottom
set grid

# Título y labels
set title "Normal-Trans Execution Time"
set ylabel "Execution time (s)"
set xlabel "Matrix Size"

# Tamaño de fuentes
set title font ",20"
set tics font ",14"
set xlabel font ",14"
set ylabel font ",14"

plot '$fDAT' using 1:2 with lines lw 2 title "normal",\
'$fDAT' using 1:5 with lines lw 2 title "transpuesta"
replot
quit

END_GNUPLOT

# Plotting de fallos de caché
echo "Plotting fallos de caché..."
gnuplot << END_GNUPLOT
# Tamaño y nombre del fichero de salida
set term pngcairo size 1600, 900
set size ratio 0.5
set output "$fPNG2"
set key left top
set grid

# Título y labels
set title "Fallos de caché"
set ylabel "Nº fallos"
set xlabel "Matrix Size"

# Tamaño de fuentes
set title font ",20"
set tics font ",14"
set xlabel font ",14"
set ylabel font ",14"

plot '$fDAT' using 1:3 with lines lw 2 dt 2 title "Fallos lectura NORMAL",\
'$fDAT' using 1:4 with lines lw 2 dt 2 lt rgb "#FF0000" title "Fallos escritura NORMAL",\
'$fDAT' using 1:6 with lines lw 2 title "Fallos lectura TRANS",\
'$fDAT' using 1:7 with lines lw 2 lt rgb "#10FF00" title "Fallos escritura TRANS"

quit

END_GNUPLOT

# Plotting de fallos de caché
echo "Plotting fallos de caché de escritura..."
gnuplot << END_GNUPLOT
# Tamaño y nombre del fichero de salida
set term pngcairo size 1600, 900
set size ratio 0.5
set output "$fPNG3"
set key left top
set grid

# Título y labels
set title "Fallos de caché de escritura"
set ylabel "Nº fallos"
set xlabel "Matrix Size"

# Tamaño de fuentes
set title font ",20"
set tics font ",14"
set xlabel font ",14"
set ylabel font ",14"

plot '$fDAT' using 1:4 with lines lw 2 dt 2 lt rgb "#FF0000" title "Fallos escritura NORMAL",\
'$fDAT' using 1:7 with lines lw 2 lt rgb "#10FF00" title "Fallos escritura TRANS"

quit

END_GNUPLOT
