#!/usr/bin/gnuplot -p

set fit logfile '/dev/null'
set key left
set size ratio 0.5
set term png size 1600, 900
set output "obs_quicksort_src.png"
set title "Número de OBs de QuickSort (src)"
set xlabel "Número de permutaciones"
set ylabel "Número de OBs"
set xrange [990:10100]
set yrange [9500:190000]
set title font ",20"
set tics font ",14"
set xlabel font ",14"
set ylabel font ",14"
avg(x) = a*x*log(a*x)
min(x) = b*x*log(b*x)
max(x) = c*x*log(c*x)
fit avg(x) 'quicksort_src.dat' using 1:3 via a
fit min(x) 'quicksort_src.dat' using 1:4 via b
fit max(x) 'quicksort_src.dat' using 1:5 via c
plot 'quicksort_src.dat' using 1:3 title "OBs medias" ls 7 lc 4 lw 2, \
     'quicksort_src.dat' using 1:4 title "OBs mínimas" ls 7 lc 2 lw 2, \
     'quicksort_src.dat' using 1:5 title "OBs máximas" ls 7 lc 1 lw 2, \
     avg(x) lc 4 lw 2, \
     min(x) lc 2 lw 2, \
     max(x) lc 1 lw 2
