#!/usr/bin/gnuplot -p

set fit logfile '/dev/null'
set key left
set size ratio 0.5
set term png size 1600, 900
set output "tiempo_comparacion.png"
set title "Comparación tiempo medio BBIN y BLIN"
set xlabel "Tamaño del diccionario"
set ylabel "Tiempo (ms)"
set xrange [0:51000]
set yrange [0:0.025]
set title font ",20"
set tics font ",14"
set xlabel font ",14"
set ylabel font ",14"
tiempo_bbin(x) = a*log(x)
tiempo_blin(x) = b*x
fit tiempo_bbin(x) 'tiempos_bbin.dat' using 1:2 via a
fit tiempo_blin(x) 'tiempos_blin.dat' using 1:2 via b
plot 'tiempos_bbin.dat' using 1:2 title "Tiempo BBIN" ls 7 lc 4 lw 2, \
     'tiempos_blin.dat' using 1:2 title "Tiempo BLIN" ls 7 lc 2 lw 2, \
     tiempo_bbin(x) lc 4 lw 2, \
     tiempo_blin(x) lc 2 lw 2, \
