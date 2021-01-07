from math import sqrt
from copy import deepcopy
from decimal import Decimal, ROUND_HALF_UP
from time import sleep

def collaborative_filtering(matrica, I, J, T, K):
    N = len(matrica)
    M = len(matrica[0])
    if (T == 1):
        matrica = transpose(matrica)
        pom = I
        I = J
        J = pom

    matrix = deepcopy(matrica)

    for i in range(N):
        ocjene = []
        for ocj in matrix[i]:
            if (ocj == 0):
                continue
            else:
                ocjene.append(ocj)
        aritm = round(sum(ocjene)/len(ocjene), 6)
        for j in range(M):
            if (matrix[i][j] == 0):
                continue
            else:
                matrix[i][j] = round((matrix[i][j] - aritm), 6)

    # ima oblik (realan_index, prava_ocjena): slicnost
    similarities = {}
    for i in range(N):
        if (i == I-1):
            continue
        else:
            pearson = pearson_similarity(matrix[i], matrix[I-1])
            if (pearson > 0 and matrica[i][J-1] > 0):
                similarities[(i+1, matrica[i][J-1])] = pearson

    similarities = sorted(similarities.items(), key=lambda x: x[1], reverse=True)[:K]

    brojnik = 0; nazivnik = 0
    for par, simil in similarities:
        brojnik += par[1]*simil
        nazivnik += simil
    rjesenje = brojnik/nazivnik

    return Decimal(Decimal(rjesenje).quantize(Decimal('.001'), rounding=ROUND_HALF_UP))

def transpose(matrix):
    result = []
    for i in range(len(matrix)):
        pomTran = []
        for j in range(len(matrix[0])):
            pomTran.append(matrix[j][i])
        result.append(pomTran)
    return result

def pearson_similarity(array1, array2):
    brojnik = sum(multiply(array1, array2))
    nazivnik = sqrt(sum(multiply(array1, array1))*sum(multiply(array2, array2)))
    return round((brojnik/nazivnik), 6)

def multiply (array1, array2):
    result = []
    for i in range(len(array1)):
        result.append(array1[i] * array2[i])
    return result



dimMatrice = input().split(' ')
N = int(dimMatrice[0]) #brRedaka (Items)
M = int(dimMatrice[1]) #brStupaca (Users)

matrica = []
for i in range(N):
    redak = []
    podatci = input().split(' ')
    for ocjena in podatci:
        if (ocjena == 'X'):
            redak.append(int(0))
        else:
            redak.append(int(ocjena))
    matrica.append(redak)

Q = int(input()) #brUpita
for i in range(Q):
    upit = input().split(' ')
    I = int(upit[0])
    J = int(upit[1])
    T = int(upit[2])
    K = int(upit[3])
    print (collaborative_filtering(matrica, I, J, T, K))




