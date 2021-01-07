;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;                                                                       ;;
;;                         SVEUČILIŠTE U ZAGREBU                         ;;
;;                 Fakultet elektrotehnike i računarstva                 ;;
;;                       Napredni operacijski sustavi                    ;;
;;                        Vježba 2: Digitalni potpis                     ;;
;;                                                                       ;;
;;                             Jakov Dorontić                            ;;
;;                                                 Zagreb, svibanj, 2020 ;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;


BRZO POKRETANJE
===============
	Pokrenite runExample.exe i odaberite koji primjer želite pregledati.
Mape Primjer1, 2, 3, 4 i 5 već imaju sve potrebne .txt datoteke spremne za
dekripciju. Mapa PrimjerX je prazna i u nju sami možete staviti svoje
datoteke za dekripciju.

	Pokrenite generator.exe i unesite sve potrebne informacije. Po
završetku stvaraju se 7 datoteka kako je prikazano u uputama za laboratorij.
Samo Vam 3 datoteke trebaju za dekripciju (asymmetric_secret_key.txt,
signature.txt, envelope.txt) koje možete premijestiti u mapu PrimjerX i
provjeriti ispravnost dekripcije.


NAŽALOST FERKO IMA OGRANIČENJE NA VELIČINU .ZIP DATOTEKE I RADI TOGA
NE MOGU PREDATI .EXE DATOTEKE.
Stoga Vas molim da pročitate upute za instalaciju u nastavku.


UPUTE ZA INSTALACIJU
====================
	Laboratorijska vježba 2 je napisana i pokrenuta na Windows 7
operacijskom sustavu. Koristi programski jezik Python 2.7.9 i knjižnicu
PyCrypto koja zahtjeva da se instalira Microsoft Visual C++ 9.0 program.

	pip install pycrypto

Za pokretanje, koristite naredbe:

	python generator.py
	
	python runExample.py

