from algoritmi import *
from math import sqrt, sin

def F6(X):
    i = len(X)
    suma = 0
    for n in range(i):
        suma += (X[n])**2
    return 0.5 + (sin(sqrt(suma))**2-0.5) / (1+0.001*suma)**2



Pm=0.1; eval=10000
N =[10, 30, 50, 100, 200, 500, 1000]
print("----------------------- Funkcija 6 dimenzije 2 -----------------------")
print('"n=10","n=30","n=50","n=100","n=200","n=500","n=1000"')
for test in range(10):
    ispis = ''
    for n in N:
        minimum, fja, evalf = pomicna_tocka(F6, 2, [-50, 150], n, Pm, eval)
        if (n == N[-1]):
            podatak = '"' + str(fja) + '"'
        else:
            podatak = '"' + str(fja) + '",'
        ispis += podatak
    print(ispis)

