from math import sqrt
from matrica import *
from copy import copy

# ******************* Postupak zlatnog reza **************************
def zlatni_rez(podatak, f, epsilon=0.000001, ispisi=False):
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
    if ispisi:
        print ("Postupak zlatnog reza:")
        ispis = " {:9} | {:9} | {:9} | {:9} || {:9} | {:9} | {:9} | {:9}".format("a", "c", "d", "b", "f(a)", "f(c)", "f(d)", "f(b)")
        print (ispis)
        print ("-"*95)
        ispis = " {:9} | {:9} | {:9} | {:9} || {:9} | {:9} | {:9} | {:9}".format(round(a, 6), round(c, 6), round(d, 6), round(b, 6), round(f(a), 6), round(fc, 6), round(fd, 6), round(f(b), 6))
        print(ispis)
    while ((b-a) > epsilon):
        if (fc < fd):
            b = d
            d = c
            c = b - k * (b - a)
            fd = fc
            fc = f(c)
            if ispisi:
                ispis = " {:9} | {:9} | {:9} | {:9} || {:9} | {:9} | {:9} | {:9}".format(round(a,6), round(c,6), round(d,6), round(b,6), round(f(a),6), round(fc,6), round(fd,6), round(f(b),6))
                print (ispis)
        else:
            a = c
            c = d
            d = a + k * (b - a)
            fc = fd
            fd = f(d)
            if ispisi:
                ispis = " {:8} | {:8} | {:8} | {:8} || {:8} | {:8} | {:8} | {:8}".format(round(a,6), round(c,6), round(d,6), round(b,6), round(f(a),6), round(fc,6), round(fd,6), round(f(b),6))
                print (ispis)
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

# *********************** Hooke-Jeeves postupak ************************
def hook_jeeves(x0, F, Dx=0.5, epsilon=0.000001):
    br_evaluacija = 0
    xp = matrica([x0])
    xb = matrica([x0])

    while (1):
        xn = istrazi(xp, F, Dx)
        if (F(xn.matrix[0]) < F(xb.matrix[0])):
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


def istrazi(xp, F, Dx):
    x = xp.copy()
    for i in range(x.numberOfColumns):
        P = F(x.matrix[0])
        x.matrix[0][i] = x.matrix[0][i] + Dx
        N = F(x.matrix[0])
        if (N > P):
            x.matrix[0][i] = x.matrix[0][i] - 2*Dx
            N = F(x.matrix[0])
            if (N > P):
                x.matrix[0][i] = x.matrix[0][i] + Dx
    return x


# ***************** Pretraživanje po koordinatnim osima **********************
def koordinatno_trazenje(x0, F, epsilon=0.000001):
    br_evaluacija = 0
    x = matrica([x0])
    while(1):
        xs = x.copy()
        for i in range(x.numberOfColumns):
            lambd =  get_lambda(x.matrix[0], F, i)
            x.matrix[0][i] += lambd
        br_evaluacija += 1
        if (norma(x - xs) <= epsilon): break

    for i in range(x.numberOfColumns):
        x.matrix[0][i] = round(x.matrix[0][i], 6)

    return x.matrix[0], br_evaluacija


def get_lambda(x, F, i):

    def f(lambd):
        izraz = []
        for j in range(len(x)):
            if (j == i):
                izraz.append(lambd+x[j])
            else:
                izraz.append(x[j])
        return F(izraz)

    interval, n = zlatni_rez(0, f)
    return round((interval[0]+interval[1]) / 2, 6)


def norma(x):
    suma = 0
    for vrijednost in x.matrix[0]:
        suma += vrijednost**2
    return sqrt(suma)


# ***************** Simpleks postupak po Nelderu i Meadu **********************
def nelder_meadu(x0, F, korak=1, alfa=1, beta=0.5, gama=2, sigma=0.5, epsilon=0.000001):
    br_evaluacija = 0
    X=[]
    X.append(x0)
    for i in range(len(x0)):
        pom = x0[:]
        pom[i] += korak
        X.append(pom)

    while(1):
        h, l = odredi_indekse(X, F)
        Xc = odredi_centroid(X, h)
        Xr = refleksija(X, Xc, h, alfa)
        if(F(Xr) < F(X[l])):
            Xe = ekspanzija(Xr, Xc, gama)
            if (F(Xe) < F(X[l])):
                X[h] = Xe
            else:
                X[h] = Xr
        else:
            brojac = 0
            zastavica = False
            for j in range(len(X)):
                if (j == h): continue
                if (F(Xr) > F(X[j])): brojac +=1
            if (brojac == len(X)-1): zastavica = True

            if (zastavica == True):
                if(F(Xr) < F(X[h])):
                    X[h] = Xr
                Xk = kontrakcija(X, Xc, h, beta)
                if (F(Xk) < F(X[h])):
                    X[h] = Xk
                else:
                    for j in range(len(X)):
                        if (j == l): continue
                        for i in range(len(X[j])):
                            X[j][i] = sigma * (X[l][i] + X[j][i])
            else:
                X[h] = Xr
        br_evaluacija += 1
        uvijet = uvijet_zaustavljanja(X, Xc, F)
        if (uvijet < epsilon or br_evaluacija >= 10000): break

    for i in range(len(Xc)):
        Xc[i] = round(Xc[i], 6)
    return Xc, br_evaluacija



def odredi_indekse(X, F):
    # h = točka sa najvecom vrijednoscu fje cilja
    h = 0
    l = 0
    for i in range(1, len(X)):
        if (F(X[h]) < F(X[i])):
            h = i
        if (F(X[l]) > F(X[i])):
            l = i
    return h, l

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

def refleksija(X, Xc, h, alfa):
    xh = matrica([X[h]])
    xc = matrica([Xc])
    Xr = xc.mulScalar(1+alfa) - xh.mulScalar(alfa)
    return Xr.matrix[0]

def ekspanzija(Xr, Xc, gama):
    xr = matrica([Xr])
    xc = matrica([Xc])
    Xe = xc.mulScalar(1-gama) + xr.mulScalar(gama)
    return Xe.matrix[0]

def kontrakcija(X, Xc, h, beta):
    xh = matrica([X[h]])
    xc = matrica([Xc])
    Xk = xc.mulScalar(1-beta) + xh.mulScalar(beta)
    return Xk.matrix[0]

def uvijet_zaustavljanja(X, Xc, F):
    n = len(X)
    suma = 0
    for j in range(len(X)):
        suma += (F(X[j])-F(Xc))**2
    suma = suma / n
    suma = sqrt(suma)
    return suma



