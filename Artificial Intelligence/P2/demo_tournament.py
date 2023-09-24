"""Illustration of tournament.

Authors:
    Alejandro Bellogin <alejandro.bellogin@uam.es>

"""

from __future__ import annotations  # For Python 3.7

import numpy as np

from game import Player, TwoPlayerGameState, TwoPlayerMatch
from heuristic import simple_evaluation_function
from reversi import (
    Reversi,
    from_array_to_dictionary_board,
    from_dictionary_to_array_board,
)
from tournament import StudentHeuristic, Tournament

#Soluciones propias___________________________________________________________________________
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

#http://samsoft.org.uk/reversi/strategy.htm 

class Solution1(StudentHeuristic):
  def get_name(self) -> str:
    return "max_fichas_v2_caet"

  def evaluation_function(self, state: TwoPlayerGameState) -> float:
    yo = state.next_player
    my_label = yo.label
    tablero = state.board
    n = 4*maxFichas(tablero, my_label)
    n = n + valorEsquinas(tablero, my_label)
    n = n + edgeFunction(tablero, my_label)
    n = n + esquinaExpandida(tablero, my_label)
    if not state.is_player_max(yo):
        n = -n
    return n

class Solution2(StudentHeuristic): #Solo para tableros 8x8 y 5x7
  def get_name(self) -> str:
    return "posicional_v2_caet"

  def evaluation_function(self, state: TwoPlayerGameState) -> float:
    juego = state.game
    yo = state.next_player
    my_label = yo.label
    tablero = state.board
    n = posicionalDinamico(juego, tablero, my_label)
    if not state.is_player_max(yo):
        n = -n
    return n

class Solution3(StudentHeuristic):
  def get_name(self) -> str:
    return "movilidad_v3_caet"

  def evaluation_function(self, state: TwoPlayerGameState) -> float:
    juego = state.game
    yo = state.next_player
    tablero = state.board
    my_label = yo.label
    n = 7*movilidad(juego, state)
    n = n + valorEsquinas(tablero, my_label)
    n = n + edgeFunction(tablero, my_label)
    n = n + esquinaExpandida(tablero, my_label)
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
    
  #valor maximo es 396
  return n

def movilidad(juego, estado):
  n = len(juego.generate_successors(estado))
  #valor maximo 15
  return n

def posicional(juego, tablero, label):
  alto = juego.height
  ancho = juego.width
  n = 0

  if alto == 8 and ancho == 8:
    for clave in tablero.keys():
      if tablero[clave] == label:
        n = n + valorPosicion8x8(clave)
  #valor maximo es 568
  return n

def valorPosicion8x8(clave):
  matriz = [[99,  -8,  8,  6,  6,  8,  -8, 99],
            [-8, -24, -4, -3, -3, -4, -24, -8],
            [ 8,  -4,  7,  4,  4,  7,  -4,  8],
            [ 6,  -3,  4,  0,  0,  4,  -3,  6],
            [ 6,  -3,  4,  0,  0,  4,  -3,  6],
            [ 8,  -4,  7,  4,  4,  7,  -4,  8],
            [-8, -24, -4, -3, -3, -4, -24, -8],
            [99,  -8,  8,  6,  6,  8,  -8, 99]]
  i, j = clave
  i, j = i - 1, j - 1
  return matriz[i][j]
  
def posicionalDinamico(juego, tablero, label):
  alto = juego.height
  ancho = juego.width
  n = 0
  t = len(tablero.keys())

  if alto == 8 and ancho == 8:
    for clave in tablero.keys():
      if tablero[clave] == label:
        n = n + valorPosicionDinamico8x8(clave, t)
  
  #n += (math.sin(t/20)**2)*edgeFunction(tablero, label)
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

# Movilidad en el anillo de-3 y -4
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
  #valor maximo es 64
  return n

#-----------------------------------------------------------------------
"""
class Solution4(StudentHeuristic):
  def get_name(self) -> str:
    return "mix_caet"

  def evaluation_function(self, state: TwoPlayerGameState) -> float:
    juego = state.game
    yo = state.next_player
    tablero = state.board
    my_label = yo.label

    n1 = mobility(tablero, my_label, juego) / 15
    #n1 = juego._choice_diff(tablero) / 100
    n2 = potentialMobility(juego, tablero, yo)
    n3 = valorEsquinas(tablero, my_label)
    n4 = posicionalDinamico(juego, tablero, my_label)
    n5 = maxFichas(tablero, my_label)
    n6 = edgeFunction(tablero, my_label)
    n7 = movilidadAnillo(tablero, my_label)

    n_fichas = len(tablero.keys()) 

    d1 = 18 #puede cambiar
    # FASES DEL JUEGO
    # 1 [0-d1] d1 -> 18
    if n_fichas <= d1:
      n = n4 + n7 #Teniamos aqui n3, pero es mejor n4 ya que al principio n3 vale 0 casi siempre
    # 2 [d1-33]
    elif n_fichas <= 33:
      n = n6 + 2*n1 + n2 #+ 4*n1 + n2 pelea por bordes (aun por ver evaluacion)
    # 3 [34-49]
    elif n_fichas <= 50:
      n = n4 + 4*n1 # esquinas e intentar dejar pocas opciones al rival
    # 4 [50-64]
    else:
      n = n1 + n5 # ??? 
    if not state.is_player_max(yo):
        n = -n
    return n
"""

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

