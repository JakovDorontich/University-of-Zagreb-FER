import numpy

unos = input("Unesi podatke: \n").split(" ")

# pretvori unos iz string liste u float listu
unos = [float(i) for i in unos]

A = numpy.mat([unos[0:3], unos[4:7], unos[8:11]])
B = numpy.mat([[unos[3]], [unos[7]], [unos[11]]])

C = numpy.linalg.solve(A,B)

print("[X Y Z]=[%f %f %f]" %(C[0][0], C[1][0], C[2][0]))
