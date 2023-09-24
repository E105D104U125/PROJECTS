import time
from game import Player, TwoPlayerGameState, TwoPlayerMatch
from heuristic import Heuristic
from reversi import Reversi, from_array_to_dictionary_board
from strategy import MinimaxAlphaBetaStrategy, MinimaxStrategy
import math

#http://samsoft.org.uk/reversi/strategy.htm 

def evaluation_function_dummy(state: TwoPlayerGameState) -> float:
    return dummy(123)

def dummy(n: int) -> int:
    return n + 4

def evaluation_function_propia(state: TwoPlayerGameState) -> float:
    juego = state.game
    yo = state.next_player
    my_label = yo.label
    tablero = state.board
    n = posicionalDinamico(juego, tablero, my_label)
    if not state.is_player_max(yo):
        n = -n
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

heuristica1 = Heuristic(name="heuristica1", evaluation_function=evaluation_function_dummy)
heuristicaPropia = Heuristic(name="propia", evaluation_function=evaluation_function_propia)

estrategiaMinimaxHeuristica1Profundidad3 = MinimaxStrategy(heuristic=heuristica1, max_depth_minimax=3, verbose=0)
estrategiaMinimaxHeuristica1Profundidad4 = MinimaxStrategy(heuristic=heuristica1, max_depth_minimax=4, verbose=0)
estrategiaMinimaxHeuristicaPropiaProfundidad3 = MinimaxStrategy(heuristic=heuristicaPropia, max_depth_minimax=3, verbose=0)
estrategiaMinimaxHeuristicaPropiaProfundidad4 = MinimaxStrategy(heuristic=heuristicaPropia, max_depth_minimax=4, verbose=0)
estrategiaAlfaBetaHeuristica1Profundidad3 = MinimaxAlphaBetaStrategy(heuristic=heuristica1, max_depth_minimax=3, verbose=0)
estrategiaAlfaBetaHeuristica1Profundidad4 = MinimaxAlphaBetaStrategy(heuristic=heuristica1, max_depth_minimax=4, verbose=0)
estrategiaAlfaBetaHeuristicaPropiaProfundidad3 = MinimaxAlphaBetaStrategy(heuristic=heuristicaPropia, max_depth_minimax=3, verbose=0)
estrategiaAlfaBetaHeuristicaPropiaProfundidad4 = MinimaxAlphaBetaStrategy(heuristic=heuristicaPropia, max_depth_minimax=4, verbose=0)

jugadorHeuristica1MinimaxProfundidad31 = Player(name="H1MM31", strategy=estrategiaMinimaxHeuristica1Profundidad3)
jugadorHeuristica1MinimaxProfundidad41 = Player(name="H1MM41", strategy=estrategiaMinimaxHeuristica1Profundidad4)
jugadorHeuristicaPropiaMinimaxProfundidad31 = Player(name="HPMM31", strategy=estrategiaMinimaxHeuristicaPropiaProfundidad3)
jugadorHeuristicaPropiaMinimaxProfundidad41 = Player(name="HPMM41", strategy=estrategiaMinimaxHeuristicaPropiaProfundidad4)
jugadorHeuristica1AlfaBetaProfundidad31 = Player(name="H1AB31", strategy=estrategiaAlfaBetaHeuristica1Profundidad3)
jugadorHeuristica1AlfaBetaProfundidad41 = Player(name="H1AB41", strategy=estrategiaAlfaBetaHeuristica1Profundidad4)
jugadorHeuristicaPropiaAlfaBetaProfundidad31 = Player(name="HPAB31", strategy=estrategiaAlfaBetaHeuristicaPropiaProfundidad3)
jugadorHeuristicaPropiaAlfaBetaProfundidad41 = Player(name="HPAB41", strategy=estrategiaAlfaBetaHeuristicaPropiaProfundidad4)
jugadorHeuristica1MinimaxProfundidad32 = Player(name="H1MM32", strategy=estrategiaMinimaxHeuristica1Profundidad3)
jugadorHeuristica1MinimaxProfundidad42 = Player(name="H1MM42", strategy=estrategiaMinimaxHeuristica1Profundidad4)
jugadorHeuristicaPropiaMinimaxProfundidad32 = Player(name="HPMM32", strategy=estrategiaMinimaxHeuristicaPropiaProfundidad3)
jugadorHeuristicaPropiaMinimaxProfundidad42 = Player(name="HPMM42", strategy=estrategiaMinimaxHeuristicaPropiaProfundidad4)
jugadorHeuristica1AlfaBetaProfundidad32 = Player(name="H1AB32", strategy=estrategiaAlfaBetaHeuristica1Profundidad3)
jugadorHeuristica1AlfaBetaProfundidad42 = Player(name="H1AB42", strategy=estrategiaAlfaBetaHeuristica1Profundidad4)
jugadorHeuristicaPropiaAlfaBetaProfundidad32 = Player(name="HPAB32", strategy=estrategiaAlfaBetaHeuristicaPropiaProfundidad3)
jugadorHeuristicaPropiaAlfaBetaProfundidad42 = Player(name="HPAB42", strategy=estrategiaAlfaBetaHeuristicaPropiaProfundidad4)