def borderFightTotal(tablero, label) -> int:
  listaAnillo = [2,3,4,5,6,7]
  suma = 0
  for clave in tablero.keys():
    if tablero[clave] == label:
      suma += matrizBorderFight(clave)
  for i in listaAnillo:
    LAux = [(2,i), (i,2), (7,i), (i,7)]
    for casilla in LAux:
      if casilla in tablero.keys() and tablero[casilla] == label:
        suma += funcionAnillo(casilla, tablero, label)
  return suma

def matrizBorderFight(clave):
  matriz = [[99,   0,  0,  0,  0,  0,   0, 99],
            [ 0,   0,  0,  0,  0,  0,   0,  0],
            [ 0,   0,  7,  4,  4,  7,   0,  0],
            [ 0,   0,  4,  1,  1,  4,   0,  0],
            [ 0,   0,  4,  1,  1,  4,   0,  0],
            [ 0,   0,  7,  4,  4,  7,   0,  0],
            [ 0,   0,  0,  0,  0,  0,   0,  0],
            [99,   0,  0,  0,  0,  0,   0, 99]]
  i, j = clave
  i, j = i - 1, j - 1
  return matriz[i][j]

def funcionAnillo(casilla, tablero, label):
  LAux1 = [3,4,5,6,7,8]
  LAux2 = [6,5,4,3,2,1]
  DiagEq = [(1,1),(2,2),(3,3),(4,4),(5,5),(6,6),(7,7),(8,8)]
  DiagDi = [(8,1),(7,2),(6,3),(5,4),(4,5),(3,6),(2,7),(1,8)]
  LCuidado = [(2,2),(2,7),(7,7),(7,2)]
  suma = 0
  i,j = casilla
  #Hay mucho codigo pero cada if i==2, i==7, j==2 y j==7 son simetricos
  if i == 2:
    if (1,j) in tablero.keys():
      if tablero[(1,j)] != label:
        suma -= 10
      else:
        suma += 10
        for k in LAux1:
          if (k,j) in tablero.keys() and tablero[(k,j)] == label:
            suma += 1 + 1.5*LAux1.index(k)
          else:
            break
    else:
      sumaAux = 0
      for k in LAux1:
        if (k,j) not in tablero.keys():
          suma += sumaAux
          break
        elif tablero[(k,j)] == label:
          sumaAux += 1 + 1.5*LAux1.index(k)
        else:
          suma -= sumaAux
          break
    if (3,j-1) in tablero.keys() and tablero[(3,j-1)] != label:
      suma -= 5
    elif (3,j+1) in tablero.keys() and tablero[(3,j+1)] != label:
      suma -= 5
  if i == 7:
    if (8,j) in tablero.keys():
      if tablero[(8,j)] != label:
        suma -= 10
      else:
        suma += 10
        for k in LAux2:
          if (k,j) in tablero.keys() and tablero[(k,j)] == label:
            suma += 1 + 1.5*LAux2.index(k)
          else:
            break
    else:
      sumaAux = 0
      for k in LAux2:
        if (k,j) not in tablero.keys():
          suma += sumaAux
          break
        elif tablero[(k,j)] == label:
          sumaAux += 1 + 1.5*LAux2.index(k)
        else:
          suma -= sumaAux
          break
    if (6,j-1) in tablero.keys() and tablero[(6,j-1)] != label:
      suma -= 5
    elif (6,j+1) in tablero.keys() and tablero[(6,j+1)] != label:
      suma -= 5
  if j == 2:
    if (i,1) in tablero.keys():
      if tablero[(i,1)] != label:
        suma -= 10
      else:
        suma += 10
        for k in LAux1:
          if (i,k) in tablero.keys() and tablero[(i,k)] == label:
            suma += 1 + 1.5*LAux1.index(k)
          else:
            break
    else:
      sumaAux = 0
      for k in LAux1:
        if (i,k) not in tablero.keys():
          suma += sumaAux
          break
        elif tablero[(i,k)] == label:
          sumaAux += 1 + 1.5*LAux1.index(k)
        else:
          suma -= sumaAux
          break
    if (i-1,3) in tablero.keys() and tablero[(i-1,3)] != label:
      suma -= 5
    elif (i+1,3) in tablero.keys() and tablero[(i+1,3)] != label:
      suma -= 5
  if j == 7:
    if (i,8) in tablero.keys():
      if tablero[(i,8)] != label:
        suma -= 10
      else:
        suma += 10
        for k in LAux2:
          if (i,k) in tablero.keys() and tablero[(i,k)] == label:
            suma += 1 + 1.5*LAux2.index(k)
          else:
            break
    else:
      sumaAux = 0
      for k in LAux2:
        if (i,k) not in tablero.keys():
          suma += sumaAux
          break
        elif tablero[(i,k)] == label:
          sumaAux += 1 + 1.5*LAux2.index(k)
        else:
          suma -= sumaAux
          break
    if (i-1,6) in tablero.keys() and tablero[(i-1,6)] != label:
      suma -= 5
    elif (i+1,6) in tablero.keys() and tablero[(i+1,6)] != label:
      suma -= 5
  if casilla in LCuidado:
    if i == j:
      for c in DiagEq:
        if c in tablero.keys() and tablero[c] != label:
          suma -= 99
    else:
      for c2 in DiagDi:
        if c2 in tablero.keys() and tablero[c2] != label:
          suma -= 99
  return suma

