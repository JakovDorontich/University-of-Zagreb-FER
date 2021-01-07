from pyglet.gl import *
from pyglet.window import *
from math import *
import numpy


class tijelo:
    A = []
    B = []
    C = []
    D = []

    def __init__(self, vrhovi, poligoni):
        self.vrhovi = vrhovi
        self.poligoni = poligoni
        self.odrediKoeficjente()
        self.odrediRaspon()

    def odrediKoeficjente(self):
        for poligon in self.poligoni:
            # uparujemo vrhove sa indexima poligona
            par1 = self.vrhovi[poligon[0]]    # oblik parN je (0.0, 0.0, 0.0)
            par2 = self.vrhovi[poligon[1]]
            par3 = self.vrhovi[poligon[2]]

            self.A.append((par2[1]-par1[1])*(par3[2]-par1[2])-(par2[2]-par1[2])*(par3[1]-par1[1]))  # x = [0]
            self.B.append(-(par2[0]-par1[0])*(par3[2]-par1[2])+(par2[2]-par1[2])*(par3[0]-par1[0])) # y = [1]
            self.C.append((par2[0]-par1[0])*(par3[1]-par1[1])-(par2[1]-par1[1])*(par3[0]-par1[0]))  # z = [2]
            self.D.append(-par1[0]*self.A[-1] - par1[1]*self.B[-1] - par1[2]*self.C[-1])

    def odrediRaspon(self):
        par = self.vrhovi[0]
        Xmax = Xmin = par[0]
        Ymax = Ymin = par[1]
        Zmax = Zmin = par[2]
        for i in range(1, len(self.vrhovi)):
            par = self.vrhovi[i]
            if (Xmax < par[0]): Xmax = par[0]
            if (Xmin > par[0]): Xmin = par[0]
            if (Ymax < par[1]): Ymax = par[1]
            if (Ymin > par[1]): Ymin = par[1]
            if (Zmax < par[2]): Zmax = par[2]
            if (Zmin > par[2]): Zmin = par[2]

        self.Xmax = Xmax
        self.Xmin = Xmin
        self.Ymax = Ymax
        self.Ymin = Ymin
        self.Zmax = Zmax
        self.Zmin = Zmin



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


    velicina_x = tijelo.Xmax - tijelo.Xmin
    velicina_y = tijelo.Ymax - tijelo.Ymin
    velicina_z = tijelo.Zmax - tijelo.Zmin
    srediste_x = (tijelo.Xmax + tijelo.Xmin) / 2
    srediste_y = (tijelo.Ymax + tijelo.Ymin) / 2
    srediste_z = (tijelo.Zmax + tijelo.Zmin) / 2

    for vrh in tijelo.vrhovi:
        # --- smjesti tijelo u radni prostor [-1, 1] ---
        vrh_x = vrh[0] - srediste_x
        vrh_y = vrh[1] - srediste_y
        vrh_z = vrh[2] - srediste_z
        rasponMax = max(velicina_x, velicina_y, velicina_z)
        vrh_x *= 2 / rasponMax
        vrh_y *= 2 / rasponMax
        vrh_z *= 2 / rasponMax

        # --- transformacija pogleda i projekcija ---
        vrh_pom = numpy.matmul(numpy.matmul([vrh_x, vrh_y, vrh_z, 1], T), P)
        vrh_pom = numpy.multiply(vrh_pom, 1/vrh_pom[3])
        vrhoviZaCrtanje.append(vrh_pom)

    return vrhoviZaCrtanje


def crtajTijelo(tijelo, vrhoviZaCrtanje, ociste):
    n=2
    m=200

    for i in range(len(tijelo.poligoni)):

        vectorPO = numpy.array([ociste[0]-tijelo.A[i], ociste[1]-tijelo.B[i], ociste[2]-tijelo.C[i]])
        vectorN = numpy.array([tijelo.A[i], tijelo.B[i], tijelo.C[i]])

        alfa = degrees(acos(numpy.sum(vectorPO*vectorN)/(numpy.linalg.norm(vectorPO)*numpy.linalg.norm(vectorN))))

        if (alfa <= 90):
            v1, v2, v3 = tijelo.poligoni[i]
            par1=vrhoviZaCrtanje[v1]
            par2=vrhoviZaCrtanje[v2]
            par3=vrhoviZaCrtanje[v3]

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
window.set_caption('Tijelo: bez straznjih poligona')

file = "teddy.obj.txt"
o, g, v, p = dohvatiPodatke(file)
mojeTijelo = tijelo(v, p)

@window.event
def on_draw():
    window.clear()
    global o, g, v, p, mojeTijelo
    mojiVrhoviZaCrtanje = odrediVrhoveZaCrtanje(mojeTijelo, o, g)
    crtajTijelo(mojeTijelo, mojiVrhoviZaCrtanje, o)



@window.event
def on_key_press(symbol, modifiers):
    global o,g
    if symbol == key.W:
        o[1] += 4
    elif symbol == key.S:
        o[1] -= 4
    elif symbol == key.D:
        o[0] += 4
    elif symbol == key.A:
        o[0] -= 4
    elif symbol == key.Q:
        o[2] += 4
    elif symbol == key.E:
        o[2] -= 4

    elif symbol == key.I:
        g[1] += 2
    elif symbol == key.K:
        g[1] -= 2
    elif symbol == key.L:
        g[0] += 2
    elif symbol == key.J:
        g[0] -= 2

    print("O(", o[0], o[1], o[2], ")")
    print("G(", g[0], g[1], g[2], ")")
    print ("----------------")
    glFlush()


pyglet.app.run()


