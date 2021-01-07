from algoritmi import *
from math import sqrt, sin

def F1(X):
    return 100*(X[1]-X[0]**2)**2 + (1-X[0])**2
def F2(X):
    return (X[0]-4)**2 + 4*((X[1]-2)**2)
def F3(X, i=5):
    suma = 0
    for n in range(1, i+1):
        suma += (X[n-1]-n)**2
    return suma

def F6(X):
    i = len(X)
    suma = 0
    for n in range(i):
        suma += (X[n])**2
    return 0.5 + (sin(sqrt(suma))**2-0.5) / (1+0.001*suma)**2

def F7(X):
    i = len(X)
    suma = 0
    for n in range(i):
        suma += (X[n])**2
    return suma**0.25 * (1+sin(50*(suma**0.1))**2)



print("Zadatak 1.\n")
print("Funkcija 1:")
print("Binarni prikaz:")
minimum, fja, evalf = binarno(F1, 2, [-50, 150], n=50, p=3, Pm=0.1, eval=10000)
print("\tMinimum je u tocki: ", minimum)
print("\tVrijednost funkcije cilja u tocki: ", fja)
print("\tBroj evaluacija funkcije cilja: ", evalf)
print("Prikaz s pomicnom tockom:")
minimum, fja, evalf = pomicna_tocka(F1, 2, [-50, 150], n=30, Pm=0.1, eval=100000)
print("\tMinimum je u tocki: ", minimum)
print("\tVrijednost funkcije cilja u tocki: ", fja)
print("\tBroj evaluacija funkcije cilja: ", evalf)

print("\nFunkcija 3:")
print("Binarni prikaz:")
minimum, fja, evalf = binarno(F3, 5, [-50, 150], n=40, p=3, Pm=0.1, eval=10000)
print("\tMinimum je u tocki: ", minimum)
print("\tVrijednost funkcije cilja u tocki: ", fja)
print("\tBroj evaluacija funkcije cilja: ", evalf)
print("Prikaz s pomicnom tockom:")
minimum, fja, evalf = pomicna_tocka(F3, 5, [-50, 150], n=30, Pm=0.1, eval=100000)
print("\tMinimum je u tocki: ", minimum)
print("\tVrijednost funkcije cilja u tocki: ", fja)
print("\tBroj evaluacija funkcije cilja: ", evalf)

print("\nFunkcija 6:")
print("Binarni prikaz:")
minimum, fja, evalf = binarno(F6, 2, [-50, 150], n=40, p=3, Pm=0.1, eval=10000)
print("\tMinimum je u tocki: ", minimum)
print("\tVrijednost funkcije cilja u tocki: ", fja)
print("\tBroj evaluacija funkcije cilja: ", evalf)
print("Prikaz s pomicnom tockom:")
minimum, fja, evalf = pomicna_tocka(F6, 2, [-50, 150], n=30, Pm=0.1, eval=100000)
print("\tMinimum je u tocki: ", minimum)
print("\tVrijednost funkcije cilja u tocki: ", fja)
print("\tBroj evaluacija funkcije cilja: ", evalf)

print("\nFunkcija 7:")
print("Binarni prikaz:")
minimum, fja, evalf = binarno(F7, 2, [-50, 150], n=40, p=3, Pm=0.1, eval=10000)
print("\tMinimum je u tocki: ", minimum)
print("\tVrijednost funkcije cilja u tocki: ", fja)
print("\tBroj evaluacija funkcije cilja: ", evalf)
print("Prikaz s pomicnom tockom:")
minimum, fja, evalf = pomicna_tocka(F7, 2, [-50, 150], n=30, Pm=0.1, eval=100000)
print("\tMinimum je u tocki: ", minimum)
print("\tVrijednost funkcije cilja u tocki: ", fja)
print("\tBroj evaluacija funkcije cilja: ", evalf)


print("--------------------------------------------------------------------------")
print("Zadatak 2.\n")
print("Funkcija 6:")
n=30; Pm=0.1; eval=100000
print("Dimenzionalnost funkcije je 1")
minimum, fja, evalf = pomicna_tocka(F6, 1, [-50, 150], n, Pm, eval)
print("\tMinimum je u tocki: ", minimum)
print("\tVrijednost funkcije cilja u tocki: ", fja)
print("\tBroj evaluacija funkcije cilja: ", evalf)
print("Dimenzionalnost funkcije je 3")
minimum, fja, evalf = pomicna_tocka(F6, 3, [-50, 150], n, Pm, eval)
print("\tMinimum je u tocki: ", minimum)
print("\tVrijednost funkcije cilja u tocki: ", fja)
print("\tBroj evaluacija funkcije cilja: ", evalf)
print("Dimenzionalnost funkcije je 6")
minimum, fja, evalf = pomicna_tocka(F6, 6, [-50, 150], n, Pm, eval)
print("\tMinimum je u tocki: ", minimum)
print("\tVrijednost funkcije cilja u tocki: ", fja)
print("\tBroj evaluacija funkcije cilja: ", evalf)
print("Dimenzionalnost funkcije je 10")
minimum, fja, evalf = pomicna_tocka(F6, 10, [-50, 150], n, Pm, eval)
print("\tMinimum je u tocki: ", minimum)
print("\tVrijednost funkcije cilja u tocki: ", fja)
print("\tBroj evaluacija funkcije cilja: ", evalf)

