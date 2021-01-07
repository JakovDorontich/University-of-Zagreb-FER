#include <stdio.h>
#include <pthread.h>
#include <stdlib.h>
#include <time.h>

int PRAVO=1;
int ZASTAVICA[2]={0,0};

void *dekker(void *x){
	int j = 1 - *((int *)x);

	for(int k = 1; k <= 5; k++){
		udi_u_KO(*((int *)x),j);
		for (int m = 1; m <= 5; m++){
			printf("Dretva: %d, K.O. br: %d (%d/5)\n",*((int *)x)+1,k,m);
			sleep(1);
		}
		izadi_iz_KO(*((int *)x),j);
	}
}


void udi_u_KO(int i, int j){
	ZASTAVICA[i]=1;
	while (ZASTAVICA[j] != 0){
		if (PRAVO == j){
			ZASTAVICA[i]=0;
			while (PRAVO == j);	//cekalica
			ZASTAVICA[i]=1;
		}
	}
}


void izadi_iz_KO(int i, int j){
	PRAVO=j;
	ZASTAVICA[i]=0;
}


int main(void){
	int BR[2];
	pthread_t thr_id[2];
	
	for(int i = 0; i < 2; i++){
		BR[i]=i;
		if (pthread_create(&thr_id[i],NULL,dekker,&BR[i]) != 0){
			printf("GRESKA! Ne mogu stvoriti dretvu.\n");
			exit(2);
		}
	}
	
	for(int i = 0; i < 2; i++){
		pthread_join(thr_id[i],NULL);
	}
	
	return 0;
}
