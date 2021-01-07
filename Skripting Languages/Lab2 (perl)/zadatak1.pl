#!/usr/bin/perl

print 'Unesite znakovni niz: ';
chop($znakNiz = <STDIN>);
print 'Unesite broj ponavljanja n: ';
chop($n = <STDIN>);

print "$znakNiz\n" x $n;
