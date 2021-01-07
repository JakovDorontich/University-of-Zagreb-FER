import numpy


def print_vectors(v1, v2, znak):
    print("(%di %dj %dk)%s(%di %dj %dk) = " %(v1[0], v1[1], v1[2], znak, v2[0], v2[1], v2[2]), end="")


vector1 = numpy.array([2, 3, -4])
vector2 = numpy.array([-1, 4, -1])
vector3 = numpy.array([2, 2, 4])

v1 = vector1 + vector2
print_vectors(vector1, vector2, "+")
print("%di %dj %dk" %(v1[0], v1[1], v1[2]))

s = numpy.sum(v1 * vector2)
print_vectors(v1, vector2, "*")
print(s)

v2 = numpy.cross(v1, vector3)
print_vectors(v1, vector3, "x")
print("%di %dj %dk" %(v2[0], v2[1], v2[2]))

v3 = numpy.linalg.norm(v2)
print("|%di %dj %dk| = %f" %(v2[0], v2[1], v2[2], v3))

v4 = -v2
print("-(%di %dj %dk) = %di %dj %dk\n" %(v2[0], v2[1], v2[2], v4[0], v4[1], v4[2]))

# ---------------------------------------- Matrice ----------------------------------------

matrix1 = numpy.mat("[1 2 3 ; 2 1 3 ; 4 5 1]")
matrix2 = numpy.mat("[-1 2 -3 ; 5 -2 7 ; -4 -1 3]")

M1 = matrix1 + matrix2
print(M1, "\n")

M2 = matrix1 * matrix2.T
print(M2, "\n")

M3 = matrix1 * matrix2.I
print(M3, "\n")

V = numpy.mat('[1 2 3 1]') * numpy.mat('[1 0 0 0 ; 0 2 0 0 ; 0 0 1 0 ; 2 3 3 1]')
print(V)

