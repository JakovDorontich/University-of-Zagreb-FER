#include <stdio.h>
#include <signal.h>
#include <unistd.h>
#include <sys/time.h>
#include <stdlib.h>
#include <math.h>


int pauza = 0;
long broj = 1000000001;
long zadnji = 1000000001;

void periodicki_ispis () {
	printf("zadnji prosti broj = %ld\n",zadnji);
}

int postavi_pauzu () {
	pauza = 1 - pauza;
	return pauza;
}

void prekini () {
	printf("zadnji prosti broj = %ld\n",zadnji);
	exit(0);
}

int prost ( unsigned long n ) {
	unsigned long i, max;

	if ( ( n & 1 ) == 0 ) /* je li paran? */
		return 0;

	max = sqrt ( n );
	for ( i = 3; i <= max; i += 2 )
		if ( ( n % i ) == 0 )
			return 0;

	return 1; /* broj je prost! */
}

int main(void){

	struct itimerval t;

	/* povezivanje obrade signala SIGALRM sa funkcijom "periodicki_ispis" */
	sigset(SIGALRM, periodicki_ispis );
	sigset(SIGINT, postavi_pauzu);
	sigset(SIGTERM, prekini);

	/* definiranje periodičkog slanja signala */
	/* prvi puta nakon: */
	t.it_value.tv_sec = 5;
	t.it_value.tv_usec = 500000;
	/* nakon prvog puta, periodicki sa periodom: */
	t.it_interval.tv_sec = 5;
	t.it_interval.tv_usec = 500000;

	/* pokretanje sata s pridruženim slanjem signala prema "t" */
	setitimer ( ITIMER_REAL, &t, NULL );


	while(1){
		if (prost(broj) == 1){
			zadnji = broj;
		}
		broj++;
		while (pauza == 1){
			pause();
		}
	}

	return 0;
}
