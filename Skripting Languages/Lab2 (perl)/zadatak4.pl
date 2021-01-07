#!/usr/bin/perl

while (defined($redak = <>)){
	chop($redak);
	
	($J,$P,$I,$T,$Z) = split (';', $redak);
	($T0,$T1,$T2) = split (' ', $T);
	($Z0,$Z1) = split (' ', $Z);
	
	($satT,$minT) = split (':', $T1);
	($satZ,$minZ) = split (':', $Z1);
	
	# sada kada smo dohvatili potrebne informacije pretvaramo ih u minute
	$satT *= 60;
	$satZ *= 60;
	
	$termin_u_min = $satT + $minT;
	$zkljuc_u_min = $satZ + $minZ;
	
	if ((($zkljuc_u_min - $termin_u_min) > 60) or ($T0 ne $Z0)){	
		print "$J $P $I - PROBLEM: $T0 $T1 --> $Z\n";
	}
	
}

