#!/usr/bin/gnuplot -p

set fit logfile '/dev/null'
set key left
set size ratio 0.5
set term png size 1600, 900
set output "time_mergesort.png"
set title "Tiempo medio de ejecucion MergeSort"
set xlabel "Número de permutaciones"
set ylabel "Tiempo (ms)"
set xrange [990:10100]
set yrange [0:1]
set title font ",20"
set tics font ",14"
set xlabel font ",14"
set ylabel font ",14"
t_real(x) = a*x*log(b*x)
t_teorico(x) = x*log(x)
fit t_real(x) 'mergesort.dat' using 1:2 via a,b
plot 'mergesort.dat' using 1:2 title "Tiempo medio quicksort" ls 7 lc 4 lw 2, \
     t_real(x) lc 4 lw 2, \
     t_teorico(x) lc 2 lw 2
