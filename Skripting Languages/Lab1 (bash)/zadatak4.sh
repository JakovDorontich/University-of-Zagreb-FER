#!/bin/bash

if [ "$#" -ne 2 ]
then
	echo "Incorrect number of arguments."
	echo "Usage: \"./zadatak4.sh <sourceDir>/ <destinationDir>/\""
	exit 1
fi


# argumenti su oblika: direktorij/

sourceDir=$1
destinationDir=$2


if [ ! -d $destinationDir ]
then
	mkdir $destinationDir
	echo -e "\n	created: $destinationDir"
fi


#  varijabla $slika je oblika: direktorij/imeSlike.png

for slika in `ls $sourceDir*\.*`
do
	datum=`stat $slika | grep -i 'Modify:' | grep -E -o '[0-9]{4}-[0-9]{2}'`
	gggg=`echo $datum | cut -d "-" -f 1`
	mm=`echo $datum | cut -d "-" -f 2`
	newDir="$destinationDir$gggg-$mm/"
	
	if [ ! -d $newDir ]
	then
		mkdir $newDir
		echo "	created: $newDir"
	fi
	
	mv "$slika" "$newDir"
done
