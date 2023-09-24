EXE = stream-ui
EXEP = stream-client
EXEC = stream-server
LIB = -lrt -lpthread

m:
	gcc -c $(EXE).c $(LIB)
	gcc -o $(EXE) $(EXE).o $(LIB)
	gcc -c $(EXEP).c $(LIB)
	gcc -o $(EXEP) $(EXEP).o $(LIB)
	gcc -c $(EXEC).c $(LIB)
	gcc -o $(EXEC) $(EXEC).o $(LIB)

clean:
	rm *.o
	rm stream-ui stream-client stream-server

