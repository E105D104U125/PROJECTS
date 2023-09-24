# Ejercicio 4 - Script para la toma de estadísticas de de caché

#!/bin/bash

# inicializar variables
P=5 # 12 mod7 
Ninicio=$((256*(P+1)))
Npaso=32
Nfinal=$((256*(P+2)))
# Vías de asociatividad
Ainicio=1 #Cantidad inicial vías de asociatividad
Apaso=2 #Cantidad del salto vías de asociatividad
Afinal=8 #Cantidad final vías de asociatividad
# Tamaño de línea
Linicio=32 #Tamaño inicial palabra de caché
Lpaso=2 #Tamaño del salto palabra de caché
Lfinal=256 #Tamaño final palabra de caché

LLsize=$((2**23)) #Tamaño caché nivel 2 (último nivel)
Lnsize=64 # Tamaño linea
FLsize=2048 # Tamaño caché primer nivel

# Ruta relativa de los archivos
NORMAL_MULT="../Ejercicio 3/multiplicacion_normal"
TRANS_MULT="../Ejercicio 3/multiplicacion_transpuesta"

# Para cada cantidad de vías de asociatividad
for ((Vias=Ainicio; Vias<=Afinal; Vias=Vias*Apaso)); do
	# Para un tamaño dado
	# Se crea el fichero vacío
	fDAT=cache_$Vias-vias.dat
	rm -f $fDAT 
	touch $fDAT 
	
	echo "Tamaño vías de asociatividad: $Vias"
	for N in $(seq $Ninicio $Npaso $Nfinal);do
		echo "	N: $N / $Nfinal..."
		fDAT1=ej4_$Vias-$N-vias-normal.dat
		fDAT2=ej4_$Vias-$N-vias-trans.dat
		
		valgrind --tool=cachegrind\
			--I1=$FLsize,$Vias,$Lnsize\
			--D1=$FLsize,$Vias,$Lnsize\
			--LL=$LLsize,1,$Lnsize\
			--cachegrind-out-file=$fDAT1 ./"$NORMAL_MULT" $N &> /dev/null
		
		valgrind --tool=cachegrind\
			--I1=$FLsize,$Vias,$Lnsize\
			--D1=$FLsize,$Vias,$Lnsize\
			--LL=$LLsize,1,$Lnsize\
			--cachegrind-out-file=$fDAT2 ./"$TRANS_MULT" $N &> /dev/null
			
		# Se cogen los datos del fichero de salida
		echo "$N $(cg_annotate $fDAT1 | grep -i "PROGRAM TOTALS"  | awk '{print $5 " " $8}' | tr -d ',')"\
		"$(cg_annotate $fDAT2 | grep -i "PROGRAM TOTALS"  | awk '{print $5 " " $8}' | tr -d ',')" >> $fDAT
		rm -f $fDAT1 $fDAT2
	done
done

# Para cada tamaño de línea
for ((Lnsize=Linicio; Lnsize<=Lfinal; Lnsize=Lnsize*Lpaso)); do
	# Para un tamaño dado
	# Se crea el fichero vacío
	fDAT=cache_$Lnsize-word.dat
	rm -f $fDAT 
	touch $fDAT 
	
	echo "Tamaño palabra de caché: $Lnsize"
	for N in $(seq $Ninicio $Npaso $Nfinal);do
		echo "	N: $N / $Nfinal..."
		fDAT1=ej4_$Lnsize-$N-w-normal.dat
		fDAT2=ej4_$Lnsize-$N-w-trans.dat
		
		valgrind --tool=cachegrind\
			--I1=$FLsize,1,$Lnsize\
			--D1=$FLsize,1,$Lnsize\
			--LL=$LLsize,1,$Lnsize\
			--cachegrind-out-file=$fDAT1 ./"$NORMAL_MULT" $N &> /dev/null
		
		valgrind --tool=cachegrind\
			--I1=$FLsize,1,$Lnsize\
			--D1=$FLsize,1,$Lnsize\
			--LL=$LLsize,1,$Lnsize\
			--cachegrind-out-file=$fDAT2 ./"$TRANS_MULT" $N &> /dev/null
			
		# Se cogen los datos del fichero de salida
		echo "$N $(cg_annotate $fDAT1 | grep -i "PROGRAM TOTALS"  | awk '{print $5 " " $8}' | tr -d ',')"\
		"$(cg_annotate $fDAT2 | grep -i "PROGRAM TOTALS"  | awk '{print $5 " " $8}' | tr -d ',')" >> $fDAT
		rm -f $fDAT1 $fDAT2
	done
done

# Plotting en script aparte