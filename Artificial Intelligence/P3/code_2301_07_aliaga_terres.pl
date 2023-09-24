% Ejercicio 2:
sum_pot_prod(_,_,Potencia,_) :- Potencia =< 0, write_log('ERROR 1.1 Potencia.'), !, fail. %Error potencia.
sum_pot_prod([_|_],[],_,_) :- write_log('ERROR 1.2 Longitud.'), !, fail. %Error longitud.
sum_pot_prod([],[_|_],_,_) :- write_log('ERROR 1.2 Longitud.'), !, fail. %Error longitud.
sum_pot_prod([],[],_,0) :- !. %Caso base.
sum_pot_prod([X|Xs],[Y|Ys],Potencia,R) :- Z is (X*Y)**Potencia, sum_pot_prod(Xs,Ys,Potencia,R2), R is Z+R2. %Caso general.

% Ejercicio 3:
penultimo([X,_|[]],X) :- !.
penultimo([_|Xs],Y) :- penultimo(Xs,Y).
segundo_penultimo([],_,_) :- write_log('ERROR 2.1 Longitud.'), !, fail. %Error longitud.
segundo_penultimo([_|[]],_,_) :- write_log('ERROR 2.1 Longitud.'), !, fail. %Error longitud.
segundo_penultimo([_,X,_|[]],X,X) :- !.
segundo_penultimo([A,X|Xs],X,Y) :- penultimo([A,X|Xs],Y).

% Ejercicio 4:
longitud_lista([],0) :- !.
longitud_lista([_|Xs],L) :- longitud_lista(Xs,M), L is 1+M.
sublista_menor(X,Menor,X) :- Menor =:= 1, !.
sublista_menor([_|Xs],Menor,L) :- M2 is Menor-1, sublista_menor(Xs,M2,L).
sublista_mayor(_,Mayor,[]) :- Mayor =:= 0, !.
sublista_mayor([X|Xs],Mayor,[X|L]) :- M2 is Mayor-1, sublista_mayor(Xs,M2,L).
sublista_mayor_menor([X|Y],Menor,Mayor,L) :- sublista_mayor([X|Y],Mayor,LAux), sublista_menor(LAux,Menor,L).
elemento_en_lista([],_) :- write_log('ERROR 3.1 Elemento.'), !, fail. %Error elemento.
elemento_en_lista([E|_],E) :- !.
elemento_en_lista([_|Xs],E) :- elemento_en_lista(Xs,E).
sublista([X|Y],_,Mayor,_,_) :- longitud_lista([X|Y],L), L < Mayor, write_log('ERROR 3.2 Indices.'), !, fail. %Error indices.
sublista(_,Menor,Mayor,_,_) :- Menor > Mayor, write_log('ERROR 3.2 Indices.'), !, fail. %Error indices.
sublista([X|Y],Menor,Mayor,E,L) :- sublista_mayor_menor([X|Y],Menor,Mayor,L), elemento_en_lista(L,E).

% Ejercicio 5:
crear_regilla(A,_,N,A) :- longitud_lista(A, L), L =:= N, !.
crear_regilla([X|Y],Aumento,N,L) :- Xn is X-Aumento, crear_regilla([Xn|[X|Y]],Aumento,N,L).
espacio_lineal(Menor,Mayor,_,_) :- Menor >= Mayor, write_log('ERROR 4.1 Indices.'), !, fail. %Error indices.
espacio_lineal(Menor,Mayor,Numero_elementos,Rejilla) :- Aum is (Mayor-Menor)/(Numero_elementos-1), crear_regilla([Mayor],Aum,Numero_elementos,Rejilla).

% Ejercicio 6:
no_negativos([]) :- !.
no_negativos([X|_]) :- X < 0, write_log('ERROR 5.1 Negativos.'), !, fail. %Error negativos.
no_negativos([_|Xn]) :- no_negativos(Xn).
suma_lista([],0) :- !.
suma_lista([X|Xn],S) :- suma_lista(Xn,S2), S is X+S2.
divide_lista([],_,[]) :- !.
divide_lista([X|Xn],Z,[Xz|Xnz]) :- Xz is X/Z, divide_lista(Xn,Z,Xnz).
normalizar(Distribucion_sin_normalizar,Distribucion) :- no_negativos(Distribucion_sin_normalizar), suma_lista(Distribucion_sin_normalizar, Z), divide_lista(Distribucion_sin_normalizar, Z, Distribucion).

