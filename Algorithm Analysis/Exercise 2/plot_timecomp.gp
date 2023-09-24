#!/usr/bin/gnuplot -p

set fit logfile '/dev/null'
set key left
set size ratio 0.5
set term png size 1600, 900
set output "comp_tiempos_quicksort.png"
set title "Tiempos de ejecucion"
set xlabel "Número de permutaciones"
set ylabel "Número de OBs"
set xrange [990:10100]
set yrange [0:1]
set title font ",20"
set tics font ",14"
set xlabel font ",14"
set ylabel font ",14"
t_qs(x) = a*x*log(b*x)+c*x
t_qs_src(x) = d*x*log(e*x)+f*x
fit t_qs(x) 'quicksort.dat' using 1:2 via a,b,c
fit t_qs_src(x) 'quicksort_src.dat' using 1:2 via d,e,f
plot 'quicksort.dat' using 1:2 title "Tiempo medio quicksort" ls 7 lc 4 lw 2, \
     'quicksort_src.dat' using 1:2 title "Tiempo medio quicksort src" ls 7 lc 2 lw 2, \
     t_qs(x) lc 4 lw 2, \
     t_qs_src(x) lc 2 lw 2
