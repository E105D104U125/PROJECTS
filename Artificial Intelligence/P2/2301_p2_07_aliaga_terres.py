from re import S
import time

import math

from game import (
  TwoPlayerGameState,
)
from tournament import (
  StudentHeuristic,
)

import reversi

class Solution1(StudentHeuristic): #Solo para tableros 8x8 y 5x7
  def get_name(self) -> str:
    return "posicional_dinamico_caet"

  def evaluation_function(self, state: TwoPlayerGameState) -> float:
    juego = state.game
    yo = state.next_player
    my_label = yo.label
    tablero = state.board
    n = posicionalDinamico(juego, tablero, my_label)
    if not state.is_player_max(yo):
        n = -n
    return n

class Solution2(StudentHeuristic):
  def get_name(self) -> str:
    return "movilidad_y_esquinas_expandidas_caet"

  def evaluation_function(self, state: TwoPlayerGameState) -> float:
    juego = state.game
    yo = state.next_player
    tablero = state.board
    my_label = yo.label
    n = 7*mobility(tablero, my_label, juego)
    n = n + valorEsquinas(tablero, my_label)
    n = n + edgeFunction(tablero, my_label)
    n = n + esquinaExpandida(tablero, my_label)
    if not state.is_player_max(yo):
        n = -n
    return n

class Solution3(StudentHeuristic):
  def get_name(self) -> str:
    return "mega_mix_caet"

  def evaluation_function(self, state: TwoPlayerGameState) -> float:
    juego = state.game
    yo = state.next_player
    tablero = state.board
    my_label = yo.label

    n1 = mobility(tablero, my_label, juego)
    n2 = potentialMobility(juego, tablero, yo)
    n3 = posicionalDinamico(juego, tablero, my_label)
    n4 = maxFichas(tablero, my_label)
    n5 = edgeFunction(tablero, my_label)
    n6 = movilidadAnillo(tablero, my_label)

    n_fichas = len(tablero.keys()) 

    # FASES DEL JUEGO
    # 1 [0-d1] d1 -> 18
    if n_fichas <= 18:
      n = n3 + n6 
    # 2 [d1-33]
    elif n_fichas <= 33:
      n = n5 + 2*n1 + n2 
    # 3 [34-49]
    elif n_fichas <= 50:
      n = n3 + 4*n1 
    # 4 [50-64]
    else:
      n = n1 + n4 
    if not state.is_player_max(yo):
        n = -n
    return n

def valorEsquinas(board, label):
  L = [(1,8), (1,1), (8,1), (8,8)]
  L2 = [(2,7), (2,2), (7,2), (7,7)]
  L3 = [(1,7), (1,2), (8,2), (8,7)]
  L4 = [(2,8), (2,1), (7,1), (7,8)]
  n = 0
  for clave in L:
    index = L.index(clave)
    clave1, clave2, clave3 = L2[index], L3[index], L4[index]
    if clave in board.keys():
      if board[clave] == label:
        n = n + 99
    if clave1 in board.keys():
      if board[clave1] == label:
        n = n - 24
    if (clave2 in board.keys() and board[clave2] == label) or (clave3 in board.keys() and  board[clave3] == label):
        n = n - 8
    
  return n
  
def posicionalDinamico(juego, tablero, label):
  alto = juego.height
  ancho = juego.width
  n = 0
  t = len(tablero.keys())

  if alto == 8 and ancho == 8:
    for clave in tablero.keys():
      if tablero[clave] == label:
        n = n + valorPosicionDinamico8x8(clave, t)
  
  n += (math.sin(t/25 + 0.75)**2)*esquinaExpandida(tablero, label)
  return n

def valorPosicionDinamico8x8(clave, t) -> float: 
  f1 = 40/(t**(2/3)+3) - 2
  f2 = 60/(t**(2/3)+3) - 2.5
  f3 = 45/(t**(1/3)+3) - 3
  f4 = 6*(math.sin(t/40 + 0.75)**2)
  f5 = 7*(math.sin(t/40 + 0.75)**2)
  matriz = [[99,  0,  f5,  f4,  f4,  f5,  0, 99],
            [0, -24, -f5/2, -f4/2, -f4/2, -f5/2, -24, 0],
            [ f5,  -f5/2,  f3,  f2,  f2,  f3, -f5/2,  f5],
            [ f4,  -f4/2,  f2,  f1,  f1,  f2,  -f4/2,  f4],
            [ f4,  -f4/2,  f2,  f1,  f1,  f2,  -f4/2,  f4],
            [ f5,  -f5/2,  f3,  f2,  f2,  f3,  -f5/2,  f5],
            [0, -24, -f5/2, -f4/2, -f4/2, -f5/2, -24, 0],
            [99,  0,  f5,  f4,  f4,  f5,  0, 99]]
  i, j = clave
  i, j = i - 1, j - 1
  return matriz[i][j]

