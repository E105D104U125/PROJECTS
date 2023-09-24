'''
    practica1.py
    Muestra el tiempo de llegada de los primeros 50 paquetes a la interfaz especificada
    como argumento y los vuelca a traza nueva con tiempo actual

    Autor: Javier Ramos <javier.ramos@uam.es>
    2020 EPS-UAM
'''

from rc1_pcap import *
import sys
import binascii
import signal
import argparse
from argparse import RawTextHelpFormatter
import time
import logging

ETH_FRAME_MAX = 1514
PROMISC = 1
NO_PROMISC = 0
TO_MS = 10
num_paquete = 0
TIME_OFFSET = 30*60

def signal_handler(nsignal,frame):
	logging.info('Control C pulsado')
	if handle:
		pcap_breakloop(handle)
		

def procesa_paquete(us,header,data):
	global num_paquete, pdumper, args
	logging.info('Nuevo paquete de {} bytes capturado en el timestamp UNIX {}.{}'.format(header.len,header.ts.tv_sec,header.ts.tv_usec))
	num_paquete += 1

	#Imprimir los N primeros bytes
	#si el paquete es mas pequeño que nbytes, se imprime entero
	lim = min(args.nbytes, len(data))
	hex_data = data[:lim].hex()

	for i in range(lim):
		print(''+hex_data[2*i]+hex_data[2*i+1], end = " ")
	print ('\n')

	#Escribir el tráfico al fichero de captura con el offset temporal
	if (args.interface != False):
		header.ts.tv_sec += TIME_OFFSET
		pcap_dump(pdumper, header, data)
	
if __name__ == "__main__":
	global pdumper, args, handle
	parser = argparse.ArgumentParser(description='Captura tráfico de una interfaz ( o lee de fichero) y muestra la longitud y timestamp de los 50 primeros paquetes',
	formatter_class=RawTextHelpFormatter)
	parser.add_argument('--file', dest='tracefile', default=False,help='Fichero pcap a abrir')
	parser.add_argument('--itf', dest='interface', default=False,help='Interfaz a abrir')
	parser.add_argument('--nbytes', dest='nbytes', type=int, default=14,help='Número de bytes a mostrar por paquete')
	parser.add_argument('--debug', dest='debug', default=False, action='store_true',help='Activar Debug messages')
	args = parser.parse_args()

	if args.debug:
		logging.basicConfig(level = logging.DEBUG, format = '[%(asctime)s %(levelname)s]\t%(message)s')
	else:
		logging.basicConfig(level = logging.INFO, format = '[%(asctime)s %(levelname)s]\t%(message)s')

	if args.tracefile is False and args.interface is False:
		logging.error('No se ha especificado interfaz ni fichero')
		parser.print_help()
		sys.exit(-1)

	if args.nbytes <= 0:
		logging.error('Se debe introducir un número mayor que cero de bytes')
		parser.print_help()
		sys.exit(-1)

	signal.signal(signal.SIGINT, signal_handler)

	errbuf = bytearray()
	handle = None

	# FLUJO DIVIDIDO
	#abrir la interfaz especificada para captura o la traza
	if (args.interface != False):
		handle = pcap_open_live(args.interface, ETH_FRAME_MAX, PROMISC, TO_MS, errbuf)
		#abrir un dumper para volcar el tráfico (si se ha especificado interfaz) 
		if (handle != None):
			d = pcap_open_dead(DLT_EN10MB, ETH_FRAME_MAX)
			if (d != None):
				nombre_traza = 'captura.' + args.interface + '.' + str(time.time()) + '.pcap'
				pdumper = pcap_dump_open(d, nombre_traza)
				pcap_close(d)
		else:
			logging.error('Error al abrir la interfaz.' + '\n' + errbuf.decode("utf-8"))
			sys.exit(-1)
	elif (args.tracefile != False):
		''' en caso de fallo en la apertura esta funcion da violacion de segmento 
			y finaliza la ejecucion, por lo que no se comprueba handle == None'''
		handle = pcap_open_offline(args.tracefile, errbuf)
	

	# FLUJO UNICO
	ret = pcap_loop(handle, -1, procesa_paquete, None)

	if ret == -1:
		logging.error('Error al capturar un paquete')
	elif ret == -2:
		logging.debug('pcap_breakloop() llamado')
	elif ret == 0:
		logging.debug('No mas paquetes o limite superado')
	logging.info('{} paquetes procesados'.format(num_paquete))

	# se cierra
	if (handle != None):
		pcap_close(handle)

	# Se cierra el dumper en caso de haberse creado
	if (args.interface != False and pdumper != None):
		pcap_dump_close(pdumper)