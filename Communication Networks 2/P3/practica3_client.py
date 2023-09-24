import numpy as np
import cv2
import conexion_ds
import peer_to_peer as p2p
import queue
import threading

# TODO
import time

from tkinter import Scrollbar
from appJar import gui
from PIL import Image, ImageTk

# TODO Pedir este atributo por entrada a la hora de registrar
PUERTO_VIDEO = 7000

class Register(object):

	registrado = False

	def __init__(self):
		self.puerto_control_propio = 0
		self.nick = ""

		# Creamos una variable que contenga el GUI principal
		self.app = gui("Registro")
		self.app.setGuiPadding(10, 10)

		# Añadimos la subventana de registro
		self.app.addLabel("user_nick", "Nick:", 0, 0)
		self.app.addEntry("nick", 0, 1)
		self.app.addLabel("pass_nick", "Contraseña:", 1, 0)
		self.app.addSecretEntry("password", 1, 1)
		self.app.addLabel("puerto_label", "Puerto control:", 2, 0)
		self.app.addEntry("puerto", 2, 1)

		self.app.addButtons(["Enviar", "Cancelar"],
							self.buttonsCallback, colspan=2)

	def start(self):
		self.app.go()

	def buttonsCallback(self, button):
		if button == "Cancelar":
			self.app.stop()

		elif button == "Enviar":
			try:
				self.puerto_control_propio = int(self.app.getEntry("puerto"))
				self.nick = self.app.getEntry("nick")
			except:
				self.app.errorBox("error", "Puerto incorrecto")
				self.app.clearEntry("puerto")
				return

			st = conexion_ds.register(self.nick, self.app.getEntry("password"), str(self.puerto_control_propio))

			if st == None:
				self.app.errorBox("error", "Error en el registro")
				return
			if st[0] == 'OK':
				if st[1] == 'WELCOME':
					self.registrado = True
					self.app.infoBox("exito", "Registro completado con éxito")
					self.app.stop()
				return
			elif st[0] == 'NOK':
				if st[1] == 'WRONG_PASS':
					self.app.errorBox("error", "Contraseña incorrecta")
					self.app.clearEntry("password")
				return