print("\nFunkcija 7:")
print("Dimenzionalnost funkcije je 1")
minimum, fja, evalf = pomicna_tocka(F7, 1, [-50, 150], n, Pm, eval)
print("\tMinimum je u tocki: ", minimum)
print("\tVrijednost funkcije cilja u tocki: ", fja)
print("\tBroj evaluacija funkcije cilja: ", evalf)
print("Dimenzionalnost funkcije je 3")
minimum, fja, evalf = pomicna_tocka(F7, 3, [-50, 150], n, Pm, eval)
print("\tMinimum je u tocki: ", minimum)
print("\tVrijednost funkcije cilja u tocki: ", fja)
print("\tBroj evaluacija funkcije cilja: ", evalf)
print("Dimenzionalnost funkcije je 6")
minimum, fja, evalf = pomicna_tocka(F7, 6, [-50, 150], n, Pm, eval)
print("\tMinimum je u tocki: ", minimum)
print("\tVrijednost funkcije cilja u tocki: ", fja)
print("\tBroj evaluacija funkcije cilja: ", evalf)
print("Dimenzionalnost funkcije je 10")
minimum, fja, evalf = pomicna_tocka(F7, 10, [-50, 150], n, Pm, eval)
print("\tMinimum je u tocki: ", minimum)
print("\tVrijednost funkcije cilja u tocki: ", fja)
print("\tBroj evaluacija funkcije cilja: ", evalf)


print("--------------------------------------------------------------------------")
print("Zadatak 3.\n")
n=10; p=4; Pm=0.1; eval=100000

print("Funkcija 6:")
print("Dimenzionalnost funkcije je 3")
print("\tBinarni prikaz:")
minimum, fja, evalf = binarno(F6, 3, [-50, 150], n, p, Pm, eval)
print("\t\tMinimum je u tocki: ", minimum)
print("\t\tVrijednost funkcije cilja u tocki: ", fja)
print("\t\tBroj evaluacija funkcije cilja: ", evalf)
print("\tPrikaz s pomicnom tockom:")
minimum, fja, evalf = pomicna_tocka(F6, 3, [-50, 150], n, Pm, eval)
print("\t\tMinimum je u tocki: ", minimum)
print("\t\tVrijednost funkcije cilja u tocki: ", fja)
print("\t\tBroj evaluacija funkcije cilja: ", evalf)

print("Dimenzionalnost funkcije je 6")
print("\tBinarni prikaz:")
minimum, fja, evalf = binarno(F6, 6, [-50, 150], n, p, Pm, eval)
print("\t\tMinimum je u tocki: ", minimum)
print("\t\tVrijednost funkcije cilja u tocki: ", fja)
print("\t\tBroj evaluacija funkcije cilja: ", evalf)
print("\tPrikaz s pomicnom tockom:")
minimum, fja, evalf = pomicna_tocka(F6, 6, [-50, 150], n, Pm, eval)
print("\t\tMinimum je u tocki: ", minimum)
print("\t\tVrijednost funkcije cilja u tocki: ", fja)
print("\t\tBroj evaluacija funkcije cilja: ", evalf)

print("\nFunkcija 7:")
print("Dimenzionalnost funkcije je 3")
print("\tBinarni prikaz:")
minimum, fja, evalf = binarno(F7, 3, [-50, 150], n, p, Pm, eval)
print("\t\tMinimum je u tocki: ", minimum)
print("\t\tVrijednost funkcije cilja u tocki: ", fja)
print("\t\tBroj evaluacija funkcije cilja: ", evalf)
print("\tPrikaz s pomicnom tockom:")
minimum, fja, evalf = pomicna_tocka(F7, 3, [-50, 150], n, Pm, eval)
print("\t\tMinimum je u tocki: ", minimum)
print("\t\tVrijednost funkcije cilja u tocki: ", fja)
print("\t\tBroj evaluacija funkcije cilja: ", evalf)

print("Dimenzionalnost funkcije je 6")
print("\tBinarni prikaz:")
minimum, fja, evalf = binarno(F7, 6, [-50, 150], n, p, Pm, eval)
print("\t\tMinimum je u tocki: ", minimum)
print("\t\tVrijednost funkcije cilja u tocki: ", fja)
print("\t\tBroj evaluacija funkcije cilja: ", evalf)
print("\tPrikaz s pomicnom tockom:")
minimum, fja, evalf = pomicna_tocka(F7, 6, [-50, 150], n, Pm, eval)
print("\t\tMinimum je u tocki: ", minimum)
print("\t\tVrijednost funkcije cilja u tocki: ", fja)
print("\t\tBroj evaluacija funkcije cilja: ", evalf)


print("--------------------------------------------------------------------------")
print("Zadatak 4.\n")
Pm=0.25; eval=10000
print("Funkcija 6:")
print("Dimenzionalnost funkcije je 2")
for n in [10, 30, 50, 100, 200, 500, 1000]:
    print("\tVelicina populacije:", n)
    minimum, fja, evalf = pomicna_tocka(F6, 2, [-50, 150], n, Pm, eval)
    print("\t\tMinimum je u tocki: ", minimum)
    print("\t\tVrijednost funkcije cilja u tocki: ", fja)
    print("\t\tBroj evaluacija funkcije cilja: ", evalf)


print("--------------------------------------------------------------------------")
print("Zadatak 5.\n")
n=30; Pm=0.1; eval=10000
print("Funkcija 6:")
print("Dimenzionalnost funkcije je 6")
for turnir in [3, 5, 10, 20]:
    print("\tVelicina turnira:", turnir)
    minimum, fja, evalf = pomicna_tocka(F6, 6, [-50, 150], n, Pm, eval, turnir=turnir)
    print("\t\tMinimum je u tocki: ", minimum)
    print("\t\tVrijednost funkcije cilja u tocki: ", fja)
    print("\t\tBroj evaluacija funkcije cilja: ", evalf)
