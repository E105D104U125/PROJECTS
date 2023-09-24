import time
import cv2
import threading
import socket
import queue
import struct

TIMEOUT = 10
PCK_SIZE = 40000


class Resolucion(object):
	LOW = (160, 120)
	MEDIUM = (320, 240)
	HIGH = (640, 480)

	def resToString(resolucion):
		return str(resolucion[0])+"x"+str(resolucion[1])

	def getResName(resolucion):
		if resolucion == HIGH:
			return "HIGH"
		elif resolucion == MEDIUM:
			return "MEDIUM"
		elif resolucion == LOW:
			return "LOW"



# Conexion de Control Recepcion
class CCRecepcion(object):
	mutex = threading.Lock()

	def __init__(self, ip_propia: str, puerto_control_propio: int, colas_gestor: [queue.Queue]):
		# TODO CAMBIAR ip_propia
		self.src_addr = ('localhost', puerto_control_propio)
		self.cola_envio = colas_gestor[0]
		self.cola_recepcion = colas_gestor[1]
		# Socket TCP
		self.s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)

		# TODO ELIMINAR
		self.s.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)

		self.s.bind(self.src_addr)
		self.hilo_envio = threading.Thread(target=self.esperar_llamada)
		self.hilo_envio.start()

	def esperar_llamada(self):
		self.s.listen()

		# TODO Matar este hilo
		while(True):
			socket_peer, addr_peer = self.s.accept()
			ip_peer, puerto_control_peer = addr_peer

			cmd = socket_peer.recv(4096)

			# Se crea un hilo para atender al comando y que el hilo de control
			# pueda seguir recibiendo
			# TODO HAY QUE VER SI ES NECESARIO CREAR UNA NUEVA CONEXION O SE PUEDE REUTILIZAR ESTA
			hilo = threading.Thread(
				target=self.procesar_comando, args=(ip_peer, puerto_control_peer, cmd,))
			hilo.start()

		self.s.close()

	# TODO
	# RESUMEN
	# OPCION 1: se reutiliza el socket
	#			En este caso, mandar comando privado al socket del hilo que escucha y procesarlo en procesar_comando
	
	def procesar_comando(self, ip_peer: str, puero_control_peer: int, cmd: bytes):
		cmd = cmd.decode('utf-8')
		cmd_args = cmd.split()
		cmd_type = cmd_args[0]

		if cmd_type == 'CALLING':
			puerto_udp_peer = cmd_args[2]
			if Llamada.llamando(Llamada) == True:
				# TODO CAMBIARLO [!!!]
				with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as socket_peer:
					socket_peer.connect((ip_peer, puero_control_peer))
					socket_peer.sendall(b'CALL_BUSY')
					socket_peer.close()
				return

			self.mutex.acquire()
			print('Entro...')
			# Notifica al usuario
			nick = cmd_args[1]
			self.cola_envio.put(nick)

			# Espera la respueta del usuario
			respuesta = self.cola_recepcion.get()
			accion, ip_propia, puerto_control_propio, puerto_udp_propio, mi_nick = respuesta

			if accion == "ACEPTAR":
				datos_llamada = {
					'ip_peer': ip_peer,
					'puerto_control_peer': puerto_control_peer,
					'puerto_udp_peer': puerto_udp_peer,
					'ip_propia': ip_propia,
					'puerto_control_propio': puerto_control_propio,
					'puerto_udp_propio': puerto_udp_propio,
					'mi_nick': mi_nick
				}

				iniciar_llamada(datos_llamada)

				socket_peer.sendall(
					b'CALL_ACCEPTED ' + mi_nick.encode('utf-8') + b' ' + struct.pack("!I", puerto_udp_propio))
				print('He aceptado la llamada de ' + cmd_args[1])
			elif accion == "RECHAZAR":
				socket_peer.sendall(b'CALL_DENIED ' + mi_nick.encode('utf-8'))

			self.mutex.release()

		elif cmd_type == 'CALL_HOLD':
			#TODO
			print('Llamada en pausa')

		elif cmd_type == 'CALL_RESUME':
			#TODO
			print('Llamada continuada')

		elif cmd_type == 'CALL_END':
			#TODO
			print('Llamada finalizada')

		socket_peer.close()
		return

