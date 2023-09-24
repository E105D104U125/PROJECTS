import requests
import socket

url = 'vega.ii.uam.es'
PORT = 8000

def getIP():
    hostname = socket.gethostname()
    local_ip = socket.gethostbyname(hostname)

    return local_ip

def dns(hostname):
    data = socket.gethostbyname_ex(hostname)
    return data[2][0]

def peticion(peticion: bytes, url = url, port  = PORT):
    try:
        with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as s:
            s.settimeout(5)
            s.connect((dns(url), port))
            s.settimeout(None)
            s.sendall(peticion)   
            data = s.recv(2048)
            return data.decode("utf-8")
    except:
        print('[!] Error al conectar con el servidor de descubrimiento.')
    return None


def register(nick:str, password:str, port: str):
    # Obtenemos la IP del equipo
    ip_address = getIP()

    # Enviamos el paquete de REGISTER
    data = peticion(b'REGISTER ' + nick.encode('utf-8') + b' ' + ip_address.encode('utf-8') + 
        b' ' + port.encode('utf-8') + b' ' + password.encode('utf-8') + b' ' + b'V0')

    if data is not None:
        data  = data.split()[0:2]
        return data

def query(nick: str):

    # Enviamos la petición QUERY
    data = peticion(b'QUERY ' + nick.encode('utf-8'))

    # Comprobamos si ha sido exitoso
    status = data[0:2]
    if status == 'OK':
        data_list = data.split()
        # Si no hay error mandamos un diccionario con los distintos parámetros
        respDict = {'nick': data_list[2], 'ip_address': data_list[3], 'port': data_list[4], 'protocols': data_list[5].split("#")}
        return respDict
    elif status == 'NOK':
        return None


def list_users() -> list:

    # Enviamos la petición LIST_USERS
    data = peticion(b'LIST_USERS')

    # Comprobamos si ha sido exitoso
    status = data[0:2]
    if status == 'OK':
        # Si no hay error devolvemos la lista (TODO: a partir del 17 porque en la ejecución que he hecho empieza ahí la lista)
        lista =  data[17:].split("#")
        list_dict = list()
        for user in lista:
            user_list = user.split()
            try:
                list_dict.append({'nick': user_list[0], 'ip_address': user_list[1], 'port': user_list[2]})
            except:
                continue
        return list_dict

    elif status == 'NOK':
        return None


def quit():

    # Enviamos la petición QUIT
    data = peticion(b'QUIT')

    return #data #No creo que tenga que devolver nada la verdad