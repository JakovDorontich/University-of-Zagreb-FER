import copy

class matrica():

    def __init__(self, matrix):
        self.matrix = matrix

        numberOfRows = 0
        numberOfColumns = 0
        for row in self.matrix:
            numberOfColumns = len(row)
            numberOfRows += 1

        self.dimension = [numberOfColumns, numberOfRows]
        self.numberOfRows = numberOfRows
        self.numberOfColumns = numberOfColumns

    def __add__(self, other):
        result = []
        for i in range(0, self.numberOfRows):
            pomSum = []
            for j in range(0, self.numberOfColumns):
                pomSum.append(self.matrix[i][j] + other.matrix[i][j])
            result.append(pomSum)
        return matrica(result)

    def __sub__(self, other):
        result = []
        for i in range(0, self.numberOfRows):
            pomSum = []
            for j in range(0, self.numberOfColumns):
                pomSum.append(self.matrix[i][j] - other.matrix[i][j])
            result.append(pomSum)
        return matrica(result)

    def __mul__(self, other):
        if (self.numberOfColumns == other.numberOfRows):
            result = [[0 for col in range(other.numberOfColumns)] for row in range(self.numberOfRows)]
            # prolaz kroz retke matrice A
            for i in range(0, self.numberOfRows):
                # prolaz kroz stupce matrice B
                for j in range(0, other.numberOfColumns):
                    # prolaz kroz retke matrice B
                    for k in range(0, other.numberOfRows):
                        result[i][j] += self.matrix[i][k] * other.matrix[k][j]
            return matrica(result)
        else:
            print("Error: broj stupaca prve matrice (A) nije jednaku broju redaka druge matrice (B)")
            return None

    def __eq__(self, other):
        if (self.numberOfColumns == other.numberOfColumns and self.numberOfRows == other.numberOfRows):
            for i in range (0, self.numberOfRows):
                for j in range (0, self.numberOfColumns):
                    if (round(self.matrix[i][j], 9) != round(other.matrix[i][j], 9)):
                        return False
                    else:
                        return True
        else:
            return False

    def getDimension(self):
        return self.dimension

    def getMatrix(self):
        return self.matrix

    def getNumberOfRows(self):
        return self.numberOfRows

    def getNumberOfColumns(self):
        return self.numberOfColumns

    def getValue(self, row, column):
        return self.matrix[row][column]

    def setValue(self, row, column, value):
        self.matrix[row][column] = float(value)
        return matrica(self.matrix)

    def copy(self):
        return copy.deepcopy(matrica(self.matrix))

    def transpose(self):
        result = []
        for i in range(0, self.numberOfColumns):
            pomTran = []
            for j in range(0, self.numberOfRows):
                pomTran.append(self.matrix[j][i])
            result.append(pomTran)
        return matrica(result)

    def mulScalar(self, scalar):
        result = []
        for i in range(0, self.numberOfRows):
            pomTran = []
            for j in range(0, self.numberOfColumns):
                pomTran.append(self.matrix[i][j]*scalar)
            result.append(pomTran)
        return matrica(result)


# ucitavanje zadane matrice
# ulaz: takstualna datoteka s vrijednostima matrice
#       po stupcima i redcima
# izlaz: 2D polje koje predstavlja matricu
def loadMatrix(file):
    data = open(file)
    matrix = []
    for line in data:
        dataRow = []
        values = line.split(" ")
        for value in values:
            dataRow.append(float(value))
        matrix.append(dataRow)
    return matrix

# ispis matrice na standardni izlaz
# ulaz: klasa matrica()
def printMatrix(A):
    string = ""
    for row in A.matrix:
        for value in row:
            string += str(value)+" "
        string += "\n"
    print (string)

# ispis matrice u novu tekstualnu datoteku
# ulaz: klasa matrica() i
#       naziv tekstualne datoteke "<name>.txt"
def exportMatrix(A, file):
    string = ""
    for row in A.matrix:
        for value in row:
            string += str(value) + " "
        string += "\n"
    data = open(file, "w")
    data.write(string)

# skalarni produkt dvaju matrica
# ulaz: matrice A i B tipa 2D polja
# izlaz: skalarni produkt dvaju matrica A i B
def scalarProduct(A, B):
    result = 0
    for i in range(len(A)):
        result += A[i] * B[i]
    return result

