from pyglet.gl import *
from pyglet.window import key
from pyglet.window import mouse

# Otvaranje prozora

window = pyglet.window.Window(350,350)
window.set_caption('Bresenham-ov postupak')
triCol = [1.0, 0.0, 0.0]
counter = 0


def on_draw():
	global x1, y1, x2, y2
	glColor3f(triCol[0], triCol[1], triCol[2])


	glBegin(GL_LINES)
	glVertex2i(x1, y1+20)
	glVertex2i(x2, y2+20)
	glEnd()


	glBegin(GL_POINTS)

	#Poseban slucaj za negativnu horizontalnu crtu (180 st.)
	if((y1 == y2) and (x2 < x1)):
		zamijeniX()

	#Poseban slucaj za negativnu vertikalnu crtu (270 st.)
	elif((x1 == x2) and (y2 < y1)):
		zamijeniY()

	#Zamijeni II kvadrant za IV i III kvadrant za I
	elif((y2>y1 and x2<x1) or (y2<y1 and x2<x1)):
		zamijeniX()
		zamijeniY()

	x0 = x2-x1
	y0 = y2-y1
	x = x1
	y = y1

	#Mijenjamo d ovisno o strmini crte, mora vrijediti 0<=d<=1
	if(abs(x0) >= abs(y0)):
		d = abs(y0/x0)
	else:
		d = abs(x0/y0)

	D = d-0.5
	
	#za blage crte
	if(abs(x0) >= abs(y0)):
		#padajuca crta (315-360st.)
		if(y0 < 0):
			for i in range(0, x0+1):
				glVertex2f(x, y)
				if(D > 0):
					y = y-1
					D = D-1
				x = x+1
				D = D+d
		#rastuca crta (0-45st.) 
		else:
			for i in range(0, x0+1):
				glVertex2f(x, y)
				if(D>0):
					y = y+1
					D = D-1
				x = x+1
				D = D+d

	#za strme crte
	else:
		#padajuca crta (270-315st.)
		if(y0<0 and x0>0):
			for i in range(0, abs(y0)+1):
				glVertex2f(x, y)
				if(D > 0):
					x = x+1
					D = D-1
				y = y-1
				D = D+d
		#rastuca crta (45-90st.)
		else:
			for i in range(0, y0+1):
				glVertex2f(x, y)
				if(D > 0):
					x = x+1
					D = D-1
				y = y+1
				D = D+d

	glEnd()

@window.event
def on_mouse_press(x, y, button, modifiers):
	global x1, x2, y1, y2, counter
	if button == mouse.LEFT:
		if counter == 0:
			print("A(%d, %d)" %(x, y))
			x1 = x
			y1 = y
			counter += 1
		elif counter == 1:
			print("B(%d, %d)" %(x, y))
			x2 = x
			y2 = y
			counter -= 1
			on_draw()
	elif button == mouse.RIGHT:
		window.clear()


@window.event
def on_key_press(symbol, modifiers):
	global triCol
	if symbol == key.R:
		triCol=[1.0, 0.0, 0.0]
	elif symbol == key.G:
		triCol = [0.0, 1.0, 0.0]
	elif symbol == key.B:
		triCol = [0.0, 0.5, 1.0]


def zamijeniX():
	global x1, x2
	a = x1
	x1 = x2
	x2 = a

def zamijeniY():
	global y1, y2
	b = y1
	y1 = y2
	y2 = b


pyglet.app.run()

