#!/bin/bash

if [ "$#" -ne 2 ]
then
	echo "Incorrect number of arguments."
	echo "Usage: \"./zadatak5.sh <sourceDir>/ '*.<fileExtension>'\""
	exit 1
fi


# argumenti su oblika: direktorij/   *.txt

echo -e "\nPredano ime direktorija: $1"
echo -e "Predano ime ekstenzije: $2\n"
sourceDir=$1
extension=$2

suma=0


#  varijabla $slika je oblika: direktorij/imeSlike.png

for folder in `find $sourceDir -type d`
do
	broj=`cat $folder/$extension | wc -l`
	echo "Unutar direktorija $folder bilo je $broj redaka."
	suma=$(($suma+$broj))
done

echo $suma
