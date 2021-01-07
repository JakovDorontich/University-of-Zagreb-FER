from math import sqrt
from matrica import *
from random import random

# ******************* Postupak zlatnog reza **************************
def zlatni_rez(podatak, f, epsilon=0.000001):
    br_evaluacija = 0
    if (type(podatak) is not list):
        interval = unimodalni(podatak, 1, f)
    else:
        interval = podatak

    k = 0.5 * (sqrt(5) - 1)
    a = interval[0]
    b = interval[1]
    c = b - k * (b - a)
    d = a + k * (b - a)
    fc = f(c)
    fd = f(d)

    while ((b-a) > epsilon):
        if (fc < fd):
            b = d
            d = c
            c = b - k * (b - a)
            fd = fc
            fc = f(c)
        else:
            a = c
            c = d
            d = a + k * (b - a)
            fc = fd
            fd = f(d)
        br_evaluacija += 1

    return [round(a, 6), round(b, 6)], br_evaluacija


def unimodalni(pocetna_tocka, h, f):
    l = pocetna_tocka - h
    r = pocetna_tocka + h
    m = pocetna_tocka
    step = 1

    fm = f(m)
    fl = f(l)
    fr = f(r)
    if (fm < fr and fm < fl):
        return [l, r]
    elif (fm > fr):
        while (1):
            l = m
            m = r
            fm = fr
            step *= 2
            r = pocetna_tocka + (h * step)
            fr = f(r)
            if (fm < fr): break
    else:
        while(1):
            r = m
            m = l
            fm = fl
            step *= 2
            l = pocetna_tocka - (h * step)
            fl = f(l)
            if (fm < fl): break

    return [l, r]

# *********************** Gradijentni spust ************************
def gradijentini_spust(pocetna_tocka, F, dF, gold=False, epsilon=0.000001):
    br_grad = 0; br_fja = 1
    konv = 10000
    X0 = pocetna_tocka
    while(konv > 0):
        br_grad += 1
        grad = dF(X0)
        norma = 0
        for g in grad:
            norma += (g**2)
        norma = sqrt(norma)

        v = []
        for g in grad:
            v.append( -g/norma)

        if (gold == True):
            lambd, n = get_gold_lambda(X0, F, v)
            br_fja += n
        else:
            lambd = 1
        for i in range(len(X0)):
            X0[i] = X0[i]+lambd*v[i]
        konv -= 1
        if (norma < epsilon): break

    if (konv == 0): print("\t\tPostupak je zavrsio divergenciom.")
    return X0, round(F(X0),6), br_fja, br_grad


def get_gold_lambda(X, F, v):
    def f(lambd):
        izraz = []
        for i in range(len(X)):
            izraz.append(X[i] + lambd*v[i])
        return F(izraz)
    interval, n = zlatni_rez(0, f)
    return round((interval[0] + interval[1]) / 2, 6), n

# *********************** Newton-Raphsonov postupak ************************
def newton_raphson(pocetna_tocka, F, dF, hF, gold=False, epsilon=0.000001):
    br_grad = 0; br_fja = 1; br_hess = 0
    konv = 1000
    X0 = pocetna_tocka
    while(konv > 0):
        br_grad += 1
        grad = matrica(dF(X0))
        br_hess += 1
        hess_inv = inverse(matrica(hF(X0)))
        smjer = hess_inv*grad

        norma = 0
        for s in smjer.matrix:
            norma += (s[0]**2)
        norma = sqrt(norma)

        v = []
        for s in smjer.matrix:
            v.append( -s[0]/norma)

        if (gold == True):
            lambd, n = get_gold_lambda(X0, F, v)
            br_fja += n
        else:
            lambd = 1
        for i in range(len(X0)):
            X0[i] = X0[i]+lambd*v[i]

        konv -= 1
        if (norma < epsilon): break

    if (konv == 0): print("\t\tPostupak je zavrsio divergenciom.")
    return X0, round(F(X0), 6), br_fja, br_grad, br_hess

# *********************** Postupak po Boxu ************************
# eksplicitno ogranicenje: [-100, 100]
# implicitna ogranicenja: x2-x1 >= 0   and   2-x1 >= 0
def prov_ekspl(x):
    return (x >= -100) and (x <= 100)
def prov_impl(x1, x2):
    return (x2-x1 >= 0) and (2-x1 >= 0)

