#!/bin/bash

proba="Ovo je proba"
echo $proba

lista_datoteka=`ls *\.*`
echo $lista_datoteka

proba3="$proba. $proba. $proba. "

a=4;b=3;c=7
d=$(($((a+4))*b%c))

broj_rijeci=`wc -w *.txt | grep total | awk '{print $1}'`
echo $broj_rijeci

find ~/

cut -d':' -f 1,6,7 </etc/passwd

ps | awk '{$1=$1;print}' | cut -f 1,6,8 -d' '

