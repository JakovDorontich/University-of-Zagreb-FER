#!/bin/bash

if [ "$#" -lt 2 ]
then
	echo "Incorrect number of arguments (at least 2 needed)."
	echo "Usage: \"./zadatak5.sh <list> <destinationDir>\""
	exit 1
fi

destinationDir=`echo $#: ${!#} | cut -d ' ' -f 2`
suma=0

if [ ! -d $destinationDir ]
then
	mkdir $destinationDir
	echo "Kreirano je kazalo $destinationDir"
fi

while [ "$#" -ne 1 ]
do
	if [ -e "$1" -a -r "$1" -a "$1" != "$destinationDir" ]
	then
		cp -r "$1" "$destinationDir"
		suma=$((suma+1))
	else
		echo "Cannot copy: $1"
	fi
	
	shift
done

echo "$suma datoteka kopirano je u kazalo $destinationDir."

