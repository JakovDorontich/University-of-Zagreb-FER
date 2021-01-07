#!/usr/bin/perl

print "Unesite broj pa pritisnite enter.\nPonavljajte postupak.\nKada ste gotovi sa unosom pritisnite Ctrl+D";
print "\n";
@brojevi = <STDIN>;

foreach $broj (@brojevi){
	$suma += $broj;
}
$ari = $suma / @brojevi;

print "Aritmeticka sredina unesenih brojeva je: $ari";