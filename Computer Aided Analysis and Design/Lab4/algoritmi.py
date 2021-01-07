from random import random, randint
from math import log10, floor, ceil
from copy import copy

def genetski(F, var, interval, n=20, Pm=0.1, eval=100000, epsilon=0.000001, p=2, odabir=0):
    if (odabir == 0):
        binarno(F, var, interval, n, p, Pm, eval, epsilon)
    else:
        pomicna_tocka(F, var, interval, n, Pm, eval, epsilon)


# ******************* Prikaz s pomicnom tockom **************************
def pomicna_tocka(F, var, interval, n=20, Pm=0.1, eval=100000, epsilon=0.000001, turnir=3):
    populacija = []
    for i in range(n):
        jedinka = []
        for j in range(var):
            R = (interval[1]-interval[0])*random() + interval[0]
            jedinka.append(R)
        populacija.append(jedinka)
    stop = 0
    while(stop <= eval):
        indexi = set()
        while (1):
            indexi.add( randint(0, n-1) )
            if (len(indexi) == turnir): break
        indexi = list(indexi)
        indexi = sortiraj_indekse(F, populacija, indexi, turnir)
        stop += 3

        nova_jedinka = krizanje(populacija, indexi, interval, nacin=1)

        mutacija = random()
        if (mutacija < Pm):
            nova_jedinka = []
            for j in range(var):
                R = (interval[1] - interval[0]) * random() + interval[0]
                nova_jedinka.append(R)

        populacija[indexi[2]] = nova_jedinka

        if (F(naj_jedinka(F, populacija)) <= epsilon): break

    jedinka = naj_jedinka(F, populacija)
    return jedinka, round(F(jedinka),6), stop-2


def sortiraj_indekse(F, populacija, indexi, turnir):
    ind = {}
    for i in range(turnir):
        ind[indexi[i]] = F(populacija[indexi[i]])
    sorted_ind = sorted(ind.items(), key=lambda item: item[1])
    return [sorted_ind[0][0], sorted_ind[1][0], sorted_ind[2][0]]


def krizanje(populacija, indexi, interval, nacin=0):
    # Aritmeticko krizanje
    if (nacin == 0):
        alfa = random()
        D = []
        for i in range(len(populacija[0])):
            D.append( alfa*populacija[indexi[0]][i] + (1-alfa)*populacija[indexi[1]][i] )
    # Heuristicko krizanje
    else:
        alfa = random()
        D = []
        for i in range(len(populacija[0])):
            D.append( populacija[indexi[0]][i] + alfa*(populacija[indexi[0]][i]-populacija[indexi[1]][i]) )
        D = provjeri_ekspl(D, interval)
    return D


def provjeri_ekspl(X, interval):
    D = X.copy()
    for i in range(len(D)):
        if (D[i] < interval[0]):
            D[i] = interval[0]
        elif (D[i] > interval[1]):
            D[i] = interval[1]
    return D


def naj_jedinka(F, populacija):
    popu = {}
    for i in range(len(populacija)):
        popu[i] = F(populacija[i])
    sorted_popu = sorted(popu.items(), key=lambda item: item[1])

    inex = sorted_popu[0][0]
    return populacija[inex]


# ******************* Binarni prikaz **************************
def binarno(F, var, interval, n=20, p=2, Pm=0.1, eval=10000, epsilon=0.000001, turnir=3):
    br_bitova = ceil( log10(floor(1+(interval[1]-interval[0])*10**p)) / log10(2) )
    populacija = []
    for i in range(n):
        jedinka = []
        for j in range(var):
            bitovi=""
            for k in range(br_bitova):
                bitovi += str(randint(0,1))
            jedinka.append(bitovi)
        populacija.append(jedinka)

    stop = 0
    while(stop <= eval):
        indexi = set()
        while (1):
            indexi.add( randint(0, n-1) )
            if (len(indexi) == turnir): break
        indexi = list(indexi)
        indexi = sortiraj_indekse(F, populacija_btf(populacija,interval,p), indexi, turnir)
        stop += 3

        nova_jedinka = krizanje_b(populacija, indexi, br_bitova)

        mutacija = random()
        if (mutacija < Pm):
            nova_jedinka = []
            for j in range(var):
                bitovi = ""
                for k in range(br_bitova):
                    bitovi += str(randint(0, 1))
                nova_jedinka.append(bitovi)

        populacija[indexi[2]] = nova_jedinka

        if (F(naj_jedinka(F, populacija_btf(populacija, interval, p))) <= epsilon): break

    jedinka = naj_jedinka(F, populacija_btf(populacija, interval, p))
    return jedinka, round(F(jedinka),p), stop-2



def krizanje_b(populacija, indexi, n, nacin=0):
    # Krizanje s 1 tockom prekida
    if (nacin == 0):
        i = randint(1, n-1)
        A = populacija[indexi[0]]
        B = populacija[indexi[1]]
        D = []
        for k in range(len(A)):
            D.append( A[k][0:i] + B[k][i:n] )
    # Krizanje s 2 tocke prekida
    else:
        prekidi = set()
        while (1):
            prekidi.add(randint(1, n-1))
            if (len(prekidi) == 2): break
        prekidi = sorted(prekidi)
        i, j = prekidi
        A = populacija[indexi[0]]
        B = populacija[indexi[1]]
        D = []
        for k in range(len(A)):
            D.append(A[k][0:i] + B[k][i:j] + A[k][j:n])
    return D


def bit_to_float(bitovi, interval, p):
    b = int(bitovi, 2)
    n = len(bitovi)
    x = interval[0] + b/(2**n - 1) * (interval[1]-interval[0])
    return round(x, p)

def populacija_btf(populacija, interval, p):
    real_populacija = []
    for i in range(len(populacija)):
        real=[]
        for j in range(len(populacija[0])):
            real.append(bit_to_float(populacija[i][j], interval, p))
        real_populacija.append(real)
    return real_populacija

def float_to_bit(x, interval, n):
    b = (x-interval[0])/(interval[1]-interval[0]) * (2**n - 1)
    b = round(b)
    b = bin(b).replace("0b", "")
    if (len(b) < n):
        razlika = n-len(b)
        pocetak=""
        for i in range(razlika):
            pocetak+="0"
        b = pocetak + b
    return b

def populacija_ftb(populacija, interval, n):
    bit_populacija = []
    for i in range(len(populacija)):
        bitovi=[]
        for j in range(len(populacija[0])):
            bitovi.append(float_to_bit(populacija[i][j], interval, n))
        bit_populacija.append(bitovi)
    return bit_populacija


