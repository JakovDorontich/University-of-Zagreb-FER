from hashlib import md5

def simhash(tekst):
    sh = [0] * 128
    jedinke = tekst.rstrip().split(" ")
    for jedinka in jedinke:
        hash = md5(jedinka.encode("utf-8"))
        hex_hash = hash.hexdigest()
        int_hash = int(hex_hash, 16)

        for i, bit in enumerate(format(int_hash, "0128b")):
            if (bit == "1"):
                sh[i] += 1
            else:
                sh[i] -= 1
    '''
    for i in range(len(sh)):
        if (sh[i] >= 0):
            sh[i] = 1
        else:
            sh[i] = 0
    int_sh = int(''.join(map(str, sh)), 2)
    hex_sh = hex(int_sh)[2:]
    '''
    str_sh = ""
    for i in range(len(sh)):
        if (sh[i] >= 0):
            str_sh += "1"
        else:
            str_sh += "0"

    return str_sh


def provediPretrazivanje(N, I, K, sazetci):
    gl_sazetak = sazetci[I]
    rjesenje = 0
    for i in range(N):
        if (i == I):
            continue
        sazetak = sazetci[i]
        hamm = 0
        for c1, c2 in zip(gl_sazetak, sazetak):
            if (c1 != c2):
                hamm += 1
                if (hamm > K):
                    break
        if (hamm <= K):
            rjesenje += 1

    return rjesenje



N = int(input())
sazetci = []
for i in range(N):
    jed = input()
    sazetak = simhash(jed)
    sazetci.append(sazetak)


Q = int(input())
for i in range(Q):
    upit = input().split(" ")
    I = int(upit[0])
    K = int(upit[1])
    rjesenje = provediPretrazivanje(N, I, K, sazetci)
    print(rjesenje)

