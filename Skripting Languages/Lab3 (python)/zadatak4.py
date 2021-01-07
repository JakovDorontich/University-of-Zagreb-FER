#!/usr/bin/perl

import sys
import urllib.request
import re

url = sys.argv[1]
#url = "http://www.python.org"

stranica = urllib.request.urlopen(url)
mybytes = stranica.read()
mystr = mybytes.decode("utf8")

linkovi = re.findall('href="([^"]+)"', mystr)

hostovi = {}
print("Linkovi:")
for link in linkovi:
    host = ""
    print ("\t"+link)
    if "https://" in link:
        host = link.replace("https://", "")
        host = host.replace("www.", "")
        host = host.split("/")[0]
    elif "http://" in link:
        host = link.replace("http://", "")
        host = host.replace("www.", "")
        host = host.split("/")[0]
    elif "www." in link:
        host = link.replace("www.", "")
        host = host.split("/")[0]
        
    if host != "":
        if hostovi.get(host, 0) == 0:
            hostovi[host] = 1
        else:
            hostovi[host] += 1

print("\nHostovi:")
for host, br in hostovi.items():
    print(str(br)+":\t"+host)

print("\nE-mailovi:")
mailovi = re.findall('([a-zA-Z0-9.]+@[a-zA-Z0-9]+\.[a-zA-Z0-9]+)', mystr)
for mail in mailovi:
    print("\t"+mail)

print("\nBroj slika:")
slike = re.findall('<img .* src="[^"]+"[^>]+>', mystr)
print ("\t"+str(len(slike)))

        

