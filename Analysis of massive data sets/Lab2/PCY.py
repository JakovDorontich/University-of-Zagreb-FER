from math import floor
from itertools import combinations

def calcA(brPredmeta, prag):
    m = 0
    for i in brPredmeta:
        if brPredmeta[i] >= prag:
            m += 1
    return (m * (m - 1) / 2)

brKosara = int(input()) #N
s = float(input())
prag = int(floor(s * brKosara))
brPretinaca = int(input()) #b

brPredmeta = {}
brParova = {}
for i in range(brKosara):
    redak = input()
    sadrzajKosare = [int(n) for n in redak.split()]

    for p in sadrzajKosare:
        predmet = int(p)
        if predmet in brPredmeta:
            brPredmeta[predmet] += 1
        else:
            brPredmeta[predmet] = 1

    for (i, j) in combinations(sadrzajKosare, 2):
        if (i, j) in brParova:
            brParova[(i, j)] += 1
        else:
            brParova[(i, j)] = 1


pretinci = [0] * brPretinaca
parovi = {}
brParovaReducirano = {}

for (i, j) in brParova:
    if brPredmeta[i] >= prag and brPredmeta[j] >= prag:
        k = (i * len(brPredmeta) + j) % brPretinaca
        pretinci[k] += brParova[(i, j)]
        brParovaReducirano[(i, j)] = k


for (i, j) in brParovaReducirano:
    k = brParovaReducirano[(i,j)]
    if (pretinci[k] >= prag ):
        parovi[(i,j)] = brParova[(i,j)]


A = calcA(brPredmeta, prag)
print(int(A))
P = len(parovi)
print(P)

X = []
for p in parovi.values():
    X.append(p)
X.sort(reverse = True)
for x in X:
    print(x)