# mnozenje retka matrice skalarom
# ulaz: matrica m klase matrica()
#       redak i=0,1,2,...
#       value vrijednost kojom mnozimo redak matrice
# izlaz: promijenjena matrica m klase matrica()
def mulMatrixRow(m, i, value):
    colums = m.getNumberOfColumns()
    for j in range(colums):
        m.setValue(i, j, m.getValue(i,j)*value)

# supstitucija unaprijed
# ulaz: matrica L klase matrica()
#       matrica b klase matrica()
#       vrijednost zaokruzivanja rezultata dijeljenja decimal
# izlaz: matrica y klase matrica()
def forwardSubstitution (L, b, decimal=9):
    y = [0 for i in range(L.numberOfRows)]
    for i in range(L.numberOfRows):
        y[i] = (b.matrix[i][0] - scalarProduct(y, L.matrix[i]))/float(L.matrix[i][i])
    result = [[round(y[i], decimal)] for i in range(len(y))]
    return matrica(result)

# supstitucija unazad
# ulaz: matrica U klase matrica()
#       matrica y klase matrica()
#       vrijednost zaokruzivanja decimal
# izlaz: matrica x klase matrica()
def backSubstitution (U, y, decimal=9):
    x = [0 for i in range(U.numberOfRows)]
    for i in reversed(range(U.numberOfRows)):
        if (U.matrix[i][i] == 0): raise ZeroDivisionError("Dijelimo s 0 u supstituciji unazad!")
        x[i] = (y.matrix[i][0] - scalarProduct(x, U.matrix[i])) / float(U.matrix[i][i])
    result = [[round(x[i], decimal)] for i in range(len(x))]
    return matrica(result)

# zaokruzivanje vrijednosti elemenata matrice
# ulaz: matrica m klase matrica()
#       vrijednost zaokruzivanja decimal
# izlaz: ustimana matrica m klase matrica()
def roundMatrix(m, decimal=9):
    rows = m.getNumberOfRows()
    for i in range(rows):
        for j in range(rows):
            m.setValue(i, j ,round(m.getValue(i, j), decimal))

# eliminacija premalenih vrijednosti elemenata matrice
# ulaz: matrica m klase matrica()
#       vrijednost tolerancije malenih brojeva epsilon
#       vrijednost zaokruzivanja decimal
# izlaz: promijenjena matrica m klase matrica()
def adjustMatrix(m, epsilon=0.000000001, decimal=9):
    rows = m.getNumberOfRows()
    for i in range(rows):
        for j in range(rows):
            if (abs(m.getValue(i, j)) <= epsilon):
                m.setValue(i, j ,float(0))
    roundMatrix(m, decimal)

# LU dekompozicija zadane matrice
# ulaz: matrica m klase matrica()
#       vrijednost tolerancije malenih brojeva epsilon
# izlaz: promijenjena matrica m klase matrica()
#        matrica L klase matrica()
#        matrica U klase matrica()
def LU_decomposition(m, epsilon=0.000000001):
    rows = m.getNumberOfRows()
    pivot = 0
    for i in range(rows-1):
        if (m.getValue(pivot, pivot) == 0):
            raise ZeroDivisionError("Pivot element je postao jednak 0 u " + str(i+1) + " iteraciji!")
        # prolaz za dijeljenje pivotom
        for j in range(pivot+1, rows):
            r = float(m.getValue(j, pivot)/m.getValue(pivot, pivot))
            if (epsilon != None and abs(r) < epsilon): r = 0
            m.setValue(j, pivot, r)
        # prolaz po mini matrici
        for i, j in [[col, row] for col in range(pivot+1, rows) for row in range(pivot+1, rows)]:
            m.setValue(i, j, float(m.getValue(i,j) - m.getValue(pivot, j) * m.getValue(i, pivot)))
        pivot+=1

    adjustMatrix(m, epsilon)
    L = []; U = []
    for i in range(rows):
        pom_L = []; pom_U = []
        for j in range(rows):
            if (i == j):
                pom_L.append(float(1))
                pom_U.append(m.getValue(i, j))
            elif (i > j):
                pom_L.append(m.getValue(i, j))
                pom_U.append(float(0))
            elif (i < j):
                pom_L.append(float(0))
                pom_U.append(m.getValue(i, j))
        L.append(pom_L)
        U.append(pom_U)

    return matrica(L), matrica(U)

