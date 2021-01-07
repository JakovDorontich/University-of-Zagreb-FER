from pyglet.gl import *
import numpy as num
from numpy import arange
from math import *


# Ucitavanje kontrolnih tocaka krivulje
def ucitaj_tocke():
    kontrolneTocke=[]
    n = 0
    file = open("kontrolne_tocke_2D.txt", "r")
    for line in file:
        n = n + 1
        parts = line.split(" ")
        t = num.array([float(parts[0]), float(parts[1]), float(parts[2]), 1])
        kontrolneTocke.append(t)
    return kontrolneTocke



# Postavke prozora
width = 800
height = 800
window = pyglet.window.Window(width, height)
window.set_location(100, 100)
window.set_caption('OpenGL 2D B-spline')

# Citanje iz datoteke
kontrolneTocke = ucitaj_tocke()
n = len(kontrolneTocke)

# Postavljanje matrice periodickog segmenta
# i pripadne derivacije
a = num.array([-1, 3 ,-3, 1])
b = num.array([3, -6, 3, 0])
c = num.array([-3, 0 ,3, 0])
d = num.array([1, 4 ,1, 0])
B = num.row_stack((a, b, c, d))
B = B/6

a = num.array([-1, 3 ,-3, 1])
b = num.array([2, -4, 2, 0])
c = num.array([-1, 0 ,1, 0])
derB = num.row_stack((a, b, c))
derB = derB/2

# Crtanje krivulje
@window.event
def on_draw():
    glMatrixMode(GL_PROJECTION)
    glLoadIdentity()
    glOrtho(-50, 50, -50, 50, -1000, 1000)

    global triCol, r, n
    glClearColor(0.0, 0.0, 0.0, 0.0)
    glClear(GL_COLOR_BUFFER_BIT)
    glColor3f(1.0, 1.0, 1.0)


    # Crtanje kontrolnog poligona
    glBegin(GL_LINES)
    for i in range(0, n - 1):
        xy1 = kontrolneTocke[i]
        xy2 = kontrolneTocke[i + 1]
        glVertex2f(xy1[0], xy1[1])
        glVertex2f(xy2[0], xy2[1])
    glEnd()

    vektoriOrijentacije=[]
    tockeKrivulje=[]
    # Crtanje krivulje
    glBegin(GL_LINE_STRIP)
    for i in range(0, n-3):
        R = num.row_stack((kontrolneTocke[i], kontrolneTocke[i + 1], kontrolneTocke[i + 2], kontrolneTocke[i + 3]))
        for t in arange(0, 1, 0.05):
            T = num.array([t**3, t**2, t, 1])
            pi = num.matmul(num.matmul(T, B), R)
            tockeKrivulje.append(pi)

            derT = num.array([t**2, t, 1])
            derPi = num.matmul(num.matmul(derT, derB), R)
            vektoriOrijentacije.append(derPi)

            tx = pi[0]
            ty = pi[1]
            tz = pi[2]
            glVertex3f(tx, ty, tz)
    glEnd()

    # Crtanje tangenta na krivulju
    glColor3f(0.0, 1.0, 1.0)
    glBegin(GL_LINES)
    for i in range (0, len(tockeKrivulje)-1):
        pocTocka = tockeKrivulje[i]
        zavrsnaTocka = pocTocka + vektoriOrijentacije[i]*0.25
        glVertex3f(pocTocka[0], pocTocka[1], pocTocka[2])
        glVertex3f(zavrsnaTocka[0], zavrsnaTocka[1], zavrsnaTocka[2])
    glEnd()


pyglet.app.run()

