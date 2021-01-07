from pyglet.gl import *
from pyglet.window import *


class tijelo:
    A = []
    B = []
    C = []
    D = []

    def __init__(self, vrhovi, poligoni):
        self.vrhovi = vrhovi
        self.poligoni = poligoni
        self.odrediKoeficjente()


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




def dohvatiImeDatotekeIPostavke(odabir):
    # m sluzi za skaliranje, n sluzi za translatiranje

    if (odabir == 1): file = "tetrahedron.obj.txt" ; n=0.2; m=300
    elif (odabir == 2): file = "kocka.obj.txt" ; n=10; m=50
    elif (odabir == 3): file = "teddy.obj.txt" ; n=30; m=15
    elif (odabir == 4): file = "bird.obj.txt" ; n=1; m=450
    elif (odabir == 5): file = "teapot.obj.txt" ; n=4; m=110
    elif (odabir == 6): file = "frog.obj.txt" ; n=10; m=45
    elif (odabir == 7): file = "all.obj.txt" ; n=0.9; m=510
    elif (odabir == 8): file = "bull.obj.txt" ; n=2350; m=0.16
    elif (odabir == 9): file = "porsche.obj.txt" ; n=0.45; m=1000
    elif (odabir == 10): file = "arena.obj.txt" ; n=0.755; m=600
    elif (odabir == 11): file = "dragon.obj.txt" ; n=0.55; m=800
    elif (odabir == 12): file = "tsd00.obj.txt" ; n=0.6; m=760
    elif (odabir == 13): file = "skull.obj.txt" ; n=0.5; m=900
    else : file = "kocka.obj.txt" ; n=1; m=300

    return file, n, m


def dohvatiPodatke(file):
    vrhovi = []
    poligoni = []
    data = open(file)

    for line in data:
        if (line.startswith("v")):
            vrijednosti = line.split(" ")
            vrhovi.append((float(vrijednosti[1]), float(vrijednosti[2]), float(vrijednosti[3])))
        elif (line.startswith("f")):
            vrijednosti = line.split(" ")
            poligoni.append((int(vrijednosti[1])-1, int(vrijednosti[2])-1, int(vrijednosti[3])-1))

    return vrhovi, poligoni


def odnosTockeITijela(tijelo, tocka):
    for i in range(len(tijelo.poligoni)):
        if ((tijelo.A[i]*tocka[0] + tijelo.B[i]*tocka[1] + tijelo.C[i]*tocka[2] + tijelo.D[i]) > 0):
            print ("Točka V je izvan tijela!")
            return
    print("Točka V je unutar tijela!")



def crtajTijelo(tijelo, n, m):

    for poligon in tijelo.poligoni:
        # uparujemo vrhove sa indexima poligona
        par1 = tijelo.vrhovi[poligon[0]]  # oblik parN je (0.0, 0.0, 0.0)
        par2 = tijelo.vrhovi[poligon[1]]
        par3 = tijelo.vrhovi[poligon[2]]

        glBegin(GL_LINES)
        glVertex2i(int((par1[0] + n) * m), int((par1[1] + n) * m))
        glVertex2i(int((par2[0] + n) * m), int((par2[1] + n) * m))

        glVertex2i(int((par2[0] + n) * m), int((par2[1] + n) * m))
        glVertex2i(int((par3[0] + n) * m), int((par3[1] + n) * m))

        glVertex2i(int((par3[0] + n) * m), int((par3[1] + n) * m))
        glVertex2i(int((par1[0] + n) * m), int((par1[1] + n) * m))
        glEnd()



window = pyglet.window.Window(900, 900)
window.set_location(20, 40)
window.set_caption('Tijelo')
print("\tTetrahedron - 1"); print("\tKocka - 2"); print("\tTeddy - 3")
print("\tBird - 4"); print("\tTeapot - 5"); print("\tFrog - 6")
print("\tAll - 7"); print("\tBull - 8"); print("\tPorsche - 9")
print("\tArena - 10"); print("\tDragon - 11"); print("\tTample - 12")
print("\tSkull - 13")
odabir = int(input("Odaberite tijelo: "))

tockaV = [int(pom) for pom in input("Unesite koordinate tocke V: ").split(" ")]

file, n, m = dohvatiImeDatotekeIPostavke(odabir)
v, p = dohvatiPodatke(file)
mojeTijelo = tijelo(v, p)
odnosTockeITijela(mojeTijelo, tockaV) # Isprobaj za kocku: (0,0,1) ili (1,1,1)

@window.event
def on_draw():
    global mojeTijelo, n, m
    crtajTijelo(mojeTijelo, n, m)



pyglet.app.run()


