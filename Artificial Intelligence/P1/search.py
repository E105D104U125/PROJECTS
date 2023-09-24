# search.py
# ---------
# Licensing Information:  You are free to use or extend these projects for
# educational purposes provided that (1) you do not distribute or publish
# solutions, (2) you retain this notice, and (3) you provide clear
# attribution to UC Berkeley, including a link to http://ai.berkeley.edu.
# 
# Attribution Information: The Pacman AI projects were developed at UC Berkeley.
# The core projects and autograders were primarily created by John DeNero
# (denero@cs.berkeley.edu) and Dan Klein (klein@cs.berkeley.edu).
# Student side autograding was added by Brad Miller, Nick Hay, and
# Pieter Abbeel (pabbeel@cs.berkeley.edu).


"""
In search.py, you will implement generic search algorithms which are called by
Pacman agents (in searchAgents.py).
"""

import util

global g_problem
global g_heuristic

class SearchProblem:
    """
    This class outlines the structure of a search problem, but doesn't implement
    any of the methods (in object-oriented terminology: an abstract class).

    You do not need to change anything in this class, ever.
    """

    def getStartState(self):
        """
        Returns the start state for the search problem.
        """
        util.raiseNotDefined()

    def isGoalState(self, state):
        """
          state: Search state

        Returns True if and only if the state is a valid goal state.
        """
        util.raiseNotDefined()

    def getSuccessors(self, state):
        """
          state: Search state

        For a given state, this should return a list of triples, (successor,
        action, stepCost), where 'successor' is a successor to the current
        state, 'action' is the action required to get there, and 'stepCost' is
        the incremental cost of expanding to that successor.
        """
        util.raiseNotDefined()

    def getCostOfActions(self, actions):
        """
         actions: A list of actions to take

        This method returns the total cost of a particular sequence of actions.
        The sequence must be composed of legal moves.
        """
        util.raiseNotDefined()


def tinyMazeSearch(problem):
    """
    Returns a sequence of moves that solves tinyMaze.  For any other maze, the
    sequence of moves will be incorrect, so only use this for tinyMaze.
    """
    from game import Directions
    s = Directions.SOUTH
    w = Directions.WEST
    return  [s, s, w, s, w, w, s, w]

def depthFirstSearch(problem):
    """
    Search the deepest nodes in the search tree first.

    Your search algorithm needs to return a list of actions that reaches the
    goal. Make sure to implement a graph search algorithm.

    To get started, you might want to try some of these simple commands to
    understand the search problem that is being passed in:
    
    print("Start:", problem.getStartState())
    print("Is the start a goal?", problem.isGoalState(problem.getStartState()))
    print("Start's successors:",problem.getSuccessors(problem.getStartState()))
    """
    pila = util.Stack()
    return busquedaGeneral(problem, pila)

def breadthFirstSearch(problem):
    """Search the shallowest nodes in the search tree first."""
    cola  = util.Queue()
    return busquedaGeneral(problem, cola)
    

def uniformCostSearch(problem):
    """Search the node of least total cost first."""

    #El objeto problem es necesario para la función heurística
    global g_problem
    g_problem = problem

    cola_prioridad = util.PriorityQueueWithFunction(funcionCosteUniforme)
    return busquedaGeneral(problem, cola_prioridad)


def nullHeuristic(state, problem=None):
    """
    A heuristic function estimates the cost from the current state to the nearest
    goal in the provided SearchProblem.  This heuristic is trivial.
    """
    return 0

def aStarSearch(problem, heuristic=nullHeuristic):
    """Search the node that has the lowest combined cost and heuristic first."""
    
    global g_heuristic
    g_heuristic = heuristic

    global g_problem
    g_problem = problem
    cola_prioridad = util.PriorityQueueWithFunction(funcionPrioridadAEstrella)
    return busquedaGeneral(problem, cola_prioridad)

# Abbreviations
bfs = breadthFirstSearch
dfs = depthFirstSearch
astar = aStarSearch
ucs = uniformCostSearch


#Funciones definidas por los alumnos

def busquedaGeneral(problem, lista_abiertos):  
    """
    Algoritmo de busqueda general. 
    Se le pasa como argumento la lista de abiertos que ha de ser la estructura de datos
    que define la estrategia de busqueda.
    Devuelve la lista de acciones que se ha de realizar para resolver el problema.
    """

    # Control de errores
    if problem.isGoalState(problem.getStartState()) \
        or lista_abiertos.isEmpty() == False:
        return []
    
    #Inicializamos lista con la raiz
    lista_abiertos.push((problem.getStartState(),[]))
    lista_cerrados = []

    #Iteramos
    while (True):
        #Si la lista esta vacia no hemos encontrado solucion
        if (lista_abiertos.isEmpty()):
            return []

        #Elegimos un nodo conforme a estrategia
        estado,camino = lista_abiertos.pop()
        #Si el nodo cumple el test devolvemos el camino hacia el
        if (problem.isGoalState(estado)):
            return camino
    
        if (estado not in lista_cerrados):
            #Añadimos el nodo a la lista de cerrados
            lista_cerrados.append(estado)
            # Expande el nodo
            # El nodo ya fue eliminado mediante pop
            sucesores = problem.getSuccessors(estado)
            # Añade la información del estado del hijo y el camino que conduce hasta él
            for (estado_hijo,accion_hijo,coste_hijo) in sucesores:
                camino_hijo = []
                camino_hijo.extend((camino+[accion_hijo] if camino != None else accion_hijo))
                lista_abiertos.push((estado_hijo,camino_hijo))

def funcionCosteUniforme(item):
    """
    Devuelve la prioridad del nodo a la hora de expandirlo para la busqueda de coste uniforme.
    El problema ha de ser registrado previamente en la variable global correspondiente.
    """
    global g_problem
    estado,camino = item
    return g_problem.getCostOfActions(camino)

def funcionPrioridadAEstrella(item):
    """
    Devuelve la prioridad del nodo a la hora de expandirlo para la busqueda de A estrella.
    El problema y la heuristica han de ser registrados previamente en las variables globales correspondientes.
    """
    global g_heuristic
    global g_problem

    estado, camino = item
    h = g_heuristic(estado,g_problem)
    g = g_problem.getCostOfActions(camino)
    f = g + h
    return f
