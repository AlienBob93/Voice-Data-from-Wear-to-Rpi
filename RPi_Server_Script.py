import RPi.GPIO as GPIO
import threading
import SocketServer
import socket

# define the GPIO pins to be used
PIN = 4
ClientData = False

class PiBrainServer(SocketServer.BaseRequestHandler):
    #The RequestHandler class for our server.

    #It is instantiated once per connection to the server, and must
    #override the handle() method to implement communication to the
    #client.

    def handle(self):
		try:
			# self.request is the TCP socket connected to the client
			self.data = self.request.recv(1024).strip()
			print "{} wrote:".format(self.client_address[0])
			print self.data
			global ClientData
			#act on the relay
			if self.data == "on":
				ClientData = True
				print "turning on"
				print ClientData
			elif self.data == "off":
				ClientData = False
				print "turning off"
				print ClientData
			else:
				print "command not recognized..."
			# just send back the same data, but upper-cased
			self.request.sendall(self.data.upper())
		except KeyboardInterrupt:
			GPIO.cleanup()
		except IOError:
			print "IOError"	
			
def process():
	HOST, PORT = "10.0.0.42", 8080
	server = SocketServer.TCPServer((HOST, PORT), PiBrainServer)
	# Activate the server; this will keep running until you
	# interrupt the program with Ctrl-C
	server.serve_forever()
	
			
if __name__ == "__main__":
	print socket.gethostname()
	global ClientData
	
	GPIO.setmode(GPIO.BCM)
	GPIO.setup(PIN, GPIO.OUT)
	
	thread = threading.Thread(target = process)
	thread.daemon = True
	thread.start()
	while True:
		GPIO.output(PIN, ClientData)