% Ejercicio 7:
positivos([]) :- !.
positivos([X|_]) :- X =< 0, write_log('ERROR 6.1. Divergencia KL no definida.'), !, fail. %Error no positivos.
positivos([_|Xn]) :- positivos(Xn).
distribucion(A) :- suma_lista(A,S), S =\= 1, write_log('ERROR 6.2. Divergencia KL definida solo para distribuciones.'), !, fail. %Error no distribucion.
distribucion(_) :- !.
misma_longitud(D1,D2) :- longitud_lista(D1,L1), longitud_lista(D2,L2), L1 =\= L2, write_log('ERROR 6.3. Distribuciones con distinta longitud.'), !, fail. %Error longitud.
misma_longitud(_,_) :- !.
calcula_divergencia([],[],0) :- !.
calcula_divergencia([X|Xn],[Y|Yn],Div) :- Aux is X/Y, DivAct is X*log(Aux), calcula_divergencia(Xn,Yn,DivN), Div is DivAct+DivN.
divergencia_kl(D1,D2,KL) :- positivos(D1), positivos(D2), distribucion(D1), distribucion(D2), misma_longitud(D1,D2), calcula_divergencia(D1,D2,KL).

% Ejercicio 8:
no_negativos3([]) :- !.
no_negativos3([X|_]) :- X < 0, write_log('ERROR 7.1 Elemento menor que 0.'), !, fail. %Error negativos.
no_negativos3([_|Xn]) :- no_negativos3(Xn).
no_negativos2([]) :- !.
no_negativos2([[X|Y]|Xn]) :- no_negativos3([X|Y]), no_negativos2(Xn). 
elemento_por_lista(_,[],[]) :- !.
elemento_por_lista(E,[X|Xn],[EX|EXn]) :- EX is E*X, elemento_por_lista(E,Xn,EXn).
elemento_por_matriz(_,[],[]) :- !.
elemento_por_matriz(E,[L|Ln],[EL|ELn]) :- elemento_por_lista(E,L,EL), elemento_por_matriz(E,Ln,ELn).
lista_por_matriz([],_,[]) :- !.
lista_por_matriz([E|En],M,[EM|EnM]) :- elemento_por_matriz(E,M,EM), lista_por_matriz(En,M,EnM).
matriz_por_matriz([],_,[]) :- !.
matriz_por_matriz([EL|ELn],M,[ELM|ELnM]) :- lista_por_matriz(EL,M,ELM), matriz_por_matriz(ELn,M,ELnM).
producto_kronecker(Matriz_A,Matriz_B,Matriz_bloques) :- no_negativos2(Matriz_A), no_negativos2(Matriz_B), matriz_por_matriz(Matriz_A,Matriz_B,Matriz_bloques).