class VideoClient(object):

	def __init__(self, window_size):
		# Creamos una variable que contenga el GUI principal
		self.app = gui("Redes2 - P2P", window_size)
		self.app.setGuiPadding(10, 10)

		# Preparación del interfaz
		self.app.addLabel("title", "Cliente Multimedia P2P - Redes2 ")
		self.app.addImage("video", "imgs/webcam.gif")
		self.app.addImage("video2", "imgs/telefono.png")
		
		# Registramos la función de captura de video
		# Esta misma función también sirve para enviar un vídeo
		# self.cap = cv2.VideoCapture(0)
		self.cap = cv2.VideoCapture('imgs/cosecha propia de patacas.mp4')
		self.app.setPollTime(20)
		self.app.registerEvent(self.capturaVideo)
		self.app.registerEvent(self.procesaVideo)

		# Añadir los botones
		self.app.addButtons(
			["Conectar", "Lista de usuarios", "Colgar", "Salir"], self.buttonsCallback)

		# Barra de estado
		# Debe actualizarse con información útil sobre la llamada (duración, FPS, etc...)
		self.app.addStatusbar(fields=2)

		# Se crea el gestor de llamadas entrantes, se inicializa fuera
		self.gestor = None


	def start(self):
		self.app.go()

	# Función que captura el frame a mostrar en cada momento
	def capturaVideo(self):
		self.setImageResolution("HIGH")
		# Capturamos un frame de la cámara o del vídeo
		ret, frame = self.cap.read()  # lectura de un frame de vídeo
		frame = cv2.resize(frame, (640, 480))

		# Aquí tendría que el código que envia el frame a la red
		# TODO CAMBIAR FPS
		p2p.send_frame(frame, p2p.Resolucion.HIGH, 40)

		# Conversión de formato para su uso en el GUI
		cv2_im = cv2.cvtColor(frame, cv2.COLOR_BGR2RGB)
		img_tk_src = ImageTk.PhotoImage(Image.fromarray(cv2_im))
		
		# Lo mostramos en el GUI
		self.app.setImageData("video", img_tk_src, fmt='PhotoImage')

		#self.app.setImageData("video", img_tk, fmt='PhotoImage')

	# TODO FALTA VER QUE REGISTER EVENT NO CREA DEMASIADOS HILOS
	def procesaVideo(self):
		time.sleep(1)
		return
		if p2p.Llamada.llamada == None:
			return

		frame, resolucion = p2p.Llamada.llamada.conexion_video.cola_recepcion.get()
		self.setImageResolution(Llamada.Resolucion.getResName(resolucion))
		# Conversión de formato para su uso en el GUI
		cv2_im = cv2.cvtColor(frame, cv2.COLOR_BGR2RGB)
		img_tk_peer = ImageTk.PhotoImage(Image.fromarray(cv2_im))
		
		# Lo mostramos en el GUI
		self.app.setImageData("video2", img_tk, fmt='PhotoImage')
		


	# Establece la resolución de la imagen capturada
	def setImageResolution(self, resolution):
		# Se establece la resolución de captura de la webcam
		# Puede añadirse algún valor superior si la cámara lo permite
		# pero no modificar estos
		if resolution == "LOW":
			self.cap.set(cv2.CAP_PROP_FRAME_WIDTH, 160)
			self.cap.set(cv2.CAP_PROP_FRAME_HEIGHT, 120)
		elif resolution == "MEDIUM":
			self.cap.set(cv2.CAP_PROP_FRAME_WIDTH, 320)
			self.cap.set(cv2.CAP_PROP_FRAME_HEIGHT, 240)
		elif resolution == "HIGH":
			self.cap.set(cv2.CAP_PROP_FRAME_WIDTH, 640)
			self.cap.set(cv2.CAP_PROP_FRAME_HEIGHT, 480)

	# Función que gestiona los callbacks de los botones
	def buttonsCallback(self, button):

		def cerrar_subventana():
			self.app.destroySubWindow("Lista")
			self.app.show()

		def seleccion_usuario():
			# Obtiene el usuario con el que quiere conectarse
			nick = self.app.getRadioButton("user")

			# Confirma que quiera iniciar la llamda
			conexion = self.app.yesNoBox(
				"Conexión", '¿Seguro quieres iniciar la videollamada con ' + nick + '?')
			if conexion == True:
				# TODO: LLAMAR A QUERY PARA VER PROTOCOLOS, IP, PUERTO..
				# TODO: COMENZAR LLAMADA
				print('LLAMANDO')
				cerrar_subventana()
			else:
				print('Conexión rechazada')

		def invocar_iniciar_llamada():
			return


		if button == "Salir":
			# Salimos de la aplicación
			conexion_ds.quit()
			self.cap.release()
			cv2.destroyAllWindows()
			self.app.stop()

		elif button == "Conectar":
			# Entrada del nick del usuario a conectar
			# Obtenemos la información del usuario
			nick = self.app.textBox(
				"Conexión", "Introduce el nick del usuario a buscar")
			if nick == None:
				return
			info = conexion_ds.query(nick)
			if info == None:
				self.app.errorBox("error2", "Usuario no hencontrado")
				return

			# Confirma que quiera iniciar la llamda
			conexion = self.app.yesNoBox("Conexión", info['nick'] + ' soporta ' + ', '.join(
				info['protocols']) + ', ¿quieres iniciar la videollamada?')
			if conexion == True:
				# Diccionario de entrada funcion
				datos_llamada = {
					'ip_peer': info['ip_address'],
					'puerto_control_peer': info['port'],
					'puerto_udp_peer': None,
					'ip_propia': conexion_ds.getIP(),
					'puerto_control_propio': self.gestor.reg.puerto_control_propio,
					'puerto_udp_propio': PUERTO_VIDEO,
					'mi_nick': self.gestor.reg.nick
				}

				# TODO BORRAR
				datos_llamada = {
					'ip_peer': 'localhost',
					'puerto_control_peer': 2001,
					'puerto_udp_peer': None,
					'ip_propia': conexion_ds.getIP(),
					'puerto_control_propio': self.gestor.reg.puerto_control_propio,
					'puerto_udp_propio': PUERTO_VIDEO,
					'mi_nick': self.gestor.reg.nick
				}

				threading.Thread(target = p2p.iniciar_llamada, args = (datos_llamada,)).start()
				
				#TODO BORRAR
				print('LLAMANDO')
			else:
				print('Conexión rechazada')

		elif button == "Lista de usuarios":
			# Muestra la lista de usuarios en una subventana
			# Obtiene la lista de usuarios
			lista = conexion_ds.list_users()
			if lista == None:
				print('Error en la lista')
				return

			# Crea la subventana y muestra la lista pudiendo elegir un usuario con el que hacer videollamda
			# TODO: páginas para la lista
			self.app.startSubWindow("Lista")
			self.app.addLabel(
				"Selecciona el usuario con el que quieres hacer la videollamada:")
			self.app.setSize(600, 400)

			self.app.startScrollPane("scroll")

			for user in lista:
				self.app.addRadioButton("user", user['nick'])
			# La función seleccion_usuario será la que realice la llamada
			self.app.setRadioButtonChangeFunction("user", seleccion_usuario)

			self.app.stopScrollPane()

			self.app.addButton("Cerrar", cerrar_subventana)
			# self.app.setStopFunction(lambda: self.app.stop())
			self.app.stopSubWindow()

			# Abre la subventana
			self.app.go(startWindow="Lista")

		elif button == "Colgar":
			self.capturaVideo()

	def ventana_llamada_entrante(self, nick: str, cola: queue.Queue):
		def rechazar():
			cerrar_llamada_entrante()
			cola.put('RECHAZAR')

		def aceptar():
			cerrar_llamada_entrante()
			cola.put('ACEPTAR')

		def cerrar_llamada_entrante():
			self.app.destroySubWindow("Llamada entrante")
			self.app.show()

		self.app.startSubWindow("Llamada entrante")
		self.app.setSize(600, 400)
		#TODO: GIF TELEFONO
		self.app.addLabel("Llamada entrante de " + nick)
		self.app.addButtons(["Aceptar", "Rechazar"],
							[aceptar, rechazar], colspan=2)
		self.app.stopSubWindow()
		self.app.go(startWindow="Llamada entrante")

		return