def box(pocetna_tocka, F):
    Xd = -100; Xg = 100
    konv = 1000
    Xc = pocetna_tocka; Xr = pocetna_tocka
    X = []
    for t in range(2*len(Xc)):
        Xi = []
        for i in range(len(Xc)):
            R = random()
            Xi.append(Xd + R*(Xg-Xd))
        if (prov_impl(Xi[0], Xi[1])==False):
            while(prov_impl(Xi[0], Xi[1])==False):
                Xi[0] = 0.5 * (Xi[0] + Xc[0])
                Xi[1] = 0.5 * (Xi[1] + Xc[1])
        X.append(Xi)

    while(konv > 0):
        h = odredi_najgoreg(X, F)
        Xc = odredi_centroid(X, h)
        Xr = [refleksija(Xc[0],X[h][0]), refleksija(Xc[1],X[h][1])]
        for i in range(len(Xc)):
            if(Xr[i] < Xd):
                Xr[i] = Xd
            elif(Xr[i] > Xg):
                Xr[i] = Xg
        if (prov_impl(Xr[0], Xr[1])==False):
            while(prov_impl(Xr[0], Xr[1])==False):
                Xr = [pomak(Xr[0],Xc[0]), pomak(Xr[1],Xc[1])]
        if (F(Xr) > F(X[h])):
            Xr = [pomak(Xr[0], Xc[0]), pomak(Xr[1], Xc[1])]
        X[h] = Xr
        konv -= 1

    return Xr, F(Xr)

def pomak(xr, xc):
    return 0.5*(xr+xc)

def refleksija(xc, xh, alfa=1):
    return (1+alfa)*xc - alfa*xh

def odredi_najgoreg(X, F):
    h = 0
    for i in range(1, len(X)):
        if (F(X[h]) < F(X[i])):
            h = i
    return h

def odredi_centroid(X, h):
    n = len(X) - 1
    suma = []
    for i in range(len(X[0])):
        suma.append(0.0)
    for i in range(len(X)):
        if (i == h):
            continue
        else:
            for j in range(len(X[i])):
                suma[j] = suma[j] + X[i][j]
    for i in range(len(suma)):
        suma[i] = suma[i] / n
    return suma


# ******** Postupak transformacije u problem bez ogranicenja ********
def mjesoviti_nacin(pocetna_tocka, U, unutar=True, parametar_t=1, epsilon=0.000001):
    t = parametar_t
    X0 = pocetna_tocka
    if (unutar == False):
        X0 = pronadi_tocku(pocetna_tocka, parametar_t)
        print ("\tPostupak pronalazenja unutarnje tocke je odredio novu pocetnu tocku: X0=", X0)
    Xm, n = hook_jeeves(X0, U, t)
    br_evaluacija = n

    udaljenost = sqrt((X0[0]-Xm[0])**2 + (X0[1]-Xm[1])**2)
    while (udaljenost > epsilon):
        X0 = Xm
        t *= 10
        Xm, n = hook_jeeves(X0, U, t)
        br_evaluacija += n
        udaljenost = sqrt((X0[0] - Xm[0]) ** 2 + (X0[1] - Xm[1]) ** 2)

    return Xm, U(Xm, t), br_evaluacija

def pronadi_tocku(pocetna_tocka, parametar_t):
    t = parametar_t
    X0 = pocetna_tocka

    def G(X, t):
        rj = 0
        if ((3-X[0]-X[1]) >= 0) and ((3+1.5*X[0]-X[1]) >= 0):
            rj += 0
        elif ((3-X[0]-X[1]) >= 0):
            rj += -t*(3+1.5*X[0]-X[1])
        else:
            rj += -t*(3-X[0]-X[1])
        return rj

    X0, n = hook_jeeves(X0, G, t)
    return X0




def hook_jeeves(x0, U, t, Dx=0.5, epsilon=0.000001):
    br_evaluacija = 0
    xp = matrica([x0])
    xb = matrica([x0])

    while (1):
        xn = istrazi(xp, U, t, Dx)
        if (U(xn.matrix[0],t) < U(xb.matrix[0],t)):
            xp = xn.mulScalar(2) - xb
            xb = xn
        else:
            Dx = Dx / 2
            xp = xb
        br_evaluacija += 1
        if (Dx < epsilon): break

    for i in range(xb.numberOfColumns):
        xb.matrix[0][i] = round(xb.matrix[0][i], 6)
    return xb.matrix[0], br_evaluacija


def istrazi(xp, U, t, Dx):
    x = xp.copy()
    for i in range(x.numberOfColumns):
        P = U(x.matrix[0],t)
        x.matrix[0][i] = x.matrix[0][i] + Dx
        N = U(x.matrix[0],t)
        if (N > P):
            x.matrix[0][i] = x.matrix[0][i] - 2*Dx
            N = U(x.matrix[0],t)
            if (N > P):
                x.matrix[0][i] = x.matrix[0][i] + Dx
    return x


