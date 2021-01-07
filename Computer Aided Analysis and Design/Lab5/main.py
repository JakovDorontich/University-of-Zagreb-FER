from algoritmi import *
from math import cos, sin
import matplotlib.pyplot as plt


def arange(start, stop, step):
    return [round(x*step, 5) for x in range(start, int(stop/step))]

def kumulativna_greska(x0, x1, x2, T, tmax):
    greska_x1 = 0
    greska_x2 = 0
    pravi_x1 = []; pravi_x2 = []
    for t in arange(0, tmax+T, T):
        pravi_x1.append(x0.matrix[0][0]*cos(t) + x0.matrix[1][0]*sin(t))
        pravi_x2.append(x0.matrix[1][0]*cos(t) - x0.matrix[0][0]*sin(t))
    for i in range(len(x1)):
        greska_x1 += abs(x1[i] - pravi_x1[i])
        greska_x2 += abs(x2[i] - pravi_x2[i])
    return greska_x1, greska_x2


print("Zadatak 1.")
T = 0.01
tmax = 10
fig1 = plt.figure(figsize=(12,5))
fig1.canvas.set_window_title("Zadatak 1")
ax1 = fig1.add_subplot(121)
ax2 = fig1.add_subplot(122)
ax1.set_title("X1"); ax2.set_title("X2")
apscisa = arange(0, tmax+T, T)
A = matrica(loadMatrix("zadatak_1_A.txt"))
B = matrica(loadMatrix("zadatak_1_B.txt"))
x0 = matrica(loadMatrix("zadatak_1_x0.txt"))
rt = matrica(loadMatrix("zadatak_1_rt.txt"))

Xk, x1, x2 = euler(A, B, x0, rt, T, tmax)
greska_x1, greska_x2 = kumulativna_greska(x0, x1, x2, T, tmax)
print("\tEulerov postupak:")
print("Rjesenje sustava (matrica):")
printMatrix(Xk)
print("Kumulativna pogreska:")
print(greska_x1)
print(greska_x2)
ax1.plot(apscisa, x1, label="Euler")
ax2.plot(apscisa, x2, label="Euler")

Xk, x1, x2 = obrnutiEuler(A, B, x0, rt, T, tmax)
greska_x1, greska_x2 = kumulativna_greska(x0, x1, x2, T, tmax)
print("\n\tObrnuti Eulerov postupak:")
print("Rjesenje sustava (matrica):")
printMatrix(Xk)
print("Kumulativna pogreska:")
print(greska_x1)
print(greska_x2)
ax1.plot(apscisa, x1, label="Obrnuti Euler")
ax2.plot(apscisa, x2, label="Obrnuti Euler")

Xk, x1, x2 = trapezni(A, B, x0, rt, T, tmax)
greska_x1, greska_x2 = kumulativna_greska(x0, x1, x2, T, tmax)
print("\n\tTrapezni postupak:")
print("Rjesenje sustava (matrica):")
printMatrix(Xk)
print("Kumulativna pogreska:")
print(greska_x1)
print(greska_x2)
ax1.plot(apscisa, x1, label="Trapezni")
ax2.plot(apscisa, x2, label="Trapezni")

Xk, x1, x2 = rungeKutta(A, B, x0, rt, T, tmax)
greska_x1, greska_x2 = kumulativna_greska(x0, x1, x2, T, tmax)
print("\n\tRunge-Kutta postupak 4.reda:")
print("Rjesenje sustava (matrica):")
printMatrix(Xk)
print("Kumulativna pogreska:")
print(greska_x1)
print(greska_x2)
ax1.plot(apscisa, x1, label="Runge-Kutta")
ax2.plot(apscisa, x2, label="Runge-Kutta")

Xk, x1, x2 = pred_kor_1(A, B, x0, rt, T, tmax)
greska_x1, greska_x2 = kumulativna_greska(x0, x1, x2, T, tmax)
print("\n\tPrediktor Euler, korektor obrnuti Euler, 2:")
print("Rjesenje sustava (matrica):")
printMatrix(Xk)
print("Kumulativna pogreska:")
print(greska_x1)
print(greska_x2)
ax1.plot(apscisa, x1, label="Euler, Obrnuti E, 2")
ax2.plot(apscisa, x2, label="Euler, Obrnuti E, 2")

Xk, x1, x2 = pred_kor_2(A, B, x0, rt, T, tmax)
greska_x1, greska_x2 = kumulativna_greska(x0, x1, x2, T, tmax)
print("\n\tPrediktor Euler, korektor Trapez, 1:")
print("Rjesenje sustava (matrica):")
printMatrix(Xk)
print("Kumulativna pogreska:")
print(greska_x1)
print(greska_x2)
ax1.plot(apscisa, x1, label="Euler, Trapez, 1")
ax2.plot(apscisa, x2, label="Euler, Trapez, 1")

