#!/usr/bin/gnuplot -p
#NUMERO MIN OBs BBIN Y BLIN AUTO

set fit logfile '/dev/null'
set key left
set size ratio 0.5
set term png size 1600, 900
set output "OBs_MIN_comparacion_potencial.png"
set title "Comparación OBs mínimas BBIN y BLIN auto"
set xlabel "Tamaño del diccionario"
set ylabel "Operaciones básicas"
set xrange [0:51000]
set yrange [0:200]
set title font ",20"
set tics font ",14"
set xlabel font ",14"
set ylabel font ",14"
ob_bbin_1(x) = a*log(x)
ob_bbin_100(x) = b*log(x)
ob_bbin_10000(x) = c*log(x)
ob_blin_auto_1(x) = d*x
ob_blin_auto_100(x) = e*x
ob_blin_auto_10000(x) = f*x
fit ob_bbin_1(x) 'tiempos_bbin_potencial_1.dat' using 1:4 via a
fit ob_bbin_100(x) 'tiempos_bbin_potencial_100.dat' using 1:4 via b
fit ob_bbin_10000(x) 'tiempos_bbin_potencial_10000.dat' using 1:4 via c
fit ob_blin_auto_1(x) 'tiempos_blin_auto_potencial_1.dat' using 1:4 via d
fit ob_blin_auto_100(x) 'tiempos_blin_auto_potencial_100.dat' using 1:4 via e
fit ob_blin_auto_10000(x) 'tiempos_blin_auto_potencial_10000.dat' using 1:4 via f
plot 'tiempos_bbin_potencial_1.dat' using 1:4 title "OBs BBIN n = 1" ls 7 lc 1 lw 2, \
     'tiempos_bbin_potencial_100.dat' using 1:4 title "OBs BBIN n = 100" ls 7 lc 1 lw 2, \
     'tiempos_bbin_potencial_10000.dat' using 1:4 title "OBs BBIN n = 10000" ls 7 lc 1 lw 2, \
     'tiempos_blin_auto_potencial_1.dat' using 1:4 title "OBs BLIN auto n = 1" ls 7 lc 4 lw 2, \
     'tiempos_blin_auto_potencial_100.dat' using 1:4 title "OBs BLIN auto n = 100" ls 7 lc 5 lw 2, \
     'tiempos_blin_auto_potencial_10000.dat' using 1:4 title "OBs BLIN auto n = 10000" ls 7 lc 6 lw 2, \
     ob_bbin_1(x) lc 1 lw 2, \
     ob_bbin_100(x) lc 2 lw 2, \
     ob_bbin_10000(x) lc 3 lw 2, \
     ob_blin_auto_1(x) lc 4 lw 2, \
     ob_blin_auto_100(x) lc 5 lw 2, \
     ob_blin_auto_10000(x) lc 6 lw 2, \
