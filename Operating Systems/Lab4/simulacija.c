//************************JAKOV DORONTIC************************
#include <stdio.h>
#include <time.h>

#define MAX_DRETVI 5
#define TIME 50

#define BLUE    "\x1b[34m"
#define END     "\x1b[0m"

int Z;
struct dretva {
   int id;
   int dolazak;
   int preostalo;
};

typedef struct{
   int ulaz;
   int izlaz;
   int polje[MAX_DRETVI+1];
}Red;
int dodaj (int element, Red *red);
int skini (int *element, Red *red);

struct dretva P[MAX_DRETVI]; //pokazivac na strukture dretvi



void generiraj_nove_podatke(){
   for(int i=0; i<MAX_DRETVI; i++){
      P[i].id = i+1;
      P[i].dolazak = rand()%10;
      P[i].preostalo = rand()%4+2;
   }
//ispis-----------------------------
   printf("|Dretva|Dolazak|Trajanje|\n");
   for(int i=0; i<MAX_DRETVI; i++){
      printf("|  %2d  |",P[i].id);
      printf("   %2d  |",P[i].dolazak);
      printf("   %2d   |",P[i].preostalo);
      printf("\n");
   }
   printf("\n");
}

void nacrtaj_tablicu(){
   printf("  t    AKT");
   for(int i=1; i<MAX_DRETVI; i++)
      printf("     PR%d",i);
   printf("\n");
}

int main(void){
   
   Red uzorak, pom;
   int x, sam_prva;
   dodaj(x,&pom);skini(&x,&pom);
   dodaj(x,&uzorak);skini(&x,&uzorak);

   srand((unsigned)time(NULL));
   printf("Simulacija raspoređivanja dretvi: Round-robin (RR)\n\n");
   generiraj_nove_podatke();
   nacrtaj_tablicu();

   for(int t=0;t<=TIME;t++){
      for(int i=0; i<MAX_DRETVI; i++){
         if(P[i].dolazak==t){
            dodaj(i,&uzorak);
            printf(BLUE" %2d -- nova dretva id=%d, dolazak=%d, trajanje=%d\n"END,t,P[i].id,P[i].dolazak,P[i].preostalo);
         }
      }
      printf("%3d ",t);
      sam_prva=1;
      Z=1;
      for(int i=0; i<MAX_DRETVI; i++){
         if(skini(&x,&uzorak) == 1){
            printf("  %d/%d/%d ",P[x].id,P[x].dolazak,P[x].preostalo);
            if(sam_prva==1){
               P[x].preostalo--;
               sam_prva=0;
            }
            if(P[x].preostalo != 0) dodaj(x,&pom);
            if(P[x].preostalo == 0) Z=0;
          }
         else{
            printf("  -/-/- ");
         }
      }
      if(Z==1)
         if(skini(&x,&pom)) dodaj(x,&pom);
      while(skini(&x,&pom)) dodaj(x,&uzorak);
      printf("\n");
   }
   
}




int dodaj (int element, Red *red){
   if((red->ulaz+1)%(MAX_DRETVI+1) == red->izlaz)
      return 0;
   red->ulaz++;
   red->ulaz%=(MAX_DRETVI+1);
   red->polje[red->ulaz]=element;
   return 1;
}
int skini (int *element, Red *red){
   if(red->ulaz == red->izlaz)
      return 0;
   red->izlaz++;
   red->izlaz%=(MAX_DRETVI+1);
   *element=red->polje[red->izlaz];
   return 1;
}
