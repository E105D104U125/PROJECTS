% Ejercicio 3
segundo_penultimo([_],_,_) :- write_log("ERROR 2.1 Longitud."), !, fail. %Error
segundo_penultimo([],_,_) :- write_log("ERROR 2.1 Longitud."), !, fail. %Error
segundo_penultimo([A,B], Segundo, Penultimo) :- Segundo is B, Penultimo is A, !.
segundo_penultimo([A,_|[]],_,Penultimo) :- Penultimo is A, !.
segundo_penultimo([_,B|L1],Segundo, Penultimo) :- Segundo is B, segundo_penultimo([B|L1],_,Penultimo).

% Ejercicio 5
espacio_lineal(Menor,Mayor,_,_) :- Menor > Mayor, write_log('ERROR 4.1 Indices.'), !, fail.
espacio_lineal(Menor,Mayor,N,_) :- Menor =:= Mayor, N > 1, write_log('ERROR 4.1 Indices.'), !, fail.
espacio_lineal(X,Y,1,[]) :- Z is Y/1, Z=:=X, !.
espacio_lineal(Menor,Mayor,N,[E|Rejilla]) :- E is Menor+(Mayor-Menor)/(N-1), N2 is N-1, espacio_lineal(E, Mayor, N2, Rejilla).

% Ejercicio 10
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