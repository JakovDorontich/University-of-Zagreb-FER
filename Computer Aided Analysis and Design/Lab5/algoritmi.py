from matrica import *


def euler(A, B, x0, rt, T, tmax, ispis=1, cetvrti=False):
    Xk = x0
    U = jedinicna(A.getNumberOfRows())
    t = T
    x1 = []; x1.append(Xk.matrix[0][0])
    x2 = []; x2.append(Xk.matrix[1][0])
    iteracija = 0
    while (t <= tmax):
        if (cetvrti == True): rt = matrica([[t],[t]])
        iteracija += 1
        M = (U + A.mulScalar(T))
        N = B.mulScalar(T)
        Xk1 = M*Xk + N*rt
        if (iteracija % ispis == 0):
            x1.append(Xk1.matrix[0][0])
            x2.append(Xk1.matrix[1][0])
        Xk = Xk1
        t = round(t + T, 5)
    return Xk, x1, x2

def obrnutiEuler(A, B, x0, rt, T, tmax, ispis=1, cetvrti=False):
    Xk = x0
    U = jedinicna(A.getNumberOfRows())
    t = T
    x1 = []; x1.append(Xk.matrix[0][0])
    x2 = []; x2.append(Xk.matrix[1][0])
    iteracija = 0
    while (t <= tmax):
        if (cetvrti == True): rt = matrica([[t], [t]])
        iteracija += 1
        P = inverse(U - A.mulScalar(T))
        Q = P.mulScalar(T) * B
        Xk1 = P*Xk + Q*rt
        if (iteracija % ispis == 0):
            x1.append(Xk1.matrix[0][0])
            x2.append(Xk1.matrix[1][0])
        Xk = Xk1
        t = round(t + T, 5)
    return Xk, x1, x2

def trapezni(A, B, x0, rt, T, tmax, ispis=1, cetvrti=False):
    Xk = x0
    U = jedinicna(A.getNumberOfRows())
    t = T
    x1 = []; x1.append(Xk.matrix[0][0])
    x2 = []; x2.append(Xk.matrix[1][0])
    iteracija = 0
    while (t <= tmax):
        if (cetvrti == True): rt = matrica([[t], [t]])
        iteracija += 1
        R = inverse(U - A.mulScalar(T/2)) * (U + A.mulScalar(T/2))
        S = inverse(U - A.mulScalar(T/2)).mulScalar(T/2) * B
        Xk1 = R*Xk + S*(rt+rt)
        if (iteracija % ispis == 0):
            x1.append(Xk1.matrix[0][0])
            x2.append(Xk1.matrix[1][0])
        Xk = Xk1
        t = round(t + T, 5)
    return Xk, x1, x2

def rungeKutta(A, B, x0, rt, T, tmax, ispis=1, cetvrti=False):
    Xk = x0
    t = T
    x1 = []; x1.append(Xk.matrix[0][0])
    x2 = []; x2.append(Xk.matrix[1][0])
    iteracija = 0
    while (t <= tmax):
        if (cetvrti == True): rt = matrica([[t], [t]])
        iteracija += 1
        m1 = A*Xk + B*rt
        m2 = A * (Xk + m1.mulScalar(T/2)) + B*rt
        m3 = A * (Xk + m2.mulScalar(T/2)) + B*rt
        m4 = A * (Xk + m3.mulScalar(T)) + B*rt
        m = m1 + m2.mulScalar(2) + m3.mulScalar(2) + m4
        Xk1 = Xk + m.mulScalar(T/6)
        if (iteracija % ispis == 0):
            x1.append(Xk1.matrix[0][0])
            x2.append(Xk1.matrix[1][0])
        Xk = Xk1
        t = round(t + T, 5)
    return Xk, x1, x2

def pred_kor_1(A, B, x0, rt, T, tmax, ispis=1, s=2, cetvrti=False):
    Xk = x0
    U = jedinicna(A.getNumberOfRows())
    t = T
    x1 = []; x1.append(Xk.matrix[0][0])
    x2 = []; x2.append(Xk.matrix[1][0])
    iteracija = 0
    while (t <= tmax):
        if (cetvrti == True): rt = matrica([[t], [t]])
        iteracija += 1
        M = (U + A.mulScalar(T))
        N = B.mulScalar(T)
        # prediktor Euler
        Xk1 = M*Xk + N*rt
        # korektor obrnuti Euler
        for i in range(s):
            Xk1 = Xk + (A*Xk1 + B*rt).mulScalar(T)
        if (iteracija % ispis == 0):
            x1.append(Xk1.matrix[0][0])
            x2.append(Xk1.matrix[1][0])
        Xk = Xk1
        t = round(t + T, 5)
    return Xk, x1, x2

def pred_kor_2(A, B, x0, rt, T, tmax, ispis=1, cetvrti=False):
    Xk = x0
    U = jedinicna(A.getNumberOfRows())
    t = T
    x1 = []; x1.append(Xk.matrix[0][0])
    x2 = []; x2.append(Xk.matrix[1][0])
    iteracija = 0
    while (t <= tmax):
        if (cetvrti == True): rt = matrica([[t], [t]])
        iteracija += 1
        M = (U + A.mulScalar(T))
        N = B.mulScalar(T)
        # prediktor Euler
        Xk1 = M*Xk + N*rt
        # korektor trapez
        Xk1 = Xk + (A*Xk+B*rt + A*Xk1+B*rt).mulScalar(T/2)
        if (iteracija % ispis == 0):
            x1.append(Xk1.matrix[0][0])
            x2.append(Xk1.matrix[1][0])
        Xk = Xk1
        t = round(t + T, 5)
    return Xk, x1, x2


