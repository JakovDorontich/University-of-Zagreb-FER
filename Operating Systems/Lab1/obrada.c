#include <stdio.h>
#include <signal.h>

#define N 6

int OZNAKA_CEKANJA[N]={0};
int PRIORITET[N]={0};
int TEKUCI_PRIORITET=0;

int sig[]={SIGUSR1, SIGUSR2, SIGTRAP, SIGTTOU, SIGINT};


void zabrani_prekidanje(){
   int i;
   for(i=0; i<N-1; i++)
      sighold(sig[i]);
}

void dozvoli_prekidanje(){
   int i;
   for(i=0; i<N-1; i++)
      sigrelse(sig[i]);
}
 

void obrada_signala(int i){
   /* obrada se simulira trošenjem vremena,
      obrada traje 5 sekundi, ispis treba biti svake sekunde */
   int j;
   switch(i){
      case 1:
         printf("-  P  -  -  -  -\n");
         for(j=1;j<=5;j++) {
            printf("-  %d  -  -  -  -\n",j);
            sleep(1);
            }
         printf("-  K  -  -  -  -\n");
         break;
      case 2:
         printf("-  -  P  -  -  -\n");
         for(j=1;j<=5;j++) {
            printf("-  -  %d  -  -  -\n",j);
            sleep(1);
            }
         printf("-  -  K  -  -  -\n");
         break;
      case 3:
         printf("-  -  -  P  -  -\n");
         for(j=1;j<=5;j++) {
            printf("-  -  -  %d  -  -\n",j);
            sleep(1);
            }
         printf("-  -  -  K  -  -\n");
         break;
      case 4:
         printf("-  -  -  -  P  -\n");
         for(j=1;j<=5;j++) {
            printf("-  -  -  -  %d  -\n",j);
            sleep(1);
            }
         printf("-  -  -  -  K  -\n");
         break;
      case 5:
         printf("-  -  -  -  -  P\n");
         for(j=1;j<=5;j++) {
            printf("-  -  -  -  -  %d\n",j);
            sleep(1);
            }
         printf("-  -  -  -  -  K\n");
         break;
   }

}

void prekidna_rutina(int sig){
   int n=-1;
   int j,x=0;
   zabrani_prekidanje();
   switch(sig){
      case SIGUSR1:
         n=1; 
         printf("-  X  -  -  -  -\n");
         break;
      case SIGUSR2:
         n=2; 
         printf("-  -  X  -  -  -\n");
         break;
      case SIGTRAP:
         n=3; 
         printf("-  -  -  X  -  -\n");
         break;
      case SIGTTOU:
         n=4; 
         printf("-  -  -  -  X  -\n");
         break;
      case SIGINT:
         n=5; 
         printf("-  -  -  -  -  X\n");
         break;
   }

   OZNAKA_CEKANJA[n]++;
   do {
      /* odredi signal najveceg prioriteta koji ceka na obradu */
      x = 0;
      for(j = TEKUCI_PRIORITET + 1;j < N ; j++){
         if(OZNAKA_CEKANJA[j] == 1) {
            x = j;
         }
      }
       
      /* ako postoji signal koji ceka i prioritetniji je od trenutnog posla, idi u obradu */
      if( x > 0 ) {
         OZNAKA_CEKANJA[x]=0;
         PRIORITET[x] = TEKUCI_PRIORITET;
         TEKUCI_PRIORITET = x ;
         dozvoli_prekidanje();
         obrada_signala(x);
         zabrani_prekidanje();
         TEKUCI_PRIORITET = PRIORITET[x];
      }
   }while(x > 0);

   dozvoli_prekidanje();

}

 

int main ( void )

{

   sigset (SIGUSR1, prekidna_rutina);
   sigset (SIGUSR2, prekidna_rutina);
   sigset (SIGTRAP, prekidna_rutina);
   sigset (SIGTTOU, prekidna_rutina);
   sigset (SIGINT, prekidna_rutina);

 

   printf("Proces obrade prekida, PID=%ld\n", getpid());
   printf("G 1 2 3 4 5\n");
   printf("-----------\n");
   printf("1 - - - - -\n");
   printf("2 - - - - -\n");
   printf("3 - - - - -\n");
   printf("4 - - - - -\n");
   printf("GP S1 S2 S3 S4 S5\n");
   printf("-----------------\n");

   for(int i = 0;i<50;i++){
      printf("%d  -  -  -  -  -\n", i );
      sleep(1);
   }

 
   printf ("Zavrsio osnovni program\n");

   return 0;

}


