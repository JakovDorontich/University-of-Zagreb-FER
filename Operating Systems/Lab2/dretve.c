#include <stdio.h>
#include <stdlib.h>
#include <pthread.h>

int A;
int N,M;

void *Povecavaj(void *x){
	for (int i = 0; i < M; i++){
		A = *((int*)x) + 1;
	}
}

int main (int argc, int *argv[]){
	if (argc <= 2){
		printf("GRESKA! Unesite dva argumenta.\n");
		exit(1);
	}

	N = atoi(argv[1]);
	M = atoi(argv[2]);
	pthread_t thr_id[N];
	A = 0;
	
	for(int i = 0; i < N; i++){
		if (pthread_create(&thr_id[i],NULL,Povecavaj,&A) != 0){
			printf("GRESKA! Ne mogu stvoriti dretvu.\n");
			exit(2);
		}
	}

	for(int i = 0; i < N; i++){
		pthread_join(thr_id[i],NULL);
	}
	
	printf("A = %d\n",A);
	
	return 0;
}