def movilidadAnillo(tablero, label) -> int:
  sumaTotal = 0
  # x x L1 x x
  # x        x
  # L3       L4
  # x        x
  # x x L2 x x

  L1 = [(3+i,7) for i in range(4)]
  L1Edge = [(3+i,8) for i in range(4)]
  L2 = [(3+i,2) for i in range(4)]
  L2Edge = [(3+i,1) for i in range(4)]
  L3 = [(2,3+i) for i in range(4)]
  L3Edge = [(1,3+i) for i in range(4)]
  L4 = [(7,3+i) for i in range(4)]
  L4Edge = [(8,3+i) for i in range(4)]

  # True si yo tengo posesion de las celdas del anillo
  L1tuya = [celda in tablero and tablero[celda] == label for celda in L1]
  L2tuya = [celda in tablero and tablero[celda] == label for celda in L2]
  L3tuya = [celda in tablero and tablero[celda] == label for celda in L3]
  L4tuya = [celda in tablero and tablero[celda] == label for celda in L4]
  
  # True si el enemigo tiene posesion de los bordes adyacentes al anillo
  L1suya = [celda in tablero and tablero[celda] != label for celda in L1Edge]
  L2suya = [celda in tablero and tablero[celda] != label for celda in L2Edge]
  L3suya = [celda in tablero and tablero[celda] != label for celda in L3Edge]
  L4suya = [celda in tablero and tablero[celda] != label for celda in L4Edge]
  
  L = [(L1, L1tuya, L1suya), (L2, L2tuya, L2suya), (L3, L3tuya, L3suya), (L4, L4tuya, L4suya)]
  # Penaliza tener fichas en el anillo en relacion a las fichas que el contrario posee
  for LAux in L:
    (_, Tuya, Suya) = LAux
    sumaTotal -= Tuya.count(True)*Suya.count(False)

  return sumaTotal

def edgeFunction(tablero, label) -> int:
  L, L2 = [], []
  i, sumaTotal = 0, 0
  
  # Consigue todas las celdas adyacentes en una direccion
  # Esquinas derecha, direccion izquierda
  for corner in [(8,1), (8,8)]:
    LAux = [(corner[0]-(i+1),corner[1]) for i in range(7)]
    if corner in tablero and tablero[corner] == label:
      L.append(LAux)
    # La esquina no es tuya pero la adyacente si. Mala posicion
    else:
      L2.append(LAux)

  # Esquinas izquierda, direccion derecha
  for corner in [(1,1), (1,8)]:
    LAux = [(corner[0]+(i+1),corner[1]) for i in range(7)]
    if corner in tablero and tablero[corner] == label:
      L.append(LAux)
    # La esquina no es tuya pero la adyacente si. Mala posicion
    else:
      L2.append(LAux)
  
  # Esquinas arriba, direccion abajo
  for corner in [(1,8), (8,8)]:
    LAux = [(corner[0],corner[1]-(i+1)) for i in range(7)]
    if corner in tablero and tablero[corner] == label:
      L.append(LAux)
    # La esquina no es tuya pero la adyacente si. Mala posicion
    else:
      L2.append(LAux)
  
  # Esquinas abajo, direccion arriba
  for corner in [(1,1), (8,1)]:
    LAux = [(corner[0],corner[1]+(i+1)) for i in range(7)]
    if corner in tablero and tablero[corner] == label:
      L.append(LAux)
    # La esquina no es tuya pero la adyacente si. Mala posicion
    else:
      L2.append(LAux)
  
  # Gradiente de puntuacion a lo largo de una esquina
  for LAux in L:
    aux, i = 0, 0
    while i < 7:
      key = LAux[i]
      if key in tablero and tablero[key] == label:
        aux += 20-2*i
      else:
        break
      i += 1
    sumaTotal += aux

  for LAux in L2:
    aux, i = 0, 0
    while i < 7:
      key = LAux[i]
      if key in tablero and tablero[key] == label:
        aux += 15-1.5*i
      else:
        break
      i += 1
    sumaTotal -= aux

  return sumaTotal

def esquinaExpandida(tablero, label) -> int:
  L = [(1,8), (1,7), (2,8), (1,6), (3,8), (2,7)]
  L2 = [(1,1), (1,2), (2,1), (1,3), (3,1), (2,2)]
  L3 = [(8,8), (8,7), (7,8), (8,6), (6,8), (7,7)]
  L4 = [(8,1), (8,2), (7,1), (8,3), (6,1), (7,2)]

  LTotal = [L, L2, L3, L4]

  sumaTotal = 0

  for LAux in LTotal:
    flag = False
    for i in range(3):
      if LAux[i] not in tablero or (LAux[i] in tablero and tablero[LAux[i]] != label):
        flag = True
        break
    if flag == True:
      continue
    for j in [3,4]:
      if LAux[j] not in tablero:
        flag = True
        break
    if flag == True:
      continue
    if LAux[5] in tablero and tablero[LAux[5]] == label:
      sumaTotal += 123
  return sumaTotal

def maxFichas(tablero, label):
  n = 0
  for l in tablero.values():
    if l == label:
      n = n + 1
  
  return n

def mobility(tablero, label, juego) -> int:
  return len(juego._get_valid_moves(tablero, label))

def potentialMobility(juego, tablero, yo) -> int:
  p_mobility = 0
  p_opponent = juego.opponent(yo)
  op_label = p_opponent.label
  for key in tablero.keys():
    c = 0
    if tablero[key] == op_label:
      # o x o
      # o - o
      # o x o
      for j in [-1,0,1]:
        for i in [-1,1]: 
          key_aux = key
          key_aux += (i,j)
          c += (1 if key_aux not in tablero else 0)
      # x o x
      # x - x
      # x o x            
      for i in [1,-1]:
        key_aux = key
        key_aux += (i,i)
        c+= (1 if key_aux not in tablero else 0)
    p_mobility += c
  return p_mobility