ax1.legend(); ax2.legend()
plt.show()

print("--------------------------------------------------------------------------")
print("Zadatak 2.\n")
T = 0.1
tmax = 1
fig2 = plt.figure(figsize=(12,5))
fig2.canvas.set_window_title("Zadatak 2")
ax1 = fig2.add_subplot(121)
ax2 = fig2.add_subplot(122)
ax1.set_title("X1"); ax2.set_title("X2")
apscisa = arange(0, tmax+T, T)
A = matrica(loadMatrix("zadatak_2_A.txt"))
B = matrica(loadMatrix("zadatak_2_B.txt"))
x0 = matrica(loadMatrix("zadatak_2_x0.txt"))
rt = matrica(loadMatrix("zadatak_2_rt.txt"))

Xk, x1, x2 = euler(A, B, x0, rt, T, tmax)
print("\tEulerov postupak:")
print("Rjesenje sustava (matrica):")
printMatrix(Xk)
ax1.plot(apscisa, x1, label="Euler")
ax2.plot(apscisa, x2, label="Euler")

Xk, x1, x2 = obrnutiEuler(A, B, x0, rt, T, tmax)
print("\n\tObrnuti Eulerov postupak:")
print("Rjesenje sustava (matrica):")
printMatrix(Xk)
ax1.plot(apscisa, x1, label="Obrnuti Euler")
ax2.plot(apscisa, x2, label="Obrnuti Euler")

Xk, x1, x2 = trapezni(A, B, x0, rt, T, tmax)
print("\n\tTrapezni postupak:")
print("Rjesenje sustava (matrica):")
printMatrix(Xk)
ax1.plot(apscisa, x1, label="Trapezni")
ax2.plot(apscisa, x2, label="Trapezni")

# T <= 0.02785
T = 0.02
apscisa = arange(0, tmax+T, T)
Xk, x1, x2 = rungeKutta(A, B, x0, rt, T, tmax)
print("\n\tRunge-Kutta postupak 4.reda:")
print("Rjesenje sustava (matrica):")
printMatrix(Xk)
ax1.plot(apscisa, x1, label="Runge-Kutta")
ax2.plot(apscisa, x2, label="Runge-Kutta")

T = 0.01
apscisa = arange(0, tmax+T, T)
Xk, x1, x2 = pred_kor_1(A, B, x0, rt, T, tmax)
print("\n\tPrediktor Euler, korektor obrnuti Euler, 2:")
print("Rjesenje sustava (matrica):")
printMatrix(Xk)
ax1.plot(apscisa, x1, label="Euler, Obrnuti E, 2")
ax2.plot(apscisa, x2, label="Euler, Obrnuti E, 2")

Xk, x1, x2 = pred_kor_2(A, B, x0, rt, T, tmax)
print("\n\tPrediktor Euler, korektor Trapez, 1:")
print("Rjesenje sustava (matrica):")
printMatrix(Xk)
ax1.plot(apscisa, x1, label="Euler, Trapez, 1")
ax2.plot(apscisa, x2, label="Euler, Trapez, 1")

ax1.legend(); ax2.legend()
plt.show()

print("--------------------------------------------------------------------------")
print("Zadatak 3.\n")
T = 0.01
tmax = 10
fig3 = plt.figure(figsize=(12,5))
fig3.canvas.set_window_title("Zadatak 3")
ax1 = fig3.add_subplot(121)
ax2 = fig3.add_subplot(122)
ax1.set_title("X1"); ax2.set_title("X2")
apscisa = arange(0, tmax+T, T)
A = matrica(loadMatrix("zadatak_3_A.txt"))
B = matrica(loadMatrix("zadatak_3_B.txt"))
x0 = matrica(loadMatrix("zadatak_3_x0.txt"))
rt = matrica(loadMatrix("zadatak_3_rt.txt"))

Xk, x1, x2 = euler(A, B, x0, rt, T, tmax)
print("\tEulerov postupak:")
print("Rjesenje sustava (matrica):")
printMatrix(Xk)
ax1.plot(apscisa, x1, label="Euler")
ax2.plot(apscisa, x2, label="Euler")

Xk, x1, x2 = obrnutiEuler(A, B, x0, rt, T, tmax)
print("\n\tObrnuti Eulerov postupak:")
print("Rjesenje sustava (matrica):")
printMatrix(Xk)
ax1.plot(apscisa, x1, label="Obrnuti Euler")
ax2.plot(apscisa, x2, label="Obrnuti Euler")

