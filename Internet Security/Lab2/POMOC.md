# Pomoć za 2. laboratorijsku vježbu

Ukoliko vam je potrebna mala pomoć za prvi zadatak ona se nalazi u nastavku.

Cron je daemon koji služi za izvršavanje komandi u određeno vrijeme.
U ovom primjeru dana mu je skripta `/usr/local/bin/backup` koja se izvršava
svake pete minute u svakom satu (0,5,10,...).
Pregledajte sadržaj te skripte:

```
    $ cat /usr/local/bin/backup
```

To je bash skripta. Na početku skripte definira se datoteka koja se
prosljeđuje naredbi `source`. Ukoliko takva skripta postoji
(`if [ -f $script ]; then`) onda se njezin sadržaj učitava s naredbom `source`.
Vaš zadatak je stvoriti datoteku `/tmp/smth` s naredbom koja će korisnika `sui`
dodati u grupu `sudo`. To se može pomoću naredbe usermod`.

Morate navesti puni put do naredbe `usermod` da bi se ona pravilno izvela.
Sačekajte da se skripta izvede. Zatim se odlogirajte i ulogirajte da bi
vam se osvježilo članstvo u grupi `sudo`.

S naredbom:
```
    $ sudo su
    [sudo] password for sui: (Unesete lozinku korisnika sui: Internet1)
```
Treba vam se pojaviti sljedeće:
```
    root@sui:/home/sui#
```
Sada možete s naredbom `reboot` restartati virtualni stroj:
```
    # reboot
```

Naredbe `echo` služe samo za ispisivanje podatka u datoteku `/tmp/backup.log`.

P.S. Kad se skripta izvede u terminalu će vam se ispisati sljedeća poruka:
```
You have new mail in /var/mail/sui
```
Mail možete provjeriti s naredbom `mail`:
```
    $ mail
```
Stisnite Enter da bi vam se učitao zadnji mail. U njemu trebate pronaći
sljedeće linije koje će vam potvrditi da je korisnik dodan u grupu `sudo`:
```
    Adding user `sui' to group `sudo' ...
    Adding user sui to group sudo
    Done.
```
Ako se nešto krivo napravili ispisati će vam se greška. npr.
```
    /tmp/smth: line 1: usermod: command not found
```
Ova poruka se javlja ako niste definirali puni put do naredbe `usermod`.
