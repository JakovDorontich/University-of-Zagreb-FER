from pyglet.gl import *
from pyglet.window import *
from math import *


class fraktal:

    def __init__(self, eps, m, Umin, Umax, Vmin, Vmax):
        self.eps = eps
        self.m = m
        self.Umin = Umin
        self.Umax = Umax
        self.Vmin = Vmin
        self.Vmax = Vmax


def dohvatiPodatke(file, odabir):
    data = open(file)

    if (odabir == 1):
        for line in data:
            if (line.startswith("M_eps")):
                vrijednosti = line.split(" ")
                M_eps = int(vrijednosti[1])
            elif (line.startswith("M_m")):
                vrijednosti = line.split(" ")
                M_m = int(vrijednosti[1])
            elif (line.startswith("M_Umin")):
                vrijednosti = line.split(" ")
                M_Umin = float(vrijednosti[1])
            elif (line.startswith("M_Umax")):
                vrijednosti = line.split(" ")
                M_Umax = float(vrijednosti[1])
            elif (line.startswith("M_Vmin")):
                vrijednosti = line.split(" ")
                M_Vmin = float(vrijednosti[1])
            elif (line.startswith("M_Vmax")):
                vrijednosti = line.split(" ")
                M_Vmax = float(vrijednosti[1])

        mojFraktal = fraktal(M_eps,M_m,M_Umin,M_Umax,M_Vmin,M_Vmax)
        return mojFraktal, 0, 0
    else:
        for line in data:
            if (line.startswith("J_eps")):
                vrijednosti = line.split(" ")
                J_eps = int(vrijednosti[1])
            elif (line.startswith("J_m")):
                vrijednosti = line.split(" ")
                J_m = int(vrijednosti[1])
            elif (line.startswith("J_Umin")):
                vrijednosti = line.split(" ")
                J_Umin = float(vrijednosti[1])
            elif (line.startswith("J_Umax")):
                vrijednosti = line.split(" ")
                J_Umax = float(vrijednosti[1])
            elif (line.startswith("J_Vmin")):
                vrijednosti = line.split(" ")
                J_Vmin = float(vrijednosti[1])
            elif (line.startswith("J_Vmax")):
                vrijednosti = line.split(" ")
                J_Vmax = float(vrijednosti[1])
            elif (line.startswith("J_Creal")):
                vrijednosti = line.split(" ")
                J_Creal = float(vrijednosti[1])
            elif (line.startswith("J_Cimag")):
                vrijednosti = line.split(" ")
                J_Cimag = float(vrijednosti[1])

        mojFraktal = fraktal(J_eps, J_m, J_Umin, J_Umax, J_Vmin, J_Vmax)
        return mojFraktal, J_Creal, J_Cimag


def crtajMandelbrot(fraktal, Xmax, Ymax):
    for x in range(Xmax + 1):
        for y in range(Ymax + 1):
            u = ((fraktal.Umax - fraktal.Umin) / Xmax) * x + fraktal.Umin
            v = ((fraktal.Vmax - fraktal.Vmin) / Ymax) * y + fraktal.Vmin

            k = -1
            c = complex(u, v)
            z = 0
            r = 0

            while (r < fraktal.eps and k < fraktal.m):
                k += 1
                z = z * z + c
                r = sqrt(z.real ** 2 + z.imag ** 2)

            if (k == fraktal.m):
                glColor3f(1.0, 1.0, 1.0)
                glBegin(GL_POINTS)
                glVertex2f(x,y)
                glEnd()
            else:
                glColor3f(1/k, 1/k, 1/k)
                glBegin(GL_POINTS)
                glVertex2f(x, y)
                glEnd()



def crtajJulije(fraktal, Creal, Cimag, Xmax, Ymax):
    c = complex(Creal, Cimag)
    for x in range(Xmax + 1):
        for y in range(Ymax + 1):
            u = ((fraktal.Umax - fraktal.Umin) / Xmax) * x + fraktal.Umin
            v = ((fraktal.Vmax - fraktal.Vmin) / Ymax) * y + fraktal.Vmin

            k = -1
            z = complex(u, v)
            r = 0

            while (r < fraktal.eps and k < fraktal.m):
                k += 1
                z = z * z + c
                r = sqrt(z.real ** 2 + z.imag ** 2)

            if (k == fraktal.m):
                glColor3f(1.0, 1.0, 1.0)
                glBegin(GL_POINTS)
                glVertex2f(x, y)
                glEnd()
            else:
                glColor3f(1/k, 1/k, 1/k)
                glBegin(GL_POINTS)
                glVertex2f(x, y)
                glEnd()



Xmax = 340
Ymax = 260

window = pyglet.window.Window(Xmax, Ymax)
window.set_location(20, 40)
window.set_caption('Fraktali')

odabir = int(input("Odaberite vrstu fraktalnog skupa:\n\t1-Mandelbrotov skup\n\t2-Julijev skup\n"))
file = "fraktali.txt"
mojFraktal, Creal, Cimag = dohvatiPodatke(file, odabir)

@window.event
def on_draw():
    window.clear()
    global odabir, mojFraktal, Creal, Cimag, Xmax, Ymax

    if (odabir == 1):
        crtajMandelbrot(mojFraktal, Xmax, Ymax)
    else:
        crtajJulije(mojFraktal, Creal, Cimag, Xmax, Ymax)




pyglet.app.run()


