# Ejercicio 3
# Multiplicación de matrices

#!/bin/bash

# inicializar variables
P=5 # 12 mod7 
Ninicio=$((256*(P+1)))
Npaso=32
Nfinal=$((256*(P+2)))
Nveces=$((256/Npaso+1))
Nrep=10

fDAT=mult.dat

# Ruta relativa de los archivos
NORMAL_MULT=multiplicacion_normal
TRANS_MULT=multiplicacion_transpuesta

# borrar el fichero DAT
rm -f $fDAT

# generar el fichero DAT vacío
touch $fDAT

echo "Running..."

# Inicializacion
for J in $(seq 0 1 $(($Nveces-1))); do
	normalTime[$J]=0
	transTime[$J]=0
done

for J in $(seq 1 1 $Nrep); do
	echo "Repeticion: $J / $Nrep"
	
	# Multiplicación normal
	echo "Multiplicación normal..."
	for N in $(seq $Ninicio $Npaso $Nfinal);do
		
		echo "N: $N / $Nfinal..."
		normalTime[$((($N-$Ninicio)/$Npaso))]=$(echo "scale=8; ${normalTime[$((N/Ninicio - 1))]}+$(./$NORMAL_MULT $N | grep 'time' | awk '{print $3}')" | bc)
	done
	
	# Multiplicación transpuesta
	echo "Multiplicación transpuesta..."
	for N in $(seq $Ninicio $Npaso $Nfinal);do
		
		echo "N: $N / $Nfinal..."
		transTime[$((($N-$Ninicio)/$Npaso))]=$(echo "scale=8; ${transTime[$((N/Ninicio - 1))]}+$(./$TRANS_MULT $N | grep 'time' | awk '{print $3}')" | bc)
	done
done

# Medias
for J in $(seq 0 1 $((Nveces-1))); do
	normalTime[$J]=$(echo "scale=8; ${normalTime[$J]}/$Nveces" | bc)
	transTime[$J]=$(echo "scale=8; ${transTime[$J]}/$Nveces" | bc)
done

# Fallos de caché para los valores de caché asignados por defecto
for N in $(seq $Ninicio $Npaso $Nfinal);do
	J=$(((N-Ninicio)/Npaso))
	
	valgrind --tool=cachegrind\
		--cachegrind-out-file=ej3_$N-normal.dat ./$NORMAL_MULT $N &> /dev/null
	
	valgrind --tool=cachegrind\
		--cachegrind-out-file=ej3_$N-trans.dat ./$TRANS_MULT $N &> /dev/null
		
	# Se cogen los datos del fichero de salida
	echo "$N ${normalTime[$J]} $(cg_annotate ej3_$N-normal.dat | grep -i "PROGRAM TOTALS"  | awk '{print $5 " " $8}' | tr -d ',')"\
	"${transTime[$J]} $(cg_annotate ej3_$N-trans.dat | grep -i "PROGRAM TOTALS"  | awk '{print $5 " " $8}' | tr -d ',')"  >> $fDAT
	
	rm -f ej3_$N-normal.dat
	rm -f ej3_$N-trans.dat
done
