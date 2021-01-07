#!/usr/bin/perl

$dulj_prefiksa = pop(@ARGV);

while (defined($redak = <>)){
	chop($redak);
	
	$redak =~ tr/A-Z/a-z/;
	@rijeci = ($redak =~ m/\b(\w{$dulj_prefiksa})/g);
	
	foreach $rijec (@rijeci){
		$hash_mapa{$rijec} += 1;
	}
}

@popis = sort keys %hash_mapa;

foreach $rijec (@popis){
	print "$rijec : $hash_mapa{$rijec}\n";
}