# LUP dekompozicija zadane matrice
# ulaz: matrica m klase matrica()
#       vrijednost tolerancije malenih brojeva epsilon
# izlaz: promijenjena matrica m klase matrica()
#        matrica L klase matrica()
#        matrica U klase matrica()
#        matrica P klase matrica()
def LUP_decomposition(m, epsilon=0.000000001):
    P = []
    rows = m.getNumberOfRows()
    pivot = 0

    # kreiraj jedinicnu matricu
    for i in range(rows):
        E = []
        for j in range(rows):
            if (i == j): E.append(float(1))
            else: E.append(float(0))
        P.append(E)
    P = matrica(P)

    for i in range(rows-1):
        # prolaz za pronalazak max pivota
        tmp_m = m.matrix[pivot]; pom_m = m.matrix[pivot]
        tmp_P = P.matrix[pivot]; pom_P = P.matrix[pivot]
        j_max = pivot+1
        zastavica = False
        for j in range(pivot+1, rows):
            if (tmp_m[pivot] < m.matrix[j][pivot] or tmp_m[pivot] == 0):
                pom_m = m.matrix[j]
                pom_P = P.matrix[j]
                j_max = j
                zastavica = True
        if (zastavica == True):
            m.matrix[pivot] = pom_m
            m.matrix[j_max] = tmp_m
            P.matrix[pivot] = pom_P
            P.matrix[j_max] = tmp_P
        if (m.getValue(pivot, pivot) == 0):
            raise ZeroDivisionError("Pivot element je postao jednak 0 u " + str(i+1) + " iteraciji!")
        # prolaz za dijeljenje pivotom
        for j in range(pivot+1, rows):
            r = float(m.getValue(j, pivot)/m.getValue(pivot, pivot))
            if (epsilon != None and abs(r) < epsilon): r = 0
            m.setValue(j, pivot, r)
        # prolaz po mini matrici
        for i, j in [[col, row] for col in range(pivot+1, rows) for row in range(pivot+1, rows)]:
            m.setValue(i, j, float(m.getValue(i,j) - m.getValue(pivot, j) * m.getValue(i, pivot)))
        pivot+=1

    adjustMatrix(m, epsilon)
    L = []; U = []
    for i in range(rows):
        pom_L = []; pom_U = []
        for j in range(rows):
            if (i == j):
                pom_L.append(float(1))
                pom_U.append(m.getValue(i, j))
            elif (i > j):
                pom_L.append(m.getValue(i, j))
                pom_U.append(float(0))
            elif (i < j):
                pom_L.append(float(0))
                pom_U.append(m.getValue(i, j))
        L.append(pom_L)
        U.append(pom_U)

    return matrica(L), matrica(U), P

# inverz matrice pomocu LUP dekompozicije
# ulaz: matrica m klase matrica()
#       vrijednost tolerancije malenih brojeva epsilon
# izlaz: inverzna matrica klase matrica()
def inverse(m, epsilon=0.000000001):
    A = m.copy()
    I = []; inv = []
    rows = m.getNumberOfRows()

    # kreiraj polje vektora jedinicne matrice
    for i in range(rows):
        E = []
        for j in range(rows):
            if (i == j):
                E.append([float(1)])
            else:
                E.append([float(0)])
        I.append(matrica(E))

    L, U, P = LUP_decomposition(A)
    adjustMatrix(L, epsilon)
    adjustMatrix(U, epsilon)

    for i in range(rows):
        y = forwardSubstitution(L, P*I[i])
        x = backSubstitution(U, y)
        inv.append(x.transpose().matrix[0])

    inv = matrica(inv).transpose()
    return inv

# izracun determinante matrice pomocu LUP dekompozicije
# ulaz: matrica m klase matrica()
# izlaz: vrijednost determinante matrice m
def determinant(m):
    A = m.copy()
    rows = m.getNumberOfRows()
    L, U, P = LUP_decomposition(A)

    mulU = 1
    for i in range(rows):
        mulU *= U.getValue(i, i)

    S = 0
    for i in range(rows):
        if (P.getValue(i, i) == 0):
            S += 1
    S = S -1

    det = ((-1)**S) * mulU
    return det


