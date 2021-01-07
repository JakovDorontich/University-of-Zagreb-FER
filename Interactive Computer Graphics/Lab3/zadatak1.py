from pyglet.gl import *
from pyglet.window import *
from math import *
import numpy


class tijelo:

    def __init__(self, vrhovi, poligoni):
        self.vrhovi = vrhovi
        self.poligoni = poligoni


def dohvatiPodatke(file):
    ociste = []
    glediste = []
    vrhovi = []
    poligoni = []
    data = open(file)

    for line in data:
        if (line.startswith("o")):
            vrijednosti = line.split(" ")
            ociste.append(int(vrijednosti[1]))
            ociste.append(int(vrijednosti[2]))
            ociste.append(int(vrijednosti[3]))
        elif (line.startswith("g")):
            vrijednosti = line.split(" ")
            glediste.append(int(vrijednosti[1]))
            glediste.append(int(vrijednosti[2]))
            glediste.append(int(vrijednosti[3]))
        elif (line.startswith("v")):
            vrijednosti = line.split(" ")
            vrhovi.append((float(vrijednosti[1]), float(vrijednosti[2]), float(vrijednosti[3])))
        elif (line.startswith("f")):
            vrijednosti = line.split(" ")
            poligoni.append((int(vrijednosti[1])-1, int(vrijednosti[2])-1, int(vrijednosti[3])-1))

    return ociste, glediste, vrhovi, poligoni


def odrediVrhoveZaCrtanje(tijelo, ociste, glediste):

    vrhoviZaCrtanje = []

    Ox, Oy, Oz = ociste
    Gx, Gy, Gz = glediste

    T1 = [[1, 0, 0, 0], [0, 1, 0, 0], [0, 0, 1, 0], [-Ox, -Oy, -Oz, 1]]
    G1 = numpy.matmul([Gx, Gy, Gz, 1], T1)
    cos_alfa = G1[0] / sqrt(G1[0] * G1[0] + G1[1] * G1[1])
    sin_alfa = G1[1] / sqrt(G1[0] * G1[0] + G1[1] * G1[1])
    T2 = [[cos_alfa, -sin_alfa, 0, 0], [sin_alfa, cos_alfa, 0, 0], [0, 0, 1, 0], [0, 0, 0, 1]]
    G2 = numpy.matmul(G1, T2)
    cos_beta = G2[2] / sqrt(G2[0] * G2[0] + G2[2] * G2[2])
    sin_beta = G2[0] / sqrt(G2[0] * G2[0] + G2[2] * G2[2])
    T3 = [[cos_beta, 0, sin_beta, 0], [0, 1, 0, 0], [-sin_beta, 0, cos_beta, 0], [0, 0, 0, 1]]
    T4 = [[0, -1, 0, 0], [1, 0, 0, 0], [0, 0, 1, 0], [0, 0, 0, 1]]
    T5 = [[-1, 0, 0, 0], [0, 1, 0, 0], [0, 0, 1, 0], [0, 0, 0, 1]]

    T = numpy.matmul(numpy.matmul(numpy.matmul(numpy.matmul(T1, T2), T3), T4), T5)

    H = sqrt((Ox - Gx)*(Ox - Gx) + (Oy - Gy)*(Oy - Gy) + (Oz - Gz)*(Oz - Gz))
    P = [[1, 0, 0, 0], [0, 1, 0, 0], [0, 0, 0, 1/H], [0, 0, 0, 0]]

    for vrh in tijelo.vrhovi:
        vrh_pom = numpy.matmul(numpy.matmul([vrh[0], vrh[1], vrh[2], 1], T), P)
        vrh_pom = numpy.multiply(vrh_pom, 1/vrh_pom[3])
        vrhoviZaCrtanje.append(vrh_pom)

    return vrhoviZaCrtanje


def crtajTijelo(tijelo, vrhoviZaCrtanje):
    n=20
    m=10

    for poligon in tijelo.poligoni:
        # uparujemo vrhove sa indexima poligona
        par1 = vrhoviZaCrtanje[poligon[0]]  # oblik parN je (0.0, 0.0, 0.0)
        par2 = vrhoviZaCrtanje[poligon[1]]
        par3 = vrhoviZaCrtanje[poligon[2]]

        glBegin(GL_LINES)
        glVertex2i(int((par1[0] + n) * m), int((par1[1] + n) * m))
        glVertex2i(int((par2[0] + n) * m), int((par2[1] + n) * m))

        glVertex2i(int((par2[0] + n) * m), int((par2[1] + n) * m))
        glVertex2i(int((par3[0] + n) * m), int((par3[1] + n) * m))

        glVertex2i(int((par3[0] + n) * m), int((par3[1] + n) * m))
        glVertex2i(int((par1[0] + n) * m), int((par1[1] + n) * m))
        glEnd()



window = pyglet.window.Window(800, 800)
window.set_location(20, 40)
window.set_caption('Tijelo')

file = "teddy.obj.txt"
o, g, v, p = dohvatiPodatke(file)
mojeTijelo = tijelo(v, p)

@window.event
def on_draw():
    window.clear()
    global o, g, v, p, mojeTijelo
    mojiVrhoviZaCrtanje = odrediVrhoveZaCrtanje(mojeTijelo, o, g)
    crtajTijelo(mojeTijelo, mojiVrhoviZaCrtanje)



@window.event
def on_key_press(symbol, modifiers):
    global o,g
    if symbol == key.W:
        o[1]+=2
    elif symbol == key.S:
        o[1]-=2
    elif symbol == key.D:
        o[0]+=2
    elif symbol == key.A:
        o[0]-=2

    elif symbol == key.I:
        g[1]+=2
    elif symbol == key.K:
        g[1]-=2
    elif symbol == key.L:
        g[0]+=2
    elif symbol == key.J:
        g[0]-=2

    print("O(", o[0], o[1], o[2], ")")
    print("G(", g[0], g[1], g[2], ")")
    print ("----------------")
    glFlush()


pyglet.app.run()


