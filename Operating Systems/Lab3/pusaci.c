#include <stdio.h>
#include <stdlib.h>
#include <pthread.h>
#include <time.h>
#include <semaphore.h>

int Sastojci[3]={0,0,0}; //duhan[0],papir[1],sibice[2]
sem_t stol_prazan, duhan_papir, papir_sibice, duhan_sibice;

void *Trgovac(){
   while(1){
      sem_wait(&stol_prazan);
      odaberi_sastojke();

      if(Sastojci[0]==1 && Sastojci[1]==1 && Sastojci[2]==0){
         printf("Trgovac stavlja: duhan i papir\n");
         sem_post(&duhan_papir);
      }
      if(Sastojci[0]==0 && Sastojci[1]==1 && Sastojci[2]==1){
         printf("Trgovac stavlja: papir i sibice\n");
         sem_post(&papir_sibice);
      }
      if(Sastojci[0]==1 && Sastojci[1]==0 && Sastojci[2]==1){
         printf("Trgovac stavlja: duhan i sibice\n");
         sem_post(&duhan_sibice);
      }
   }
}

void *Pusac_s_duhanom(){
   while(1){
      sem_wait(&papir_sibice);
      Sastojci[1]=0;
      Sastojci[2]=0;
      printf("Pusac 1: uzima sastojke i ide pusiti\n\n");
      sem_post(&stol_prazan);
      sleep(rand()%5+1);
   }
}

void *Pusac_s_papirom(){
   while(1){
      sem_wait(&duhan_sibice);
      Sastojci[0]=0;
      Sastojci[2]=0;
      printf("Pusac 2: uzima sastojke i ide pusiti\n\n");
      sem_post(&stol_prazan);
      sleep(rand()%5+1);
   }
}

void *Pusac_s_sibicama(){
   while(1){
      sem_wait(&duhan_papir);
      Sastojci[0]=0;
      Sastojci[1]=0;
      printf("Pusac 3: uzima sastojke i ide pusiti\n\n");
      sem_post(&stol_prazan);
      sleep(rand()%5+1);
   }
}

void odaberi_sastojke(){
   int prvi,drugi;
   prvi=rand()%3;
   while(1){
      drugi=rand()%3;
      if(drugi != prvi){
         break;
      }
   }
   Sastojci[prvi]=1;
   Sastojci[drugi]=1;
}

int main (void){

   pthread_t thr_id[4];
   srand((unsigned)time(NULL));
   printf("Pusac 1: ima duhan\n");
   printf("Pusac 2: ima papir\n");
   printf("Pusac 3: ima sibice\n");
   
   sem_init(&stol_prazan,0,1);
   sem_init(&duhan_papir,0,0);
   sem_init(&papir_sibice,0,0);
   sem_init(&duhan_sibice,0,0);

   if (pthread_create(&thr_id[0],NULL,Trgovac,NULL) != 0){
      printf("GRESKA! Ne mogu stvoriti dretvu: Trgovac.\n");
      exit(1);
   }
   if (pthread_create(&thr_id[1],NULL,Pusac_s_duhanom,NULL) != 0){
      printf("GRESKA! Ne mogu stvoriti dretvu: Pusac_s_duhanom.\n");
      exit(2);
   }
   if (pthread_create(&thr_id[2],NULL,Pusac_s_papirom,NULL) != 0){
      printf("GRESKA! Ne mogu stvoriti dretvu: Pusac_s_papirom.\n");
      exit(3);
   }
   if (pthread_create(&thr_id[3],NULL,Pusac_s_sibicama,NULL) != 0){
      printf("GRESKA! Ne mogu stvoriti dretvu: Pusac_s_sibicama.\n");
      exit(4);
   }
   
   for(int i=0; i<4; i++){
      pthread_join(thr_id[i],NULL);
   }
   
   return 0;
}
