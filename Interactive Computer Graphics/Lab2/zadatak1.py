import sys
from pyglet.gl import *
from pyglet.window import *
from pyglet.window import mouse

# Varijable: brVrhova i brTocak imaju ulogu logičkog toka
# izvođenja programa, tj. sluze za upravljanje pritiska misa

Xkoordinata = []
Ykoordinata = []
brVrhova = int(input("Koliko vrhova ima Vaš poligon: "))
brTocka = 0; xV = 0; yV = 0

class poligon:

    a = []
    b = []
    c = []
    lijevi = []

    def __init__(self, vrhX, vrhY):
        self.vrhX = vrhX
        self.vrhY = vrhY
        self.n = len(vrhX)
        self.racunajKoefPoligonKonv(self.n)


    def racunajKoefPoligonKonv(self, n):
        i0 = n - 1
        for i in range(n):
            self.a.insert(i0, (self.vrhY[i0] - self.vrhY[i]))
            self.b.insert(i0, (-(self.vrhX[i0] - self.vrhX[i])))
            self.c.insert(i0, (self.vrhX[i0] * self.vrhY[i] - self.vrhY[i0] * self.vrhX[i]))
            self.lijevi.insert(i0, (self.vrhY[i0] < self.vrhY[i]))
            i0 = i


def provjeriPoligonKonv(polel):
    ispod = iznad = 0
    i0 = polel.n - 2
    for i in range(polel.n):
        if (i0 >= polel.n): i0 = 0
        r = polel.a[i0] * polel.vrhX[i] + polel.b[i0] * polel.vrhY[i] + polel.c[i0]
        if (r > 0): iznad += 1
        #else: ispod +=1
        i0 += 1
    if (iznad == 0):
        return True
    else:
        return False


def crtajPoligonKonv(polel):
    i0 = polel.n - 1
    for i in range(0, polel.n):
        glBegin(GL_LINES)
        glVertex2i(polel.vrhX[i0], polel.vrhY[i0])
        glVertex2i(polel.vrhX[i], polel.vrhY[i])
        glEnd()
        i0 = i

def popuniPoligonKonv (polel):

    #polel.lijevi[1]=False

    # trazenje min i max koordinata
    xmin = xmax = polel.vrhX[0]
    ymin = ymax = polel.vrhY[0]
    for i in range (0, polel.n):
        if (xmin > polel.vrhX[i]): xmin = polel.vrhX[i]
        if (xmax < polel.vrhX[i]): xmax = polel.vrhX[i]
        if (ymin > polel.vrhY[i]): ymin = polel.vrhY[i]
        if (ymax < polel.vrhY[i]): ymax = polel.vrhY[i]
    # bojanje poligona
    for y in range(ymin, ymax+1):
        # pronadi najvece lijevo i najmanje desno sjeciste
        L, D = xmin, xmax
        i0 = polel.n - 1
        for i in range (0, polel.n):
            if (polel.a[i0] == 0):
                if (polel.vrhY[i0] == y):
                    if (polel.vrhX[i0] < polel.vrhX[i]):
                        L = polel.vrhX[i0]
                        D = polel.vrhX[i]
                    else:
                        L = polel.vrhX[i]
                        D = polel.vrhX[i0]
                    break
            else:
                x = (-(polel.b[i0]) * y - polel.c[i0])/polel.a[i0]
                if (polel.lijevi[i0]):
                    if (L < x): L = x
                else:
                    if (D > x): D = x
            i0 = (i0+1)%polel.n
        glBegin(GL_LINES)
        glVertex2i(round(L), y)
        glVertex2i(round(D), y)
        glEnd()


def odnosTockeIPoligona (polel, tocka):
    for i in range(polel.n):
        if ((tocka[0] * polel.a[i] + tocka[1] * polel.b[i] + polel.c[i]) > 0):
            print ("Točka V je izvan poligona!")
            return
    print("Točka V je unutar poligona!")



window=pyglet.window.Window(600, 600)
window.set_caption('Konveksni poligon')



@window.event
def on_draw():
    global brVrhova, brTocka, Xkoordinata, Ykoordinata, xV, yV

    glClear(GL_COLOR_BUFFER_BIT)
    glColor3f(0.0,1.0,1.0)
    glLoadIdentity()
    if (brVrhova == 0):
        poli1 = poligon(Xkoordinata, Ykoordinata)
        if (provjeriPoligonKonv(poli1) == False):
            print ("Zadali ste konkavan poligon ili ste krivim smijerom zadali vrhove")
            sys.exit(1)
        #poli1 = poligon([50, 150, 300, 100], [200, 350, 150, 50])
        popuniPoligonKonv(poli1)
        brTocka = 1

    if (brTocka == 2):
        window.clear()
        crtajPoligonKonv(poligon(Xkoordinata, Ykoordinata))
        glColor3f(1.0, 1.0, 1.0)
        glBegin(GL_POINTS)
        glVertex2i(xV, yV)
        glEnd()
        odnosTockeIPoligona(poligon(Xkoordinata, Ykoordinata), [xV, yV])
        brTocka += 1



@window.event
def on_mouse_press(x, y, button, modifiers):
    global brVrhova, brTocka, Xkoordinata, Ykoordinata, xV, yV
    if (button == mouse.LEFT and brVrhova > 0):
        brVrhova -= 1
        print ("Unesen je vrh (%d, %d) unesite još %d vrha" %(x, y, brVrhova))
        Xkoordinata.append(x)
        Ykoordinata.append(y)

    if (button == mouse.LEFT and brVrhova == 0 and brTocka == 1):
        brVrhova = -1
        brTocka = 2
        xV = x; yV = y
        print ("\nUnesena je ispitna točka V(%d, %d)" %(x, y))



pyglet.app.run()
