#define _XOPEN_SOURCE 500
#define MAXREAD 20 
#include <stdio.h>
#include <stdlib.h>
#include <sys/types.h>
#include <sys/ipc.h>
#include <sys/msg.h>
#include <sys/stat.h>
#include <signal.h>
#include <errno.h>
#include <time.h>
#include <string.h>
#include <fcntl.h>
#include <ftw.h>
#include <unistd.h>
#include <limits.h>


int brProcesa;


// ************ metode za rad s redom ************

struct my_queue {
	int index;
	int clock;
};

//red se sortira po satu, a ako su satovi isti onda se sortira po indexu procesa
void sort_queue(struct my_queue *q, int n) {
	struct my_queue temp;
	for(int i=1; i<n; i++) {
		for(int j=0; j<n-1; j++) {
			if(q[j].clock > q[j+1].clock) {
				temp = q[j];
				q[j] = q[j+1];
				q[j+1] = temp;
			}else if(q[j].clock == q[j+1].clock) {
				if(q[j].index > q[j+1].index) {
					temp = q[j];
					q[j] = q[j+1];
					q[j+1] = temp;
				}
			}
		}
	}
}

void init_queue(struct my_queue *q, int n) {
	for(int i=0; i<n; i++) {
		q[i].index = -1;
		q[i].clock = INT_MAX;
	}
}

void pop_queue(struct my_queue *q, int n) {
	q[0].index = -1;
	q[0].clock = INT_MAX;
	sort_queue(q, n);
}


// ************ metode za rad s cjevovodima ************

int unlink_cb(const char *fpath, const struct stat *sb, int typeflag, struct FTW *ftwbuf) {
    int rv = remove(fpath);
    return rv;
}

//rekurzivno brisanje svega u direktoriju
int rmrf(char *path) {
    return nftw(path, unlink_cb, 0600, FTW_DEPTH | FTW_PHYS);
}


void obrisi_cjevovod(int failure) {
	char path[] = "./cjev";
	rmrf(&path);
	exit(0);
}

int uskladi_sat(Ci, Tj){
	int max;
	max = (Ci > Tj) ? Ci : Tj;
	return max + 1;
}

int strToInt(char* str){
    int mult = 1;
    int re = 0;
    int len = strlen(str);
    for(int i=strlen(str)-1; i>=0; i--) {
        re = re + ((int)str[i]-48)*mult;
        mult = mult*10;
    }
    return re;
}

// ************ metoda za rad s procesima ************