% Ejercicio 9:
% Distancia euclidea:
suma_cuadrado_resta([],[],0) :- !.
suma_cuadrado_resta([X1|X1n],[X2|X2n],Suma) :- S is (X1-X2)**2, suma_cuadrado_resta(X1n,X2n,Suman), Suma is S+Suman.
distancia_euclidea(X1,X2,D) :- suma_cuadrado_resta(X1,X2,Suma), D is Suma**(1/2).
% Distancia entre puntos:
distancia_puntos(_,[],[]) :- !.
distancia_puntos(T,[E|En],[TE|TEn]) :- distancia_euclidea(T,E,TE), distancia_puntos(T,En,TEn).
distancia_lista_puntos([],_,[]) :- !.
distancia_lista_puntos([T|Tn],E,[TE|TnE]) :- distancia_puntos(T,E,TE), distancia_lista_puntos(Tn,E,TnE).
calcular_distancias(X_entrenamiento,X_test,Matriz_resultados) :- distancia_lista_puntos(X_test,X_entrenamiento,Matriz_resultados).
% Contadores:
cuenta_elemento(_,[],0) :- !.
cuenta_elemento(E,[E|Xn],Si) :- cuenta_elemento(E,Xn,N), Si is N+1, !.
cuenta_elemento(E,[_|Xn],No) :- cuenta_elemento(E,Xn,No).
contadores([],_,[]) :- !.
contadores([E|En],L,[[N,E]|NEn]) :- cuenta_elemento(E,L,N), contadores(En,L,NEn).
elemento_en_lista2(E,[[_,E]|_]) :- !.
elemento_en_lista2(E,[_|Xn]) :- elemento_en_lista2(E,Xn).
elimina_repetidos([],[]) :- !.
elimina_repetidos([[_,E]|NEn],NNEn) :- elimina_repetidos(NEn,NNEn), elemento_en_lista2(E,NNEn), !.
elimina_repetidos([[Y,X]|NEn],[[Y,X]|NNEn]) :- elimina_repetidos(NEn,NNEn), \+ elemento_en_lista2(X,NNEn).
calcular_contadores(L,Resultado) :- contadores(L,L,Auxiliar), elimina_repetidos(Auxiliar,Resultado).
% Etiqueta mas relevante:
etiqueta_mayor([],[0,0]) :- !.
etiqueta_mayor([[N,E]|NEn],[E,N]) :- etiqueta_mayor(NEn,[_,M]), N > M, !.
etiqueta_mayor([[N,_]|NEn],[F,M]) :- etiqueta_mayor(NEn,[F,M]), M >= N.
calcular_etiqueta_mas_relevante(K_etiquetas,Etiqueta) :- calcular_contadores(K_etiquetas,Contador), etiqueta_mayor(Contador,[Etiqueta,_]).
% K etiquetas mas relevantes:
introduce_en_orden(X,[],[X|[]]) :- !.
introduce_en_orden([X,D],[[Y,Dy]|Yn],[[X,D]|[[Y,Dy]|Yn]]) :- D > Dy, !.
introduce_en_orden([X,D],[[Y,Dy]|Yn],[[Y,Dy]|Res]) :- introduce_en_orden([X,D],Yn,Res).
ordenar2([],[],L,L) :- !.
ordenar2([Y|Yn],[D|Dn],L,LOrdenada) :- introduce_en_orden([Y,D],L,Res), ordenar2(Yn,Dn,Res,LOrdenada).
ordenar([Y|Yn],[D|Dn],Ordenada) :- ordenar2(Yn,Dn,[[Y,D]],Ordenada).
selecciona_K(L,K,L) :- longitud_lista(L,Longitud), Longitud =:= K, !.
selecciona_K([_|Xn],K,L) :- selecciona_K(Xn,K,L).
quita_D([],[]) :- !.
quita_D([[Y,_]|YDn],[Y|Res]) :- quita_D(YDn,Res).
calcular_K_etiquetas_mas_relevantes(Y_entrenamiento,K,Vec_distancias,K_etiquetas) :- ordenar(Y_entrenamiento,Vec_distancias,YOrdenadaConD), quita_D(YOrdenadaConD,YOrdenada), selecciona_K(YOrdenada,K,K_etiquetas).
% Predecir etiqueta de 1 punto:
predecir_etiqueta(Y_entrenamiento,K,Vec_distancias,Etiqueta) :- calcular_K_etiquetas_mas_relevantes(Y_entrenamiento,K,Vec_distancias,KE), calcular_etiqueta_mas_relevante(KE,Etiqueta), !.
% Predecir etiqueta de lista de puntos:
predecir(_,_,[],[]) :- !.
predecir(Y,K,[D|Dn],[YActual|Yn]) :- predecir_etiqueta(Y,K,D,YActual), predecir(Y,K,Dn,Yn).
predecir_etiquetas(Y_entrenamiento,K,Matriz_resultados,Y_test) :- predecir(Y_entrenamiento,K,Matriz_resultados,Y_test).
% Vecinos proximos:
k_vecinos_proximos(X_entrenamiento,Y_entrenamiento,K,X_test,Y_test) :- calcular_distancias(X_entrenamiento,X_test,Distancias), predecir_etiquetas(Y_entrenamiento,K,Distancias,Y_test), !.
% Patrones:
leave_one_out(X_entrenamiento,_,I,_,0) :- longitud_lista(X_entrenamiento,L), L =:= I, !.
leave_one_out(X_entrenamiento,Etiquetas,I,K,Aciertos) :- I2 is I+1, nth0(I,X_entrenamiento,XI,XSinI), nth0(I,Etiquetas,EI,ESinI), calcular_distancias(XSinI,[XI],[Distancias|[]]), predecir_etiqueta(ESinI,K,Distancias,EI), leave_one_out(X_entrenamiento,Etiquetas,I2,K,A), Aciertos is A+1, !.
leave_one_out(X_entrenamiento,Etiquetas,I,K,Aciertos) :- I2 is I+1, leave_one_out(X_entrenamiento,Etiquetas,I2,K,Aciertos).
crear_patrones([],[]) :- !.
crear_patrones([row(A,B,C,D)|Rows],[[A,B,C,D]|PRows]) :- crear_patrones(Rows,PRows).
leer_patrones(Fichero,Patrones) :- csv_read_file(Fichero,Rows,[functor(row)]), crear_patrones(Rows,Patrones), !.
crear_etiquetas([row(E1,E2,E3,E4,E5,E6,E7,E8,E9,E10,E11,E12,E13,E14,E15,E16,E17,E18,E19,E20,E21,E22,E23,E24,E25,E26,E27,E28,E29,E30,E31,E32,E33,E34,E35,E36,E37,E38,E39,E40,E41,E42,E43,E44,E45,E46,E47,E48,E49,E50,E51,E52,E53,E54,E55,E56,E57,E58,E59,E60,E61,E62,E63,E64,E65,E66,E67,E68,E69,E70,E71,E72,E73,E74,E75,E76,E77,E78,E79,E80,E81,E82,E83,E84,E85,E86,E87,E88,E89,E90,E91,E92,E93,E94,E95,E96,E97,E98,E99,E100,E101,E102,E103,E104,E105,E106,E107,E108,E109,E110,E111,E112,E113,E114,E115,E116,E117,E118,E119,E120,E121,E122,E123,E124,E125,E126,E127,E128,E129,E130,E131,E132,E133,E134,E135,E136,E137,E138,E139,E140,E141,E142,E143,E144,E145,E146,E147,E148,E149,E150)],
[E1,E2,E3,E4,E5,E6,E7,E8,E9,E10,E11,E12,E13,E14,E15,E16,E17,E18,E19,E20,E21,E22,E23,E24,E25,E26,E27,E28,E29,E30,E31,E32,E33,E34,E35,E36,E37,E38,E39,E40,E41,E42,E43,E44,E45,E46,E47,E48,E49,E50,E51,E52,E53,E54,E55,E56,E57,E58,E59,E60,E61,E62,E63,E64,E65,E66,E67,E68,E69,E70,E71,E72,E73,E74,E75,E76,E77,E78,E79,E80,E81,E82,E83,E84,E85,E86,E87,E88,E89,E90,E91,E92,E93,E94,E95,E96,E97,E98,E99,E100,E101,E102,E103,E104,E105,E106,E107,E108,E109,E110,E111,E112,E113,E114,E115,E116,E117,E118,E119,E120,E121,E122,E123,E124,E125,E126,E127,E128,E129,E130,E131,E132,E133,E134,E135,E136,E137,E138,E139,E140,E141,E142,E143,E144,E145,E146,E147,E148,E149,E150]) :- !.
leer_etiquetas(Fichero,Etiquetas) :- csv_read_file(Fichero,Rows,[functor(row)]), crear_etiquetas(Rows,Etiquetas), !.
clasifica_patrones('iris_patrones.csv','iris_etiquetas.csv',K,Tasa_aciertos) :- leer_patrones('iris_patrones.csv',Patrones), leer_etiquetas('iris_etiquetas.csv',Etiquetas), leave_one_out(Patrones,Etiquetas,0,K,Aciertos), longitud_lista(Patrones,L), Tasa_aciertos is Aciertos/L, !.
crea_lista_aciertos(I,F,[]) :- I =:= F, !.
crea_lista_aciertos(I,F,[[I,AI]|AIn]) :- I2 is I+1, clasifica_patrones('iris_patrones.csv','iris_etiquetas.csv',I,AI), crea_lista_aciertos(I2,F,AIn).
lista_tasa_aciertos(L1,L2,L3,L4,L5,L6,L7,L8,L9,L10,L11,L12,L13,L14,L15,L16,L17,L18,L19,L20,L21,L22,L23,L24,L25,L26,L27,L28,L29,L30) :- crea_lista_aciertos(1,6,L1), crea_lista_aciertos(6,11,L2), crea_lista_aciertos(11,16,L3), crea_lista_aciertos(16,21,L4), crea_lista_aciertos(21,26,L5), crea_lista_aciertos(26,31,L6), crea_lista_aciertos(31,36,L7), crea_lista_aciertos(36,41,L8), crea_lista_aciertos(41,46,L9), crea_lista_aciertos(46,51,L10), crea_lista_aciertos(51,56,L11), crea_lista_aciertos(56,61,L12), crea_lista_aciertos(61,66,L13), crea_lista_aciertos(66,71,L14), crea_lista_aciertos(71,76,L15), crea_lista_aciertos(76,81,L16), crea_lista_aciertos(81,86,L17), crea_lista_aciertos(86,91,L18),
crea_lista_aciertos(91,96,L19), crea_lista_aciertos(96,101,L20), crea_lista_aciertos(101,106,L21), crea_lista_aciertos(106,111,L22), crea_lista_aciertos(111,116,L23), crea_lista_aciertos(116,121,L24), crea_lista_aciertos(121,126,L25), crea_lista_aciertos(126,131,L26), crea_lista_aciertos(131,136,L27), crea_lista_aciertos(136,141,L28), crea_lista_aciertos(141,146,L29), crea_lista_aciertos(146,150,L30), !.

