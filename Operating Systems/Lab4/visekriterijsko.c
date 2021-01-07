//************************JAKOV DORONTIC************************
#include <stdio.h>
#include <time.h>

#define MAX_DRETVI 5
#define TIME 50

#define BLUE    "\x1b[34m"
#define END     "\x1b[0m"

struct dretva {
   int id;
   int dolazak;
   int preostalo;
   int prioritet;
};

struct dretva P[MAX_DRETVI]; //pokazivac na strukture
int uzorak[MAX_DRETVI], pom_polje[MAX_DRETVI]={0};
int brojac=0;

void generiraj_nove_podatke(){
   for(int i=0; i<MAX_DRETVI; i++){
      P[i].id = i+1;
      P[i].dolazak = rand()%10;
      P[i].preostalo = rand()%4+2;
      P[i].prioritet = rand()%3+1;
   }
//ispis-----------------------------
   printf("|Dretva|Dolazak|Trajanje|Prioritet|\n");
   for(int i=0; i<MAX_DRETVI; i++){
      printf("|  %2d  |",P[i].id);
      printf("   %2d  |",P[i].dolazak);
      printf("   %2d   |",P[i].preostalo);
      printf("    %2d   |",P[i].prioritet);
      printf("\n");
   }
   printf("\n");
}

void nacrtaj_tablicu(){
   printf("  t     AKT");
   for(int i=1; i<MAX_DRETVI; i++)
      printf("       PR%d",i);
   printf("\n");
}

void init_pom_polje(){
   for(int i=0; i<MAX_DRETVI; i++)
      pom_polje[i]=-1;
}

void postavi_pom_polje(){
   int a=0;

   for(int p=3; p>=1; p--){
      for(int i=0; i<brojac; i++){
         if(P[uzorak[i]].prioritet==p && P[uzorak[i]].preostalo>0){
            pom_polje[a]=uzorak[i];
            a++;
         }
      }
   }
}



int main(void){
   srand((unsigned)time(NULL));

   printf("Simulacija raspoređivanja dretvi: SCHED_FIFO\n\n");
   generiraj_nove_podatke();
   nacrtaj_tablicu();
   init_pom_polje();

   for(int t=0;t<=TIME;t++){
      for(int i=0; i<MAX_DRETVI; i++){
         if(P[i].dolazak==t){
            printf(BLUE" %2d -- nova dretva id=%d, dolazak=%d, trajanje=%d, prioritet=%d\n"END,
                                                                  t,P[i].id,P[i].dolazak,P[i].preostalo,P[i].prioritet);
            uzorak[brojac]=i;
            brojac++;
         }
      }
      postavi_pom_polje();
      printf("%3d ",t);
      for(int i=0; i<MAX_DRETVI; i++){
         if(pom_polje[i]!=-1){
            printf("  %d/%d/%d/%d ",P[pom_polje[i]].id,P[pom_polje[i]].dolazak,P[pom_polje[i]].preostalo,P[pom_polje[i]].prioritet);
         }
         else{
            printf("  -/-/-/- ");
         }
      }
      printf("\n");
      P[pom_polje[0]].preostalo--;
      init_pom_polje();
   }

   return 0;
}