class Solution5(StudentHeuristic):
  def get_name(self) -> str:
    return "odiabordes_caet"

  def evaluation_function(self, state: TwoPlayerGameState) -> float:
    yo = state.next_player
    tablero = state.board
    my_label = yo.label

    n = borderFightTotal(tablero, my_label) 
    if not state.is_player_max(yo):
        n = -n
    return n
#Fin soluciones propias______________________________________________________________________

class Heuristic1(StudentHeuristic):

    def get_name(self) -> str:
        return "dummy"

    def evaluation_function(self, state: TwoPlayerGameState) -> float:
        # Use an auxiliary function.
        return self.dummy(123)

    def dummy(self, n: int) -> int:
        return n + 4


class Heuristic2(StudentHeuristic):

    def get_name(self) -> str:
        return "random"

    def evaluation_function(self, state: TwoPlayerGameState) -> float:
        return float(np.random.rand())


class Heuristic3(StudentHeuristic):

    def get_name(self) -> str:
        return "heuristic"

    def evaluation_function(self, state: TwoPlayerGameState) -> float:
        return simple_evaluation_function(state)


def create_match(player1: Player, player2: Player) -> TwoPlayerMatch:

    initial_board = None#np.zeros((dim_board, dim_board))
    initial_player = player1

    """game = TicTacToe(
        player1=player1,
        player2=player2,
        dim_board=dim_board,
    )"""

    initial_board = (
        ['..B.B..',
        '.WBBW..',
        'WBWBB..',
        '.W.WWW.',
        '.BBWBWB']
    )
    if initial_board is None:
        height, width = 8, 8
    else:
        height = len(initial_board)
        width = len(initial_board[0])
        try:
            initial_board = from_array_to_dictionary_board(initial_board)
        except ValueError:
            raise ValueError('Wrong configuration of the board')
        else:
            print("Successfully initialised board from array")
    
    game = Reversi(
        player1=player1,
        player2=player2,
        height=8,
        width=8
    )

    game_state = TwoPlayerGameState(
        game=game,
        board=initial_board,
        initial_player=initial_player,
    )

    return TwoPlayerMatch(game_state, max_seconds_per_move=1000, gui=False)


tour = Tournament(max_depth=3, init_match=create_match)
#strats = {'opt1': [Heuristic1], 'opt2': [Heuristic2], 'opt3': [Heuristic3], 'opt4': [Solution1], 'opt5': [Solution2], 'opt6': [Solution3]}
#strats = {'opt3': [Solution5], 'opt4': [Solution7]}
strats = {'opt1': [Solution1], 'opt2': [Solution2], 'opt3': [Solution3], 'opt5': [Solution5]}

n = 1
scores, totals, names = tour.run(
    student_strategies=strats,
    increasing_depth=False,
    n_pairs=n, 
    allow_selfmatch=False,
)

print(
    'Results for tournament where each game is repeated '
    + '%d=%dx2 times, alternating colors for each player' % (2 * n, n),
)

# print(totals)
# print(scores)

print('\ttotal:', end='')
for name1 in names:
    print('\t%s' % (name1), end='')
print()
for name1 in names:
    print('%s\t%d:' % (name1, totals[name1]), end='')
    for name2 in names:
        if name1 == name2:
            print('\t---', end='')
        else:
            print('\t%d' % (scores[name1][name2]), end='')
    print()
