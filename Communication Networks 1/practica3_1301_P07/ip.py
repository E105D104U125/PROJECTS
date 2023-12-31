from ethernet import *
from arp import *
from fcntl import ioctl
import subprocess
from math import *
SIOCGIFMTU = 0x8921
SIOCGIFNETMASK = 0x891b
#Diccionario de protocolos. Las claves con los valores numéricos de protocolos de nivel superior a IP
#por ejemplo (1, 6 o 17) y los valores son los nombres de las funciones de callback a ejecutar.
protocols={}
#Valor inicial para el IPID
IPID = 0
#Valor de ToS por defecto
DEFAULT_TOS = 0
#Tamaño mínimo de la cabecera IP
IP_MIN_HLEN = 20
#Tamaño máximo de la cabecera IP
IP_MAX_HLEN = 60
#Valor de TTL por defecto
DEFAULT_TTL = 64

def chksum(msg:bytes) -> int:
    '''
        Nombre: chksum
        Descripción: Esta función calcula el checksum IP sobre unos datos de entrada dados (msg)
        Argumentos:
            -msg: array de bytes con el contenido sobre el que se calculará el checksum
        Retorno: Entero de 16 bits con el resultado del checksum en ORDEN DE RED
    '''
    s = 0       
    for i in range(0, len(msg), 2):
        if (i+1) < len(msg):
            a = msg[i] 
            b = msg[i+1]
            s = s + (a+(b << 8))
        elif (i+1)==len(msg):
            s += msg[i]
        else:
            raise 'Error calculando el checksum'
    s = s + (s >> 16)
    s = ~s & 0xffff

    return s

def getMTU(interface:str) -> int:
    '''
        Nombre: getMTU
        Descripción: Esta función obteiene la MTU para un interfaz dada
        Argumentos:
            -interface: cadena con el nombre la interfaz sobre la que consultar la MTU
        Retorno: Entero con el valor de la MTU para la interfaz especificada
    '''
    s = socket.socket(socket.AF_PACKET, socket.SOCK_RAW)
    ifr = struct.pack('16sH', interface.encode("utf-8"), 0)
    mtu = struct.unpack('16sH', ioctl(s,SIOCGIFMTU, ifr))[1]
   
    s.close()
   
    return mtu
   
def getNetmask(interface:str) -> int:
    '''
        Nombre: getNetmask
        Descripción: Esta función obteiene la máscara de red asignada a una interfaz 
        Argumentos:
            -interface: cadena con el nombre la interfaz sobre la que consultar la máscara
        Retorno: Entero de 32 bits con el valor de la máscara de red
    '''
    s = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
    ip = fcntl.ioctl(
        s.fileno(),
       SIOCGIFNETMASK,
        struct.pack('256s', (interface[:15].encode('utf-8')))
    )[20:24]
    s.close()
    return struct.unpack('!I',ip)[0]


def getDefaultGW(interface:str) -> int:
    '''
        Nombre: getDefaultGW
        Descripción: Esta función obteiene el gateway por defecto para una interfaz dada
        Argumentos:
            -interface: cadena con el nombre la interfaz sobre la que consultar el gateway
        Retorno: Entero de 32 bits con la IP del gateway
    '''
    p = subprocess.Popen(['ip r | grep default | awk \'{print $3}\''], stdout=subprocess.PIPE, shell=True)
    dfw = p.stdout.read().decode('utf-8')
    print(dfw)
    return struct.unpack('!I',socket.inet_aton(dfw))[0]



