#include <stdio.h>
#include <signal.h>
#include <stdlib.h>
#include <time.h>

int pid=0;
int sig[]={0, SIGUSR1, SIGUSR2, SIGTRAP, SIGTTOU};

void prekidna_rutina(int sig){

   kill(pid,SIGKILL);
   exit(0);
}

int main(int argc, char *argv[]){
   pid=atoi(argv[1]);
   int posalji;
   char odabir;
   sigset(SIGINT, prekidna_rutina);
   srand((unsigned)time(NULL));
   
   printf("Zelite li random generiranje signala? Y/N:\n");
   scanf("%c",&odabir);

   if ((odabir == 'Y') || (odabir == 'y')){
      while(1){
      posalji = rand()%(4-1+1)+1;
      printf("Saljem sig[%d]\n",posalji);
      kill(pid,sig[posalji]);
      sleep(3);
      }
   }
   else{
      printf("Unesite broj signala 1-4: \n");
      while(1){
         scanf("%d",&posalji);
         if((posalji<1)||(posalji>4)) break;
         printf("Saljem sig[%d]\n",posalji);
         kill(pid,sig[posalji]);
         sleep(3);
      }
   }
   end:
   return 0;
}
