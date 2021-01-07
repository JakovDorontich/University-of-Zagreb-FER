import numpy

A = input("Unesi vrh trokuta A(x,y,z):\n").split(" ")
A = [float(i) for i in A]
B = input("Unesi vrh trokuta B(x,y,z):\n").split(" ")
B = [float(i) for i in B]
C = input("Unesi vrh trokuta C(x,y,z):\n").split(" ")
C = [float(i) for i in C]
T = input("Unesi točku T(x,y,z):\n").split(" ")
T = [float(i) for i in T]

T = numpy.mat([[T[0]], [T[1]], [T[2]]])
matrix = numpy.mat([[A[0], B[0], C[0]], [A[1], B[1], C[1]], [A[2], B[2], C[2]] ])

t = (matrix.I * T)

print("\nBaricentrične kooridnate točke T: (%f %f %f)" %(t[0][0], t[1][0], t[2][0]))
