#!/usr/bin/gnuplot -p

set fit logfile '/dev/null'
set key left
set size ratio 0.5
set term png size 1600, 900
set output "obs_comparacion.png"
set title "Comparación OBs medias BBIN y BLIN"
set xlabel "Tamaño del diccionario"
set ylabel "Número de OBs"
set xrange [4000:51000]
set yrange [0:26000]
set title font ",20"
set tics font ",14"
set xlabel font ",14"
set ylabel font ",14"
ob_bbin(x) = a*log(x)
ob_blin(x) = b*x
fit ob_bbin(x) 'tiempos_bbin.dat' using 1:3 via a
fit ob_blin(x) 'tiempos_blin.dat' using 1:3 via b
plot 'tiempos_bbin.dat' using 1:3 title "OBs BBIN" ls 7 lc 4 lw 2, \
     'tiempos_blin.dat' using 1:3 title "OBs BLIN" ls 7 lc 2 lw 2, \
     ob_bbin(x) lc 4 lw 2, \
     ob_blin(x) lc 2 lw 2, \
