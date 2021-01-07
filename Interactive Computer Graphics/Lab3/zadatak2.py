from pyglet.gl import *
from pyglet.window import *
from math import *
import numpy


def dohvatiPodatke(file):
    vrhovi = []
    data = open(file)

    for line in data:
        if (line.startswith("v")):
            vrijednosti = line.split(" ")
            vrhovi.append((int(vrijednosti[1]), int(vrijednosti[2])))

    return vrhovi


def crtajPoligon(vrhovi):
    for i in range(len(vrhovi)-1):
        par1 = vrhovi[i]
        par2 = vrhovi[i+1]

        glBegin(GL_LINES)
        glVertex2i(par1[0], par1[1])
        glVertex2i(par2[0], par2[1])
        glEnd()


def crtajKrivulju(vrhovi):
    n = len(vrhovi) - 1
    t = 0.0
    c = []
    for i in range(len(vrhovi)):
        c.append(factorial(n) / (factorial(i) * factorial(n - i)))

    while (t <= 1.0):
        i = 0
        p = [0, 0, 0]
        for vrh in vrhovi:
            r = [vrh[0], vrh[1], 0]
            b = c[i] * t ** i * (1 - t) ** (n - i)
            p = numpy.add(p, numpy.multiply(r, b))
            i += 1

        glBegin(GL_POINTS)
        glVertex2f(p[0], p[1])
        glEnd()

        t += 0.001



window = pyglet.window.Window(800, 800)
window.set_location(20, 40)
window.set_caption('Bezierova krivulja')

file = "bezierova_krivulja.txt"
v = dohvatiPodatke(file)


@window.event
def on_draw():
    global v
    glColor3f(1.0, 1.0, 1.0)
    glColor3f(0.0, 1.0, 1.0)
    crtajKrivulju(v)


pyglet.app.run()


