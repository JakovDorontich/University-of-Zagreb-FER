#!/usr/bin/perl

file = open("ulaz.txt", "r").read()
lines = file.split("\n")

hipoteze = []
for line in lines:
    pom = line.split(" ")
    pom = [float(i) for i in pom]
    pom.sort()
    hipoteze.append(pom)

print("Hyp#Q10#Q20#Q30#Q40#Q50#Q60#Q70#Q80#Q90")

for i in range(len(hipoteze)):
    print("{:03d}".format(i+1), end="")
    for posto in [round(x*0.1,1) for x in range(1,10)]:
        j = int(posto*len(hipoteze[i]))
        print ("#"+str(hipoteze[i][j]), end="")
    print()
