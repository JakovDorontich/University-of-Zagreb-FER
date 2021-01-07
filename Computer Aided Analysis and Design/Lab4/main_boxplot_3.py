from algoritmi import *
from math import sqrt, sin

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



n=10; p=4; Pm=0.1; eval=100000

print("----------------------- Funkcija 6 dimenzije 3 -----------------------")
print('"Binarni_prikaz","Prikaz_s_pomicnom_tockom"')
for test in range(10):
    minimum_b, fja_b, evalf_b = binarno(F6, 3, [-50, 150], n, p, Pm, eval)
    minimum_t, fja_t, evalf_t = pomicna_tocka(F6, 3, [-50, 150], n, Pm, eval)
    print('"'+str(fja_b)+'","'+str(round(fja_t, p))+'"')
print("----------------------- Funkcija 6 dimenzije 6 -----------------------")
print('"Binarni_prikaz","Prikaz_s_pomicnom_tockom"')
for test in range(10):
    minimum_b, fja_b, evalf_b = binarno(F6, 6, [-50, 150], n, p, Pm, eval)
    minimum_t, fja_t, evalf_t = pomicna_tocka(F6, 6, [-50, 150], n, Pm, eval)
    print('"'+str(fja_b)+'","'+str(round(fja_t, p))+'"')

print("----------------------- Funkcija 7 dimenzije 3 -----------------------")
print('"Binarni_prikaz","Prikaz_s_pomicnom_tockom"')
for test in range(10):
    minimum_b, fja_b, evalf_b = binarno(F7, 3, [-50, 150], n, p, Pm, eval)
    minimum_t, fja_t, evalf_t = pomicna_tocka(F7, 3, [-50, 150], n, Pm, eval)
    print('"'+str(fja_b)+'","'+str(round(fja_t, p))+'"')
print("----------------------- Funkcija 7 dimenzije 6 -----------------------")
print('"Binarni_prikaz","Prikaz_s_pomicnom_tockom"')
for test in range(10):
    minimum_b, fja_b, evalf_b = binarno(F7, 6, [-50, 150], n, p, Pm, eval)
    minimum_t, fja_t, evalf_t = pomicna_tocka(F7, 6, [-50, 150], n, Pm, eval)
    print('"'+str(fja_b)+'","'+str(round(fja_t, p))+'"')



