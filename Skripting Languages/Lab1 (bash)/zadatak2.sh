#!/bin/bash

grep -i -E 'banana|jabuka|jagoda|dinja|lubenica' namirnice.txt

grep -i -v -E 'banana|jabuka|jagoda|dinja|lubenica' namirnice.txt > ne-voce.txt

grep -r -E '[A-Z]{3}[0-9]{6}' ~/projekti/

find . -mtime +7 -mtime -14 -ls

for i in {1..15};do printf "%d " $i; done
