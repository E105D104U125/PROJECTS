from ip import *
from threading import Lock
import struct

ICMP_PROTO = 1


ICMP_ECHO_REQUEST_TYPE = 8
ICMP_ECHO_REPLY_TYPE = 0

timeLock = Lock()
icmp_send_times = {}

HEADER_LEN = 8

def process_ICMP_message(us:ctypes.c_void_p,header:pcap_pkthdr,data:bytes,srcIP:int) -> None:
    '''
        Nombre: process_ICMP_message
        Descripción: Esta función procesa un mensaje ICMP. Esta función se ejecutará por cada datagrama IP que contenga
        un 1 en el campo protocolo de IP
        Esta función debe realizar, al menos, las siguientes tareas:
            -Calcular el checksum de ICMP:
                -Si es distinto de 0 el checksum es incorrecto y se deja de procesar el mensaje
            -Extraer campos tipo y código de la cabecera ICMP
            -Loggear (con logging.debug) el valor de tipo y código
            -Si el tipo es ICMP_ECHO_REQUEST_TYPE:
                -Generar un mensaje de tipo ICMP_ECHO_REPLY como respuesta. Este mensaje debe contener
                los datos recibidos en el ECHO_REQUEST. Es decir, "rebotamos" los datos que nos llegan.
                -Enviar el mensaje usando la función sendICMPMessage
            -Si el tipo es ICMP_ECHO_REPLY_TYPE:
                -Extraer del diccionario icmp_send_times el valor de tiempo de envío usando como clave los campos srcIP e icmp_id e icmp_seqnum
                contenidos en el mensaje ICMP. Restar el tiempo de envio extraído con el tiempo de recepción (contenido en la estructura pcap_pkthdr)
                -Se debe proteger el acceso al diccionario de tiempos usando la variable timeLock
                -Mostrar por pantalla la resta. Este valor será una estimación del RTT
            -Si es otro tipo:
                -No hacer nada

        Argumentos:
            -us: son los datos de usuarios pasados por pcap_loop (en nuestro caso este valor será siempre None)
            -header: estructura pcap_pkthdr que contiene los campos len, caplen y ts.
            -data: array de bytes con el conenido del mensaje ICMP
            -srcIP: dirección IP que ha enviado el datagrama actual.
        Retorno: Ninguno   
    '''
    if (chksum(data) != 0):
        return None

    tipo = data[0]
    code = data[1]
    logging.debug('Tipo: %d\tCódigo: %d', tipo, code)
    icmp_id = struct.unpack('!H', data[4:6])[0]
    icmp_seqnum = struct.unpack('!H', data[6:8])[0]

    if (code != 0):
    	return None

    if (tipo == ICMP_ECHO_REQUEST_TYPE):
        #Genera y manda un mensaje de tipo ICMP_ECHO_REPLY
        if(sendICMPMessage(data[HEADER_LEN:], ICMP_ECHO_REPLY_TYPE, code, icmp_id, icmp_seqnum, srcIP) == False):
            return None

    elif (tipo == ICMP_ECHO_REPLY_TYPE):
        #Extraemos del diccionario el tiempo de envio
        timeLock.acquire()
        if ((srcIP + icmp_id + icmp_seqnum) in icmp_send_times):
        	sendTime = header.ts.tv_sec + header.ts.tv_usec*10**(-6) - icmp_send_times[srcIP + icmp_id + icmp_seqnum]
        timeLock.release()
        print('RTT = ', sendTime)

    return None

    

def sendICMPMessage(data:bytes,tipo:int,code:int,icmp_id:int,icmp_seqnum:int,dstIP:int) -> bool:
    '''
        Nombre: sendICMPMessage
        Descripción: Esta función construye un mensaje ICMP y lo envía.
        Esta función debe realizar, al menos, las siguientes tareas:
            -Si el campo type es ICMP_ECHO_REQUEST_TYPE o ICMP_ECHO_REPLY_TYPE:
                -Construir la cabecera ICMP
                -Añadir los datos al mensaje ICMP
                -Calcular el checksum y añadirlo al mensaje donde corresponda
                -Si type es ICMP_ECHO_REQUEST_TYPE
                    -Guardar el tiempo de envío (llamando a time.time()) en el diccionario icmp_send_times
                    usando como clave el valor de dstIp+icmp_id+icmp_seqnum
                    -Se debe proteger al acceso al diccionario usando la variable timeLock

                -Llamar a sendIPDatagram para enviar el mensaje ICMP
                
            -Si no:
                -Tipo no soportado. Se devuelve False

        Argumentos:
            -data: array de bytes con los datos a incluir como payload en el mensaje ICMP
            -type: valor del campo tipo de ICMP
            -code: valor del campo code de ICMP 
            -icmp_id: entero que contiene el valor del campo ID de ICMP a enviar
            -icmp_seqnum: entero que contiene el valor del campo Seqnum de ICMP a enviar
            -dstIP: entero de 32 bits con la IP destino del mensaje ICMP
        Retorno: True o False en función de si se ha enviado el mensaje correctamente o no    
    '''
    if ((tipo != ICMP_ECHO_REQUEST_TYPE) & (tipo != ICMP_ECHO_REPLY_TYPE)):
    	return False

    message1 = struct.pack('B', tipo) + struct.pack('B', code)
    message2 = struct.pack('!H', icmp_id) + struct.pack('!H', icmp_seqnum) + data

    message = message1 + struct.pack('H', chksum(message1 + message2)) + message2

    if (tipo == ICMP_ECHO_REQUEST_TYPE):
        timeLock.acquire()
        icmp_send_times[dstIP + icmp_id + icmp_seqnum] = time.time()
        timeLock.release()

    return sendIPDatagram(dstIP, message, ICMP_PROTO)

   
def initICMP() -> None:
    '''
        Nombre: initICMP
        Descripción: Esta función inicializa el nivel ICMP
        Esta función debe realizar, al menos, las siguientes tareas:
            -Registrar (llamando a registerIPProtocol) la función process_ICMP_message con el valor de protocolo 1

        Argumentos:
            -Ninguno
        Retorno: Ninguno
    '''
    registerIPProtocol(process_ICMP_message, ICMP_PROTO)