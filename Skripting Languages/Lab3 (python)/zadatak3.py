#!/usr/bin/perl

import os
import re

# {jmbag : (Prezime, Ime)}
studenti = {}
# {(jmabg, brLabVj): bodovi}
laboratoriji = {}
broj = []

dirs = os.listdir(".")
lines = open("studenti.txt", "r").read().split("\n")

for line in lines:
    pom = line.split(" ")
    studenti[pom[0]] = (pom[1], pom[2])
    

for datoteka in dirs:
    if re.match("(Lab_[0-9]+_g[0-9]+.txt)", datoteka):
        broj.append(int(datoteka.split("_")[1]))
        lines = open(datoteka, "r").read().split("\n")
        for line in lines:
            pom = line.split(" ")
            if (laboratoriji.get((pom[0], broj[-1]), 0) == 0):
                laboratoriji[(pom[0], broj[-1])] = pom[1]
            else:
                print ("Upozorenje: student sa JMBAG-om "+pom[0]+" je vise puta bio na "+str(broj[-1])+" laboratoriju!")


print ("JMBAG \t\t Prezime, Ime\t", end="")
broj.sort()
for br in broj:
    print("\tL"+str(br), end="")
print()
for jmbag, student in studenti.items():
    print(jmbag+"\t"+student[0]+", "+student[1]+"\t", end="")
    for br in broj:
        bodovi = laboratoriji.get((jmbag, br), "/")
        print("\t"+str(bodovi), end="")
    print()