Xk, x1, x2 = trapezni(A, B, x0, rt, T, tmax)
print("\n\tTrapezni postupak:")
print("Rjesenje sustava (matrica):")
printMatrix(Xk)
ax1.plot(apscisa, x1, label="Trapezni")
ax2.plot(apscisa, x2, label="Trapezni")

Xk, x1, x2 = rungeKutta(A, B, x0, rt, T, tmax)
print("\n\tRunge-Kutta postupak 4.reda:")
print("Rjesenje sustava (matrica):")
printMatrix(Xk)
ax1.plot(apscisa, x1, label="Runge-Kutta")
ax2.plot(apscisa, x2, label="Runge-Kutta")

Xk, x1, x2 = pred_kor_1(A, B, x0, rt, T, tmax)
print("\n\tPrediktor Euler, korektor obrnuti Euler, 2:")
print("Rjesenje sustava (matrica):")
printMatrix(Xk)
ax1.plot(apscisa, x1, label="Euler, Obrnuti E, 2")
ax2.plot(apscisa, x2, label="Euler, Obrnuti E, 2")

Xk, x1, x2 = pred_kor_2(A, B, x0, rt, T, tmax)
print("\n\tPrediktor Euler, korektor Trapez, 1:")
print("Rjesenje sustava (matrica):")
printMatrix(Xk)
ax1.plot(apscisa, x1, label="Euler, Trapez, 1")
ax2.plot(apscisa, x2, label="Euler, Trapez, 1")

ax1.legend(); ax2.legend()
plt.show()

print("--------------------------------------------------------------------------")
print("Zadatak 4.\n")
T = 0.01
tmax = 1
fig4 = plt.figure(figsize=(12,5))
fig4.canvas.set_window_title("Zadatak 4")
ax1 = fig4.add_subplot(121)
ax2 = fig4.add_subplot(122)
ax1.set_title("X1"); ax2.set_title("X2")
apscisa = arange(0, tmax+T, T)
A = matrica(loadMatrix("zadatak_4_A.txt"))
B = matrica(loadMatrix("zadatak_4_B.txt"))
x0 = matrica(loadMatrix("zadatak_4_x0.txt"))
rt = matrica(loadMatrix("zadatak_4_rt.txt"))

Xk, x1, x2 = euler(A, B, x0, rt, T, tmax, 1, True)
print("\tEulerov postupak:")
print("Rjesenje sustava (matrica):")
printMatrix(Xk)
ax1.plot(apscisa, x1, label="Euler")
ax2.plot(apscisa, x2, label="Euler")

Xk, x1, x2 = obrnutiEuler(A, B, x0, rt, T, tmax, 1, True)
print("\n\tObrnuti Eulerov postupak:")
print("Rjesenje sustava (matrica):")
printMatrix(Xk)
ax1.plot(apscisa, x1, label="Obrnuti Euler")
ax2.plot(apscisa, x2, label="Obrnuti Euler")

Xk, x1, x2 = trapezni(A, B, x0, rt, T, tmax, 1, True)
print("\n\tTrapezni postupak:")
print("Rjesenje sustava (matrica):")
printMatrix(Xk)
ax1.plot(apscisa, x1, label="Trapezni")
ax2.plot(apscisa, x2, label="Trapezni")

Xk, x1, x2 = rungeKutta(A, B, x0, rt, T, tmax, 1, True)
print("\n\tRunge-Kutta postupak 4.reda:")
print("Rjesenje sustava (matrica):")
printMatrix(Xk)
ax1.plot(apscisa, x1, label="Runge-Kutta")
ax2.plot(apscisa, x2, label="Runge-Kutta")

Xk, x1, x2 = pred_kor_1(A, B, x0, rt, T, tmax, 1, 2, True)
print("\n\tPrediktor Euler, korektor obrnuti Euler, 2:")
print("Rjesenje sustava (matrica):")
printMatrix(Xk)
ax1.plot(apscisa, x1, label="Euler, Obrnuti E, 2")
ax2.plot(apscisa, x2, label="Euler, Obrnuti E, 2")

Xk, x1, x2 = pred_kor_2(A, B, x0, rt, T, tmax, 1, True)
print("\n\tPrediktor Euler, korektor Trapez, 1:")
print("Rjesenje sustava (matrica):")
printMatrix(Xk)
ax1.plot(apscisa, x1, label="Euler, Trapez, 1")
ax2.plot(apscisa, x2, label="Euler, Trapez, 1")

ax1.legend(); ax2.legend()
plt.show()