class GestorLlamadasEntrantes(object):
	gestor = None

	def __init__(self, vc: VideoClient, reg: Register, colas_gestor: [queue.Queue]):
		self.gestor = self
		self.vc = vc
		self.cola_envio = colas_gestor[1]
		self.cola_recepcion = colas_gestor[0]
		self.reg = reg

		self.hilo = threading.Thread(
			target=self.espera_notificacion)
		self.hilo.start()

	def espera_notificacion(self):
		# TODO MATAR ESTE HILO AL SALIR
		#while True:
		datos_llamada = self.cola_recepcion.get()
		nick = datos_llamada
		print('Llamada entrante de ' + nick)
		
		cola_decision = queue.Queue() 
		self.vc.app.queueFunction(vc.ventana_llamada_entrante, nick, cola_decision)

		decision = cola_decision.get()
	
		self.cola_envio.put((decision, conexion_ds.getIP(), self.reg.puerto_control_propio, (PUERTO_VIDEO if decision == 'ACEPTAR' else 0), self.reg.nick))


if __name__ == '__main__':
	reg = Register()
	reg.start()
	if not reg.registrado:
		exit()

	# Crear aquí los threads de lectura, de recepción y,
	# en general, todo el código de inicialización que sea necesario

	# Conexion de control a la espera de llamadas
	# TODO Recoger este hilo
	colas_gestor = [queue.Queue(),queue.Queue()]
	# TODO
	p2p.CCRecepcion(conexion_ds.getIP(), reg.puerto_control_propio ,colas_gestor)

	vc = VideoClient("1000x800")
	vc.gestor = GestorLlamadasEntrantes(vc, reg, colas_gestor)
	# Lanza el bucle principal del GUI
	# El control ya NO vuelve de esta función, por lo que todas las
	# acciones deberán ser gestionadas desde callbacks y threads
	vc.start()
