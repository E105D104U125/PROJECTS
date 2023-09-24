# Script para la toma de datos de estadísticas de caché

#!/bin/bash

# inicializar variables
P=5 # 12 mod7 
Ninicio=$((1024*(P+1)))
Npaso=256
Nfinal=$((1024*(P+2)))
Tcinicio=1024 #Tamaño inicial caché de datos
Tcmult=2 #Tamaño del salto multiplicativo
Tcfinal=8192 #Tamaño final caché de datos
LLsize=$((2**23)) #Tamaño caché nivel 2 (último nivel)
Lnsize=64 # Tamaño linea

# Ruta relativa de los archivos
SLOW_ROUTE=../FicherosC/slow
FAST_ROUTE=../FicherosC/fast

echo "Running slow and fast..."

# Se iteran todos los tamaños de caché
for ((J = Tcinicio; J <= Tcfinal; J = J*Tcmult)); do
	# Para un tamaño dado
	# Se crea el fichero vacío
	fDAT=cache_$J.dat
	rm -f $fDAT 
	touch $fDAT 
	
	echo "Tamaño caché primer nivel: $J B"
	for N in $(seq $Ninicio $Npaso $Nfinal);do
		echo "	N: $N / $Nfinal..."
		
		valgrind --tool=cachegrind\
			--I1=$J,1,$Lnsize\
			--D1=$J,1,$Lnsize\
			--LL=$LLsize,1,$Lnsize\
			--cachegrind-out-file=ej2_$J-$N-slow.dat ./$SLOW_ROUTE $N &> /dev/null
		
		valgrind --tool=cachegrind\
			--I1=$J,1,$Lnsize\
			--D1=$J,1,$Lnsize\
			--LL=$LLsize,1,$Lnsize\
			--cachegrind-out-file=ej2_$J-$N-fast.dat ./$FAST_ROUTE $N &> /dev/null
			
		# Se cogen los datos del fichero de salida
		echo "$N $(cg_annotate ej2_$J-$N-slow.dat | grep -i "PROGRAM TOTALS"  | awk '{print $5 " " $8}' | tr -d ',')"\
		"$(cg_annotate ej2_$J-$N-fast.dat | grep -i "PROGRAM TOTALS"  | awk '{print $5 " " $8}' | tr -d ',')" >> $fDAT
		
		rm -f ej2_$J-$N-slow.dat
		rm -f ej2_$J-$N-fast.dat
	done
done

# Plotting en script aparte