initial_board = (
    ['..B.B..',
     '.WBBW..',
     'WBWBB..',
     '.W.WWW.',
     '.BBWBWB']
)

if initial_board is None:
    height_a, width_a = 8, 8
else:
    height_a = len(initial_board)
    width_a = len(initial_board[0])
    try:
        initial_board = from_array_to_dictionary_board(initial_board)
    except ValueError:
        raise ValueError('Wrong configuration of the board')
    else:
        print("Successfully initialised board from array")


juegoHeuristica1MinimaxProfundidad3 = Reversi(player1=jugadorHeuristica1MinimaxProfundidad31, player2=jugadorHeuristica1MinimaxProfundidad32, height=height_a, width=width_a)
juegoHeuristica1MinimaxProfundidad4 = Reversi(player1=jugadorHeuristica1MinimaxProfundidad41, player2=jugadorHeuristica1MinimaxProfundidad42, height=height_a, width=width_a)
juegoHeuristicaPropiaMinimaxProfundidad3 = Reversi(player1=jugadorHeuristicaPropiaMinimaxProfundidad31, player2=jugadorHeuristicaPropiaMinimaxProfundidad32, height=height_a, width=width_a)
juegoHeuristicaPropiaMinimaxProfundidad4 = Reversi(player1=jugadorHeuristicaPropiaMinimaxProfundidad41, player2=jugadorHeuristicaPropiaMinimaxProfundidad42, height=height_a, width=width_a)
juegoHeuristica1AlfaBetaProfundidad3 = Reversi(player1=jugadorHeuristica1AlfaBetaProfundidad31, player2=jugadorHeuristica1AlfaBetaProfundidad32, height=height_a, width=width_a)
juegoHeuristica1AlfaBetaProfundidad4 = Reversi(player1=jugadorHeuristica1AlfaBetaProfundidad41, player2=jugadorHeuristica1AlfaBetaProfundidad42, height=height_a, width=width_a)
juegoHeuristicaPropiaAlfaBetaProfundidad3 = Reversi(player1=jugadorHeuristicaPropiaAlfaBetaProfundidad31, player2=jugadorHeuristicaPropiaAlfaBetaProfundidad32, height=height_a, width=width_a)
juegoHeuristicaPropiaAlfaBetaProfundidad4 = Reversi(player1=jugadorHeuristicaPropiaAlfaBetaProfundidad41, player2=jugadorHeuristicaPropiaAlfaBetaProfundidad42, height=height_a, width=width_a)

