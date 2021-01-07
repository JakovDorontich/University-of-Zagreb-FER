#!/usr/bin/perl

def dimenzija_i_rjecnik(matrix):
    lines = matrix.split("\n")
    
    dimenzija = lines[0].split(" ")
    dimenzija = [int(i) for i in dimenzija]

    matrica = {}
    for line in lines[1:]:
        pom = line.split(" ")
        matrica[(int(pom[0]), int(pom[1]))] = float(pom[2])
        
    return dimenzija, matrica
    

def pomnozi_matrice(d1, m1, d2, m2):
    if (d1[1] != d2[0]):
        print ("Matrice nemaju uskladene dimenzije!")
        exit()
    dimenzija = [d1[0], d2[1]]
    matrica = {}
    for redak in range(1, d2[0]+1):
        for stupac in range(1, d2[1]+1):
            matrica[(redak, stupac)] = 0
            for stupacP in range (1, d1[1]+1):
                matrica[(redak, stupac)] += float(m1.get((redak, stupacP), 0) * m2.get((stupacP, stupac), 0))
                
    return dimenzija, matrica


file = open("matrice.txt", "r").read()
m1, m2 = file.split("\n\n")

d1, m1 = dimenzija_i_rjecnik(m1)
d2, m2 = dimenzija_i_rjecnik(m2)

d3, m3 = pomnozi_matrice(d1, m1, d2, m2)

for redak in range(1, d3[0]+1):
    for stupac in range (1, d3[1]+1):
        print ("%6.2f " % (m3[(redak, stupac)]), end="")
    print()

newFile = open("umnozak.txt", "w")
newFile.write(str(d3[0])+" "+str(d3[1])+"\n")
for pozicija, vrijednost in m3.items():
    if (vrijednost != 0):
        redak, stupac = pozicija
        newFile.write(str(redak)+" "+str(stupac)+" "+str(vrijednost)+"\n")

newFile.close()

