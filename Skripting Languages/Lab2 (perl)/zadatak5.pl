#!/usr/bin/perl

# zastavica služi za odredjivanje prvog retka
$zastavica = 0;

while (defined($redak = <>)){
	chop($redak);
	
	if ($zastavica == 0){
		@faktori = split ';', $redak;
		$zastavica = 1;
	}else{
		@bodovi = split ';', $redak;
		$jmbg = shift(@bodovi);
		$prezime = shift(@bodovi);
		$ime = shift(@bodovi);
		
		$suma = 0;
		foreach $k (0..$#faktori){
			if ($bodovi[$k] eq '-'){
				$suma += 0;
			}
			$suma += ($faktori[$k]*$bodovi[$k]);
		}
		
		#spremimo studenta u listu
		push (@rang, "$suma;$jmbg;$prezime;$ime");
	}
	
}

@rang = reverse sort{$a <=> $b} @rang;

print "\nLista po rangu:\n";
print "-------------------\n";
foreach $i (0..$#rang){
	($S,$J,$P,$I) = split (';', $rang[$i]);
	print "  " . $i+1 . ". $P, $I ($J)  : $S\n";
}

