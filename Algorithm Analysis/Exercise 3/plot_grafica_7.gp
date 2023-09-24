#!/usr/bin/gnuplot -p

set fit logfile '/dev/null'
set key left
set size ratio 0.5
set term png size 1600, 900
set output "OBs_MEDIA_blin_auto_ordenado_potencial.png"
set title "OBs medias BLIN auto para diccionario ordenado y distribución potencial"
set xlabel "Tamaño del diccionario"
set ylabel "Operaciones básicas"
set xrange [0:51000]
set yrange [0:20]
set title font ",20"
set tics font ",14"
set xlabel font ",14"
set ylabel font ",14"
ob_blin_auto(x) = a*log(x)
fit ob_blin_auto(x) 'tiempos_blin_auto_ordenado_potencial.dat' using 1:3 via a
plot 'tiempos_blin_auto_ordenado_potencial.dat' using 1:3 title "OBs BLIN auto" ls 7 lc 1 lw 2, \
     ob_blin_auto(x) lc 1 lw 2
