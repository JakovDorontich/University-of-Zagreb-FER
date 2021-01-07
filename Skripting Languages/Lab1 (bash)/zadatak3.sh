#!/bin/bash

for ime in `ls | grep -E '[0-9]{4}-02-[0-9]{2}'`
do
# varijabla $ime je oblika: localhost_access_log.2008-02-24.txt
	datum=`echo $ime | cut -d "." -f 2`
	dd=`echo $datum | cut -d "-" -f 3`
	mm=`echo $datum | cut -d "-" -f 2`
	gggg=`echo $datum | cut -d "-" -f 1`
	
	echo "datum: $dd-$mm-$gggg"
	echo "--------------------------------------------------"
	cut -d '"' -f 2 $ime | sort | uniq -c | sort -n -r

done
