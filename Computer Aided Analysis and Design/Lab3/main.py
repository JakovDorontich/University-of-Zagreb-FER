from algoritmi import *
from math import log


def F1(X):
    return 100*(X[1]-X[0]**2)**2 + (1-X[0])**2
def dF1(X):
    return [400*(X[0]**3-X[0]*X[1])+2*(X[0]-1), 200*(X[1]-X[0]**2)]
def d_F1(X):
    return [ [400*(X[0]**3-X[0]*X[1])+2*(X[0]-1)], [200*(X[1]-X[0]**2)] ]
def hF1(X):
    return [ [400*(3*X[0]**2-X[1])+2, -400*X[0]], [-400*X[0], 200] ]

def F2(X):
    return (X[0]-4)**2 + 4*((X[1]-2)**2)
def dF2(X):
    return [2*(X[0]-4), 8*(X[1]-2)]
def d_F2(X):
    return [ [2*(X[0]-4)], [8*(X[1]-2)] ]
def hF2(X):
    return [ [2, 0], [0, 8] ]

def F3(X):
    return (X[0]-2)**2 + (X[1]+3)**2
def dF3(X):
    return [2*(X[0]-2), 2*(X[1]+3)]

def U1(X, t):
    if ((X[1]-X[0]) < 0) or ((2-X[0]) < 0):
        return 1000000
    return 100*(X[1]-X[0]**2)**2 + (1-X[0])**2 - (1/t)*log(X[1]-X[0]) - (1/t)*log(2-X[0])

def U2(X, t):
    try:
        return (X[0]-4)**2 + 4*((X[1]-2)**2) - (1/t)*log(X[1]-X[0]) - (1/t)*log(2-X[0])
    except ValueError:
        return 1000000

def U4(X, t):
    try:
        return ((X[0]-3)**2 + X[1]**2) - (1/t)*log(3-X[0]-X[1]) - (1/t)*log(3+1.5*X[0]-X[1]) + t*(X[1]-1)**2
    except ValueError:
        return 1000000



print("Zadatak 1.\n")
print("Funkcija 3: \tX0=[0, 0]")
x0 = [0, 0]
print("Postupak gradijentnog spusta:")
minimum, fja, br_fja, br_grad = gradijentini_spust(x0, F3, dF3, gold=False)
print("\tMinimum je u tocki: ", minimum)
print("\tVrijednost funkcije cilja u tocki: ", fja)
print("\tBroj evaluacija funkcije cilja: ", br_fja)
print("\tBroj evaluacija gradijenta: ", br_grad)

print("Postupak gradijentnog spusta (zlatni rez):")
minimum, fja, br_fja, br_grad = gradijentini_spust(x0, F3, dF3, gold=True)
print("\tMinimum je u tocki: ", minimum)
print("\tVrijednost funkcije cilja u tocki: ", fja)
print("\tBroj evaluacija funkcije cilja: ", br_fja)
print("\tBroj evaluacija gradijenta: ", br_grad)


print("--------------------------------------------------------------------------")
print("Zadatak 2.\n")
print("Funkcija 1: \tX0=[-1.9, 2]")
x0 = [-1.9, 2]
print("Postupak gradijentnog spusta (zlatni rez):")
minimum, fja, br_fja, br_grad = gradijentini_spust(x0, F1, dF1, gold=True, epsilon=0.0001)
print("\tMinimum je u tocki: ", minimum)
print("\tVrijednost funkcije cilja u tocki: ", fja)
print("\tBroj evaluacija funkcije cilja: ", br_fja)
print("\tBroj evaluacija gradijenta: ", br_grad)

print("Newton-Raphsonov postupak (zlatni rez):")
minimum, fja, br_fja, br_grad, br_hess = newton_raphson(x0, F1, d_F1, hF1, gold=True)
print("\tMinimum je u tocki: ", minimum)
print("\tVrijednost funkcije cilja u tocki: ", fja)
print("\tBroj evaluacija funkcije cilja: ", br_fja)
print("\tBroj evaluacija gradijenta: ", br_grad)
print("\tBroj evaluacija Hesseove matrice: ", br_hess)

print("\nFunkcija 2: \tX0=[0.1, 0.3]")
x0 = [0.1, 0.3]
print("Postupak gradijentnog spusta (zlatni rez):")
minimum, fja, br_fja, br_grad = gradijentini_spust(x0, F2, dF2, gold=True, epsilon=0.0001)
print("\tMinimum je u tocki: ", minimum)
print("\tVrijednost funkcije cilja u tocki: ", fja)
print("\tBroj evaluacija funkcije cilja: ", br_fja)
print("\tBroj evaluacija gradijenta: ", br_grad)

print("Newton-Raphsonov postupak (zlatni rez):")
minimum, fja, br_fja, br_grad, br_hess = newton_raphson(x0, F2, d_F2, hF2, gold=True)
print("\tMinimum je u tocki: ", minimum)
print("\tVrijednost funkcije cilja u tocki: ", fja)
print("\tBroj evaluacija funkcije cilja: ", br_fja)
print("\tBroj evaluacija gradijenta: ", br_grad)
print("\tBroj evaluacija Hesseove matrice: ", br_hess)


print("--------------------------------------------------------------------------")
print("Zadatak 3.\n")
print("Funkcija 1: \tX0=[-1.9, 2]")
x0 = [-1.9, 2]
print("Postupak po Boxu:")
minimum, fja = box(x0, F1)
print("\tMinimum je u tocki: ", minimum)
print("\tVrijednost funkcije cilja u tocki: ", fja)

print("\nFunkcija 2: \tX0=[0.1, 0.3]")
x0 = [0.1, 0.3]
print("Postupak po Boxu:")
minimum, fja = box(x0, F2)
print("\tMinimum je u tocki: ", minimum)
print("\tVrijednost funkcije cilja u tocki: ", fja)


print("--------------------------------------------------------------------------")
print("Zadatak 4.\n")
print("Funkcija 1: \tX0=[-1.9, 2]")
x0 = [-1.9, 2]
print("Postupak transformacije u problem bez ograničenja:")
minimum, fja, n = mjesoviti_nacin(x0, U1)
print("\tMinimum je u tocki: ", minimum)
print("\tVrijednost funkcije cilja u tocki: ", fja)
print("\tBroj evaluacija: ", n)

print("\nFunkcija 2: \tX0=[0.1, 0.3]")
x0 = [0.1, 0.3]
print("Postupak transformacije u problem bez ograničenja:")
minimum, fja, n = mjesoviti_nacin(x0, U2)
print("\tMinimum je u tocki: ", minimum)
print("\tVrijednost funkcije cilja u tocki: ", fja)
print("\tBroj evaluacija: ", n)


print("--------------------------------------------------------------------------")
print("Zadatak 5.\n")
print("Funkcija 4: \tX0=[0, 0]")
x0 = [0, 0]
print("Postupak transformacije u problem bez ograničenja:")
minimum, fja, n = mjesoviti_nacin(x0, U4)
print("\tMinimum je u tocki: ", minimum)
print("\tVrijednost funkcije cilja u tocki: ", fja)
print("\tBroj evaluacija: ", n)

print("\nFunkcija 4: \tX0=[5, 5]")
x0 = [5, 5]
print("Postupak transformacije u problem bez ograničenja:")
minimum, fja, n = mjesoviti_nacin(x0, U4, unutar=False)
print("\tMinimum je u tocki: ", minimum)
print("\tVrijednost funkcije cilja u tocki: ", fja)
print("\tBroj evaluacija: ", n)