def process_IP_datagram(us:ctypes.c_void_p,header:pcap_pkthdr,data:bytes,srcMac:bytes) -> None:
    '''
        Nombre: process_IP_datagram
        Descripción: Esta función procesa datagramas IP recibidos.
            Se ejecuta una vez por cada trama Ethernet recibida con Ethertype 0x0800
            Esta función debe realizar, al menos, las siguientes tareas:
                -Extraer los campos de la cabecera IP (includa la longitud de la cabecera)
                -Calcular el checksum sobre los bytes de la cabecera IP
                    -Comprobar que el resultado del checksum es 0. Si es distinto el datagrama se deja de procesar
                -Analizar los bits de de MF y el offset. Si el offset tiene un valor != 0 dejar de procesar el datagrama (no vamos a reensamblar)
                -Loggear (usando logging.debug) el valor de los siguientes campos:
                    -Longitud de la cabecera IP
                    -IPID
                    -Valor de las banderas DF y MF
                    -Valor de offset
                    -IP origen y destino
                    -Protocolo
                -Comprobar si tenemos registrada una función de callback de nivel superior consultando el diccionario protocols y usando como
                clave el valor del campo protocolo del datagrama IP.
                    -En caso de que haya una función de nivel superior registrada, debe llamarse a dicha funciñón 
                    pasando los datos (payload) contenidos en el datagrama IP.
        
        Argumentos:
            -us: Datos de usuario pasados desde la llamada de pcap_loop. En nuestro caso será None
            -header: cabecera pcap_pktheader
            -data: array de bytes con el contenido del datagrama IP
            -srcMac: MAC origen de la trama Ethernet que se ha recibido
        Retorno: Ninguno
    '''
    global protocols

    #Extracción de los campos de las cabeceras
    ipv = (data[0] & (0xF0))>>4
    if (ipv != 4):
        return
    ihl = data[0] & (0x0F)
    headerLenght = ihl*4

    typeService = data[1]
    if (typeService != 0):
        return

    totalLength = struct.unpack('!H', data[2:4])[0]
    identification = struct.unpack('!H', data[4:6])[0]
    DF = (data[6] & (0x40)) >> 6
    MF = (data[6] & (0x20)) >> 5

    offset = struct.unpack('!H', bytes([data[6] & (0x1F), data[7] & (0xFF)]))[0]
    realOffset = offset*8

    timeLive = data[8]
    protocol = data[9]
    header_checksum = struct.unpack('!H', data[10:12])[0]
    ipSrc = struct.unpack('!I', data[12:16])[0]
    ipDst = struct.unpack('!I', data[16:20])[0]
    opts = data[20:headerLenght]

    #Comprobación checksum
    if (chksum(data[0:headerLenght]) != 0):
        return

    if (offset != 0):
    	return

    #Logging debug
    logging.debug('Longitud de la cabecera IP: %d\tIPID: %d\tDF: %d\tMF: %d\tOffset: %d\tIP origen: %d\tIP destino: %d\tProtocolo: %d'
    	,headerLenght, identification, DF, MF, realOffset, ipSrc, ipDst, protocol)

    #Comprobar si existe una función de callback de nivel superior asociada al protocolo de la trama.
    if (protocol in protocols):
        #Llamar a la función de nivel superior con los parámetros que corresponde
        protocols[protocol](us, header, data[headerLenght:], ipSrc)
    else:
        #No está en el diccionario
        return

def registerIPProtocol(callback_fun: Callable[[ctypes.c_void_p,pcap_pkthdr,bytes,int],None],protocol:int) -> None:
    '''
        Nombre: registerIPProtocol
        Descripción: Esta función recibirá el nombre de una función y su valor de protocolo IP asociado y añadirá en la tabla 
            (diccionario) de protocolos de nivel superior dicha asociación. 
            Este mecanismo nos permite saber a qué función de nivel superior debemos llamar al recibir un datagrama IP  con un 
            determinado valor del campo protocolo (por ejemplo TCP o UDP).
            Por ejemplo, podemos registrar una función llamada process_UDP_datagram asociada al valor de protocolo 17 y otra 
            llamada process_ICMP_message asocaida al valor de protocolo 1. 
        Argumentos:
            -callback_fun: función de callback a ejecutar cuando se reciba el protocolo especificado. 
                La función que se pase como argumento debe tener el siguiente prototipo: funcion(us,header,data,srcIp):
                Dónde:
                    -us: son los datos de usuarios pasados por pcap_loop (en nuestro caso este valor será siempre None)
                    -header: estructura pcap_pkthdr que contiene los campos len, caplen y ts.
                    -data: payload del datagrama IP. Es decir, la cabecera IP NUNCA se pasa hacia arriba.
                    -srcIP: dirección IP que ha enviado el datagrama actual.
                La función no retornará nada. Si un datagrama se quiere descartar basta con hacer un return sin valor y dejará de procesarse.
            -protocol: valor del campo protocolo de IP para el cuál se quiere registrar una función de callback.
        Retorno: Ninguno 
    '''
    global protocols

    protocols[protocol] = callback_fun

def initIP(interface:str,opts=None) -> bool:
    global myIP, MTU, netmask, defaultGW,ipOpts
    '''
        Nombre: initIP
        Descripción: Esta función inicializará el nivel IP. Esta función debe realizar, al menos, las siguientes tareas:
            -Llamar a initARP para inicializar el nivel ARP
            -Obtener (llamando a las funciones correspondientes) y almacenar en variables globales los siguientes datos:
                -IP propia
                -MTU
                -Máscara de red (netmask)
                -Gateway por defecto
            -Almacenar el valor de opts en la variable global ipOpts
            -Registrar a nivel Ethernet (llamando a registerCallback) la función process_IP_datagram con el Ethertype 0x0800
        Argumentos:
            -interface: cadena de texto con el nombre de la interfaz sobre la que inicializar ip
            -opts: array de bytes con las opciones a nivel IP a incluir en los datagramas o None si no hay opciones a añadir
        Retorno: True o False en función de si se ha inicializado el nivel o no
    '''
    if(initARP(interface) != 0):
        return False

    myIP = getIP(interface)
    MTU = getMTU(interface)
    netmask = getNetmask(interface)
    defaultGW = getDefaultGW(interface)

    # Comprobación tamaño ipOpts
    if (opts != None):
        if (len(opts) > IP_MAX_HLEN - IP_MIN_HLEN):
            return False
    ipOpts = opts

    registerCallback(process_IP_datagram, int(0x0800))
    return True

