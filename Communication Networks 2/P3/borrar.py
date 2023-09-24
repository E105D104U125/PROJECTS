import socket
import struct
import threading
import time

def foo():
    frame = 'ads#ads#ads#asd#9asasdd#adsasd##'
    frame = frame.encode('utf-8')
    count, i = 0, 0
    while count < 4:
        if frame[i] == 35:
            count += 1
        i += 1 
    return frame[i:], frame[0:i-1]

if __name__ == "__main__":
    #print(foo())
    a, b = [1,2]
    print(a)



class Conexion(object):
	def __init__(self, src_addr, dst_addr):
		self.src_addr = src_addr
		if dst_addr != None:
			self.dst_addr = dst_addr

	def enviarPaquete(self, peticion: bytes) -> bool:
		try:
			self.s.settimeout(TIMEOUT)
			self.s.sendto(peticion, self.dst_addr)
			return True
		except:
			print('[!] Error con la comunicación con peer.')
			return False

	def crearPaquete():
		pass

	def hilo_envio():
		pass

	def hilo_recepcion():
		pass


# Conexion de Control Envío
class CCLlamada(Conexion):
	def __init__(self, ip: str, puerto: int, socket):
		super().__init__(ip, puerto)
		# Socket TCP
		self.s = socket

		self.hilo_envio = threading.Thread(target=self.hilo_envio, args=(1,))
		self.hilo_envio.start()

		self.hilo_recepcion = threading.Thread(target=self.hilo_recepcion, args=(1,))
		self.hilo_recepcion.start()

	def hilo_envio(self):
		# semaforo
		# llamada = variable_global_global
		# semaforo
		llamada = True

		while llamada:
			data = self.s.recv(4096)
			# Procesa el comando

			return

		return

	def crearPaquete(self):
		return