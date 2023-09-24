MAIN = miner
PIRATA = miner_pirata
LIB = -lpthread -lrt
MONITOR = monitor
CLEAR = clear

m: miner.c monitor.c
	gcc -c $(MAIN).c $(LIB)
	gcc -o $(MAIN) $(MAIN).o $(LIB)
	gcc -c $(MONITOR).c $(LIB)
	gcc -o $(MONITOR) $(MONITOR).o $(LIB)
	gcc -c $(PIRATA).c $(LIB)
	gcc -o $(PIRATA) $(PIRATA).o $(LIB)

clear:
	gcc -c $(CLEAR).c $(LIB)
	gcc -o $(CLEAR) $(CLEAR).o $(LIB)

clean:
	rm -f *.o $(MAIN) $(MONITOR) $(PIRATA) *.txt
