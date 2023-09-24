#!/usr/bin/gnuplot

# Ajustes varios
set fit logfile '/dev/null'
set key left
set size ratio 0.5

# Tamaño y nombre del fichero de salida
set term png size 1600, 900
set output "bbin_obs.png"

# Título y labels
set title "Número de OBs de Búsqueda Binaria"
set xlabel "Tamaño del diccionario"
set ylabel "Número de OBs"

# Rango
set xrange [4000:51000]
set yrange [0:20]

# Tamaño de fuentes
set title font ",20"
set tics font ",14"
set xlabel font ",14"
set ylabel font ",14"

# Regresiones
avg(x) = b*log(x)
min(x) = c
max(x) = d*log(x)
fit avg(x) 'tiempos_bbin.dat' using 1:3 via b
fit min(x) 'tiempos_bbin.dat' using 1:4 via c
fit max(x) 'tiempos_bbin.dat' using 1:5 via d

# Plot
plot 'tiempos_bbin.dat' using 1:3 title "OBs medias" ls 7 lc 4 lw 2, \
     'tiempos_bbin.dat' using 1:4 title "OBs mínimas" ls 7 lc 2 lw 2, \
     'tiempos_bbin.dat' using 1:5 title "OBs máximas" ls 7 lc 1 lw 2, \
     avg(x) lc 4 lw 2, \
     min(x) lc 2 lw 2, \
     max(x) lc 1 lw 2
