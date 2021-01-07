from matrica import *

print("Zadatak 1.")
print("\nPocetna matrica A:")
A = matrica(loadMatrix("zadatak_1_A.txt"))
printMatrix(A)

b = 11.6
R = A.mulScalar(b)
R = R.mulScalar(1/b)

print("Konacna matrica R nakon mnozenja i dijeljenja matrice A sa skalarom " + str(b) + ":")
printMatrix(R)

if (A == R): print("Matrice A i R su jednake do na 9-tu decimalu")
else: print("Matrice A i R nisu jednake do na 9-tu decimalu")

print("--------------------------------------------------------------------------")
print("Zadatak 2.")
A = matrica(loadMatrix("zadatak_2_A.txt"))
b = matrica(loadMatrix("zadatak_2_b.txt"))
print("\nRjesenje sustava LU dekompozicijom:")
try:
    L, U = LU_decomposition(A)
    y = forwardSubstitution(L, b)
    x = backSubstitution(U, y)
    print("Rjesenje sustava, tj. matrica x je oblika: ")
    printMatrix(x)
except ZeroDivisionError as e:
    print ("ERROR: " + str(e))

A = matrica(loadMatrix("zadatak_2_A.txt"))
b = matrica(loadMatrix("zadatak_2_b.txt"))
print("\nRjesenje sustava LUP dekompozicijom:")
try:
    L, U, P = LUP_decomposition(A)
    b = P * b
    print("Matrica LU je oblika: ")
    printMatrix(A)
    print("Matrica P je oblika: ")
    printMatrix(P)
    y = forwardSubstitution(L, b)
    x = backSubstitution(U, y)
    print("Rjesenje sustava, tj. matrica x je oblika: ")
    printMatrix(x)
except ZeroDivisionError as e:
    print ("ERROR: " + str(e))

print("--------------------------------------------------------------------------")
print("Zadatak 3.")
A = matrica(loadMatrix("zadatak_3_A.txt"))
b = matrica(loadMatrix("zadatak_2_b.txt"))
print("\nRjesenje sustava LU dekompozicijom:")
try:
    L, U = LU_decomposition(A)
    print("Matrica L je oblika: ")
    printMatrix(L)
    print("Matrica U je oblika: ")
    printMatrix(U)
    y = forwardSubstitution(L, b)
    x = backSubstitution(U, y)
    print("Rjesenje sustava, tj. matrica x je oblika: ")
    printMatrix(x)
except ZeroDivisionError as e:
    print ("ERROR: " + str(e))

A = matrica(loadMatrix("zadatak_3_A.txt"))
b = matrica(loadMatrix("zadatak_2_b.txt"))
print("\nRjesenje sustava LUP dekompozicijom:")
try:
    L, U, P = LUP_decomposition(A)
    b = P * b
    print("Matrica L je oblika: ")
    printMatrix(L)
    print("Matrica U je oblika: ")
    printMatrix(U)
    y = forwardSubstitution(L, b)
    x = backSubstitution(U, y)
    print("Rjesenje sustava, tj. matrica x je oblika: ")
    printMatrix(x)
except ZeroDivisionError as e:
    print ("ERROR: " + str(e))

print("--------------------------------------------------------------------------")
print("Zadatak 4.")
A = matrica(loadMatrix("zadatak_4_A.txt"))
b = matrica(loadMatrix("zadatak_4_b.txt"))
print("\nRjesenje sustava LU dekompozicijom:")
try:
    L, U = LU_decomposition(A)
    print("Matrica L je oblika: ")
    printMatrix(L)
    print("Matrica U je oblika: ")
    printMatrix(U)
    y = forwardSubstitution(L, b)
    x = backSubstitution(U, y)
    print("Rjesenje sustava, tj. matrica x je oblika: ")
    printMatrix(x)
except ZeroDivisionError as e:
    print ("ERROR: " + str(e))

A = matrica(loadMatrix("zadatak_4_A.txt"))
b = matrica(loadMatrix("zadatak_4_b.txt"))
print("\nRjesenje sustava LUP dekompozicijom:")
try:
    L, U, P = LUP_decomposition(A)
    b = P * b
    print("Matrica L je oblika: ")
    printMatrix(L)
    print("Matrica U je oblika: ")
    printMatrix(U)
    y = forwardSubstitution(L, b)
    x = backSubstitution(U, y)
    print("Rjesenje sustava, tj. matrica x je oblika: ")
    printMatrix(x)
except ZeroDivisionError as e:
    print ("ERROR: " + str(e))

print("--------------------------------------------------------------------------")
print("Zadatak 5.")
A = matrica(loadMatrix("zadatak_5_A.txt"))
b = matrica(loadMatrix("zadatak_5_b.txt"))

print("\nRjesenje sustava LUP dekompozicijom:")
try:
    L, U, P = LUP_decomposition(A)
    b = P * b
    print("Matrica L je oblika: ")
    printMatrix(L)
    print("Matrica U je oblika: ")
    printMatrix(U)
    y = forwardSubstitution(L, b)
    x = backSubstitution(U, y)
    print("Rjesenje sustava, tj. matrica x je oblika: ")
    printMatrix(x)
except ZeroDivisionError as e:
    print ("ERROR: " + str(e))

print("--------------------------------------------------------------------------")
print("Zadatak 6.")
A = matrica(loadMatrix("zadatak_6_A.txt"))
b = matrica(loadMatrix("zadatak_6_b.txt"))

mulMatrixRow(A, 0, 0.000000001)
mulMatrixRow(b, 0, 0.000000001)
mulMatrixRow(A, 2, 10000000000)
mulMatrixRow(b, 2, 10000000000)

print("\nRjesenje sustava LUP dekompozicijom:")
try:
    L, U, P = LUP_decomposition(A, 0.000001)
    b = P * b
    print("Matrica L je oblika: ")
    printMatrix(L)
    print("Matrica U je oblika: ")
    printMatrix(U)
    y = forwardSubstitution(L, b)
    x = backSubstitution(U, y)
    print("Rjesenje sustava, tj. matrica x je oblika: ")
    printMatrix(x)
except ZeroDivisionError as e:
    print ("ERROR: " + str(e))

print("--------------------------------------------------------------------------")
print("Zadatak 7.")

A = matrica(loadMatrix("zadatak_7_A.txt"))
print("\nInverz matrice koristenjem LUP dekompozicijom:")
try:
    A = inverse(A)
    printMatrix(A)
except ZeroDivisionError as e:
    print("ERROR: " + str(e))

print("--------------------------------------------------------------------------")
print("Zadatak 8.")

A = matrica(loadMatrix("zadatak_8_A.txt"))
print("\nInverz matrice koristenjem LUP dekompozicijom:")
try:
    A = inverse(A)
    roundMatrix(A, 6)
    printMatrix(A)
except ZeroDivisionError as e:
    print("ERROR: " + str(e))

print("--------------------------------------------------------------------------")
print("Zadatak 9.")

A = matrica(loadMatrix("zadatak_8_A.txt"))
print("\nDeterminanta matrice koristenjem LUP dekompozicijom:")
try:
    det = determinant(A)
    print (round(det, 6), "\n")
except ZeroDivisionError as e:
    print("ERROR: " + str(e))

print("--------------------------------------------------------------------------")
print("Zadatak 10.")

A = matrica(loadMatrix("zadatak_2_A.txt"))
print("\nDeterminanta matrice koristenjem LUP dekompozicijom:")
try:
    det = determinant(A)
    print (round(det, 6))
except ZeroDivisionError as e:
    print("ERROR: " + str(e))