% Ejercicio 10:
fractal :-
    new(D, window('Fractal')),
    send(D, size, size(800, 600)),
    drawFractal(D, 100, 300, 700, 300, 7),
    send(D, open),!.

drawFractal(D,X1,Y1,X2,Y2,0) :-
    new(Line, line(X1, Y1, X2, Y2, none)),
    send(D, display, Line).

% Se asume que el triángulo que emerge de una línea recta es equilátero
% Y la proyección ortogonal del punto que no se encuentra en el segmento
% al segmento es el punto medio del mismo.
% Se asume también que los lados del triángulo tienen un tercio de la longitud
% inicial del segmento.
drawFractal(D,X1,Y1,X2,Y2,Depth) :-
    % Por convención, X1 =< X2
    % Punto medio
    X is (X1+X2)/2,
    Y is (Y1+Y2)/2,
    % Vector perpendicular al segmento
    Norm is sqrt((X2-X1)**2+(Y2-Y1)**2),
    Vx is -(Y2-Y1)/Norm,
    Vy is (X2-X1)/Norm,
    % Punto de la altura del triángulo.
    H is sqrt((Norm/3)**2-(Norm/6)**2),
    Hx is X - Vx*H,
    Hy is Y - Vy*H,
    % Puntos 1/4, 3/4,
    Px is round(X1/3+2*X2/3),
    Py is round(Y1/3+2*Y2/3),
    Qx is round(2*X1/3+X2/3),
    Qy is round(2*Y1/3+Y2/3),
    NewDepth is Depth-1,
    drawFractal(D,X1,Y1,Qx,Qy,NewDepth),
    drawFractal(D,Qx,Qy,Hx,Hy,NewDepth),
    drawFractal(D,Hx,Hy,Px,Py,NewDepth),
    drawFractal(D,Px,Py,X2,Y2,NewDepth).