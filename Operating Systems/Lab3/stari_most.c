#include <stdio.h>
#include <stdlib.h>
#include <pthread.h>
#include <time.h>
#include <malloc.h>

#define PLAVO  "\x1b[34m"
#define END    "\x1b[0m"

int *smjer;
int auti=0, smjer_na_mostu;
									//int prosli_odjednom=0, ima_cekaca;
pthread_mutex_t M;
pthread_cond_t uv[2];

void *Auto(void *rbr){
   printf("Auto %d cekana prelazak preko mosta\n",*((int*)rbr));
   Popni_se_na_most(smjer[*((int*)rbr)]);
   printf(PLAVO"Auto %d je na mostu\n"END,*((int*)rbr));
   sleep(1);
   printf("Auto %d je presao most\n",*((int*)rbr));
   Sidi_sa_mosta(smjer[*((int*)rbr)]);
}

void Popni_se_na_most(int SMJER){
   pthread_mutex_lock(&M);
   while((auti==3) || (auti>0 && SMJER!=smjer_na_mostu))	        //|| (prosli_odjednom==5 && ima_cekaca!=0)
      pthread_cond_wait(&uv[SMJER],&M);
									//prosli_odjednom++;
   auti++;
   smjer_na_mostu=SMJER;
   pthread_mutex_unlock(&M);
}

void Sidi_sa_mosta(int SMJER){
   pthread_mutex_lock(&M);
   auti--;
									//ima_cekaca--;
   if(auti == 0){
									//prosli_odjednom=0;
      for(int i=0; i<3; i++)
         pthread_cond_signal(&uv[1-SMJER]);
   }
   else
      pthread_cond_signal(&uv[SMJER]);
   pthread_mutex_unlock(&M);
}

int main(void){

   int br_auta,r;
   srand((unsigned)time(NULL));

   printf("Unesite broj automobila: ");
   scanf("%d",&br_auta);
   
   smjer=malloc(sizeof(int)*br_auta);
   int *BR=malloc(sizeof(int)*br_auta);
									//ima_cekaca=br_auta+1;
   pthread_t thr_id[br_auta];

   printf("Auto:  ");
   for(int i=0; i<br_auta; i++) printf("|%2d",i);
   printf("|\nSmjer: ");
   for(int i=0; i<br_auta; i++){
      r=rand()%2;
      smjer[i]=r;
      printf("|%2d",r);
   }
   printf("|\n\n");

   pthread_mutex_init(&M,NULL);
   pthread_cond_init(&uv,NULL);

   for(int i=0; i<br_auta; i++){
      BR[i]=i;
      if(pthread_create(&thr_id[i],NULL,Auto,(void*)&BR[i]) != 0){
         printf("GRESKA! Ne mogu stvoriti dretvu Auto[%d]\n",i);
         exit(1);
      }
   }

   for(int i=0; i<br_auta; i++){
      pthread_join(thr_id[i],NULL);
   }

   return 0;
}
