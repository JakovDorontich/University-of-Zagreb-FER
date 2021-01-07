from pyglet.gl import *
import numpy as num
from numpy import arange
from math import *
from time import sleep


# Ucitavanje kontrolnih tocaka krivulje
def ucitaj_tocke():
    kontrolneTocke = []
    n = 0
    file = open("kontrolne_tocke_spirala.txt", "r")
    for line in file:
        n = n + 1
        parts = line.split(" ")
        t =[float(parts[0]), float(parts[1]), float(parts[2])]
        kontrolneTocke.append(t)
    return kontrolneTocke

# Izracun tocaka krivulje preko kontrolnih tocaka
def krivulja(kontrolneTocke):
    global r, n, tockeKrivulje, B, derB, vektoriOrijentacije, der2B, der2pit
    for i in range(0, n - 3):
        R = num.row_stack((kontrolneTocke[i], kontrolneTocke[i + 1], kontrolneTocke[i + 2], kontrolneTocke[i + 3]))
        for t in arange(0, 1, 0.015):
            T = num.array([t**3, t**2, t, 1])
            pi = num.matmul(num.matmul(T, B), R)
            tockeKrivulje.append(pi)
            
            derT = num.array([t**2, t, 1])
            derPi = num.matmul(num.matmul(derT, derB), R)
            vektoriOrijentacije.append(derPi)
            
            der2T = num.array([t, 1])
            der2Pi = num.matmul(num.matmul(der2T,der2B), R)
            der2pit.append(der2Pi)

# Ucitavanje objekta(vrhovi i poligoni)
def obj_reader():
    global vrhovi, poligoni
    file = open("kocka.obj","r")
    for line in file:
        parts = line.split(" ")
        if parts[0]=="v":
            new=float( parts[3].replace("\n","") )
            t=num.array([float(parts[1]),float(parts[2]),new, 1])
            vrhovi.append(t)
        elif parts[0]=="f":
            new = int(parts[3].replace("\n", ""))
            p=(int(parts[1])-1,int(parts[2])-1,new-1)
            poligoni.append(p)



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

a = num.array([-1, 3 ,-3, 1])
b = num.array([1, -2, 1, 0])
der2B = num.row_stack((a, b))

# Citanje iz datoteke
r = ucitaj_tocke()
n = len(r)
i = 0

# Izracun tocaka i tangenti na B-spline krivulju
tockeKrivulje=[]
vektoriOrijentacije=[]
der2pit = []
krivulja(r)

# Citanje objekta
vrhovi=[]
poligoni=[]
obj_reader()

# Stvaranje platna i iscrtavanje
width = 800
height = 800
window = pyglet.window.Window(width, height)
window.set_location(100, 100)
window.set_caption('OpenGL 3D B-spline')

def draw(i):
    global vrhovi, poligoni, tockeKrivulje, counter, vektoriOrijentacije, der2pit, inicijalnaRot
    glClearColor(0.0, 0.0, 0.0, 0.0)
    glColor3f(1.0, 1.0, 1.0)
    glClear(GL_COLOR_BUFFER_BIT)

    glMatrixMode(GL_PROJECTION)
    glLoadIdentity()
    glOrtho(-10, 45, -10, 45, -1000, 1000)
    scale = 3

    tk = tockeKrivulje[i]
    newVrhovi = []

    glTranslatef(12.5, -10.0, 0.0)
    glRotatef(90, -1.0, 0.0, 0.0)

    
    w = vektoriOrijentacije[i]
    u = num.cross(vektoriOrijentacije[i], der2pit[i])
    v = num.cross(w,u)
    u = u / num.linalg.norm(u)
    v = v / num.linalg.norm(v)
    w = w / num.linalg.norm(w)
    DCM = num.column_stack((w,v,u))
    DCMinv = num.linalg.inv(DCM)
    for v in vrhovi:
        # Skaliranje vrhova
        V = num.array([v[0]* scale, v[1]* scale, v[2]* scale, 1])
        # Rotacija vrhova
        V = num.array([V[0], V[1], V[2]])
        V = num.matmul(V, DCMinv)
        # Translacija vrhova
        V = num.array([V[0], V[1], V[2]])
        V = num.array([V[0]+tk[0], V[1]+tk[1], V[2]+tk[2], 1])
        newVrhovi.append(V)
    

    '''
    # Translacija vrhova
    glTranslatef(tk[0], tk[1], tk[2])
    # Rotacija vrhova
    s = num.array([0.0, 0.0, 1.0])
    e = vektoriOrijentacije[i]
    cosfi = num.dot(s, e) / (num.linalg.norm(s) * num.linalg.norm(e))
    angle = degrees(acos(cosfi))
    os = num.cross(s, e)
    glRotatef(angle, os[0], os[1], os[2])
    # Skaliranje vrhova
    glScalef(scale, scale, scale)
    for v in vrhovi:
        V = num.array([v[0] , v[1] , v[2] , 1])
        newVrhovi.append(V)
    '''

    # Crtanje objekta
    glBegin(GL_LINES)
    for p in poligoni:
        v1 = (newVrhovi[p[0]])
        v2 = (newVrhovi[p[1]])
        v3 = (newVrhovi[p[2]])

        glVertex3f(v1[0], v1[1], v1[2])
        glVertex3f(v2[0], v2[1], v2[2])
        glVertex3f(v2[0], v2[1], v2[2])
        glVertex3f(v3[0], v3[1], v3[2])
        glVertex3f(v3[0], v3[1], v3[2])
        glVertex3f(v1[0], v1[1], v1[2])
    glEnd()

    # Crtanje krivulje
    glBegin(GL_LINE_STRIP)
    for i in range(0, len(tockeKrivulje) - 1):
        pocTocka = tockeKrivulje[i]
        glVertex3f(pocTocka[0], pocTocka[1], pocTocka[2])
    glEnd()

    # Crtanje tangenta na krivulju
    glColor3f(0.0, 1.0, 1.0)
    glBegin(GL_LINES)
    for i in range (0, len(tockeKrivulje)-1, 10):
        pocTocka = tockeKrivulje[i]
        zavrsnaTocka = pocTocka + vektoriOrijentacije[i]*0.50
        glVertex3f(pocTocka[0], pocTocka[1], pocTocka[2])
        glVertex3f(zavrsnaTocka[0], zavrsnaTocka[1], zavrsnaTocka[2])
    glEnd()




@window.event
def on_draw():
    global i
    if (i < len(tockeKrivulje)-3):
        window.clear()
        draw(i)
        sleep(0.005)
        i += 1
    else:
        window.clear()
        draw(len(tockeKrivulje)-2)
        i=0



def update(x,y):
    pass

pyglet.clock.schedule(update, 10)
pyglet.app.run()