# Funcion auxiliar creación cabecera
def createIPHeader(totalLength:int,MF:int,offset:int,protocol:int,dstIP:int) -> bytes:
    global IPID, myIP

    header1 = struct.pack('B', (4 << 4) | headLen//4) + bytes([0x00]) +\
        struct.pack('!H', totalLength) + struct.pack('!H', IPID) +\
        struct.pack('!H', (MF << 13) | offset) + struct.pack('B', 64) +\
        struct.pack('B', protocol)
    header2 = struct.pack('!I', myIP) + struct.pack('!I', dstIP)
    if ipOpts != None:
        header2 += ipOpts

    #Cálculo del checksum
    checksum = chksum(header1 + header2)

    return header1 + struct.pack('H', checksum) + header2

def sendIPDatagram(dstIP:int,data:bytes,protocol:int) -> bool:
    global IPID, ipOpts, myIP, MTU, defaultGW, netmask, headLen
    '''
        Nombre: sendIPDatagram
        Descripción: Esta función construye un datagrama IP y lo envía. En caso de que los datos a enviar sean muy grandes la función
        debe generar y enviar el número de fragmentos IP que sean necesarios.
        Esta función debe realizar, al menos, las siguientes tareas:
            -Determinar si se debe fragmentar o no y calcular el número de fragmentos
            -Para cada datagrama o fragmento:
                -Construir la cabecera IP con los valores que corresponda.Incluir opciones en caso de que ipOpts sea distinto de None
                -Calcular el checksum sobre la cabecera y añadirlo a la cabecera en la posición correcta
                -Añadir los datos a la cabecera IP
                -En el caso de que sea un fragmento ajustar los valores de los campos MF y offset de manera adecuada
                -Enviar el datagrama o fragmento llamando a sendEthernetFrame. Para determinar la dirección MAC de destino
                al enviar los datagramas:
                    -Si la dirección IP destino está en mi subred:
                        -Realizar una petición ARP para obtener la MAC asociada a dstIP y usar dicha MAC
                    -Si la dirección IP destino NO está en mi subred:
                        -Realizar una petición ARP para obtener la MAC asociada al gateway por defecto y usar dicha MAC
            -Para cada datagrama (no fragmento):
                -Incrementar la variable IPID en 1.
        Argumentos:
            -dstIP: entero de 32 bits con la IP destino del datagrama 
            -data: array de bytes con los datos a incluir como payload en el datagrama
            -protocol: valor numérico del campo IP protocolo que indica el protocolo de nivel superior de los datos
            contenidos en el payload. Por ejemplo 1, 6 o 17.
        Retorno: True o False en función de si se ha enviado el datagrama correctamente o no
          
    '''
    headLen = IP_MIN_HLEN + (len(ipOpts) if (ipOpts!= None) else 0)
    cargaMaxima = MTU - headLen
    cargaMaxima = cargaMaxima - cargaMaxima%8

    if (len(data) > cargaMaxima):
        # Numero fragmento fragmentación
        nPaqDec = len(data)/cargaMaxima
        frag = floor(nPaqDec) + (0 if (floor(nPaqDec) == nPaqDec) else 1)
    else:
        # Caso 1: No Fragmentado
        frag = 1
        header = createIPHeader(headLen+len(data),0,0,protocol,dstIP)
        frame = header + data

    # Para cada fragmento
    for i in range(0, frag):
        # Caso 2: Fragmentado
        if(frag > 1):
            # Crea el nuevo fragmento
            chunk = data[floor(cargaMaxima*i):min(floor(cargaMaxima*(i+1)),len(data))]

            header = createIPHeader(headLen+len(chunk),1 if (i != frag-1) else 0,floor(cargaMaxima*i/8),protocol,dstIP)
            frame = header + chunk

        # Envío de la trama
        if ((netmask & myIP) != (netmask & dstIP)):
            # No esta en la subred
            mac = ARPResolution(defaultGW)
        else:
            # Esta en la subred
            mac = ARPResolution(dstIP)
        if (sendEthernetFrame(frame, len(frame), int(0x0800), mac)):
        	return False

    IPID += 1
    return True