estadoHeuristica1MinimaxProfundidad3 = TwoPlayerGameState(game=juegoHeuristica1MinimaxProfundidad3, board=initial_board, initial_player=jugadorHeuristica1MinimaxProfundidad31)
estadoHeuristica1MinimaxProfundidad4 = TwoPlayerGameState(game=juegoHeuristica1MinimaxProfundidad4, board=initial_board, initial_player=jugadorHeuristica1MinimaxProfundidad41)
estadoHeuristicaPropiaMinimaxProfundidad3 = TwoPlayerGameState(game=juegoHeuristicaPropiaMinimaxProfundidad3, board=initial_board, initial_player=jugadorHeuristicaPropiaMinimaxProfundidad31)
estadoHeuristicaPropiaMinimaxProfundidad4 = TwoPlayerGameState(game=juegoHeuristicaPropiaMinimaxProfundidad4, board=initial_board, initial_player=jugadorHeuristicaPropiaMinimaxProfundidad41)
estadoHeuristica1AlfaBetaProfundidad3 = TwoPlayerGameState(game=juegoHeuristica1AlfaBetaProfundidad3, board=initial_board, initial_player=jugadorHeuristica1AlfaBetaProfundidad31)
estadoHeuristica1AlfaBetaProfundidad4 = TwoPlayerGameState(game=juegoHeuristica1AlfaBetaProfundidad4, board=initial_board, initial_player=jugadorHeuristica1AlfaBetaProfundidad41)
estadoHeuristicaPropiaAlfaBetaProfundidad3 = TwoPlayerGameState(game=juegoHeuristicaPropiaAlfaBetaProfundidad3, board=initial_board, initial_player=jugadorHeuristicaPropiaAlfaBetaProfundidad31)
estadoHeuristicaPropiaAlfaBetaProfundidad4 = TwoPlayerGameState(game=juegoHeuristicaPropiaAlfaBetaProfundidad4, board=initial_board, initial_player=jugadorHeuristicaPropiaAlfaBetaProfundidad41)

partidoHeuristica1MinimaxProfundidad3 = TwoPlayerMatch(estadoHeuristica1MinimaxProfundidad3, max_seconds_per_move=1000, gui=False)
partidoHeuristica1MinimaxProfundidad4 = TwoPlayerMatch(estadoHeuristica1MinimaxProfundidad4, max_seconds_per_move=1000, gui=False)
partidoHeuristicaPropiaMinimaxProfundidad3 = TwoPlayerMatch(estadoHeuristicaPropiaMinimaxProfundidad3, max_seconds_per_move=1000, gui=False)
partidoHeuristicaPropiaMinimaxProfundidad4 = TwoPlayerMatch(estadoHeuristicaPropiaMinimaxProfundidad4, max_seconds_per_move=1000, gui=False)
partidoHeuristica1AlfaBetaProfundidad3 = TwoPlayerMatch(estadoHeuristica1AlfaBetaProfundidad3, max_seconds_per_move=1000, gui=False)
partidoHeuristica1AlfaBetaProfundidad4 = TwoPlayerMatch(estadoHeuristica1AlfaBetaProfundidad4, max_seconds_per_move=1000, gui=False)
partidoHeuristicaPropiaAlfaBetaProfundidad3 = TwoPlayerMatch(estadoHeuristicaPropiaAlfaBetaProfundidad3, max_seconds_per_move=1000, gui=False)
partidoHeuristicaPropiaAlfaBetaProfundidad4 =TwoPlayerMatch(estadoHeuristicaPropiaAlfaBetaProfundidad4, max_seconds_per_move=1000, gui=False)

inicio = time.time()
partidoHeuristica1MinimaxProfundidad3.play_match()
fin = time.time()
print("Heuristica 1 Minimax Profundidad 3:", (fin-inicio))

inicio = time.time()
partidoHeuristica1MinimaxProfundidad4.play_match()
fin = time.time()
print("Heuristica 1 Minimax Profundidad 4:", (fin-inicio))

inicio = time.time()
partidoHeuristicaPropiaMinimaxProfundidad3.play_match()
fin = time.time()
print("Heuristica Propia Minimax Profundidad 3:", (fin-inicio))

inicio = time.time()
partidoHeuristicaPropiaMinimaxProfundidad4.play_match()
fin = time.time()
print("Heuristica Propia Minimax Profundidad 4:", (fin-inicio))

inicio = time.time()
partidoHeuristica1AlfaBetaProfundidad3.play_match()
fin = time.time()
print("Heuristica 1 AlfaBeta Profundidad 3:", (fin-inicio))

inicio = time.time()
partidoHeuristica1AlfaBetaProfundidad4.play_match()
fin = time.time()
print("Heuristica 1 AlfaBeta Profundidad 4:", (fin-inicio))

inicio = time.time()
partidoHeuristicaPropiaAlfaBetaProfundidad3.play_match()
fin = time.time()
print("Heuristica Propia AlfaBeta Profundidad 3:", (fin-inicio))

inicio = time.time()
partidoHeuristicaPropiaAlfaBetaProfundidad4.play_match()
fin = time.time()
print("Heuristica Propia AlfaBeta Profundidad 4:", (fin-inicio))