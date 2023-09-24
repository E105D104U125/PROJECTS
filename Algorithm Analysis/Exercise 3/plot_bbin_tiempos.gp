#!/usr/bin/gnuplot

# Ajustes varios
set fit logfile '/dev/null'
set key left

# Tamaño y nombre del fichero de salida
set size ratio 0.5
set term png size 1600, 900
set output "bbin_tiempos2.png"

# Título y labels
set title "Tiempo medio de ejecución de Búsqueda Binaria"
set xlabel "Tamaño del diccionario"
set ylabel "Tiempo (ns)"

# Rango
set xrange [4000:51000]
set yrange [30:70]

# Tamaño de fuentes
set title font ",20"
set tics font ",14"
set xlabel font ",14"
set ylabel font ",14"

# Regresiones
tiempo(x) = a*log(x)
fit tiempo(x) 'tiempos_bbin_V2.dat' using 1:2 via a

# Plot
plot 'tiempos_bbin_V2.dat' using 1:2 title "Tiempo medio Búsqueda Binaria" ls 7 lc 4 lw 2, \
     tiempo(x) lc 4 lw 2
