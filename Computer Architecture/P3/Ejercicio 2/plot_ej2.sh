# Plotting ejercicio 2 - Estadísticas de caché

#!/bin/bash

# inicializar variables
P=5 # 12 mod7 
Tcinicio=1024 #Tamaño inicial caché de datos
Tcmult=2 #Tamaño del salto multiplicativo
Tcfinal=8192 #Tamaño final caché de datos
RangoCache="" # Almacena el rango de tamaño de caché

# Imagenes
fPNGL=cache_lectura.png
fPNGE=cache_escritura.png

# borrar el fichero DAT y el fichero PNG
rm -f $fPNGL $fPNGE

# Se iteran todos los tamaños de caché
for ((J = Tcinicio; J <= Tcfinal; J = J*Tcmult)); do
	RangoCache+=" $J" 
done

# Elimina el espacio inicial
RangoCache=${RangoCache:1:${#RangoCache}-1}
RangoCache+="\""
RangoCache="\""$RangoCache;

# Plotting de fallos de lectura
echo "Plotting fallos de lectura..."
gnuplot << END_GNUPLOT
# Tamaño y nombre del fichero de salida
set size ratio 0.5
set term pngcairo size 1600, 900
set output "$fPNGL"
set key left top
set grid

# Título y labels
set title "Fallos lectura"
set ylabel "Nº fallos"
set xlabel "Matrix Size"

# Rango
set xrange [$((1024*(P+1) - 10)):$((1024*(P+2) + 10))]

# Tamaño de fuentes
set title font ",20"
set tics font ",14"
set xlabel font ",14"
set ylabel font ",14"

plot for [i in $RangoCache] 'cache_'.i.'.dat' using 1:2 with lines lw 2 dt 2 title 'cache tam '.i.' slow',\
for [i in $RangoCache] 'cache_'.i.'.dat' using 1:4 with lines lw 2 title 'cache tam '.i.' fast'

quit

END_GNUPLOT


# Plotting de fallos de escritura
echo "Plotting fallos de escritura..."
gnuplot << END_GNUPLOT
# Tamaño y nombre del fichero de salida
set size ratio 0.5
set term pngcairo size 1600, 900
set output "$fPNGE"
set key left top
set grid

# Título y labels
set title "Fallos escritura"
set ylabel "Nº fallos"
set xlabel "Matrix Size"

# Rango
set xrange [$((1024*(P+1) - 10)):$((1024*(P+2) + 10))]

# Tamaño de fuentes
set title font ",20"
set tics font ",14"
set xlabel font ",14"
set ylabel font ",14"

plot for [i in $RangoCache] 'cache_'.i.'.dat' using 1:3 with lines lw 2 dt 2 title 'cache tam '.i.' slow',\
for [i in $RangoCache] 'cache_'.i.'.dat' using 1:5 with lines lw 2 title 'cache tam '.i.' fast'

quit

END_GNUPLOT
