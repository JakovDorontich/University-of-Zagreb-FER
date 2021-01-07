from algoritmi import *
from math import sqrt,sin
from random import randint

def g(x):
    return (x-3)**2
def G(X):
    return (X[0]-3)**2
def F1(X):
    return 100*(X[1]-X[0]**2)**2 + (1-X[0])**2
def F2(X):
    return (X[0]-4)**2 + 4*((X[1]-2)**2)
def F3(X, i=5):
    suma=0
    for n in range(1, i+1):
        suma += (X[n-1]-n)**2
    return suma
def F4(X):
    return abs( (X[0]-X[1])*(X[0]+X[1]) ) + sqrt(X[0]**2 + X[1]**2)
def F6(X):
    return 0.5 + ( sin(sqrt(X[0]**2+X[1]**2))**2 - 0.5 ) / (( 1+ 0.001*(X[0]**2+X[1]**2) )**2)



print("Zadatak 1.\n")
print("Pocetne tocke pretrazivanja X0 su: 10, 20, 50, 100\n")
for x0 in [10, 20, 50, 100]:
    print("Postupak zlatnog reza:")
    interval, n = zlatni_rez([-x0, x0], g)
    print("\tMinimum je u intervalu: ", interval)
    print("\tBroj evaluacija: ", n)

    print("Pretraživanje po koordinatnim osima:")
    minimum, n = koordinatno_trazenje([x0], G)
    print("\tMinimum je u tocki: ", minimum)
    print("\tBroj evaluacija: ", n)

    print("Simpleks postupak po Nelderu i Meadu: ")
    minimum, n = nelder_meadu([x0], G)
    print("\tMinimum je u tocki: ", minimum)
    print("\tBroj evaluacija: ", n)

    print ("Hooke-Jeeves postupak: ")
    minimum, n = hook_jeeves([x0], G)
    print("\tMinimum je u tocki: ", minimum)
    print("\tBroj evaluacija: ", n)
    print()


print("--------------------------------------------------------------------------")
print("Zadatak 2.\n")
print("Funkcija 1: \tX0=[-1.9, 2]")
x0 = [-1.9, 2]
print("Pretraživanje po koordinatnim osima:")
minimum, n = koordinatno_trazenje(x0, F1)
print("\tMinimum je u tocki: ", minimum)
print("\tBroj evaluacija: ", n)

print("Simpleks postupak po Nelderu i Meadu: ")
minimum, n = nelder_meadu(x0, F1)
print("\tMinimum je u tocki: ", minimum)
print("\tBroj evaluacija: ", n)

print("Hooke-Jeeves postupak: ")
minimum, n = hook_jeeves(x0, F1)
print("\tMinimum je u tocki: ", minimum)
print("\tBroj evaluacija: ", n)
print()
# -------------------------------------
print("Funkcija 2: \tX0=[0.1, 0.3]")
x0 = [0.1, 0.3]
print("Pretraživanje po koordinatnim osima:")
minimum, n = koordinatno_trazenje(x0, F2)
print("\tMinimum je u tocki: ", minimum)
print("\tBroj evaluacija: ", n)

print("Simpleks postupak po Nelderu i Meadu: ")
minimum, n = nelder_meadu(x0, F2)
print("\tMinimum je u tocki: ", minimum)
print("\tBroj evaluacija: ", n)

print("Hooke-Jeeves postupak: ")
minimum, n = hook_jeeves(x0, F2)
print("\tMinimum je u tocki: ", minimum)
print("\tBroj evaluacija: ", n)
print()
# -------------------------------------
print("Funkcija 3: \tX0=[0, 0, 0, 0, 0]")
x0 = [0, 0, 0, 0, 0]
print("Pretraživanje po koordinatnim osima:")
minimum, n = koordinatno_trazenje(x0, F3)
print("\tMinimum je u tocki: ", minimum)
print("\tBroj evaluacija: ", n)

print("Simpleks postupak po Nelderu i Meadu: ")
minimum, n = nelder_meadu(x0, F3)
print("\tMinimum je u tocki: ", minimum)
print("\tBroj evaluacija: ", n)

print("Hooke-Jeeves postupak: ")
minimum, n = hook_jeeves(x0, F3)
print("\tMinimum je u tocki: ", minimum)
print("\tBroj evaluacija: ", n)
print()
# -------------------------------------
print("Funkcija 4: \tX0=[5.1, 1.1]")
x0 = [5.1, 1.1]
print("Pretraživanje po koordinatnim osima:")
minimum, n = koordinatno_trazenje(x0, F4)
print("\tMinimum je u tocki: ", minimum)
print("\tBroj evaluacija: ", n)

print("Simpleks postupak po Nelderu i Meadu: ")
minimum, n = nelder_meadu(x0, F4)
print("\tMinimum je u tocki: ", minimum)
print("\tBroj evaluacija: ", n)

print("Hooke-Jeeves postupak: ")
minimum, n = hook_jeeves(x0, F4)
print("\tMinimum je u tocki: ", minimum)
print("\tBroj evaluacija: ", n)
print()


print("--------------------------------------------------------------------------")
print("Zadatak 3.\n")
print("Funkcija 4: \tX0=[5, 5]")
x0 = [5, 5]
print("Simpleks postupak po Nelderu i Meadu: ")
minimum, n = nelder_meadu(x0, F4)
print("\tMinimum je u tocki: ", minimum)
print("\tBroj evaluacija: ", n)

print("Hooke-Jeeves postupak: ")
minimum, n = hook_jeeves(x0, F4)
print("\tMinimum je u tocki: ", minimum)
print("\tBroj evaluacija: ", n)
print()


print("--------------------------------------------------------------------------")
print("Zadatak 4.\n")
print("Funkcija 1: \tX0=[0.5, 0.5]")
x0 = [-2, 2]
print("Simpleks postupak po Nelderu i Meadu: ")
for i in range(1, 21):
    print("Korak pocetnog simpleksa iznosi ", i)
    minimum, n = nelder_meadu(x0, F1, i)
    print("\tMinimum je u tocki: ", minimum)
    print("\tBroj evaluacija: ", n)
print()
print("Funkcija 1: \tX0=[20, 20]")
x0 = [20, 20]
for i in range(1, 21):
    print("Korak pocetnog simpleksa iznosi ", i)
    minimum, n = nelder_meadu(x0, F1, i)
    print("\tMinimum je u tocki: ", minimum)
    print("\tBroj evaluacija: ", n)


print("--------------------------------------------------------------------------")
print("Zadatak 5.\n")
print("Simpleks postupak po Nelderu i Meadu: ")
for i in range(5):
    x1 = randint(-50, 50)
    x2 = randint(-50, 50)
    x0 = [x1, x2]
    print("X0=["+str(x1)+", "+str(x2)+"]")
    minimum, n = nelder_meadu(x0, F6, epsilon=0.0001)
    print("\tMinimum je u tocki: ", minimum)
    print("\tBroj evaluacija: ", n)