void filozof(int i, int *popis_veza) {
	struct my_queue red_zahtjeva[10];
	char zahtjev[]="ZAHTJEV";
	char odgovor[]="ODGOVOR";
	char izlazak[]="IZLAZAK";
	char path[10], message[MAXREAD], buf[MAXREAD];
	char A, B;
	int r, brojacW, brojacR, veza, a, b, pfd, num, Cj, j;
	int indexZahtjeva = 0;
	int clock = 0;
	int brOdgovora = 0;
	int brVeza = brProcesa*(brProcesa-1);
	int brCjev = brProcesa-1;
	int pfd_polje_r[8], pfd_polje_w[8];

	srand(time(NULL)+i);
	clock = (rand()%(30-1))+1;
	init_queue(&red_zahtjeva, brProcesa);
	
	path[0]='.';path[1]='/';path[2]='c';path[3]='j';path[4]='e';path[5]='v';path[6]='/';
	
	//otvori cjevovode filozofa "i" prema ostalim filozofima za pisanje te za citanje
	brojacW=0;brojacR=0;
	int upis;
	for(int n=0; n<brVeza; n++) {
		veza = popis_veza[n];
		a=veza/10; b=veza%10;
		A='0'+a; B='0'+b;
		path[7]=A;
		path[8]=B;
		path[9]='\0';
		//ako je cjevovod obilka "12" a mi smo filozof 1 tada ga otvori za pisanje (prema 2)
		if (a==i) {
			pfd = open(path, O_WRONLY);
			upis=pfd*100+a*10+b;
			pfd_polje_w[brojacW] = upis;
			brojacW++;
		}
		//ako je cjevovod obilka "21" a mi smo filozof 1 tada ga otvori za citanje (od 2)
		if (b==i) {
			pfd = open(path, O_RDONLY);
			upis=pfd*100+a*10+b;
			pfd_polje_r[brojacR] = upis;
			brojacR++;
		}
	}

	//---sudjeluj na konferenciji---
	r=(rand()%(3-1))+1;
	sleep(r);
	
	
	//stavi poruku ZAHTJEV(i, T(i)) u svoj red_zahtjeva
	red_zahtjeva[0].index = i;
	red_zahtjeva[0].clock = clock;
	//salji ZAHTJEV(i, T(i)) ostalim procesima
	for(int n=0; n<brCjev; n++) {
		printf("P%d: saljem ZAHTJEV(%d, %d)\n", i, i, clock);
		num = clock*10+i;
		sprintf(message, "%d", num);
		(void) write(pfd_polje_w[n]/100, message, strlen(message) + 1);
	}

	//kada proces primi poruku ZAHTJEV(j, T(j))
	for(int n=0; n<brCjev; n++) {
		//procitaj ZAHTJEV(j, T(j))
		(void) read(pfd_polje_r[n]/100, buf, MAXREAD);
		num = strToInt(&buf);
		Cj = num/10;
		j = num%10;
		printf("P%d: procitao ZAHTJEV(%d, %d)\n", i, j, Cj);
		//uskladi svoj sat Ci
		clock = uskladi_sat(clock, Cj);
		//stavi ZAHTJEV(j, T(j)) u svoj red_zahtjeva
		indexZahtjeva++;
		red_zahtjeva[indexZahtjeva].index = j;
		red_zahtjeva[indexZahtjeva].clock = Cj;
		sort_queue(&red_zahtjeva, brProcesa);
		//salji ODGOVOR(i, T(i)) procesu koji ti je poslao ZAHTJEV
		printf("P%d: saljem ODGOVOR(%d, %d)\n", i, i, clock);
		num = clock*10+i;
		sprintf(message, "%d", num);
		(void) write(pfd_polje_w[n]/100, message, strlen(message) + 1);
	}
	

	for(int n=0; n<brCjev; n++) {
		//ako si prvi u redu_zahtjeva tada kreni u K.O.
		if(red_zahtjeva[0].index == i) {
			break;
		}
		sleep(2);
		//procitaj IZLAZAK(j, T(j))
		(void) read(pfd_polje_r[n]/100, buf, MAXREAD);
		num = strToInt(&buf);
		Cj = num/10;
		j = num%10;
		//uskladi svoj sat Ci
		clock = uskladi_sat(clock, Cj);
		//ukloni prvoga iz reda
		pop_queue(&red_zahtjeva, brProcesa);
	}


	printf("\tFilozof %d je za stolom.\n", i);
	//---K.O.---
	r=(rand()%(10-1))+1;
	sleep(r);
	
	
	//ukloni sebe iz red_zahtjeva
	pop_queue(&red_zahtjeva, brProcesa);
	//posalji svim procesima IZLAZAK(i, T(i))
	for(int n=0; n<brCjev; n++) {
		printf("P%d: saljem IZLAZAK(%d, %d)\n", i, i, clock);
		num = clock*10+i;
		sprintf(message, "%d", num);
		(void) write(pfd_polje_w[n]/100, message, strlen(message) + 1);
	}
	

	
	//---sudjeluj na konferenciji---
	r=(rand()%(2-1))+1;
	sleep(r);
	
}


int main(void) {
	char path[] = "./cjev";
	char path2[10];
	char A, B;
	int veza = 0;
	int popis_veza[72];
	int brojac;
	srand((unsigned)time(NULL));
	
	printf("Unesite broj filozofa (procesa): ");
	scanf("%d", &brProcesa);
	if ((brProcesa < 3) || (brProcesa > 9)) {
		printf("GRESKA! Krivi unos. Interval [3,9]\n");
		exit(1);
	}
	
	//stvori mapu za cjevovode
	mkdir(path, 0600);
	sigset(SIGINT, obrisi_cjevovod);
	
	path2[0]='.';path2[1]='/';path2[2]='c';path2[3]='j';path2[4]='e';path2[5]='v';path2[6]='/';
	brojac = 0;
	//stvori n(n-1) cjevovoda gdje je ime cjevovoda oblika "12" sto oznacava orjentaciju toka: od 1 prema 2
	for(int a=1; a<=brProcesa-1; a++) {
		for(int b=a+1; b<=brProcesa; b++) {
			//za 4 procesa to su: "12","13","14","23","24","34"
			veza = a*10+b;
			popis_veza[brojac] = veza;
			brojac++;
			A='0'+a; B='0'+b;
			path2[7]=A;
			path2[8]=B;
			path2[9]='\0';
			if (mknod(path2, S_IFIFO | 00600, 0)==-1) {
				printf("GRESKA! Ne mogu stvoriti cjevovod.\n");
				exit(1);
			}
			//zatim: "21","31","41","32","42","43"
			veza = b*10+a;
			popis_veza[brojac] = veza;
			brojac++;
			A='0'+a; B='0'+b;
			path2[7]=B;
			path2[8]=A;
			path2[9]='\0';
			if (mknod(path2, S_IFIFO | 00600, 0)==-1) {
				printf("GRESKA! Ne mogu stvoriti cjevovod.\n");
				exit(1);
			}
			
		}
	}
	
	//stvori procese filozofe
	for(int i=1; i<=brProcesa; i++) {
		switch(fork()) {
			case -1: printf("GRESKA! Ne mogu stvoriti proces.\n");
				 exit(2);
			case  0: filozof(i, &popis_veza);
				 exit(0);
			default: break;
		}
	}


	for (int i=1; i<=brProcesa; i++) {
		wait(NULL);
	}
	
	//obrisi mapu i sve cjevovode
	rmrf(&path);
	
	return 0;
}



