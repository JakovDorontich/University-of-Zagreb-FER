#!/usr/bin/perl

while (defined($redak = <>)){
	chop($redak);
	
	@pom_split = split ':', $redak;
	$sat = $pom_split[1];
	
	$posjeta[$sat] += 1;
	
	if(eof){
		@pom_datum = split '.', $ARGV;
		$datum = $pom_datum[1];
		
		print "\n Datum: $datum\n";
		print " sat : broj pistupa\n";
		print "-------------------------------\n";
		foreach $h (0..23){
			print "  $h : $posjeta[$h]\n";
		}
		@posjeta = ( );
	}
	
}

