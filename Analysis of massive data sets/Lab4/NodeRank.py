
info = input().split(' ')
n = int(info[0])
beta = float(info[1])
teleport = (1 - beta) / n

graf = {}
for cvor in range(n):
    ulaz = input().rstrip().split(' ')
    veze = []
    for v in ulaz:
        veze.append(int(v))
    graf[cvor] = veze

r = {}
for i in range(n):
    r[(i, 0)] = 1 / n

for iteracija in range(1, 101):
    for cvor in graf:
        r[(cvor, iteracija)] = teleport
    for cvor in graf:
        r_old = beta * (r[(cvor, iteracija-1)] / len(graf[cvor]))
        for veza in graf[cvor]:
            r[(veza, iteracija)] += r_old

q = int(input())
for i in range(q):
    info = input().split(' ')
    n = int(info[0])
    t = int(info[1])
    print ("%.10f" % r[(n, t)])





