#include <stdio.h>
#include <stdlib.h>
#include <sys/types.h>
#include <sys/ipc.h>
#include <sys/msg.h>
#include <signal.h>
#include <errno.h>
#include <time.h>
#include <string.h>


struct my_msgbuf {
	long mtype;
	char mtext[200];
};

int msqid;


void zatvori_vrtuljak(int failure) {
	if (msgctl(msqid, IPC_RMID, NULL) == -1) {
		printf("GRESKA! Ne mogu obrisati red poruka.\n");
		exit(1);
	}
	printf("\nVrtuljak je zatvoren.\n");
	exit(0);
}


void posjetitelj(int n) {
	struct my_msgbuf buf;
	key_t key;
	char poruka_zelje[]="Zelim se voziti.";
	int r;
	
	srand(time(NULL)+n);
	
	key = getuid();
	
	//Posjetitelj n se zeli 3 puta voziti na vrtuljku
	for (int i=0; i<3; i++) {
		r=(rand()%(3-1))+1;
		sleep(r);
		
		//Posalji vrtuljku tip poruke 1, tj. "Zelim se voziti."
		memcpy(buf.mtext, poruka_zelje, strlen(poruka_zelje)+1);
		buf.mtype = 1;
		if (msgsnd(msqid, (struct msgbuf *)&buf, strlen(poruka_zelje)+1, 0) == -1) {
			printf("GRESKA! Ne mogu poslati poruku.\n");
		}
		
		//Cekaj odgovor koji je tip poruke 2, tj. "Sjedni."
		if (msgrcv(msqid, (struct msgbuf *)&buf, sizeof(buf)-sizeof(long), 2, 0) == -1) {
			printf("GRESKA! Ne mogu procitati poruku.\n");
			exit(1);
		}
		printf("\tSjeo posjetitelj %d.\n", n);
		
		//Cekaj odgovor koji je tip poruke 3, tj. "Ustani."
		if (msgrcv(msqid, (struct msgbuf *)&buf, sizeof(buf)-sizeof(long), 3, 0) == -1) {
			printf("GRESKA! Ne mogu procitati poruku.\n");
			exit(1);
		}
		printf("\tOtisao posjetitelj %d.\n", n);
		
	}
	printf("-> Posjetitelj %d zavrsio.\n", n);
}


void vrtuljak() {
	struct my_msgbuf buf;
	key_t key;
	char poruka_sjedni[]="Sjedni.";
	char poruka_ustani[]="Ustani.";
	int r;
	
	srand((unsigned)time(NULL));
	
	key = getuid();
	
	while (1) {
		//Napuni prazan mjesta na vrtuljku (4 mjesta)
		for (int i=0; i<4; i++) {
			//Citaj tip poruke 1, tj. "Zelim se voziti."
			if (msgrcv(msqid, (struct msgbuf *)&buf, sizeof(buf)-sizeof(long), 1, 0) == -1) {
				printf("GRESKA! Ne mogu procitati poruku.\n");
				exit(1);
			}

			//Odgovori na poruku sa tipom poruke 2, tj. "Sjedni."
			memcpy(buf.mtext, poruka_sjedni, strlen(poruka_sjedni)+1);
			buf.mtype = 2;
			if (msgsnd(msqid, (struct msgbuf *)&buf, strlen(poruka_sjedni)+1, 0) == -1) {
				printf("GRESKA! Ne mogu poslati poruku.\n");
			}
		}
		//Pokreni vrtuljak par sekundi
		sleep(1);
		printf("Pokrenuo vrtuljak.\n");
		r=(rand()%(3-1))+1;
		sleep(r);
		printf("Vrtuljak zaustavljen.\n");
		
		//Posalji 4 poruke tipa 3, tj. "Ustani."
		for (int i=0; i<4; i++) {
			memcpy(buf.mtext, poruka_ustani, strlen(poruka_ustani)+1);
			buf.mtype = 3;
			if (msgsnd(msqid, (struct msgbuf *)&buf, strlen(poruka_ustani)+1, 0) == -1) {
				printf("GRESKA! Ne mogu poslati poruku.\n");
			}
		}
	}
}


int main(void) {
	key_t key;
	
	srand((unsigned)time(NULL));
	
	//Stvori red poruka
	key = getuid();
	if ((msqid = msgget(key, 0600 | IPC_CREAT)) == -1) {
		printf("GRESKA! Ne mogu stvoriti red poruka.\n");
		exit(1);
	}
	
	//Ako korisnik pošalje signal SIGINT obriši red poruka
	sigset(SIGINT, zatvori_vrtuljak);
	
	//Stvori 8 procesa posjetitelja
	for (int n=1; n<=8; n++) {
		switch(fork()) {
			case -1: printf("GRESKA! Ne mogu stvoriti proces.\n");
				 exit(2);
			case  0: posjetitelj(n);
				 exit(0);
			default: break;
		}
	}
	
	//Upravljaj posjetiteljima po uzoru na raspodijeljeni centralizirani protokol
	vrtuljak();
	
	//Pricekaj da svi procesi zavrse
	for (int n=1; n<=8; n++) {
		wait(NULL);
	}
	
	return 0;
}