class ConexionVideo(object):
	def __init__(self, src_addr, dst_addr):
		self.src_addr = src_addr
		self.dst_addr = dst_addr
		self.estado = True

		# Socket UDP
		try:
			self.s = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
			self.s.bind(self.src_addr)

			self.n_paquete = 0
			self.cola_envio = queue.Queue()
			self.hilo_envio = threading.Thread(
				target=self.hilo_envio, args=(self.cola_envio,))

			self.hilo_envio.start()
			self.cola_recepcion = queue.Queue()
			self.hilo_recepcion = threading.Thread(
				target=self.hilo_recepcion, args=(self.cola_recepcion,))
			self.hilo_recepcion.start()
		except:
			print('[!] Fallo al iniciar la conexion UDP')
			self.estado = False

	def enviarPaquete(self, peticion: bytes) -> bool:
		try:
			#self.s.settimeout(TIMEOUT)
			self.s.sendto(peticion, self.dst_addr)
			return True
		except:
			print('[!] Error con la comunicación con peer.')
			# Se perdera la llamada en el siguiente intento de envio
			self.estado = False
			return False

	def crearPaquete(self, frame, numero_orden: int, resolucion: Resolucion, fps: int) -> bytes:
		# Cabecera
		time_stamp = time.time()
		cabecera = str(numero_orden) + '#' + str(time_stamp) + '#' + \
			Resolucion.resToString(resolucion) + '#' + str(fps) + '#'

		# Compresión JPG al 50% de resolución (se puede variar)
		encode_param = [cv2.IMWRITE_JPEG_QUALITY, 50]
		result, encimg = cv2.imencode('.jpg', frame, encode_param)
		if result == False:
			print('Error al codificar imagen')
			return None
		encimg = encimg.tobytes()

		return b''.join([bytes(cabecera, 'utf-8'), encimg])

	def obtener_frame(frame: bytes):
		count, i = 0, 0
		while count < 4:
			if frame[i] == 35:
				count += 1
			i += 1 
		return frame[i:], frame[0:i-1]

	def hilo_envio(self, cola: queue.Queue):
		# TODO
		# semaforo
		# llamada = variable_global_global
		# semaforo
		llamada = True
		while llamada:
			print('Preparando el envio...' + str(self.n_paquete))
			data = cola.get()
			paquete = self.crearPaquete(
				data[0], self.n_paquete, data[1], data[2])
			self.enviarPaquete(paquete)
			self.n_paquete += 1
		return

	def hilo_recepcion(self, cola: queue.Queue):
		# TODO
		# semaforo
		# llamada = variable_global_global
		# semaforo
		llamada = True
		while llamada:
			print('Recibo...')
			paquete = self.s.recv(PCK_SIZE)

			# Procesamiento del paquete
			encimg, cabecera = obtener_frame(paquete)
			numero_orden, time_stamp, resolucion, fps = cabecera.split('#')
			
			decimg = cv2.imdecode(np.frombuffer(encimg,np.uint8), 1)
			self.cola.put((decimg, resolucion))
		return


class Llamada(object):
	llamada = None

	def __init__(self, src_addr, dst_addr):
		self.conexion_video = ConexionVideo(src_addr, dst_addr)

		# TODO Comprueba que se han creado bien los sockets


	def colgar_llamada(self):
		# TODO VARIABLE FINALIZACION
		self.conexion_video.hilo_envio.join()
		self.conexion_video.hilo_recepcion.join()
		llamada = None

	def _send_frame(frame, resolucion: Resolucion(), fps: int):
		if Llamada.llamada == None:
			return

		# Se perdio la conexion, se acaba la llamada
		# TODO Cerrar posible ventana del GUI
		if Llamada.llamada.conexion_video.estado == False:
			Llamada.llamada = None
			return
		try:
			Llamada.llamada.conexion_video.cola_envio.put((frame, resolucion, fps))
		except:
			print('[!] Cola de envio de video llena')


def iniciar_llamada(datos_llamada: dict):
	ip_peer = datos_llamada['ip_peer']
	puerto_control_peer = datos_llamada['puerto_control_peer']
	puerto_udp_peer = datos_llamada['puerto_udp_peer']
	ip_propia = datos_llamada['ip_propia']
	puerto_control_propio = datos_llamada['puerto_control_propio']
	puerto_udp_propio = datos_llamada['puerto_udp_propio']
	mi_nick = datos_llamada['mi_nick']

	# CASO 1: Llamada saliente
	if puerto_udp_peer == None:
		cmd = ''
		peer_addr = (ip_peer, puerto_control_peer)
		src_addr = (ip_propia, puerto_control_propio)
		
		# TODO Hacer que CCRecepcion llame
		with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as s:
			s.bind(src_addr)
			s.connect(peer_addr)
			# Envio de la peticion de llamada		 
			s.sendall(b'CALLING ' + mi_nick.encode('utf-8') +
					  b' ' + str(puerto_udp_propio).encode('utf-8'))
			
			# Recibo el comando de respuesta
			s.settimeout(TIMEOUT)
			data = s.recv(4096)
			
			# TODO BORRAR
			print(data)

			# Procesamiento del comando recibido
			try:
				data = data.split()
				cmd = data[0]
				puerto_udp_peer = struct.unpack('!I', data[2])[0]
			except:
				print('[!] Error con la peticion')
				s.close()
				return
			
			# TODO
			if cmd == b'CALL_DENIED' or cmd == b'CALL_BUSY' or cmd != b'CALL_ACCEPTED':
				s.close()
				return
			
			s.close()

	# CASO 2: Llamada entrante y llamada aceptada en llamada saliente
	# Registra la llamada creando un nuevo objeto
	peer_udp_addr = (ip_peer, puerto_udp_peer)
	src_udp_addr = (ip_propia, puerto_udp_propio)
	l = Llamada(src_udp_addr, peer_udp_addr)
	Llamada.llamada = l

def send_frame(frame, resolucion: Resolucion(), fps: int):
	if Llamada.llamada != None:
		# Envia a la llamada
		print('Envio...')
		Llamada._send_frame(frame, resolucion, fps)