from hashlib import md5
from time import sleep

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


def provediPretrazivanje(I, K, simHash, kandidati):
    gl_sazetak = simHash[I]
    rjesenje = 0
    if I not in kandidati:
        return 0
    for i in kandidati[I]:
        sazetak = simHash[i]
        hamm = 0
        for c1, c2 in zip(gl_sazetak, sazetak):
            if (c1 != c2):
                hamm += 1
                if (hamm > K):
                    break
        if (hamm <= K):
            rjesenje += 1

    return rjesenje


def hash2int(hash_16):
    sh = [0] * 16
    for i in range(len(sh)):
        if (hash_16[i] == "1"):
            sh[i] = 1
        else:
            sh[i] = 0
    int_sh = int(''.join(map(str, sh)), 2)
    return int_sh



N = int(input())
simHash = []
for i in range(N):
    jed = input()
    sazetak = simhash(jed)
    simHash.append(sazetak)

k = 128; b = 8; r = 16
kandidati = {}
for pojas in range(b):
    pretinci = {}
    for trenutni_id in range(N):
        hash = simHash[trenutni_id]
        a = pojas * r
        b = a + r
        hash_16 = hash[a:b]
        val = hash2int(hash_16)

        tekstovi_u_pretincu = set()
        if (pretinci.get(val)):
            tekstovi_u_pretincu = pretinci.get(val)
            for tekst_id in tekstovi_u_pretincu:
                if ( (kandidati.get(trenutni_id) is None) or (tekst_id not in kandidati.get(trenutni_id)) ):
                    kandidati.setdefault(trenutni_id, []).append(tekst_id)
                    kandidati.setdefault(tekst_id, []).append(trenutni_id)
        else:
            tekstovi_u_pretincu = set()
        tekstovi_u_pretincu.add(trenutni_id)
        pretinci[val] = tekstovi_u_pretincu


Q = int(input())
for i in range(Q):
    upit = input().split(" ")
    I = int(upit[0])
    K = int(upit[1])
    rjesenje = provediPretrazivanje(I, K, simHash, kandidati)
    print(rjesenje)

