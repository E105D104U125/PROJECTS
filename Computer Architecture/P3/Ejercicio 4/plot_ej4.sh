# Plotting ejercicio 3 - tiempos de ejecución

#!/bin/bash

fDAT=mult.dat
fPNG1=vias_fallos_lectura.png
fPNG2=vias_fallos_escritura.png
fPNG3=word_fallos_lectura.png
fPNG4=word_fallos_escritura.png

Ainicio=1 #Cantidad inicial vías de asociatividad
Apaso=2 #Cantidad del salto vías de asociatividad
Afinal=8 #Cantidad final vías de asociatividad
# Tamaño de línea
Linicio=32 #Tamaño inicial palabra de caché
Lpaso=2 #Tamaño del salto palabra de caché
Lfinal=256 #Tamaño final palabra de caché
RangoVias=""
RangoWord=""
P=5 #12 mod 7
Ninicio=$((256*(P+1)))
Nfinal=$((256*(P+2)))

# borrar los ficheros PNG
rm -f $fPNG1 $fPNG2 $fPNG3 $fPNG4

# Se iteran todos los tamaños de vía
for ((J = Ainicio; J <= Afinal; J = J*Apaso)); do
	RangoVias+=" $J" 
done
# Elimina el espacio inicial
RangoVias=${RangoVias:1:${#RangoVias}-1}
RangoVias+="\""
RangoVias="\""$RangoVias;

# Se iteran todos los tamaños de palabra
for ((J = Linicio; J <= Lfinal; J = J*Lpaso)); do
	RangoWord+=" $J" 
done
# Elimina el espacio inicial
RangoWord=${RangoWord:1:${#RangoWord}-1}
RangoWord+="\""
RangoWord="\""$RangoWord;

# Fallos de lectura en función de las vías
echo "Plotting fallos de lectura en función de las vías..."
gnuplot << END_GNUPLOT
# Tamaño y nombre del fichero de salida
set size ratio 0.5
set term pngcairo size 1600, 900
set output "$fPNG1"
set key left top
set grid

# Título y labels
set title "Fallos caché de lectura en función de las vías"
set ylabel "Nº fallos"
set xlabel "Matrix Size"

# Rango
set xrange [$((Ninicio - 10)):$((Nfinal + 10))]

# Tamaño de fuentes
set title font ",20"
set tics font ",14"
set xlabel font ",14"
set ylabel font ",14"

plot for [i in $RangoVias] 'cache_'.i.'-vias.dat' using 1:2 with lines lw 2 dt 2 title 'cache vias '.i.' normal',\
for [i in $RangoVias] 'cache_'.i.'-vias.dat' using 1:4 with lines lw 2 title 'cache vias '.i.' trans'

END_GNUPLOT
fDAT=cache_$Lnsize-word.dat
fDAT=cache_$Vias-vias.dat

# Fallos de escritura en función de las vías
echo "Plotting fallos de escritura en función de las vías..."
gnuplot << END_GNUPLOT
# Tamaño y nombre del fichero de salida
set size ratio 0.5
set term pngcairo size 1600, 900
set output "$fPNG2"
set key left top
set grid

# Título y labels
set title "Fallos caché de escritura en función de las vías"
set ylabel "Nº fallos"
set xlabel "Matrix Size"

# Rango
set xrange [$((Ninicio - 10)):$((Nfinal + 10))]

# Tamaño de fuentes
set title font ",20"
set tics font ",14"
set xlabel font ",14"
set ylabel font ",14"

plot for [i in $RangoVias] 'cache_'.i.'-vias.dat' using 1:3 with lines lw 2 dt 2 title 'cache vias '.i.' normal',\
for [i in $RangoVias] 'cache_'.i.'-vias.dat' using 1:5 with lines lw 2 title 'cache vias '.i.' trans'
quit

END_GNUPLOT

# Fallos de lectura en función de la longitud de palabra
echo "Plotting fallos de lectura en función de la longitud de palabra..."
gnuplot << END_GNUPLOT
# Tamaño y nombre del fichero de salida
set size ratio 0.5
set term pngcairo size 1600, 900
set output "$fPNG3"
set key left top
set grid

# Título y labels
set title "Fallos caché de lectura  en función de la palabra"
set ylabel "Nº fallos"
set xlabel "Matrix Size"

# Rango
set xrange [$((Ninicio - 10)):$((Nfinal + 10))]

# Tamaño de fuentes
set title font ",20"
set tics font ",14"
set xlabel font ",14"
set ylabel font ",14"

plot for [i in $RangoWord] 'cache_'.i.'-word.dat' using 1:2 with lines lw 2 dt 2 title 'cache vias '.i.' normal',\
for [i in $RangoWord] 'cache_'.i.'-word.dat' using 1:4 with lines lw 2 title 'cache vias '.i.' trans'

END_GNUPLOT
fDAT=cache_$Lnsize-word.dat
fDAT=cache_$Vias-vias.dat

# Fallos de escritura en función de la longitud de palabra
echo "Plotting fallos de escritura en función de la longitud de palabra..."
gnuplot << END_GNUPLOT
# Tamaño y nombre del fichero de salida
set size ratio 0.5
set term pngcairo size 1600, 900
set output "$fPNG4"
set key left top
set grid

# Título y labels
set title "Fallos caché de escritura  en función de la palabra"
set ylabel "Nº fallos"
set xlabel "Matrix Size"

# Rango
set xrange [$((Ninicio - 10)):$((Nfinal + 10))]

# Tamaño de fuentes
set title font ",20"
set tics font ",14"
set xlabel font ",14"
set ylabel font ",14"

plot for [i in $RangoWord] 'cache_'.i.'-word.dat' using 1:3 with lines lw 2 dt 2 title 'word  size '.i.' normal',\
for [i in $RangoWord] 'cache_'.i.'-word.dat' using 1:5 with lines lw 2 title 'word size '.i.' trans'
quit

END_GNUPLOT

