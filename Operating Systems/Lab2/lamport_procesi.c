#include <stdio.h>
#include <signal.h>
#include <stdlib.h>
#include <time.h>
#include <sys/types.h>
#include <sys/ipc.h>
#include <sys/shm.h>
#include <stdatomic.h>

int Id_ZAP;
int *ZajednickaMem;
int *TRAZIM, *BROJ, *STOL;
int brProcesa, brStolova, brZauzetih = 0;
int ZADNJI_BROJ=0;

void brisiZAP(void){
	shmdt((char *) ZajednickaMem);
	shmctl(Id_ZAP, IPC_RMID, NULL);
	exit(0);
}


void udi_u_KO(int i){
	TRAZIM[i]=1;
	ZADNJI_BROJ++;
	BROJ[i]=ZADNJI_BROJ;
	TRAZIM[i]=0;
	
	for(int j = 0; j < brProcesa; j++){
		while (TRAZIM[j] != 0);
		while ((BROJ[j] != 0) && (BROJ[j] < BROJ[i] || (BROJ[j] == BROJ[i] && j < i)));
	}
}

void izadi_iz_KO(int i){
	BROJ[i] = 0;
}



void restoran(int i){
	int randStol;
	int brSlobStolova;
	int IdStola[brStolova];

	srand(time(NULL)+i);
	while (brZauzetih < brStolova){
		brSlobStolova=0;
		for (int j = 0; j < brStolova; j++){
			if(STOL[j] == 0){
				IdStola[brSlobStolova]=j;
				brSlobStolova++;
			}
		}


		sleep(1);
		randStol = rand()%brSlobStolova;
		printf("Proces %d: odabirem stol %d\n", i+1, IdStola[randStol]+1);
		udi_u_KO(i);
		if((STOL[IdStola[randStol]] == 0) && (brZauzetih < brStolova)){
			STOL[IdStola[randStol]]=i+1;
			brZauzetih++;
			printf("Proces %d: rezerviram stol %d, stanje:\n", i+1, IdStola[randStol]+1);
		}
		else printf("Proces %d: neuspjela rezervacija stola %d, stanje:\n", i+1, IdStola[randStol]+1);
		
		for (int j = 0; j < brStolova; j++){
			if(STOL[j] == 0) printf("-");
			else printf("%d", STOL[j]);
		}
		printf("\n");
		izadi_iz_KO(i);
		brSlobStolova--;
	}
}

int main(void){
	
	sigset(SIGINT, brisiZAP);

	printf("Unesite broj procesa: ");
	scanf("%d",&brProcesa);
	printf("Unesite broj stolova: ");
	scanf("%d",&brStolova);
	
	Id_ZAP = shmget(IPC_PRIVATE, (2*brProcesa+brStolova)*sizeof(int), 0600);
	if(Id_ZAP == -1){
		printf("GRESKA! Ne mogu stvoriti Zajednici Adresni Prostor.\n");
		exit(1);
	}
	
	ZajednickaMem = (int *) shmat(Id_ZAP, NULL, 0);
	TRAZIM = ZajednickaMem;
	BROJ = TRAZIM + (brProcesa*sizeof(int));
	STOL = BROJ + (brProcesa*sizeof(int));
	

	for(int i = 0; i < brStolova; i++){
		STOL[i] = 0;
	}
	for(int i = 0; i < brProcesa; i++){
		BROJ[i] = 0;
		TRAZIM[i] = 0;
	}


	for (int i = 0; i < brProcesa; i++){
		switch(fork()){
			case -1: printf("GRESKA! Ne mogu stvoriti proces.\n");
				 exit(2);
			case  0: restoran(i);
				 exit(0);
			default: break;
		
		}
		
	}

	for (int i = 0; i < brProcesa; i++){
		wait(NULL);
	}

	brisiZAP();
	return 0;
}
